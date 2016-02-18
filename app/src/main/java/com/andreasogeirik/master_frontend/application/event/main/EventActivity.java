package com.andreasogeirik.master_frontend.application.event.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.andreasogeirik.master_frontend.application.auth.entrance.EntranceActivity;
import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventPresenter;
import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventView;
import com.andreasogeirik.master_frontend.application.user.my_profile.MyProfileActivity;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.application.event.create.CreateEventActivity;
import com.andreasogeirik.master_frontend.layout.CustomSwipeRefreshLayout;
import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.model.Friendship;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.LogoutHandler;
import com.andreasogeirik.master_frontend.util.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EventActivity extends AppCompatActivity implements EventView, CustomSwipeRefreshLayout.OnRefreshListener {
    private EventPresenter presenter;

    @Bind(R.id.swipe_container)
    CustomSwipeRefreshLayout swipeContainer;

    @Bind(R.id.listView)
    ListView listView;

    @Bind(R.id.empty_view)
    TextView emptyView;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Her må current user være initialisert fra før...

        SessionManager sessionManager = SessionManager.getInstance();
        sessionManager.initialize(this);

        presenter = new EventPresenterImpl(this);

        if (sessionManager.getCookie() == null){
            startLoginActivity();
        }
        else {
            presenter.findUser();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_activity);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        this.swipeContainer.setOnRefreshListener(this);
        this.swipeContainer.setListView(this.listView);
        this.listView.setEmptyView(this.emptyView);


        Button b = new Button(this);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventActivity.this, MyProfileActivity.class);
                intent.putExtra("user", CurrentUser.getInstance().getUser());
                EventActivity.this.startActivity(intent);
            }
        });
        listView.addHeaderView(b);

        loadDummyEvents();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case R.id.sign_out:
                LogoutHandler.getInstance().logOut();
                i = new Intent(this, EntranceActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                this.startActivity(i);
                return true;
            case R.id.create_event:
                i = new Intent(this, CreateEventActivity.class);
                this.startActivity(i);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public void loadDummyEvents(){
        List<Map<String, String>> eventList = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            Map<String, String> event = new HashMap<>();
            event.put("Event", "Event " + Integer.toString(i));
            eventList.add(event);
        }
        SimpleAdapter simpleAdpt = new SimpleAdapter(this, eventList, android.R.layout.
                simple_list_item_1, new String[] {"Event"}, new int[] {android.R.id.text1});
        this.listView.setEmptyView(this.emptyView);
        this.listView.setAdapter(simpleAdpt);
        this.swipeContainer.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        deleteItems();
    }

    public void deleteItems(){
        this.listView.setAdapter(new SimpleAdapter(this, new ArrayList<Map<String, ?>>(), android.R.
                layout.simple_list_item_1, new String[]{"Event"}, new int[]{android.R.id.text1}));
        swipeContainer.setRefreshing(false);
    }

    @Override
    public void addFriendships(Set<Friendship> friendships, Set<Friendship> requests) {
        CurrentUser.getInstance().getUser().setFriends(friendships);
        CurrentUser.getInstance().getUser().setRequests(requests);

        Iterator<Friendship> it = friendships.iterator();
        while(it.hasNext()) {
            System.out.println("fs" + it.next());
        }

        it = requests.iterator();
        while(it.hasNext()) {
            System.out.println("rq " + it.next());
        }
    }


    @Override
    public void findUserSuccess(User user) {
        CurrentUser.getInstance().setUser(user);
        presenter.findFriendships();//find friends
        // else go on
    }

    @Override
    public void findUserFailure(int code) {
        startLoginActivity();
    }

    private void startLoginActivity() {
        Intent i = new Intent(this, EntranceActivity.class);
        startActivity(i);
        finish();
    }
}