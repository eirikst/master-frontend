package com.andreasogeirik.master_frontend.application.main.fragments.feed;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.andreasogeirik.master_frontend.application.event.main.EventActivity;
import com.andreasogeirik.master_frontend.application.general.GeneralPresenter;
import com.andreasogeirik.master_frontend.application.main.fragments.feed.interfaces.LogInteractor;
import com.andreasogeirik.master_frontend.application.main.fragments.feed.interfaces.LogPresenter;
import com.andreasogeirik.master_frontend.application.main.fragments.feed.interfaces.LogView;
import com.andreasogeirik.master_frontend.application.user.profile.ProfileActivity;
import com.andreasogeirik.master_frontend.application.user.profile_others.ProfileOthersActivity;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.model.ContentType;
import com.andreasogeirik.master_frontend.model.LogElement;
import com.andreasogeirik.master_frontend.util.Constants;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by eirikstadheim on 26/02/16.
 */
public class LogPresenterImpl extends GeneralPresenter implements LogPresenter,
        LogInteractorImpl.LogListener {
    private LogView view;
    private LogInteractor interactor;

    private Set<LogElement> log = new HashSet<>();

    private CountDownTimer timer;


    public LogPresenterImpl(LogView view) {
        super(((Fragment)view).getActivity(), NO_CHECK);
        this.view = view;

        interactor = new LogInteractorImpl(this);

        findLog();
    }

    public void findLog() {
        interactor.findLog(log.size());
    }

    @Override
    public void onLoadingMyLogSuccess(Set<LogElement> log) {
        this.log.addAll(log);

        view.setLog(this.log);
        if (log.size() < Constants.NUMBER_OF_LOG_ELEMENTS_RETURNED) {
            view.noMoreLog();
        }
    }

    @Override
    public void onLoadingMyLogFailure(int code) {
        view.displayMessage("En feil skjedde under lasting av nyheter");
    }

    @Override
    public void elementChosen(LogElement element) {
        if(element.getType() == ContentType.CREATE_EVENT || element.getType() == ContentType.
                MODIFY_EVENT || element.getType() == ContentType.PARTICIPATE_EVENT) {
            startEventActivity(element.getContentId());
        }
        else if(element.getType() == ContentType.POST_EVENT || element.getType() ==
                ContentType.LIKE_EVENT_POST) {
            startEventActivityFocusPost(element.getContentId(), element.getRefId());
        }
        else if(element.getType() == ContentType.COMMENT_EVENT || element.getType() ==
                ContentType.LIKE_EVENT_COMMENT) {
            startEventActivityFocusComment(element.getContentId(), element.getRefId());
        }
        else if(element.getType() == ContentType.FRIENDSHIP || element.getType() ==
                ContentType.USER_REGISTERED) {
            startProfileActivity(element.getContentId());
        }
        else if(element.getType() == ContentType.POST_USER || element.getType() ==
                ContentType.LIKE_USER_POST) {
            startProfileActivityFocusPost(element.getContentId(), element.getRefId());

        }
        else if(element.getType() == ContentType.COMMENT_USER || element.getType() ==
                ContentType.LIKE_USER_COMMENT) {
            startProfileActivityFocusComment(element.getContentId(), element.getRefId());

        }
    }

    private void startEventActivity(int eventId) {
        Intent intent = new Intent(getActivity(), EventActivity.class);
        intent.putExtra("eventId", eventId);
        getActivity().startActivity(intent);
    }

    private void startEventActivityFocusPost(int eventId, int refId) {
        Intent intent = new Intent(getActivity(), EventActivity.class);
        intent.putExtra("eventId", eventId);
        intent.putExtra("post", refId);
        getActivity().startActivity(intent);
    }

    private void startEventActivityFocusComment(int eventId, int refId) {
        Intent intent = new Intent(getActivity(), EventActivity.class);
        intent.putExtra("eventId", eventId);
        intent.putExtra("comment", refId);
        getActivity().startActivity(intent);
    }

    private void startProfileActivity(int userId) {
        if (CurrentUser.getInstance().getUser().isFriendWith(userId) ||
                CurrentUser.getInstance().getUser().getId() == userId) {
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            intent.putExtra("user", userId);
            getActivity().startActivity(intent);
        }
        //or if not friend
        else {
            Intent intent = new Intent(getActivity(), ProfileOthersActivity.class);
            intent.putExtra("userId", userId);
            getActivity().startActivity(intent);
        }
    }

    private void startProfileActivityFocusPost(int userId, int postId) {
        if (CurrentUser.getInstance().getUser().isFriendWith(userId) ||
                CurrentUser.getInstance().getUser().getId() == userId) {
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            intent.putExtra("user", userId);
            intent.putExtra("post", postId);
            getActivity().startActivity(intent);
        }
        //or if not friend
        else {
            Intent intent = new Intent(getActivity(), ProfileOthersActivity.class);
            intent.putExtra("userId", userId);
            getActivity().startActivity(intent);
        }
    }

    private void startProfileActivityFocusComment(int userId, int commentId) {
        if (CurrentUser.getInstance().getUser().isFriendWith(userId) ||
                CurrentUser.getInstance().getUser().getId() == userId) {
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            intent.putExtra("user", userId);
            intent.putExtra("comment", commentId);
            getActivity().startActivity(intent);
        }
        //or if not friend
        else {
            Intent intent = new Intent(getActivity(), ProfileOthersActivity.class);
            intent.putExtra("userId", userId);
            getActivity().startActivity(intent);
        }
    }



    /*
     * Update log each minute
     */
    private void startUpdateLogTimer() {
        timer = new CountDownTimer(5000, 5000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                int id = 0;
                for (LogElement element : log) {
                    if(element.getId() > id) {
                        id = element.getId();
                    }
                }
                Log.w("d", "update");
                interactor.updateLog(id);//0 if empty log, will be the same as call to /me/log with offset 0
                this.start();
            }
        };
        timer.start();
    }

    @Override
    public void onLoadingMyUpdatedLogSuccess(Set<LogElement> log) {
        this.log.addAll(log);
        view.setLog(this.log);
    }

    @Override
    public void onLoadingMyUpdatedLogFailure(int code) {
        view.displayMessage("En feil skjedde under lasting av nyheter");
    }

    @Override
    public void activityVisible(boolean visible) {
        if(timer != null) {
            if (visible) {
                timer.onFinish();
            } else {
                timer.cancel();
            }
        }
        else {
            startUpdateLogTimer();
        }
    }
}