package com.test.store.util;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;

public class StatusCodeUtils {
    public final static int SUCCESS_1 = 1;
    public final static int SUCCESS_0 = 0;
    public final static int USERNAME_ERROR = -1;
    public final static int PASSWORD_ERROR = -2;
    public final static int STOCK_ERROR = -3;
    public final static int DEL_ERROR = -4;
    public final static int DATA_IS_EMPTY = -5;

    public static String getCodeJsonString(int code) {
        HashMap<String, Integer> message = new HashMap<String, Integer>();
        message.put("code",code);
        return JSON.toJSONString(message);
    }
}
