package com.example.mywork2.domain;


//the users' comments for our app
public class Comment {
    private byte[] avatar;
    private String commentId;
    private String username;
    private int rating;
    private String content;
    private String time;

    public Comment(String commentId, String username, int rating, String content, String time) {
        this.commentId = commentId;
        this.username = username;
        this.rating = rating;
        this.content = content;
        this.time = time;
    }

    public Comment(byte[] avatar, String commentId, String username, int rating, String content, String time) {
        this.avatar = avatar;
        this.commentId = commentId;
        this.username = username;
        this.rating = rating;
        this.content = content;
        this.time = time;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
