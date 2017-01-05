package com.buyerologie.topic.enums;

public enum TopicCate {

    NEWS(1), COURSE(2);

    private int cateId;

    private TopicCate(int cateId) {
        this.cateId = cateId;
    }

    public int getCateId() {
        return cateId;
    }
}
