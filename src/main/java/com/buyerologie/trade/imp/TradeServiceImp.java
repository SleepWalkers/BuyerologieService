package com.buyerologie.trade.imp;

import java.sql.Timestamp;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.buyerologie.enums.PayType;
import com.buyerologie.trade.ProductService;
import com.buyerologie.trade.TradeService;
import com.buyerologie.trade.dao.TradeOrderDao;
import com.buyerologie.trade.exception.PayTypeNotChoseException;
import com.buyerologie.trade.exception.TradeException;
import com.buyerologie.trade.exception.VipProductNotExistException;
import com.buyerologie.trade.model.TradeOrder;
import com.buyerologie.trade.model.VipProduct;
import com.buyerologie.user.UserService;
import com.buyerologie.user.exception.UserException;
import com.buyerologie.user.exception.UserNotFoundException;
import com.buyerologie.user.model.User;

@Service("tradeService")
public class TradeServiceImp implements TradeService {

    @Resource
    private TradeOrderDao  tradeOrderDao;

    @Resource
    private UserService    userService;

    @Resource
    private ProductService productService;

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

    private long generateOrderNumber(int buyerId, int productId) {
        return Long.parseLong((buyerId + "").substring(0, 1) + System.currentTimeMillis()
                              + productId);
    }

    @Override
    public void trade(long orderNumber, String outPayOrder, String outBuyerAccount,
                      String outBuyerId) {
        if (orderNumber <= 0) {
            return;
        }
        TradeOrder tradeOrder = tradeOrderDao.selectById(orderNumber);
        if (tradeOrder == null) {
            return;
        }

        tradeOrder.setOutBuyerAccount(outBuyerAccount);
        tradeOrder.setOutBuyerId(outBuyerId);
        tradeOrder.setOutPayOrderNumber(outPayOrder);
        tradeOrder.setPaidTime(new Timestamp(System.currentTimeMillis()));
        tradeOrderDao.updateById(tradeOrder);
    }

    @Override
    public TradeOrder get(long orderNumber) {
        if (orderNumber <= 0) {
            return null;
        }
        return tradeOrderDao.selectById(orderNumber);
    }
}
