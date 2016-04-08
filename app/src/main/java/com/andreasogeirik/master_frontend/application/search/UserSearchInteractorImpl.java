package com.andreasogeirik.master_frontend.application.search;

import android.util.Log;

import com.andreasogeirik.master_frontend.application.search.interfaces.UserSearchInteractor;
import com.andreasogeirik.master_frontend.application.search.interfaces.UserSearchPresenter;
import com.andreasogeirik.master_frontend.communication.SearchUsersTask;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by eirikstadheim on 16/02/16.
 */
public class UserSearchInteractorImpl implements UserSearchInteractor,
        SearchUsersTask.OnFinishedSearchingUsersListener {
    private String tag = getClass().getSimpleName();

    private UserSearchPresenter presenter;

    public UserSearchInteractorImpl(UserSearchPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void searchForUsers(String searchString, int offset) {
        new SearchUsersTask(this, searchString, offset).execute();
    }

    @Override
    public void onSuccessUserSearch(JSONArray usersJson, int offset) {
        Set<User> users = new HashSet<>();
        try {
            for(int i = 0; i < usersJson.length(); i++) {
                users.add(new User(usersJson.getJSONObject(i)));
            }
            presenter.onSuccessUserSearch(users, offset);
        }
        catch (JSONException e) {
            Log.w(tag, "JSON error: " + e);
            presenter.onFailureUserSearch(Constants.CLIENT_ERROR);
        }
    }

    @Override
    public void onFailedUserSearch(int code) {
        presenter.onFailureUserSearch(code);
    }
}
