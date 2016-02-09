package com.andreasogeirik.master_frontend.application.auth.register;

import android.text.TextUtils;

import com.andreasogeirik.master_frontend.application.auth.register.interfaces.RegisterInteractor;
import com.andreasogeirik.master_frontend.communication.RegisterTask;
import com.andreasogeirik.master_frontend.listener.OnRegisterFinishedListener;
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
    public void registerUser(User user, String password) {
        // TODO: Må forbedre validering med email regex, password policy, osv.
        if (TextUtils.isEmpty(user.getEmail())){
            registerView.setEmailError("The email is empty");
        }
        else if (TextUtils.isEmpty(user.getPassword())){
            registerView.setPasswordError("The password is empty");
        }
        else if (user.getPassword().length() < 3){
            registerView.setPasswordError("The password needs to be at least 3 characters long");
        }
        else if (!TextUtils.equals(user.getPassword(), password)){
            registerView.setPasswordError("The passwords don't match");
        }
        else if (TextUtils.isEmpty(user.getFirstname())){
            registerView.setFirstnameError("The firstname is empty");
        }
        else if (TextUtils.isEmpty(user.getLastname())){
            registerView.setLastnameError("The lastname is empty");
        }
        else if (TextUtils.isEmpty(user.getLocation())){
            registerView.setLocationError("The location is empty");
        }
        else{
            registerView.showProgress();
            interactor.registerUser(user);
        }
    }

    @Override
    public void registerSuccess() {
        registerView.navigateToWelcomeActivity();
    }

    @Override
    public void registerError(String error) {
        registerView.hideProgress();
        registerView.registrationFailed(error);
    }
}
