package com.andreasogeirik.master_frontend.application.search;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.search.interfaces.UserSearchPresenter;
import com.andreasogeirik.master_frontend.application.search.interfaces.UserSearchView;
import com.andreasogeirik.master_frontend.application.user.profile.ProfilePresenterImpl;
import com.andreasogeirik.master_frontend.layout.adapter.UserSearchListAdapter;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserSearchActivity extends AppCompatActivity implements UserSearchView {
    @Bind(R.id.user_list)
    ListView userListView;

    private EditText searchString;
    private Button searchBtn;
    private Button loadMoreBtn;

    private UserSearchPresenter presenter;
    private UserSearchListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_search_activity);
        ButterKnife.bind(this);


        //should get a user if not null
        if(savedInstanceState != null) {
            try {
                presenter = new UserSearchPresenterImpl(this,
                        (List<User>)savedInstanceState.getSerializable("users"));
            }
            catch(ClassCastException e) {
                throw new ClassCastException(e + "/nObject in savedInstanceState bundle cannot " +
                        "be cast to User in " + this.toString());
            }
        }
        else {
            presenter = new UserSearchPresenterImpl(this, null);
        }
    }

    @Override
    public void setupView(List<User> users) {
        adapter = new UserSearchListAdapter(this, new ArrayList<>(users));
        userListView.setAdapter(adapter);

        View listHeader = getLayoutInflater().inflate(R.layout.user_search_list_header, null);
        userListView.addHeaderView(listHeader);

        searchString = (EditText)listHeader.findViewById(R.id.user_search_text);
        searchBtn = (Button)listHeader.findViewById(R.id.user_search_btn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.searchForUser(searchString.getText().toString());
            }
        });


        View listFooter = getLayoutInflater().inflate(R.layout.user_search_list_footer, null);
        userListView.addFooterView(listFooter);

        loadMoreBtn = (Button)listFooter.findViewById(R.id.user_search_load_more_btn);
        loadMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.loadMoreUsers();
            }
        });

        searchString.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count > 0 && !(start == 0 && count < 2)) {
                    presenter.searchForUser(searchString.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = adapter.getItem(position - 1);
                presenter.profileChosen(user);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.saveInstanceState(outState);
    }

    @Override
    public void setUsers(List<User> users) {
        adapter.setData(users);
    }

    @Override
    public void displayMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showLoadMore(boolean show) {
        if(show) {
            loadMoreBtn.setVisibility(View.VISIBLE);
        }
        else {
            loadMoreBtn.setVisibility(View.GONE);
        }
    }
}
