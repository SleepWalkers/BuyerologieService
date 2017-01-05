package com.buyerologie.topic.exception;

public class TopicTitleCateNullException extends TopicPublishException {

    /** @author Sleepwalker 2016年12月2日 下午4:43:41 */
    private static final long   serialVersionUID  = -7703420994033368726L;

    private static final String DEFAULT_ERROR_MSG = "所属类别不存在";

    public TopicTitleCateNullException() {
        super(DEFAULT_ERROR_MSG);
    }

    public TopicTitleCateNullException(int errorCode, String msg) {
        super(errorCode, msg);
    }

    public TopicTitleCateNullException(String msg) {
        super(msg);
    }
}
