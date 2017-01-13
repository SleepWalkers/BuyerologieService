package com.buyerologie.trade.pay.imp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.text.DecimalFormat;
import java.util.Map;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import com.buyerologie.core.spring.context.SystemContext;
import com.buyerologie.trade.pay.PayService;
import com.buyerologie.trade.pay.WxPayConfig;
import com.buyerologie.trade.pay.WxScanPayReqData;
import com.buyerologie.trade.pay.WxScanPayResData;
import com.buyerologie.trade.pay.exception.PayException;
import com.buyerologie.trade.pay.utils.WeixinSign;
import com.buyerologie.trade.pay.utils.XMLParser;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

@Service("weixinPayService")
public class WeixinPayService implements PayService {

    private Logger              logger         = LoggerFactory.getLogger(WeixinPayService.class);

    //请求器的配置
    private RequestConfig       requestConfig;

    //服务器异步通知页面路径
    private final String        NOTIFY_URL     = "/weixin/pay_return";

    //连接超时时间，默认10秒
    private int                 socketTimeout  = 10000;

    //传输超时时间，默认30秒
    private int                 connectTimeout = 30000;

    private final String        TRADE_TYPE     = "NATIVE";

    //HTTP请求器
    private CloseableHttpClient httpClient;

    //商品或支付单简要描述
    private final String        BODY           = "尚课网的订单";

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
     *将从API返回的XML数据映射到Java对象
     * @param xml
     * @param tClass
     * @return
     */
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

}
