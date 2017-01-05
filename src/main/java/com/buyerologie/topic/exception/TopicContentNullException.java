package com.buyerologie.topic.exception;

public class TopicContentNullException extends TopicPublishException {

    /** @author Sleepwalker 2016年12月5日 上午12:02:50 */
    private static final long   serialVersionUID  = 4201470216313959032L;

    private static final String DEFAULT_ERROR_MSG = "内容不存在";

    public TopicContentNullException() {
        super(DEFAULT_ERROR_MSG);
    }

    public TopicContentNullException(int errorCode, String msg) {
        super(errorCode, msg);
    }

    public TopicContentNullException(String msg) {
        super(msg);
    }
}
