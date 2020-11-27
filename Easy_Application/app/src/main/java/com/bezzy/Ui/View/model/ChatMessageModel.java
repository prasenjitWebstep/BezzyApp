package com.bezzy.Ui.View.model;

public class ChatMessageModel {
    public String message_by,chat_message,chat_date_time,chat_read_unread_status;

    public ChatMessageModel(String message_by, String chat_message, String chat_date_time, String chat_read_unread_status) {
        this.message_by = message_by;
        this.chat_message = chat_message;
        this.chat_date_time = chat_date_time;
        this.chat_read_unread_status = chat_read_unread_status;
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

    public String getChat_read_unread_status() {
        return chat_read_unread_status;
    }
}
