package com.andreasogeirik.master_frontend.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.event.main.EventActivity;
import com.andreasogeirik.master_frontend.application.main.MainPageActivity;
import com.andreasogeirik.master_frontend.communication.GetFriendsTask;
import com.andreasogeirik.master_frontend.communication.GetMeTask;
import com.andreasogeirik.master_frontend.communication.GetMyFriendsTask;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.listener.OnFinishedLoadingFriendshipsListener;
import com.andreasogeirik.master_frontend.model.Friendship;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by eirikstadheim on 06/04/16.
 */
public class GcmListenerService extends com.google.android.gms.gcm.GcmListenerService {
    private final String TAG = getClass().getSimpleName();

    public static final String DEFAULT_ACTION = "default";
    public static final String EVENT_ACTION = "event";

    private boolean userLoaded = false;
    private boolean friendsLoaded = false;
    private int count = 0;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("msg");
        String action = data.getString("action");
        String actionIdString = data.getString("actionId");
        System.out.println(message);
        System.out.println(action);
        System.out.println(actionIdString);

        if(action.equals(DEFAULT_ACTION)) {
            notifyDefault(message);
        }
        else if(action.equals(EVENT_ACTION)) {
            try {
                int actionId = Integer.parseInt(data.getString("actionId"));
                if(actionId != 0) {
                    notifyEvent(message, actionId);
                    Log.i(getClass().getSimpleName(), "Notifying event");
                }
                else {
                    Log.w(getClass().getSimpleName(), "actionId 0");
                }
            }
            catch(NumberFormatException e) {
                Log.w(getClass().getSimpleName(), "Error parsing actionId from gcm.");
            }
        }
    }

    private void notifyEvent(final String msg, final int eventId) {
        if(CurrentUser.getInstance().isInitialized()) {
            notifyEventCallback(msg, eventId);
        }
        else {
            new GetMeTask(new GetMeTask.OnFinishedLoadingMeListener() {
                @Override
                public void onLoadingMeSuccess(JSONObject jsonUser) {
                    count++;
                    try {
                        CurrentUser.getInstance().setUser(new User(jsonUser));
                        userLoaded = true;
                        count++;

                        if(count%2 == 0 && friendsLoaded) {
                            notifyEventCallback(msg, eventId);
                        }
                    }
                    catch(JSONException e) {
                        Log.w(TAG, "JSON error: " + e);
                        userLoaded = false;
                        friendsLoaded = false;
                    }
                }

                @Override
                public void onLoadingMeFailure(int code) {
                    count++;
                    userLoaded = false;
                    friendsLoaded = false;
                }
            }).execute();

            new GetMyFriendsTask(new OnFinishedLoadingFriendshipsListener() {
                @Override
                public void onSuccessFriendshipsLoad(JSONArray friendshipsJson) {
                    count++;
                    Set<Friendship> friendships = new HashSet<>();
                    try {
                        for(int i = 0; i < friendshipsJson.length(); i++) {
                            friendships.add(new Friendship(friendshipsJson.getJSONObject(i)));
                        }
                        CurrentUser.getInstance().getUser().setFriends(friendships);

                        friendsLoaded = true;
                        count++;

                        if(count%2 == 0 && userLoaded) {
                            notifyEventCallback(msg, eventId);
                        }
                    }
                    catch (JSONException e) {
                        Log.w(TAG, "JSON error: " + e);
                        userLoaded = false;
                        friendsLoaded = false;
                    }
                }

                @Override
                public void onFailedFriendshipsLoad(int code) {
                    count++;
                    userLoaded = false;
                    friendsLoaded = false;
                }
            }).execute();
        }
    }

    void notifyEventCallback(String msg, int eventId) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.icon)
                        .setContentTitle(getResources().getString(R.string.app_name))
                        .setContentText(msg)
                        .setDefaults(Notification.DEFAULT_ALL) // requires VIBRATE permission
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg));
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, EventActivity.class);
        resultIntent.putExtra("eventId", eventId);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainPageActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.

        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(new Random().nextInt(), notification);
    }


    void notifyDefault(String msg) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.icon)
                        .setContentTitle(getResources().getString(R.string.app_name))
                        .setContentText(msg)
                        .setDefaults(Notification.DEFAULT_ALL) // requires VIBRATE permission
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg));
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainPageActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainPageActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.

        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(new Random().nextInt(), notification);
    }
}
