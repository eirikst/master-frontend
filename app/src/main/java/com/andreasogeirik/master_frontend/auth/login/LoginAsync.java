package com.andreasogeirik.master_frontend.auth.login;

import android.os.AsyncTask;

import com.andreasogeirik.master_frontend.model.User;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Andreas on 26.01.2016.
 */
public class LogincAsync extends AsyncTask<Void, Void, User> {
    protected String doInBackground(String... urls) {
        RestTemplate template = new RestTemplate();
        template.getMessageConverters().add(new StringHttpMessageConverter());
        String string = template.getForObject(urls[0], String.class);
        return string;
    }


    @Override
    protected User doInBackground(Void... params) {
        return null;
    }

    protected void onPostExecute(User user) {

    }
}
