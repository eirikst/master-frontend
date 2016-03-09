package com.andreasogeirik.master_frontend.application.search.interfaces;

import android.os.Bundle;

import com.andreasogeirik.master_frontend.model.User;

import java.util.Set;

/**
 * Created by eirikstadheim on 07/03/16.
 */
public interface UserSearchPresenter {
    void searchForUser(String searchString);
    void loadMoreUsers();
    void onSuccessUserSearch(Set<User> users, int offset);
    void onFailureUserSearch(int code);

    void findImage(String imageUri);
    void saveInstanceState(Bundle state);
    void profileChosen(User user);
}
