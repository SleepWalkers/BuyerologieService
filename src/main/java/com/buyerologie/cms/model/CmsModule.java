package com.buyerologie.cms.model;

import java.sql.Timestamp;

public class CmsModule {
    private int       id;

    private String    name;

    private Timestamp gmtCreated;

    private Timestamp gmtModified;

    public CmsModule() {
    }

    public CmsModule(CmsModule cmsModule) {
        this.id = cmsModule.getId();
        this.name = cmsModule.getName();
        this.gmtCreated = cmsModule.getGmtCreated();
        this.gmtModified = cmsModule.getGmtModified();
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