package com.hb.server.logic;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;


/**
 * Created by hanbin on 2018/2/5.
 * <p/>
 * 逻辑层基类
 */
public class BaseLogic {


    public JSONObject onSuccess(JSONObject data, String message) {
        return onResult(200, 10, message, data);
    }

    public JSONObject onError(int code, String message) {
        return onResult(code, 10, message, new JSONObject());
    }

    private JSONObject onResult(int result, int code, String message, JSONObject data) {
        JSONObject resultJson = new JSONObject();
        resultJson.put("result", result);
        resultJson.put("code", code);
        resultJson.put("message", TextUtils.isEmpty(message) ? "ok" : message);
        resultJson.put("data", data);
        return resultJson;
    }

}
