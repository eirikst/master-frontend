package com.andreasogeirik.master_frontend.application.user.friend;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;

import com.andreasogeirik.master_frontend.application.event.main.EventActivity;
import com.andreasogeirik.master_frontend.application.general.interactors.GeneralPresenter;
import com.andreasogeirik.master_frontend.application.user.friend.interfaces.FriendListPresenter;
import com.andreasogeirik.master_frontend.application.user.friend.interfaces.FriendListView;
import com.andreasogeirik.master_frontend.application.user.my_profile.MyProfileActivity;
import com.andreasogeirik.master_frontend.application.user.profile_not_friend.ProfileOthersActivity;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.model.Friendship;
import com.andreasogeirik.master_frontend.util.ImageInteractor;
import com.andreasogeirik.master_frontend.util.UserPreferencesManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by eirikstadheim on 20/02/16.
 */
public class FriendListPresenterImpl extends GeneralPresenter implements FriendListPresenter,
        ImageInteractor.OnImageFoundListener {
    private FriendListView view;

    //model
    List<Friendship> friendships;

    public FriendListPresenterImpl(FriendListView view, List<Friendship> friendships) {
        super((Activity)view);
        if (friendships == null) {
            throw new NullPointerException("Friendship list cannot be null in " + this.toString());
        }
        if (view == null) {
            throw new NullPointerException("View cannot be null in " + this.toString());
        }

        this.view = view;
        this.friendships = friendships;

        //check that current user singleton is set, if not redirection
        userAvailable();

        view.initGUI(this.friendships);
    }

    @Override
    public void findImage(String imageUri) {
        if (imageUri == null || imageUri.equals("")) {
            return;//do nothing and default image is still there
        }
        ImageInteractor.getInstance().findImage(imageUri, getActivity().getExternalFilesDir(Environment.
                DIRECTORY_PICTURES), this);
    }

    @Override
    public void foundImage(String imageUri, Bitmap bitmap) {
        view.setProfileImage(imageUri, bitmap);
    }

    @Override
    public void onProgressChange(int percent) {
        //Not implemented, doesn't need to
    }

    @Override
    public void imageNotFound(String imageUri) {
        //do nothing, default image is set
    }

    /*
     * Called by the view when a profile is chosen from the list of friends
     */
    @Override
    public void profileChosen(int position) {
        //if friend or one self
        if (CurrentUser.getInstance().getUser().isFriendWith(friendships.get(position).getFriend()) ||
                CurrentUser.getInstance().getUser().equals(friendships.get(position).getFriend())) {
            Intent intent = new Intent(getActivity(), MyProfileActivity.class);
            intent.putExtra("user", friendships.get(position).getFriend());
            getActivity().startActivity(intent);
        }
        //or if not friend
        else {
            Intent intent = new Intent(getActivity(), ProfileOthersActivity.class);
            intent.putExtra("user", friendships.get(position).getFriend());
            getActivity().startActivity(intent);
        }
    }

    /*
     * Save instance
     */
    @Override
    public void saveInstanceState(Bundle outState) {
        outState.putSerializable("friends", (ArrayList) friendships);
    }
}
