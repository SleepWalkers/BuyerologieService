package com.buyerologie.user.model;

public class UserTopicCollectRelation {
    private int userId;

    private int topicId;

    public UserTopicCollectRelation() {
    }

    public UserTopicCollectRelation(int userId, int topicId) {
        this.userId = userId;
        this.topicId = topicId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }
}