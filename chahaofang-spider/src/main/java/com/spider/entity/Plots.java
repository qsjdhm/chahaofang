package com.spider.entity;

import java.util.UUID;

/**
 * Created by zhangyan on 17/7/19.
 * 单元楼原型
 */
public class Plots {

    public Plots() {

    }

    private String name = null;  // 单元楼名称
    private String fdcUrl = null;  // 单元楼页面政府网URL
    private String area = null;  // 建筑面积
    private String decoration = null;  // 装修标准
    private String utilization = null;  // 规划用途
    private String mortgage = null;  // 有无抵押
    private String salePermit = null;  // 商品房预售许可证
    private String landUseCertificate = null;  // 国有土地使用证
    private String planningPermit = null;  // 建设工程规划许可证
    private String constructionPermit = null;  // 建设工程施工许可证
    private String pFloorId = null;  // 所属地块ID
    private String pFloorName = null;  // 所属地块名称
    private String hash = null;  // 内容hash

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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDecoration() {
        return decoration;
    }

    public void setDecoration(String decoration) {
        this.decoration = decoration;
    }

    public String getUtilization() {
        return utilization;
    }

    public void setUtilization(String utilization) {
        this.utilization = utilization;
    }

    public String getMortgage() {
        return mortgage;
    }

    public void setMortgage(String mortgage) {
        this.mortgage = mortgage;
    }

    public String getSalePermit() {
        return salePermit;
    }

    public void setSalePermit(String salePermit) {
        this.salePermit = salePermit;
    }

    public String getLandUseCertificate() {
        return landUseCertificate;
    }

    public void setLandUseCertificate(String landUseCertificate) {
        this.landUseCertificate = landUseCertificate;
    }

    public String getPlanningPermit() {
        return planningPermit;
    }

    public void setPlanningPermit(String planningPermit) {
        this.planningPermit = planningPermit;
    }

    public String getConstructionPermit() {
        return constructionPermit;
    }

    public void setConstructionPermit(String constructionPermit) {
        this.constructionPermit = constructionPermit;
    }

    public String getpFloorId() {
        return pFloorId;
    }

    public void setpFloorId(String pFloorId) {
        this.pFloorId = pFloorId;
    }

    public String getpFloorName() {
        return pFloorName;
    }

    public void setpFloorName(String pFloorName) {
        this.pFloorName = pFloorName;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
