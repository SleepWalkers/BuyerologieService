package com.buyerologie.trade.pay.exception;

public class PayRecordCreateException extends PayException {

    private static final long serialVersionUID = 1027688961309529722L;

    public PayRecordCreateException(int errorCode, String msg) {
        super(errorCode, msg);
    }

    public PayRecordCreateException() {
        super(DEFAULT_ERROR_CODE, "支付记录创建失败！");
    }
}
