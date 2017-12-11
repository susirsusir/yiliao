package com.yl.yiliao.event;



public class ChangeNameEvent {
    private String name;
    public ChangeNameEvent(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
