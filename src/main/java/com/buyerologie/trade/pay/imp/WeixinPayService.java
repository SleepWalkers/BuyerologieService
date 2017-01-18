package com.buyerologie.trade.pay.imp;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import com.buyerologie.core.spring.context.SystemContext;
import com.buyerologie.trade.dao.TradeOrderDao;
import com.buyerologie.trade.exception.TradeException;
import com.buyerologie.trade.model.TradeOrder;
import com.buyerologie.trade.pay.WxPayConfig;
import com.buyerologie.trade.pay.WxScanPayReqData;
import com.buyerologie.trade.pay.WxScanPayResData;
import com.buyerologie.trade.pay.exception.PayException;
import com.buyerologie.trade.pay.exception.PayRecordNotFoundException;
import com.buyerologie.trade.pay.exception.PayReturnVerifyException;
import com.buyerologie.trade.pay.utils.WeixinSign;
import com.buyerologie.trade.pay.utils.XMLParser;
import com.buyerologie.trade.pay.utils.sign.MD5;
import com.buyerologie.user.exception.UserException;
import com.buyerologie.vip.exception.VipException;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

@Service("weixinPayService")
public class WeixinPayService extends AbstractPayService {

    @Resource
    private TradeOrderDao       tradeOrderDao;

    //请求器的配置
    private RequestConfig       requestConfig;

    //HTTP请求器
    private CloseableHttpClient httpClient;

    //商品或支付单简要描述
    private final String        BODY           = "尚课网的订单";

    //服务器异步通知页面路径
    private final String        NOTIFY_URL     = "/weixin/pay_return";

    //连接超时时间，默认10秒
    private int                 socketTimeout  = 10000;

    //传输超时时间，默认30秒
    private int                 connectTimeout = 30000;

    private final String        TRADE_TYPE     = "NATIVE";

    private static final Logger logger         = Logger.getLogger(WeixinPayService.class);

    /**
     * 微信支付
     * @author shuijing
     * 2016年4月1日 上午11:22:31
     * @param request
     * @param response
     * @param payOrderId    需支付的订单号
     * @param actualPrice   支付金额 
     * @return
     */
    @Override
    public String pay(long orderNumber, double totalPrice, double actualPrice) throws PayException {
        //      String date=getDate();

        HttpServletRequest request = SystemContext.getRequest();
        //定单总金额
        double sendPrice = actualPrice * 100;
        DecimalFormat decimalFormat = new DecimalFormat("#0");
        int amount = Integer.parseInt(decimalFormat.format(sendPrice));

        //服务器异步通知页面路径
        String serverName = request.getServerName();
        String notifyUrl = getUrl(serverName, NOTIFY_URL);

        //
        String clientIp = getClientIP();

        WxScanPayReqData scanPayReqData = new WxScanPayReqData(
        //要支付的商品的描述信息，用户会在支付成功页面里看到这个信息
            BODY,
            //支付订单里面可以填的附加数据，API会将提交的这个附加数据原样返回，有助于商户自己可以注明该笔消费的具体内容，方便后续的运营和记录
            "9",
            //商户系统内部的订单号,32个字符内可包含字母, 确保在商户系统唯一
            orderNumber + "",
            //订单总金额，单位为“分”，只能整数
            amount,
            //订单生成的机器IP
            clientIp, notifyUrl, TRADE_TYPE, "");

        try {
            String result = sendPost(WxPayConfig.PAY_API, scanPayReqData);

            WxScanPayResData ac = (WxScanPayResData) getObjectFromXML(result,
                WxScanPayResData.class);

            if (checkIsSignValidFromResponseString(result)) {
                return ac.getCode_url();
            } else {
                return "统一下单签名验证不通过";
            }

        } catch (UnrecoverableKeyException | KeyManagementException | KeyStoreException
                | NoSuchAlgorithmException | IOException e) {
            logger.error("", e);
        } catch (ParserConfigurationException e) {
            logger.error("", e);
        } catch (SAXException e) {
            logger.error("", e);
        }
        return null;
    }

    /**
     * 通过Https往API post xml数据
     * @param url    API地址
     * @param xmlObj 要提交的XML数据对象
     * @return API回包的实际数据
     * @throws IOException
     * @throws KeyStoreException
     * @throws UnrecoverableKeyException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */

