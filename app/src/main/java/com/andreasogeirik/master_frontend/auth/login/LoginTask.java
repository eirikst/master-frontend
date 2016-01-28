package com.andreasogeirik.master_frontend.auth.login;

import android.os.AsyncTask;
import android.util.Log;

import com.andreasogeirik.master_frontend.model.User;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Andreas on 28.01.2016.
 */
public class LoginTask extends AsyncTask<Void, Void, String> {

    private User user;
    private String url;

    public LoginTask(User user, String url) {
        this.user = user;
        this.url = url;
    }

    protected String doInBackground(Void... params) {
        try {

            RestTemplate template = new RestTemplate();

            JSONObject request = new JSONObject();
            request.put("username", this.user.getUsername());
            request.put("password", this.user.getPassword());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity(request.toString(), headers);

            ResponseEntity<String> loginResponse = template.exchange(this.url, HttpMethod.POST, entity, String.class);
            return loginResponse.getBody();
        }
        catch (Exception e){
            Log.v("Login", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(String responseBody) {
        System.out.println(responseBody);
    }
}