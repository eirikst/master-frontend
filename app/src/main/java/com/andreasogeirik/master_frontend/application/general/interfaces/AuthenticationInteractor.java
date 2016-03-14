package com.andreasogeirik.master_frontend.application.general.interfaces;

import com.andreasogeirik.master_frontend.model.User;

/**
 * Created by eirikstadheim on 23/02/16.
 */
public interface AuthenticationInteractor {
    interface AuthenticationListener {
        void findMeSuccess(User user);
        void findMeFailure(int code);
    }

    void checkAuth();
}
