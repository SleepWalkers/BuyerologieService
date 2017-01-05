package com.buyerologie.topic.model;

import java.sql.Timestamp;

import com.buyerologie.topic.vo.Course;
import com.buyerologie.topic.vo.DetailedNews;

public class TopicContent {
    private int       id;

    private int       titleId;

    private int       parentId;

    private int       replyNum;

    private int       creatorId;

    private String    content;

    private String    videoId;

    private boolean   isDelete;

    private Timestamp gmtCreated;

    private Timestamp gmtModified;

    public TopicContent() {
    }

    public TopicContent(int titleId, Course course) {
        this.titleId = titleId;
        this.parentId = 0;
        this.creatorId = course.getContent().getCreatorId();
        this.content = course.getContent().getContent();
        this.videoId = course.getVideoId();
    }

    public TopicContent(int titleId, DetailedNews detailedNews) {
        this.titleId = titleId;
        this.parentId = 0;
        this.creatorId = detailedNews.getContent().getCreatorId();
        this.content = detailedNews.getContent().getContent();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTitleId() {
        return titleId;
    }

    public void setTitleId(int titleId) {
        this.titleId = titleId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public int getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(int replyNum) {
        this.replyNum = replyNum;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}