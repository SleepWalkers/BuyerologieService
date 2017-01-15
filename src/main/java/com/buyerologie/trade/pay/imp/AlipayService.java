package com.buyerologie.trade.pay.imp;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.buyerologie.core.spring.context.SystemContext;
import com.buyerologie.trade.dao.TradeOrderDao;
import com.buyerologie.trade.exception.TradeException;
import com.buyerologie.trade.model.TradeOrder;
import com.buyerologie.trade.pay.AlipayConfig;
import com.buyerologie.trade.pay.exception.PayException;
import com.buyerologie.trade.pay.exception.PayMoneyException;
import com.buyerologie.trade.pay.exception.PayRecordNotFoundException;
import com.buyerologie.trade.pay.exception.PayReturnVerifyException;
import com.buyerologie.trade.pay.exception.PayServerErrorException;
import com.buyerologie.trade.pay.utils.AlipayNotify;
import com.buyerologie.trade.pay.utils.AlipaySubmit;
import com.buyerologie.user.exception.UserException;
import com.buyerologie.vip.exception.VipException;

@Service("alipayService")
public class AlipayService extends AbstractPayService {
    /**
    * 服务器异步通知页面路径
    */
    private static final String NOTIFY_URL            = "/alipay/pay_notify";

    /**
    * 页面跳转同步通知页面路径
    */
    private static final String RETURN_URL            = "/alipay/pay_return";

    private static final String PAY_ORDER_DESCRIPTION = "尚课网的订单";

    @Resource
    private TradeOrderDao       tradeOrderDao;

    private static Logger       logger                = LoggerFactory
        .getLogger(AlipayService.class);

    @Override
    public String pay(long orderNumber, double totalPrice, double actualPrice) throws PayException {
        HttpServletRequest request = SystemContext.getRequest();
        HttpServletResponse response = SystemContext.getResponse();
        if (request == null || response == null) {
            throw new PayServerErrorException("Request,Response都不能为空");
        }
        String serverName = request.getServerName();
        if (StringUtils.isBlank(serverName)) {
            throw new PayServerErrorException("ServerName不能为空");
        }
        if (actualPrice <= 0 || totalPrice <= 0 || actualPrice > totalPrice) {
            throw new PayMoneyException();
        }

        String notifyUrl = getUrl(serverName, NOTIFY_URL);
        String returnUrl = getUrl(serverName, RETURN_URL);

        //把请求参数打包成数组
        Map<String, String> sParaTemp = new HashMap<String, String>();
        sParaTemp.put("service", AlipayConfig.service);
        sParaTemp.put("partner", AlipayConfig.partner);
        sParaTemp.put("seller_id", AlipayConfig.seller_id);
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
        sParaTemp.put("payment_type", AlipayConfig.payment_type);
        sParaTemp.put("notify_url", notifyUrl);
        sParaTemp.put("return_url", returnUrl);
        //若要使用请调用类文件submit中的query_timestamp函数
        sParaTemp.put("out_trade_no", orderNumber + "");
        sParaTemp.put("subject", PAY_ORDER_DESCRIPTION);
        sParaTemp.put("total_fee", actualPrice + "");
        sParaTemp.put("body", PAY_ORDER_DESCRIPTION);

        //建立请求
        String sHtmlText = AlipaySubmit.buildRequest(sParaTemp, "get", "确认");

        redirect2AliPay(sHtmlText, response);

        return "";
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
        TradeOrder tradeOrder = tradeOrderDao.selectById(orderNumber);

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

    /**
     * 跳转到支付宝
     *
     * @param name
     * @param description
     * @param showUrl
     * @param totalPrice
     * @param response
     * @param notifyUrl
     * @param returnUrl
     * @param payRecordId
     */
    private void redirect2AliPay(String html, HttpServletResponse response) {
        response.setContentType("text/html");
        response.setCharacterEncoding(AlipayConfig.input_charset);
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write(html);
            writer.close();
        } catch (IOException e) {
            logger.error("IO错误", e);
        }
    }

    /**
     * 获取返回链接的域名
     *
     * @param serverName
     * @param suffixUrl
     * @return
     */
    private String getUrl(String serverName, String suffixUrl) {
        if (StringUtils.isBlank(serverName)) {
            return "";
        }
        if (StringUtils.isBlank(suffixUrl)) {
            suffixUrl = "";
        }
        String url = "";
        if ("localhost".equals(serverName)) {
            url = "http://" + "www.buyerology.cn" + suffixUrl;
        } else {
            url = "http://" + serverName + suffixUrl;
        }
        return url;
    }

}
