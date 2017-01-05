package com.buyerologie.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.buyerologie.user.model.UserTopicCollectRelation;

public interface UserTopicCollectRelationDao {
    void insert(UserTopicCollectRelation userTopicCollectRelation);

    void deleteByUserIdAndTopicId(@Param("userId") int userId, @Param("topicId") int topicId);

    List<UserTopicCollectRelation> selectByUserIdAndTopicId(@Param("userId") int userId,
                                                            @Param("topicId") int topicId);

    List<Integer> selectByUserIdAndCateId(@Param("userId") int userId, @Param("cateId") int cateId);
}
