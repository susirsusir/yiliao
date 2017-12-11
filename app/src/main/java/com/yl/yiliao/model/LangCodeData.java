package com.yl.yiliao.model;


import java.util.List;

public class LangCodeData {

    private List<LangCodeItem> Data;
    private int Code;
    private String Description;

    public List<LangCodeItem> getData() {
        return Data;
    }

    public void setData(List<LangCodeItem> data) {
        Data = data;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
