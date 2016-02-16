package com.andreasogeirik.master_frontend.layout.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.model.Post;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.DateUtility;

import java.util.List;

/**
 * Created by eirikstadheim on 05/02/16.
 */
public class FriendListAdapter extends ArrayAdapter<User> {
    private List<User> friends;


    public FriendListAdapter(Context context, List<User> friends) {
        super(context, 0, friends);
        this.friends = friends;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User friend = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.friend_list_layout,
                    parent, false);
        }

        // Lookup views
        ImageView image = (ImageView)convertView.findViewById(R.id.friend_image);
        TextView name = (TextView)convertView.findViewById(R.id.friend_name);

        // Populate the data using the posts
        image.setImageResource(R.drawable.profile);//hardcoded
        name.setText(friend.getFirstname() + " " + friend.getLastname());

        // Return view for rendering
        return convertView;
    }
}