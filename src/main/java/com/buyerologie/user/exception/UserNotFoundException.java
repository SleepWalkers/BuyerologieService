package com.buyerologie.user.exception;

public class UserNotFoundException extends UserException {

    /**  */
    private static final long   serialVersionUID = 1L;
    private static final String ERROR_MSG        = "该用户不存在";

    public UserNotFoundException(int errorCode, String msg) {
        super(errorCode, msg);
    }

    public UserNotFoundException(String msg) {
        super(msg);
    }

    public UserNotFoundException() {
        super(ERROR_MSG);
    }
}
