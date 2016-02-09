package com.andreasogeirik.master_frontend.data;

import com.andreasogeirik.master_frontend.model.User;

/**
 * Created by eirikstadheim on 07/02/16.
 */
public class CurrentUser {
    private static CurrentUser instance;
    private User user;

    protected CurrentUser() {
    }

    public static CurrentUser getInstance() {
        if(instance == null) {
            instance = new CurrentUser();
        }
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
