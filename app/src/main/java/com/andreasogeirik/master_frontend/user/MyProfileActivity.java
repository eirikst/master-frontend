package com.andreasogeirik.master_frontend.user;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.layout.adapter.PostListAdapter;
import com.andreasogeirik.master_frontend.model.Post;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.user.interfaces.MyProfilePresenter;
import com.andreasogeirik.master_frontend.user.interfaces.MyProfileView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MyProfileActivity extends AppCompatActivity implements MyProfileView, AdapterView.OnItemClickListener {
    //@Bind(R.id.post_list)

    MyProfilePresenter presenter;

    User me;
    List<Post> posts = new ArrayList<Post>();
    PostListAdapter postListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ButterKnife.bind(this);
        presenter = new MyProfilePresenterImpl(this);

        setContentView(R.layout.my_profile_activity);

        //find and set header view on listview
        View header = getLayoutInflater().inflate(R.layout.my_profile_post_list_header, null);
        ListView postListView = (ListView)findViewById(R.id.post_list);
        postListView.addHeaderView(header);

        postListView.setOnItemClickListener(this);

        //set adapter on listview
        postListAdapter = new PostListAdapter(MyProfileActivity.this, posts);
        postListView.setAdapter(postListAdapter);

        presenter.findPosts(0);

        System.out.println("activity inn");


/*
        posts.add(new Post());
        posts.add(new Post());
        posts.add(new Post());
        posts.add(new Post());

        posts.get(0).setMessage("Jeg og Adolf var ute og gikk en tur idag. Det var helt " +
                "fantastisk vær i marka, og begge stortrivdes. Kyss og klem alle sammen :)");
        posts.get(0).setCreated(new Date());
        posts.get(1).setMessage("Jeg og Adolf var ute og gikk en tur idag. Det var helt " +
                "fantastisk vær i marka, og begge stortrivdes. Kyss og klem alle sammen :)");
        posts.get(1).setCreated(new Date());
        posts.get(2).setMessage("Jeg og Adolf var ute og gikk en tur idag. Det var helt " +
                "fantastisk vær i marka, og begge stortrivdes. Kyss og klem alle sammen :)");
        posts.get(2).setCreated(new Date());
        posts.get(3).setMessage("Jeg og Adolf var ute og gikk en tur idag. Det var helt " +
                "fantastisk vær i marka, og begge stortrivdes. Kyss og klem alle sammen :)");
        posts.get(3).setCreated(new Date());

*/
    }

    @Override
    public void addPosts(List<Post> posts) {
        this.posts.addAll(posts);
        System.out.println("BRØDRE");
        for(int i = 0; i < posts.size(); i++) {
            System.out.println(posts.get(i).toString());
        }
        postListAdapter.notifyDataSetChanged();
        //oppdatere view bror?
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
