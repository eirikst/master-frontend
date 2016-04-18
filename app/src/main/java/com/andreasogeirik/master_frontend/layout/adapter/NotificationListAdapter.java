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
import com.andreasogeirik.master_frontend.layout.transformation.CircleTransform;
import com.andreasogeirik.master_frontend.listener.OnUserClickListener;
import com.andreasogeirik.master_frontend.model.Friendship;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by eirikstadheim on 05/02/16.
 */
public class NotificationListAdapter extends ArrayAdapter<Object> {
    public interface Listener {
        void acceptFriendship(int friendshipId);
        void rejectFriendship(int friendshipId);
        void navigateToUser(User user);
    }

    private Context context;
    private List<Object> objects;
    private Listener listener;
    private CircleTransform circleTransform = new CircleTransform();

    public NotificationListAdapter(Context context, List<Object> objects, Listener
            listener) {
        super(context, 0, objects);
        this.context = context;
        this.objects = objects;
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


        //load image
        if(friendship.getFriend().getThumbUri() != null && !friendship.getFriend().getThumbUri().isEmpty()) {
            Picasso.with(context)
                    .load(friendship.getFriend().getThumbUri())
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

        //listen to click on user
        OnUserClickListener userClickListener = new OnUserClickListener(new OnUserClickListener.Listener() {
            @Override
            public void userClicked(User user) {
                listener.navigateToUser(user);
            }
        }, friendship.getFriend());

        image.setOnClickListener(userClickListener);
        text.setOnClickListener(userClickListener);


        // Return view for rendering
        return convertView;
    }

    public void setNotifications(Set<Object> notifications) {
        this.objects.clear();
        this.objects.addAll(notifications);
        notifyDataSetChanged();
    }
}