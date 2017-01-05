package com.buyerologie.topic.vo;

import java.sql.Timestamp;
import java.util.List;

import com.buyerologie.topic.model.TopicContent;
import com.buyerologie.topic.model.TopicTitle;
import com.buyerologie.user.model.User;

public class Topic {

    private int               id;

    private int               creatorId;

    private String            title;

    private String            creatorName;

    private TopicContent      content;

    private List<ListComment> comments;

    private Timestamp         postTime;

    private boolean           isCollected;

    public Topic() {
    }

    public Topic(User creator, TopicTitle topicTitle, TopicContent topicContent) {
        this.id = topicTitle.getId();
        this.creatorId = topicTitle.getCreatorId();
        this.title = topicTitle.getTitle();
        if (creator != null) {
            this.creatorName = creator.getNickname();
        }
        this.content = topicContent;
        this.postTime = topicTitle.getGmtModified();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Timestamp getPostTime() {
        return postTime;
    }

    public void setPostTime(Timestamp postTime) {
        this.postTime = postTime;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public boolean getIsCollected() {
        return isCollected;
    }

    public void setIsCollected(boolean isCollected) {
        this.isCollected = isCollected;
    }

    public TopicContent getContent() {
        return content;
    }

    public void setContent(TopicContent content) {
        this.content = content;
    }

    public List<ListComment> getComments() {
        return comments;
    }

    public void setComments(List<ListComment> comments) {
        this.comments = comments;
    }

}
