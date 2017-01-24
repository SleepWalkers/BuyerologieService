package com.buyerologie.trade.model;

import java.sql.Timestamp;

public class VipProduct {
    private int       id;

    private String    name;

    private double    price;

    private int       availableDays;

    private Timestamp gmtCreated;

    private Timestamp gmtModified;

    private String    description;

    public VipProduct() {
    }

    public VipProduct(String name, double price, int availableDays, String description) {
        this.name = name;
        this.price = price;
        this.availableDays = availableDays;
        this.description = description;
    }

    public String getOrderProductName() {
        return this.name + "，可用天数:" + this.availableDays + "天";
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAvailableDays() {
        return availableDays;
    }

    public void setAvailableDays(int availableDays) {
        this.availableDays = availableDays;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}