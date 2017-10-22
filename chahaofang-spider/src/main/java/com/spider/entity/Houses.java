package com.spider.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by zhangyan on 17/7/19.
 * 楼盘原型
 */
public class Houses {

    public Houses() {

    }

    private String name = null;  // 楼盘名称
    private String fdcName = null;  // 政府网查询时用的楼盘名称
    private String sfwUrl = null;  // 搜房网URL
    private String cover = null;  // 封面
    private String address = null;  // 地址
    private String averagePrice = null;  // 均价
    private String openingDate = null;  // 开盘日期
    private String pRebName = null;  // 所属房产商名称
    private String hash = null;  // 内容hash
    private List<Floor> floorList = new ArrayList<Floor>();  // 下级地块列表

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFdcName() {
        return fdcName;
    }

    public void setFdcName(String fdcName) {
        this.fdcName = fdcName;
    }

    public String getSfwUrl() {
        return sfwUrl;
    }

    public void setSfwUrl(String sfwUrl) {
        this.sfwUrl = sfwUrl;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(String averagePrice) {
        this.averagePrice = averagePrice;
    }

    public String getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(String openingDate) {
        this.openingDate = openingDate;
    }

    public String getpRebName() {
        return pRebName;
    }

    public void setpRebName(String pRebName) {
        this.pRebName = pRebName;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public List<Floor> getFloorList() {
        return floorList;
    }

    public void setFloorList(List<Floor> floorList) {
        this.floorList = floorList;
    }
}
