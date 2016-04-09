package com.andreasogeirik.master_frontend.communication;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.andreasogeirik.master_frontend.listener.OnUpdateUserFinishedListener;
import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.UserPreferencesManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.OkHttpClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Andreas on 09.03.2016.
 */
public class UpdateUserTask extends AsyncTask<Void, Void, Pair<Integer, ResponseEntity<String>>> {
    private String tag = getClass().getSimpleName();

    private JSONObject user;
    private OnUpdateUserFinishedListener listener;

    public UpdateUserTask(JSONObject user, OnUpdateUserFinishedListener listener) {
        this.listener = listener;
        this.user = user;
    }

    protected Pair<Integer, ResponseEntity<String>> doInBackground(Void... params) {
        ResponseEntity<String> response;
        RestTemplate template = new RestTemplate();
        ((OkHttpClientHttpRequestFactory) template.getRequestFactory()).setConnectTimeout(1000 * 10);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");
        headers.set("Cookie", UserPreferencesManager.getInstance().getCookie());
        HttpEntity<String> entity = new HttpEntity(user.toString(), headers);

        try {
            response = template.exchange(Constants.BACKEND_URL + "users", HttpMethod.POST, entity, String.class);
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

    protected void onPostExecute(Pair<Integer, ResponseEntity<String>> response) {
        if (response.first == Constants.OK) {

            try {
                JSONObject user = new JSONObject(response.second.getBody());
                listener.onUpdateUserSuccess(user);
            } catch (JSONException e) {
                Log.w(tag, "JSON error:" + e);
                listener.onUserUpdateError(Constants.JSON_PARSE_ERROR);
            }

        } else {
            listener.onUserUpdateError(response.first);
        }
    }
}
