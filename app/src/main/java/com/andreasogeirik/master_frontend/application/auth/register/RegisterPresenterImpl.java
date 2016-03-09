package com.andreasogeirik.master_frontend.application.auth.register;

import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;

import com.andreasogeirik.master_frontend.application.auth.register.interfaces.RegisterInteractor;
import com.andreasogeirik.master_frontend.application.auth.register.interfaces.RegisterPresenter;
import com.andreasogeirik.master_frontend.application.auth.register.interfaces.RegisterView;
import com.andreasogeirik.master_frontend.util.image.ImageStatusCode;
import com.andreasogeirik.master_frontend.util.image.SampleImageTask;
import com.andreasogeirik.master_frontend.listener.OnSampleImageFinishedListener;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;


/**
 * Created by Andreas on 05.02.2016.
 */
public class RegisterPresenterImpl implements RegisterPresenter, OnSampleImageFinishedListener {

    private RegisterView registerView;
    private RegisterInteractor interactor;

    public RegisterPresenterImpl(RegisterView registerView) {
        this.registerView = registerView;
        this.interactor = new RegisterInteractorImpl(this);
    }


    @Override
    public void registerUser(String email, String password, String rePassword, String firstname, String lastname, String location, byte[] byteImage) {
        // TODO: Må forbedre validering med email regex, password policy, osv.
        if (TextUtils.isEmpty(email)){
            registerView.setEmailError("The email is empty");
        }
        else if (TextUtils.isEmpty(password)){
            registerView.setPasswordError("The password is empty");
        }
        else if (password.length() < 3){
            registerView.setPasswordError("The password needs to be at least 3 characters long");
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
            registerView.showProgress();
            interactor.registerUser(new User(email, password, firstname, lastname, location), byteImage);
        }
    }

    @Override
    public void sampleImage(InputStream inputStream) {
//        registerView.showProgress();
        new SampleImageTask(this, inputStream, true).execute();
    }

    @Override
    public void registerSuccess() {
        registerView.navigateToWelcomeView();
    }

    @Override
    public void registerError(int error) {
        registerView.hideProgress();

        if(error == Constants.CLIENT_ERROR) {
            registerView.registrationFailed("E-posten er allerede registrert i systemet. " +
                    "Vennligst logg inn på denne kontoen.");
        }
        else if(error == Constants.RESOURCE_ACCESS_ERROR) {
            registerView.registrationFailed("Fant ikke ressurs. Prøv igjen.");
        }
    }

    @Override
    public void onSampleSuccess(Bitmap bitmap, byte[] byteImage) {
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, false);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] scaledByteImage = stream.toByteArray();

        registerView.setImage(scaledByteImage, scaledBitmap);
    }

    @Override
    public void onSampleError(ImageStatusCode statusCode) {

    }
}
