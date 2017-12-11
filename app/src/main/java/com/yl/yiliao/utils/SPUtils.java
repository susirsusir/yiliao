package com.yl.yiliao.utils;


import android.content.Context;
import android.content.SharedPreferences;

public class SPUtils {


    // 保存用户手机号 包含区号
    public static String getPhoneNo(Context context) {
        SharedPreferences sp = context.getSharedPreferences("yiliao", Context.MODE_PRIVATE);
        return sp.getString("PhoneNo", "");
    }

    public static void setPhoneNo(String phoneNo, Context context) {
        SharedPreferences sp = context.getSharedPreferences("yiliao", Context.MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit();
        et.putString("PhoneNo", phoneNo);
        et.commit();
    }

    // 保存用户头像 包含区号
    public static String getAvater(String phone, Context context) {
        SharedPreferences sp = context.getSharedPreferences("yiliao", Context.MODE_PRIVATE);
        return sp.getString("Avater" + phone, "");
    }

    public static void setAvater(String phone, String avater, Context context) {
        SharedPreferences sp = context.getSharedPreferences("yiliao", Context.MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit();
        et.putString("Avater" + phone, avater);
        et.commit();
    }


    // 保存token以此识别是否登陆
    public static String getToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences("yiliao", Context.MODE_PRIVATE);
        return sp.getString("token", "");
    }

    public static void setToken(String sn, Context context) {
        SharedPreferences sp = context.getSharedPreferences("yiliao", Context.MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit();
        et.putString("token", sn);
        et.commit();
    }


    // 设置软键盘高度
    public static int getSoftHight(Context context) {
        SharedPreferences sp = context.getSharedPreferences("yiliao", Context.MODE_PRIVATE);
        return sp.getInt("SoftHight", 800);
    }

    public static void setSoftHight(int softHight, Context context) {
        SharedPreferences sp = context.getSharedPreferences("yiliao", Context.MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit();
        et.putInt("SoftHight", softHight);
        et.commit();
    }


    // 设置sn
    public static String getImei(Context context) {
        SharedPreferences sp = context.getSharedPreferences("yiliao", Context.MODE_PRIVATE);
        return sp.getString("SN", "");
    }

    public static void setImei(String sn, Context context) {
        SharedPreferences sp = context.getSharedPreferences("yiliao", Context.MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit();
        et.putString("SN", sn);
        et.commit();
    }


    // 是否支持amr
    public static boolean isSupportAmr(Context context) {
        SharedPreferences sp = context.getSharedPreferences("yiliao", Context.MODE_PRIVATE);
        return sp.getBoolean("SupportAmr", true);
    }

    public static void setIsSupportAmr(boolean isSupport, Context context) {
        SharedPreferences sp = context.getSharedPreferences("yiliao", Context.MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit();
        et.putBoolean("SupportAmr", isSupport);
        et.commit();
    }


    // 文本翻译 源语言name保存
    public static String getTextFromName(Context context) {
        SharedPreferences sp = context.getSharedPreferences("yiliao", Context.MODE_PRIVATE);
        return sp.getString("TextFromName", "");
    }

    public static void setTextFromName(String name, Context context) {
        SharedPreferences sp = context.getSharedPreferences("yiliao", Context.MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit();
        et.putString("TextFromName", name);
        et.commit();
    }

    // 文本翻译 源语言type保存
    public static String getTextFromType(Context context) {
        SharedPreferences sp = context.getSharedPreferences("yiliao", Context.MODE_PRIVATE);
        return sp.getString("TextFromType", "");
    }

    public static void setTextFromType(String type, Context context) {
        SharedPreferences sp = context.getSharedPreferences("yiliao", Context.MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit();
        et.putString("TextFromType", type);
        et.commit();
    }

    // 文本翻译 目标语言name保存
    public static String getTextToName(Context context) {
        SharedPreferences sp = context.getSharedPreferences("yiliao", Context.MODE_PRIVATE);
        return sp.getString("TextToName", "");
    }

    public static void setTextToName(String name, Context context) {
        SharedPreferences sp = context.getSharedPreferences("yiliao", Context.MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit();
        et.putString("TextToName", name);
        et.commit();
    }

    // 文本翻译 目标语言类型保存
    public static String getTextToType(Context context) {
        SharedPreferences sp = context.getSharedPreferences("yiliao", Context.MODE_PRIVATE);
        return sp.getString("TextToType", "");
    }

    public static void setTextToType(String type, Context context) {
        SharedPreferences sp = context.getSharedPreferences("yiliao", Context.MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit();
        et.putString("TextToType", type);
        et.commit();
    }

    // 语音翻译 源语言name保存
    public static String getVoiceFromName(Context context) {
        SharedPreferences sp = context.getSharedPreferences("yiliao", Context.MODE_PRIVATE);
        return sp.getString("VoiceFromName", "");
    }

    public static void setVoiceFromName(String name, Context context) {
        SharedPreferences sp = context.getSharedPreferences("yiliao", Context.MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit();
        et.putString("VoiceFromName", name);
        et.commit();
    }

    // 语音翻译 源语言type保存
    public static String getVoiceFromType(Context context) {
        SharedPreferences sp = context.getSharedPreferences("yiliao", Context.MODE_PRIVATE);
        return sp.getString("VoiceFromType", "");
    }

    public static void setVoiceFromType(String type, Context context) {
        SharedPreferences sp = context.getSharedPreferences("yiliao", Context.MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit();
        et.putString("VoiceFromType", type);
        et.commit();
    }

    // 语音翻译 目标语言name保存
    public static String getVoiceToName(Context context) {
        SharedPreferences sp = context.getSharedPreferences("yiliao", Context.MODE_PRIVATE);
        return sp.getString("VoiceToName", "");
    }

    public static void setVoiceToName(String name, Context context) {
        SharedPreferences sp = context.getSharedPreferences("yiliao", Context.MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit();
        et.putString("VoiceToName", name);
        et.commit();
    }


    // 语音翻译 目标语言type保存
    public static String getVoiceToType(Context context) {
        SharedPreferences sp = context.getSharedPreferences("yiliao", Context.MODE_PRIVATE);
        return sp.getString("VoiceToType", "");
    }

    public static void setVoiceToType(String type, Context context) {
        SharedPreferences sp = context.getSharedPreferences("yiliao", Context.MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit();
        et.putString("VoiceToType", type);
        et.commit();
    }

    // 聊天 源语言name保存
    public static String getChatFromName(Context context, String phone) {
        SharedPreferences sp = context.getSharedPreferences("yiliao", Context.MODE_PRIVATE);
        return sp.getString("ChatFromName" + phone, "");
    }

    public static void setChatFromName(String name, String phone, Context context) {
        SharedPreferences sp = context.getSharedPreferences("yiliao", Context.MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit();
        et.putString("ChatFromName" + phone, name);
        et.commit();
    }

    // 聊天 源语言type保存
    public static String getChatFromType(Context context, String phone) {
        SharedPreferences sp = context.getSharedPreferences("yiliao", Context.MODE_PRIVATE);
        return sp.getString("ChatFromType" + phone, "");
    }

    public static void setChatFromType(String type, String phone, Context context) {
        SharedPreferences sp = context.getSharedPreferences("yiliao", Context.MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit();
        et.putString("ChatFromType" + phone, type);
        et.commit();
    }

    // 聊天 目标语言name保存
    public static String getChatToName(Context context, String phone) {
        SharedPreferences sp = context.getSharedPreferences("yiliao", Context.MODE_PRIVATE);
        return sp.getString("ChatToName" + phone, "");
    }

    public static void setChatToName(String name, String phone, Context context) {
        SharedPreferences sp = context.getSharedPreferences("yiliao", Context.MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit();
        et.putString("ChatToName" + phone, name);
        et.commit();
    }


    // 聊天 目标语言type保存
    public static String getChatToType(Context context, String phone) {
        SharedPreferences sp = context.getSharedPreferences("yiliao", Context.MODE_PRIVATE);
        return sp.getString("ChatToType" + phone, "");
    }

    public static void setChatToType(String type, String phone, Context context) {
        SharedPreferences sp = context.getSharedPreferences("yiliao", Context.MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit();
        et.putString("ChatToType" + phone, type);
        et.commit();
    }
}
