package com.andreasogeirik.master_frontend.layout.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.model.Friendship;
import com.andreasogeirik.master_frontend.util.Constants;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by eirikstadheim on 05/02/16.
 */
public class FriendListAdapter extends ArrayAdapter<Friendship> {

    private Context context;
    private List<Friendship> friends;

    public FriendListAdapter(Context context, List<Friendship> friends) {
        super(context, 0, friends);
        this.context = context;
        this.friends = friends;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Friendship friendship = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.friend_list_layout,
                    parent, false);
        }

        // Lookup views
        ImageView image = (ImageView)convertView.findViewById(R.id.friend_image);
        TextView name = (TextView)convertView.findViewById(R.id.friend_name);


        //load image
        if(friendship.getFriend().getImageUri() != null && !friendship.getFriend().getImageUri().isEmpty()) {
            Picasso.with(context)
                    .load(friendship.getFriend().getImageUri())
                    .error(R.drawable.default_profile)
                    .resize(Constants.LIST_IMAGE_WIDTH, Constants.LIST_IMAGE_HEIGHT)
                    .centerCrop()
                    .into(image);
        }
        else {
            Picasso.with(context)
                    .load(R.drawable.default_profile)
                    .resize(Constants.LIST_IMAGE_WIDTH, Constants.LIST_IMAGE_HEIGHT)
                    .centerCrop()
                    .into(image);
        }

        //Populate name
        name.setText(friendship.getFriend().getFirstname() + " " + friendship.getFriend().getLastname());

        // Return view for rendering
        return convertView;
    }
}