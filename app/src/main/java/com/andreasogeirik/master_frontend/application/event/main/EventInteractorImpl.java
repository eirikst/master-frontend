package com.andreasogeirik.master_frontend.application.event.main;

import android.util.Log;

import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventInteractor;
import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventPresenter;
import com.andreasogeirik.master_frontend.communication.AttendEventTask;
import com.andreasogeirik.master_frontend.communication.DeleteEventTask;
import com.andreasogeirik.master_frontend.communication.GetEventTask;
import com.andreasogeirik.master_frontend.communication.PostTask;
import com.andreasogeirik.master_frontend.listener.OnAttendEventFinishedListener;
import com.andreasogeirik.master_frontend.listener.OnDeleteEventFinishedListener;
import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.model.Post;
import com.andreasogeirik.master_frontend.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;



/**
 * Created by Andreas on 10.02.2016.
 */
public class EventInteractorImpl implements EventInteractor, OnAttendEventFinishedListener,
        OnDeleteEventFinishedListener, PostTask.OnFinishedPostingListener,
        GetEventTask.OnFinishedLoadingEventListener{
    private String tag = getClass().getSimpleName();

    private EventPresenter presenter;

    public EventInteractorImpl(EventPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void attendEvent(int eventId) {
        new AttendEventTask(eventId, this, true).execute();
    }

    @Override
    public void unAttendEvent(int eventId) {
        new AttendEventTask(eventId, this, false).execute();
    }

    @Override
    public void deleteEvent(int eventId) {
        new DeleteEventTask(eventId, this).execute();
    }

    @Override
    public void post(int eventId, String message) {
        new PostTask(this, message, eventId, PostTask.EVENT).execute();
    }

    @Override
    public void onAttendSuccess(JSONObject jsonEvent) {
        try {
            Event event = new Event(jsonEvent);
            presenter.attendSuccess(event);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttendError(int error) {
        presenter.attendError(error);
    }

    @Override
    public void onDeleteEventSuccess() {
        this.presenter.deleteSuccess();
    }

    @Override
    public void onDeleteEventError(int error) {
        this.presenter.deleteError(error);
    }

    @Override
    public void onSuccessPost(JSONObject jsonPost) {
        try {
            Post post = new Post(jsonPost);
            presenter.postSuccess(post);
        }
        catch(JSONException e) {
            e.printStackTrace();
            presenter.postFailure(Constants.JSON_PARSE_ERROR);
        }
    }

    @Override
    public void onFailurePost(int code) {
        presenter.postFailure(code);
    }

    @Override
    public void findEvent(int id) {
        new GetEventTask(this, id).execute();
    }

    @Override
    public void onLoadingEventSuccess(JSONObject jsonEvent) {
        try {
            Event event = new Event(jsonEvent);
            presenter.eventSuccess(event);
        }
        catch(JSONException e) {
            Log.w(tag, "Failed parsing event");
            e.printStackTrace();
            presenter.eventFailure(Constants.JSON_PARSE_ERROR);
        }
    }

    @Override
    public void onLoadingEventFailure(int code) {
        presenter.eventFailure(code);
    }
}
