package com.andreasogeirik.master_frontend.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by eirikstadheim on 05/02/16.
 */
public class Comment implements Serializable {
    private int id;
    private User writer;
    private String message;
    private Date timeCreated;
    private Set<User> likers = new HashSet<>();

    public Comment() {

    }

    public Comment(JSONObject comment) throws JSONException{
        id = comment.getInt("id");
        writer = new User(comment.getJSONObject("user"));
        message = comment.getString("message");
        timeCreated = new Date(comment.getLong("timeCreated"));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getWriter() {
        return writer;
    }

    public void setWriter(User writer) {
        this.writer = writer;
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

    public Set<User> getLikers() {
        return likers;
    }

    public void setLikers(Set<User> likers) {
        this.likers = likers;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", writer=" + writer +
                ", message='" + message + '\'' +
                ", timeCreated=" + timeCreated +
                ", likers=" + likers +
                '}';
    }
}
