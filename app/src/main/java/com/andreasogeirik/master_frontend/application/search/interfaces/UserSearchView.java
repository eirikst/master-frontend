package com.andreasogeirik.master_frontend.application.search.interfaces;

import android.graphics.Bitmap;

import com.andreasogeirik.master_frontend.model.User;

import java.util.List;

/**
 * Created by eirikstadheim on 07/03/16.
 */
public interface UserSearchView {
    void setupView(List<User> users);
    void setUsers(List<User> users);
    void setImage(String imageUri, Bitmap image);
    void displayMessage(String message);
    void showLoadMore(boolean show);
}
