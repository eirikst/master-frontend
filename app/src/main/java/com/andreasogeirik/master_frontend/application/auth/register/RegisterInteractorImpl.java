package com.andreasogeirik.master_frontend.application.auth.register;

import com.andreasogeirik.master_frontend.application.auth.register.interfaces.RegisterInteractor;
import com.andreasogeirik.master_frontend.application.auth.register.interfaces.RegisterPresenter;
import com.andreasogeirik.master_frontend.communication.LoginTask;
import com.andreasogeirik.master_frontend.communication.RegisterTask;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.listener.OnLoginFinishedListener;
import com.andreasogeirik.master_frontend.listener.OnRegisterFinishedListener;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.UserPreferencesManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


/**
 * Created by Andreas on 05.02.2016.
 */
public class RegisterInteractorImpl implements RegisterInteractor, OnRegisterFinishedListener, OnLoginFinishedListener {
    private RegisterPresenter presenter;
    private String password;

    public RegisterInteractorImpl(RegisterPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void registerUser(User user) {
        this.password = user.getPassword();
        new RegisterTask(userToJson(user), this).execute();

    }

    @Override
    public void onRegisterSuccess(JSONObject jsonUser) {
        try {
            User user = new User(jsonUser);
            CurrentUser.getInstance().setUser(user);
            MultiValueMap<String, String> credentials = new LinkedMultiValueMap<>();
            credentials.add("username", user.getEmail());
            credentials.add("password", this.password);
            new LoginTask(credentials, this).execute();
        } catch (JSONException e) {
            this.presenter.registerOrLoginError(Constants.JSON_PARSE_ERROR);
        }
    }

    @Override
    public void onRegisterError(int error) {
        presenter.registerOrLoginError(error);
    }

    private JSONObject userToJson(User user) {

        JSONObject jsonUser = new JSONObject();
        try {
            jsonUser.put("email", user.getEmail());
            jsonUser.put("password", user.getPassword());
            jsonUser.put("firstname", user.getFirstname());
            jsonUser.put("lastname", user.getLastname());
            jsonUser.put("location", user.getLocation());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonUser;
    }

    @Override
    public void onLoginError(int error) {
        this.presenter.registerOrLoginError(error);
    }

    @Override
    public void onLoginSuccess(JSONObject jsonUser, String sessionId) {
        UserPreferencesManager.getInstance().saveCookie(sessionId);
        this.presenter.registerAndLoginSuccess();
    }
}
