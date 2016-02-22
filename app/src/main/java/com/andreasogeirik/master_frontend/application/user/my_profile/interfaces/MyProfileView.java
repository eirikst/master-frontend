package com.andreasogeirik.master_frontend.application.user.my_profile.interfaces;

import com.andreasogeirik.master_frontend.model.Friendship;
import com.andreasogeirik.master_frontend.model.Post;
import com.andreasogeirik.master_frontend.model.User;

import java.util.List;
import java.util.Set;

/**
 * Created by eirikstadheim on 05/02/16.
 */
public interface MyProfileView {
    void addPosts(List<Post> posts);
    void addFriends(Set<Friendship> friends);
}