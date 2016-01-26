package com.andreasogeirik.master_frontend;

import android.os.AsyncTask;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by eirikstadheim on 20/01/16.
 */
public class GetRequest extends AsyncTask<String, Integer, String> {
    protected String doInBackground(String... urls) {
        try{
            RestTemplate template = new RestTemplate();
            template.getMessageConverters().add(new StringHttpMessageConverter());
            String string = template.getForObject(urls[0], String.class);
            return string;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return "";
        }
    }

    protected void onProgressUpdate(Integer... progress) {

    }

    protected void onPostExecute(String result) {
        System.out.println(result);
    }
}