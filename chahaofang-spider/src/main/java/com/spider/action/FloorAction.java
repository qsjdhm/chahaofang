package com.spider.action;

import com.spider.entity.Floor;
import com.spider.entity.Plots;
import com.spider.service.impl.houses.FloorServiceImpl;
import com.spider.service.impl.system.SocketServiceImpl;
import com.spider.service.impl.system.SpiderProgressServiceImpl;
import com.spider.service.impl.system.SqlServiceImpl;
import com.spider.utils.SetHash;
import com.spider.utils.SysConstant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FloorAction {

    SpiderProgressServiceImpl progressService = new SpiderProgressServiceImpl();
    SqlServiceImpl sqlService = new SqlServiceImpl();
    FloorServiceImpl floorService = new FloorServiceImpl();


    /**
     * 同步地块的地块的所有信息
     * syncAllList("中海国际社区")
     */
    public void syncAllList(String housesName) {

        // 1. 循环调用service方法获取数据
        List<Floor> allFloorList = new ArrayList<Floor>();
        int number = 1;
        boolean isTimedOut = false;

        do {
            List<Floor> pageFloorList = new ArrayList<Floor>();

            // 组织同步信息数据列表
            List locationList = new ArrayList();
            locationList.add(housesName);

            try {
                progressService.addProgress(
                        "地块", "分页", number,
                        "开始", "", locationList, null
                );

                pageFloorList = floorService.getListByPage(housesName, number);

                progressService.addProgress(
                        "地块", "分页", number,
                        "完成", "", locationList, null
                );
            } catch (IOException e) {
                if (e.toString().indexOf("Read timed out") > -1) {
                    isTimedOut = true;

                    String url = new SysConstant().FLOOR_LIST_URL + "/index_"+number+".shtml?zn=all&pu=all&pn="+housesName+"&en=";
                    progressService.addProgress(
                            "地块", "分页", number,
                            "超时异常", url, locationList, e
                    );
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

                    // 更新入数据库
                    sqlService.updateFloor(floor);
                    List<Plots> floorPlotsList = floor.getPlotsList();
                    sqlService.updatePlotsList(floorPlotsList);
                }
            }
        } while (number > 0);

        // 每次action结束，关闭socket
        new SocketServiceImpl().closeSocketServer();
    }


    /**
     * 根据页数同步此页地块的数据列表
     * syncListByPage("中海国际社区", 1)
     */
    public void syncListByPage(String fdcName, int number) {

        List<Floor> floorList = new ArrayList<Floor>();

        // 组织同步信息数据列表
        List locationList = new ArrayList();
        locationList.add(fdcName);

        try {
            progressService.addProgress(
                    "地块", "分页", number,
                    "开始", "", locationList, null
            );

            floorList = floorService.getListByPage(fdcName, number);

            // 更新入数据库
            for(Floor floor : floorList) {
                sqlService.updateFloor(floor);
                List<Plots> floorPlotsList = floor.getPlotsList();
                sqlService.updatePlotsList(floorPlotsList);
            }

            progressService.addProgress(
                    "地块", "分页", number,
                    "完成", "", locationList, null
            );
        } catch (IOException e) {
            if (e.toString().indexOf("Read timed out") > -1) {

                String url = new SysConstant().FLOOR_LIST_URL + "/index_"+number+".shtml?zn=all&pu=all&pn="+fdcName+"&en=";
                progressService.addProgress(
                        "地块", "分页", number,
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
     * 根据某一个的url同步此地块的所有信息
     * syncDetailsByUrl("中海国际社区", "中海国际社区B-2地块", "http://www.jnfdc.gov.cn/onsaling/show.shtml?prjno=c4d9a76b-b289-42b5-a65f-c99882645ff6"")
     */
    public void syncDetailsByUrl(String fdcName, String fdcFloorName, String url) {

        Floor floor = new Floor();
        List locationList = new ArrayList();
        locationList.add(fdcName);
        locationList.add(fdcFloorName);

        try {
            progressService.addProgress(
                    "地块", "详情", 0,
                    "开始", "", locationList, null
            );

            floor = floorService.getDetailsByUrl(url);
            floor.setpHousesName(fdcName);
            // 因为存在单独同步一个地块数据情况，所以要考虑在action中设置地块的pHousesName
            // 所以在action中需要重新根据每个字段的name重新设置hash值
            floor.setHash(new SetHash().setFloorHash(floor));

            List<Plots> floorPlotsList = floorService.getPlotsListByFloorDetailsUrl(fdcFloorName, url);
            floor.setPlotsList(floorPlotsList);

            // 更新入数据库
            sqlService.updateFloor(floor);
            sqlService.updatePlotsList(floorPlotsList);

            progressService.addProgress(
                    "地块", "详情", 0,
                    "完成", "", locationList, null
            );
        } catch (IOException e) {
            if (e.toString().indexOf("Read timed out") > -1) {

                progressService.addProgress(
                        "地块", "详情", 0,
                        "超时异常", url, locationList, e
                );
            }
            e.printStackTrace();
        } finally {
            // 每次action结束，关闭socket
            new SocketServiceImpl().closeSocketServer();
        }
    }


}
