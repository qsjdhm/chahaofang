package com.spider.utils;

import com.spider.entity.Floor;
import com.spider.entity.Plots;

/**
 * Created by zhangyan on 17/9/9.
 * 设置地块、单元楼的hash
 *
 * 因为存在单独同步一个地块数据情况，所以要考虑在action中设置地块的pHousesName
 * 所以在action中需要重新根据每个字段的name重新设置hash值，所以多了此工具类来设置
 *
 */
public class SetHash {

    /**
     * 设置地块最新hash
     * return hash
     */
    public static String setFloorHash (Floor floor) {
        String name = floor.getName();
        String fdcUrl = floor.getFdcUrl();
        String canSold = floor.getCanSold();
        String address = floor.getAddress();
        String county = floor.getCounty();
        String scale = floor.getScale();
        String totalPlotsNumber = floor.getTotalPlotsNumber();
        String property = floor.getProperty();
        String pHousesName = floor.getpHousesName();
        String hash = name+fdcUrl+canSold+address+county+scale+totalPlotsNumber+property+pHousesName;

        return hash;
    }

    /**
     * 设置单元楼最新hash
     * return hash
     */
    public static String setPlotsHash (Plots plots) {
        String name = plots.getName();
        String fdcUrl = plots.getFdcUrl();
        String area = plots.getArea();
        String decoration = plots.getDecoration();
        String utilization = plots.getUtilization();
        String mortgage = plots.getMortgage();
        String salePermit = plots.getSalePermit();
        String landUseCertificate = plots.getLandUseCertificate();
        String planningPermit = plots.getPlanningPermit();
        String constructionPermit = plots.getConstructionPermit();
        String pFloorName = plots.getpFloorName();
        String hash = name+fdcUrl+area+decoration+utilization+mortgage+salePermit+landUseCertificate+planningPermit+constructionPermit+pFloorName;

        return hash;

    }
}
