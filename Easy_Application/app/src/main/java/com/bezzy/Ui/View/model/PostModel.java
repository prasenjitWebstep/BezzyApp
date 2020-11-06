package com.bezzy.Ui.View.model;

import org.json.JSONArray;

import java.util.ArrayList;

public class PostModel {
    String id,image,type,postId;

    public PostModel(String id, String image,String type,String postId) {
        this.id = id;
        this.image = image;
        this.type = type;
        this.postId = postId;
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
}
