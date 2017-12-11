package com.yl.yiliao.utils;


public class Constants {
    public static final String[] fromLanguageType = {"auto", "zh", "en", "yue", "wyw", "jp", "kor", "fra", "spa", "th", "ara", "ru", "pt", "de", "it", "el", "nl", "pl", "bul", "est", "dan", "fin", "cs", "rom"
            , "slo", "swe", "hu", "cht"};
    public static final String[] fromLanguageName = {"自动检测", "中文", "英语", "粤语", "文言文", "日语", "韩语", "法语", "西班牙语", "泰语", "阿拉伯语", "俄语", "葡萄牙语", "德语"
            , "意大利语", "希腊语", "荷兰语", "波兰语", "保加利亚语", "爱沙尼亚语", "丹麦语", "芬兰语", "捷克语", "罗马尼亚语", "斯洛文尼亚语"
            , "瑞典语", "匈牙利语", "繁体中文"};

    public static final String[] toLanguageType = {"en", "zh", "yue", "wyw", "jp", "kor", "fra", "spa", "th", "ara", "ru", "pt", "de", "it", "el", "nl", "pl", "bul", "est", "dan", "fin", "cs", "rom"
            , "slo", "swe", "hu", "cht"};
    public static final String[] toLanguageName = {"英语", "中文", "粤语", "文言文", "日语", "韩语", "法语", "西班牙语", "泰语", "阿拉伯语", "俄语", "葡萄牙语", "德语"
            , "意大利语", "希腊语", "荷兰语", "波兰语", "保加利亚语", "爱沙尼亚语", "丹麦语", "芬兰语", "捷克语", "罗马尼亚语", "斯洛文尼亚语"
            , "瑞典语", "匈牙利语", "繁体中文"};

    public static final int CHAT_ITEM_TYPE_SEND = 0;
    public static final int CHAT_ITEM_TYPE_RECEIVE = 1;
    public static final int FRAGMENT_MAIN = 0;
    public static final int FRAGMENT_MSG = 1;
    public static final int FRAGMENT_FRID = 2;
    public static final int FRAGMENT_MINE = 3;


    // url统一字段
    public static String BASE_URL = "http://27.155.100.158:3389";
    public static String BASE_SERVICE = "tcp://27.155.100.158:1883";

    // 获取验证码
    public static String GET_VALIDATE_CODE = BASE_URL + "/users/register/get_validate_code?phoneNo=";

    // 注册
    public static String REGISTER = BASE_URL + "/users/register";

    // 登陆
    public static String LOGIN = BASE_URL + "/users/login";

    // 获取用户信息
    public static String GET_SELF_INFO = BASE_URL + "/users/get_self_info";

    // 查看二维码
    public static String GET_QRCODE = BASE_URL + "/get_qrcode";

    // 修改用户信息
    public static String CHANGE_USER_INFO = BASE_URL + "/users/change_user_info";

    // 查找用户信息
    public static String GET_OTHER_USER_INFO = BASE_URL + "/users/get_user_info?phoneNo=";

    // 好友添加
    public static String ADD_FRIEND = BASE_URL + "/friends/add_friend?phoneNo=";

    // 获取好友列表
    public static String GET_FRIEND_LIST = BASE_URL + "/friends/get_list";

    // 上传头像
    public static String UP_AVATER = BASE_URL + "/upload/photo";

    // 翻译
    public static String TRANSLATE = BASE_URL + "/translate";

    // 获取语言列表
    public static String GET_LANG_CODE = BASE_URL + "/get_lang_code?type=text";

    // 获取语言列表
    public static String GET_LANG_CODE_VOICE = BASE_URL + "/get_lang_code?type=audio";

    // 获取国家代码
    public static String GET_PHONE_CODE = BASE_URL + "/get_phone_code";

    // 获取gps
    public static String GET_GPS = BASE_URL + "/get_gps_region?gps=";

    // 获取国家地区
    public static String GET_REGION_LIST = BASE_URL + "/get_region_list";

}
