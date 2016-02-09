package com.andreasogeirik.master_frontend.communication;

import android.os.AsyncTask;

import com.andreasogeirik.master_frontend.listener.OnRegisterFinishedListener;
import com.andreasogeirik.master_frontend.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Andreas on 05.02.2016.
 */
public class RegisterTask extends AsyncTask<Void, Void, ResponseEntity<String>> {

    private JSONObject user;
    private OnRegisterFinishedListener listener;

    public RegisterTask(JSONObject user, OnRegisterFinishedListener listener) {
        this.listener = listener;
        this.user = user;
    }

    protected ResponseEntity<String> doInBackground(Void... params) {
        ResponseEntity<String> registerResponse;
        RestTemplate template = new RestTemplate();
        ((SimpleClientHttpRequestFactory) template.getRequestFactory()).setConnectTimeout(1000 * 10);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity(user.toString(), headers);

        try {
            registerResponse = template.exchange(Constants.BACKEND_URL + "user", HttpMethod.PUT, entity, String.class);
            return registerResponse;
        }
        catch (HttpClientErrorException clientException){
            clientException.getStatusCode();
            registerResponse = new ResponseEntity(clientException.getStatusCode());
            return registerResponse;
        }
        catch (ResourceAccessException resourceException) {
            return null;
        }
    }

    protected void onPostExecute(ResponseEntity<String> responseEntity) {
        if (responseEntity == null) {
            this.listener.onRegisterError("Could not connect to the server, check connection");
            return;
        }
        // TODO: Hente email of passord fra server
        if (responseEntity.getStatusCode().is2xxSuccessful()){
            try {
                this.listener.onRegisterSuccess(new JSONObject(responseEntity.getBody()));
            }
            catch(JSONException e) {
                listener.onRegisterError("Plutselig skjer det noe galt vettu");
            }
            return;
        }
        else if (responseEntity.getStatusCode() == HttpStatus.CONFLICT){
            this.listener.onRegisterError("The email already exists");
            return;
        }
        else{
            this.listener.onRegisterError("Please try again");
            return;
        }
    }
}
