package com.buyerologie.trade.imp;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.buyerologie.enums.PayType;
import com.buyerologie.trade.ProductService;
import com.buyerologie.trade.TradeService;
import com.buyerologie.trade.dao.TradeOrderDao;
import com.buyerologie.trade.exception.OrderNotExistException;
import com.buyerologie.trade.exception.PayTypeNotChoseException;
import com.buyerologie.trade.exception.TradeException;
import com.buyerologie.trade.exception.VipProductNotExistException;
import com.buyerologie.trade.model.TradeOrder;
import com.buyerologie.trade.model.VipProduct;
import com.buyerologie.trade.pay.PayService;
import com.buyerologie.user.UserService;
import com.buyerologie.user.exception.UserException;
import com.buyerologie.user.exception.UserNotFoundException;
import com.buyerologie.user.model.User;
import com.buyerologie.vip.VipService;
import com.buyerologie.vip.exception.VipException;

public abstract class AbstractTradeService implements TradeService {

    @Resource
    private TradeOrderDao       tradeOrderDao;

    @Resource
    private VipService          vipService;

    @Resource
    private UserService         userService;

    @Resource
    private PayService          aliPayService;

    @Resource
    private ProductService      productService;

    @Resource
    private PayService          weixinPayService;

    private static final Logger logger = Logger.getLogger(AbstractTradeService.class);

    @Override
    public long trade(int buyerId, PayType payType, int productId) throws UserException,
                                                                  TradeException {

        if (buyerId <= 0) {
            throw new UserNotFoundException();
        }
        if (productId <= 0) {
            throw new VipProductNotExistException();
        }

        if (payType == null) {
            throw new PayTypeNotChoseException();
        }

        User user = userService.getUser(buyerId);
        if (user == null) {
            throw new UserNotFoundException();
        }

        VipProduct vipProduct = productService.get(productId);
        if (vipProduct == null) {
            throw new VipProductNotExistException();
        }

        long orderNumber = generateOrderNumber(buyerId, productId);

        TradeOrder tradeOrder = new TradeOrder();
        tradeOrder.setActualPrice(vipProduct.getPrice());
        tradeOrder.setBuyerId(buyerId);
        tradeOrder.setIsCancel(false);
        tradeOrder.setOrderNumber(orderNumber);
        tradeOrder.setPayType(payType.getPayType());
        tradeOrder.setProductId(productId);
        tradeOrder.setTotalPrice(vipProduct.getPrice());
        tradeOrderDao.insert(tradeOrder);

        return orderNumber;
    }

    @Override
    public String trade(long orderNumber) throws TradeException {
        TradeOrder tradeOrder = tradeOrderDao.selectById(orderNumber);
        if (tradeOrder == null) {
            throw new OrderNotExistException();
        }
        PayType payType = PayType.get(tradeOrder.getPayType());

        switch (payType) {
            case WEIXIN: {
                return weixinPayService.pay(orderNumber, tradeOrder.getActualPrice(),
                    tradeOrder.getActualPrice());
            }
            case ALIPAY: {
                return aliPayService.pay(orderNumber, tradeOrder.getActualPrice(),
                    tradeOrder.getActualPrice());
            }
            default:
                return "";
        }
    }

    private long generateOrderNumber(int buyerId, int productId) {
        return Long.parseLong((buyerId + "").substring(0, 1) + System.currentTimeMillis()
                              + productId);
    }

    @Override
    public TradeOrder get(long orderNumber) {
        if (orderNumber <= 0) {
            return null;
        }
        return tradeOrderDao.selectById(orderNumber);
    }

    protected void addVip(TradeOrder tradeOrder) throws TradeException, UserException, VipException {

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
