package com.buyerologie.video.model;

import java.sql.Timestamp;

public class PlayList {
    private long      id;

    private String    title;

    private Timestamp gmtCreated;

    private Timestamp gmtModified;

    public PlayList() {
    }

    public PlayList(PlayList playList) {
        this.id = playList.getId();
        this.title = playList.getTitle();
    }

    public PlayList(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
}