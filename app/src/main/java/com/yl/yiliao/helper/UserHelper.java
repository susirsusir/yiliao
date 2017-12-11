package com.yl.yiliao.helper;


import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.yl.yiliao.activity.LoginActivity;
import com.yl.yiliao.utils.SPUtils;

public class UserHelper {
    public static boolean autoLogin(Context context) {
        if(isNotLogin(context)){
            context.startActivity(new Intent(context, LoginActivity.class));
            return false;
        }
        return true;
    }

    public static boolean isLogin(Context context) {
        if(isNotLogin(context)){
            return false;
        }
        return true;
    }

    public static boolean isNotLogin(Context context) {
        return TextUtils.isEmpty(SPUtils.getToken(context));
    }

}
