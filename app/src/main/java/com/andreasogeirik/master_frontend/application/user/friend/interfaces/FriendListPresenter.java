package com.andreasogeirik.master_frontend.application.user.friend.interfaces;

import android.os.Bundle;

import java.io.File;

/**
 * Created by eirikstadheim on 20/02/16.
 */
public interface FriendListPresenter {
    void profileChosen(int position);
    void saveInstanceState(Bundle outState);
}
