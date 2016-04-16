package com.andreasogeirik.master_frontend.communication;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.UserPreferencesManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;


/**
 * Created by Eirik on 23.04.2016.
 */
public class PostTask extends AsyncTask<Void, Void, Pair<Integer, ResponseEntity<String>>> {
    private String tag = getClass().getSimpleName();

    public interface OnFinishedPostingListener {
        void onSuccessPost(JSONObject post);
        void onFailurePost(int code);
    }

    public static final int USER = 1;
    public static final int EVENT = 2;

    private OnFinishedPostingListener listener;
    private String message;
    private int id;
    private int userOrEvent;

    public PostTask(OnFinishedPostingListener listener, String message, int id, int userOrEvent) {//bruker egentlig ikke userid sånn det ble nå...
        this.listener = listener;
        this.message = message;
        this.id = id;
        this.userOrEvent = userOrEvent;
    }

    protected Pair<Integer, ResponseEntity<String>> doInBackground(Void... params) {
        ResponseEntity<String> response;
        RestTemplate template = new RestTemplate();
        ((SimpleClientHttpRequestFactory) template.getRequestFactory()).setConnectTimeout(1000 * 10);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", UserPreferencesManager.getInstance().getCookie());
        headers.set("Content-type", "application/json; charset=utf-8");

        JSONObject post = new JSONObject();
        try {
            post.put("message", message);
        }
        catch(JSONException e) {
            return new Pair(Constants.JSON_PARSE_ERROR, null);
        }

        HttpEntity<String> entity = new HttpEntity(post.toString(), headers);

        try {
            if(userOrEvent == USER) {
                response = template.exchange(Constants.BACKEND_URL + "/me/posts", HttpMethod.PUT, entity, String.class);
            }
            else {
                response = template.exchange(Constants.BACKEND_URL + "/events/" + id +
                        "/posts", HttpMethod.PUT, entity, String.class);
            }
            return new Pair(Constants.OK, response);
        }
        catch (HttpClientErrorException e) {
            Log.w(tag, "Client error:" + e);
            return new Pair(Constants.CLIENT_ERROR, null);
        }
        catch (ResourceAccessException e) {
            Log.w(tag, "Resource error:" + e);
            return new Pair(Constants.RESOURCE_ACCESS_ERROR, null);
        }
        catch(Exception e) {
            Log.w(tag, "Some error:" + e);
            return new Pair(Constants.SOME_ERROR, null);
        }
    }

    protected void onPostExecute(Pair<Integer, ResponseEntity<String>> response) {
        //success
        if (response.first == Constants.OK) {
            try {
                listener.onSuccessPost(new JSONObject(response.second.getBody()));
            }
            catch(JSONException e) {
                listener.onFailurePost(Constants.JSON_PARSE_ERROR);
            }
        }
        //failure
        else {
            listener.onFailurePost(response.first);
        }
    }
}