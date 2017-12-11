package com.yl.yiliao.event;


public class ChangeCountryEvent {
    private String country;
    public ChangeCountryEvent(String country){
        this.country = country;
    }

    public String getCountry() {
        return country;
    }
}
