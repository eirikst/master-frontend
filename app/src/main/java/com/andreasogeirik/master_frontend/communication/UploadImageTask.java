package com.andreasogeirik.master_frontend.communication;

import android.os.AsyncTask;
import android.util.Pair;

import com.andreasogeirik.master_frontend.listener.OnImageUploadFinishedListener;
import com.andreasogeirik.master_frontend.listener.OnLoginFinishedListener;
import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.UserPreferencesManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.OkHttpClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Andreas on 18.02.2016.
 */
public class UploadImageTask extends AsyncTask<Void, Void, Pair<Integer, ResponseEntity<String>>> {

    private byte[] byteImage;
    private OnImageUploadFinishedListener listener;

    public UploadImageTask(byte[] byteImage, OnImageUploadFinishedListener listener) {
        this.byteImage = byteImage;
        this.listener = listener;
    }

    protected Pair<Integer, ResponseEntity<String>> doInBackground(Void... params) {
        ResponseEntity<String> response;
        RestTemplate template = new RestTemplate();
        ((OkHttpClientHttpRequestFactory) template.getRequestFactory()).setConnectTimeout(1000 * 10);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Cookie", UserPreferencesManager.getInstance().getCookie());
        HttpEntity<String> entity = new HttpEntity(byteImage, headers);

        try {
            response = template.exchange(Constants.BACKEND_URL + "image", HttpMethod.PUT, entity, String.class);
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
        if (response.first == Constants.OK) {
                listener.onImageUploadSuccess(response.second.getBody());
        } else {
            listener.onImageUploadError(response.first);
        }
    }
}
