package com.buyerologie.video.model;

public class PlayListVideoRelation {
    private long   playListId;

    private String videoId;

    public PlayListVideoRelation() {
    }

    public PlayListVideoRelation(long playListId, String videoId) {
        this.playListId = playListId;
        this.videoId = videoId;
    }

    public long getPlayListId() {
        return playListId;
    }

    public void setPlayListId(long playListId) {
        this.playListId = playListId;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}