package com.spider.action;

import com.spider.entity.Floor;
import com.spider.entity.Houses;
import com.spider.entity.Plots;
import com.spider.service.impl.houses.FloorServiceImpl;
import com.spider.service.impl.houses.HousesServiceImpl;
import com.spider.service.impl.system.SocketServiceImpl;
import com.spider.service.impl.system.SpiderProgressServiceImpl;
import com.spider.service.impl.system.SqlServiceImpl;
import com.spider.utils.SysConstant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HousesAction {

    SpiderProgressServiceImpl progressService = new SpiderProgressServiceImpl();
    SqlServiceImpl sqlService = new SqlServiceImpl();
    HousesServiceImpl housesService = new HousesServiceImpl();


    /**
     * 同步楼盘的所有信息
     * syncAllList()
     */
    public void syncAllList() {
        // 1. 循环调用service方法获取数据
        List<Houses> allHousesList = new ArrayList<Houses>();
        int number = 1;
        boolean isTimedOut = false;

        do {
            List<Houses> pageHousesList = new ArrayList<Houses>();
            try {
                progressService.addProgress(
                        "楼盘", "分页", number,
                        "开始", "", new ArrayList(), null
                );

                pageHousesList = housesService.getListByPage(number);

                progressService.addProgress(
                        "楼盘", "分页", number,
                        "完成", "", new ArrayList(), null
                );
            } catch (IOException e) {
                if (e.toString().indexOf("Read timed out") > -1) {
                    isTimedOut = true;

                    String url = new SysConstant().HOUSES_LIST_URL + "/b9"+number;
                    progressService.addProgress(
                            "楼盘", "分页", number,
                            "超时异常", url, new ArrayList(), e
                    );
                }
                e.printStackTrace();
            }

            // 如果这页数据为空，并且是由爬虫超时导致的，就继续获取第2页
            // 如果第2页数据为空并超时，继续第3页
            // 如果第3页数据为空，但是不是超时导致的，代表获取数据完成，结束循环
            if (pageHousesList.size()==0) {
                if (isTimedOut) {
                    number++;
                } else {
                    number = 0;
                }
            } else {
                number++;

                for(Houses houses : pageHousesList) {
                    allHousesList.add(houses);

                    // 更新入数据库
                    sqlService.updateHouses(houses);
                    List<Floor> housesFloorList = houses.getFloorList();
                    for(Floor floor : housesFloorList) {
                        sqlService.updateFloor(floor);
                        List<Plots> floorPlotsList = floor.getPlotsList();
                        sqlService.updatePlotsList(floorPlotsList);
                    }
                }
            }
        } while (number > 0);

        // 每次action结束，关闭socket
        new SocketServiceImpl().closeSocketServer();
    }

    /**
     * 根据页数同步此页楼盘的数据列表
     * syncListByPage(2)
     */
    public void syncListByPage(int number) {

        try {
            progressService.addProgress(
                    "楼盘", "分页", number,
                    "开始", "", new ArrayList(), null
            );

            List<Houses> housesList = housesService.getListByPage(number);

            for(Houses houses : housesList) {
                // 存储单个楼盘数据
                sqlService.updateHouses(houses);

                // 更新入数据库
                List<Floor> housesFloorList = houses.getFloorList();
                for(Floor floor : housesFloorList) {
                    sqlService.updateFloor(floor);
                    List<Plots> floorPlotsList = floor.getPlotsList();
                    sqlService.updatePlotsList(floorPlotsList);
                }
            }

            progressService.addProgress(
                    "楼盘", "分页", number,
                    "完成", "", new ArrayList(), null
            );
        } catch (IOException e) {
            if (e.toString().indexOf("Read timed out") > -1) {

                String url = new SysConstant().HOUSES_LIST_URL + "/b9"+number;
                progressService.addProgress(
                        "楼盘", "分页", number,
                        "超时异常", url, new ArrayList(), e
                );
            }
            e.printStackTrace();
        } finally {
            // 每次action结束，关闭socket
            new SocketServiceImpl().closeSocketServer();
        }
    }

    /**
     * 根据某一个的url同步此个楼盘的所有信息
     * syncDetailsByUrl("中海国际社区", "http://zhonghaiguojishequ0531.fang.com")
     */
    public void syncDetailsByUrl(String name, String url) {

        Houses houses = new Houses();
        List locationList = new ArrayList();
        locationList.add(name);

        try {
            progressService.addProgress(
                    "楼盘", "详情", 0,
                    "开始", "", locationList, null
            );

            houses = housesService.getDetailsByUrl(url);
            // 因为上面只是获取单个楼盘数据，所以需要单独调用下潜的地块数据，并且在获取地块数据接口中，有获取单元楼列表逻辑
            // 所以这里不需要单独获取单元楼数据
            List<Floor> housesFloorList = housesService.getFloorListByHousesName(name);
            houses.setFloorList(housesFloorList);


            // 更新入数据库
            sqlService.updateHouses(houses);
            for(Floor floor : housesFloorList) {
                sqlService.updateFloor(floor);
                List<Plots> floorPlotsList = floor.getPlotsList();
                sqlService.updatePlotsList(floorPlotsList);
            }


            progressService.addProgress(
                    "楼盘", "详情", 0,
                    "完成", "", locationList, null
            );
        } catch (IOException e) {
            if (e.toString().indexOf("Read timed out") > -1) {

                progressService.addProgress(
                        "楼盘", "详情", 0,
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
