package com.andreasogeirik.master_frontend.application.main.fragments.feed;

import android.content.Context;
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
import com.andreasogeirik.master_frontend.application.main.fragments.feed.interfaces.LogPresenter;
import com.andreasogeirik.master_frontend.application.main.fragments.feed.interfaces.LogView;
import com.andreasogeirik.master_frontend.layout.adapter.LogListAdapter;
import com.andreasogeirik.master_frontend.model.LogElement;

import java.util.Set;


/**
 * Created by eirikstadheim on 12/02/16.
 */
public class LogFragment extends Fragment implements LogView {
    public interface LogFragmentListener {

    }

    private LogFragmentListener callback;//the activity that created the fragment
    private LogPresenter presenter;
    private ListView listView;
    private Button footer;
    private LogListAdapter listAdapter;


    /*
     * Creates a new instance of the fragment
     */
    public static LogFragment newInstance() {
        LogFragment f = new LogFragment();
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
            callback = (LogFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement AttendingEventsListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listAdapter = new LogListAdapter(getActivity().getApplicationContext());

        presenter = new LogPresenterImpl(this);
    }

    /*
     * Inflates the layout, set adapter
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.log_fragment, container, false);
        listView = (ListView)view.findViewById(R.id.log_list);
        listView.setAdapter(listAdapter);

        footer = (Button)inflater.inflate(R.layout.attending_events_list_footer, listView, false);
        listView.addFooterView(footer);

        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter != null) {
                    presenter.findLog();
                }
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogElement element = listAdapter.getItem(position);
                presenter.elementChosen(element);
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.activityVisible(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.activityVisible(false);
    }

    @Override
    public void displayMessage(String message) {
        if(getActivity() != null) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setLog(Set<LogElement> log) {
        listAdapter.clear();
        listAdapter.addAll(log);
    }

    @Override
    public void noMoreLog() {
        if(footer != null) {
            footer.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateListView() {
        listAdapter.notifyDataSetChanged();
    }
}
