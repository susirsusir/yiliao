package com.yl.yiliao.model;


import java.io.Serializable;
import java.util.List;

public class Province implements Serializable {

    private static final long serialVersionUID = -1514560118308525758L;
    private String Name;
    private String Code;
    private List<City> City;

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

    public List<City> getCity() {
        return City;
    }

    public void setCity(List<City> city) {
        City = city;
    }
}
