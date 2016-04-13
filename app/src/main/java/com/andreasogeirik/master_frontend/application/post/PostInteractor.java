package com.andreasogeirik.master_frontend.application.post;

import com.andreasogeirik.master_frontend.model.User;

/**
 * Created by eirikstadheim on 13/04/16.
 */
public interface PostInteractor {
    void findPosts(User user, int start);

    void likePost(int postId);
    void likeComment(int commentId);

    void unlikePost(int postId);
    void unlikeComment(int commentId);
}
