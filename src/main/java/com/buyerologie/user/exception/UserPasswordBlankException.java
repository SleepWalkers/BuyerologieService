package com.buyerologie.user.exception;

public class UserPasswordBlankException extends UserException {

    /** @author Sleepwalker 2016年12月3日 下午12:57:28 */
    private static final long   serialVersionUID  = 1014098706160777239L;

    private static final String DEFAULT_ERROR_MSG = "密码不能为空";

    public UserPasswordBlankException() {
        super(DEFAULT_ERROR_MSG);
    }

    public UserPasswordBlankException(int errorCode, String msg) {
        super(errorCode, msg);
    }

    public UserPasswordBlankException(String msg) {
        super(msg);
    }

}
