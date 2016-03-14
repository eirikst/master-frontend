package com.andreasogeirik.master_frontend.application.user.profile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;

import com.andreasogeirik.master_frontend.application.general.GeneralPresenter;
import com.andreasogeirik.master_frontend.application.user.friend.FriendListActivity;
import com.andreasogeirik.master_frontend.application.user.profile.interfaces.ProfileInteractor;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.model.Friendship;
import com.andreasogeirik.master_frontend.model.UserPost;
import com.andreasogeirik.master_frontend.application.user.profile.interfaces.ProfilePresenter;
import com.andreasogeirik.master_frontend.application.user.profile.interfaces.ProfileView;
import com.andreasogeirik.master_frontend.model.User;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public class ProfilePresenterImpl extends GeneralPresenter implements ProfilePresenter {
    private ProfileView view;
    private ProfileInteractor interactor;

    //model
    private User user;


    public ProfilePresenterImpl(ProfileView view, User user) {
        super((Activity)view);
        if(user == null) {
            throw new NullPointerException("User object cannot be null in " + this.toString());
        }
        if(view == null) {
            throw new NullPointerException("View cannot be null in " + this.toString());
        }

        this.view = view;
        this.interactor = new ProfileInteractorImpl(this);
        this.user = user;

        //check that current user singleton is set, if not redirection
        userAvailable();

        //init gui
        boolean thisIsMyProfile = CurrentUser.getInstance().getUser().equals(user);
        view.initView(this.user, thisIsMyProfile);

        //init model
        findPosts();//get first posts
        findFriends(this.user.getId());
        findImage();
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
        //TODO: use code? Don't think so. Eller hvis ikke logget inn, redirect til login
        view.displayMessage("Feil ved lasting av poster");
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
        //TODO: use code? Don't think so. Eller hvis ikke logget inn, redirect til login
        view.displayMessage("Feil ved lasting av venner");
    }

    /*
     * Image handling
     */
    private void findImage() {
        if(user.getImageUri() == null || user.getImageUri().isEmpty()) {
            System.out.println("User's image uri null or empty for user " + user.getId());
            return;
        }
        interactor.findImage(user.getImageUri(), getActivity().getExternalFilesDir(Environment.
                DIRECTORY_PICTURES));
    }

    @Override
    public void imageFound(String imageUrl, Bitmap result) {
        view.setProfileImage(result);
    }

    @Override
    public void imageNotFound(String imageUri) {
        view.setProfileImage(null);//null means set standard image
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
}