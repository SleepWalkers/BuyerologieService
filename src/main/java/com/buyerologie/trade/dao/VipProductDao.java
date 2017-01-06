package com.buyerologie.trade.dao;

import java.util.List;

import com.buyerologie.trade.model.VipProduct;

public interface VipProductDao {
    void deleteById(int id);

    void insert(VipProduct vipProduct);

    VipProduct selectById(int id);

    void updateById(VipProduct vipProduct);

    List<VipProduct> selectAll();
}
