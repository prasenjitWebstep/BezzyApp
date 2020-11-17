package com.bezzy.Ui.View.model;

import org.json.JSONArray;

public class FriendsPostModel {
    public String post_id,post_type,number_of_likes,number_of_comment,name,photo,contents,userLikeStatus,postTime;
    public JSONArray post_image_video;

    public FriendsPostModel(String post_id, String post_type, String number_of_likes, String number_of_comment, String name, String photo, String contents,String userLikeStatus,String postTime,JSONArray post_image_video) {
        this.post_id = post_id;
        this.post_type = post_type;
        this.number_of_likes = number_of_likes;
        this.number_of_comment = number_of_comment;
        this.name = name;
        this.photo = photo;
        this.contents = contents;
        this.userLikeStatus = userLikeStatus;
        this.postTime = postTime;
        this.post_image_video = post_image_video;
    }

    public String getPost_id() {
        return post_id;
    }

    public String getPost_type() {
        return post_type;
    }

    public String getNumber_of_likes() {
        return number_of_likes;
    }

    public String getNumber_of_comment() {
        return number_of_comment;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }

    public String getContents() {
        return contents;
    }

    public String getUserLikeStatus() {
        return userLikeStatus;
    }

    public String getPostTime() {
        return postTime;
    }

    public JSONArray getPost_image_video() {
        return post_image_video;
    }

}
