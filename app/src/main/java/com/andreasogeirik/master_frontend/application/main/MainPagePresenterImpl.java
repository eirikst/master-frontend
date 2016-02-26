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
public class MainPagePresenterImpl extends GeneralPresenter implements EventPresenter,
        ImageInteractor.OnImageFoundListener {
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
            //presenter.findUser();
            view.navigateToLogin();
            return;
        }

        initDomain();
        view.initGUI();
    }

    @Override
    public void saveInstanceState(Bundle instanceState) {
        //Todo:add state???
    }

    private void initDomain() {
        //TODO:tenke på hvordan dette gjøres vs saveinstancestate: presenter lages jo på nytt etter ny instance, altså lastes dette fra nett
        findFriendships();
        findAttendingEvents();
    }

    @Override
    public void findAttendingEvents() {
        interactor.findAttendingEvents();
    }

    @Override
    public void successAttendingEvents(Set<Event> events) {
        System.out.println("Attending events:");
        Iterator it = events.iterator();
        while(it.hasNext()) {
            System.out.println(it.next());
        }

        this.attendingEvents = events;
        view.setAttendingEvents(events);
    }

    @Override
    public void errorAttendingEvents(int code) {
        view.displayMessage("Lasting av aktiviteter feilet");
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
        //TODO:do something
    }

    @Override
    public void findUser() {
        interactor.findUser();
    }

    @Override
    public void findUserSuccess(User user) {
        //TODO:do something
    }

    @Override
    public void findUserFailure(int code) {
        //TODO:do something
    }

    @Override
    public void findImage(String imageUri) {
        ImageInteractor.getInstance().findImage(imageUri, getActivity().getExternalFilesDir
                (Environment.DIRECTORY_PICTURES), this);
    }

    @Override
    public void foundImage(String imageUri, Bitmap bitmap) {
        view.setAttendingImage(imageUri, bitmap);
    }

    @Override
    public void onProgressChange(int percent) {
        //do nothing
    }

    @Override
    public void imageNotFound(String imageUri) {
        //do nothing, default image is set
    }
}