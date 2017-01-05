package com.buyerologie.video.vo;

import java.util.List;

import com.buyerologie.video.model.PlayList;

public class DetailedPlayList extends PlayList {

    private List<ListVideo> videos;

    public DetailedPlayList() {
    }

    public DetailedPlayList(PlayList playList, List<ListVideo> videos) {
        super(playList);
        this.setVideos(videos);
    }

    public List<ListVideo> getVideos() {
        return videos;
    }

    public void setVideos(List<ListVideo> videos) {
        this.videos = videos;
    }

}
