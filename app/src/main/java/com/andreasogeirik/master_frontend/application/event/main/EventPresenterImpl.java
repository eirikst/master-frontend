package com.andreasogeirik.master_frontend.application.event.main;

import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventInteractor;
import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventPresenter;
import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventView;
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
    public void findFriends(int userId) {
        interactor.findFriends(userId);
    }

    @Override
    public void successFriendsLoad(Set<User> friends) {
        view.addFriends(friends);
    }

    @Override
    public void errorFriendsLoad(int code) {
        //handle this
    }
}