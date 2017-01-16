package com.buyerologie.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.buyerologie.user.model.VipDetail;

public interface VipDetailDao {
    void deleteById(int userId);

    void insert(VipDetail vipDetail);

    VipDetail selectById(int userId);

    void updateById(VipDetail vipDetail);

    VipDetail selectBySourceOrder(@Param("sourceOrder") String sourceOrder);

    VipDetail selectLastByUserId(@Param("userId") int userId);

    List<VipDetail> selectByLimit(@Param("start") int start, @Param("limit") int limit);

    int countUserNum();
}
