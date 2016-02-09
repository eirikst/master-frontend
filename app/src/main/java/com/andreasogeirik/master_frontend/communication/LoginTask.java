package com.andreasogeirik.master_frontend.communication;

import android.os.AsyncTask;

import com.andreasogeirik.master_frontend.listener.OnLoginFinishedListener;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;

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
public class LoginTask extends AsyncTask<Void, Void, ResponseEntity<String>> {
    private MultiValueMap<String, String> credentials;
    private OnLoginFinishedListener listener;

    public LoginTask(MultiValueMap<String, String> credentials, OnLoginFinishedListener listener) {
        this.credentials = credentials;
        this.listener = listener;
    }

    protected ResponseEntity<String> doInBackground(Void... params) {
        ResponseEntity<String> loginResponse;
        RestTemplate template = new RestTemplate();
        ((SimpleClientHttpRequestFactory) template.getRequestFactory()).setConnectTimeout(1000 * 10);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity(credentials, headers);

        try {
            loginResponse = template.exchange(Constants.BACKEND_URL + "login", HttpMethod.POST, entity, String.class);
            return loginResponse;
        }
        catch (HttpClientErrorException clientException) {
            clientException.getStatusCode();
            loginResponse = new ResponseEntity(clientException.getStatusCode());
            return loginResponse;
        }
        catch (ResourceAccessException resourceException) {
            return null;
        }
    }

    protected void onPostExecute(ResponseEntity<String> loginResponse) {
        if (loginResponse == null) {
            this.listener.onLoginError("Could not connect to the server, check connection");
        } else {
            HttpStatus statusCode = loginResponse.getStatusCode();
            if (statusCode.equals(HttpStatus.OK)) {
                try {
                    User user = new User(new JSONObject(loginResponse.getBody()));
                    CurrentUser.getInstance().setUser(user);
                    this.listener.onLoginSuccess(loginResponse.getHeaders().getFirst("Set-Cookie"));
                }
                catch(JSONException e) {
                    System.out.println("Feil ass");
                }
                this.listener.onLoginSuccess(loginResponse.getHeaders().getFirst("Set-Cookie"));
            } else {
                this.listener.onLoginError("The email or password doesn't match any account");
            }
        }
    }
}