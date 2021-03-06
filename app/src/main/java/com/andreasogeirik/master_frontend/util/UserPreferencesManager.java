package com.andreasogeirik.master_frontend.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Andreas on 05.02.2016.
 */
public class UserPreferencesManager {
    private String tag = getClass().getSimpleName();

    private static UserPreferencesManager instance;
    private SharedPreferences preferences;

    protected UserPreferencesManager() {

    }

    public static UserPreferencesManager getInstance(){
        if (instance == null) instance = new UserPreferencesManager();
        return instance;
    }

    /*
     * This must be called before first usage, preferably from first activity
     */
    public void initialize(Context context){
        Log.i(tag, "Init sharedpref");
                preferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);
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

/*    public void saveGcmToken(String token) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("gcmToken", token);
        editor.commit();
    }

    public String getGcmToken() {
        String gcmToken = preferences.getString("gcmToken", null);
        return gcmToken;
    }

    public void removeGcmToken(){
        preferences.edit().remove("gcmToken").commit();
    }

    public boolean tokenAvailable() {
        return getGcmToken() != null;
    }
*/
}
