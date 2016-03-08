package com.andreasogeirik.master_frontend.application.auth.register;

import com.andreasogeirik.master_frontend.application.auth.register.interfaces.RegisterInteractor;
import com.andreasogeirik.master_frontend.application.auth.register.interfaces.RegisterPresenter;
import com.andreasogeirik.master_frontend.communication.GetEventTask;
import com.andreasogeirik.master_frontend.communication.RegisterTask;
import com.andreasogeirik.master_frontend.communication.UploadImageTask;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.listener.OnImageUploadFinishedListener;
import com.andreasogeirik.master_frontend.listener.OnRegisterFinishedListener;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Andreas on 05.02.2016.
 */
public class RegisterInteractorImpl implements RegisterInteractor, OnRegisterFinishedListener, OnImageUploadFinishedListener {
    private RegisterPresenter presenter;
    private User user;

    public RegisterInteractorImpl(RegisterPresenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void registerUser(User user, byte[] byteImage) {
        this.user = user;

        if (byteImage != null) {
            // Execute image upload
            new UploadImageTask(byteImage, this).execute();
        } else {
            // No image selected, create event without image
            new RegisterTask(userToJson(user), this).execute();
        }
    }

    @Override
    public void onRegisterSuccess(JSONObject user) {
        try {
            CurrentUser.getInstance().setUser(new User(user));
        }
        catch (JSONException e) {
            presenter.registerError(Constants.JSON_PARSE_ERROR);
        }
        presenter.registerSuccess();
    }

    @Override
    public void onRegisterError(int error) {
        presenter.registerError(error);
    }

    private JSONObject userToJson(User user) {

        JSONObject jsonUser = new JSONObject();
        try {
            jsonUser.put("email", user.getEmail());
            jsonUser.put("password", user.getPassword());
            jsonUser.put("firstname", user.getFirstname());
            jsonUser.put("lastname", user.getLastname());
            jsonUser.put("location", user.getLocation());
            if (user.getImageUri() != null) {
                jsonUser.put("imageUri", user.getImageUri());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonUser;
    }

    @Override
    public void onImageUploadSuccess(String imageUrl) {
        System.out.println("JADA");
        user.setImageUri(imageUrl);
        new RegisterTask(userToJson(user), this).execute();
    }

    @Override
    public void onImageUploadError(int error) {
        System.out.println("FEIL");
        presenter.registerError(error);
    }
}
