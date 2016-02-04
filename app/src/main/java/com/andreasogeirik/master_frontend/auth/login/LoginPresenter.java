package com.andreasogeirik.master_frontend.auth.login;

import com.andreasogeirik.master_frontend.auth.login.interfaces.ILoginPresenter;
import com.andreasogeirik.master_frontend.auth.login.interfaces.ILoginView;
import com.andreasogeirik.master_frontend.auth.login.interfaces.OnLoginFinishedListener;
import com.andreasogeirik.master_frontend.util.Constants;

/**
 * Created by Andreas on 26.01.2016.
 */
public class LoginPresenter implements ILoginPresenter, OnLoginFinishedListener {

    private ILoginView view;

    public LoginPresenter(ILoginView loginView){
        this.view = loginView;
    }

    @Override
    public void attemptLogin(String email, String password) {
        new LoginTask(email, password, Constants.BACKEND_URL  + "/login", this).execute();
    }

    @Override
    public void onError(String errorMessage) {
        this.view.loginFailed(errorMessage);
    }

    @Override
    public void onSuccess(String cookie) {

        this.view.navigateToEventActivity(cookie);
    }
}
