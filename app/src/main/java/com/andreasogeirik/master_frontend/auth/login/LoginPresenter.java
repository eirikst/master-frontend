package com.andreasogeirik.master_frontend.auth.login;

import com.andreasogeirik.master_frontend.model.User;
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
    public void attemptLogin(String username, String password) {
        User user = new User(username, password);
        new LoginTask(user, Constants.BACKEND_URL  + "/account/login").execute();
    }

    @Override
    public void onError() {
        this.view.loginFailed();
    }

    @Override
    public void onSuccess() {
        this.view.navigateToListActivity();
    }
}
