package com.buyerologie.video;

import java.util.List;

import com.buyerologie.video.model.PlayList;
import com.buyerologie.video.model.Video;
import com.buyerologie.video.vo.DetailedPlayList;
import com.buyerologie.video.vo.ListVideo;

public interface VideoService {

    void sync();

    List<DetailedPlayList> getAllDetailedPlayLists();

    List<Video> getAllVideos();

    List<PlayList> getAll();

    List<ListVideo> getListVideos(List<String> videoIds);

    DetailedPlayList getDetailedPlayList(String videoId);

    DetailedPlayList getDetailedPlayList(long playListId);
}
