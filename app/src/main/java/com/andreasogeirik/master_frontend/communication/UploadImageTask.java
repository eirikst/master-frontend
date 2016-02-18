package com.andreasogeirik.master_frontend.communication;

import android.os.AsyncTask;
import android.util.Pair;

import com.andreasogeirik.master_frontend.listener.OnImageUploadFinishedListener;
import com.andreasogeirik.master_frontend.listener.OnLoginFinishedListener;
import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Andreas on 18.02.2016.
 */
public class UploadImageTask extends AsyncTask<Void, Void, Pair<Integer, ResponseEntity<String>>> {

    private JSONObject jsonImage;
    private OnImageUploadFinishedListener listener;

    public UploadImageTask(JSONObject jsonImage, OnImageUploadFinishedListener listener) {
        this.jsonImage = jsonImage;
        this.listener = listener;
    }

    protected Pair<Integer, ResponseEntity<String>> doInBackground(Void... params) {
        ResponseEntity<String> response;
        RestTemplate template = new RestTemplate();
        ((SimpleClientHttpRequestFactory) template.getRequestFactory()).setConnectTimeout(1000 * 10);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Cookie", SessionManager.getInstance().getCookie());
        HttpEntity<String> entity = new HttpEntity(jsonImage.toString(), headers);

        try {
            response = template.exchange(Constants.BACKEND_URL + "image", HttpMethod.PUT, entity, String.class);
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
                JSONObject image = new JSONObject(response.second.getBody());
                listener.onImageUploadSuccess(image);
            } catch (JSONException e) {
                System.out.println("JSON error:" + e);
                listener.onImageUploadError(Constants.JSON_PARSE_ERROR);
            }

        } else {
            listener.onImageUploadError(response.first);
        }
    }
}
