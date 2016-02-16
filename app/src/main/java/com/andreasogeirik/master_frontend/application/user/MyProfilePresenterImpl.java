package com.andreasogeirik.master_frontend.application.user;

import com.andreasogeirik.master_frontend.application.user.interfaces.MyProfileInteractor;
import com.andreasogeirik.master_frontend.communication.GetPostsTask;
import com.andreasogeirik.master_frontend.listener.OnFinishedLoadingPostsListener;
import com.andreasogeirik.master_frontend.model.Post;
import com.andreasogeirik.master_frontend.application.user.interfaces.MyProfilePresenter;
import com.andreasogeirik.master_frontend.application.user.interfaces.MyProfileView;
import com.andreasogeirik.master_frontend.model.User;

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


    @Override
    public void findPosts(int start) {
        interactor.findPosts(start);
    }

    @Override
    public void findFriends() {
        interactor.findFriends();
    }

    @Override
    public void successPostsLoad(List<Post> posts) {
        view.addPosts(posts);
    }

    @Override
    public void errorPostsLoad(int code) {
        //handle this
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