package com.andreasogeirik.master_frontend.auth.logout;

import android.os.AsyncTask;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Andreas on 05.02.2016.
 */
public class LogoutTask extends AsyncTask<Void, Void, ResponseEntity<String>> {

    private String url;
    private String cookie;


    public LogoutTask(String url, String cookie) {
        this.url = url;
        this.cookie = cookie;
    }

    protected ResponseEntity<String> doInBackground(Void... params) {
        ResponseEntity<String> logoutResponse;
        RestTemplate template = new RestTemplate();
        ((SimpleClientHttpRequestFactory) template.getRequestFactory()).setConnectTimeout(1000 * 10);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Set-Cookie", cookie);
        HttpEntity<String> entity = new HttpEntity(null, headers);
        try {
            logoutResponse = template.exchange(this.url + "/logout", HttpMethod.POST, entity, String.class);
            return logoutResponse;
        } catch (ResourceAccessException resourceException) {
            return null;
        }
    }

    protected void onPostExecute(ResponseEntity<String> loginResponse) {
        // TODO: Sende svar hvis ingen kontakt med server
//        if (loginResponse == null) {
//            this.listener.onError("Could not connect to the server, check connection");
//        }
        return;
    }
}

