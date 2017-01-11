package com.buyerologie.trade;

import javax.servlet.http.HttpServletRequest;

import com.buyerologie.enums.PayType;
import com.buyerologie.trade.exception.TradeException;
import com.buyerologie.trade.model.TradeOrder;
import com.buyerologie.trade.pay.exception.PayException;
import com.buyerologie.user.exception.UserException;

public interface TradeService {

    String trade(int buyerId, PayType payType, int productId) throws UserException, TradeException,
                                                             PayException;

    TradeOrder get(long orderNumber);

    boolean payReturn(HttpServletRequest request) throws PayException;

}
