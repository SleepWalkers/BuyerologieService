package com.buyerologie.topic.vo;

import java.util.List;

import com.buyerologie.topic.model.Image;
import com.buyerologie.topic.model.TopicTitle;

public class ListNews {

    private int         id;

    private String      title;

    private List<Image> images;

    public ListNews() {
    }

    public ListNews(TopicTitle topicTitle, List<Image> images) {
        this.id = topicTitle.getId();
        this.title = topicTitle.getTitle();
        this.images = images;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
