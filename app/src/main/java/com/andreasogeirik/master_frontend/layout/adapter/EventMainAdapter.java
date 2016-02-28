package com.andreasogeirik.master_frontend.layout.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.model.EventPost;

import java.util.List;


/**
 * Created by Andreas on 26.02.2016.
 */
public class EventMainAdapter extends ArrayAdapter<EventPost> {

    private List<EventPost> posts;

    public EventMainAdapter(Context context, List<EventPost> posts) {
        super(context, 0);
        this.posts = posts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_post_list_layout, parent, false);
        }

        return convertView;

    }
}
