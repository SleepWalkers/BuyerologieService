package com.buyerologie.topic.dao;

import org.apache.ibatis.annotations.Param;

import com.buyerologie.topic.model.TopicImageRelation;

public interface TopicImageRelationDao {
    void insert(TopicImageRelation topicImageRelation);

    void delete(@Param("imageId") int imageId, @Param("titleId") int titleId);
}
