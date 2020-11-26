package com.bezzy.Ui.View.model;

public class Chatlist_item {
    private String userID;
    private String userName;
    private String lastmsg;
    private String date;
    private String unreadmsg;
    private String userimage;
    private String activeStatus;

    public Chatlist_item(String userID, String userName, String lastmsg, String date, String unreadmsg, String userimage, String activeStatus) {
        this.userID = userID;
        this.userName = userName;
        this.lastmsg = lastmsg;
        this.date = date;
        this.unreadmsg = unreadmsg;
        this.userimage = userimage;
        this.activeStatus = activeStatus;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getLastmsg() {
        return lastmsg;
    }

    public String getDate() {
        return date;
    }

    public String getUnreadmsg() {
        return unreadmsg;
    }

    public String getImage() {
        return userimage;
    }

    public String getActiveStatus() {
        return activeStatus;
    }
}
