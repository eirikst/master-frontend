package com.andreasogeirik.master_frontend.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Andreas on 05.02.2016.
 */
public class SessionManager {
    private static SessionManager instance;
    private SharedPreferences preferences;

    protected SessionManager() {

    }

    public static SessionManager getInstance(){
        if (instance == null) instance = new SessionManager();
        return instance;
    }

    /*
     * This must be called before first usage, preferably from first activity
     */
    public void initialize(Context context){
        if(preferences == null) {
            preferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        }
    }


    public String getCookie(){
        String cookie = preferences.getString("cookie", null);
        return cookie;
    }

    public void saveCookie(String cookie){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("cookie", cookie);
        editor.commit();
    }

    public void deleteCookie(){
        preferences.edit().remove("cookie").commit();
    }

    public void signOut(){
        String cookie = preferences.getString("cookie", null);
        preferences.edit().remove("cookie").commit();
    }
}
