package com.andreasogeirik.master_frontend.model;

import android.util.Pair;

import java.util.Calendar;
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
    private Calendar startDate;
    private Calendar endDate;
    private Pair<Integer, Integer> timeStart;
    private Pair <Integer, Integer> timeEnd;
    private String imageURI;
    private User admin;
    private Set<User> users = new HashSet<User>(0);
    private Set<EventPost> posts = new HashSet<EventPost>(0);

    public Event(String name, String location, String description, Calendar startDate, Calendar endDate, Pair<Integer, Integer> timeStart, Pair<Integer, Integer> timeEnd, String imageURI) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.imageURI = imageURI;
    }

    public Event() {

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

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    public Pair<Integer, Integer> getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Pair<Integer, Integer> timeStart) {
        this.timeStart = timeStart;
    }

    public Pair<Integer, Integer> getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Pair<Integer, Integer> timeEnd) {
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
