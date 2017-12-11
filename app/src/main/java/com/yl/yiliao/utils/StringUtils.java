package com.yl.yiliao.utils;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;

import com.yl.yiliao.R;
import com.yl.yiliao.activity.LoginActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class StringUtils {


    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {

        String telRegex = "[1][34578]\\d{9}";
        if (TextUtils.isEmpty(mobiles)) {
            return false;
        } else {
            return mobiles.matches(telRegex);
        }
    }




    /**
     * URL格式编码转换
     */
    public static String encode(String str) {
        return URLEncoder.encode(str);
    }

    public static String decode(String str) {
        return URLDecoder.decode(str);
    }




    // "ro.boot.serialno", "ro.serialno"
    // 获取imei号 实际上为sn号
    public static String getImei() {
        String ret;
        try {
            Method systemProperties_get = Class.forName("android.os.SystemProperties").getMethod("get", String.class);
            if ((ret = (String) systemProperties_get.invoke(null, "ro.boot.serialno")) != null)
                return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return "";
    }

    /**
     * 注册点击事件
     */
    public static void setRegisterText(Context mContext, TextView tv) {
        StringBuilder actionText = new StringBuilder();
         actionText.append("还没有译聊账号？ 赶紧来" + "<a href='注册'>注册</a>" + "一个吧！");
        tv.setText(Html.fromHtml(actionText.toString()));
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        CharSequence text = tv.getText();
        Spannable spannable = (Spannable) tv.getText();
        URLSpan[] urlspan = spannable.getSpans(0, text.length(), URLSpan.class);
        SpannableStringBuilder stylesBuilder = new SpannableStringBuilder(text);
        stylesBuilder.clearSpans();
        for (URLSpan url : urlspan) {
            TextViewURLSpan myURLSpan = new TextViewURLSpan(mContext);
            stylesBuilder.setSpan(myURLSpan, spannable.getSpanStart(url),
                    spannable.getSpanEnd(url), spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        tv.setText(stylesBuilder);
        // 设置点击时背景色为透明（系统默认4.0之后淡绿色，之前黄色）
        tv.setHighlightColor(mContext.getResources().getColor(android.R.color.transparent));

    }


    private static class TextViewURLSpan extends ClickableSpan {
        private Context mContext;

        public TextViewURLSpan(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
             ds.setColor(mContext.getResources().getColor(R.color.cursor_color));
            ds.setUnderlineText(true); //去掉下划线
            ds.clearShadowLayer();
        }

        @Override
        public void onClick(View widget) {
            Intent intent = new Intent(mContext, LoginActivity.class);
            intent.putExtra("isLogin", false);
            mContext.startActivity(intent);
            ((Activity) mContext).overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            if(mContext instanceof LoginActivity){
                ((Activity) mContext).finish();
            }
        }
    }

    /**
     * 文件转base64字符串
     * @param file
     * @return
     */
    public static String fileToBase64(File file) {
        String base64 = null;
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            byte[] bytes = new byte[in.available()];
            int length = in.read(bytes);
            base64 = Base64.encodeToString(bytes, 0, length, Base64.NO_WRAP); // 做一下特殊说明，这里通常使用DEFAULT，但在这里使用NO_WRAP是为了去除换行符
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return base64;
    }

}
