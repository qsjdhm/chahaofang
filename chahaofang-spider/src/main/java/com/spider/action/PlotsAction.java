package com.spider.action;

import com.spider.entity.Plots;
import com.spider.service.impl.houses.PlotsServiceImpl;
import com.spider.service.impl.system.SocketServiceImpl;
import com.spider.service.impl.system.SpiderProgressServiceImpl;
import com.spider.service.impl.system.SqlServiceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlotsAction {

    SpiderProgressServiceImpl progressService = new SpiderProgressServiceImpl();
    SqlServiceImpl sqlService = new SqlServiceImpl();
    PlotsServiceImpl plotsService = new PlotsServiceImpl();


    /**
     * 同步单元楼的所有信息
     * syncAllList("中海国际社区B-2地块", "http://www.jnfdc.gov.cn/onsaling/show.shtml?prjno=c4d9a76b-b289-42b5-a65f-c99882645ff6")
     */
    public void syncAllList(String floorName, String floorDetailsUrl) {

        // 1. 循环调用service方法获取数据
        List<Plots> allPlotsList = new ArrayList<Plots>();
        int number = 1;
        boolean isTimedOut = false;

        do {
            List<Plots> pagePlotsList = new ArrayList<Plots>();

            // 组织同步信息数据列表
            List locationList = new ArrayList();
            locationList.add(floorName);

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
                    isTimedOut = true;

                    String url = floorDetailsUrl.replace("show", "show_"+number);
                    progressService.addProgress(
                            "单元楼", "分页", number,
                            "超时异常", url, locationList, e
                    );
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
                for(Plots floor : pagePlotsList) {
                    allPlotsList.add(floor);

                    // 更新入数据库
                    sqlService.updatePlotsList(pagePlotsList);
                }
            }
        } while (number > 0);

        // 2. 每次action结束，关闭socket
        new SocketServiceImpl().closeSocketServer();
    }


    /**
     * 根据页数同步此页单元楼的数据列表
     * syncListByPage("中海国际社区B-2地块", "http://www.jnfdc.gov.cn/onsaling/show.shtml?prjno=c4d9a76b-b289-42b5-a65f-c99882645ff6", 2)
     */
    public void syncListByPage(String floorName, String floorDetailsUrl, int number) {

        List<Plots> plotsList = new ArrayList<Plots>();
        // 组织同步信息数据列表
        List locationList = new ArrayList();
        locationList.add(floorName);

        try {
            progressService.addProgress(
                    "单元楼", "分页", number,
                    "开始", "", locationList, null
            );

            plotsList = plotsService.getListByPage(floorDetailsUrl, number);

            // 更新入数据库
            sqlService.updatePlotsList(plotsList);

            progressService.addProgress(
                    "单元楼", "分页", number,
                    "完成", "", locationList, null
            );
        } catch (IOException e) {
            if (e.toString().indexOf("Read timed out") > -1) {

                String url = floorDetailsUrl.replace("show", "show_"+number);
                progressService.addProgress(
                        "单元楼", "分页", number,
                        "超时异常", url, locationList, e
                );
            }
            e.printStackTrace();
        } finally {
            // 每次action结束，关闭socket
            new SocketServiceImpl().closeSocketServer();
        }
    }


    /**
     * 根据某一个的url同步此单元楼的所有信息
     * syncDetailsByUrl("中海国际社区B-2地块", "67楼", "http://www.jnfdc.gov.cn/onsaling/bshow.shtml?bno=23c5bfad-f26f-4f8f-b1db-cf15b8a9e1ac")
     */
    public void syncDetailsByUrl(String floorName, String fdcPlotsName, String url) {

        Plots plots = new Plots();
        List locationList = new ArrayList();
        locationList.add(floorName);
        locationList.add(fdcPlotsName);

        try {
            progressService.addProgress(
                    "单元楼", "详情", 0,
                    "开始", "", locationList, null
            );

            plots = plotsService.getDetailsByUrl(url);
            plots.setpFloorName(floorName);

            // 更新入数据库
            sqlService.updatePlots(plots);

            progressService.addProgress(
                    "单元楼", "详情", 0,
                    "完成", "", locationList, null
            );
        } catch (IOException e) {
            if (e.toString().indexOf("Read timed out") > -1) {

                progressService.addProgress(
                        "单元楼", "详情", 0,
                        "超时异常", url, locationList, null
                );
            }
            e.printStackTrace();
        } finally {
            // 每次action结束，关闭socket
            new SocketServiceImpl().closeSocketServer();
        }
    }

}
