package com.buyerologie.topic.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.buyerologie.topic.model.TopicTitle;

public interface TopicTitleDao {

    void insert(TopicTitle topicTitle);

    void updateById(TopicTitle topicTitle);

    void deleteById(int id);

    TopicTitle selectById(int id);

    List<TopicTitle> selectByTopicIds(@Param("topicIds") String topicIds);

    List<TopicTitle> selectByCateId(@Param(value = "cateId") int cateId,
                                    @Param(value = "start") int start,
                                    @Param(value = "limit") int limit);

    int countByCateId(@Param(value = "cateId") int cateId);
}
