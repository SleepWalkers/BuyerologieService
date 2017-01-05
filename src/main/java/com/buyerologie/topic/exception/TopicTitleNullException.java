package com.buyerologie.topic.exception;

public class TopicTitleNullException extends TopicPublishException {

    /** @author Sleepwalker 2016年12月2日 下午4:43:41 */
    private static final long   serialVersionUID  = -7703420994033368726L;

    private static final String DEFAULT_ERROR_MSG = "标题不存在";

    public TopicTitleNullException() {
        super(DEFAULT_ERROR_MSG);
    }

    public TopicTitleNullException(int errorCode, String msg) {
        super(errorCode, msg);
    }

    public TopicTitleNullException(String msg) {
        super(msg);
    }
}
