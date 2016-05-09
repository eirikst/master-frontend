package com.andreasogeirik.master_frontend.application.auth.register;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.auth.register.interfaces.RegisterPresenter;
import com.andreasogeirik.master_frontend.application.auth.register.interfaces.RegisterView;
import com.andreasogeirik.master_frontend.application.user.photo.PhotoActivity;
import com.andreasogeirik.master_frontend.layout.ProgressBarManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity implements RegisterView {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.email_register)
    EditText emailView;
    @Bind(R.id.password_register)
    EditText passwordView;
    @Bind(R.id.re_password_register)
    EditText rePasswordView;
    @Bind(R.id.firstname)
    EditText firstnameView;
    @Bind(R.id.lastname)
    EditText lastnameView;
    @Bind(R.id.location)
    EditText locationView;
    @Bind(R.id.register_error)
    TextView errorMessage;

    @Bind(R.id.register_form)
    View registerFormView;

    @Bind(R.id.register_progress)
    View progressView;

    @Bind(R.id.register_button)
    AppCompatButton mRegister_button;

    private RegisterPresenter presenter;
    private ProgressBarManager progressBarManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        ButterKnife.bind(this);
        this.presenter = new RegisterPresenterImpl(this);
        this.progressBarManager = new ProgressBarManager(this, registerFormView, progressView);
        setupToolbar();

        ColorStateList csl = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.teal));
        mRegister_button.setSupportBackgroundTintList(csl);
    }

    /*
     * Toolbar setup
     */
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    @OnClick(R.id.register_button)
    public void onClick() {
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();
        String rePassword = rePasswordView.getText().toString();
        String firstname = firstnameView.getText().toString();
        String lastname = lastnameView.getText().toString();
        String location = locationView.getText().toString();

        presenter.registerUser(email, password, rePassword, firstname, lastname, location);
    }

    @Override
    public void navigateToPhotoView() {
        Intent i = new Intent(this, PhotoActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    @Override
    public void registrationFailed(String error) {
        errorMessage.setText(error);
        errorMessage.setVisibility(View.VISIBLE);
        errorMessage.requestFocus();
    }

    @Override
    public void showProgress() {
        this.progressBarManager.showProgress(true);
    }

    @Override
    public void hideProgress() {
        this.progressBarManager.showProgress(false);
    }

    @Override
    public void setEmailError(String error) {
        emailView.setError(error);
        View focusView = emailView;
        focusView.requestFocus();
    }

    @Override
    public void setPasswordError(String error) {
        passwordView.setError(error);
        View focusView = passwordView;
        focusView.requestFocus();
    }

    @Override
    public void setFirstnameError(String error) {
        firstnameView.setError(error);
        View focusView = firstnameView;
        focusView.requestFocus();
    }

    @Override
    public void setLastnameError(String error) {
        lastnameView.setError(error);
        View focusView = lastnameView;
        focusView.requestFocus();
    }

    @Override
    public void setLocationError(String error) {
        locationView.setError(error);
        View focusView = locationView;
        focusView.requestFocus();
    }
}
