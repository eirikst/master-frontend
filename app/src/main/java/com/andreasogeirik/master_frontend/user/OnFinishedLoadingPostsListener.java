package com.andreasogeirik.master_frontend.user;

import com.andreasogeirik.master_frontend.model.Post;

import java.util.List;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public interface OnFinishedLoadingPostsListener {
    void onSuccessPostsLoad(List<Post> posts);
    void onFailedPostsLoad(String error);
}
