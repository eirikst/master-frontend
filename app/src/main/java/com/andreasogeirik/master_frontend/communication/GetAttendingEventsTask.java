package com.andreasogeirik.master_frontend.communication;

import android.os.AsyncTask;
import android.util.Pair;

import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.UserPreferencesManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public class GetAttendingEventsTask extends AsyncTask<Void, Void, Pair<Integer, ResponseEntity<String>>> {
    public interface OnFinishedLoadingAttendingEventsListener {
        void onSuccessAttendingEvents(JSONArray events);
        void onFailureAttendingEvents(int code);
    }


    private OnFinishedLoadingAttendingEventsListener listener;
    private int start;
    private User user;

    public GetAttendingEventsTask(OnFinishedLoadingAttendingEventsListener listener, User user) {
        this.listener = listener;
        this.user = user;
    }

    @Override
    protected Pair<Integer, ResponseEntity<String>> doInBackground(Void... params) {
        ResponseEntity<String> response;

        RestTemplate template = new RestTemplate();
        ((SimpleClientHttpRequestFactory) template.getRequestFactory()).setConnectTimeout(1000 * 10);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", UserPreferencesManager.getInstance().getCookie());

        HttpEntity<String> entity = new HttpEntity(null, headers);

        try {
            response = template.exchange(Constants.BACKEND_URL + "users/" + user.getId() +
                            "/events/attending",
                    HttpMethod.GET, entity, String.class);
            return new Pair(Constants.OK, response);
        }
        catch (HttpClientErrorException e) {
            System.out.println("Client exception:" + e);
            return new Pair(Constants.CLIENT_ERROR, null);
        }
        catch (ResourceAccessException e) {
            System.out.println("Resource error:" + e);
            return new Pair(Constants.RESOURCE_ACCESS_ERROR, null);
        }
    }

    @Override
    protected void onPostExecute(Pair<Integer, ResponseEntity<String>> response) {
        if (response.first == Constants.OK) {

            try {
                JSONArray events = new JSONArray(response.second.getBody());
                listener.onSuccessAttendingEvents(events);
            }
            catch(JSONException e) {
                System.out.println("JSON error:" + e);
                listener.onFailureAttendingEvents(Constants.JSON_PARSE_ERROR);
            }

        }
        else {
            listener.onFailureAttendingEvents(response.first);
        }
    }
}