package com.andreasogeirik.master_frontend.application.user.friend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.andreasogeirik.master_frontend.application.general.GeneralPresenter;
import com.andreasogeirik.master_frontend.application.user.friend.interfaces.FriendListPresenter;
import com.andreasogeirik.master_frontend.application.user.friend.interfaces.FriendListView;
import com.andreasogeirik.master_frontend.application.user.profile.ProfileActivity;
import com.andreasogeirik.master_frontend.application.user.profile_others.ProfileOthersActivity;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.model.Friendship;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eirikstadheim on 20/02/16.
 */
public class FriendListPresenterImpl extends GeneralPresenter implements FriendListPresenter {
    private FriendListView view;

    //model
    List<Friendship> friendships;

    public FriendListPresenterImpl(FriendListView view, List<Friendship> friendships) {
        super((Activity)view, CHECK_USER_AVAILABLE);
        if (friendships == null) {
            throw new NullPointerException("Friendship list cannot be null in " + this.toString());
        }
        if (view == null) {
            throw new NullPointerException("View cannot be null in " + this.toString());
        }

        this.view = view;
        this.friendships = friendships;

        view.initGUI(this.friendships);
    }

    /*
     * Called by the view when a profile is chosen from the list of friends
     */
    @Override
    public void profileChosen(int position) {
        //if friend or one self
        if (CurrentUser.getInstance().getUser().isFriendWith(friendships.get(position).getFriend()) ||
                CurrentUser.getInstance().getUser().equals(friendships.get(position).getFriend())) {
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
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
