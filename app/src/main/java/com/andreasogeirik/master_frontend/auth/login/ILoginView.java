package com.andreasogeirik.master_frontend.auth.login;

import org.json.JSONObject;

/**
 * Created by Andreas on 26.01.2016.
 */
public interface ILoginView {
    void navigateToListActivity(JSONObject object);
    void loginFailed(String errorMessage);
}
