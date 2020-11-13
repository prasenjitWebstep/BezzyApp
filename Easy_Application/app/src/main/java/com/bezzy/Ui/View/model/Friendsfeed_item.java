package com.bezzy.Ui.View.model;

public class Friendsfeed_item {
    String friendId,friendName,friendPhoto,friendPostDays,todayPost;

    public Friendsfeed_item(String friendId, String friendName, String friendPhoto, String friendPostDays, String todayPost) {
        this.friendId = friendId;
        this.friendName = friendName;
        this.friendPhoto = friendPhoto;
        this.friendPostDays = friendPostDays;
        this.todayPost = todayPost;
    }

    public String getFriendId() {
        return friendId;
    }

    public String getFriendName() {
        return friendName;
    }

    public String getFriendPhoto() {
        return friendPhoto;
    }

    public String getFriendPostDays() {
        return friendPostDays;
    }

    public String getTodayPost() {
        return todayPost;
    }

}
