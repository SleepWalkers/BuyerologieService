package com.buyerologie.trade.pay.exception;

import com.buyerologie.common.exception.BizException;

public class PayException extends BizException {

    private static final long serialVersionUID = 1L;

    public PayException(int errorCode, String msg) {
        super(errorCode, msg);
    }

    public PayException(String msg) {
        super(DEFAULT_ERROR_CODE, msg);
    }
}
