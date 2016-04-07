package com.andreasogeirik.master_frontend.communication;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.andreasogeirik.master_frontend.gcm.GcmApiService;
import com.andreasogeirik.master_frontend.gcm.GcmTokenSingleton;
import com.andreasogeirik.master_frontend.listener.OnLoginFinishedListener;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Iterator;
import java.util.Map;


/**
 * Created by Andreas on 28.01.2016.
 */
public class LoginTask extends AsyncTask<Void, Void, Pair<Integer, ResponseEntity<String>>> {
    private MultiValueMap<String, String> credentials;
    private OnLoginFinishedListener listener;

    public LoginTask(MultiValueMap<String, String> credentials, OnLoginFinishedListener listener) {
        this.credentials = credentials;
        this.listener = listener;
    }

    protected Pair<Integer, ResponseEntity<String>> doInBackground(Void... params) {
        ResponseEntity<String> response;
        RestTemplate template = new RestTemplate();
        ((SimpleClientHttpRequestFactory) template.getRequestFactory()).setConnectTimeout(1000 * 10);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity(credentials, headers);


        try {
            response = template.exchange(Constants.BACKEND_URL + "login", HttpMethod.POST, entity, String.class);
            return new Pair(Constants.OK, response);
        }
        catch (HttpClientErrorException e) {
            System.out.println("Client error:" + e);
            return new Pair(Constants.CLIENT_ERROR, null);
        }
        catch (ResourceAccessException e) {
            System.out.println("Resource error:" + e);
            return new Pair(Constants.RESOURCE_ACCESS_ERROR, null);
        }
        catch(Exception e) {
            System.out.println("Some error:" + e);
            return new Pair(Constants.SOME_ERROR, null);
        }
    }

    protected void onPostExecute(Pair<Integer, ResponseEntity<String>> response) {
        if (response.first == Constants.OK) {
            //set gcm token on server after login
            if(GcmTokenSingleton.getInstance().tokenAvailable()) {
                new GcmApiService(GcmTokenSingleton.getInstance().getToken(), GcmApiService.ADD_TOKEN, null).execute();
                Log.i(getClass().getSimpleName(), "Adding token " + GcmTokenSingleton.getInstance().getToken());
            }

            try {
                JSONObject user = new JSONObject(response.second.getBody());
                listener.onLoginSuccess(user, response.second.getHeaders().getFirst("Set-Cookie"));
            }
            catch(JSONException e) {
                System.out.println("JSON error:" + e);
                listener.onLoginError(Constants.JSON_PARSE_ERROR);
            }

        }
        else {
            listener.onLoginError(response.first);
        }
    }
}