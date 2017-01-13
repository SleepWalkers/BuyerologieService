package com.buyerologie.trade.imp;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.buyerologie.trade.dao.TradeOrderDao;
import com.buyerologie.trade.exception.TradeException;
import com.buyerologie.trade.model.TradeOrder;
import com.buyerologie.trade.pay.PayService;
import com.buyerologie.trade.pay.exception.PayException;
import com.buyerologie.trade.pay.exception.PayRecordNotFoundException;
import com.buyerologie.trade.pay.exception.PayReturnVerifyException;
import com.buyerologie.trade.pay.exception.PayServerErrorException;
import com.buyerologie.trade.pay.utils.AlipayNotify;
import com.buyerologie.user.exception.UserException;
import com.buyerologie.vip.exception.VipException;

@Service("alipayTradeService")
public class AlipayTradeService extends AbstractTradeService {

    @Resource
    private TradeOrderDao       tradeOrderDao;

    @Resource
    private PayService          alipayService;

    private static final Logger logger = Logger.getLogger(AlipayTradeService.class);

    private TradeOrder getTradeOrder(Map<String, String> params) throws PayException {

        if (logger.isInfoEnabled()) {
            logger.info("Web Alipay---The callback result paras are " + params.toString());
        }
        String out_trade_no = params.get("out_trade_no");
        //交易状态
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
        if (logger.isInfoEnabled()) {
            logger.info("Web Alipay---The trade status is " + params.get("trade_status") + "!");
        }
        if (tradeOrder.getPaidTime() != null) {
            return null;
        }
        return tradeOrder;
    }

    @Override
    public boolean payReturn(HttpServletRequest request) throws TradeException, UserException,
                                                        VipException {

        if (logger.isInfoEnabled()) {
            logger.info("Web Alipay---Start handling the callback/notify result!");
        }
        if (request == null) {
            throw new PayServerErrorException("Request不能为空");
        }
        //获取支付宝GET过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        rebuildParamsMap(request, params);
        TradeOrder tradeOrder = getTradeOrder(params);
        if (tradeOrder == null) {
            return false;
        }
        String trade_status = params.get("trade_status");
        // 即时到帐接口       
        if ("TRADE_FINISHED".equals(trade_status) || "TRADE_SUCCESS".equals(trade_status)) {
            paidTradeOrder(params, tradeOrder);
            addVip(tradeOrder);
            return true;
        } else {
            if (logger.isInfoEnabled()) {
                logger.info("Web Alipay---End handling the callback result!");
            }
            return false;
        }
    }

    private void paidTradeOrder(Map<String, String> params, TradeOrder tradeOrder) {
        if (tradeOrder == null) {
            return;
        }
        //判断该笔订单是否在商户网站中已经做过处理
        tradeOrder.setPaidTime(new Timestamp(System.currentTimeMillis()));
        tradeOrder.setOutBuyerId(params.get("buyer_id"));
        tradeOrder.setOutBuyerAccount(params.get("buyer_email"));
        tradeOrder.setOutPayOrderNumber(params.get("trade_no"));
        tradeOrderDao.updateById(tradeOrder);
        if (logger.isInfoEnabled()) {
            logger.info("Web Alipay---End handling the callback result!");
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
