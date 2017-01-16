package com.buyerologie.trade.imp;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.buyerologie.enums.OrderStatus;
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
import com.buyerologie.utils.PageUtil;
import com.buyerologie.vip.VipService;
import com.buyerologie.vip.exception.VipException;

@Service("tradeService")
public class TradeServiceImp implements TradeService {

    @Resource
    private TradeOrderDao  tradeOrderDao;

    @Resource
    private VipService     vipService;

    @Resource
    private UserService    userService;

    @Resource
    private PayService     alipayService;

    @Resource
    private ProductService productService;

    @Resource
    private PayService     weixinPayService;

    //    private static final Logger logger = Logger.getLogger(TradeServiceImp.class);

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
        tradeOrder.setProductName(vipProduct.getOrderProductName());
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
            case ALIPAY: {
                return alipayService.pay(orderNumber, tradeOrder.getActualPrice(),
                    tradeOrder.getActualPrice());
            }
            case WEIXIN: {
                return weixinPayService.pay(orderNumber, tradeOrder.getActualPrice(),
                    tradeOrder.getActualPrice());
            }
            default: {
                throw new PayTypeNotChoseException();
            }
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

    @Override
    public boolean payReturn(HttpServletRequest request, PayType payType) throws TradeException,
                                                                         UserException,
                                                                         VipException {
        if (payType == null) {
            return false;
        }

        switch (payType) {
            case ALIPAY: {
                return alipayService.payReturn(request);
            }
            case WEIXIN: {
                return weixinPayService.payReturn(request);
            }
            default: {
                return false;
            }
        }
    }

    @Override
    public List<TradeOrder> get(OrderStatus orderStatus, int page, int pageSize) {
        if (orderStatus == null) {
            return tradeOrderDao.selectAllByLimit(PageUtil.getStart(page, pageSize),
                PageUtil.getLimit(page, pageSize));
        }
        switch (orderStatus) {
            case WAIT_BUYER_PAY: {
                return tradeOrderDao.selectWaitForPayByLimit(PageUtil.getStart(page, pageSize),
                    PageUtil.getLimit(page, pageSize));
            }
            case TRADE_SUCCESS: {
                return tradeOrderDao.selectPaidOrdersByLimit(PageUtil.getStart(page, pageSize),
                    PageUtil.getLimit(page, pageSize));
            }
            case TRADE_CANCELED: {
                return tradeOrderDao.selectCanceledByLimit(PageUtil.getStart(page, pageSize),
                    PageUtil.getLimit(page, pageSize));
            }
            default:
                return null;
        }
    }

    @Override
    public int countOrderNum(OrderStatus orderStatus) {
        if (orderStatus == null) {
            return tradeOrderDao.countAll();
        }
        switch (orderStatus) {
            case WAIT_BUYER_PAY: {
                return tradeOrderDao.countWaitForPay();
            }
            case TRADE_SUCCESS: {
                return tradeOrderDao.countPaidOrders();
            }
            case TRADE_CANCELED: {
                return tradeOrderDao.countCanceled();
            }
            default:
                return 0;
        }
    }
}
