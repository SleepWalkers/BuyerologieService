package com.buyerologie.trade.imp;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.buyerologie.trade.exception.TradeException;
import com.buyerologie.trade.pay.PayService;
import com.buyerologie.user.exception.UserException;
import com.buyerologie.vip.exception.VipException;

@Service("weixinTradeService")
public class WeixinTradeService extends AbstractTradeService {

    @Resource
    private PayService weixinPayService;

    @Override
    public boolean payReturn(HttpServletRequest request) throws TradeException, UserException,
                                                        VipException {
        return false;
    }

}
