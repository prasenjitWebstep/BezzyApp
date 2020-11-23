package com.bezzy.Ui.View.model;

public class FriendsHolder {
    public String friendId, name, image;

    public FriendsHolder(String friendId, String name, String image) {
        this.friendId = friendId;
        this.name = name;
        this.image = image;
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
}
