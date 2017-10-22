package com.spider.service.impl.houses;

import com.spider.action.HousesAction;
import com.spider.entity.Floor;
import com.spider.entity.Houses;
import com.spider.entity.Plots;
import com.spider.service.houses.IFloorService;
import com.spider.service.impl.system.SpiderProgressServiceImpl;
import com.spider.utils.AnalysisHouseUtil;
import com.spider.utils.SetHash;
import com.spider.utils.SysConstant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FloorServiceImpl implements IFloorService {

    private SpiderProgressServiceImpl progressService = new SpiderProgressServiceImpl();
    PlotsServiceImpl plotsService = new PlotsServiceImpl();


    /**
     * 根据楼盘名称获取number页的地块列表
     */
    @Override
    public List<Floor> getListByPage(String fdcName, int number) throws IOException {

        String fdcUrl = new SysConstant().FLOOR_LIST_URL + "/index_"+number+".shtml?zn=all&pu=all&pn="+fdcName+"&en=";
        List<Floor> floorList = new ArrayList<Floor>();

        Document pageDoc = Jsoup.connect(fdcUrl).timeout(5000).get();
        Elements trs = pageDoc.select(".project_table tr");

        for (Element tr : trs) {
            // 只获取有效tr里面的数据
            if (tr.select("td").size() > 1) {
                try {
                    Floor floor = getDetailsByElement(fdcName, tr);
                    floor.setpHousesName(fdcName);
                    floor.setHash(new SetHash().setFloorHash(floor));

                    String floorName = floor.getName();
                    String floorDetailsUrl = floor.getFdcUrl();
                    List<Plots> floorPlotsList = getPlotsListByFloorDetailsUrl(floorName, floorDetailsUrl);
                    floor.setPlotsList(floorPlotsList);
                    floorList.add(floor);

                    List locationList = new ArrayList();
                    locationList.add(fdcName);
                    locationList.add(floor.getName());
                    progressService.addProgress(
                            "地块", "详情", 0,
                            "完成", "", locationList, null
                    );
                } catch (IOException e) {
                    if (e.toString().indexOf("Read timed out") > -1) {
                        // 错误信息
                        Elements tds = tr.select("td");
                        String fdcFloorName = tds.eq(1).attr("title");  // 地块名称
                        String fdcFloorUrl = new SysConstant().FLOOR_DETAILS_URL + tds.eq(1).select("a").attr("href");  // 地块详情页面政府网URL

                        List locationList = new ArrayList();
                        locationList.add(fdcName);
                        locationList.add(fdcFloorName);
                        progressService.addProgress(
                                "地块", "详情", 0,
                                "超时异常", fdcFloorUrl, locationList, e
                        );

                        // 错误时外部需进行以下操作获取此地块详情数据
//                        Floor floor = getDetailsByUrl(fdcFloorUrl);
//                        floor.setpHousesName(fdcName);
//                        List<Plots> floorPlotsList = getPlotsListByBaseUrl(floor.getFdcUrl());
//                        floor.setPlotsList(floorPlotsList);
//                        return floor;
                    }
                    e.printStackTrace();
                }
            }
        }

        return floorList;
    }

    /**
     * 根据dom获取此地块的详情数据
     * 为了是补全部分数据（根据错误url时无法使用）
     */
    @Override
    public Floor getDetailsByElement(String fdcName, Element tr) throws IOException {

        Elements tds = tr.select("td");
        String fdcFloorName = tds.eq(1).attr("title");  // 地块名称
        String fdcDetailsUrl = new SysConstant().FLOOR_DETAILS_URL + tds.eq(1).select("a").attr("href");  // 地块详情页面政府网URL

        List locationList = new ArrayList();
        locationList.add(fdcName);
        locationList.add(fdcFloorName);
        progressService.addProgress(
                "地块", "详情", 0,
                "开始", "", locationList, null
        );

        Floor floor = getDetailsByUrl(fdcDetailsUrl);
        return floor;
    }

    /**
     * 根据url获取地块详情数据（可单独供action调用）
     */
    @Override
    public Floor getDetailsByUrl(String url) throws IOException {

        Document detailedDoc = Jsoup.connect(url).timeout(5000).get();
        Elements trs = detailedDoc.select(".message_table tr");

        String name = trs.eq(1).select("td").eq(1).text();
        String fdcUrl = url;  // 地块详情页面政府网URL
        String canSold = null;  // 可售套数
        String address = trs.eq(1).select("td").eq(3).text();
        String county = trs.eq(2).select("td").eq(3).text();
        String scale = trs.eq(3).select("td").eq(1).text();
        String totalPlotsNumber = trs.eq(3).select("td").eq(3).text();
        String property = trs.eq(5).select("td").eq(1).text();

        Floor floor = new Floor();
        floor.setName(name);
        floor.setFdcUrl(fdcUrl);
        floor.setCanSold(canSold);
        floor.setAddress(address);
        floor.setCounty(county);
        floor.setScale(scale);
        floor.setTotalPlotsNumber(totalPlotsNumber);
        floor.setProperty(property);
        floor.setHash(name+fdcUrl+canSold+address+county+scale+totalPlotsNumber+property);

        return floor;
    }


    /**
     * 根据地块详情URL获取它的所有楼盘列表
     */
    @Override
    public List<Plots> getPlotsListByFloorDetailsUrl(String floorName, String floorDetailsUrl) throws IOException {

        // 此地块下的所有单元楼列表
        List<Plots> allPlotsList = new ArrayList<Plots>();
        int number = 1;
        boolean isTimedOut = false;

        // 组织同步信息数据列表
        List locationList = new ArrayList();
        locationList.add(floorName);

        do {
            List<Plots> pagePlotsList = new ArrayList<Plots>();

            try {
                progressService.addProgress(
                        "单元楼", "分页", number,
                        "开始", "", locationList, null
                );

                pagePlotsList = plotsService.getListByPage(floorDetailsUrl, number);

                progressService.addProgress(
                        "单元楼", "分页", number,
                        "完成", "", locationList, null
                );
            } catch (IOException e) {
                if (e.toString().indexOf("Read timed out") > -1) {
                    // 判断e是超时异常，把isError设为true，表示这页数据为0是由超时异常导致的
                    isTimedOut = true;

                    String url = floorDetailsUrl.replace("show", "show_"+number);
                    progressService.addProgress(
                            "单元楼", "分页", number,
                            "超时异常", url, locationList, e
                    );


                    // 错误时外部需进行以下操作获取此楼盘楼盘第number页的列表数据
//                    List<Plots> pagePlotsList = plotsService.getListByPage(floorDetailsUrl, number);
//                    return pagePlotsList;
                }

                e.printStackTrace();
            }

            // 如果这页数据为空，并且是由爬虫超时导致的，就继续获取第2页
            // 如果第2页数据为空并超时，继续第3页
            // 如果第3页数据为空，但是不是超时导致的，代表获取数据完成，结束循环
            if (pagePlotsList.size()==0) {
                if (isTimedOut) {
                    number++;
                } else {
                    number = 0;
                }
            } else {
                number++;
                // 把每页的地块数据添加到全部的地块列表中
                for(Plots plots : pagePlotsList) {
                    allPlotsList.add(plots);
                }
            }
        } while (number > 0);

        return allPlotsList;
    }



}
