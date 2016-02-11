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
import com.andreasogeirik.master_frontend.util.DateUtility;

import java.util.Comparator;
import java.util.List;

/**
 * Created by eirikstadheim on 05/02/16.
 */
public class PostListAdapter extends ArrayAdapter<Post> {
    private List<Post> posts;


    public PostListAdapter(Context context, List<Post> posts) {
        super(context, 0, posts);
        this.posts = posts;

        this.sort(new Comparator<Post>() {
            @Override
            public int compare(Post lhs, Post rhs) {
                return lhs.compareTo(rhs);
            }
        });
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Post post = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.my_profile_post_list_layout, parent, false);
        }

        // Lookup views
        ImageView image = (ImageView)convertView.findViewById(R.id.post_image);
        TextView message = (TextView)convertView.findViewById(R.id.post_message);
        TextView dateCreated = (TextView)convertView.findViewById(R.id.post_date);
        TextView nrOfComments = (TextView)convertView.findViewById(R.id.comment_nr);
        TextView nrOfLikes = (TextView)convertView.findViewById(R.id.like_nr);

        // Populate the data using the posts
        image.setImageResource(R.drawable.profile);//hardcoded
        message.setText(post.getMessage());
        dateCreated.setText(DateUtility.getDateUtility().format(post.getCreated()));
        nrOfComments.setText("" + posts.get(position).getComments().size() + " comments");
        nrOfLikes.setText("" + posts.get(position).getLikers().size() + " likes");

        // Return view for rendering
        return convertView;
    }
}