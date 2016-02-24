package com.andreasogeirik.master_frontend.application.main;

import android.app.Activity;

import com.andreasogeirik.master_frontend.application.main.interfaces.EventInteractor;
import com.andreasogeirik.master_frontend.application.main.interfaces.EventPresenter;
import com.andreasogeirik.master_frontend.application.main.interfaces.EventView;
import com.andreasogeirik.master_frontend.application.general.interactors.GeneralPresenter;
import com.andreasogeirik.master_frontend.model.Friendship;
import com.andreasogeirik.master_frontend.model.User;

import java.util.Set;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public class MainPagePresenterImpl extends GeneralPresenter implements EventPresenter {
    private EventView view;
    private EventInteractor interactor;

    public MainPagePresenterImpl(EventView view) {
        super((Activity)view);
        this.view = view;
        this.interactor = new MainPageInteractorImpl(this);
    }

    @Override
    public void findFriendships() {
        interactor.findFriendships();
    }

    @Override
    public void successFriendshipsLoad(Set<Friendship> friendships, Set<Friendship> requests) {
        view.addFriendships(friendships, requests);
    }

    @Override
    public void errorFriendshipsLoad(int code) {
        //handle this
    }

    @Override
    public void findUser() {
        interactor.findUser();
    }

    @Override
    public void findUserSuccess(User user) {
        view.findUserSuccess(user);
    }

    @Override
    public void findUserFailure(int code) {
        view.findUserFailure(code);
    }
}