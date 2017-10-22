package com.spider.dao;

import com.spider.entity.Reb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangyan on 17/8/26.
 */
public interface RebMapper {
    Reb findByName(String name);
    void insertReb(Reb reb);
    void updateReb(Reb reb);

    List<Reb> select(Map<String, String> rebInfo);
    void insert(Reb reb);
    void update(Reb reb);
}
