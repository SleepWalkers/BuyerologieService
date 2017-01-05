package com.buyerologie.video.model;

import java.sql.Timestamp;
import java.util.Date;

public class Video {
    private String    id;

    private String    title;

    private Date      duration;

    private Timestamp gmtCreated;

    private Timestamp gmtModified;

    public Video() {
    }

    public Video(Video video) {
        this.id = video.getId();
        this.title = video.getTitle();
        this.duration = video.getDuration();
    }

    public Video(String id, String title, Date duration) {
        this.id = id;
        this.title = title;
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDuration() {
        return duration;
    }

    public void setDuration(Date duration) {
        this.duration = duration;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}