package com.andreasogeirik.master_frontend.communication;

import android.os.AsyncTask;

import com.andreasogeirik.master_frontend.listener.OnFinishedLoadingPostsListener;
import com.andreasogeirik.master_frontend.model.Post;
import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
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
public class GetPostsTask extends AsyncTask<Void, Void, ResponseEntity<String>> {

    private OnFinishedLoadingPostsListener listener;

    public GetPostsTask(OnFinishedLoadingPostsListener listener) {
        this.listener = listener;
    }

    @Override
    protected ResponseEntity<String> doInBackground(Void... params) {
        ResponseEntity<String> response;

        RestTemplate template = new RestTemplate();
        ((SimpleClientHttpRequestFactory) template.getRequestFactory()).setConnectTimeout(1000 * 10);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", SessionManager.getInstance().getCookie());

        HttpEntity<String> entity = new HttpEntity(null, headers);

        try {
            response = template.exchange(Constants.BACKEND_URL + "user/post?start=0", HttpMethod.GET
                    , entity, String.class);
            return response;
        }
        catch (HttpClientErrorException clientException) {
            return new ResponseEntity(clientException.getStatusCode());
        }
        catch (ResourceAccessException resourceException) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(ResponseEntity<String> response) {
        if (response == null) {
            listener.onFailedPostsLoad("Could not connect to the server, check connection");
        } else {
            HttpStatus statusCode = response.getStatusCode();
            if (statusCode.equals(HttpStatus.OK)) {

                try {
                    JSONArray posts = new JSONArray(response.getBody());
                    listener.onSuccessPostsLoad(posts);
                }
                catch(JSONException e) {
                    throw new RuntimeException(e);
                }

            } else {
                listener.onFailedPostsLoad("" + response.toString());
            }
        }
    }
}