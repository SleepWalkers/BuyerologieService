package com.buyerologie.vip.exception;

public class VipExpireException extends VipException {

    /** @author Sleepwalker 2017年1月7日 上午12:23:15 */
    private static final long   serialVersionUID  = -274256119617275378L;
    private static final String DEFAULT_ERROR_MSG = "会员已过期，请重新购买";

    public VipExpireException() {
        super(DEFAULT_ERROR_MSG);
    }

    public VipExpireException(int errorCode, String msg) {
        super(errorCode, msg);
    }

    public VipExpireException(String msg) {
        super(msg);
    }

}
