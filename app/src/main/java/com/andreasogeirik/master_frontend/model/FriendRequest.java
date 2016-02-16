package com.andreasogeirik.master_frontend.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by eirikstadheim on 16/02/16.
 */
public class FriendRequest implements Serializable {
    private Date friendsSince;
    private boolean myRequest;
    private User user;

    public FriendRequest() {
    }

    public FriendRequest(Date friendsSince, boolean myRequest, User user) {
        this.friendsSince = friendsSince;
        this.myRequest = myRequest;
        this.user = user;
    }

    public Date getFriendsSince() {
        return friendsSince;
    }

    public void setFriendsSince(Date friendsSince) {
        this.friendsSince = friendsSince;
    }

    public boolean isMyRequest() {
        return myRequest;
    }

    public void setMyRequest(boolean myRequest) {
        this.myRequest = myRequest;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "FriendRequest{" +
                "friendsSince=" + friendsSince +
                ", myRequest=" + myRequest +
                ", user=" + user +
                '}';
    }
}
