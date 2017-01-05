package com.buyerologie.video.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.buyerologie.video.model.PlayListVideoRelation;

public interface PlayListVideoRelationDao {
    void insert(PlayListVideoRelation playListVideoRelation);

    List<String> selectVideoListByPlayListId(@Param("playListId") long playListId);

    PlayListVideoRelation selectByPlayListIdAndVideoId(@Param("playListId") long playListId,
                                                       @Param("videoId") String videoId);

    long selectByVideoId(@Param("videoId") String videoId);

    void deleteByPlayListIdAndVideoId(@Param("playListId") long playListId,
                                      @Param("videoId") String videoId);

    void deleteByPlayListId(@Param("playListId") long playListId);
}