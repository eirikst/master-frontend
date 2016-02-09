package com.andreasogeirik.master_frontend.application.auth.welcome;

import com.andreasogeirik.master_frontend.application.auth.welcome.interfaces.WelcomeInteractor;
import com.andreasogeirik.master_frontend.application.auth.welcome.interfaces.WelcomePresenter;
import com.andreasogeirik.master_frontend.application.auth.welcome.interfaces.WelcomeView;
import com.andreasogeirik.master_frontend.communication.LoginTask;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.listener.OnLoginFinishedListener;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Created by Andreas on 05.02.2016.
 */
public class WelcomeInteractorImpl implements WelcomeInteractor, OnLoginFinishedListener {

    private WelcomePresenter presenter;

    public WelcomeInteractorImpl(WelcomePresenter presenter) {
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
    public void onLoginError(int error) {
        presenter.loginError(error);
    }

    @Override
    public void onLoginSuccess(JSONObject user, String sessionId) {
        //save current user and session
        try {
            CurrentUser.getInstance().setUser(new User(user));
        }
        catch (JSONException e) {
            presenter.loginError(Constants.JSON_PARSE_ERROR);
        }

        SessionManager.getInstance().saveCookie(sessionId);
        presenter.loginSuccess();
    }
}
