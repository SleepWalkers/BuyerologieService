package com.buyerologie.trade.exception;

public class GenerateOrderException extends TradeException {

    /** @author Sleepwalker 2017年1月6日 下午11:57:51 */
    private static final long serialVersionUID = -1481417483990900376L;

    public GenerateOrderException(int errorCode, String msg) {
        super(errorCode, msg);
    }

    public GenerateOrderException(String msg) {
        super(msg);
    }

}
