package com.andreasogeirik.master_frontend.auth.login;

import android.text.TextUtils;

import com.andreasogeirik.master_frontend.auth.login.interfaces.LoginPresenter;
import com.andreasogeirik.master_frontend.auth.login.interfaces.LoginView;
import com.andreasogeirik.master_frontend.auth.login.interfaces.OnLoginFinishedListener;
import com.andreasogeirik.master_frontend.util.Constants;

/**
 * Created by Andreas on 26.01.2016.
 */
public class LoginPresenterImpl implements LoginPresenter, OnLoginFinishedListener {

    private LoginView loginView;

    public LoginPresenterImpl(LoginView loginView){
        this.loginView = loginView;
    }

    @Override
    public void attemptLogin(String email, String password) {

        // TODO: Må forbedre validering med email regex, password policy, osv.
        if (TextUtils.isEmpty(email)){
            loginView.setEmailError("The email is empty");
        }
        else if (TextUtils.isEmpty(password)){
            loginView.setPasswordError("The password is empty");
        }
        else{
            loginView.showProgress();
            new LoginTask(email, password, Constants.BACKEND_URL, this).execute();
        }
    }

    @Override
    public void onLoginError(String errorMessage) {
        loginView.hideProgress();
        loginView.loginFailed(errorMessage);
    }

    @Override
    public void onLoginSuccess(String cookie) {
        loginView.navigateToEventActivity(cookie);
    }
}
