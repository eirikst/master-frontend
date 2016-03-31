package com.andreasogeirik.master_frontend.application.auth.login;

import com.andreasogeirik.master_frontend.application.auth.login.interfaces.LoginInteractor;
import com.andreasogeirik.master_frontend.application.auth.login.interfaces.LoginPresenter;
import com.andreasogeirik.master_frontend.communication.LoginTask;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.listener.OnLoginFinishedListener;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.UserPreferencesManager;

import org.json.JSONException;
import org.json.JSONObject;
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
    public void onLoginSuccess(JSONObject user, String sessionId) {
        try {
            CurrentUser.getInstance().setUser(new User(user));
            UserPreferencesManager.getInstance().saveCookie(sessionId);
            presenter.loginSuccess();
        }
        catch (JSONException e) {
            presenter.loginError(Constants.JSON_PARSE_ERROR);
        }
    }

    @Override
    public void onLoginError(int error) {
        presenter.loginError(error);
    }
}
