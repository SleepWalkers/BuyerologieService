package com.buyerologie.trade.exception;

public class OrderNotBelongToYouException extends TradeException {

    /** @author Sleepwalker 2017年1月7日 上午12:14:14 */
    private static final long   serialVersionUID  = 5491205504083908251L;

    private static final String DEFAULT_ERROR_MSG = "该交易订单不属于你，无法增加你的会员天数";

    public OrderNotBelongToYouException() {
        super(DEFAULT_ERROR_MSG);
    }

    public OrderNotBelongToYouException(int errorCode, String msg) {
        super(errorCode, msg);
    }

    public OrderNotBelongToYouException(String msg) {
        super(msg);
    }

}
