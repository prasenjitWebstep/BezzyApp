package com.bezzy.Ui.View.model;

public class Friendsnoti_item
{
    String header,desc,img,id,user_relation_status;

    public Friendsnoti_item(String header, String desc, String img, String id,String user_relation_status) {
        this.header = header;
        this.desc = desc;
        this.img = img;
        this.id = id;
        this.user_relation_status = user_relation_status;
    }

    public String getHeader() {
        return header;
    }

    public String getDesc() {
        return desc;
    }

    public String getImg() {
        return img;
    }

    public String getId() {
        return id;
    }

    public String getUser_relation_status() {
        return user_relation_status;
    }
}
