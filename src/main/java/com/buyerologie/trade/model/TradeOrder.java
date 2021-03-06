package com.buyerologie.trade.model;

import java.sql.Timestamp;

public class TradeOrder {
    private long      orderNumber;

    private int       buyerId;

    private boolean   isCancel;

    private double    totalPrice;

    private double    actualPrice;

    private String    outBuyerId;

    private String    outBuyerAccount;

    private String    outPayOrderNumber;

    private int       productId;

    private String    productName;

    private Timestamp paidTime;

    private int       payType;

    private Timestamp gmtCreated;

    private Timestamp gmtModified;

    public TradeOrder() {
    }

    public TradeOrder(TradeOrder tradeOrder) {
        this.orderNumber = tradeOrder.getOrderNumber();
        this.buyerId = tradeOrder.getBuyerId();
        this.isCancel = tradeOrder.getIsCancel();
        this.totalPrice = tradeOrder.getTotalPrice();
        this.actualPrice = tradeOrder.getActualPrice();
        this.outBuyerAccount = tradeOrder.getOutBuyerAccount();
        this.outBuyerAccount = tradeOrder.getOutBuyerAccount();
        this.outPayOrderNumber = tradeOrder.getOutPayOrderNumber();
        this.productId = tradeOrder.getProductId();
        this.productName = tradeOrder.getProductName();
        this.paidTime = tradeOrder.getPaidTime();
        this.payType = tradeOrder.getPayType();
        this.gmtCreated = tradeOrder.getGmtCreated();
        this.gmtModified = tradeOrder.getGmtModified();
    }

    public long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    public boolean getIsCancel() {
        return isCancel;
    }

    public void setIsCancel(boolean isCancel) {
        this.isCancel = isCancel;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(double actualPrice) {
        this.actualPrice = actualPrice;
    }

    public String getOutBuyerId() {
        return outBuyerId;
    }

    public void setOutBuyerId(String outBuyerId) {
        this.outBuyerId = outBuyerId;
    }

    public String getOutBuyerAccount() {
        return outBuyerAccount;
    }

    public void setOutBuyerAccount(String outBuyerAccount) {
        this.outBuyerAccount = outBuyerAccount;
    }

    public String getOutPayOrderNumber() {
        return outPayOrderNumber;
    }

    public void setOutPayOrderNumber(String outPayOrderNumber) {
        this.outPayOrderNumber = outPayOrderNumber;
    }

    public Timestamp getPaidTime() {
        return paidTime;
    }

    public void setPaidTime(Timestamp paidTime) {
        this.paidTime = paidTime;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
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

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}