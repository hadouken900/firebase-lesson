package com.example.randomname; //поменять название пакета на свой

import java.util.Date;

public class Message {
    private String content;
    private String date;

    public Message(String content) {
        this.content = content;
        Date date = new Date();
        int hours = date.getHours();
        int minutes = date.getMinutes();
        int seconds = date.getSeconds();

        this.date = String.format("%02d:%02d:%02d",hours,minutes,seconds);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
