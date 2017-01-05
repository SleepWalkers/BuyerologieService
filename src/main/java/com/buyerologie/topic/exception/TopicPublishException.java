package com.buyerologie.topic.exception;

public class TopicPublishException extends TopicException {

    /** @author Sleepwalker 2016年12月2日 下午4:45:54 */
    private static final long serialVersionUID = 7854474869452347440L;

    public TopicPublishException(int errorCode, String msg) {
        super(errorCode, msg);
    }

    public TopicPublishException(String msg) {
        super(msg);
    }
}
