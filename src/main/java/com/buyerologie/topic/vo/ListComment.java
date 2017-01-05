package com.buyerologie.topic.vo;

import java.sql.Timestamp;

import com.buyerologie.topic.model.TopicContent;
import com.buyerologie.user.model.User;

public class ListComment {
    private User      creator;

    private String    comment;

    private Timestamp postTime;

    public ListComment() {
    }

    public ListComment(User creator, TopicContent topicContent) {
        this.creator = creator;
        this.comment = topicContent.getContent();
        this.postTime = topicContent.getGmtModified();
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Timestamp getPostTime() {
        return postTime;
    }

    public void setPostTime(Timestamp postTime) {
        this.postTime = postTime;
    }
}
