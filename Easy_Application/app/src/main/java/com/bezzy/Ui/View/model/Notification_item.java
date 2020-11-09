package com.bezzy.Ui.View.model;

public class Notification_item {
    String img;
    String type;
    String descrip;
    String fromId;

    public Notification_item(String type, String descrip,String fromId) {
        this.type = type;
        this.descrip = descrip;
        this.fromId = fromId;
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

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    public String getFromId() {
        return fromId;
    }
}
