package com.buyerologie.user.imp;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.buyerologie.topic.CourseService;
import com.buyerologie.user.UserActionService;
import com.buyerologie.user.dao.UserBroadcastRecordDao;
import com.buyerologie.user.dao.UserTopicCollectRelationDao;
import com.buyerologie.user.model.UserBroadcastRecord;
import com.buyerologie.user.model.UserTopicCollectRelation;

@Service("userActionService")
public class UserActionServiceImp implements UserActionService {

    @Resource
    private CourseService               courseService;

    @Resource
    private UserBroadcastRecordDao      userBroadcastRecordDao;

    @Resource
    private UserTopicCollectRelationDao userTopicCollectRelationDao;

    @Override
    public void collect(int userId, int topicId) {
        userTopicCollectRelationDao.insert(new UserTopicCollectRelation(userId, topicId));
    }

    @Override
    public boolean isCollected(int userId, int topicId) {
        if (userId <= 0 || topicId <= 0) {
            return false;
        }
        return !userTopicCollectRelationDao.selectByUserIdAndTopicId(userId, topicId).isEmpty();
    }

    @Override
    public void uncollect(int userId, int topicId) {
        if (userId <= 0 || topicId <= 0) {
            return;
        }
        userTopicCollectRelationDao.deleteByUserIdAndTopicId(userId, topicId);
    }

    @Override
    public void watchVideo(int userId, String videoId) {
        if (userId <= 0 || StringUtils.isBlank(videoId)) {
            return;
        }
        userBroadcastRecordDao.insert(new UserBroadcastRecord(userId, videoId));
    }

    @Override
    public List<UserBroadcastRecord> getBroadcastRecords(int userId) {
        if (userId <= 0) {
            return null;
        }
        return userBroadcastRecordDao.selectByUserId(userId);
    }

    @Override
    public List<Integer> getCollectedTopicId(int userId, int topicCateId) {
        if (userId <= 0 || topicCateId <= 0) {
            return null;
        }
        return userTopicCollectRelationDao.selectByUserIdAndCateId(userId, topicCateId);
    }

    @Override
    public void uncollect(int userId, String videoId) {
        if (userId <= 0 || StringUtils.isBlank(videoId)) {
            return;
        }
        uncollect(userId, courseService.getCourseId(videoId));
    }
}
