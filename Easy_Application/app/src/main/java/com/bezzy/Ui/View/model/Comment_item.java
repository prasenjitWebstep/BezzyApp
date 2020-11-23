package com.bezzy.Ui.View.model;

public class Comment_item {
    String commentid,username,user_image,post_comment_time,commentText;

    public Comment_item(String commentid, String username, String user_image, String post_comment_time, String commentText) {
        this.commentid = commentid;
        this.username = username;
        this.user_image = user_image;
        this.post_comment_time = post_comment_time;
        this.commentText = commentText;
    }

    public String getCommentid() {

        return commentid;
    }

    public void setCommentid(String commentid) {
        this.commentid = commentid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getPost_comment_time() {
        return post_comment_time;
    }

    public void setPost_comment_time(String post_comment_time) {
        this.post_comment_time = post_comment_time;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }
}
