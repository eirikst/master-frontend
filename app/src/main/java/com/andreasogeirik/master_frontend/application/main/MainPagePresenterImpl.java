package com.andreasogeirik.master_frontend.application.main;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;

import com.andreasogeirik.master_frontend.application.main.interfaces.EventInteractor;
import com.andreasogeirik.master_frontend.application.main.interfaces.EventPresenter;
import com.andreasogeirik.master_frontend.application.main.interfaces.EventView;
import com.andreasogeirik.master_frontend.application.general.interactors.GeneralPresenter;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.model.Friendship;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.ImageInteractor;
import com.andreasogeirik.master_frontend.util.UserPreferencesManager;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public class MainPagePresenterImpl extends GeneralPresenter implements EventPresenter {
    private EventView view;
    private EventInteractor interactor;

    //model
    private Set<Event> attendingEvents;

    public MainPagePresenterImpl(EventView view) {
        super((Activity)view);
        this.view = view;
        this.interactor = new MainPageInteractorImpl(this);

        //init text size
        Constants.USER_SET_SIZE = UserPreferencesManager.getInstance().getTextSize();


        //TODO:kan denne legges i generell presenter?
        if (UserPreferencesManager.getInstance().getCookie() == null){
            view.navigateToLogin();
            return;
        }
        if(CurrentUser.getInstance().getUser() == null) {
            //TODO: get user from nettet eller gå til login
            interactor.findUser();
            return;
        }
        else {
            initDomain();
            view.initGUI();
        }
    }

    @Override
    public void saveInstanceState(Bundle instanceState) {
        //Todo:add state???
    }

    private void initDomain() {
        //TODO:tenke på hvordan dette gjøres vs saveinstancestate: presenter lages jo på nytt etter
        // ny instance, altså lastes dette fra nett
        findFriendships();
    }

    @Override
    public void findFriendships() {
        interactor.findFriendships();
    }

    @Override
    public void successFriendshipsLoad(Set<Friendship> friendships, Set<Friendship> requests) {
        CurrentUser.getInstance().getUser().setFriends(friendships);
        CurrentUser.getInstance().getUser().setRequests(requests);
    }

    @Override
    public void errorFriendshipsLoad(int code) {
        //TODO:don't do
        interactor.findUser();
    }

    @Override
    public void findUser() {
        interactor.findUser();
    }

    @Override
    public void findUserSuccess(User user) {
        CurrentUser.getInstance().setUser(user);
        initDomain();
        view.initGUI();
    }

    @Override
    public void findUserFailure(int code) {
        view.navigateToLogin();
    }
}