package com.spider.utils;

/**
 * Created by zhangyan on 17/7/16.
 * 解析楼盘的信息，比如名称叫[历下]银盛泰・金域蓝山处理成金域蓝山
 */
public class AnalysisHouseUtil {

    /**
     * 提取有效的楼盘名称
     * return name
     */
    public static String extractValidHousesName (String name) {
        if (name.split("]").length > 1) {
            name = name.split("]")[1];
        }

        if (name.split("・").length > 1) {
            name = name.split("・")[1];
        }
        return name;
    }
}
