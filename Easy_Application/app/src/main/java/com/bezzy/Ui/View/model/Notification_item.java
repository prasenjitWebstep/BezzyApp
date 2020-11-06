package com.bezzy.Ui.View.model;

public class Notification_item {
    int img;
    int imgic;
    String descrip;
    String name;

    public Notification_item( String descrip) {
        this.img = img;
        this.imgic = imgic;
        this.descrip = descrip;
        this.name = name;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public int getImgic() {
        return imgic;
    }

    public void setImgic(int imgic) {
        this.imgic = imgic;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
