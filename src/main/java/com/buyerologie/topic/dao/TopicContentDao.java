package com.buyerologie.topic.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.buyerologie.topic.model.TopicContent;

public interface TopicContentDao {
    void insert(TopicContent topicContent);

    void updateById(TopicContent topicContent);

    void deleteById(int id);

    void deleteByTitleId(@Param("titleId") int titleId);

    TopicContent selectById(int id);

    TopicContent selectMainContent(@Param("titleId") int titleId);

    List<TopicContent> selectByTitleId(@Param("titleId") int titleId, @Param("start") int start,
                                       @Param("limit") int limit);

    Integer selectByVideoId(@Param("videoId") String videoId);

    List<TopicContent> selectVideoContentByTitleId(@Param("titleIds") String titleIds);

    int countByTitleId(@Param("titleId") int titleId);
}
