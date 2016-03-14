package com.andreasogeirik.master_frontend.communication;

import android.os.AsyncTask;
import android.util.Pair;

import com.andreasogeirik.master_frontend.listener.OnFriendRequestedListener;
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
public class FriendRequestTask extends AsyncTask<Void, Void, Pair<Integer, ResponseEntity<String>>> {

    private OnFriendRequestedListener listener;
    private int userId;

    public FriendRequestTask(OnFriendRequestedListener listener, int userId) {
        this.listener = listener;
        this.userId = userId;
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
            response = template.exchange(Constants.BACKEND_URL + "users/" + userId +  "/friendships",
                    HttpMethod.PUT, entity, String.class);
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

    @Override
    protected void onPostExecute(Pair<Integer, ResponseEntity<String>> response) {
        if (response.first == Constants.OK) {
            try {
             listener.onFriendRequestSuccess(new JSONObject(response.second.getBody()));
            }
            catch(JSONException e) {
                System.out.println("JSON parse error." + e);
                listener.onFriendRequestFailure(Constants.JSON_PARSE_ERROR);
            }
        }
        else {
            listener.onFriendRequestFailure(response.first);
        }
    }
}