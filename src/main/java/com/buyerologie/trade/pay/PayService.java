package com.buyerologie.trade.pay;

import javax.servlet.http.HttpServletRequest;

import com.buyerologie.trade.exception.TradeException;
import com.buyerologie.trade.pay.exception.PayException;
import com.buyerologie.user.exception.UserException;
import com.buyerologie.vip.exception.VipException;

public interface PayService {

    /**
     * 支付
     * 
     * @param name 支付的商品名
     * @param totalPrice 总价
     * @param actualPrice 实付金额
     * @return
     * @throws PayException 
     */
    public String pay(long orderNumber, double totalPrice, double actualPrice) throws PayException;

    boolean payReturn(HttpServletRequest request) throws TradeException, UserException,
                                                  VipException;

}
