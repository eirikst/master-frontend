package com.andreasogeirik.master_frontend.layout.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.layout.model_wrapper.CommentWrapper;
import com.andreasogeirik.master_frontend.layout.model_wrapper.PostListElement;
import com.andreasogeirik.master_frontend.layout.model_wrapper.PostWrapper;
import com.andreasogeirik.master_frontend.layout.transformation.CircleTransform;
import com.andreasogeirik.master_frontend.model.Comment;
import com.andreasogeirik.master_frontend.model.Post;
import com.andreasogeirik.master_frontend.model.UserSmall;
import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.DateUtility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * Created by eirikstadheim on 05/02/16.
 */
public class PostListAdapter extends ArrayAdapter<PostListElement> {
    public interface PostListCallback {
        void likeComment(int commentId);
        void likePost(int postId);
        void unlikeComment(int commentId);
        void unlikePost(int postId);
        void showComment(Post post);
    }

    private static final int POST = 0;
    private static final int COMMENT = 1;

    private Comparator comparator;
    private PostListCallback callback;
    private CircleTransform circleTransform = new CircleTransform();


    public PostListAdapter(Context context, List<Post> posts, PostListCallback callback) {
        super(context, 0, new ArrayList<PostListElement>());
        this.callback = callback;

        List<PostListElement> elements = new ArrayList<>();

        //add all list elements from the posts(posts and comments are list elements)
        for(Post post: posts) {
            elements.add(new PostWrapper(post));

            for(Comment comment: post.getComments()) {
                elements.add(new CommentWrapper(comment, post));
            }
        }

        comparator = new Comparator<PostListElement>() {
            @Override
            public int compare(PostListElement lhs, PostListElement rhs) {
                return lhs.compareTo(rhs);
            }
        };
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if(getItem(position).isPost()) {
            return POST;
        }
        else {
            return COMMENT;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PostListElement item = getItem(position);
        if(item.isPost()) {
            Post post = ((Post)item.getModel());
            return getPostView(post, position, convertView, parent);
        }
        else {
            Comment comment = ((Comment)item.getModel());
            return getCommentView(comment, position, convertView, parent);
        }
    }

    public View getPostView(final Post post, int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.post_list_layout, parent, false);
        }

        // Lookup views
        ImageView image = (ImageView)convertView.findViewById(R.id.post_image);


        if(post.getWriter().getThumbUri() != null && !post.getWriter().getThumbUri().isEmpty()) {
            Picasso.with(getContext())
                    .load(post.getWriter().getThumbUri())
                    .error(R.drawable.default_profile)
                    .resize(Constants.LIST_IMAGE_WIDTH, Constants.LIST_IMAGE_HEIGHT)
                    .centerCrop()
                    .transform(circleTransform)
                    .into(image);
        }
        else {
            Picasso.with(getContext())
            .load(R.drawable.default_profile)
                    .resize(Constants.LIST_IMAGE_WIDTH, Constants.LIST_IMAGE_HEIGHT)
                    .centerCrop()
                    .transform(circleTransform)
                    .into(image);
        }


        TextView name = (TextView)convertView.findViewById(R.id.post_name);
        TextView message = (TextView)convertView.findViewById(R.id.post_message);
        TextView dateCreated = (TextView)convertView.findViewById(R.id.post_date);
        TextView nrOfComments = (TextView)convertView.findViewById(R.id.comment_nr);
        TextView nrOfLikes = (TextView)convertView.findViewById(R.id.like_nr);
        LinearLayout unlikeBtn = (LinearLayout)convertView.findViewById(R.id.unlike_btn);
        LinearLayout likeBtn = (LinearLayout)convertView.findViewById(R.id.like_btn);
        LinearLayout commentBtn = (LinearLayout)convertView.findViewById(R.id.comment_btn);

        if(post.likes(CurrentUser.getInstance().getUser())) {
            likeBtn.setVisibility(View.GONE);
            unlikeBtn.setVisibility(View.VISIBLE);
        }
        else {
            likeBtn.setVisibility(View.VISIBLE);
            unlikeBtn.setVisibility(View.GONE);
        }

        name.setText(post.getWriter().getFirstname() + " " + post.getWriter().getLastname());
        message.setText(post.getMessage());
        dateCreated.setText(DateUtility.formatFull(post.getCreated()));
        if(post.getComments().size() == 1) {
            nrOfComments.setText("1 kommentar");
        }
        else {
            nrOfComments.setText(post.getComments().size() + " kommentarer");
        }

