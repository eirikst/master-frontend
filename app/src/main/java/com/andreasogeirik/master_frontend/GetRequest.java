package com.andreasogeirik.master_frontend;

import android.os.AsyncTask;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.URL;

/**
 * Created by eirikstadheim on 20/01/16.
 */
class GetRequest extends AsyncTask<String, Integer, String> {
    protected String doInBackground(String... urls) {
        RestTemplate template = new RestTemplate();
        template.getMessageConverters().add(new StringHttpMessageConverter());
        String string = template.getForObject(urls[0], String.class);
        return string;
    }

    protected void onProgressUpdate(Integer... progress) {

    }

    protected void onPostExecute(String result) {
        System.out.println(result);
    }
}