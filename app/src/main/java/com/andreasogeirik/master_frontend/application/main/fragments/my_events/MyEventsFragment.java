package com.andreasogeirik.master_frontend.application.main.fragments.my_events;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.event.main.EventActivity;
import com.andreasogeirik.master_frontend.application.main.fragments.my_events.interfaces.MyEventView;
import com.andreasogeirik.master_frontend.application.main.fragments.my_events.interfaces.MyEventsPresenter;
import com.andreasogeirik.master_frontend.layout.adapter.EventListAdapter;
import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.model.User;

import java.util.Set;


/**
 * Created by eirikstadheim on 12/02/16.
 */
public class MyEventsFragment extends Fragment implements EventListAdapter.Listener,
        MyEventView {
    public interface MyEventsListener {
        }

    private MyEventsListener callback;//the activity that created the fragment
    private MyEventsPresenter presenter;
    private ListView listView;
    private EventListAdapter listAdapter;
    private Button footer;


    /*
     * Creates a new instance of the fragment
     */
    public static MyEventsFragment newInstance() {
        MyEventsFragment f = new MyEventsFragment();
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
            callback = (MyEventsListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement MyEventsListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listAdapter = new EventListAdapter(getActivity().getApplicationContext(), this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        presenter = new MyEventsPresenterImpl(this);
    }

    /*
         * Inflates the layout, set adapter
         */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.my_events_fragment, container, false);
        listView = (ListView)view.findViewById(R.id.event_list);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event event = listAdapter.getItem(position);
                System.out.println("Trykker på event: " + event);
                Intent intent = new Intent(getActivity(), EventActivity.class);
                intent.putExtra("event", event);
                startActivity(intent);
            }
        });

        footer = (Button)inflater.inflate(R.layout.my_events_list_footer, container, false);
        listView.addFooterView(footer);

        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.findMyPastEvents();
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
        if(getActivity() != null) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void findImage(String imageUri) {
        presenter.findImage(imageUri);
    }

    @Override
    public void setMyEvents(Set<Event> events) {
        listAdapter.clear();
        listAdapter.addAll(events);
    }

    @Override
    public void setEventImage(String imageUri, Bitmap bitmap) {
        listAdapter.addImage(imageUri, bitmap);
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
