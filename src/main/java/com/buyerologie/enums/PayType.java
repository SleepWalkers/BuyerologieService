package com.buyerologie.enums;

public enum PayType {

    ALIPAY(1), WEIXIN(2);

    private int payType;

    private PayType(int payType) {
        this.payType = payType;
    }

    public static PayType get(int payTypeCode) {
        for (PayType payType : PayType.values()) {
            if (payType.getPayType() == payTypeCode) {
                return payType;
            }
        }
        return null;
    }

    public int getPayType() {
        return payType;
    }

}
