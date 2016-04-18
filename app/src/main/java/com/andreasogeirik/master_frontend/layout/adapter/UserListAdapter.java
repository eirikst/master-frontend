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
import com.andreasogeirik.master_frontend.layout.transformation.CircleTransform;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;
import com.squareup.picasso.Picasso;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by eirikstadheim on 05/02/16.
 */
public class UserListAdapter extends ArrayAdapter<User> {

    private Context context;
    private List<User> users;
    private CircleTransform circleTransform = new CircleTransform();

    public UserListAdapter(Context context, List<User> users) {
        super(context, 0, users);
        this.context = context;
        this.users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_search_list_layout,
                    parent, false);
        }

        // Lookup views
        ImageView image = (ImageView)convertView.findViewById(R.id.user_image);
        TextView name = (TextView)convertView.findViewById(R.id.name);


        //load image
        if(user.getThumbUri() != null && !user.getThumbUri().isEmpty()) {
            Picasso.with(context)
                    .load(user.getThumbUri())
                    .error(R.drawable.default_profile)
                    .resize(Constants.LIST_IMAGE_WIDTH, Constants.LIST_IMAGE_HEIGHT)
                    .centerCrop()
                    .transform(circleTransform)
                    .into(image);
        }
        else {
            Picasso.with(context)
                    .load(R.drawable.default_profile)
                    .resize(Constants.LIST_IMAGE_WIDTH, Constants.LIST_IMAGE_HEIGHT)
                    .centerCrop()
                    .transform(circleTransform)
                    .into(image);
        }


        //Populate name
        name.setText(user.getFirstname() + " " + user.getLastname());

        // Return view for rendering
        return convertView;
    }

    public void setData(Collection<User> users) {
        this.clear();
        this.addAll(users);
    }
}