    public String sendPost(String url, Object xmlObj) throws IOException, KeyStoreException,
                                                     UnrecoverableKeyException,
                                                     NoSuchAlgorithmException,
                                                     KeyManagementException {

        String result = null;
        HttpPost httpPost = new HttpPost(url);

        //解决XStream对出现双下划线的bug
        XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8",
            new XmlFriendlyNameCoder("-_", "_")));

        // xStreamForRequestPostData.aliasPackage("", "com.tencent.protocol.pay_protocol");
        xStreamForRequestPostData.aliasType("xml", WxScanPayReqData.class);

        //将要提交给API的数据对象转换成XML格式数据Post给API
        String postDataXML = xStreamForRequestPostData.toXML(xmlObj);
        logger.info("API，POST过去的数据是：" + postDataXML);

        //得指明使用UTF-8编码，否则到API服务器XML的中文不能被成功识别
        StringEntity postEntity = new StringEntity(postDataXML, "UTF-8");
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.setEntity(postEntity);

        logger.info("executing request" + httpPost.getRequestLine());

        httpClient = HttpClients.custom().build();

        //根据默认超时限制初始化requestConfig
        requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout)
            .setConnectTimeout(connectTimeout).build();

        //设置请求器的配置
        httpPost.setConfig(requestConfig);

        try {
            HttpResponse response = httpClient.execute(httpPost);

            HttpEntity entity = response.getEntity();

            result = EntityUtils.toString(entity, "UTF-8");

        } catch (ConnectionPoolTimeoutException e) {
            logger.error("http get throw ConnectionPoolTimeoutException(wait time out)");

        } catch (ConnectTimeoutException e) {
            logger.error("http get throw ConnectTimeoutException");

        } catch (SocketTimeoutException e) {
            logger.error("http get throw SocketTimeoutException");

        } catch (Exception e) {
            logger.error("http get throw Exception");

        } finally {
            httpPost.abort();
        }

        return result;
    }

    /**
     *将从API返回的XML数据映射到Java对象
     * @param xml
     * @param tClass
     * @return
     */
    @SuppressWarnings("rawtypes")
    public Object getObjectFromXML(String xml, Class tClass) {
        XStream xStreamForResponseData = new XStream();
        xStreamForResponseData.alias("xml", tClass);
        return xStreamForResponseData.fromXML(xml);
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

    /**
     * 获取客户端IP
     * @return
     */
    public String getClientIP() {
        InetAddress address;
        String clientIP = "";
        try {
            address = InetAddress.getLocalHost();
            clientIP = address.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return clientIP;
    }

    @Override
    public boolean payReturn(HttpServletRequest request) throws TradeException, UserException,
                                                        VipException {
        Map<String, String> params = getWxReturnMap(request);
        //119先注释掉
        //addWXPayLog(map);
        logger.info("web wxPay params" + params.toString());

        if (params.isEmpty()) {
            return false;
        }

        TradeOrder tradeOrder = getTradeOrder(params);
        if (tradeOrder == null) {
            return false;
        }
        String trade_status = params.get("result_code");
        // 即时到帐接口       
        if ("SUCCESS".equals(trade_status)) {
            paidTradeOrder(params, tradeOrder);
            addVip(tradeOrder);
            return true;
        } else {
            if (logger.isInfoEnabled()) {
                logger.info("Web weixin---End handling the callback result!");
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
        tradeOrder.setOutBuyerId(params.get("openid"));
        tradeOrder.setOutBuyerAccount(params.get("openid"));
        tradeOrder.setOutPayOrderNumber(params.get("transaction_id"));
        tradeOrderDao.updateById(tradeOrder);
        if (logger.isInfoEnabled()) {
            logger.info("Web wxpay---End handling the callback result!");
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

        if (logger.isInfoEnabled()) {
            logger.info("Web Wxpay---Start handling the callback/notify result!");
        }

        if (logger.isInfoEnabled()) {
            logger.info("Web Wxpay---The callback result params are " + params.toString());
        }
        //返回结果
        String return_code = params.get("return_code");

        if (return_code.equals("FAIL")) {
            logger.info("Web Wxpay---The reason of result fail " + params.get("return_msg"));
            throw new PayReturnVerifyException(params.get("return_msg"));
        }

        //计算得出通知验证结果
        boolean verifySuccess = false;
        try {
            verifySuccess = checkIsSignValidFromResponseMap(params);
        } catch (ParserConfigurationException e) {
            logger.error("", e);
        } catch (IOException e) {
            logger.error("", e);
        } catch (SAXException e) {
            logger.error("", e);
        }

        if (!verifySuccess) {
            throw new PayReturnVerifyException("Web Wxpay---参数验证失败！");
        }

        TradeOrder tradeOrder = tradeOrderDao.selectById(orderNumber);

        if (tradeOrder == null) {
            throw new PayRecordNotFoundException();
        }
        if (logger.isInfoEnabled()) {
            logger.info("Web Wxpay----The trade status is " + params.get("result_code") + "!");
        }
        if (tradeOrder.getPaidTime() != null) {
            return null;
        }
        return tradeOrder;
    }

    /**
     *微信异步通知参数转成map
     * @param request
     * @throws IOException
     * @throws DocumentException
     */
    @SuppressWarnings("unchecked")
    private Map<String, String> getWxReturnMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            // 解析结果存储在HashMap
            InputStream inputStream = request.getInputStream();
            logger.info("Wx Pay return params inputStream" + inputStream.toString());
            // 读取输入流
            SAXReader reader = new SAXReader();
            logger.info("Wx Pay return params reader" + reader.toString());
            Document document = reader.read(inputStream);
            logger.info("Wx Pay return params" + document.toString());
            // 得到xml根元素
            Element root = document.getRootElement();
            // 得到根元素的所有子节点
            List<Element> elementList = root.elements();

            // 遍历所有子节点
            for (Element e : elementList)
                map.put(e.getName(), e.getText());

            // 释放资源
            inputStream.close();

        } catch (IOException e) {
            logger.error("", e);
        } catch (DocumentException e) {
            logger.error("", e);
        }
        return map;
    }

    /**
     * 检验API返回的数据里面的签名是否合法，避免数据在传输的过程中被第三方篡改
     * @param responseString API返回的XML数据字符串
     * @return API签名是否合法
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public boolean checkIsSignValidFromResponseString(String responseString)
                                                                            throws ParserConfigurationException,
                                                                            IOException,
                                                                            SAXException {

        Map<String, Object> map = XMLParser.getMapFromXML(responseString);

        String signFromAPIResponse = map.get("sign").toString();
        if (signFromAPIResponse == "" || signFromAPIResponse == null) {
            logger.info("API返回的数据签名数据不存在，有可能被第三方篡改!!!");
            return false;
        }

        //清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名
        map.put("sign", "");

        //将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
        String signForAPIResponse = WeixinSign.getSign(map);

        if (!signForAPIResponse.equals(signFromAPIResponse)) {
            //签名验不过，表示这个API返回的数据有可能已经被篡改了
            return false;
        }
        return true;
    }

    /**
     * 检验API返回的数据里面的签名是否合法，避免数据在传输的过程中被第三方篡改
     * @param responseString API返回的XML数据字符串
     * @return API签名是否合法
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public boolean checkIsSignValidFromResponseMap(Map<String, String> map)
                                                                           throws ParserConfigurationException,
                                                                           IOException,
                                                                           SAXException {

        String signFromAPIResponse = map.get("sign").toString();
        if (signFromAPIResponse == "" || signFromAPIResponse == null) {
            logger.info("API返回的数据签名数据不存在，有可能被第三方篡改!!!");
            return false;
        }

        //清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名
        map.put("sign", "");

        //将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
        String signForAPIResponse = getSignForMap(map);

        if (!signForAPIResponse.equals(signFromAPIResponse)) {
            //签名验不过，表示这个API返回的数据有可能已经被篡改了
            return false;
        }
        return true;
    }

    /**
     *获取微信支付签名
     * @param map
     * @return
     */
    public String getSignForMap(Map<String, String> map) {
        List<String> list = new ArrayList<String>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getValue() != "") {
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        int size = list.size();
        String[] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        result += "key=" + WxPayConfig.getKey();
        //Util.log("Sign Before MD5:" + result);
        result = MD5.MD5Encode(result).toUpperCase();
        //Util.log("Sign Result:" + result);
        return result;
    }

}
