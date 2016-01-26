package com.andreasogeirik.master_frontend.auth.login;

import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;

/**
 * Created by Andreas on 26.01.2016.
 */
public class LoginPresenter implements ILoginPresenter {

    private ILoginView view;

    public LoginPresenter(ILoginView loginView){
        this.view = loginView;
    }

    @Override
    public void attemptLogin(String username, String password) {
        User user = new User(username, password);
        new LoginAsync(user, Constants.BACKEND_URL).execute();
    }
}
