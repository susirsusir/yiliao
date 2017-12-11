package com.yl.yiliao.model;


/**
 * Catalog  string  // "audio" 或者"text"  "AddFriendRequest"
 Time     string // 1510312224，https://unixtime.51240.com/
 FromUser string //phoneno of sender
 ToUser   string // phoneno of recver

 FromLang string  //源语言"auto", "zh", "en"
 ToLang   string // 目标语言"auto", "zh", "en"

 FromText  string  // 源文本
 FromAudio string // base64编码的amr文件, 16kbps

 ToText     string //目标文本
 ToAudioUrl string // amr文件到下载地址URL
 */

public class ChatMsgJson {

    private String Catalog = "";
    private String Time = "";
    private String FromUser = "";
    private String ToUser = "";
    private String FromLang = "";
    private String ToLang = "";
    private String FromText = "";
    private String FromAudio = "";
    private String ToText = "";
    private String ToAudioUrl = "";
    private String FromPhoneNo = "";
    private String ToPhoneNo = "";

    public String getFromPhoneNo() {
        return FromPhoneNo;
    }

    public void setFromPhoneNo(String fromPhoneNo) {
        FromPhoneNo = fromPhoneNo;
    }

    public String getToPhoneNo() {
        return ToPhoneNo;
    }

    public void setToPhoneNo(String toPhoneNo) {
        ToPhoneNo = toPhoneNo;
    }

    public String getCatalog() {
        return Catalog;
    }

    public void setCatalog(String catalog) {
        Catalog = catalog;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getFromUser() {
        return FromUser;
    }

    public void setFromUser(String fromUser) {
        FromUser = fromUser;
    }

    public String getToUser() {
        return ToUser;
    }

    public void setToUser(String toUser) {
        ToUser = toUser;
    }

    public String getFromLang() {
        return FromLang;
    }

    public void setFromLang(String fromLang) {
        FromLang = fromLang;
    }

    public String getToLang() {
        return ToLang;
    }

    public void setToLang(String toLang) {
        ToLang = toLang;
    }

    public String getFromText() {
        return FromText;
    }

    public void setFromText(String fromText) {
        FromText = fromText;
    }

    public String getFromAudio() {
        return FromAudio;
    }

    public void setFromAudio(String fromAudio) {
        FromAudio = fromAudio;
    }

    public String getToText() {
        return ToText;
    }

    public void setToText(String toText) {
        ToText = toText;
    }

    public String getToAudioUrl() {
        return ToAudioUrl;
    }

    public void setToAudioUrl(String toAudioUrl) {
        ToAudioUrl = toAudioUrl;
    }
}
