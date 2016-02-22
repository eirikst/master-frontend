package com.andreasogeirik.master_frontend.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andreas on 10.02.2016.
 */
public class EventPostComment implements Serializable {
    private int id;
    private String message;
    private Date timeCreated;
    private User user;
    private EventPost post;
    private Set<EventCommentLike> likes = new HashSet<EventCommentLike>(0);

    public EventPostComment(int id, String message, Date timeCreated) {
        this.id = id;
        this.message = message;
        this.timeCreated = timeCreated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public EventPost getPost() {
        return post;
    }

    public void setPost(EventPost post) {
        this.post = post;
    }

    public Set<EventCommentLike> getLikes() {
        return likes;
    }

    public void setLikes(Set<EventCommentLike> likes) {
        this.likes = likes;
    }
}
