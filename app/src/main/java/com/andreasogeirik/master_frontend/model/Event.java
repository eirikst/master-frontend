package com.andreasogeirik.master_frontend.model;

import com.andreasogeirik.master_frontend.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
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
    private String imageUri;
    private String thumbUri;
    private int difficulty  = Constants.EVENT_DIFFICULTY_EASY;
    private ActivityType activityType;
    private User admin;
    private Set<User> users = new HashSet<User>(0);
    private Set<Post> posts = new HashSet<Post>(0);

    public Event(String name, String location, String description, Calendar startDate, int difficulty) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.startDate = startDate;
        this.difficulty = difficulty;

//        for (ActivityType type : ActivityType.values()) {
//            if (activityTypeId == type.getId()){
//                this.activityType = type;
//                break;
//            }
//        }
    }

    public Event(JSONObject jsonEvent) throws JSONException {
        this.id = jsonEvent.getInt("id");
        this.name = jsonEvent.getString("name");
        this.location = jsonEvent.getString("location");
        this.description = jsonEvent.getString("description");
        this.activityType = ActivityType.getTypeById(jsonEvent.getInt("activityTypeId"));
        this.startDate = new GregorianCalendar();
        this.startDate.setTimeInMillis(jsonEvent.getLong("timeStart"));
        if (!jsonEvent.isNull("timeEnd")){
            this.endDate = new GregorianCalendar();
            this.endDate.setTimeInMillis(jsonEvent.getLong("timeEnd"));
        }

        this.imageUri = jsonEvent.getString("imageUri");
        this.thumbUri = jsonEvent.getString("thumbUri");
        this.difficulty = jsonEvent.getInt("difficulty");
        this.admin = new User(jsonEvent.getJSONObject("admin"));
        JSONArray jsonUsers = jsonEvent.getJSONArray("users");
        for (int i = 0; i < jsonUsers.length(); i++) {
            this.users.add(new User(jsonUsers.getJSONObject(i)));
        }
        this.difficulty = jsonEvent.getInt("difficulty");
    }

    public Event() {

    }

    public int compareTo(Event event) {
        if(this.getStartDate().before(event.getStartDate())) {
            return 1;
        }
        return -1;
    }

    public boolean attends(User user) {
        for(User u: users) {
            if(u.equals(user)) {
                return true;
            }
        }
        return false;
    }

    public void addPosts(Collection<Post> posts) {
        if(posts != null) {
            this.posts.addAll(posts);
        }
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

    public String getThumbUri() {
        return thumbUri;
    }

    public void setThumbUri(String thumbUri) {
        this.thumbUri = thumbUri;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
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

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }



    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", activity type='" + activityType+ '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", imageUri='" + imageUri + '\'' +
                ", admin=" + admin +
                ", users=" + users +
                ", posts=" + posts +
                '}';
    }
}
