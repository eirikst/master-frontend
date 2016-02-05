package com.andreasogeirik.master_frontend.auth.login;

import android.os.AsyncTask;

import com.andreasogeirik.master_frontend.auth.login.interfaces.OnLoginFinishedListener;

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

/**
 * Created by Andreas on 28.01.2016.
 */
public class LoginTask extends AsyncTask<Void, Void, ResponseEntity<String>> {

    private String email;
    private String password;
    private String url;
    private OnLoginFinishedListener listener;

    public LoginTask(String email, String password, String url, OnLoginFinishedListener listener) {
        this.email = email;
        this.password = password;
        this.url = url;
        this.listener = listener;
    }

    protected ResponseEntity<String> doInBackground(Void... params) {
        ResponseEntity<String> loginResponse;
        RestTemplate template = new RestTemplate();
        ((SimpleClientHttpRequestFactory) template.getRequestFactory()).setConnectTimeout(1000 * 10);
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", this.email);
        map.add("password", this.password);
        HttpEntity<String> entity = new HttpEntity(map, headers);
        try {
            loginResponse = template.exchange(this.url + "/login", HttpMethod.POST, entity, String.class);
            return loginResponse;
        } catch (HttpClientErrorException clientException) {
            clientException.getStatusCode();
            loginResponse = new ResponseEntity(clientException.getStatusCode());
            return loginResponse;
        } catch (ResourceAccessException resourceException) {
            return null;
        }
    }

    protected void onPostExecute(ResponseEntity<String> loginResponse) {
        if (loginResponse == null) {
            this.listener.onError("Could not connect to the server, check connection");
        } else {
            HttpStatus statusCode = loginResponse.getStatusCode();
            if (statusCode.equals(HttpStatus.FOUND)) {
                this.listener.onSuccess(loginResponse.getHeaders().getFirst("Set-Cookie"));
            } else {
                this.listener.onError("The email or password doesn't match any account");
            }
        }
    }
}