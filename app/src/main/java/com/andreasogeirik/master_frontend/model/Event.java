package com.andreasogeirik.master_frontend.model;

import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andreas on 10.02.2016.
 */
public class Event implements Serializable {

    private int id;
    private String name;
    private String location;
    private String description;
    private Calendar startDate;
    private Calendar endDate;
    private Pair<Integer, Integer> startTime;
    private Pair <Integer, Integer> endTime;
    private String imageUrl;
    private User admin;
    private Set<User> users = new HashSet<User>(0);
    private Set<EventPost> posts = new HashSet<EventPost>(0);

    public Event(String name, String location, String description, Calendar startDate, Calendar
            endDate, Pair<Integer, Integer> startTime, Pair<Integer, Integer> endTime,
                 String imageUrl) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.imageUrl = imageUrl;
    }

    public Event(JSONObject eventJson) throws JSONException {
        id = eventJson.getInt("id");
        name = eventJson.getString("name");
        location = eventJson.getString("location");
        startDate = new GregorianCalendar();
        startDate.setTimeInMillis(eventJson.getLong("timeStart"));
        if(!eventJson.isNull("timeEnd")) {
            endDate = new GregorianCalendar();
            endDate.setTimeInMillis(eventJson.getLong("timeEnd"));
        }
        imageUrl = eventJson.getString("imageUri");
        if(!eventJson.isNull("admin")) {
            admin = new User(eventJson.getJSONObject("admin"));
        }
        if(!eventJson.isNull("users")) {
            JSONArray jsonUsers = eventJson.getJSONArray("users");
            for (int i = 0; i < jsonUsers.length(); i++) {
                users.add(new User(jsonUsers.getJSONObject(i)));
            }
        }
        //TODO: mulig eventpost burde inn her og
    }

    public Event() {

    }

    public int compareTo(Event event) {
        if(this.getStartDate().before(event.getStartDate())) {
            return -1;
        }
        return 1;
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

    public Pair<Integer, Integer> getStartTime() {
        return startTime;
    }

    public void setStartTime(Pair<Integer, Integer> startTime) {
        this.startTime = startTime;
    }

    public Pair<Integer, Integer> getEndTime() {
        return endTime;
    }

    public void setEndTime(Pair<Integer, Integer> endTime) {
        this.endTime = endTime;
    }

    public String getImageURI() {
        return imageUrl;
    }

    public void setImageURI(String imageURI) {
        this.imageUrl = imageURI;
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

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", imageUrl='" + imageUrl + '\'' +
                ", admin=" + admin +
                ", users=" + users +
                ", posts=" + posts +
                '}';
    }
}
