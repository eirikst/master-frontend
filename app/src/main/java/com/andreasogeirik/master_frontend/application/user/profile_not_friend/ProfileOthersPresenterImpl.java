package com.andreasogeirik.master_frontend.application.user.profile_not_friend;

import com.andreasogeirik.master_frontend.application.user.profile_not_friend.interfaces.ProfileOthersInteractor;
import com.andreasogeirik.master_frontend.application.user.profile_not_friend.interfaces.ProfileOthersPresenter;
import com.andreasogeirik.master_frontend.application.user.profile_not_friend.interfaces.ProfileOthersView;
import com.andreasogeirik.master_frontend.model.Friendship;
import com.andreasogeirik.master_frontend.model.User;

/**
 * Created by eirikstadheim on 17/02/16.
 */
public class ProfileOthersPresenterImpl implements ProfileOthersPresenter {
    private ProfileOthersView view;
    private ProfileOthersInteractor interactor;

    public ProfileOthersPresenterImpl(ProfileOthersView view) {
        this.view = view;
        interactor = new ProfileOthersInteractorImpl(this);
    }

    @Override
    public void requestFriendship(User user) {
        interactor.requestFriendship(user);
    }

    @Override
    public void friendRequestSuccess(Friendship friendship) {
        view.friendRequestSuccess(friendship);
    }

    @Override
    public void friendRequestFailure(int code) {
        view.friendRequestFailure(code);
    }

    @Override
    public void acceptRequest(int friendshipId) {
        interactor.acceptFriendship(friendshipId);
    }

    @Override
    public void acceptRequestSuccess(int friendshipId) {
        view.acceptRequestSuccess(friendshipId);
    }

    @Override
    public void acceptRequestFailure(int code) {
        view.acceptRequestFailure(code);
    }

    @Override
    public void rejectRequest(int friendshipId) {
        interactor.rejectFriendship(friendshipId);
    }

    @Override
    public void rejectRequestSuccess(int friendshipId) {
        view.rejectRequestSuccess(friendshipId);
    }

    @Override
    public void rejectRequestFailure(int code) {
        view.rejectRequestFailure(code);
    }
}
