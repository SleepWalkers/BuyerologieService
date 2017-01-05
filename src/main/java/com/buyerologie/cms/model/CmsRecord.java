package com.buyerologie.cms.model;

import java.sql.Timestamp;

public class CmsRecord {
    private int       id;

    private String    name;

    private String    value;

    private Timestamp gmtCreated;

    private Timestamp gmtModified;

    public CmsRecord() {
    }

    public CmsRecord(CmsRecord record) {
        this.id = record.id;
        this.name = record.name;
        this.value = record.value;
        this.gmtCreated = record.gmtCreated;
        this.gmtModified = record.gmtModified;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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