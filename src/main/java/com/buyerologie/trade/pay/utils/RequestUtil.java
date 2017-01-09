package com.buyerologie.trade.pay.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.buyerologie.trade.pay.AlipayConfig;
import com.buyerologie.trade.pay.enums.SignType;
import com.buyerologie.trade.pay.utils.sign.MD5;

public class RequestUtil {

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String buildParams(Map<String, String> params) {
        if (params == null) {
            return "";
        }
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        StringBuffer paramStr = new StringBuffer();
        for (int i = 0; i < params.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (StringUtils.isBlank(value)) {
                continue;
            }
            paramStr.append(key);
            paramStr.append("=");
            paramStr.append(value);
            if (i != params.size() - 1) {
                paramStr.append("&");
            }
        }
        return paramStr.toString();
    }

    /** 
     * 除去数组中的空值和签名参数
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, String> paramFilter(Map<String, String> sArray) {

        Map<String, String> result = new HashMap<String, String>();

        if (sArray == null || sArray.size() <= 0) {
            return result;
        }

        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (StringUtils.isBlank(value) || key.equalsIgnoreCase("sign")
                || key.equalsIgnoreCase("sign_type")) {
                continue;
            }
            result.put(key, value);
        }

        return result;
    }

    /**
     * 生成签名结果
     * @param sPara 要签名的数组
     * @return 签名结果字符串
     */
    public static String buildRequestSign(Map<String, String> sPara, SignType signType) {
        String signStr = "";
        if (signType == null) {
            return signStr;
        }
        String prestr = RequestUtil.buildParams(sPara); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        switch (signType) {
            case MD5: {
                signStr = MD5.sign(prestr, AlipayConfig.key, AlipayConfig.input_charset);
                break;
            }
            default:
                break;
        }
        return signStr;

    }

    public static void main(String[] args) {

    }
}
