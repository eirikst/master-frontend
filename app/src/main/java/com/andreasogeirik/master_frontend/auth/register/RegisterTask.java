package com.andreasogeirik.master_frontend.auth.register;

import android.os.AsyncTask;

import com.andreasogeirik.master_frontend.auth.register.interfaces.OnRegisterFinishedListener;

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

    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private String location;
    private String url;
    private OnRegisterFinishedListener listener;

    public RegisterTask(String email, String password, String firstname, String lastname, String location, String url, OnRegisterFinishedListener listener) {
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.location = location;
        this.url = url;
        this.listener = listener;
    }

    protected ResponseEntity<String> doInBackground(Void... params) {
        ResponseEntity<String> registerResponse;
        RestTemplate template = new RestTemplate();
        ((SimpleClientHttpRequestFactory) template.getRequestFactory()).setConnectTimeout(1000 * 10);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("email", this.email);
            jsonObject.put("password", this.password);
            jsonObject.put("firstname", this.firstname);
            jsonObject.put("lastname", this.lastname);
            jsonObject.put("location", this.location);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpEntity<String> entity = new HttpEntity(jsonObject.toString(), headers);
        try {
            registerResponse = template.exchange(this.url + "/user", HttpMethod.PUT, entity, String.class);
            return registerResponse;
        } catch (HttpClientErrorException clientException){
            clientException.getStatusCode();
            registerResponse = new ResponseEntity(clientException.getStatusCode());
            return registerResponse;
        }
        catch (ResourceAccessException resourceException) {
            return null;
        }
    }

    protected void onPostExecute(ResponseEntity<String> responseEntity) {

        // TODO: Sende svar hvis ingen kontakt med server
        if (responseEntity == null) {
            this.listener.onRegisterError("Could not connect to the server, check connection");
            return;
        }
        // TODO: Hente email of passord fra server
        if (responseEntity.getStatusCode().is2xxSuccessful()){
            this.listener.onRegisterSuccess(email, password);
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
