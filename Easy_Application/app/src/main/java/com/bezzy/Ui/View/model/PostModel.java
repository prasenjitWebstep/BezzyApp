package com.bezzy.Ui.View.model;

import org.json.JSONArray;

public class PostModel {
    JSONArray array;
    String id;

    public PostModel(JSONArray array, String id) {
        this.array = array;
        this.id = id;
    }

    public JSONArray getArray() {
        return array;
    }

    public String getId() {
        return id;
    }
}
