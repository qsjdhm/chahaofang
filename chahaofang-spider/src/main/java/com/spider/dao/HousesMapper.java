package com.spider.dao;

import com.spider.entity.Houses;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangyan on 17/8/30.
 */
public interface HousesMapper {
    Houses findByName(String name);
    void insertHouses(Houses houses);
    void updateHouses(Houses houses);

    List<Houses> select(Map<String, String> housesInfo);
    void insert(Houses houses);
    void update(Houses houses);
}
