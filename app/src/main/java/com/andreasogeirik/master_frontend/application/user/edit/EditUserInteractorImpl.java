package com.andreasogeirik.master_frontend.application.user.edit;

import com.andreasogeirik.master_frontend.application.user.edit.interfaces.EditUserInteractor;
import com.andreasogeirik.master_frontend.application.user.edit.interfaces.EditUserPresenter;
import com.andreasogeirik.master_frontend.communication.UpdateUserTask;
import com.andreasogeirik.master_frontend.listener.OnUpdateUserFinishedListener;
import com.andreasogeirik.master_frontend.model.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Andreas on 07.04.2016.
 */
public class EditUserInteractorImpl implements EditUserInteractor, OnUpdateUserFinishedListener {
    private EditUserPresenter presenter;

    public EditUserInteractorImpl(EditUserPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void updateUser(User user) {
        JSONObject jsonUser = new JSONObject();

        try {
            jsonUser.put("firstname", user.getFirstname());
            jsonUser.put("lastname", user.getLastname());
            jsonUser.put("location", user.getLocation());
            jsonUser.put("imageUri", user.getImageUri());
            new UpdateUserTask(jsonUser, this).execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateSuccess(JSONObject user) {
        this.presenter.updateSuccess();
    }

    @Override
    public void onUpdateError(int error) {
        this.presenter.updateError(error);
    }
}
