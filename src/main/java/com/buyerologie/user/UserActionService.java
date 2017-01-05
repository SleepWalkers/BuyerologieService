package com.buyerologie.user;

import java.util.List;

import com.buyerologie.user.model.UserBroadcastRecord;

public interface UserActionService {

    void collect(int userId, int topicId);

    void uncollect(int userId, int topicId);

    void uncollect(int userId, String videoId);

    boolean isCollected(int userId, int topicId);

    List<Integer> getCollectedTopicId(int userId, int topicCateId);

    void watchVideo(int userId, String videoId);

    List<UserBroadcastRecord> getBroadcastRecords(int userId);
}
