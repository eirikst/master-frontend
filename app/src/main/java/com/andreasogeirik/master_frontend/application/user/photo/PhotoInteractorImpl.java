package com.andreasogeirik.master_frontend.application.user.photo;

import com.andreasogeirik.master_frontend.application.user.photo.interfaces.PhotoInteractor;
import com.andreasogeirik.master_frontend.application.user.photo.interfaces.PhotoPresenter;
import com.andreasogeirik.master_frontend.communication.UpdateUserTask;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.listener.OnUpdateUserFinishedListener;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Andreas on 09.03.2016.
 */
public class PhotoInteractorImpl implements PhotoInteractor, OnUpdateUserFinishedListener {

    private PhotoPresenter presenter;

    public PhotoInteractorImpl(PhotoPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void updateUser(JSONObject jsonImageUris) {

        String imageUri = "";
        String thumbUri = "";

        try {
            imageUri = jsonImageUris.getString("imageUri");
            thumbUri = jsonImageUris.getString("thumbUri");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        User user = CurrentUser.getInstance().getUser();

        JSONObject jsonUser = new JSONObject();

        try {
            jsonUser.put("firstname", user.getFirstname());
            jsonUser.put("lastname", user.getLastname());
            jsonUser.put("location", user.getLocation());
            jsonUser.put("imageUri", imageUri);
            jsonUser.put("thumbUri", thumbUri);
            new UpdateUserTask(jsonUser, this).execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateUserSuccess(JSONObject user) {
        try {
            CurrentUser.getInstance().setUser(new User(user));
        }
        catch (JSONException e) {
            presenter.userUpdatedError(Constants.JSON_PARSE_ERROR);
        }
        this.presenter.userUpdatedSuccess();
    }

    @Override
    public void onUserUpdateError(int error) {

    }
}
