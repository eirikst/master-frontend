package com.andreasogeirik.master_frontend.application.event.main;

import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventInteractor;
import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventPresenter;
import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventView;
import com.andreasogeirik.master_frontend.model.Friendship;
import com.andreasogeirik.master_frontend.model.User;

import java.util.Set;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public class EventPresenterImpl implements EventPresenter {
    private EventView view;
    private EventInteractor interactor;

    public EventPresenterImpl(EventView view) {
        this.view = view;
        this.interactor = new EventInteractorImpl(this);
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