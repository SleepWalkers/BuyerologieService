package com.buyerologie.trade.pay;

public class WxPayConfig {

    //这个就是自己要保管好的私有Key了（切记只能放在自己的后台代码里，不能放在任何可能被看到源代码的客户端程序中）
    // 每次自己Post数据给API的时候都要用这个key来对所有字段进行签名，生成的签名会放在Sign这个字段，API收到Post数据的时候也会用同样的签名算法对Post过来的数据进行签名和验证
    // 收到API的返回的时候也要用这个key来对返回的数据算下签名，跟API的Sign数据进行比较，如果值不一致，有可能数据被第三方给篡改

    private static String  key                 = "B5eHlCi99IuvDBgHxCrhxFaiztHMdnmj";

    //微信分配的公众号ID（开通公众号之后可以获取到）
    private static String  appID               = "wxf931125e9a3f3600";

    //微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）
    private static String  mchID               = "1416789902";

    //是否使用异步线程的方式来上报API测速，默认为异步模式
    private static boolean useThreadToDoReport = true;

    //HTTPS证书密码，默认密码等于商户号MCHID
    private static String  certPassword        = "1253096501";

    //机器IP
    private static String  ip                  = "";

    public static String   input_charset       = "utf-8";

    //1）统一下订单API
    public static String   PAY_API             = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    public static boolean isUseThreadToDoReport() {
        return useThreadToDoReport;
    }

    public static void setUseThreadToDoReport(boolean useThreadToDoReport) {
        WxPayConfig.useThreadToDoReport = useThreadToDoReport;
    }

    public static void setKey(String key) {
        WxPayConfig.key = key;
    }

    public static void setAppID(String appID) {
        WxPayConfig.appID = appID;
    }

    public static void setMchID(String mchID) {
        WxPayConfig.mchID = mchID;
    }

    public static void setIp(String ip) {
        WxPayConfig.ip = ip;
    }

    public static String getCertPassword() {
        return certPassword;
    }

    public static String getKey() {
        return key;
    }

    public static String getAppid() {
        return appID;
    }

    public static String getMchid() {
        return mchID;
    }

    public static String getIP() {
        return ip;
    }

}
