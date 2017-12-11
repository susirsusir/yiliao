package com.yl.yiliao.utils;

import com.google.gson.Gson;


public class GsonUtils {

    public static <T> T getResult(String json, Class<T> classOfT) {
        T object = null;
        try {
            object = new Gson().fromJson(json, classOfT);
        } catch (Exception e) {
        }
        return object;
    }

}
