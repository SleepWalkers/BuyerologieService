package com.buyerologie.user.exception;

public class UserPasswordWrongException extends UserException {

    /** @author Sleepwalker 2016年12月3日 下午3:43:02 */
    private static final long   serialVersionUID  = 8047765062668025758L;

    private static final String DEFAULT_ERROR_MSG = "密码错误";

    public UserPasswordWrongException() {
        super(DEFAULT_ERROR_MSG);
    }

    public UserPasswordWrongException(int errorCode, String msg) {
        super(errorCode, msg);
    }

    public UserPasswordWrongException(String msg) {
        super(msg);
    }

}
