package com.buyerologie.trade.exception;

public class VipProductNotExistException extends GenerateOrderException {

    /** @author Sleepwalker 2017年1月6日 下午11:59:02 */
    private static final long   serialVersionUID  = -352143103123598505L;

    private static final String DEFAULT_ERROR_MSG = "所选会员不存在，请重新选择";

    public VipProductNotExistException() {
        super(DEFAULT_ERROR_MSG);
    }

    public VipProductNotExistException(int errorCode, String msg) {
        super(errorCode, msg);
    }

    public VipProductNotExistException(String msg) {
        super(msg);
    }

}
