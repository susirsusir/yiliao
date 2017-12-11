package com.yl.yiliao.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.File;


public class UpFileRequestUtils {



    public static void uploadingAvator(Context mContext, String imgPath, RequestCallBack<String> callBack) {
        RequestParams params = getRequestParams(mContext);
        if (TextUtils.isEmpty(imgPath)) {
            Toast.makeText(mContext, "头像修改失败", Toast.LENGTH_SHORT).show();
            return;
        }
        params.addBodyParameter("uploadfile", new File(imgPath), "multipart/form-data");
        new HttpUtils().send(HttpRequest.HttpMethod.POST, Constants.UP_AVATER, params, callBack);
    }


    private static RequestParams getRequestParams(Context mContext) {
        RequestParams params = new RequestParams();
        params.addHeader("Auth", SPUtils.getPhoneNo(mContext) + "_" + SPUtils.getToken(mContext) + "_" + SPUtils.getImei(mContext));
        return params;
    }

}
