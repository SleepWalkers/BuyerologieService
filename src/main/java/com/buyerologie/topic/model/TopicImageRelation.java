package com.buyerologie.topic.model;

public class TopicImageRelation {
    private int topicTitleId;

    private int imageId;

    private int type;

    public TopicImageRelation() {
    }

    public TopicImageRelation(int topicTitleId, int imageId, int type) {
        this.topicTitleId = topicTitleId;
        this.imageId = imageId;
        this.type = type;
    }

    public int getTopicTitleId() {
        return topicTitleId;
    }

    public void setTopicTitleId(int topicTitleId) {
        this.topicTitleId = topicTitleId;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}