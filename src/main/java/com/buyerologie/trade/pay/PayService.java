package com.buyerologie.trade.pay;

import com.buyerologie.trade.pay.exception.PayException;

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

}
