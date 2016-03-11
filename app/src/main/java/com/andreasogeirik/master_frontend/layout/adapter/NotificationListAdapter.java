package com.andreasogeirik.master_frontend.layout.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.model.Friendship;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by eirikstadheim on 05/02/16.
 */
public class NotificationListAdapter extends ArrayAdapter<Object> {
    public interface Listener {
        void findImage(String imageUri);
        void acceptFriendship(int friendshipId);
        void rejectFriendship(int friendshipId);
    }

    private Context context;
    private List<Object> objects;
    private Map<String, Bitmap> images;
    private Listener listener;

    public NotificationListAdapter(Context context, List<Object> objects, Listener
            listener) {
        super(context, 0, objects);
        this.context = context;
        this.objects = objects;
        this.images = new HashMap<String, Bitmap>();
        this.listener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Object object = getItem(position);

        if(object instanceof Friendship) {
            return getFriendshipView((Friendship)object, convertView, parent);
        }

        //TODO: return null ok eller fucker det opp viewet?
        return null;
    }

    private View getFriendshipView(final Friendship friendship, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.notification_friend_list_layout,
                    parent, false);
        }

        // Lookup views
        ImageView image = (ImageView)convertView.findViewById(R.id.notification_friend_image);
        TextView text = (TextView)convertView.findViewById(R.id.notification_friend_text);


        // Populate profile image

        //image in local map
        if(images.containsKey(friendship.getFriend().getImageUri())) {
            System.out.println("Image found and set for " + friendship.getFriend().getFirstname());
            image.setImageBitmap(images.get(friendship.getFriend().getImageUri()));
        }
        else {
            //no image, user standard
            if(friendship.getFriend().getImageUri() == null || friendship.getFriend().getImageUri().equals("")) {
                System.out.println("Image null or empty for " + friendship.getFriend().getFirstname() + ". Setting default image");
                setDefaultImage(image, friendship.getFriend().getImageUri());
            }
            //get image from outside
            else {
                System.out.println("Image not found for " + friendship.getFriend().getFirstname() + ". Fetching...");
                listener.findImage(friendship.getFriend().getImageUri());
            }
        }

        //Populate name
        text.setText(friendship.getFriend().getFirstname() + " " + friendship.getFriend()
                .getLastname() + " ønsker å bli din venn");

        Button acceptBtn = (Button)convertView.findViewById(R.id.accept_friend);
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.acceptFriendship(friendship.getId());
            }
        });

        Button rejectBtn = (Button)convertView.findViewById(R.id.reject_friend);
        rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.rejectFriendship(friendship.getId());
            }
        });

        // Return view for rendering
        return convertView;
    }

    /*
     * Set image to null to get default. Name is always needed
     */
    public void addImage(String name, Bitmap image) {
        images.put(name, image);
        notifyDataSetChanged();
    }

    private void setDefaultImage(ImageView image, String imageName) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.default_profile);
        if(bitmap != null) {
            image.setImageBitmap(bitmap);
            images.put(imageName, bitmap);
        }
    }

    public void setNotifications(Set<Object> notifications) {
        this.objects.clear();
        this.objects.addAll(notifications);
        notifyDataSetChanged();
    }
}