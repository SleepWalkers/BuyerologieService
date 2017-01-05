package com.buyerologie.user.exception;

import com.buyerologie.common.exception.BizException;

public class UserException extends BizException {

    /** @author Sleepwalker 2016年12月3日 下午12:55:14 */
    private static final long serialVersionUID = 1129446748147423386L;

    public UserException(int errorCode, String msg) {
        super(errorCode, msg);
    }

    public UserException(String msg) {
        super(msg);
    }

}
