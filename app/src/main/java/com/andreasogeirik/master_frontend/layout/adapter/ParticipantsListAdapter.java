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
import com.andreasogeirik.master_frontend.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andreas on 11.03.2016.
 */
public class ParticipantsListAdapter extends ArrayAdapter<User> {

    public interface Listener {
        void findImage(String imageUri);
    }

    private Context context;
    private List<User> participants;
    private Map<String, Bitmap> profileImages;
    private Listener listener;

    public ParticipantsListAdapter(Context context, List<User> participants, Listener
            listener) {
        super(context, 0, participants);
        this.context = context;
        this.participants = participants;
        this.profileImages = new HashMap<>();
        this.listener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        System.out.println(profileImages);

        // Get the data item for this position
        User user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.participant_item, parent, false);
        }
        // Lookup view for data population
        ImageView profilePic = (ImageView) convertView.findViewById(R.id.profile_pic);
        TextView participantName = (TextView) convertView.findViewById(R.id.name);

        // Populate the data into the template view using the data object

        // Populate profile image

        //image in local map
        if(profileImages.containsKey(user.getImageUri())) {
            System.out.println("Image found and set for " + user.getFirstname());
            profilePic.setImageBitmap(profileImages.get(user.getImageUri()));
        }
        else {
            //no image, user standard
            if(user.getImageUri() == null || user.getImageUri().equals("")) {
                System.out.println("Image null or empty for " + user.getFirstname() + ". Setting standard image");
                setDefaultImage(profilePic, user.getImageUri());
            }
            //get image from outside
            else {
                System.out.println("Image not found for " + user.getFirstname() + ". Fetching...");
                listener.findImage(user.getImageUri());
            }
        }

        //Populate name
        participantName.setText(user.getFirstname() + " " + user.getLastname());

        // Return the completed view to render on screen
        return convertView;
    }

    /*
     * Set image to null to get default. Name is always needed
     */
    public void addImage(String name, Bitmap image) {
        profileImages.put(name, image);
        notifyDataSetChanged();
    }

    private void setDefaultImage(ImageView image, String imageName) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.default_profile);
        if(bitmap != null) {
            image.setImageBitmap(bitmap);
            profileImages.put(imageName, bitmap);
        }
    }
}
