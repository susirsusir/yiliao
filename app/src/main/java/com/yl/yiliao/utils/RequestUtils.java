package com.yl.yiliao.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.yl.yiliao.application.YLApplication;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;



public class RequestUtils {
    public static final int Method_POST = 1;
    public static final int Method_GET = 0;
    public static final int Method_NOTYPE = 2;

    private String url;
    private RequestCallback callback;
    private Context mContext;
    private String TAG;

    public RequestUtils(Context mContext, String TAG, String url, RequestCallback callback) {
        this.url = url;
        this.callback = callback;
        this.mContext = mContext;
        this.TAG = TAG;
        Log.e(TAG, url);
        if (mContext != null) {
            if (isNetworkAvailable(mContext)) {
                getStringRequest();
            } else {
                showHint();
            }
        } else {
            showHint();
        }


    }


    private Object object;

    public RequestUtils(Context mContext, String TAG, String url, Object object, RequestCallback callback) {
        this.url = url;
        this.object = object;
        this.callback = callback;
        this.mContext = mContext;
        this.TAG = TAG;
        Log.e(TAG, url);
        if (mContext != null) {
            if (isNetworkAvailable(mContext)) {
                postJsonRequest();
            } else {
                showHint();
            }
        } else {
            showHint();
        }


    }

    private void showHint() {
        if (mContext != null) {
            Toast.makeText(mContext, "没有网路连接，请稍后重试", Toast.LENGTH_SHORT).show();
        }
        callback.fail(null);
    }


    private void getStringRequest() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.e(TAG, "success result :" + s);
                        callback.success(s);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, "VolleyError :" + volleyError.toString());

                callback.fail(volleyError);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Auth", SPUtils.getPhoneNo(mContext) + "_" + SPUtils.getToken(mContext) + "_" + SPUtils.getImei(mContext));
                map.put("Content-type", "application/json");

                return map;
            }
        };
        YLApplication.mRequestQueue.add(stringRequest);
    }


    private void postJsonRequest() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e(TAG, "success result " + jsonObject.toString());
                        callback.success(jsonObject.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
//                        Log.e("Error:", volleyError.getMessage());
                        Log.e(TAG, "VolleyError " + volleyError.toString());
                        callback.fail(volleyError);
                    }
                }) {

            @Override
            public byte[] getBody() {

                try {
                    return object.toString().getBytes("UTF-8");

                } catch (Exception e) {
                }
                return null;
            }
        };

        YLApplication.mRequestQueue.add(jsonObjectRequest);

    }

    public static interface RequestCallback {
        void success(String result);

        void fail(VolleyError volleyError);
    }


    public boolean isNetworkAvailable(Context context) {
//        Context appContext = context.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (manager == null) {
            return false;
        } else {
            if (manager.getActiveNetworkInfo() != null) {
                return manager.getActiveNetworkInfo().isAvailable();
            }
        }
        return false;
    }

}
