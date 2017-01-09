package com.buyerologie.trade.pay.exception;

public class PayRecordNotFoundException extends PayException {

    private static final long serialVersionUID = 1027688961309529722L;

    public PayRecordNotFoundException(int errorCode, String msg) {
        super(errorCode, msg);
    }

    public PayRecordNotFoundException() {
        super(DEFAULT_ERROR_CODE, "找不到支付记录！");
    }

    public PayRecordNotFoundException(String msg) {
        super(DEFAULT_ERROR_CODE, msg);
    }
}
