package com.buyerologie.topic;

import java.util.List;

import com.buyerologie.topic.exception.TopicException;
import com.buyerologie.topic.model.TopicContent;

public interface TopicContentService {

    /**
     * 根据id获取内容
     * @param contentId
     * @return
     */
    TopicContent getContent(int contentId);

    int getTopicId(String videoId);

    TopicContent getMainContent(int titleId);

    List<TopicContent> getSubContents(int titleId, int page, int pageSize);

    List<TopicContent> getVideoContents(List<Integer> titleIds);

    int countContentNum(int titleId);

    void publishContent(TopicContent topicContent) throws TopicException;

    void editContent(TopicContent topicContent) throws TopicException;

    void deleteContent(int contentId);

    void deleteAllContent(int titleId);
}
