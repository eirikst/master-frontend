package com.andreasogeirik.master_frontend.application.user.edit;

import android.app.Activity;

import com.andreasogeirik.master_frontend.application.general.GeneralPresenter;
import com.andreasogeirik.master_frontend.application.user.edit.interfaces.EditUserInteractor;
import com.andreasogeirik.master_frontend.application.user.edit.interfaces.EditUserPresenter;
import com.andreasogeirik.master_frontend.application.user.edit.interfaces.EditUserView;
import com.andreasogeirik.master_frontend.application.user.profile.ProfileInteractorImpl;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;

/**
 * Created by Andreas on 07.04.2016.
 */
public class EditUserPresenterImpl extends GeneralPresenter implements EditUserPresenter {

    EditUserView view;
    private EditUserInteractor interactor;
    private User user;

    public EditUserPresenterImpl(EditUserView view) {
        super((Activity)view, CHECK_USER_AVAILABLE);
        this.user = CurrentUser.getInstance().getUser();
        if(user == null) {
            throw new NullPointerException("User object cannot be null in " + this.toString());
        }
        if(view == null) {
            throw new NullPointerException("View cannot be null in " + this.toString());
        }
        this.view = view;
        this.interactor = new EditUserInteractorImpl(this);
    }


    @Override
    public void setUserAttributes() {
        this.view.setUserAttributes(user.getFirstname(), user.getLastname(), user.getLocation());
    }

    @Override
    public void updateUser(String firstname, String lastname, String location) {
        User user = new User();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setLocation(location);
        user.setImageUri(this.user.getImageUri());
        this.interactor.updateUser(user);
    }

    @Override
    public void updateSuccess() {
        this.view.naviagetToProfileView();
    }

    @Override
    public void updateError(int error) {
        switch (error) {
            case Constants.RESOURCE_ACCESS_ERROR:
                this.view.displayUpdateError("Kunne ikke oppdatere bruker, ingen kontakt med server");
                break;
            case Constants.UNAUTHORIZED:
                checkAuth();
                break;
            case Constants.CLIENT_ERROR:
                this.view.displayUpdateError("Noe gikk galt, prøv igjen senere");
                break;
            case Constants.SOME_ERROR:
                this.view.displayUpdateError("Noe gikk galt, prøv igjen senere");
        }
    }
}
