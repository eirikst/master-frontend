package com.andreasogeirik.master_frontend.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Andreas on 26.01.2016.
 */
public class User {
    private int id;
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private String location;
    private String imageUri;
    private Date timeCreated;

    public User(int id, String email, String firstname, String lastname, String location, String imageUri, Date timeCreated) {
        this.id = id;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.location = location;
        this.imageUri = imageUri;
        this.timeCreated = timeCreated;
    }

    // Used to edit users
    public JSONObject createJSONWithId() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", this.id);
        jsonObject.put("email", this.email);
        jsonObject.put("firstname", this.firstname);
        jsonObject.put("lastname", this.lastname);
        jsonObject.put("location", this.location);
        jsonObject.put("imageUri", this.imageUri);

        return jsonObject;
    }

    // Used to create users
    public JSONObject createJSONWithoutId() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", this.email);
        jsonObject.put("password", this.password);
        jsonObject.put("firstname", this.firstname);
        jsonObject.put("lastname", this.lastname);
        jsonObject.put("location", this.location);
        jsonObject.put("imageUri", this.imageUri);

        return jsonObject;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public Date getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }
}