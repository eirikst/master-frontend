package com.andreasogeirik.master_frontend.application.user.friend;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.user.friend.interfaces.FriendListPresenter;
import com.andreasogeirik.master_frontend.application.user.friend.interfaces.FriendListView;
import com.andreasogeirik.master_frontend.application.user.my_profile.MyProfileActivity;
import com.andreasogeirik.master_frontend.application.user.profile_not_friend.ProfileOthersActivity;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.layout.adapter.FriendListAdapter;
import com.andreasogeirik.master_frontend.model.Friendship;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FriendListActivity extends AppCompatActivity implements FriendListView,
        AdapterView.OnItemClickListener,
        FriendListAdapter.Listener
    {
        private FriendListPresenter presenter;

    @Bind(R.id.friends_list)ListView listView;

    private FriendListAdapter listAdapter;
    private List<Friendship> friends = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_list_activity);
        ButterKnife.bind(this);

        presenter = new FriendListPresenterImpl(this);

        if(savedInstanceState != null) {
            System.out.println("Saved instance state restored");
            friends.addAll((List<Friendship>) savedInstanceState.getSerializable("friends"));
        }
        else {
            Intent intent = getIntent();
            friends.addAll((HashSet<Friendship>) intent.getSerializableExtra("friends"));
        }
        initListView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("friends", (ArrayList) friends);
    }

    private void initListView() {
        listView.setOnItemClickListener(this);

        //set adapter on listview
        listAdapter = new FriendListAdapter(this, friends, this);
        listView.setAdapter(listAdapter);
    }

    /*
     * Handle click on friend list
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //if friend or one self
        if (CurrentUser.getInstance().getUser().isFriendWith(friends.get(position).getFriend()) ||
                CurrentUser.getInstance().getUser().equals(friends.get(position).getFriend())) {
            Intent intent = new Intent(FriendListActivity.this, MyProfileActivity.class);
            intent.putExtra("user", friends.get(position).getFriend());//this yo bro
            startActivity(intent);
        }
        //or if not friend
        else {
            Intent intent = new Intent(FriendListActivity.this, ProfileOthersActivity.class);
            intent.putExtra("user", friends.get(position).getFriend());//this yo bro
            startActivity(intent);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /*
     * Profile image handling
     */

    @Override
    public void findImage(String imageUri) {
        presenter.findImage(imageUri, getExternalFilesDir(Environment.DIRECTORY_PICTURES));
    }

    @Override
    public void setProfileImage(String imageUri, Bitmap bitmap) {
        listAdapter.addImage(imageUri, bitmap);
        this.bitmap = bitmap;

        Button b = (Button)findViewById(R.id.b);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView i = (ImageView)findViewById(R.id.brohim);
                i.setImageBitmap(FriendListActivity.this.bitmap);
            }
        });
    }

    @Override
    public void findProfileImageFailure(String imageUri) {
        listAdapter.addImage(imageUri, null);//null means default image
    }

        Bitmap bitmap = null;
}