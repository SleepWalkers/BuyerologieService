package com.buyerologie.topic.model;

import java.sql.Timestamp;

import com.buyerologie.topic.vo.Course;
import com.buyerologie.topic.vo.DetailedNews;

public class TopicTitle {
    private int       id;

    private int       creatorId;

    private int       cateId;

    private String    title;

    private boolean   isFree;

    private boolean   isDelete;

    private Timestamp gmtCreated;

    private Timestamp gmtModified;

    public TopicTitle() {
    }

    public TopicTitle(Course course) {
        this.id = course.getId();
        this.creatorId = course.getCreatorId();
        this.title = course.getTitle();
        this.isFree = course.getIsFree();
        this.cateId = 2;
    }

    public TopicTitle(DetailedNews detailedNews) {
        this.id = detailedNews.getId();
        this.creatorId = detailedNews.getCreatorId();
        this.title = detailedNews.getTitle();
        this.isFree = true;
        this.cateId = 1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public int getCateId() {
        return cateId;
    }

    public void setCateId(int cateId) {
        this.cateId = cateId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
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

    public boolean getIsFree() {
        return isFree;
    }

    public void setIsFree(boolean isFree) {
        this.isFree = isFree;
    }

}