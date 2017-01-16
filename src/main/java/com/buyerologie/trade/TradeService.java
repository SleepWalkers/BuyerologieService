package com.buyerologie.trade;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.buyerologie.enums.OrderStatus;
import com.buyerologie.enums.PayType;
import com.buyerologie.trade.exception.TradeException;
import com.buyerologie.trade.model.TradeOrder;
import com.buyerologie.user.exception.UserException;
import com.buyerologie.vip.exception.VipException;

public interface TradeService {

    long trade(int buyerId, PayType payType, int productId) throws UserException, TradeException;

    String trade(long orderNumber) throws TradeException;

    TradeOrder get(long orderNumber);

    boolean payReturn(HttpServletRequest request, PayType payType) throws TradeException,
                                                                  UserException, VipException;

    int countOrderNum(OrderStatus orderStatus);

    List<TradeOrder> get(int buyerId);

    List<TradeOrder> get(OrderStatus orderStatus, int page, int pageSize);
}
