package com.buyerologie.topic.exception;

import com.buyerologie.common.exception.BizException;

public class TopicException extends BizException {

    /** @author Sleepwalker 2016年12月2日 下午4:43:41 */
    private static final long serialVersionUID = -7703420994033368726L;

    public TopicException(int errorCode, String msg) {
        super(errorCode, msg);
    }

    public TopicException(String msg) {
        super(msg);
    }

}
