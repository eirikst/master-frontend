package com.andreasogeirik.master_frontend.application.user.profile.interfaces;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.andreasogeirik.master_frontend.model.Friendship;
import com.andreasogeirik.master_frontend.model.UserPost;

import java.util.Set;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public interface ProfilePresenter {
    void findPosts();

    void successPostsLoad(Set<UserPost> posts);
    void errorPostsLoad(int code);

    void successFriendsLoad(Set<Friendship> friends);
    void errorFriendsLoad(int code);

    void imageFound(String imageUrl, Bitmap bitmap);
    void imageNotFound(String imageUri);

    void friendListSelected();

    void saveInstanceState(Bundle bundle);
}