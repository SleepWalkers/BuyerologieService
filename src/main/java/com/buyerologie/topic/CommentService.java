package com.buyerologie.topic;

import java.util.List;

import com.buyerologie.topic.exception.TopicException;
import com.buyerologie.topic.vo.ListComment;

public interface CommentService {

    List<ListComment> getListComments(int topicId, int page, int pageSize);

    void publicComment(int topicId, int creatorId, String comment) throws TopicException;

    int countComment(int newsId);
}
