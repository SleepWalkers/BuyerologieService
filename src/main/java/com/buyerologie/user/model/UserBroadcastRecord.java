package com.buyerologie.user.model;

import java.sql.Timestamp;

public class UserBroadcastRecord {
    private int    id;

    private int    userId;

    private String videoId;

    public UserBroadcastRecord() {
    }

    public UserBroadcastRecord(int userId, String videoId) {
        this.userId = userId;
        this.videoId = videoId;
    }

    private Timestamp gmtCreated;

    private Timestamp gmtModified;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}