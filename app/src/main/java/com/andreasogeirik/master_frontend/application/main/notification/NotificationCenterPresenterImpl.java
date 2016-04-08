package com.andreasogeirik.master_frontend.application.main.notification;

import android.support.v4.app.Fragment;

import com.andreasogeirik.master_frontend.application.general.GeneralPresenter;
import com.andreasogeirik.master_frontend.application.main.notification.interfaces.NotificationCenterInteractor;
import com.andreasogeirik.master_frontend.application.main.notification.interfaces.NotificationCenterPresenter;
import com.andreasogeirik.master_frontend.application.main.notification.interfaces.NotificationCenterView;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.model.Friendship;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by eirikstadheim on 26/02/16.
 */
public class NotificationCenterPresenterImpl extends GeneralPresenter implements NotificationCenterPresenter {
    private NotificationCenterView view;
    private NotificationCenterInteractor interactor;
    private Set<Object> notifications = new HashSet<>();



    public NotificationCenterPresenterImpl(NotificationCenterView view, Set<Object> notifications) {
        super(((Fragment) view).getActivity(), NO_CHECK);
        this.view = view;
        this.notifications = notifications;
        this.interactor = new NotificationCenterInteractorImpl(this);
    }

    @Override
    public void acceptFriendship(int friendshipId) {
        interactor.acceptFriendship(friendshipId);
    }

    @Override
    public void failureAcceptFriendship(int code) {
        view.displayMessage("En feil skjedde. Vennligst prøv igjen");
    }

    @Override
    public void successAcceptFriendship(int friendshipId) {
        for(Object obj: notifications) {
            if(obj instanceof Friendship) {
                if(friendshipId == ((Friendship)obj).getId()) {
                    notifications.remove(obj);
                    break;
                }
            }
        }

        CurrentUser.getInstance().getUser().goFromRequestToFriend(friendshipId);
        view.setNotifications(notifications);
    }

    @Override
    public void rejectFriendship(int friendshipId) {
        interactor.rejectFriendship(friendshipId);
    }

    @Override
    public void successRejectFriendship(int friendshipId) {
        for(Object obj: notifications) {
            if(obj instanceof Friendship) {
                if(friendshipId == ((Friendship)obj).getId()) {
                    notifications.remove(obj);
                    break;
                }
            }
        }

        CurrentUser.getInstance().getUser().removeFriendship(friendshipId);
        view.setNotifications(notifications);
    }

    @Override
    public void failureRejectFriendship(int code) {
        view.displayMessage("En feil skjedde. Vennligst prøv igjen");
    }
}