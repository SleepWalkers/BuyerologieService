package com.buyerologie.trade.exception;

public class PayTypeNotChoseException extends GenerateOrderException {

    /** @author Sleepwalker 2017年1月7日 上午12:00:01 */
    private static final long   serialVersionUID  = -1288808177945745959L;

    private static final String DEFAULT_ERROR_MSG = "请选择支付方式";

    public PayTypeNotChoseException() {
        super(DEFAULT_ERROR_MSG);
    }

    public PayTypeNotChoseException(int errorCode, String msg) {
        super(errorCode, msg);
    }

    public PayTypeNotChoseException(String msg) {
        super(msg);
    }
}
