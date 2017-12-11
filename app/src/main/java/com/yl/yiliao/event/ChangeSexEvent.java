package com.yl.yiliao.event;


public class ChangeSexEvent {
    private String sex;
    public ChangeSexEvent(String sex){
        this.sex = sex;
    }

    public String getSex() {
        return sex;
    }
}
