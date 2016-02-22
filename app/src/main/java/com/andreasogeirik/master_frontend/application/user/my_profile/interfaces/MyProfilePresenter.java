package com.andreasogeirik.master_frontend.application.user.my_profile.interfaces;

import android.graphics.Bitmap;

import com.andreasogeirik.master_frontend.model.Friendship;
import com.andreasogeirik.master_frontend.model.Post;
import com.andreasogeirik.master_frontend.model.User;

import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public interface MyProfilePresenter {
    void findPosts(User user, int start);
    void findFriends(int userId);

    void successPostsLoad(List<Post> posts);
    void errorPostsLoad(int code);

    void successFriendsLoad(Set<Friendship> friends);
    void errorFriendsLoad(int code);

    void findImage(String imageUri, File storagePath);
    void imageFound(String imageUrl, Bitmap bitmap);
    void imageNotFound();
}