package com.andreasogeirik.master_frontend.listener;

import android.view.View;

import com.andreasogeirik.master_frontend.model.User;

public class OnUserClickListener implements View.OnClickListener {
    public interface Listener {
        void userClicked(User user);
    }

    private Listener listener;
    private User user;

    public OnUserClickListener(Listener listener, User user) {
        this.listener = listener;
        this.user = user;
    }

    @Override
    public void onClick(View v) {
        listener.userClicked(user);
    }
}