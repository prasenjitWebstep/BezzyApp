package com.bezzy.Ui.View.model;

public class FriendsHolder {
    public String friendId, name, image,user_relation_status;

    public FriendsHolder(String friendId, String name, String image,String user_relation_status) {
        this.friendId = friendId;
        this.name = name;
        this.image = image;
        this.user_relation_status = user_relation_status;
    }

    public String getFriendId() {
        return friendId;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getUser_relation_status() {
        return user_relation_status;
    }
}
