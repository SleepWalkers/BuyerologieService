package com.buyerologie.video.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.buyerologie.video.model.Video;

public interface VideoDao {
    void deleteById(String id);

    void insert(Video video);

    Video selectById(String id);

    void updateById(Video video);

    List<Video> selectAll();

    List<Video> selectByPlayList(@Param("playListId") long playListId);

    List<Video> selectByIds(@Param("ids") String ids);

}
