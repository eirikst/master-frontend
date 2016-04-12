package com.andreasogeirik.master_frontend.layout.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.model.UserPost;
import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.DateUtility;
import com.squareup.picasso.Picasso;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * Created by eirikstadheim on 05/02/16.
 */
public class PostListAdapter extends ArrayAdapter<UserPost> {
    public static final int SET_ALL = -1;

    private List<UserPost> posts;
    private Comparator comparator;
    private User user;


    public PostListAdapter(Context context, List<UserPost> posts, User user) {
        super(context, 0, posts);
        this.posts = posts;
        this.user = user;

        comparator = new Comparator<UserPost>() {
            @Override
            public int compare(UserPost lhs, UserPost rhs) {
                return lhs.compareTo(rhs);
            }
        };
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserPost post = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.profile_post_list_layout, parent, false);
        }

        // Lookup views
        ImageView image = (ImageView)convertView.findViewById(R.id.event_image);

        if(user.getImageUri() != null && user.getImageUri().isEmpty()) {
            Picasso.with(getContext())
                    .load(user.getImageUri())
                    .error(R.drawable.default_profile)
                    .resize(Constants.LIST_IMAGE_WIDTH, Constants.LIST_IMAGE_HEIGHT)
                    .centerCrop()
                    .into(image);
        }
        else {
            Picasso.with(getContext())
                    .load(R.drawable.default_profile)
                    .resize(Constants.LIST_IMAGE_WIDTH, Constants.LIST_IMAGE_HEIGHT)
                    .centerCrop()
                    .into(image);
        }


        TextView message = (TextView)convertView.findViewById(R.id.post_message);
        TextView dateCreated = (TextView)convertView.findViewById(R.id.post_date);
        TextView nrOfComments = (TextView)convertView.findViewById(R.id.comment_nr);
        TextView nrOfLikes = (TextView)convertView.findViewById(R.id.like_nr);

        // Populate the data using the posts
        message.setText(post.getMessage());
        dateCreated.setText(DateUtility.formatFull(post.getCreated()));
        nrOfComments.setText(posts.get(position).getComments().size() + " comments");
        nrOfLikes.setText(posts.get(position).getLikers().size() + " likes");


        // Return view for rendering
        return convertView;
    }

    @Override
    public void add(UserPost object) {
        super.add(object);
        sort(comparator);
    }

    @Override
    public void addAll(Collection<? extends UserPost> collection) {
        super.addAll(collection);
        sort(comparator);
    }

    @Override
    public void addAll(UserPost... items) {
        super.addAll(items);
        sort(comparator);
    }
}