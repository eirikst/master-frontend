package com.andreasogeirik.master_frontend.auth.register;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.auth.register.interfaces.IRegisterView;

import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity implements IRegisterView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    public void navigateToEventActivity(JSONObject object) {

    }

    @Override
    public void registrationFailed(String errorMessage) {

    }
}
