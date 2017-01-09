package com.buyerologie.trade.pay.exception;

public class PayMoneyException extends PayException {

    private static final long serialVersionUID = 1027688961309529722L;

    public PayMoneyException(int errorCode, String msg) {
        super(errorCode, msg);
    }

    public PayMoneyException() {
        super(DEFAULT_ERROR_CODE, "支付金额错误！");
    }

    public PayMoneyException(String msg) {
        super(DEFAULT_ERROR_CODE, msg);
    }
}
