package com.buyerologie.user.model;

import java.sql.Timestamp;

public class VipDetail {

    private int       id;

    private int       userId;

    private Timestamp startTime;

    private Timestamp endTime;

    private String    sourceOrder;

    private Timestamp gmtCreated;

    private Timestamp gmtModified;

    public VipDetail() {
    }

    public VipDetail(VipDetail vipDetail) {
        this.id = vipDetail.getId();
        this.userId = vipDetail.getUserId();
        this.startTime = vipDetail.getStartTime();
        this.endTime = vipDetail.getEndTime();
        this.sourceOrder = vipDetail.getSourceOrder();
        this.gmtCreated = vipDetail.getGmtCreated();
        this.gmtModified = vipDetail.getGmtModified();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public String getSourceOrder() {
        return sourceOrder;
    }

    public void setSourceOrder(String sourceOrder) {
        this.sourceOrder = sourceOrder;
    }

    public Timestamp getGmtCreated() {
        return gmtCreated;
    }

    public void setGmtCreated(Timestamp gmtCreated) {
        this.gmtCreated = gmtCreated;
    }

    public Timestamp getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Timestamp gmtModified) {
        this.gmtModified = gmtModified;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}