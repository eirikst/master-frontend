package com.andreasogeirik.master_frontend.application.user.profile_not_friend;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;

import com.andreasogeirik.master_frontend.application.general.GeneralPresenter;
import com.andreasogeirik.master_frontend.application.user.profile.ProfileActivity;
import com.andreasogeirik.master_frontend.application.user.profile_not_friend.interfaces.ProfileOthersInteractor;
import com.andreasogeirik.master_frontend.application.user.profile_not_friend.interfaces.ProfileOthersPresenter;
import com.andreasogeirik.master_frontend.application.user.profile_not_friend.interfaces.ProfileOthersView;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.model.Friendship;
import com.andreasogeirik.master_frontend.model.User;

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
        super((Activity)view);
        if(user == null) {
            throw new NullPointerException("User object cannot be null in " + this.toString());
        }
        if(view == null) {
            throw new NullPointerException("View cannot be null in " + this.toString());
        }

        this.view = view;
        interactor = new ProfileOthersInteractorImpl(this);
        this.user = user;

        //check that current user singleton is set, if not redirection
        userAvailable();

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

        findImage();
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

        view.displayMessage("Venneforespørsel sendt");
        view.setIHaveRequestedView();
    }

    @Override
    public void friendRequestFailure(int code) {
        view.displayMessage("En feil skjedde under forespørsel");
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

        view.displayMessage("Venneforespørsel akseptert");

        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        intent.putExtra("user", friendship.getFriend());
        getActivity().startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void acceptRequestFailure(int code) {
        view.displayMessage("En feil skjedde under akseptering");
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

        view.displayMessage("Vennskap fjernet");
        view.setRequestFriendButton();

    }

    @Override
    public void rejectRequestFailure(int code) {
        view.displayMessage("En feil skjedde under fjerning av forespørsel");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /*
     * Image handling
     */

    private void findImage() {
        if(user.getImageUri() == null || user.getImageUri().isEmpty()) {
            System.out.println("User's image uri null or empty for user " + user.getId());
            return;
        }
        interactor.findImage(user.getImageUri(), getActivity().getExternalFilesDir
                (Environment.DIRECTORY_PICTURES));
    }

    @Override
    public void imageFound(String imageUrl, Bitmap result) {
        view.setProfileImage(result);
    }

    @Override
    public void imageNotFound() {
        //Do nothing and default image is still there
    }

    /*
     * Save state
     */
    @Override
    public void saveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable("user", user);
    }
}