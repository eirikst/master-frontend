package com.andreasogeirik.master_frontend.auth.login;

import android.os.AsyncTask;

import com.andreasogeirik.master_frontend.auth.login.interfaces.OnLoginFinishedListener;
import com.andreasogeirik.master_frontend.model.User;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Andreas on 28.01.2016.
 */
public class LoginTask extends AsyncTask<Void, Void, String> {

    private User user;
    private String url;
    private OnLoginFinishedListener listener;

    public LoginTask(User user, String url, OnLoginFinishedListener listener) {
        this.user = user;
        this.url = url;
        this.listener = listener;
    }

    protected String doInBackground(Void... params) {
        try {
            RestTemplate template = new RestTemplate();
            ((SimpleClientHttpRequestFactory)template.getRequestFactory()).setConnectTimeout(1000 * 10);

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
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(String responseBody) {

        if (responseBody == null){
            this.listener.onError("Could not retrieve data from server, check connection");
        }
        else{
            try {
                JSONObject object = new JSONObject(responseBody);
                this.listener.onSuccess(object);
            } catch (JSONException e) {
                e.printStackTrace();
                this.listener.onError("Could not transform response to JSON object");
            }
        }
        System.out.println(responseBody);
    }
}