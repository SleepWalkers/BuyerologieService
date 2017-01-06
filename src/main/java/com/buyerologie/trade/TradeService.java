package com.buyerologie.trade;

import com.buyerologie.enums.PayType;
import com.buyerologie.trade.exception.TradeException;
import com.buyerologie.trade.model.TradeOrder;
import com.buyerologie.user.exception.UserException;

public interface TradeService {

    long trade(int buyerId, PayType payType, int productId) throws UserException, TradeException;

    void trade(long orderNumber, String outPayOrder, String outBuyerAccount, String outBuyerId);

    TradeOrder get(long orderNumber);
}
