package com.buyerologie.topic.exception;

public class TopicTitleBlankException extends TopicPublishException {

    /** @author Sleepwalker 2016年12月2日 下午4:43:41 */
    private static final long   serialVersionUID  = -7703420994033368726L;

    private static final String DEFAULT_ERROR_MSG = "标题不能为空";

    public TopicTitleBlankException() {
        super(DEFAULT_ERROR_MSG);
    }

    public TopicTitleBlankException(int errorCode, String msg) {
        super(errorCode, msg);
    }

    public TopicTitleBlankException(String msg) {
        super(msg);
    }
}
