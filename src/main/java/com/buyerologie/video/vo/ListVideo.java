package com.buyerologie.video.vo;

import com.buyerologie.video.model.Video;

public class ListVideo extends Video {

    private long    playListId;

    private int     courseId;

    private boolean learned;

    public ListVideo() {
    }

    public ListVideo(int courseId, long playListId, Video video) {
        super(video);
        this.courseId = courseId;
        this.playListId = playListId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public long getPlayListId() {
        return playListId;
    }

    public void setPlayListId(long playListId) {
        this.playListId = playListId;
    }

    public boolean isLearned() {
        return learned;
    }

    public void setLearned(boolean learned) {
        this.learned = learned;
    }

}
