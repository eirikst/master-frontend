package com.andreasogeirik.master_frontend.gcm;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.UserPreferencesManager;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.OkHttpClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by eirikstadheim on 06/04/16.
 */
public class GcmApiService  extends AsyncTask<Void, Void, Pair<Integer, ResponseEntity<String>>> {
    public interface Listener {
        void onGcmTaskFinished();
    }
    public static final int ADD_TOKEN = 1;
    public static final int REMOVE_TOKEN = 2;

    private String token;
    private HttpMethod method;
    private Listener listener;

    public GcmApiService(String token, int status, Listener listener) {
        this.token = token;

        if(status == REMOVE_TOKEN) {
            method = HttpMethod.DELETE;
        }
        else {
            method = HttpMethod.POST;
        }
        this.listener = listener;
    }

    @Override
    protected Pair<Integer, ResponseEntity<String>> doInBackground(Void... params) {
        ResponseEntity<String> response;
        RestTemplate template = new RestTemplate();
        ((OkHttpClientHttpRequestFactory) template.getRequestFactory()).setConnectTimeout(1000 * 10);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", UserPreferencesManager.getInstance().getCookie());

        try {
            if(method == HttpMethod.DELETE) {
                HttpEntity<String> entity = new HttpEntity("{}", headers);
                response = template.exchange(Constants.BACKEND_URL + "gcm?gcmToken=" + token, method, entity, String.class);
            }
            else {
                MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
                body.add("gcmToken", token);
                HttpEntity<String> entity = new HttpEntity(body, headers);
                response = template.exchange(Constants.BACKEND_URL + "gcm", method, entity, String.class);
            }
            return new Pair(Constants.OK, response);
        }
        catch (ResourceAccessException e) {
            Log.w(getClass().getSimpleName(), e);
            return new Pair(Constants.RESOURCE_ACCESS_ERROR, null);
        }
        catch (HttpClientErrorException e) {
            Log.w(getClass().getSimpleName(), e);
            if(e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                return new Pair(Constants.UNAUTHORIZED, null);
            }
            return new Pair(Constants.CLIENT_ERROR, null);
        }
        catch(Exception e) {
            Log.w(getClass().getSimpleName(), e);
            return new Pair(Constants.SOME_ERROR, null);
        }
        finally {
            if(listener != null) {
                listener.onGcmTaskFinished();
            }
        }
    }
}
