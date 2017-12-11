package com.yl.yiliao.model;


import java.io.Serializable;
import java.util.List;

public class CountryItem implements Serializable{
    private static final long serialVersionUID = -2152155203260305388L;
    private String Name;
    private String Code;
    private List<Province> State;

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

    public List<Province> getState() {
        return State;
    }

    public void setState(List<Province> state) {
        State = state;
    }
}
