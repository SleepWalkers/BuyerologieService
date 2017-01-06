package com.buyerologie.vip.exception;

import com.buyerologie.common.exception.BizException;

public class VipException extends BizException {

    /** @author Sleepwalker 2017年1月7日 上午12:21:34 */
    private static final long serialVersionUID = 5247994932885627020L;

    public VipException(int errorCode, String msg) {
        super(errorCode, msg);
    }

    public VipException(String msg) {
        super(msg);
    }
}
