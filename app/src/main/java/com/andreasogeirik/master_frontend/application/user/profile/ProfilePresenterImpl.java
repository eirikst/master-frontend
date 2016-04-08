package com.andreasogeirik.master_frontend.application.user.profile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;

import com.andreasogeirik.master_frontend.application.general.GeneralPresenter;
import com.andreasogeirik.master_frontend.application.user.friend.FriendListActivity;
import com.andreasogeirik.master_frontend.application.user.profile.interfaces.ProfileInteractor;
import com.andreasogeirik.master_frontend.communication.UploadImageTask;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.listener.OnSampleImageFinishedListener;
import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.model.Friendship;
import com.andreasogeirik.master_frontend.model.UserPost;
import com.andreasogeirik.master_frontend.application.user.profile.interfaces.ProfilePresenter;
import com.andreasogeirik.master_frontend.application.user.profile.interfaces.ProfileView;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.image.ImageStatusCode;
import com.andreasogeirik.master_frontend.util.image.SampleImageTask;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public class ProfilePresenterImpl extends GeneralPresenter implements ProfilePresenter {
    private ProfileView view;
    private ProfileInteractor interactor;
    boolean thisIsMyProfile;

    //model
    private User user;

    public ProfilePresenterImpl(ProfileView view, int userId) {
        super((Activity)view, CHECK_USER_AVAILABLE);

        this.user = new User(userId);
        this.view = view;
        this.interactor = new ProfileInteractorImpl(this);

        //init gui
        thisIsMyProfile = CurrentUser.getInstance().getUser().equals(user);
        view.initView();

        //init model
        findUser(userId);
        findPosts();//get first posts
        findFriends(this.user.getId());
        findAttendingEvents();
    }


    /*
     * Get user
     */
    @Override
    public void findUser(int userId) {
        interactor.findUser(userId);
    }

    @Override
    public void onSuccessUserLoad(User user) {
        this.user.copyUser(user);
        view.initUser(user, thisIsMyProfile);
        view.setProfileImage(user.getImageUri());
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

    /*
     * Handling set of posts
     */
    @Override
    public void findPosts() {
        interactor.findPosts(user, user.getPosts().size());
    }

    @Override
    public void successPostsLoad(Set<UserPost> posts) {
        user.addPosts(posts);
        view.addPosts(posts);
    }

    @Override
    public void errorPostsLoad(int code) {
        if(code == Constants.UNAUTHORIZED) {
            checkAuth();
        }
        else {
            view.displayMessage("Feil ved lasting av poster");
        }
    }

    @Override
    public void findAttendingEvents() {
        interactor.findAttendingEvents(user);
    }

    @Override
    public void successAttendingEvents(Set<Event> events) {
        if(events.size() == 0) {
            view.setEventButtonText("Se aktiviteter");
        }
        else if(events.size() == 1) {
            for(Event e: events) {
                view.setEventButtonText("Deltar på " + e.getName());
            }
        }
        else {
            view.setEventButtonText("Deltar på " + events.size() + " aktiviteter");
        }
    }

    @Override
    public void failureAttendingEvents(int code) {
        view.displayMessage("Error loading user's events");
    }

    /*
         * Handling set of friends
         */
    private void findFriends(int userId) {
        interactor.findFriends(userId);
    }

    @Override
    public void successFriendsLoad(Set<Friendship> friends) {
        user.addFriends(friends);
        view.setFriendCount(user.getFriends().size());
    }

    @Override
    public void errorFriendsLoad(int code) {
        if(code == Constants.UNAUTHORIZED) {
            checkAuth();
        }
        else {
            view.displayMessage("Feil ved lasting av venner");
        }
    }

    /*
     * Go to user's friend list view
     */
    @Override
    public void friendListSelected() {
        Intent intent = new Intent(getActivity(), FriendListActivity.class);
        intent.putExtra("friends", new ArrayList<Friendship>(user.getFriends()));
        getActivity().startActivity(intent);
    }

    /*
     * Save instance state
     */
    @Override
    public void saveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable("user", user);
    }

    @Override
    public void accessEvents() {
        Intent intent = new Intent(getActivity(), AttendingEventsActivity.class);
        intent.putExtra("user", user);
        getActivity().startActivity(intent);
    }

    @Override
    public void updateUser(InputStream inputStream) {
        interactor.sampleImage(inputStream);
    }

    @Override
    public void userUpdateSuccess() {
        view.refreshView();
    }

    @Override
    public void userUpdateError(int error) {

    }
}