package com.buyerologie.trade.pay.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import com.buyerologie.trade.pay.WxPayConfig;
import com.buyerologie.trade.pay.utils.sign.MD5;

public class WeixinSign {

    /**
     *获取微信支付签名
     * @param map
     * @return
     */
    public static String getSign(Map<String, Object> map) {
        ArrayList<String> list = new ArrayList<String>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
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
