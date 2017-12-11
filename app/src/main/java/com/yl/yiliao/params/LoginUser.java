package com.yl.yiliao.params;


public class LoginUser {
    private String PhoneNo;
    private String ValidateCode;
    private String Token;
    private String Imei;

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public void setValidateCode(String validateCode) {
        ValidateCode = validateCode;
    }

    public void setToken(String token) {
        Token = token;
    }

    public void setImei(String imei) {
        Imei = imei;
    }

    @Override
    public String toString() {
        return '{' +
                "\"PhoneNo\":\"" + PhoneNo + '\"' +
                ", \"ValidateCode\":\"" + ValidateCode + '\"' +
                ", \"Token\":\"" + Token + '\"' +
                ", \"Imei\":\"" + Imei + '\"' +
                '}';
    }
}
