package com.andreasogeirik.master_frontend.application.general.interactors;

import com.andreasogeirik.master_frontend.communication.GetMeTask;
import com.andreasogeirik.master_frontend.listener.OnFinishedLoadingUserListener;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by eirikstadheim on 23/02/16.
 */
public class AuthenticationInteractorImpl implements OnFinishedLoadingUserListener {
    public interface AuthenticationListener {
        void findMeSuccess(User user);
        void findMeFailure(int code);
    }

    private AuthenticationListener listener;

    public AuthenticationInteractorImpl(AuthenticationListener listener) {
        if(listener == null) {
            throw new NullPointerException("Listener cannot be null in " + this.toString());
        }
        this.listener = listener;
    }

    /*
     * Used to get logged in user object
     */
    public void checkAuth() {
        new GetMeTask(this).execute();
    }

    @Override
    public void onLoadingUserSuccess(JSONObject jsonUser) {
        try {
            User user = new User(jsonUser);
            listener.findMeSuccess(user);
        }
        catch (JSONException e) {
            listener.findMeFailure(Constants.JSON_PARSE_ERROR);
        }

    }

    @Override
    public void onLoadingUserFailure(int code) {
        listener.findMeFailure(code);
    }
}
