package com.andreasogeirik.master_frontend.application.auth.login;

import com.andreasogeirik.master_frontend.application.auth.login.interfaces.LoginInteractor;
import com.andreasogeirik.master_frontend.application.auth.login.interfaces.LoginPresenter;
import com.andreasogeirik.master_frontend.communication.LoginTask;
import com.andreasogeirik.master_frontend.listener.OnLoginFinishedListener;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.SessionManager;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Created by eirikstadheim on 09/02/16.
 */
public class LoginInteractorImpl implements LoginInteractor, OnLoginFinishedListener {
    LoginPresenter presenter;

    public LoginInteractorImpl(LoginPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void attemptLogin(User user) {
        MultiValueMap<String, String> credentials = new LinkedMultiValueMap<>();
        credentials.add("username", user.getEmail());
        credentials.add("password", user.getPassword());

        new LoginTask(credentials, this).execute();
    }

    @Override
    public void onLoginError(String errorMessage) {
        presenter.loginError(errorMessage);
    }

    @Override
    public void onLoginSuccess(String cookie) {
        SessionManager.getInstance().saveCookie(cookie);
        presenter.loginSuccess();
    }
}
