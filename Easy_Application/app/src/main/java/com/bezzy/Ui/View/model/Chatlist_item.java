package com.bezzy.Ui.View.model;

public class Chatlist_item {
    private String userID;
    private String userName;
    private String lastmsg;
    private String date;
    private String urlProfile;

    public Chatlist_item(String userID, String userName, String lastmsg, String date, String urlProfile) {
        this.userID = userID;
        this.userName = userName;
        this.lastmsg = lastmsg;
        this.date = date;
        this.urlProfile = urlProfile;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLastmsg() {
        return lastmsg;
    }

    public void setLastmsg(String lastmsg) {
        this.lastmsg = lastmsg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrlProfile() {
        return urlProfile;
    }

    public void setUrlProfile(String urlProfile) {
        this.urlProfile = urlProfile;
    }
}
