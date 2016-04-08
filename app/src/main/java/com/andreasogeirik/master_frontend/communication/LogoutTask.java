package com.andreasogeirik.master_frontend.communication;

import android.util.Log;

import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.UserPreferencesManager;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.OkHttpClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Andreas on 05.02.2016.
 */
public class LogoutTask {
    private String tag = getClass().getSimpleName();

    public void logout() {
        ResponseEntity<String> logoutResponse;
        RestTemplate template = new RestTemplate();
        ((OkHttpClientHttpRequestFactory) template.getRequestFactory()).setConnectTimeout(1000 * 10);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", UserPreferencesManager.getInstance().getCookie());
        HttpEntity<String> entity = new HttpEntity("{}", headers);

        try {
            ResponseEntity<String> response = template.exchange(Constants.BACKEND_URL + "logout",
                    HttpMethod.POST, entity, String.class);
        }
        catch (HttpClientErrorException clientException) {
            Log.w(tag, "Logout failed. " + clientException);
        }
        catch (ResourceAccessException resourceException) {
            Log.w(tag, "Logout failed. " + resourceException);
        }
        catch(Exception e) {
            Log.w(tag, "Some error:" + e);
        }
    }
}

