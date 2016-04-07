package com.andreasogeirik.master_frontend.gcm;

/**
 * Created by eirikstadheim on 06/04/16.
 */
public class GcmTokenSingleton {
    private static GcmTokenSingleton instance;
    private String token;

    protected GcmTokenSingleton() {
    }

    public static GcmTokenSingleton getInstance(){
        if (instance == null) instance = new GcmTokenSingleton();
        return instance;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean tokenAvailable() {
        return token != null;
    }
}
