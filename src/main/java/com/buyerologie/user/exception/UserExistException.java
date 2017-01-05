package com.buyerologie.user.exception;

public class UserExistException extends UserException {

    /** @author Sleepwalker 2016年12月3日 下午12:55:58 */
    private static final long   serialVersionUID  = -2350528180042280540L;

    private static final String DEFAULT_ERROR_MSG = "用户已存在";

    public UserExistException() {
        super(DEFAULT_ERROR_MSG);
    }

    public UserExistException(int errorCode, String msg) {
        super(errorCode, msg);
    }

    public UserExistException(String msg) {
        super(msg);
    }

}
