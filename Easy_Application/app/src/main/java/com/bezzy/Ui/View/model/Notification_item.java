package com.bezzy.Ui.View.model;

public class Notification_item {
    String img;
    String type;
    String descrip;
    String fromId;
    String friendrequestStatus;
    String postId;
    String postType;

    public Notification_item(String img, String type, String descrip, String fromId, String friendrequestStatus,String postId,String postType) {
        this.img = img;
        this.type = type;
        this.descrip = descrip;
        this.fromId = fromId;
        this.friendrequestStatus = friendrequestStatus;
        this.postId = postId;
        this.postType = postType;
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

    public String getPostId() {
        return postId;
    }

    public String getPostType() {
        return postType;
    }

}
