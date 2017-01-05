package com.buyerologie.topic.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.buyerologie.topic.model.Image;

public interface ImageDao {
    void insert(Image image);

    void updateById(Image image);

    void deleteById(int id);

    List<Image> selectByTopicId(@Param(value = "topicId") int topicId, @Param("type") int type);

    Image selectById(int id);
}