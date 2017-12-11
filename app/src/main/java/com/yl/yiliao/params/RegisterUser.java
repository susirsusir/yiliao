package com.yl.yiliao.params;



public class RegisterUser {
    private String PhoneNo;
    private String Imei;
    private String NickName;
    private String ValidateCode;

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public String getImei() {
        return Imei;
    }

    public void setImei(String imei) {
        Imei = imei;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getValidateCode() {
        return ValidateCode;
    }

    public void setValidateCode(String validateCode) {
        ValidateCode = validateCode;
    }

    @Override
    public String toString() {
        return '{' +
                "\"PhoneNo\":\"" + PhoneNo + '\"' +
                ", \"Imei\":\"" + Imei + '\"' +
                ", \"NickName\":\"" + NickName + '\"' +
                ", \"ValidateCode\":\"" + ValidateCode + '\"' +
                '}';
    }
}
