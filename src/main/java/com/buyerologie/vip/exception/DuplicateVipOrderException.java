package com.buyerologie.vip.exception;

public class DuplicateVipOrderException extends VipException {

    /** @author Sleepwalker 2017年1月7日 上午12:23:15 */
    private static final long   serialVersionUID  = -274256119617275378L;
    private static final String DEFAULT_ERROR_MSG = "该订单对应的会员时长已加，请勿重复提交";

    public DuplicateVipOrderException() {
        super(DEFAULT_ERROR_MSG);
    }

    public DuplicateVipOrderException(int errorCode, String msg) {
        super(errorCode, msg);
    }

    public DuplicateVipOrderException(String msg) {
        super(msg);
    }

}
