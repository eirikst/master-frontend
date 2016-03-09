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
import java.util.Set;

/**
 * Created by eirikstadheim on 05/02/16.
 */
public class UserSearchListAdapter extends ArrayAdapter<User> {
    public interface Listener {
        void findImage(String imageUri);
    }

    private Context context;
    private List<User> users;
    private Map<String, Bitmap> profileImages;
    private Listener listener;

    public UserSearchListAdapter(Context context, List<User> users, Listener
            listener) {
        super(context, 0, users);
        this.context = context;
        this.users = users;
        this.profileImages = new HashMap<String, Bitmap>();
        this.listener = listener;
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


        //image in local map
        if(profileImages.containsKey(user.getImageUri())) {
            System.out.println("Image found and set for " + user.getFirstname());
            image.setImageBitmap(profileImages.get(user.getImageUri()));
        }
        else {
            //no image, user standard
            if(user.getImageUri() == null || user.getImageUri().equals("")) {
                System.out.println("Image null or empty for " + user.getFirstname() + ". Setting standard image");
                setDefaultImage(image, user.getImageUri());
            }
            //get image from outside
            else {
                System.out.println("Image not found for " + user.getFirstname() + ". Fetching...");
                listener.findImage(user.getImageUri());
            }
        }


        //Populate name
        name.setText(user.getFirstname() + " " + user.getLastname());

        // Return view for rendering
        return convertView;
    }

    public void setData(List<User> users) {
        this.clear();
        this.addAll(users);
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