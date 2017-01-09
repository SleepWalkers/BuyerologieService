package com.buyerologie.trade.pay.exception;

public class PayReturnVerifyException extends PayException {

    private static final long serialVersionUID = 1027688961309529722L;

    public PayReturnVerifyException(int errorCode, String msg) {
        super(errorCode, msg);
    }

    public PayReturnVerifyException() {
        super(DEFAULT_ERROR_CODE, "返回参数验证错误！");
    }

    public PayReturnVerifyException(String msg) {
        super(DEFAULT_ERROR_CODE, msg);
    }
}
