package com.andreasogeirik.master_frontend.application.user.profile.interfaces;

import android.os.Bundle;

import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.model.Friendship;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.model.Post;

import java.util.Set;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public interface ProfilePresenter {
    void findUser(int userId);
    void onSuccessUserLoad(User user);

    void onFailedUserLoad(int code);

    void findPosts();

    void likePost(int postId);
    void likeComment(int commentId);

    void unlikePost(int postId);
    void unlikeComment(int commentId);


    void successPostsLoad(Set<Post> posts);
    void errorPostsLoad(int code);

    void successFriendsLoad(Set<Friendship> friends);
    void errorFriendsLoad(int code);

    void friendListSelected();

    void saveInstanceState(Bundle bundle);

    void findAttendingEvents();
    void successAttendingEvents(Set<Event> events);
    void failureAttendingEvents(int code);

    void accessEvents();
}