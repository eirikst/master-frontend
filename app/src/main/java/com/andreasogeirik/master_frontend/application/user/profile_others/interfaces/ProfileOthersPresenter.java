package com.andreasogeirik.master_frontend.application.user.profile_others.interfaces;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.andreasogeirik.master_frontend.model.Friendship;

/**
 * Created by eirikstadheim on 17/02/16.
 */
public interface ProfileOthersPresenter {
    void requestFriendship();
    void friendRequestSuccess(Friendship friendship);
    void friendRequestFailure(int code);

    void acceptRequest();
    void acceptRequestSuccess(int friendshipId);
    void acceptRequestFailure(int code);

    void rejectRequest();
    void rejectRequestSuccess(int friendshipId);
    void rejectRequestFailure(int code);

    void saveInstanceState(Bundle savedInstanceState);
}
