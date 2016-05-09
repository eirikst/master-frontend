package com.andreasogeirik.master_frontend.application.main.fragments.attending_events;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.event.main.EventActivity;
import com.andreasogeirik.master_frontend.application.main.fragments.attending_events.interfaces.AttendingEventView;
import com.andreasogeirik.master_frontend.application.main.fragments.attending_events.interfaces.AttendingEventsPresenter;
import com.andreasogeirik.master_frontend.layout.adapter.EventListAdapter;
import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.model.User;

import java.util.Set;


/**
 * Created by eirikstadheim on 12/02/16.
 */
public class AttendingEventsFragment extends Fragment implements AttendingEventView {
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
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            callback = (AttendingEventsListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement AttendingEventsListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listAdapter = new EventListAdapter(getActivity().getApplicationContext());
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event event = listAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), EventActivity.class);
                intent.putExtra("event", event);
                startActivity(intent);
            }
        });

        footer = (Button)inflater.inflate(R.layout.attending_events_list_footer, listView, false);
        listView.addFooterView(footer);

        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(presenter != null) {
                    presenter.findAttendedEvents();
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        User user = (User)getActivity().getIntent().getSerializableExtra("user");
        presenter = new AttendingEventsPresenterImpl(this, user);
    }

    @Override
    public void displayMessage(String message) {
        if(getActivity() != null) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setAttendingEvents(Set<Event> events) {
        listAdapter.clear();
        listAdapter.addAll(events);
    }

    @Override
    public void setNoMoreEventsToLoad() {
        if(footer != null) {
            footer.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateListView() {
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void setUser(User user) {
        listAdapter.setUser(user);
    }
}
