package com.yl.yiliao.model;


import java.io.Serializable;

public class City implements Serializable{
    private static final long serialVersionUID = 1848446596725271821L;
    private String Name;
    private String Code;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }
}
