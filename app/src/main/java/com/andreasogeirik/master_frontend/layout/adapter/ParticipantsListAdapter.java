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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andreas on 11.03.2016.
 */
public class ParticipantsListAdapter extends ArrayAdapter<User> {

    private Context context;
    private List<User> participants;
    private Map<String, Bitmap> profileImages;
    private CircleTransform circleTransform = new CircleTransform();

    public ParticipantsListAdapter(Context context, List<User> participants) {
        super(context, 0, participants);
        this.context = context;
        this.participants = participants;
        this.profileImages = new HashMap<>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        
        // Get the data item for this position
        User user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.participant_item, parent, false);
        }
        // Lookup view for data population
        ImageView image = (ImageView) convertView.findViewById(R.id.profile_pic);
        TextView participantName = (TextView) convertView.findViewById(R.id.name);

        // Populate the data into the template view using the data object

        // Populate profile image

        //add image
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
        participantName.setText(user.getFirstname() + " " + user.getLastname());

        // Return the completed view to render on screen
        return convertView;
    }
}
