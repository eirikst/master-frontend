package com.andreasogeirik.master_frontend.application.auth.login;

import android.text.TextUtils;

import com.andreasogeirik.master_frontend.application.auth.login.interfaces.LoginInteractor;
import com.andreasogeirik.master_frontend.application.auth.login.interfaces.LoginPresenter;
import com.andreasogeirik.master_frontend.application.auth.login.interfaces.LoginView;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;

/**
 * Created by Andreas on 26.01.2016.
 */
public class LoginPresenterImpl implements LoginPresenter {
    private LoginView loginView;
    private LoginInteractor interactor;

    public LoginPresenterImpl(LoginView loginView){
        this.loginView = loginView;
        this.interactor = new LoginInteractorImpl(this);
    }

    @Override
    public void attemptLogin(User user) {

        // TODO: Må forbedre validering med email regex, password policy, osv. Interactor stuff??
        if (TextUtils.isEmpty(user.getEmail())){
            loginView.setEmailError("The email is empty");
        }
        else if (TextUtils.isEmpty(user.getPassword())){
            loginView.setPasswordError("The password is empty");
        }
        else{
            loginView.showProgress();
            interactor.attemptLogin(user);
        }
    }

    @Override
    public void loginSuccess() {
        loginView.navigateToEventView();
    }

    @Override
    public void loginError(int error) {
        loginView.hideProgress();

        if(error == Constants.CLIENT_ERROR) {
            loginView.loginFailed("Feil brukernavn eller passord");
        }
        else if(error == Constants.RESOURCE_ACCESS_ERROR) {
            loginView.loginFailed("Fant ikke ressurs. Prøv igjen.");
        }
    }
}
