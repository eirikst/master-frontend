package com.andreasogeirik.master_frontend.communication;

import android.os.AsyncTask;
import android.util.Pair;

import com.andreasogeirik.master_frontend.listener.OnDeleteEventFinishedListener;
import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.UserPreferencesManager;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.OkHttpClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Andreas on 28.03.2016.
 */
public class DeleteEventTask extends AsyncTask<Void, Void, Pair<Integer, ResponseEntity<String>>> {

    private int eventId;
    private OnDeleteEventFinishedListener listener;

    public DeleteEventTask(int eventId, OnDeleteEventFinishedListener listener) {
        this.eventId = eventId;
        this.listener = listener;
    }

    protected Pair<Integer, ResponseEntity<String>> doInBackground(Void... params) {
        RestTemplate template = new RestTemplate();
        ((OkHttpClientHttpRequestFactory) template.getRequestFactory()).setConnectTimeout(1000 * 10);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-type", "application/json; charset=utf-8");
        headers.set("Cookie", UserPreferencesManager.getInstance().getCookie());
        HttpEntity<String> entity = new HttpEntity(null, headers);
        try {
            template.exchange(Constants.BACKEND_URL + "events/" + eventId, HttpMethod.DELETE, entity, String.class);
            return new Pair(Constants.OK, null);
        } catch (ResourceAccessException e) {
            System.out.println("Resource error:" + e);
            return new Pair(Constants.RESOURCE_ACCESS_ERROR, null);
        } catch (HttpClientErrorException e) {
            System.out.println("Client exception:" + e);

            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                return new Pair(Constants.UNAUTHORIZED, null);
            }

            return new Pair(Constants.CLIENT_ERROR, null);
        } catch (Exception e) {
            System.out.println("Some error:" + e);
            return new Pair(Constants.SOME_ERROR, null);
        }
    }

    protected void onPostExecute(Pair<Integer, ResponseEntity<String>> response) {
        if (response.first == Constants.OK) {
            listener.onDeleteEventSuccess();
        } else {
            listener.onDeleteEventError(response.first);
        }
    }
}