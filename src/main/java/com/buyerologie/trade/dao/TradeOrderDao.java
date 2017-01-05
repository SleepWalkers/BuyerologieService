package com.buyerologie.trade.dao;

import com.buyerologie.trade.model.TradeOrder;

public interface TradeOrderDao {
    void deleteById(long orderNumber);

    void insert(TradeOrder tradeOrder);

    TradeOrder selectById(long orderNumber);

    void updateById(TradeOrder tradeOrder);
}
