package com.bezzy.Ui.View.model;

public class Friendsfeed_item {
    String id,image,type,postId,video;

    public Friendsfeed_item(String id, String image, String type, String postId, String video) {
        this.id = id;
        this.image = image;
        this.type = type;
        this.postId = postId;
        this.video = video;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}
