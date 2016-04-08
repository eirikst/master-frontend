package com.andreasogeirik.master_frontend.application.search;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.andreasogeirik.master_frontend.application.general.GeneralPresenter;
import com.andreasogeirik.master_frontend.application.search.interfaces.UserSearchInteractor;
import com.andreasogeirik.master_frontend.application.search.interfaces.UserSearchPresenter;
import com.andreasogeirik.master_frontend.application.search.interfaces.UserSearchView;
import com.andreasogeirik.master_frontend.application.user.profile.ProfileActivity;
import com.andreasogeirik.master_frontend.application.user.profile_others.ProfileOthersActivity;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UserSearchPresenterImpl extends GeneralPresenter implements UserSearchPresenter {
    private UserSearchView view;
    private List<User> users;
    private UserSearchInteractor interactor;
    private String lastSearch = "";

    public UserSearchPresenterImpl(UserSearchView view, List<User> users) {
        super((Activity)view, CHECK_USER_AVAILABLE);
        this.view = view;
        this.interactor = new UserSearchInteractorImpl(this);
        if(users != null) {
            this.users = users;
        }
        else {
            this.users = new ArrayList<>();
        }

        //check that current user singleton is set, if not redirection
        userAvailable();

        view.setupView(this.users);

        if(users != null) {
            if (users.size() < Constants.NUMBER_OF_USERS_RETURNED_SEARCH) {
                view.showLoadMore(false);
            } else {
                view.showLoadMore(true);
            }
        }
        else {
            view.showLoadMore(false);
        }
    }

    /*
     * Only first search, use loadMoreUsers to get more users
     */
    @Override
    public void searchForUser(String searchString) {
        lastSearch = searchString;
        interactor.searchForUsers(searchString, 0);
    }

    @Override
    public void loadMoreUsers() {
        interactor.searchForUsers(lastSearch, users.size());
    }

    @Override
    public void onSuccessUserSearch(Set<User> users, int offset) {
        if(offset != 0) {
            this.users.addAll(users);
        }
        else {
            this.users = new ArrayList<>(users);
        }

        if(users.size() < Constants.NUMBER_OF_USERS_RETURNED_SEARCH) {
            view.showLoadMore(false);
        }
        else {
            view.showLoadMore(true);
        }

        view.setUsers(this.users);
    }

    @Override
    public void onFailureUserSearch(int code) {
        if(code == Constants.UNAUTHORIZED) {
            checkAuth();
        }
        else {
            view.displayMessage("Vennligst prøv igjen.");
        }
    }

    @Override
    public void saveInstanceState(Bundle state) {
        state.putSerializable("users", (ArrayList)users);
    }

    /*
     * Called by the view when a profile is chosen from the list of friends
     */
    @Override
    public void profileChosen(User user) {
        //if friend or one self
        if (CurrentUser.getInstance().getUser().isFriendWith(user) ||
                CurrentUser.getInstance().getUser().equals(user)) {
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            intent.putExtra("user", user.getId());
            getActivity().startActivity(intent);
        }
        //or if not friend
        else {
            Intent intent = new Intent(getActivity(), ProfileOthersActivity.class);
            intent.putExtra("user", user);
            getActivity().startActivity(intent);
        }
    }
}
