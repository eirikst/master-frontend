package com.andreasogeirik.master_frontend.communication;

import android.os.AsyncTask;
import android.util.Pair;

import com.andreasogeirik.master_frontend.listener.OnAttendEventFinishedListener;
import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.UserPreferencesManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Andreas on 10.03.2016.
 */
public class AttendEventTask extends AsyncTask<Void, Void, Pair<Integer, ResponseEntity<String>>> {

    private int eventId;
    private OnAttendEventFinishedListener listener;
    private String attendType = "/attend";

    public AttendEventTask(int eventId, OnAttendEventFinishedListener listener, boolean isAttendRequest) {
        this.eventId = eventId;
        this.listener = listener;
        if (!isAttendRequest){
            this.attendType = "/unattend";
        }

    }

    protected Pair<Integer, ResponseEntity<String>> doInBackground(Void... params) {
        ResponseEntity<String> response;
        RestTemplate template = new RestTemplate();
        ((SimpleClientHttpRequestFactory) template.getRequestFactory()).setConnectTimeout(1000 * 10);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", UserPreferencesManager.getInstance().getCookie());
        HttpEntity<String> entity = new HttpEntity(null, headers);

        try {
            System.out.println("REQUEST GÅR");
            response = template.exchange(Constants.BACKEND_URL + "events/" + eventId + attendType, HttpMethod.POST, entity, String.class);
            return new Pair(Constants.OK, response);
        }
        catch (ResourceAccessException e) {
            System.out.println("Resource error:" + e);
            return new Pair(Constants.RESOURCE_ACCESS_ERROR, null);
        }
        catch (HttpClientErrorException e) {
            System.out.println("Client exception:" + e);

            if(e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                return new Pair(Constants.UNAUTHORIZED, null);
            }

            return new Pair(Constants.CLIENT_ERROR, null);
        }
        catch(Exception e) {
            System.out.println("Some error:" + e);
            return new Pair(Constants.SOME_ERROR, null);
        }
    }

    protected void onPostExecute(Pair<Integer, ResponseEntity<String>> response) {
        System.out.println("REQUEST TILBAKE");
        if (response.first == Constants.OK) {
            try {
                JSONObject jsonEvent = new JSONObject(response.second.getBody());
                listener.onAttendSuccess(jsonEvent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            listener.onAttendError(response.first);
        }
    }
}
