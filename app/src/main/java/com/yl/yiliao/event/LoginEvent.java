package com.yl.yiliao.event;



public class LoginEvent {
    private boolean isSueecss;

    public boolean isSueecss() {
        return isSueecss;
    }

    public LoginEvent(boolean isSueecss){
        this.isSueecss = isSueecss;
    }
}
