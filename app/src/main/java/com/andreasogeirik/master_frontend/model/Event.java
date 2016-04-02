package com.andreasogeirik.master_frontend.model;

import com.andreasogeirik.master_frontend.util.Constants;

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
    private String imageUrl;
    private int difficulty  = Constants.EVENT_DIFFICULTY_EASY;
    private User admin;
    private Set<User> users = new HashSet<User>(0);
    private Set<EventPost> posts = new HashSet<EventPost>(0);

    public Event(String name, String location, String description, Calendar startDate) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.startDate = startDate;
    }

    public Event(JSONObject jsonEvent) throws JSONException {
        this.id = jsonEvent.getInt("id");
        this.name = jsonEvent.getString("name");
        this.location = jsonEvent.getString("location");
        this.description = jsonEvent.getString("description");
        this.startDate = new GregorianCalendar();
        this.startDate.setTimeInMillis(jsonEvent.getLong("timeStart"));
        if (!jsonEvent.isNull("timeEnd")){
            this.endDate = new GregorianCalendar();
            this.endDate.setTimeInMillis(jsonEvent.getLong("timeEnd"));
        }
        this.imageUrl = jsonEvent.getString("imageUri");
        this.difficulty = jsonEvent.getInt("difficulty");
        this.admin = new User(jsonEvent.getJSONObject("admin"));
        JSONArray jsonUsers = jsonEvent.getJSONArray("users");
        for (int i = 0; i < jsonUsers.length(); i++) {
            this.users.add(new User(jsonUsers.getJSONObject(i)));
        }

        // TODO FIKSE POSTER
    }

    public Event() {

    }

    public int compareTo(Event event) {
        if(this.getStartDate().before(event.getStartDate())) {
            return 1;
        }
        return -1;
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

    public String getImageURI() {
        return imageUrl;
    }

    public void setImageURI(String imageURI) {
        this.imageUrl = imageURI;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
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
                ", imageUrl='" + imageUrl + '\'' +
                ", admin=" + admin +
                ", users=" + users +
                ", posts=" + posts +
                '}';
    }
}
