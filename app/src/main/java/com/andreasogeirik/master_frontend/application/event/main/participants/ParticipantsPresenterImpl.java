package com.andreasogeirik.master_frontend.application.event.main.participants;

import android.app.Activity;
import android.content.Intent;

import com.andreasogeirik.master_frontend.application.event.main.participants.interfaces.ParticipantsPresenter;
import com.andreasogeirik.master_frontend.application.event.main.participants.interfaces.ParticipantsView;
import com.andreasogeirik.master_frontend.application.general.GeneralPresenter;
import com.andreasogeirik.master_frontend.application.user.profile.ProfileActivity;
import com.andreasogeirik.master_frontend.application.user.profile_others.ProfileOthersActivity;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Andreas on 11.03.2016.
 */
public class ParticipantsPresenterImpl extends GeneralPresenter implements ParticipantsPresenter {

    private List<User> users = new ArrayList<>();
    private ParticipantsView participantsView;

    public ParticipantsPresenterImpl(ParticipantsView participantsView, HashSet<User> users) {
        super((Activity) participantsView, CHECK_USER_AVAILABLE);
        this.participantsView = participantsView;
        this.users.addAll(users);
    }

    @Override
    public void initGui() {
        participantsView.initGui(this.users);
    }


    @Override
    public void profileChosen(int position) {
        //if friend or one self
        if (CurrentUser.getInstance().getUser().isFriendWith(users.get(position)) ||
                CurrentUser.getInstance().getUser().equals(users.get(position))) {
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            intent.putExtra("user", users.get(position).getId());
            getActivity().startActivity(intent);
        }
        //or if not friend
        else {
            Intent intent = new Intent(getActivity(), ProfileOthersActivity.class);
            intent.putExtra("user", users.get(position));
            getActivity().startActivity(intent);
        }

    }
}
