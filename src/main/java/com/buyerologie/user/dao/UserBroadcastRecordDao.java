package com.buyerologie.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.buyerologie.user.model.UserBroadcastRecord;

public interface UserBroadcastRecordDao {
    void deleteById(int id);

    void insert(UserBroadcastRecord userBroadcastRecord);

    UserBroadcastRecord selectById(int id);

    void updateById(UserBroadcastRecord userBroadcastRecord);

    List<UserBroadcastRecord> selectByUserId(@Param("userId") int userId);
}
