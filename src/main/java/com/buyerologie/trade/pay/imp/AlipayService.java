package com.buyerologie.trade.pay.imp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.buyerologie.core.spring.context.SystemContext;
import com.buyerologie.trade.pay.AlipayConfig;
import com.buyerologie.trade.pay.PayService;
import com.buyerologie.trade.pay.exception.PayException;
import com.buyerologie.trade.pay.exception.PayMoneyException;
import com.buyerologie.trade.pay.exception.PayServerErrorException;
import com.buyerologie.trade.pay.utils.AlipaySubmit;

@Service("alipayService")
public class AlipayService implements PayService {

    /**
    * 服务器异步通知页面路径
    */
    private static final String NOTIFY_URL            = "/alipay/pay_notify";

    /**
    * 页面跳转同步通知页面路径
    */
    private static final String RETURN_URL            = "/alipay/pay_return";

    private static final String PAY_ORDER_DESCRIPTION = "尚课网的订单";

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

        //////////////////////////////////////////////////////////////////////////////////

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

    //
    //    private String getRedirectHtml(long orderNumber, double actualPrice, String notifyUrl,
    //                                   String returnUrl) {
    //        String html = alipayDirect(orderNumber, actualPrice, notifyUrl, returnUrl);
    //        return html;
    //    }

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
    protected void redirect2AliPay(String html, HttpServletResponse response) {
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

    //    //即时到帐
    //    private String alipayDirect(long orderNumber, double actualPrice, String notifyUrl,
    //                                String returnUrl) {
    //
    //        Map<String, String> paramMap = new HashMap<>();
    //        paramMap.put("service", "create_direct_pay_by_user");
    //        paramMap.put("partner", AlipayConfig.partner);
    //        paramMap.put("_input_charset", AlipayConfig.input_charset);
    //        paramMap.put("payment_type", PAYMENT_TYPE);
    //        //必填，不能修改
    //        //服务器异步通知页面路径
    //        //需http://格式的完整路径，不能加?id=123这类自定义参数
    //        paramMap.put("notify_url", notifyUrl);
    //        //页面跳转同步通知页面路径
    //        //需http://格式的完整路径，不能加?id=123这类自定义参数，不能写成http://localhost/
    //        paramMap.put("return_url", returnUrl);
    //        //卖家支付宝帐户
    //        paramMap.put("seller_email", SELLER_EMAIL);
    //
    //        paramMap.put("out_trade_no", orderNumber + "");
    //        //订单名称
    //        paramMap.put("subject", PAY_ORDER_DESCRIPTION);
    //        //付款金额
    //        DecimalFormat df = new DecimalFormat("#0.00");
    //        String total_fee = df.format(actualPrice);
    //        //必填
    //        paramMap.put("total_fee", total_fee);
    //        //订单描述
    //        paramMap.put("body", PAY_ORDER_DESCRIPTION);
    //        //商品展示地址
    //        //需以http://开头的完整路径，例如：http://www.xxx.com/myorder.html
    //        paramMap.put("show_url", BUYEROLOGY_WEBSITE);
    //        //防钓鱼时间戳
    //        //若要使用请调用类文件submit中的query_timestamp函数
    //        paramMap.put("anti_phishing_key", "");
    //        //客户端的IP地址
    //        //非局域网的外网IP地址，如：221.0.0.1
    //        paramMap.put("exter_invoke_ip", "");
    //        paramMap.put("extra_common_param", "");
    //
    //        return buildRequest(paramMap, "post");
    //    }

    /**
     * 建立请求，以表单HTML形式构造（默认）
     * @param sParaTemp 请求参数数组
     * @param strMethod 提交方式。两个值可选：post、get
     * @param strButtonName 确认按钮显示文字
     * @return 提交表单HTML文本
     */
    //    private String buildRequest(Map<String, String> sParaTemp, String strMethod) {
    //        //待请求参数数组
    //        Map<String, String> sPara = buildRequestPara(sParaTemp);
    //        List<String> keys = new ArrayList<String>(sPara.keySet());
    //
    //        StringBuffer sbHtml = new StringBuffer();
    //
    //        sbHtml
    //            .append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"><html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><title>云集--交易</title></head><body><form id=\"alipaysubmit\" name=\"alipaysubmit\" action=\""
    //                    + ALIPAY_GATEWAY_NEW + "?_input_charset=utf-8\" method=\"" + strMethod + "\">");
    //
    //        for (int i = 0; i < keys.size(); i++) {
    //            String name = keys.get(i);
    //            String value = sPara.get(name);
    //
    //            sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
    //        }
    //
    //        //submit按钮控件请不要含有name属性
    //        sbHtml.append("<input type=\"submit\" value=\"确认\" style=\"display:none;\"></form>");
    //        sbHtml.append("<script>document.forms['alipaysubmit'].submit();</script></body></html>");
    //
    //        return sbHtml.toString();
    //    }

    /**
     * 生成要请求给支付宝的参数数组
     * @param sParaTemp 请求前的参数数组
     * @return 要请求的参数数组
     */
    //    private Map<String, String> buildRequestPara(Map<String, String> sParaTemp) {
    //
    //        //除去数组中的空值和签名参数
    //        Map<String, String> sPara = RequestUtil.paramFilter(sParaTemp);
    //        //生成签名结果
    //        String mysign = RequestUtil.buildRequestSign(sPara, SignType.MD5);
    //
    //        //签名结果与签名方式加入请求提交参数组中
    //        sPara.put("sign", mysign);
    //        sPara.put("sign_type", AlipayConfig.sign_type);
    //
    //        return sPara;
    //    }

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
