package com.spider.service.impl.houses;

import com.spider.entity.Floor;
import com.spider.entity.Houses;
import com.spider.service.houses.IHousesService;
import com.spider.service.impl.system.SpiderProgressServiceImpl;
import com.spider.utils.AnalysisHouseUtil;
import com.spider.utils.SysConstant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class HousesServiceImpl implements IHousesService {

    private SpiderProgressServiceImpl progressService = new SpiderProgressServiceImpl();

    private FloorServiceImpl floorService = new FloorServiceImpl();


    /**
     * 返回当前页的楼盘数据
     */
    @Override
    public List<Houses> getListByPage(int housesNumber) throws IOException {

        String sfwUrl = new SysConstant().HOUSES_LIST_URL + "/b9"+housesNumber;
        List<Houses> housesList = new ArrayList<Houses>();

        Document pageDoc = Jsoup.connect(sfwUrl).timeout(15000).get();  // 承载抓取到的每页房产商DOM数据
        Elements lis = pageDoc.select("#newhouse_loupai_list li");

        for (Element li : lis) {
            if (li.select(".clearfix").size() > 0) {
                try {
                    // 下潜地块数据查询
                    Houses houses = getDetailsByElement(li);
                    String fdcName = houses.getFdcName();

                    // 获取此楼盘的所有地块数据
                    List<Floor> housesFloorList = getFloorListByHousesName(fdcName);
                    houses.setFloorList(housesFloorList);
                    housesList.add(houses);

                    List locationList = new ArrayList();
                    locationList.add(fdcName);
                    progressService.addProgress(
                            "楼盘", "详情", 0,
                            "完成", "", locationList, null
                    );
                } catch (IOException e) {
                    if (e.toString().indexOf("Read timed out") > -1) {
                        // 错误信息
                        String sfwHousesName = li.select(".nlc_details .nlcd_name a").text();
                        String fdcHousesName = AnalysisHouseUtil.extractValidHousesName(sfwHousesName);
                        String sfwHousesUrl = li.select(".nlc_details .nlcd_name a").attr("href");

                        List locationList = new ArrayList();
                        locationList.add(fdcHousesName);
                        progressService.addProgress(
                                "楼盘", "详情", 0,
                                "超时异常", sfwHousesUrl, locationList, e
                        );


                        // 错误时外部需进行以下操作获取楼盘详情数据
//                        Houses houses = getDetailsByUrl(sfwHousesUrl);
//                        String fdcName = houses.getFdcName();
//                        List<Floor> housesFloorList = getFloorListByHousesName(fdcHousesName);
//                        houses.setFloorList(housesFloorList);
//                        return houses;
                    }
                    e.printStackTrace();
                }
            }
        }

        return housesList;
    }


    /**
     * 根据dom获取此楼盘的详情数据
     * 为了是补全部分数据（根据错误url时无法使用）
     */
    @Override
    public Houses getDetailsByElement(Element li) throws IOException {

        String sfwHousesName = li.select(".nlc_details .nlcd_name a").text();
        String fdcHousesName = AnalysisHouseUtil.extractValidHousesName(sfwHousesName);
        String sfwDetailsUrl = li.select(".nlc_details .nlcd_name a").attr("href");

        List locationList = new ArrayList();
        locationList.add(fdcHousesName);
        progressService.addProgress(
                "楼盘", "详情", 0,
                "开始", "", locationList, null
        );

        Houses houses = getDetailsByUrl(sfwDetailsUrl);
        return houses;
    }

    /**
     * 根据url获取楼盘详情数据（可单独供action调用）
     */
    @Override
    public Houses getDetailsByUrl(String url) throws IOException {

        String sfwUrl = url;
        String name = null;
        String fdcName = null;
        String cover = null;
        String address = null;
        String averagePrice = null;
        String openingDate = null;
        String pRebName = null;

        Document detailedDoc = Jsoup.connect(url).timeout(15000).get();

        // 如果是公寓类型
        if (detailedDoc.select(".inf_left1 strong").text().equals("")) {
            name = detailedDoc.select(".lp-name span").text();
            cover = detailedDoc.select(".banner-img").attr("src");
            address = detailedDoc.select(".lp-type").eq(3).select("i").text();
            averagePrice = detailedDoc.select(".l-price strong").text();
            openingDate = detailedDoc.select(".lp-type").eq(2).select("a").text();
            pRebName = detailedDoc.select("#txt_developer").attr("value").trim();
        } else {
            name = detailedDoc.select(".inf_left1 strong").text();
            cover = detailedDoc.select(".bannerbg_pos a img").attr("src");
            address = detailedDoc.select("#xfdsxq_B04_12 span").text();
            averagePrice = detailedDoc.select(".prib").text();
            openingDate = detailedDoc.select(".kaipan").text();
            pRebName = detailedDoc.select("#txt_developer").attr("value").trim();
        }

        fdcName = AnalysisHouseUtil.extractValidHousesName(name);
        Houses houses = new Houses();
        houses.setName(name);
        houses.setFdcName(fdcName);
        houses.setSfwUrl(sfwUrl);
        houses.setCover(cover);
        houses.setAddress(address);
        houses.setAveragePrice(averagePrice);
        houses.setOpeningDate(openingDate);
        houses.setpRebName(pRebName);
        houses.setHash(name+fdcName+sfwUrl+cover+address+averagePrice+openingDate+pRebName);

        return houses;
    }

    /**
     * 根据楼盘名称获取它的所有地块列表
     */
    @Override
    public List<Floor> getFloorListByHousesName(String fdcName) throws IOException {
        // 此楼盘下所有地块列表
        List<Floor> allFloorList = new ArrayList<Floor>();
        int number = 1;
        boolean isTimedOut = false;

        do {
            List<Floor> pageFloorList = new ArrayList<Floor>();

            // 组织同步信息数据列表
            List locationList = new ArrayList();
            locationList.add(fdcName);

            try {
                progressService.addProgress(
                        "地块", "分页", number,
                        "开始", "", locationList, null
                );

                // 获取此楼盘的第number页数据
                pageFloorList = floorService.getListByPage(fdcName, number);

                progressService.addProgress(
                        "地块", "分页", number,
                        "完成", "", locationList, null
                );
            } catch (IOException e) {
                if (e.toString().indexOf("Read timed out") > -1) {
                    isTimedOut = true;

                    String url = new SysConstant().FLOOR_LIST_URL + "/index_"+number+".shtml?zn=all&pu=all&pn="+fdcName+"&en=";
                    progressService.addProgress(
                            "地块", "分页", number,
                            "超时异常", url, locationList, e
                    );

                    // 错误时外部需进行以下操作获取此楼盘楼盘第number页的列表数据
//                    List<Floor> pageFloorList = floorService.getListByPage(name, number);
//                    return pageFloorList;
                }
                e.printStackTrace();
            }

            // 如果这页数据为空，并且是由爬虫超时导致的，就继续获取第2页
            // 如果第2页数据为空并超时，继续第3页
            // 如果第3页数据为空，但是不是超时导致的，代表获取数据完成，结束循环
            if (pageFloorList.size()==0) {
                if (isTimedOut) {
                    number++;
                } else {
                    number = 0;
                }
            } else {
                number++;
                // 把每页的地块数据添加到全部的地块列表中
                for(Floor floor : pageFloorList) {
                    allFloorList.add(floor);
                }
            }
        } while (number > 0);

        return allFloorList;
    }


}
