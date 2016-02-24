package com.andreasogeirik.master_frontend.communication;

import android.os.AsyncTask;
import android.util.Pair;

import com.andreasogeirik.master_frontend.listener.OnRegisterFinishedListener;
import com.andreasogeirik.master_frontend.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Andreas on 05.02.2016.
 */
public class RegisterTask extends AsyncTask<Void, Void, Pair<Integer, ResponseEntity<String>>> {

    private JSONObject user;
    private OnRegisterFinishedListener listener;

    public RegisterTask(JSONObject user, OnRegisterFinishedListener listener) {
        this.listener = listener;
        this.user = user;
    }

    protected Pair<Integer, ResponseEntity<String>> doInBackground(Void... params) {
        ResponseEntity<String> response;
        RestTemplate template = new RestTemplate();
        ((SimpleClientHttpRequestFactory) template.getRequestFactory()).setConnectTimeout(1000 * 10);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");//this viktig for å få riktig charset
        HttpEntity<String> entity = new HttpEntity(user.toString(), headers);

        try {
            response = template.exchange(Constants.BACKEND_URL + "users", HttpMethod.PUT, entity, String.class);
            return new Pair(Constants.OK, response);
        } catch (HttpClientErrorException e) {
            System.out.println("Client error:" + e);
            return new Pair(Constants.CLIENT_ERROR, null);
        } catch (ResourceAccessException e) {
            System.out.println("Resource error:" + e);
            return new Pair(Constants.RESOURCE_ACCESS_ERROR, null);
        }
    }

    protected void onPostExecute(Pair<Integer, ResponseEntity<String>> response) {
        if (response.first == Constants.OK) {

            try {
                JSONObject user = new JSONObject(response.second.getBody());
                listener.onRegisterSuccess(user);
            } catch (JSONException e) {
                System.out.println("JSON error:" + e);
                listener.onRegisterError(Constants.JSON_PARSE_ERROR);
            }

        }
        else {
            listener.onRegisterError(response.first);
        }
    }
}