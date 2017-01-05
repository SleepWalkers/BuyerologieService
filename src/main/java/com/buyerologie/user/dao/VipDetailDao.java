package com.buyerologie.user.dao;

import com.buyerologie.user.model.VipDetail;

public interface VipDetailDao {
    void deleteById(int userId);

    void insert(VipDetail vipDetail);

    VipDetail selectById(int userId);

    void updateById(VipDetail vipDetail);
}
