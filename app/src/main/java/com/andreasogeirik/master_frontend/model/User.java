package com.andreasogeirik.master_frontend.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Andreas on 26.01.2016.
 */
public class User implements Serializable {
    private int id;
    private String email;
    private boolean enabled;
    private String password;
    private String firstname;
    private String lastname;
    private String location;
    private String imageUri;
    private Date timeCreated;
    private Set<User> friends;

    public User() {
    }

    public User(int id, String email, boolean enabled, String firstname, String lastname, String location,
                String imageUri) {
        this.id = id;
        this.email = email;
        this.enabled = enabled;
        this.firstname = firstname;
        this.lastname = lastname;
        this.location = location;
        this.imageUri = imageUri;
    }

    public User(JSONObject json) throws JSONException {
        id = json.getInt("id");
        email = json.getString("email");
        enabled = json.getBoolean("enabled");
        firstname = json.getString("firstname");
        lastname = json.getString("lastname");
        location = json.getString("location");
        imageUri = json.getString("imageUri");
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

    public boolean isFriendWith(User user) {
        if(friends.contains(user) ) System.out.println("vi er venner bror");
        return friends.contains(user);
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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

    public Set<User> getFriends() {
        return friends;
    }

    public void setFriends(Set<User> friends) {
        this.friends = friends;
    }

    @Override
    public String toString() {
        Iterator it = null;
        if(friends != null) {
            it = friends.iterator();
        }
        String user = "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", enabled=" + enabled +
                ", password='" + password + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", location='" + location + '\'' +
                ", imageUri='" + imageUri + '\'' +
                ", timeCreated=" + timeCreated +
                ", friends=";
        if(it != null) {
            while (it.hasNext()) {
                user += it.next();
            }
        }
        user += '}';
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id == user.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}