package com.buyerologie.cms.model;

import java.sql.Timestamp;

public class CmsPage {
    private int       id;

    private String    name;

    private String    enName;

    private String    title;

    private String    description;

    private String    keywords;

    private Timestamp gmtCreated;

    private Timestamp gmtModified;

    public CmsPage() {
    }

    public CmsPage(CmsPage page) {
        this.id = page.id;
        this.name = page.name;
        this.enName = page.enName;
        this.title = page.title;
        this.description = page.description;
        this.keywords = page.keywords;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
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