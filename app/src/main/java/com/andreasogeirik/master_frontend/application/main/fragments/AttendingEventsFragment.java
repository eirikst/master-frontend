package com.andreasogeirik.master_frontend.application.main.fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.layout.adapter.EventListAdapter;
import com.andreasogeirik.master_frontend.model.Event;

import java.util.Set;


/**
 * Created by eirikstadheim on 12/02/16.
 */
public class AttendingEventsFragment extends Fragment implements EventListAdapter.Listener {
    public interface AttendingEventsListener {
        public void findImage(String imageUri);
    }
    private AttendingEventsListener callback;//the activity that created the fragment
    private ListView listView;
    private EventListAdapter listAdapter;


    /*
     * Creates a new instance of the fragment
     */
    public static AttendingEventsFragment newInstance() {
        AttendingEventsFragment f = new AttendingEventsFragment();
        return f;
    }

    /*
     * Checks that the activity that creates the fragment implements the interface for callback
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            callback = (AttendingEventsListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement AttendingEventsListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //TODO:saved instance state bro?
        listAdapter = new EventListAdapter(getActivity().getApplicationContext(), this);

    }

    /*
     * Inflates the layout, set adapter
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.attending_events_fragment, container, false);
        listView = (ListView)view.findViewById(R.id.event_list);
        listView.setAdapter(listAdapter);

        return view;
    }

    /*
     * Updates the event model
     */
    public void setEventsList(Set<Event> events) {
        listAdapter.clear();
        listAdapter.addAll(events);
    }

    public void setImage(String imageUri, Bitmap bitmap) {
        listAdapter.addImage(imageUri, bitmap);
    }

    @Override
    public void findImage(String imageUri) {
        callback.findImage(imageUri);
    }
}
