package com.andreasogeirik.master_frontend.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by eirikstadheim on 07/02/16.
 */
public class SharedPrefSingleton {
    private static SharedPrefSingleton mInstance;
    private Context context;
    //
    private SharedPreferences pref;

    private SharedPrefSingleton(){ }

    public static SharedPrefSingleton getInstance(){
        if (mInstance == null) mInstance = new SharedPrefSingleton();
        return mInstance;
    }

    /*
     * This must be called before first usage, preferably from first activity
     */
    public void initialize(Context context){
        this.context = context;
        //
        pref = this.context.getSharedPreferences("session", Context.MODE_PRIVATE);
    }

    public String getSessionId() {
        String sessionId = pref.getString("cookie", null);
        return sessionId;
    }
}