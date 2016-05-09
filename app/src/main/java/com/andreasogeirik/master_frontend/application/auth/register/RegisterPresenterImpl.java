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
            registerView.setEmailError("Skriv inn e-post");
        }
        else if (!EmailValidator.validate(trimmedEmail)){
            registerView.setEmailError("Dette er ikke en gyldig e-post");
        }
        else if (TextUtils.isEmpty(password)) {
            registerView.setPasswordError("Skriv inn et passord");
        } else if (password.length() < 3) {
            registerView.setPasswordError("Passordet må være minst 3 tegn langt");
        } else if (!TextUtils.equals(password, rePassword)) {
            registerView.setPasswordError("Passordene matcher ikke");
        } else if (TextUtils.isEmpty(firstname)) {
            registerView.setFirstnameError("Skriv inn et fornavn");
        } else if (TextUtils.isEmpty(lastname)) {
            registerView.setLastnameError("Skriv inn et etternavn");
        } else if (TextUtils.isEmpty(location)) {
            registerView.setLocationError("Skriv inn et sted");
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
