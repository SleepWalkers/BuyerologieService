package com.buyerologie.trade.pay.exception;

public class PayServerErrorException extends PayException {

    private static final long serialVersionUID = 1027688961309529722L;

    public PayServerErrorException(int errorCode, String msg) {
        super(errorCode, msg);
    }

    public PayServerErrorException() {
        super(DEFAULT_ERROR_CODE, "支付服务错误！");
    }

    public PayServerErrorException(String msg) {
        super(DEFAULT_ERROR_CODE, msg);
    }
}
