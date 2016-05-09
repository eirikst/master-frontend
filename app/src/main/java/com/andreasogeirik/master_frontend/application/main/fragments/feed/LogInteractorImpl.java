package com.andreasogeirik.master_frontend.application.main.fragments.feed;

import android.util.Log;

import com.andreasogeirik.master_frontend.application.main.fragments.attending_events.interfaces.AttendingEventsInteractor;
import com.andreasogeirik.master_frontend.application.main.fragments.attending_events.interfaces.AttendingEventsPresenter;
import com.andreasogeirik.master_frontend.application.main.fragments.feed.interfaces.LogInteractor;
import com.andreasogeirik.master_frontend.communication.GetAttendedEventsTask;
import com.andreasogeirik.master_frontend.communication.GetAttendingEventsTask;
import com.andreasogeirik.master_frontend.communication.GetMyLogTask;
import com.andreasogeirik.master_frontend.communication.GetMyUpdatedLogTask;
import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.model.LogElement;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by eirikstadheim on 16/02/16.
 */
public class LogInteractorImpl implements LogInteractor, GetMyLogTask.OnFinishedLoadingMyTaskListener,
GetMyUpdatedLogTask.OnFinishedLoadingMyUpdatedLogListener {
    public interface LogListener {
        void onLoadingMyLogSuccess(Set<LogElement> log);
        void onLoadingMyLogFailure(int code);
        void onLoadingMyUpdatedLogSuccess(Set<LogElement> log);
        void onLoadingMyUpdatedLogFailure(int code);
    }

    private String tag = getClass().getSimpleName();

    private LogListener callback;

    public LogInteractorImpl(LogListener callback) {
        this.callback = callback;
    }

    @Override
    public void findLog(int offset) {
        new GetMyLogTask(this, offset).execute();
    }

    @Override
    public void onLoadingMyLogSuccess(JSONArray logJson) {
        Set<LogElement> log = new HashSet<>();

        for(int i = 0; i < logJson.length(); i++) {
            try {
                log.add(new LogElement(logJson.getJSONObject(i)));
            }
            catch (JSONException e) {
                Log.w(tag, "JSON error: " + e);
                callback.onLoadingMyLogFailure(Constants.JSON_PARSE_ERROR);
            }
        }

        callback.onLoadingMyLogSuccess(log);
    }

    @Override
    public void onLoadingMyLogFailure(int code) {
        callback.onLoadingMyLogFailure(code);
    }

    @Override
    public void updateLog(int lastLogId) {
        new GetMyUpdatedLogTask(this, lastLogId).execute();
    }

    @Override
    public void onLoadingMyUpdatedLogSuccess(JSONArray logJson) {
        Set<LogElement> log = new HashSet<>();

        for(int i = 0; i < logJson.length(); i++) {
            try {
                log.add(new LogElement(logJson.getJSONObject(i)));
            }
            catch (JSONException e) {
                Log.w(tag, "JSON error: " + e);
                callback.onLoadingMyLogFailure(Constants.JSON_PARSE_ERROR);
            }
        }

        callback.onLoadingMyUpdatedLogSuccess(log);
    }

    @Override
    public void onLoadingMyUpdatedLogFailure(int code) {
        callback.onLoadingMyUpdatedLogFailure(code);
    }
}
