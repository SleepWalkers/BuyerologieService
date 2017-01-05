package com.buyerologie.topic.exception;

public class TopicCreatorNullException extends TopicPublishException {

    /** @author Sleepwalker 2016年12月2日 下午4:50:06 */
    private static final long   serialVersionUID  = -4015896411536925863L;

    private static final String DEFAULT_ERROR_MSG = "创建者不存在";

    public TopicCreatorNullException() {
        super(DEFAULT_ERROR_MSG);
    }

    public TopicCreatorNullException(int errorCode, String msg) {
        super(errorCode, msg);
    }

    public TopicCreatorNullException(String msg) {
        super(msg);
    }
}
