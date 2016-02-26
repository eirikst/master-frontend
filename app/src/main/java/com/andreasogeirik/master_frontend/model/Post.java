package com.andreasogeirik.master_frontend.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by eirikstadheim on 05/02/16.
 */
public class Post implements Serializable {
    private int id;
    private String message;
    private String imageUri;
    private Date created;
    private Set<Comment> comments = new HashSet<>();
    private Set<User> likers = new HashSet<>();

    public Post() {
    }

    public Post(JSONObject post) throws JSONException {
        id = post.getInt("id");
        message = post.getString("message");
        imageUri = post.getString("imageUri");
        created = new Date(post.getLong("timeCreated"));

        JSONArray jsonComments = post.getJSONArray("likers");
        JSONArray jsonLikers = post.getJSONArray("likers");

        setComments(jsonComments);
        setLikers(jsonLikers);
    }

    private void setComments(JSONArray jsonComments) throws JSONException {
        for (int i = 0; i < jsonComments.length(); i++) {
            Comment comment = new Comment();
            comment.setId(jsonComments.getJSONObject(i).getInt("id"));
            comment.setMessage(jsonComments.getJSONObject(i).getString("message"));
            comments.add(comment);
        }
    }

    private void setLikers(JSONArray jsonLikers) throws JSONException {
        for (int i = 0; i < jsonLikers.length(); i++) {
            User liker = new User();
            liker.setId(jsonLikers.getJSONObject(i).getInt("id"));
            liker.setLastname(jsonLikers.getJSONObject(i).getString("lastname"));
            liker.setFirstname(jsonLikers.getJSONObject(i).getString("firstname"));
            likers.add(liker);
        }
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

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Set getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<User> getLikers() {
        return likers;
    }

    public void setLikers(Set<User> likers) {
        this.likers = likers;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", imageUri='" + imageUri + '\'' +
                ", created=" + created +
                ", comments=" + comments +
                ", likers=" + likers +
                '}';
    }

    public int compareTo(Post post) {
        if(this.getCreated().before(post.getCreated())) {
            return 1;
        }
        return -1;
    }
}
