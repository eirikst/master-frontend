package com.andreasogeirik.master_frontend.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by eirikstadheim on 17/02/16.
 */
public class Friendship implements Serializable {
    public static final int FRIENDS = 1;
    public static final int I_REQUESTED = 2;
    public static final int FRIEND_REQUESTED = 3;

    private int id;
    private User friend;
    private int status;
    private Date friendsSince;

    public Friendship() {
    }

    public Friendship(int id, User friend, int status, Date friendsSince) {
        this.id = id;
        this.friend = friend;
        this.status = status;
        this.friendsSince = friendsSince;
    }

    public Friendship(JSONObject friendship) throws JSONException {
        id = friendship.getInt(("id"));
        friend = new User(friendship.getJSONObject("friend"));
        status = friendship.getInt("status");
        friendsSince = new Date(friendship.getLong("friendsSince"));
    }

    public boolean isMyRequest() {
        return status == I_REQUESTED;
    }

    public boolean iWasRequested() {
        return status == FRIEND_REQUESTED;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    /*
     * Getters and setters
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getFriendsSince() {
        return friendsSince;
    }

    public void setFriendsSince(Date friendsSince) {
        this.friendsSince = friendsSince;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public String toString() {
        return "Friendship{" +
                "id=" + id +
                ", user=" + friend +
                ", status=" + status +
                ", friendsSince=" + friendsSince +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Friendship that = (Friendship) o;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
