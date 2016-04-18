package com.andreasogeirik.master_frontend.data;

import com.andreasogeirik.master_frontend.model.User;

/**
 * Created by eirikstadheim on 07/02/16.
 */
public class CurrentUser {
    private static CurrentUser instance;
    private User user = new User();

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
        //lists stays the same for the user, so that if this is used by the get user task and the
        // posts has been set before the other user data, the posts will remain
        this.user.copy(user);
    }
}
