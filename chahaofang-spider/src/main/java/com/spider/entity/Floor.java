package com.spider.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by zhangyan on 17/7/19.
 * 地块原型
 */
public class Floor {

    public Floor() {

    }

    private String name = null;  // 地块名称
    private String fdcUrl = null;  // 政府网地块URL
    private String canSold = null;  // 可售套数
    private String address = null;  // 项目地址
    private String county = null;  // 所在区县
    private String scale = null;  // 项目规模
    private String totalPlotsNumber = null;  // 总栋数
    private String property = null;  // 物业公司
    private String pHousesId = null;  // 所属楼盘ID
    private String pHousesName = null;  // 所属楼盘名称
    private String hash = null;  // 内容hash
    private List<Plots> plotsList = new ArrayList<Plots>();  // 下级单元楼列表

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFdcUrl() {
        return fdcUrl;
    }

    public void setFdcUrl(String fdcUrl) {
        this.fdcUrl = fdcUrl;
    }

    public String getCanSold() {
        return canSold;
    }

    public void setCanSold(String canSold) {
        this.canSold = canSold;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getTotalPlotsNumber() {
        return totalPlotsNumber;
    }

    public void setTotalPlotsNumber(String totalPlotsNumber) {
        this.totalPlotsNumber = totalPlotsNumber;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getpHousesId() {
        return pHousesId;
    }

    public void setpHousesId(String pHousesId) {
        this.pHousesId = pHousesId;
    }

    public String getpHousesName() {
        return pHousesName;
    }

    public void setpHousesName(String pHousesName) {
        this.pHousesName = pHousesName;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public List<Plots> getPlotsList() {
        return plotsList;
    }

    public void setPlotsList(List<Plots> plotsList) {
        this.plotsList = plotsList;
    }
}
