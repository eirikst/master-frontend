package com.andreasogeirik.master_frontend.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Andreas on 26.01.2016.
 */
public class UserSmall implements Serializable {
    private int id;
    private String firstname;
    private String lastname;
    private String thumbUri;

    public UserSmall() {
    }

    public UserSmall(int id) {
        this.id = id;
    }

    public UserSmall(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public UserSmall(int id, String firstname, String lastname) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public UserSmall(JSONObject json) throws JSONException {
        id = json.getInt("id");
        firstname = json.getString("firstname");
        lastname = json.getString("lastname");
        thumbUri = json.getString("thumbUri");
    }

    public UserSmall(User user) {
        this.id = user.getId();
        this.firstname = user.getFirstname();
        this.lastname = user.getLastname();
        this.thumbUri = user.getThumbUri();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UserSmall{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                '}';
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

    public String getThumbUri() {
        return thumbUri;
    }

    public void setThumbUri(String thumbUri) {
        this.thumbUri = thumbUri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserSmall user = (UserSmall) o;

        return id == user.id;

    }

    public boolean equals(User user) {
        return id == user.getId();
    }

    @Override
    public int hashCode() {
        return id;
    }
}