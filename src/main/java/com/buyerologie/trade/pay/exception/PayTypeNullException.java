package com.buyerologie.trade.pay.exception;

public class PayTypeNullException extends PayException {

    private static final long serialVersionUID = 1027688961309529722L;

    public PayTypeNullException(int errorCode, String msg) {
        super(errorCode, msg);
    }

    public PayTypeNullException() {
        super(DEFAULT_ERROR_CODE, "支付类型不能为空！");
    }

    public PayTypeNullException(String msg) {
        super(DEFAULT_ERROR_CODE, msg);
    }
}
