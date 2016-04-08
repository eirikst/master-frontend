package com.andreasogeirik.master_frontend.application.main;

import android.app.Activity;
import android.os.Bundle;

import com.andreasogeirik.master_frontend.application.main.interfaces.EventInteractor;
import com.andreasogeirik.master_frontend.application.main.interfaces.EventPresenter;
import com.andreasogeirik.master_frontend.application.main.interfaces.EventView;
import com.andreasogeirik.master_frontend.application.general.GeneralPresenter;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.model.Friendship;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.UserPreferencesManager;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public class MainPagePresenterImpl extends GeneralPresenter implements EventPresenter {
    private EventView view;
    private EventInteractor interactor;

    public MainPagePresenterImpl(EventView view) {
        super((Activity)view, GeneralPresenter.NO_CHECK);
        this.view = view;
        this.interactor = new MainPageInteractorImpl(this);

        //init text size
        Constants.USER_SET_SIZE = UserPreferencesManager.getInstance().getTextSize();
        if(Constants.USER_SET_SIZE < 1) {
            Constants.USER_SET_SIZE = Constants.MEDIUM;
        }

//        // This is called so that we will have no latency waiting for a 401 of the user is not authenticated
//        if(UserPreferencesManager.getInstance().getCookie() == null) {
//            view.navigateToEntrance();
//            return;
//        }
//        findUser();
    }

    @Override
    public void saveInstanceState(Bundle instanceState) {
        //Todo:add state???
    }

    private void initDomain() {
        //TODO:tenke på hvordan dette gjøres vs saveinstancestate: presenter lages jo på nytt etter
        // ny instance, altså lastes dette fra nett
        //findFriendships();
    }

    @Override
    public void findFriendships() {
        interactor.findFriendships();
    }

    @Override
    public void successFriendshipsLoad(Set<Friendship> friendships, Set<Friendship> requests) {
        CurrentUser.getInstance().getUser().setFriends(friendships);
        CurrentUser.getInstance().getUser().setRequests(requests);

        int requestCount = 0;
        for(Friendship friendship: requests) {
            if(friendship.getStatus() == Friendship.FRIEND_REQUESTED) {
                requestCount++;
            }
        }
        if(requestCount != 0) {
            view.setNotificationCount(requestCount);
        }
    }

    @Override
    public void errorFriendshipsLoad(int code) {
        view.navigateToEntrance();
    }

    @Override
    public void findUser() {
//        this.view.showProgress();
        interactor.findUser();
    }

    @Override
    public void findUserSuccess(User user) {
//        this.view.hideProgress();
        CurrentUser.getInstance().setUser(user);
        //initDomain();
        view.initGUI();
    }

    @Override
    public void findUserFailure(int code) {
//        this.view.hideProgress();
        view.navigateToEntrance();
    }

    @Override
    public void accessNotificationCenter() {
        //get all friendships, place them in set of objects
        Set<Object> requests = new HashSet<>();

        Set<Friendship> friendships = CurrentUser.getInstance().getUser().getRequests();
        for(Friendship friendship: friendships) {
            if(friendship.getStatus() == Friendship.FRIEND_REQUESTED) {
                requests.add(friendship);
            }
        }

        view.showNotificationCenter(requests);
    }

    @Override
    public void checkUser() {
        if(UserPreferencesManager.getInstance().getCookie() == null) {
            view.navigateToEntrance();
            return;
        }
        findUser();
    }
}