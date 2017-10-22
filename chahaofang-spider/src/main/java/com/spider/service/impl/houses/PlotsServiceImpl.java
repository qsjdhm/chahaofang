package com.spider.service.impl.houses;

import com.spider.entity.Floor;
import com.spider.entity.Plots;
import com.spider.service.houses.IPlotsService;
import com.spider.service.impl.system.SpiderProgressServiceImpl;
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


public class PlotsServiceImpl implements IPlotsService {

    private SpiderProgressServiceImpl progressService = new SpiderProgressServiceImpl();
    Logger logger = LogManager.getLogger(PlotsServiceImpl.class.getName());


    /**
     * 根据地块详情URL获取number页的单元楼列表
     */
    public List<Plots> getListByPage(String url, int number) throws IOException {

        String floorDetailsUrl = url.replace("show", "show_"+number);
        List<Plots> plotsList = new ArrayList<Plots>();

        Document pageDoc = Jsoup.connect(floorDetailsUrl).timeout(5000).get();
        String floorName = pageDoc.select(".message_table tr").eq(1).select("td").eq(1).text();
        Elements trs = pageDoc.select(".project_table tr");

        for (Element tr : trs) {
            // 只获取有效tr里面的数据
            if (tr.select("td").size() > 1) {
                try {
                    Plots plots = getDetailsByElement(floorName, tr);
                    plots.setpFloorName(floorName);
                    plots.setHash(new SetHash().setPlotsHash(plots));

                    plotsList.add(plots);

                    List locationList = new ArrayList();
                    locationList.add(floorName);
                    locationList.add(plots.getName());
                    progressService.addProgress(
                            "单元楼", "详情", 0,
                            "完成", "", locationList, null
                    );
                } catch (IOException e) {
                    if (e.toString().indexOf("Read timed out") > -1) {
                        // 错误信息
                        Elements tds = tr.select("td");
                        String fdcPlotsName = tds.eq(1).attr("title");  // 单元楼名称
                        String fdcPlotsUrl = new SysConstant().FLOOR_DETAILS_URL + tds.eq(1).select("a").attr("href");  // 单元楼页面政府网URL

                        List locationList = new ArrayList();
                        locationList.add(floorName);
                        locationList.add(fdcPlotsName);
                        progressService.addProgress(
                                "单元楼", "详情", 0,
                                "超时异常", fdcPlotsUrl, locationList, e
                        );

                        // 错误时外部需进行以下操作获取此地块详情数据
//                        Plots plots = getDetailsByUrl(fdcPlotsUrl);
//                        plots.setpFloorName(floorName);
//                        return plots;
                    }

                }
            }
        }

        return plotsList;
    }

    /**
     * 根据dom获取此单元楼的详情数据
     * 为了是补全部分数据（根据错误url时无法使用）
     */
    @Override
    public Plots getDetailsByElement(String floorName, Element tr) throws IOException {

        Elements tds = tr.select("td");
        String fdcPlotsName = tds.eq(1).attr("title");  // 单元楼名称
        String fdcDetailsUrl = new SysConstant().PLOTS_DETAILS_URL + "/" + tds.eq(1).select("a").attr("href");  // 单元楼页面政府网URL

        List locationList = new ArrayList();
        locationList.add(floorName);
        locationList.add(fdcPlotsName);
        progressService.addProgress(
                "单元楼", "详情", 0,
                "开始", "", locationList, null
        );

        Plots plots = getDetailsByUrl(fdcDetailsUrl);
        //plots.setName(fdcPlotsName);
        return plots;
    }

    /**
     * 根据url获取单元楼详情数据（可单独供action调用）
     */
    @Override
    public Plots getDetailsByUrl(String url) throws IOException {

        // 根据url继续下潜抓取详细信息
        Document detailedDoc = Jsoup.connect(url).timeout(5000).get();
        Elements trs = detailedDoc.select(".message_table tr");

        String name = trs.eq(1).select("td").eq(1).text();  // 单元楼名称
        String fdcUrl = url;  // 单元楼页面政府网URL
        String area = trs.eq(4).select("td").eq(3).text()+"(万m²)";  // 建筑面积
        String decoration = trs.eq(5).select("td").eq(3).text();  // 装修标准
        String utilization = trs.eq(6).select("td").eq(1).text();  // 规划用途
        String mortgage = trs.eq(6).select("td").eq(3).text();  // 有无抵押
        String salePermit = trs.eq(7).select("td").eq(1).text();  // 商品房预售许可证
        String landUseCertificate = trs.eq(7).select("td").eq(3).text();  // 国有土地使用证
        String planningPermit = trs.eq(8).select("td").eq(1).text();  // 建设工程规划许可证
        String constructionPermit = trs.eq(8).select("td").eq(3).text();  // 建设工程施工许可证
        String hash = name+fdcUrl+area+decoration+utilization+mortgage+salePermit+landUseCertificate+planningPermit+constructionPermit;

        Plots plots = new Plots();
        plots.setName(name);
        plots.setFdcUrl(fdcUrl);
        plots.setArea(area);
        plots.setDecoration(decoration);
        plots.setUtilization(utilization);
        plots.setMortgage(mortgage);
        plots.setSalePermit(salePermit);
        plots.setLandUseCertificate(landUseCertificate);
        plots.setPlanningPermit(planningPermit);
        plots.setConstructionPermit(constructionPermit);
        plots.setHash(hash);

        return plots;
    }



}
