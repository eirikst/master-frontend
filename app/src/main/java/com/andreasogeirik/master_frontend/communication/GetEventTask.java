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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public class GetEventTask extends AsyncTask<Void, Void, Pair<Integer, ResponseEntity<String>>> {
    public interface OnFinishedLoadingEventListener {
        void onLoadingEventSuccess(JSONObject event);
        void onLoadingEventFailure(int code);
    }

    private String tag = getClass().getSimpleName();

    private OnFinishedLoadingEventListener listener;
    private int eventId;

    public GetEventTask(OnFinishedLoadingEventListener listener, int eventId) {
        this.listener = listener;
        this.eventId = eventId;
    }

    @Override
    protected Pair<Integer, ResponseEntity<String>> doInBackground(Void... params) {
        ResponseEntity<String> response;

        RestTemplate template = new RestTemplate();
        SimpleClientHttpRequestFactory factory = ((SimpleClientHttpRequestFactory) template.getRequestFactory());
        factory.setConnectTimeout(1000 * 5);
        factory.setReadTimeout(1000 * 5);


        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", UserPreferencesManager.getInstance().getCookie());

        HttpEntity<String> entity = new HttpEntity(null, headers);

        try {
            response = template.exchange(Constants.BACKEND_URL + "events/" + eventId,
                    HttpMethod.GET, entity, String.class);
            return new Pair(Constants.OK, response);
        }
        catch (ResourceAccessException e) {
            Log.w(tag, "Resource error:" + e);
            return new Pair(Constants.RESOURCE_ACCESS_ERROR, null);
        }
        catch (HttpClientErrorException e) {
            Log.w(tag, "Client exception:" + e);

            if(e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                return new Pair(Constants.UNAUTHORIZED, null);
            }

            return new Pair(Constants.CLIENT_ERROR, null);
        }
        catch(Exception e) {
            Log.w(tag, "Some error:" + e);
            return new Pair(Constants.SOME_ERROR, null);
        }
    }

    @Override
    protected void onPostExecute(Pair<Integer, ResponseEntity<String>> response) {
        if (response.first == Constants.OK) {

            try {
                JSONObject event = new JSONObject(response.second.getBody());
                listener.onLoadingEventSuccess(event);
            }
            catch(JSONException e) {
                Log.w(tag, "JSON error:" + e);
                listener.onLoadingEventFailure(Constants.JSON_PARSE_ERROR);
            }

        }
        else {
            listener.onLoadingEventFailure(response.first);
        }
    }
}