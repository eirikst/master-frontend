package com.andreasogeirik.master_frontend.application.user.edit.fragments;

import com.andreasogeirik.master_frontend.application.user.edit.fragments.interfaces.EditPasswordInteractor;
import com.andreasogeirik.master_frontend.application.user.edit.fragments.interfaces.EditPasswordPresenter;
import com.andreasogeirik.master_frontend.communication.UpdatePasswordTask;
import com.andreasogeirik.master_frontend.listener.OnUpdatePasswordFinishedListener;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Created by Andreas on 08.04.2016.
 */
public class EditPasswordInteractorImpl implements EditPasswordInteractor, OnUpdatePasswordFinishedListener {

    EditPasswordPresenter presenter;

    public EditPasswordInteractorImpl(EditPasswordPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void updatePassword(String currentPass, String newPass) {
        MultiValueMap<String, String> passwords = new LinkedMultiValueMap<>();
        passwords.add("currentPassword", currentPass);
        passwords.add("newPassword", newPass);
        new UpdatePasswordTask(this, passwords).execute();
    }

    @Override
    public void onUpdateSuccess() {
        this.presenter.updateSuccess();
    }

    @Override
    public void onUpdateError(int error) {
        this.presenter.updateError(error);
    }
}
