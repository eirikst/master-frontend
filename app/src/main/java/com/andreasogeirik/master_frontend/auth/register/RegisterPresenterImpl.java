package com.andreasogeirik.master_frontend.auth.register;

import android.text.TextUtils;

import com.andreasogeirik.master_frontend.auth.register.interfaces.OnRegisterFinishedListener;
import com.andreasogeirik.master_frontend.auth.register.interfaces.RegisterPresenter;
import com.andreasogeirik.master_frontend.auth.register.interfaces.RegisterView;
import com.andreasogeirik.master_frontend.util.Constants;


/**
 * Created by Andreas on 05.02.2016.
 */
public class RegisterPresenterImpl implements RegisterPresenter, OnRegisterFinishedListener {

    RegisterView registerView;

    public RegisterPresenterImpl(RegisterView registerView) {
        this.registerView = registerView;
    }


    @Override
    public void validateCredentials(String email, String password, String rePassword, String firstname, String lastname, String location) {
        if (registerView != null){
            registerView.showProgress();
        }

        // TODO: Må forbedre validering med email regex, password policy, osv.
        if (TextUtils.isEmpty(email)){
            registerView.setEmailError("The email is empty");
        }
        else if (TextUtils.isEmpty(password)){
            registerView.setPasswordError("The password is empty");
        }
        else if (!TextUtils.equals(password, rePassword)){
            registerView.setPasswordError("The passwords don't match");
        }
        else if (TextUtils.isEmpty(firstname)){
            registerView.setFirstnameError("The firstname is empty");
        }
        else if (TextUtils.isEmpty(lastname)){
            registerView.setLastnameError("The lastname is empty");
        }
        else if (TextUtils.isEmpty(location)){
            registerView.setLocationError("The location is empty");
        }
        else{
            new RegisterTask(email, password, firstname, lastname, location, Constants.BACKEND_URL, this).execute();
        }
    }

    @Override
    public void onRegisterError(String error) {
        registerView.registrationFailed(error);
    }

    @Override
    public void onRegisterSuccess(String email, String password) {
        registerView.navigateToWelcomeActivity(email, password);
    }
}
