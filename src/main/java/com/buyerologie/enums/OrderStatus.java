package com.buyerologie.enums;

public enum OrderStatus {

    /**
     * 未付款
     */
    WAIT_BUYER_PAY(21, "未付款"),

    /**
     * 已支付
     */
    TRADE_SUCCESS(22, "已支付"),

    /**
     * 已取消
     */
    TRADE_CANCELED(-2, "已取消");

    private int    statusCode;

    private String desc;      //中文描述

    public static OrderStatus getOrderStatus(int statusCode) {
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.getOrderStatus() == statusCode) {
                return orderStatus;
            }
        }
        return null;
    }

    private OrderStatus(int statusCode, String desc) {
        this.statusCode = statusCode;
        this.desc = desc;
    }

    public String getDesc() {
        return this.desc;
    }

    public int getOrderStatus() {
        return this.statusCode;
    }

}
