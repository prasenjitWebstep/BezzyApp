package com.bezzy.Ui.View.model;

public class Notification_item {
    String img;
    String type;
    String descrip;
    String fromId;
    String friendrequestStatus;

    public Notification_item(String img, String type, String descrip, String fromId, String friendrequestStatus) {
        this.img = img;
        this.type = type;
        this.descrip = descrip;
        this.fromId = fromId;
        this.friendrequestStatus = friendrequestStatus;
    }

    public String getImg() {
        return img;
    }

    public String getType() {
        return type;
    }

    public String getDescrip() {
        return descrip;
    }

    public String getFromId() {
        return fromId;
    }

    public String getFriendrequestStatus() {
        return friendrequestStatus;
    }
}