        nrOfLikes.setText(post.getLikers().size() + " liker");

        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.likePost(post.getId());
            }
        });

        unlikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.unlikePost(post.getId());
            }
        });

        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.showComment(post);
            }
        });

        // Return view for rendering
        return convertView;

    }

    public View getCommentView(final Comment comment, int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.profile_post_list_layout_comment, parent, false);
        }


        // Lookup views
        ImageView image = (ImageView)convertView.findViewById(R.id.comment_image);


        if(comment.getWriter().getThumbUri() != null && !comment.getWriter().getThumbUri().isEmpty()) {
            Picasso.with(getContext())
                    .load(comment.getWriter().getThumbUri())
                    .error(R.drawable.default_profile)
                    .resize(Constants.LIST_IMAGE_WIDTH, Constants.LIST_IMAGE_HEIGHT)
                    .centerCrop()
                    .transform(circleTransform)
                    .into(image);
        }
        else {
            Picasso.with(getContext())
                    .load(R.drawable.default_profile)
                    .resize(Constants.LIST_IMAGE_WIDTH, Constants.LIST_IMAGE_HEIGHT)
                    .centerCrop()
                    .transform(circleTransform)
                    .into(image);
        }


        TextView name = (TextView)convertView.findViewById(R.id.comment_name);
        TextView message = (TextView)convertView.findViewById(R.id.comment_message);
        TextView dateCreated = (TextView)convertView.findViewById(R.id.comment_date);
        TextView nrOfLikes = (TextView)convertView.findViewById(R.id.comment_like_nr);
        LinearLayout unlikeBtn = (LinearLayout)convertView.findViewById(R.id.unlike_btn);
        LinearLayout likeBtn = (LinearLayout)convertView.findViewById(R.id.like_btn);

        if(comment.likes(CurrentUser.getInstance().getUser())) {
            likeBtn.setVisibility(View.GONE);
            unlikeBtn.setVisibility(View.VISIBLE);
        }
        else {
            likeBtn.setVisibility(View.VISIBLE);
            unlikeBtn.setVisibility(View.GONE);
        }

        // Populate the data using the posts
        name.setText(comment.getWriter().getFirstname() + " " + comment.getWriter().getLastname());
        message.setText(comment.getMessage());
        dateCreated.setText(DateUtility.formatFull(comment.getTimeCreated()));
        nrOfLikes.setText(comment.getLikers().size() + " liker");

        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.likeComment(comment.getId());
            }
        });

        unlikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.unlikeComment(comment.getId());
            }
        });



        // Return view for rendering
        return convertView;

    }


    public void add(Post post) {
        List<PostListElement> elements = new ArrayList<>();
        elements.add(new PostWrapper(post));

        for(Comment comment: post.getComments()) {
            elements.add(new CommentWrapper(comment, post));
        }

        super.addAll(elements);
        sort(comparator);
    }

    public void addPosts(Collection<? extends Post> collection) {
        List<PostListElement> elements = new ArrayList<>();

        for(Post post: collection) {
            elements.add(new PostWrapper(post));

            for(Comment comment: post.getComments()) {
                elements.add(new CommentWrapper(comment, post));
            }
        }

        super.addAll(elements);
        sort(comparator);
    }

    @Override
    public void addAll(Collection<? extends PostListElement> collection) {
        super.addAll(collection);
        sort(comparator);
    }

    public void addComment(Post post, Comment comment) {
        super.add(new CommentWrapper(comment, post));
        sort(comparator);
    }

    public void updateComment(int id, boolean like) {
        for(int i = 0; i < getCount(); i++) {
            PostListElement element = getItem(i);

            if(!element.isPost() && element.getId() == id) {
                //like
                if (like) {
                    ((Comment) element.getModel()).getLikers().add(new UserSmall(CurrentUser.getInstance().getUser()));
                }
                //unlike
                else {
                    ((Comment) element.getModel()).getLikers().remove(new UserSmall(CurrentUser.getInstance().getUser()));
                }
            }
        }
        notifyDataSetChanged();
    }

    public void updatePost(int id, boolean like) {
        for(int i = 0; i < getCount(); i++) {
            PostListElement element = getItem(i);

            if(element.isPost() && element.getId() == id) {
                //like
                if (like) {
                    ((Post) element.getModel()).getLikers().add(new UserSmall(CurrentUser.getInstance().getUser()));
                }
                //unlike
                else {
                    ((Post) element.getModel()).getLikers().remove(new UserSmall(CurrentUser.getInstance().getUser()));
                }
            }
        }
        notifyDataSetChanged();
    }
}