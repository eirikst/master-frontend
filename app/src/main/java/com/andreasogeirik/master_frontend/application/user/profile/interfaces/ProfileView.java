package com.andreasogeirik.master_frontend.application.user.profile.interfaces;

import com.andreasogeirik.master_frontend.model.Comment;
import com.andreasogeirik.master_frontend.model.Post;
import com.andreasogeirik.master_frontend.model.User;

import java.util.Set;

/**
 * Created by eirikstadheim on 05/02/16.
 */
public interface ProfileView {
    void initUser(User user, boolean me);
    void initView();

    void addPosts(Set<Post> posts);
    void noPostsToShow();

    void setFriendCount(int count);
    void setProfileImage(String imageUri);

    void updatePostLike(int id, boolean like);
    void updateCommentLike(int id, boolean like);

    void displayLoadPostsButton(boolean display);

    void addComment(Post post, Comment comment);
    void commentFinished();
    void commentFinishedWithError();

    void displayMessage(String message);
    void setEventButtonText(String text);

    void postFinishedSuccessfully();
    void postFinishedWithError();

}