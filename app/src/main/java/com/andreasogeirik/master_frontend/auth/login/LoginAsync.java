package com.andreasogeirik.master_frontend.auth.login;

import android.os.AsyncTask;
import android.util.Log;

import com.andreasogeirik.master_frontend.model.User;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Andreas on 26.01.2016.
 */
public class LoginAsync extends AsyncTask<Void, Void, User> {

    private User user;
    private String url;

    public LoginAsync(User user, String url) {
        this.user = user;
        this.url = url;
    }

    protected User doInBackground(Void... params) {
        try {
            RestTemplate template = new RestTemplate();
            template.getMessageConverters().add(new StringHttpMessageConverter());
            User user = template.postForObject(this.url, this.user, User.class);
            return user;
        }
        catch (Exception e){
            Log.v("Login", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(User user) {
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
    }
}
