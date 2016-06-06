package com.andreasogeirik.master_frontend.application.user.profile_others;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.andreasogeirik.master_frontend.application.general.GeneralPresenter;
import com.andreasogeirik.master_frontend.application.user.profile.ProfileActivity;
import com.andreasogeirik.master_frontend.application.user.profile_others.interfaces.ProfileOthersInteractor;
import com.andreasogeirik.master_frontend.application.user.profile_others.interfaces.ProfileOthersPresenter;
import com.andreasogeirik.master_frontend.application.user.profile_others.interfaces.ProfileOthersView;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.model.Friendship;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by eirikstadheim on 17/02/16.
 */
public class ProfileOthersPresenterImpl extends GeneralPresenter implements ProfileOthersPresenter {
    private ProfileOthersView view;
    private ProfileOthersInteractor interactor;

    //model
    private User user;

    public ProfileOthersPresenterImpl(ProfileOthersView view, User user) {
        super((Activity)view, CHECK_USER_AVAILABLE);
        if(user == null) {
            throw new NullPointerException("User object cannot be null in " + this.toString());
        }
        if(view == null) {
            throw new NullPointerException("View cannot be null in " + this.toString());
        }

        this.view = view;
        interactor = new ProfileOthersInteractorImpl(this);
        this.user = user;

        setupView();
    }

    public ProfileOthersPresenterImpl(ProfileOthersView view, int userId) {
        super((Activity)view, CHECK_USER_AVAILABLE);
        this.view = view;
        interactor = new ProfileOthersInteractorImpl(this);

        this.user = new User(userId);

        interactor.findUser(userId);
    }

    @Override
    public void onSuccessUserLoad(User user) {
        this.user.copyUser(user);
        setupView();
    }

    @Override
    public void onFailedUserLoad(int code) {
        if(code == Constants.UNAUTHORIZED) {
            checkAuth();
        }
        else {
            view.displayMessage("Feil ved lasting av bruker");
        }
    }


    private void setupView() {
        //setup gui
        if(CurrentUser.getInstance().getUser().iHaveRequested(user)) {
            view.setupGUI(user, Friendship.I_REQUESTED);
        }
        else if(CurrentUser.getInstance().getUser().iWasRequested(user)) {
            view.setupGUI(user, Friendship.FRIEND_REQUESTED);
        }
        else {
            view.setupGUI(user, -1);
        }

        view.setProfileImage(user.getImageUri());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /*
     * Request friendship methods
     */
    @Override
    public void requestFriendship() {
        interactor.requestFriendship(user);
    }

    @Override
    public void friendRequestSuccess(Friendship friendship) {
        CurrentUser.getInstance().getUser().addRequest(friendship);

        view.displayMessage("Friend request sent");
        view.setIHaveRequestedView();
    }

    @Override
    public void friendRequestFailure(int code) {
        if(code == Constants.UNAUTHORIZED) {
            checkAuth();
        }
        else {
            view.displayMessage("An error occurred when sending the friend request");
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /*
     * Accept friendship methods
     */
    @Override
    public void acceptRequest() {
        Set<Friendship> requests = CurrentUser.getInstance().getUser().getRequests();
        Iterator<Friendship> it = requests.iterator();
        while(it.hasNext()) {
            Friendship f = it.next();
            if(f.getFriend().equals(user)) {
                interactor.acceptFriendship(f.getId());
                return;
            }
        }
    }

    @Override
    public void acceptRequestSuccess(int friendshipId) {
        CurrentUser.getInstance().getUser().goFromRequestToFriend(friendshipId);
        Friendship friendship = CurrentUser.getInstance().getUser().findFriend(friendshipId);

        view.displayMessage("Friend request accepted");

        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        intent.putExtra("user", friendship.getFriend().getId());
        getActivity().startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void acceptRequestFailure(int code) {
        if(code == Constants.UNAUTHORIZED) {
            checkAuth();
        }
        else {
            view.displayMessage("En feil skjedde under akseptering");
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /*
     * Reject friendship methods
     */
    @Override
    public void rejectRequest() {
        Set<Friendship> requests = CurrentUser.getInstance().getUser().getRequests();
        Iterator<Friendship> it = requests.iterator();
        while(it.hasNext()) {
            Friendship f = it.next();
            if(f.getFriend().equals(user)) {
                interactor.rejectFriendship(f.getId());
                return;
            }
        }
    }

    @Override
    public void rejectRequestSuccess(int friendshipId) {
        CurrentUser.getInstance().getUser().removeFriendship(friendshipId);

        view.displayMessage("Friendship removed");
        view.setRequestFriendButton();

    }

    @Override
    public void rejectRequestFailure(int code) {
        if(code == Constants.UNAUTHORIZED) {
            checkAuth();
        }
        else {
            view.displayMessage("En feil skjedde under fjerning av forespørsel");
        }
    }

    /*
     * Save state
     */
    @Override
    public void saveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable("user", user);
    }
}