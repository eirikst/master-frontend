package com.andreasogeirik.master_frontend.model;

/**
 * Created by Andreas on 10.02.2016.
 */
public class UserPostLike {
    private int id;
    private User user;
    private UserPost post;

    public UserPostLike(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserPost getPost() {
        return post;
    }

    public void setPost(UserPost post) {
        this.post = post;
    }
}
