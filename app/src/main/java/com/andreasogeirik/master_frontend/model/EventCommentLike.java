package com.andreasogeirik.master_frontend.model;

/**
 * Created by Andreas on 10.02.2016.
 */
public class EventCommentLike {
    private int id;
    private User user;
    private EventPostComment comment;

    public EventCommentLike(int id) {
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

    public EventPostComment getComment() {
        return comment;
    }

    public void setComment(EventPostComment comment) {
        this.comment = comment;
    }
}
