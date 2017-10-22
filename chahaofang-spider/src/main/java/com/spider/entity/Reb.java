package com.spider.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyan on 17/7/19.
 * 房产商原型
 */
public class Reb {

    public Reb() {

    }

    private String name = null;  // 房产商名称
    private String fdcUrl = null;  // 政府网URL
    private String qualificationLevel = null;  // 资质等级
    private String qualificationId = null;  // 资质编号
    private String legalPerson = null;  // 法人代表
    private String address = null;  // 企业地址
    private String phone = null;  // 联系电话
    private String mail = null;  // 企业邮箱
    private String registeredCapital = null;  // 注册资金
    private String type = null;  // 企业类型
    private String introduction = null;  // 企业简介
    private String hash = null;  // 内容hash
    private List<Houses> housesList = new ArrayList<Houses>();  // 下级楼盘列表


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

    public String getQualificationLevel() {
        return qualificationLevel;
    }

    public void setQualificationLevel(String qualificationLevel) {
        this.qualificationLevel = qualificationLevel;
    }

    public String getQualificationId() {
        return qualificationId;
    }

    public void setQualificationId(String qualificationId) {
        this.qualificationId = qualificationId;
    }

    public String getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getRegisteredCapital() {
        return registeredCapital;
    }

    public void setRegisteredCapital(String registeredCapital) {
        this.registeredCapital = registeredCapital;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public List<Houses> getHousesList() {
        return housesList;
    }

    public void setHousesList(List<Houses> housesList) {
        this.housesList = housesList;
    }

}
