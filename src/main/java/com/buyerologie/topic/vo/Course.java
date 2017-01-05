package com.buyerologie.topic.vo;

import com.buyerologie.topic.model.TopicContent;
import com.buyerologie.topic.model.TopicTitle;
import com.buyerologie.user.model.User;

public class Course extends Topic {

    private String videoId;

    public Course() {
    }

    public Course(User creator, TopicTitle topicTitle, TopicContent topicContent) {
        super(creator, topicTitle, topicContent);
        this.videoId = topicContent.getVideoId();
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}
