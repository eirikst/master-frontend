package com.andreasogeirik.master_frontend.listener;

import android.util.Pair;

/**
 * Created by Andreas on 24.02.2016.
 */
public interface OnTimeSetListener {
    void onTimeSet(Pair<Integer, Integer> hourMinutePair, Boolean isStartTime);
}
