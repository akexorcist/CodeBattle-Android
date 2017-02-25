package com.firebasedevday.codebattle.chat.model;

/**
 * Created by Akexorcist on 2/19/2017 AD.
 */

public class Message {
    public static final String TYPE_IMAGE = "image";
    public static final String TYPE_TEXT = "text";

    private String avatar;
    private String data;
    private String type;
    private String username;
    private String senderId;

    public Message() {
    }

    public String getAvatar() {
        return avatar;
    }

    public Message setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    public String getData() {
        return data;
    }

    public Message setData(String data) {
        this.data = data;
        return this;
    }

    public String getType() {
        return type;
    }

    public Message setType(String type) {
        this.type = type;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public Message setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getSenderId() {
        return senderId;
    }

    public Message setSenderId(String senderId) {
        this.senderId = senderId;
        return this;
    }
}
