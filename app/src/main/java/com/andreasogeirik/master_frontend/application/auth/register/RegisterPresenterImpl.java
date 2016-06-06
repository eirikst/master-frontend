package com.andreasogeirik.master_frontend.application.auth.register;

import android.text.TextUtils;

import com.andreasogeirik.master_frontend.application.auth.register.interfaces.RegisterInteractor;
import com.andreasogeirik.master_frontend.application.auth.register.interfaces.RegisterPresenter;
import com.andreasogeirik.master_frontend.application.auth.register.interfaces.RegisterView;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.validation.EmailValidator;

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
        String trimmedEmail = email.trim();

        if (TextUtils.isEmpty(trimmedEmail)) {
            registerView.setEmailError("Enter an email address");
        }
        else if (!EmailValidator.validate(trimmedEmail)){
            registerView.setEmailError("This is not a valid email address");
        }
        else if (TextUtils.isEmpty(password)) {
            registerView.setPasswordError("Enter a password");
        } else if (password.length() < 3) {
            registerView.setPasswordError("The password must be at least 3 characters");
        } else if (!TextUtils.equals(password, rePassword)) {
            registerView.setPasswordError("The password don't match");
        } else if (TextUtils.isEmpty(firstname)) {
            registerView.setFirstnameError("Enter a first name");
        } else if (TextUtils.isEmpty(lastname)) {
            registerView.setLastnameError("Enter a surname");
        } else if (TextUtils.isEmpty(location)) {
            registerView.setLocationError("Enter a location");
        } else {
            registerView.showProgress();
            interactor.registerUser(new User(trimmedEmail, password, firstname, lastname, location));
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
                this.registerView.registrationFailed("Could not find the requested resource. Please try again.");
                break;
            // This can't happen. Do Nothing
            case Constants.UNAUTHORIZED:
                break;
            case Constants.CLIENT_ERROR:
                this.registerView.registrationFailed("This email is already registered.");
                break;
            case Constants.SOME_ERROR:
                this.registerView.registrationFailed("Something went wrong. Please try again later.");
                break;
        }
    }
}
