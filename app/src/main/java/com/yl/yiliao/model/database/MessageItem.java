package com.yl.yiliao.model.database;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class MessageItem {
    //不能用int
    @Id(autoincrement = true)
    private Long n_id;
    // 发送或接收时间
    private String create_at;

    // 内容
    private String txt_content;

    // 接收方手机号  永远是自己 用来做校验
    private String receivePhone;

    // 发送方手机号
    private String sendPhone;
    // 发送方昵称
    private String sendName;
    // 发送方头像
    private String sendAvatar;
    // 未读消息条数
    private int count;
    @Generated(hash = 427548514)
    public MessageItem(Long n_id, String create_at, String txt_content,
            String receivePhone, String sendPhone, String sendName,
            String sendAvatar, int count) {
        this.n_id = n_id;
        this.create_at = create_at;
        this.txt_content = txt_content;
        this.receivePhone = receivePhone;
        this.sendPhone = sendPhone;
        this.sendName = sendName;
        this.sendAvatar = sendAvatar;
        this.count = count;
    }
    @Generated(hash = 1158896028)
    public MessageItem() {
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
    public String getTxt_content() {
        return this.txt_content;
    }
    public void setTxt_content(String txt_content) {
        this.txt_content = txt_content;
    }
    public String getReceivePhone() {
        return this.receivePhone;
    }
    public void setReceivePhone(String receivePhone) {
        this.receivePhone = receivePhone;
    }
    public String getSendPhone() {
        return this.sendPhone;
    }
    public void setSendPhone(String sendPhone) {
        this.sendPhone = sendPhone;
    }
    public String getSendName() {
        return this.sendName;
    }
    public void setSendName(String sendName) {
        this.sendName = sendName;
    }
    public String getSendAvatar() {
        return this.sendAvatar;
    }
    public void setSendAvatar(String sendAvatar) {
        this.sendAvatar = sendAvatar;
    }
    public int getCount() {
        return this.count;
    }
    public void setCount(int count) {
        this.count = count;
    }
   
}
