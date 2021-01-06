package com.bezzy.Ui.View.model;

import org.json.JSONArray;

import java.util.ArrayList;

public class PostModel {
    String id, image, type, postId, postTime, postDate,postTag;

    public PostModel(String id, String image, String type, String postId, String postTime, String postDate, String postTag) {
        this.id = id;
        this.image = image;
        this.type = type;
        this.postId = postId;
        this.postTime = postTime;
        this.postDate = postDate;
        this.postTag = postTag;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getType() {
        return type;
    }

    public String getPostId() {
        return postId;
    }

    public String getPostTime() {
        return postTime;
    }

    public String getPostDate() {
        return postDate;
    }

    public String getPostTag() {
        return postTag;
    }
}
