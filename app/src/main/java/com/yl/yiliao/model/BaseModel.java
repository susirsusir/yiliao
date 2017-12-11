package com.yl.yiliao.model;



public class BaseModel <T>{
    private T Data;
    private String Description;
    private int Code;

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }
}
