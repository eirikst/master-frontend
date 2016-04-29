package com.andreasogeirik.master_frontend.application.user.edit;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.general.GeneralPresenter;
import com.andreasogeirik.master_frontend.application.user.edit.interfaces.EditUserInteractor;
import com.andreasogeirik.master_frontend.application.user.edit.interfaces.EditUserPresenter;
import com.andreasogeirik.master_frontend.application.user.edit.interfaces.EditUserView;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.image.ImageStatusCode;
import com.andreasogeirik.master_frontend.util.validation.user.details.UpdateUserStatusCodeContainer;
import com.andreasogeirik.master_frontend.util.validation.user.details.UpdateUserStatusCodes;
import com.andreasogeirik.master_frontend.util.validation.user.details.UpdateUserValidator;

import java.io.InputStream;

/**
 * Created by Andreas on 07.04.2016.
 */
public class EditUserPresenterImpl extends GeneralPresenter implements EditUserPresenter {

    EditUserView view;
    private EditUserInteractor interactor;
    private User user;

    public EditUserPresenterImpl(EditUserView view) {
        super((Activity) view, CHECK_USER_AVAILABLE);
        this.user = CurrentUser.getInstance().getUser();
        if (user == null) {
            throw new NullPointerException("User object cannot be null in " + this.toString());
        }
        if (view == null) {
            throw new NullPointerException("View cannot be null in " + this.toString());
        }
        this.view = view;
        this.interactor = new EditUserInteractorImpl(this);
    }


    @Override
    public void setUserAttributes() {
        this.view.setUserAttributes(user.getFirstname(), user.getLastname(), user.getLocation(), user.getImageUri());
    }

    @Override
    public void updateUser(String firstname, String lastname, String location) {
        UpdateUserStatusCodeContainer statusCodeContainer = UpdateUserValidator.validate(firstname, lastname, location);
        if (statusCodeContainer.getCode() != UpdateUserStatusCodes.OK) {
            String message = statusCodeContainer.getMessage();
            switch (statusCodeContainer.getCode()) {
                case FIRST_NAME_ERROR:
                    this.view.firstnameError(message);
                    break;
                case LAST_NAME_ERROR:
                    this.view.lastnameError(message);
                    break;
                case LOCATION_ERROR:
                    this.view.locationError(message);
                    break;
            }

        } else {
            this.view.showProgress();
            this.user.setFirstname(firstname);
            this.user.setLastname(lastname);
            this.user.setLocation(location);
            this.interactor.updateUser(user);
        }
    }

    @Override
    public void updateSuccess() {
        this.view.hideProgress();
        this.view.naviagteToProfileView(user.getId());
    }

    @Override
    public void updateError(int error) {
        this.view.hideProgress();
        switch (error) {
            case Constants.RESOURCE_ACCESS_ERROR:
                this.view.displayUpdateError(getActivity().getResources().getString(R.string.resource_access_error));
                break;
            case Constants.UNAUTHORIZED:
                checkAuth();
                break;
            // Dette skal ikke skje..
            case Constants.CLIENT_ERROR:
                this.view.displayUpdateError(getActivity().getResources().getString(R.string.some_error));
                break;
            case Constants.SOME_ERROR:
                this.view.displayUpdateError(getActivity().getResources().getString(R.string.some_error));
                break;
        }
    }

    @Override
    public void sampleImage(InputStream inputStream) {
        this.interactor.sampleImage(inputStream);
    }

    @Override
    public void sampleSuccess(Bitmap image) {
        this.view.updateImage(image);
    }

    @Override
    public void sampleError(ImageStatusCode statusCode) {
        switch (statusCode) {
            case NOT_AN_IMAGE:
                view.imageError("Den valgte filen var ikke et bilde");
                break;
            case FILE_NOT_FOUND:
                view.imageError("Fant ikke den valgte filen");
                break;
        }
    }

    @Override
    public void uploadImageError(int error) {
        this.view.hideProgress();
        switch (error) {
            case Constants.RESOURCE_ACCESS_ERROR:
                this.view.displayUpdateError(getActivity().getResources().getString(R.string.resource_access_error));
                break;
            case Constants.UNAUTHORIZED:
                checkAuth();
                break;
            // Dette skal ikke skje..
            case Constants.CLIENT_ERROR:
                this.view.displayUpdateError(getActivity().getResources().getString(R.string.some_error));
                break;
            case Constants.SOME_ERROR:
                this.view.displayUpdateError(getActivity().getResources().getString(R.string.some_error));
                break;
        }
    }
}
