package com.andreasogeirik.master_frontend.util;

import com.andreasogeirik.master_frontend.communication.LogoutTask;

/**
 * Created by eirikstadheim on 09/02/16.
 */
public class LogoutHandler {
    private static LogoutHandler instance;

    protected LogoutHandler() {

    }

    public static LogoutHandler getInstance() {
        if(instance != null) {
            return instance;
        }
        return new LogoutHandler();
    }

    public void logOut() {
        new Thread()
        {
            public void run() {
                new LogoutTask().logout();
                SessionManager.getInstance().deleteCookie();
            }
        }.start();
    }
}