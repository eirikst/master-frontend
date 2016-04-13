package com.andreasogeirik.master_frontend.application.auth.register;

import android.text.TextUtils;

import com.andreasogeirik.master_frontend.application.auth.register.interfaces.RegisterInteractor;
import com.andreasogeirik.master_frontend.application.auth.register.interfaces.RegisterPresenter;
import com.andreasogeirik.master_frontend.application.auth.register.interfaces.RegisterView;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;

/**
 * Created by Andreas on 05.02.2016.
 */
public class RegisterPresenterImpl implements RegisterPresenter {

    private RegisterView registerView;
    private RegisterInteractor interactor;

    public RegisterPresenterImpl(RegisterView registerView) {
        this.registerView = registerView;
        this.interactor = new RegisterInteractorImpl(this);
    }


    @Override
    public void registerUser(String email, String password, String rePassword, String firstname, String lastname, String location) {
        // TODO: Må forbedre validering med email regex, password policy, osv.
        if (TextUtils.isEmpty(email)) {
            registerView.setEmailError("The email is empty");
        } else if (TextUtils.isEmpty(password)) {
            registerView.setPasswordError("The password is empty");
        } else if (password.length() < 3) {
            registerView.setPasswordError("The password needs to be at least 3 characters long");
        } else if (!TextUtils.equals(password, rePassword)) {
            registerView.setPasswordError("The passwords don't match");
        } else if (TextUtils.isEmpty(firstname)) {
            registerView.setFirstnameError("The firstname is empty");
        } else if (TextUtils.isEmpty(lastname)) {
            registerView.setLastnameError("The lastname is empty");
        } else if (TextUtils.isEmpty(location)) {
            registerView.setLocationError("The location is empty");
        } else {
            registerView.showProgress();
            interactor.registerUser(new User(email, password, firstname, lastname, location));
        }
    }

    @Override
    public void registerAndLoginSuccess() {
        registerView.hideProgress();
        registerView.navigateToPhotoView();
    }

    @Override
    public void registerOrLoginError(int error) {
        registerView.hideProgress();
        switch (error) {
            case Constants.RESOURCE_ACCESS_ERROR:
                this.registerView.registrationFailed("Fant ikke ressurs. Prøv igjen");
                break;
            // This can't happen. Do Nothing
            case Constants.UNAUTHORIZED:
                break;
            case Constants.CLIENT_ERROR:
                this.registerView.registrationFailed("E-posten er allerede registrert i systemet.");
                break;
            case Constants.SOME_ERROR:
                this.registerView.registrationFailed("Noe gikk galt, prøv igjen senere");
                break;
        }
    }
}
