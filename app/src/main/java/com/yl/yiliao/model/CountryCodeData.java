package com.yl.yiliao.model;


import java.util.List;

public class CountryCodeData {
    private List<CountryCodeItem> Data;
    private int Code;
    private String Description;

    public List<CountryCodeItem> getData() {
        return Data;
    }

    public void setData(List<CountryCodeItem> data) {
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
