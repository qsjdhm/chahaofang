package com.spider.dao;

import com.spider.entity.Plots;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangyan on 17/8/30.
 */
public interface PlotsMapper {
    Plots findByName(String name);
    List<Plots> findByFloorName(String floorName);
    void insertPlots(Plots plots);
    void updatePlots(Plots plots);


    List<Plots> select(Map<String, String> plotsInfo);
    void insert(Plots plots);
    void update(Plots plots);
}
