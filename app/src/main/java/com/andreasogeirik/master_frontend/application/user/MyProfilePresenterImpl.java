package com.andreasogeirik.master_frontend.application.user;

import com.andreasogeirik.master_frontend.application.user.interfaces.MyProfileInteractor;
import com.andreasogeirik.master_frontend.communication.GetPostsTask;
import com.andreasogeirik.master_frontend.listener.OnFinishedLoadingPostsListener;
import com.andreasogeirik.master_frontend.model.Post;
import com.andreasogeirik.master_frontend.application.user.interfaces.MyProfilePresenter;
import com.andreasogeirik.master_frontend.application.user.interfaces.MyProfileView;

import java.util.List;

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
        interactor.findPosts(0);
    }

    @Override
    public void successPostsLoad(List<Post> posts) {
        view.addPosts(posts);
    }

    @Override
    public void errorPostsLoad(String msg) {
        //handle this
    }
}