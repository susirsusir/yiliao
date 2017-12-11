package com.yl.yiliao.event;


import com.yl.yiliao.model.ChatMsgJson;

public class ChatItemEvent {
    private String publishTopic;
    private ChatMsgJson chatMsgJson;

    public String getPublishTopic() {
        return publishTopic;
    }

    public void setPublishTopic(String publishTopic) {
        this.publishTopic = publishTopic;
    }

    public ChatMsgJson getChatMsgJson() {
        return chatMsgJson;
    }

    public void setChatMsgJson(ChatMsgJson chatMsgJson) {
        this.chatMsgJson = chatMsgJson;
    }
}
