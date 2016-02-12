package com.andreasogeirik.master_frontend.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andreas on 10.02.2016.
 */
public class Event {

    private int id;
    private String name;
    private String location;
    private String description;
    private Date timeCreated;
    private Date timeStart;
    private Date timeEnd;
    private String imageURI;
    private User admin;
    private Set<User> users = new HashSet<User>(0);
    private Set<EventPost> posts = new HashSet<EventPost>(0);

    public Event(int id, String name, String location, String description, Date timeCreated, Date timeStart, Date timeEnd, String imageURI) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.description = description;
        this.timeCreated = timeCreated;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.imageURI = imageURI;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<EventPost> getPosts() {
        return posts;
    }

    public void setPosts(Set<EventPost> posts) {
        this.posts = posts;
    }
}
