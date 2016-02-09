package com.andreasogeirik.master_frontend.application.user.interfaces;

import com.andreasogeirik.master_frontend.model.Post;

import java.util.List;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public interface MyProfilePresenter {
    void findPosts(int start);
    void successPostsLoad(List<Post> posts);
    void errorPostsLoad(String msg);
}
