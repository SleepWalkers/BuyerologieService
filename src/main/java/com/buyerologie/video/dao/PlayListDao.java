package com.buyerologie.video.dao;

import java.util.List;

import com.buyerologie.video.model.PlayList;

public interface PlayListDao {
    void deleteById(long id);

    void insert(PlayList playList);

    PlayList selectById(long id);

    void updateById(PlayList playList);

    List<Long> getAllPlayListId();

    List<PlayList> getAll();
}