package com.yl.yiliao.event;


public class AddSubScribeEvent {
    private String hostPhone;

    public String getHostPhone() {
        return hostPhone;
    }

    public AddSubScribeEvent(String hostPhone) {
        this.hostPhone = hostPhone;
    }
}
