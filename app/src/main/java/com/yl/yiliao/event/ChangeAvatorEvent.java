package com.yl.yiliao.event;


public class ChangeAvatorEvent {
    private String imgPath;

    public ChangeAvatorEvent(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getImgPath() {
        return imgPath;
    }
}
