package com.buyerologie.topic.vo;

import com.buyerologie.topic.model.TopicContent;
import com.buyerologie.topic.model.TopicTitle;
import com.buyerologie.user.model.User;

public class DetailedNews extends Topic {

    public DetailedNews() {
    }

    public DetailedNews(User creator, TopicTitle topicTitle, TopicContent topicContent) {
        super(creator, topicTitle, topicContent);
    }
}
