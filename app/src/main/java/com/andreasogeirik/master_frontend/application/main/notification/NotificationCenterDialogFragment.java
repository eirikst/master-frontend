package com.andreasogeirik.master_frontend.application.main.notification;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.main.notification.interfaces.NotificationCenterPresenter;
import com.andreasogeirik.master_frontend.application.main.notification.interfaces.NotificationCenterView;
import com.andreasogeirik.master_frontend.layout.adapter.NotificationListAdapter;
import com.andreasogeirik.master_frontend.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class NotificationCenterDialogFragment extends DialogFragment implements NotificationCenterView,
        NotificationListAdapter.Listener {
    private ListView notificationList;
    private NotificationListAdapter adapter;

    private NotificationCenterPresenter presenter;
    private NotificationCenterListener callback;

    public interface NotificationCenterListener {
        void setNotificationCount(int count);
    }

    public static NotificationCenterDialogFragment newInstance(HashSet<Object> objects) {
        NotificationCenterDialogFragment f = new NotificationCenterDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("notifications", objects);
        f.setArguments(bundle);

        return f;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            callback = (NotificationCenterListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NotificationListAdapter.Listener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();

        Dialog dialog = getDialog();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);

        presenter.checkFriendships();//to update if back button is pressed to get into this view
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.notification_dialog_fragment, container, false);

        Dialog dialog = getDialog();
        if(dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }

        Bundle bundle = this.getArguments();
        if(bundle != null) {
            Set<Object> notifications = (Set<Object>)bundle.getSerializable("notifications");

            presenter = new NotificationCenterPresenterImpl(this, notifications);
            adapter = new NotificationListAdapter(getActivity(), new ArrayList<>(notifications), this);
        }
        else {
            presenter = new NotificationCenterPresenterImpl(this, new HashSet());
            adapter = new NotificationListAdapter(getActivity(), new ArrayList(), this);
        }
        //TODO:savedinstancestate og teste om bundle!= null-greiene faktisk fungerer


        notificationList = (ListView)v.findViewById(R.id.notification_list);


        notificationList.setAdapter(adapter);

        return v;
    }

    @Override
    public void acceptFriendship(int friendshipId) {
        presenter.acceptFriendship(friendshipId);
    }

    @Override
    public void rejectFriendship(int friendshipId) {
        presenter.rejectFriendship(friendshipId);
    }

    @Override
    public void displayMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
    }

    @Override
    public void setNotifications(Set<Object> notifications) {
        adapter.setNotifications(notifications);
        callback.setNotificationCount(notifications.size());
    }

    @Override
    public void navigateToUser(User user) {
        presenter.navigateToUser(user);
    }
}