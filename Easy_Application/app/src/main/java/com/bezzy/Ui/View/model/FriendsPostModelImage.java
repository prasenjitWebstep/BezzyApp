package com.bezzy.Ui.View.model;

public class FriendsPostModelImage {
    public String postUrl,postId,postType;

    public FriendsPostModelImage(String postUrl, String postId, String postType) {
        this.postUrl = postUrl;
        this.postId = postId;
        this.postType = postType;
    }

    public String getPostUrl() {
        return postUrl;
    }

    public String getPostId() {
        return postId;
    }

    public String getPostType() {
        return postType;
    }

}
