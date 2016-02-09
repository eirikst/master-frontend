package com.andreasogeirik.master_frontend.user;

import com.andreasogeirik.master_frontend.model.Post;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.user.interfaces.MyProfilePresenter;
import com.andreasogeirik.master_frontend.user.interfaces.MyProfileView;

import java.util.List;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public class MyProfilePresenterImpl implements MyProfilePresenter, OnFinishedLoadingPostsListener {
    private MyProfileView view;

    public MyProfilePresenterImpl(MyProfileView view) {
        this.view = view;
    }


    @Override
    public void findPosts(int start) {
        System.out.println("presenter inn");
        new GetPostsTask(this).execute();
    }

    @Override
    public void onSuccessPostsLoad(List<Post> posts) {
        System.out.println("SUCCESS:");

        for(int i = 0; i < posts.size(); i++) {
            System.out.println(posts.get(i).toString());
        }
        view.addPosts(posts);
    }

    @Override
    public void onFailedPostsLoad(String error) {
        System.out.println("ERROR:");
        System.out.println(error);
    }
}
