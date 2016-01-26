package com.andreasogeirik.master_frontend;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.andreasogeirik.master_frontend.auth.login.LoginActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends ActionBarActivity {

    String url = "http://78.91.1.119:8080/";

    @Bind(R.id.login) Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);


//        new GetRequest().execute(url + "test");

        /*try {
            RestTemplate template = new RestTemplate();
            JSONObject obj = template.getForObject(url + "test.json", JSONObject.class);
            System.out.println(obj.get("content"));
        }
        catch(JSONException e) {
            System.out.println(e.getMessage() + "bror, en feil skjedde");
        }*/
    }


    @OnClick(R.id.login)
    public void submit(){
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
