package com.yl.yiliao.params;



public class WelLoginUser {

    private String PhoneNo;
    private String Token;
    private String Imei;

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getImei() {
        return Imei;
    }

    public void setImei(String imei) {
        Imei = imei;
    }

    @Override
    public String toString() {
        return '{' +
                "\"PhoneNo\":\"" + PhoneNo + '\"' +
                ", \"Token\":\"" + Token + '\"' +
                ", \"Imei\":\"" + Imei + '\"' +
                '}';
    }
}
