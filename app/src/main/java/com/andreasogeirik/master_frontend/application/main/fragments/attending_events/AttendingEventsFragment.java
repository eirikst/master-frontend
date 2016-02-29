package com.andreasogeirik.master_frontend.application.main.fragments.attending_events;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.main.fragments.attending_events.interfaces.AttendingEventView;
import com.andreasogeirik.master_frontend.application.main.fragments.attending_events.interfaces.AttendingEventsPresenter;
import com.andreasogeirik.master_frontend.layout.adapter.EventListAdapter;
import com.andreasogeirik.master_frontend.model.Event;

import java.util.Set;


/**
 * Created by eirikstadheim on 12/02/16.
 */
public class AttendingEventsFragment extends Fragment implements EventListAdapter.Listener,
        AttendingEventView {
    public interface AttendingEventsListener {
        }

    private AttendingEventsListener callback;//the activity that created the fragment
    private AttendingEventsPresenter presenter;
    private ListView listView;
    private Button footer;
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

        listAdapter = new EventListAdapter(getActivity().getApplicationContext(), this);
        presenter = new AttendingEventsPresenterImpl(this);
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

        footer = (Button)inflater.inflate(R.layout.attending_events_list_footer, container, false);
        listView.addFooterView(footer);

        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.findAttendedEvents();
            }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //save nothing, should get a fresh set of events
    }

    @Override
    public void displayMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void findImage(String imageUri) {
        presenter.findImage(imageUri);
    }

    @Override
    public void setAttendingEvents(Set<Event> events) {
        listAdapter.clear();
        listAdapter.addAll(events);
    }

    @Override
    public void setAttendingImage(String imageUri, Bitmap bitmap) {
        listAdapter.addImage(imageUri, bitmap);
    }

    @Override
    public void setNoMoreEventsToLoad() {
        footer.setVisibility(View.GONE);
    }
}