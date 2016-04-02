package com.andreasogeirik.master_frontend.communication;

import android.util.Pair;

import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.UserPreferencesManager;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Andreas on 05.02.2016.
 */
public class LogoutTask {

    public void logout() {
        ResponseEntity<String> logoutResponse;
        RestTemplate template = new RestTemplate();
        ((SimpleClientHttpRequestFactory) template.getRequestFactory()).setConnectTimeout(1000 * 10);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", UserPreferencesManager.getInstance().getCookie());
        HttpEntity<String> entity = new HttpEntity(null, headers);

        try {
            ResponseEntity<String> response = template.exchange(Constants.BACKEND_URL + "logout",
                    HttpMethod.POST, entity, String.class);
        }
        catch (HttpClientErrorException clientException) {
            System.out.println("Logout failed. " + clientException);
        }
        catch (ResourceAccessException resourceException) {
            System.out.println("Logout failed. " + resourceException);
        }
        catch(Exception e) {
            System.out.println("Some error:" + e);
        }
    }
}

