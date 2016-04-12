package com.andreasogeirik.master_frontend.layout.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.model.Post;
import com.andreasogeirik.master_frontend.util.DateUtility;

import java.util.List;


/**
 * Created by Andreas on 26.02.2016.
 */
public class EventMainAdapter extends ArrayAdapter<Post> {

    private List<Post> posts;

    public EventMainAdapter(Context context, List<Post> posts) {
        super(context, 0);
        this.posts = posts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Post post = getItem(position);


        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_post_list_layout, parent, false);
        }

        TextView message = (TextView)convertView.findViewById(R.id.post_message);
        TextView dateCreated = (TextView)convertView.findViewById(R.id.post_date);
        TextView nrOfComments = (TextView)convertView.findViewById(R.id.comment_nr);
        TextView nrOfLikes = (TextView)convertView.findViewById(R.id.like_nr);

        // Populate the data using the posts
        message.setText(post.getMessage());
        dateCreated.setText(DateUtility.formatFull(post.getCreated()));
        nrOfComments.setText("" + posts.get(position).getComments().size() + " comments");
        // TODO Implement likes
//        nrOfLikes.setText("" + posts.get(position).getLikers().size() + " likes");

        return convertView;

    }
}
