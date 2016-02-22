package com.andreasogeirik.master_frontend.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andreas on 10.02.2016.
 */
public class EventPost implements Serializable {
    private int id;
    private String message;
    private Date timeCreated;
    private String imageUri;
    private User user;
    private Event event;
    private Set<UserPostComment> comments = new HashSet<UserPostComment>(0);
    private Set<UserPostLike> likes = new HashSet<UserPostLike>(0);

    public EventPost(int id, String message, Date timeCreated, String imageUri) {
        this.id = id;
        this.message = message;
        this.timeCreated = timeCreated;
        this.imageUri = imageUri;
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

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Set<UserPostComment> getComments() {
        return comments;
    }

    public void setComments(Set<UserPostComment> comments) {
        this.comments = comments;
    }

    public Set<UserPostLike> getLikes() {
        return likes;
    }

    public void setLikes(Set<UserPostLike> likes) {
        this.likes = likes;
    }
}
