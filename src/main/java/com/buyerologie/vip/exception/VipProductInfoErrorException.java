package com.buyerologie.vip.exception;

public class VipProductInfoErrorException extends VipException {

    /** @author Sleepwalker 2017年1月7日 上午12:23:15 */
    private static final long   serialVersionUID  = -274256119617275378L;
    private static final String DEFAULT_ERROR_MSG = "输入会员信息有误，请重新输入";

    public VipProductInfoErrorException() {
        super(DEFAULT_ERROR_MSG);
    }

    public VipProductInfoErrorException(int errorCode, String msg) {
        super(errorCode, msg);
    }

    public VipProductInfoErrorException(String msg) {
        super(msg);
    }

}
