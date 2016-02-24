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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by eirikstadheim on 05/02/16.
 */
public class FriendListAdapter extends ArrayAdapter<Friendship> {
    public interface Listener {
        public void findImage(String imageUri);
    }

    private Context context;
    private List<Friendship> friends;
    private Map<String, Bitmap> profileImages;
    private Listener listener;

    public FriendListAdapter(Context context, List<Friendship> friends, Listener
            listener) {
        super(context, 0, friends);
        this.context = context;
        this.friends = friends;
        this.profileImages = new HashMap<String, Bitmap>();
        this.listener = listener;
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


        // Populate profile image

        //image in local map
        if(profileImages.containsKey(friendship.getFriend().getImageUri())) {
            System.out.println("Image found and set for " + friendship.getFriend().getFirstname());
            image.setImageBitmap(profileImages.get(friendship.getFriend().getImageUri()));
        }
        else {
            //no image, user standard
            if(friendship.getFriend().getImageUri() == null || friendship.getFriend().getImageUri().equals("")) {
                System.out.println("Image null or empty for " + friendship.getFriend().getFirstname() + ". Setting standard image");
                setDefaultImage(image, friendship.getFriend().getImageUri());
            }
            //get image from outside
            else {
                System.out.println("Image not found for " + friendship.getFriend().getFirstname() + ". Fetching...");
                listener.findImage(friendship.getFriend().getImageUri());
            }
        }

        //Populate name
        name.setText(friendship.getFriend().getFirstname() + " " + friendship.getFriend().getLastname());

        // Return view for rendering
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