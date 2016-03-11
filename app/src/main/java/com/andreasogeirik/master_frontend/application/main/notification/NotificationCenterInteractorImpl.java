package com.andreasogeirik.master_frontend.application.main.notification;

import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.app.Fragment;

import com.andreasogeirik.master_frontend.application.general.interactors.GeneralPresenter;
import com.andreasogeirik.master_frontend.application.main.notification.interfaces.NotificationCenterInteractor;
import com.andreasogeirik.master_frontend.application.main.notification.interfaces.NotificationCenterPresenter;
import com.andreasogeirik.master_frontend.application.main.notification.interfaces.NotificationCenterView;
import com.andreasogeirik.master_frontend.communication.AcceptRequestTask;
import com.andreasogeirik.master_frontend.communication.UnFriendTask;
import com.andreasogeirik.master_frontend.listener.OnAcceptRequestListener;
import com.andreasogeirik.master_frontend.listener.OnUnfriendedListener;
import com.andreasogeirik.master_frontend.util.ImageInteractor;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by eirikstadheim on 26/02/16.
 */
public class NotificationCenterInteractorImpl implements NotificationCenterInteractor, OnAcceptRequestListener, OnUnfriendedListener {
    private NotificationCenterPresenter presenter;

    public NotificationCenterInteractorImpl(NotificationCenterPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void acceptFriendship(int friendshipId) {
        new AcceptRequestTask(this, friendshipId).execute();
    }

    @Override
    public void rejectFriendship(int friendshipId) {
        new UnFriendTask(this, friendshipId).execute();
    }

    @Override
    public void onAcceptRequestSuccess(int friendshipId) {
        presenter.successAcceptFriendship(friendshipId);
    }

    @Override
    public void onAcceptRequestFailure(int code) {
        presenter.failureAcceptFriendship(code);
    }

    @Override
    public void onRejectFriendSuccess(int friendshipId) {
        presenter.successRejectFriendship(friendshipId);
    }

    @Override
    public void onRejectFriendFailure(int code) {
        presenter.failureRejectFriendship(code);
    }
}