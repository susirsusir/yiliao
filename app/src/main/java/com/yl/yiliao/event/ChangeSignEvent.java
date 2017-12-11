package com.yl.yiliao.event;



public class ChangeSignEvent {
    private String signature;
    public ChangeSignEvent(String signature){
        this.signature = signature;
    }

    public String getSignature() {
        return signature;
    }
}
