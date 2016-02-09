package com.andreasogeirik.master_frontend.application.auth.register;

import android.text.TextUtils;

import com.andreasogeirik.master_frontend.application.auth.register.interfaces.RegisterInteractor;
import com.andreasogeirik.master_frontend.application.auth.register.interfaces.RegisterPresenter;
import com.andreasogeirik.master_frontend.application.auth.register.interfaces.RegisterView;
import com.andreasogeirik.master_frontend.communication.RegisterTask;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.listener.OnRegisterFinishedListener;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Andreas on 05.02.2016.
 */
public class RegisterInteractorImpl implements RegisterInteractor, OnRegisterFinishedListener {
    private RegisterPresenter presenter;

    public RegisterInteractorImpl(RegisterPresenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void registerUser(User user) {
        JSONObject jsonUser = new JSONObject();

        try {
            jsonUser.put("email", user.getEmail());
            jsonUser.put("password", user.getPassword());
            jsonUser.put("firstname", user.getFirstname());
            jsonUser.put("lastname", user.getLastname());
            jsonUser.put("location", user.getLocation());
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        new RegisterTask(jsonUser, this).execute();
    }

    @Override
    public void onRegisterSuccess(JSONObject user) {
        try {
            CurrentUser.getInstance().setUser(new User(user));
        }
        catch (JSONException e) {
            presenter.registerError(Constants.JSON_PARSE_ERROR);
        }
        presenter.registerSuccess();
    }

    @Override
    public void onRegisterError(int error) {
        presenter.registerError(error);
    }
}
