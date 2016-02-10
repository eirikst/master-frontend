package com.andreasogeirik.master_frontend.application.user.interfaces;

import com.andreasogeirik.master_frontend.model.Post;
import com.andreasogeirik.master_frontend.model.User;

import java.util.List;
import java.util.Set;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public interface MyProfilePresenter {
    void findPosts(int start);
    void findFriends();
    void successPostsLoad(List<Post> posts);
    void errorPostsLoad(int code);
    void successFriendsLoad(Set<User> friends);
    void errorFriendsLoad(int code);
}
