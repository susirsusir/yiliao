package com.yl.yiliao.model.database;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class ChatItem {
    //不能用int
    @Id(autoincrement = true)
    private Long n_id;

    // 发送或接收时间
    private String create_at;

    // 发送或者接收 0发送 1接收
    private int sourceType;

    // 发送方手机号
    private String sendPhone;

    // 接收方手机号
    private String receivePhone;

    // 原文本
    private String fromContent;
    // 原语音
    private String fromVoice;
    // 翻译后文本
    private String toContent;
    // 翻译后语音
    private String toVoice;
    @Generated(hash = 1305044375)
    public ChatItem(Long n_id, String create_at, int sourceType, String sendPhone,
            String receivePhone, String fromContent, String fromVoice,
            String toContent, String toVoice) {
        this.n_id = n_id;
        this.create_at = create_at;
        this.sourceType = sourceType;
        this.sendPhone = sendPhone;
        this.receivePhone = receivePhone;
        this.fromContent = fromContent;
        this.fromVoice = fromVoice;
        this.toContent = toContent;
        this.toVoice = toVoice;
    }
    @Generated(hash = 1079613128)
    public ChatItem() {
    }
    public Long getN_id() {
        return this.n_id;
    }
    public void setN_id(Long n_id) {
        this.n_id = n_id;
    }
    public String getCreate_at() {
        return this.create_at;
    }
    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }
    public int getSourceType() {
        return this.sourceType;
    }
    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }
    public String getSendPhone() {
        return this.sendPhone;
    }
    public void setSendPhone(String sendPhone) {
        this.sendPhone = sendPhone;
    }
    public String getReceivePhone() {
        return this.receivePhone;
    }
    public void setReceivePhone(String receivePhone) {
        this.receivePhone = receivePhone;
    }
    public String getFromContent() {
        return this.fromContent;
    }
    public void setFromContent(String fromContent) {
        this.fromContent = fromContent;
    }
    public String getFromVoice() {
        return this.fromVoice;
    }
    public void setFromVoice(String fromVoice) {
        this.fromVoice = fromVoice;
    }
    public String getToContent() {
        return this.toContent;
    }
    public void setToContent(String toContent) {
        this.toContent = toContent;
    }
    public String getToVoice() {
        return this.toVoice;
    }
    public void setToVoice(String toVoice) {
        this.toVoice = toVoice;
    }
    
}
