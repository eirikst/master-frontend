package com.andreasogeirik.master_frontend.util;

import android.util.Log;

import com.andreasogeirik.master_frontend.communication.LogoutTask;
import com.andreasogeirik.master_frontend.gcm.GcmApiService;
import com.andreasogeirik.master_frontend.gcm.GcmTokenSingleton;

/**
 * Created by eirikstadheim on 09/02/16.
 */
public class LogoutHandler implements GcmApiService.Listener {
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
        //remove gcm token from server after logout
        //the logout call is made in the listener method for the Gcm task
        if(GcmTokenSingleton.getInstance().tokenAvailable()) {
            new GcmApiService(GcmTokenSingleton.getInstance().getToken(), GcmApiService.REMOVE_TOKEN, this).execute();
            Log.i(getClass().getSimpleName(), "Removing token " + GcmTokenSingleton.getInstance().getToken());
        }
    }

    @Override
    public void onGcmTaskFinished() {
        new Thread()
        {
            public void run() {
                new LogoutTask().logout();
                UserPreferencesManager.getInstance().deleteCookie();
            }
        }.start();
    }
}