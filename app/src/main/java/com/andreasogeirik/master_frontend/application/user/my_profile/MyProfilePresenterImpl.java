package com.andreasogeirik.master_frontend.application.user.my_profile;

import com.andreasogeirik.master_frontend.application.user.my_profile.interfaces.MyProfileInteractor;
import com.andreasogeirik.master_frontend.model.Post;
import com.andreasogeirik.master_frontend.application.user.my_profile.interfaces.MyProfilePresenter;
import com.andreasogeirik.master_frontend.application.user.my_profile.interfaces.MyProfileView;
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
    public void findPosts(User user, int start) {
        interactor.findPosts(user, start);
    }

    @Override
    public void findFriends(int userId) {
        interactor.findFriends(userId);
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