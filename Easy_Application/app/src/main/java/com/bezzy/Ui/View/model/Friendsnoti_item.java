package com.bezzy.Ui.View.model;

public class Friendsnoti_item
{
    String header,desc,img,id;

    public Friendsnoti_item(String header, String desc, String img, String id) {
        this.header = header;
        this.desc = desc;
        this.img = img;
        this.id = id;
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

}
