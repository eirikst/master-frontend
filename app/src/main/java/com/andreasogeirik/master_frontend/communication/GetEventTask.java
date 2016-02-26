package com.andreasogeirik.master_frontend.communication;

import android.os.AsyncTask;
import android.util.Pair;

import com.andreasogeirik.master_frontend.listener.OnEventLoadedListener;
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
 * Created by Andreas on 24.02.2016.
 */
public class GetEventTask extends AsyncTask<Void, Void, Pair<Integer, ResponseEntity<String>>> {

    private int eventId;
    private OnEventLoadedListener listener;

    public GetEventTask(int eventId, OnEventLoadedListener listener) {
        this.eventId = eventId;
        this.listener = listener;
    }

    protected Pair<Integer, ResponseEntity<String>> doInBackground(Void... params) {
        ResponseEntity<String> response;
        RestTemplate template = new RestTemplate();
        ((SimpleClientHttpRequestFactory) template.getRequestFactory()).setConnectTimeout(1000 * 10);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", UserPreferencesManager.getInstance().getCookie());
        HttpEntity<String> entity = new HttpEntity(headers);

        try {
            response = template.exchange(Constants.BACKEND_URL + "events/" + eventId, HttpMethod.GET, entity, String.class);
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
                JSONObject jsonEvent = new JSONObject(response.second.getBody());
                listener.onSuccess(jsonEvent);
            } catch (JSONException e) {
                System.out.println("JSON error:" + e);
                listener.onError(Constants.JSON_PARSE_ERROR);
            }

        } else {
            listener.onError(response.first);
        }
    }
}
