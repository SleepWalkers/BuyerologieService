package com.buyerologie.trade.exception;

public class OrderNotExistException extends TradeException {
    /** @author Sleepwalker 2017年1月7日 上午12:14:14 */
    private static final long   serialVersionUID  = 5491205504083908251L;

    private static final String DEFAULT_ERROR_MSG = "订单不存在，无法增加会员天数";

    public OrderNotExistException() {
        super(DEFAULT_ERROR_MSG);
    }

    public OrderNotExistException(int errorCode, String msg) {
        super(errorCode, msg);
    }

    public OrderNotExistException(String msg) {
        super(msg);
    }
}
