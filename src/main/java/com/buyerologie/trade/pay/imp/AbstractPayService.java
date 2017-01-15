package com.buyerologie.trade.pay.imp;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.buyerologie.trade.ProductService;
import com.buyerologie.trade.exception.TradeException;
import com.buyerologie.trade.model.TradeOrder;
import com.buyerologie.trade.model.VipProduct;
import com.buyerologie.trade.pay.PayService;
import com.buyerologie.user.exception.UserException;
import com.buyerologie.vip.VipService;
import com.buyerologie.vip.exception.VipException;

public abstract class AbstractPayService implements PayService {

    @Resource
    private VipService          vipService;

    @Resource
    private ProductService      productService;

    private static final Logger logger = Logger.getLogger(AbstractPayService.class);

    protected void addVip(TradeOrder tradeOrder) throws TradeException, UserException,
                                                 VipException {

        VipProduct vipProduct = productService.get(tradeOrder.getProductId());
        if (vipProduct == null) {
            logger.info("订单号为: " + tradeOrder.getOrderNumber() + "的订单，购买的会员ID为: "
                        + tradeOrder.getProductId() + "所对应的会员为空");
            return;
        }

        vipService.add(tradeOrder.getBuyerId(), vipProduct.getAvailableDays(),
            tradeOrder.getOrderNumber());
    }

}
