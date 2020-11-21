package com.bezzy.Ui.View.model;

public class ChatMessageModel {
    public String message_by,chat_message,chat_date_time;

    public ChatMessageModel(String message_by, String chat_message, String chat_date_time) {
        this.message_by = message_by;
        this.chat_message = chat_message;
        this.chat_date_time = chat_date_time;
    }

    public String getMessage_by() {
        return message_by;
    }

    public String getChat_message() {
        return chat_message;
    }

    public String getChat_date_time() {
        return chat_date_time;
    }
}
