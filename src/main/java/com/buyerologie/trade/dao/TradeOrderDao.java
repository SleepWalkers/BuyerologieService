package com.buyerologie.trade.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.buyerologie.trade.model.TradeOrder;

public interface TradeOrderDao {
    void deleteById(long orderNumber);

    void insert(TradeOrder tradeOrder);

    TradeOrder selectById(long orderNumber);

    void updateById(TradeOrder tradeOrder);

    int countAll();

    int countWaitForPay();

    int countPaidOrders();

    int countCanceled();

    List<TradeOrder> selectByBuyerId(@Param("buyerId") int buyerId);

    List<TradeOrder> selectAllByLimit(@Param("start") int start, @Param("limit") int limit);

    List<TradeOrder> selectWaitForPayByLimit(@Param("start") int start, @Param("limit") int limit);

    List<TradeOrder> selectPaidOrdersByLimit(@Param("start") int start, @Param("limit") int limit);

    List<TradeOrder> selectCanceledByLimit(@Param("start") int start, @Param("limit") int limit);

}
