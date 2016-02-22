package com.andreasogeirik.master_frontend.application.user.my_profile;

import android.graphics.Bitmap;

import com.andreasogeirik.master_frontend.application.user.my_profile.interfaces.MyProfileInteractor;
import com.andreasogeirik.master_frontend.model.Friendship;
import com.andreasogeirik.master_frontend.model.Post;
import com.andreasogeirik.master_frontend.application.user.my_profile.interfaces.MyProfilePresenter;
import com.andreasogeirik.master_frontend.application.user.my_profile.interfaces.MyProfileView;
import com.andreasogeirik.master_frontend.model.User;

import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public class MyProfilePresenterImpl implements MyProfilePresenter {
    private MyProfileView view;
    private MyProfileInteractor interactor;

    public MyProfilePresenterImpl(MyProfileView view) {
        this.view = view;
        this.interactor = new MyProfileInteractorImpl(this);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /*
     * Handling list of posts
     */
    @Override
    public void findPosts(User user, int start) {
        interactor.findPosts(user, start);
    }

    @Override
    public void successPostsLoad(List<Post> posts) {
        view.addPosts(posts);
    }

    @Override
    public void errorPostsLoad(int code) {
        //TODO:handle this
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    /*
     * Handling list of friends
     */
    @Override
    public void findFriends(int userId) {
        interactor.findFriends(userId);
    }

    @Override
    public void successFriendsLoad(Set<Friendship> friends) {
        view.addFriends(friends);
    }

    @Override
    public void errorFriendsLoad(int code) {
        //TODO:handle this
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    /*
     * Image handling
     */

    @Override
    public void findImage(String imageUri, File storagePath) {
        interactor.findImage(imageUri, storagePath);
    }

    @Override
    public void imageFound(String imageUrl, Bitmap result) {
        view.setProfileImage(result);
    }

    @Override
    public void imageNotFound() {
        view.findProfileImageFailure();
    }
}