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
    private final String tag = getClass().getSimpleName();

    private int id;
    private User writer;
    private String message;
    private String imageUri;
    private Date created;
    private Set<Comment> comments = new HashSet<>();
    private Set<UserSmall> likers = new HashSet<>();

    public Post() {
    }

    public Post(JSONObject post) throws JSONException {
        id = post.getInt("id");
        writer = new User(post.getJSONObject("writer"));
        message = post.getString("message");
        imageUri = post.getString("imageUri");
        created = new Date(post.getLong("timeCreated"));

        if(!post.isNull("comments")) {
            JSONArray jsonComments = post.getJSONArray("comments");
            setComments(jsonComments);
        }
        if(!post.isNull("likers")) {
            JSONArray jsonLikers = post.getJSONArray("likers");
            setLikers(jsonLikers);
        }
    }

    private void setComments(JSONArray jsonComments) throws JSONException {
        for (int i = 0; i < jsonComments.length(); i++) {
            Comment comment = new Comment(jsonComments.getJSONObject(i));
            comments.add(comment);
        }
    }

    private void setLikers(JSONArray jsonLikers) throws JSONException {
        for (int i = 0; i < jsonLikers.length(); i++) {
            UserSmall liker = new UserSmall(jsonLikers.getJSONObject(i));
            likers.add(liker);
        }
    }

    public boolean likes(User user) {
        for(UserSmall liker: likers) {
            if(liker.equals(user)) {
                return true;
            }
        }
        return false;
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

    public Set<Comment> getComments() {
        return comments;
    }

    public Set<UserSmall> getLikers() {
        return likers;
    }

    public void setLikers(Set<UserSmall> likers) {
        this.likers = likers;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
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
