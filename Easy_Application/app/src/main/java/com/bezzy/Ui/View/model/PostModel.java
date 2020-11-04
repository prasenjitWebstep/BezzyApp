package com.bezzy.Ui.View.model;

import org.json.JSONArray;

import java.util.ArrayList;

public class PostModel {
    String id,image;

    public PostModel(String id, String image) {
        this.id = id;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }
}
