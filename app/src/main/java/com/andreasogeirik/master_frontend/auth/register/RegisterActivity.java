package com.andreasogeirik.master_frontend.auth.register;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.auth.register.interfaces.RegisterView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity implements RegisterView {

    @Bind(R.id.email_register)
    EditText mEmailView;
    @Bind(R.id.password_register)
    EditText mPasswordView;
    @Bind(R.id.re_password_register)
    EditText mRePasswordView;
    @Bind(R.id.firstname)
    EditText mFirstnameView;
    @Bind(R.id.lastname)
    EditText mLastnameView;
    @Bind(R.id.location)
    EditText mLocationView;

    @Bind(R.id.register_form)
    View mRegisterFormView;

    @Bind(R.id.register_progress)
    View mProgressView;

    @Bind(R.id.register_button)
    Button mRegister_button;

    RegisterPresenterImpl presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        this.presenter = new RegisterPresenterImpl(this);
    }

    @OnClick(R.id.register_button)
    public void onClick() {
        showProgress();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String rePassword = mRePasswordView.getText().toString();
        String firstname = mFirstnameView.getText().toString();
        String lastname = mLastnameView.getText().toString();
        String location = mLocationView.getText().toString();

        presenter.validateCredentials(email, password, rePassword, firstname, lastname, location);
    }

    @Override
    public void navigateToWelcomeActivity(String email, String password) {
        Intent i = new Intent(this, WelcomeActivity.class);
        i.putExtra("email", email);
        i.putExtra("password", password);
        startActivity(i);
        finish();
    }

    @Override
    public void registrationFailed(String error) {
        Toast toast = Toast.makeText(this, error, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void setEmailError(String error) {
        mEmailView.setError(error);
        View focusView = null;
        focusView = mEmailView;
        focusView.requestFocus();
    }

    @Override
    public void setPasswordError(String error) {
        mPasswordView.setError(error);
        View focusView = null;
        focusView = mPasswordView;
        focusView.requestFocus();
    }

    @Override
    public void setFirstnameError(String error) {
        mFirstnameView.setError(error);
        View focusView = null;
        focusView = mFirstnameView;
        focusView.requestFocus();
    }

    @Override
    public void setLastnameError(String error) {
        mLastnameView.setError(error);
        View focusView = null;
        focusView = mLastnameView;
        focusView.requestFocus();
    }

    @Override
    public void setLocationError(String error) {
        mLocationView.setError(error);
        View focusView = null;
        focusView = mLocationView;
        focusView.requestFocus();
    }
}
