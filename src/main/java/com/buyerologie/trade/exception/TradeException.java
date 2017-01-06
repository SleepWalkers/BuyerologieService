package com.buyerologie.trade.exception;

import com.buyerologie.common.exception.BizException;

public class TradeException extends BizException {

    /** @author Sleepwalker 2017年1月6日 下午11:57:12 */
    private static final long serialVersionUID = -76843277275644107L;

    public TradeException(int errorCode, String msg) {
        super(errorCode, msg);
    }

    public TradeException(String msg) {
        super(msg);
    }
}
