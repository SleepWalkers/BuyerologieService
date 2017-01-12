package com.buyerologie.trade.imp;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
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
import com.buyerologie.trade.pay.PayService;
import com.buyerologie.trade.pay.exception.PayException;
import com.buyerologie.trade.pay.exception.PayRecordNotFoundException;
import com.buyerologie.trade.pay.exception.PayReturnVerifyException;
import com.buyerologie.trade.pay.exception.PayServerErrorException;
import com.buyerologie.trade.pay.utils.AlipayNotify;
import com.buyerologie.user.UserService;
import com.buyerologie.user.exception.UserException;
import com.buyerologie.user.exception.UserNotFoundException;
import com.buyerologie.user.model.User;

@Service("tradeService")
public class TradeServiceImp implements TradeService {

    @Resource
    private TradeOrderDao       tradeOrderDao;

    @Resource
    private PayService          alipayService;

    @Resource
    private UserService         userService;

    @Resource
    private ProductService      productService;

    private static final Logger logger = Logger.getLogger(TradeServiceImp.class);

    @Override
    public String trade(int buyerId, PayType payType, int productId) throws UserException,
                                                                    TradeException, PayException {

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

        return alipayService.pay(orderNumber, vipProduct.getPrice(), vipProduct.getPrice());
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
    public boolean payReturn(HttpServletRequest request) throws PayException {

        if (logger.isInfoEnabled()) {
            logger.info("Web Alipay---Start handling the callback/notify result!");
        }
        if (request == null) {
            throw new PayServerErrorException("Request不能为空");
        }
        //获取支付宝GET过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        rebuildParamsMap(request, params);
        if (logger.isInfoEnabled()) {
            logger.info("Web Alipay---The callback result paras are " + params.toString());
        }
        String out_trade_no = params.get("out_trade_no");
        //交易状态
        String trade_status = params.get("trade_status");
        if (StringUtils.isBlank(out_trade_no)) {
            throw new PayReturnVerifyException("订单号验证失败！");
        }
        long orderNumber = Long.parseLong(out_trade_no);
        //计算得出通知验证结果
        boolean verifySuccess = AlipayNotify.verify(params);
        if (!verifySuccess) {
            throw new PayReturnVerifyException("参数验证失败！");
        }
        TradeOrder tradeOrder = this.get(orderNumber);

        if (tradeOrder == null) {
            throw new PayRecordNotFoundException();
        }
        if (tradeOrder.getPaidTime() != null) {
            return true;
        }

        if (logger.isInfoEnabled()) {
            logger.info("Web Alipay---The trade status is " + trade_status + "!");
        }
        // 即时到帐接口       
        if ("TRADE_FINISHED".equals(trade_status) || "TRADE_SUCCESS".equals(trade_status)) {
            //判断该笔订单是否在商户网站中已经做过处理
            tradeOrder.setPaidTime(new Timestamp(System.currentTimeMillis()));
            tradeOrder.setOutBuyerId(params.get("buyer_id"));
            tradeOrder.setOutBuyerAccount(params.get("buyer_email"));
            tradeOrder.setOutPayOrderNumber(params.get("trade_no"));
            tradeOrderDao.updateById(tradeOrder);
            if (logger.isInfoEnabled()) {
                logger.info("Web Alipay---End handling the callback result!");
            }
            return true;
        } else {
            if (logger.isInfoEnabled()) {
                logger.info("Web Alipay---End handling the callback result!");
            }
            return false;
        }
    }

    /**
     * 重新构造参数，使参数变为key value的map，值的结构用逗号隔开，编码设为utf-8
     *
     * @param request
     * @param charEncode
     * @param params
     */
    private void rebuildParamsMap(HttpServletRequest request, Map<String, String> params) {
        Map<?, ?> requestParams = request.getParameterMap();
        if (requestParams == null) {
            return;
        }
        for (Object nameObj : requestParams.keySet()) {
            String name = (String) nameObj;
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            int length = values.length;
            for (int i = 0; i < length; i++) {
                if (i == length - 1) {
                    valueStr = valueStr + values[i];
                } else {
                    valueStr = valueStr + values[i] + ",";
                }
            }
            params.put(name, valueStr);
        }
    }
}
