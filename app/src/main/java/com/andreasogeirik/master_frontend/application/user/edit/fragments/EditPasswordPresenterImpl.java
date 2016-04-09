package com.andreasogeirik.master_frontend.application.user.edit.fragments;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.general.GeneralPresenter;
import com.andreasogeirik.master_frontend.application.user.edit.fragments.interfaces.EditPasswordInteractor;
import com.andreasogeirik.master_frontend.application.user.edit.fragments.interfaces.EditPasswordPresenter;
import com.andreasogeirik.master_frontend.application.user.edit.fragments.interfaces.EditPasswordView;
import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.validation.user.password.UpdatePasswordStatusCodeContainer;
import com.andreasogeirik.master_frontend.util.validation.user.password.UpdatePasswordStatusCodes;
import com.andreasogeirik.master_frontend.util.validation.user.password.UpdatePasswordValidator;

/**
 * Created by Andreas on 08.04.2016.
 */
public class EditPasswordPresenterImpl extends GeneralPresenter implements EditPasswordPresenter {

    EditPasswordView view;
    EditPasswordInteractor interactor;

    public EditPasswordPresenterImpl(EditPasswordView view) {
        super(((Fragment)view).getActivity(), CHECK_USER_AVAILABLE);
        this.view = view;
        this.interactor = new EditPasswordInteractorImpl(this);
    }

    @Override
    public void updatePassword(String currentPassword, String newPassword, String rePassword) {
        UpdatePasswordStatusCodeContainer statusCodeContainer = UpdatePasswordValidator.validate(currentPassword, newPassword, rePassword);
        if (statusCodeContainer.getCode() != UpdatePasswordStatusCodes.OK){
            String message = statusCodeContainer.getMessage();
            switch (statusCodeContainer.getCode()) {
                case CURRENT_PASS_ERROR:
                    this.view.currentPasswordError(message);
                    break;
                case NEW_PASS_ERROR:
                    this.view.newPasswordError(message);
                    break;
                case RE_PASS_ERROR:
                    this.view.rePasswordError(message);
                    break;
            }
        }
        else{
            this.interactor.updatePassword(currentPassword, newPassword);
        }
    }

    @Override
    public void updateSuccess() {
        view.navigateBack();
    }

    @Override
    public void updateError(int error) {
        switch (error) {
            case Constants.RESOURCE_ACCESS_ERROR:
                this.view.displayErrors(getActivity().getResources().getString(R.string.resource_access_error));
                break;
            case Constants.CLIENT_ERROR:
                this.view.displayErrors(getActivity().getResources().getString(R.string.user_edit_client_error));
                break;
            case Constants.UNAUTHORIZED:
                checkAuth();
                break;
            case Constants.SOME_ERROR:
                this.view.displayErrors(getActivity().getResources().getString(R.string.some_error));
                break;
        }
    }
}
