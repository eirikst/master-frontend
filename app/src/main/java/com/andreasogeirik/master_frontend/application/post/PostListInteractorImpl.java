package com.andreasogeirik.master_frontend.application.post;

import android.util.Log;

import com.andreasogeirik.master_frontend.communication.CommentTask;
import com.andreasogeirik.master_frontend.communication.GetEventPostsTask;
import com.andreasogeirik.master_frontend.communication.GetPostsTask;
import com.andreasogeirik.master_frontend.communication.LikeTask;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.listener.OnFinishedLoadingPostsListener;
import com.andreasogeirik.master_frontend.model.Comment;
import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.model.Post;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by eirikstadheim on 13/04/16.
 */
public class PostListInteractorImpl implements PostListInteractor, LikeTask.OnFinishedLikingListener
, OnFinishedLoadingPostsListener, CommentTask.OnFinishedCommentingListener {
    public interface Listener {
        void successPostsLoad(Set<Post> posts);
        void errorPostsLoad(int code);

        void onSuccessComment(Post post, Comment comment);
        void onFailureComment(int code);

        void onSuccessPostLike(int id);
        void onFailurePostLike(int id);
        void onSuccessCommentLike(int id);
        void onFailureCommentLike(int id);
        void onSuccessPostUnlike(int id);
        void onFailurePostUnlike(int id);
        void onSuccessCommentUnlike(int id);
        void onFailureCommentUnlike(int id);
    }
    private final String tag = getClass().getSimpleName();

    private Listener listener;

    public PostListInteractorImpl(Listener listener) {
        this.listener = listener;
    }


    /*
     * Find posts
     */
    @Override
    public void findPosts(User user, int start) {
        new GetPostsTask(this, user, start).execute();
    }

    /*
     * Find posts for events
     */
    @Override
    public void findPosts(Event event, int start) {
        new GetEventPostsTask(this, event, start).execute();
    }

    @Override
    public void onSuccessPostsLoad(JSONArray jsonPosts) {
        Set<Post> posts = new HashSet<>();

        try {
            for (int i = 0; i < jsonPosts.length(); i++) {
                posts.add(new Post(jsonPosts.getJSONObject(i)));
            }
        }
        catch (JSONException e) {
            Log.w(tag, "JSON error: " + e);
            e.printStackTrace();
            listener.errorPostsLoad(Constants.JSON_PARSE_ERROR);
        }
        listener.successPostsLoad(posts);
    }

    @Override
    public void onFailedPostsLoad(int code) {
        listener.errorPostsLoad(code);
    }


    /*
     * Comment
     */
    @Override
    public void comment(Post post, String message) {
        new CommentTask(this, message, post, CurrentUser.getInstance().getUser().getId()).execute();
    }

    @Override
    public void onSuccessComment(Post post, JSONObject jsonComment) {
        try {
            Comment comment = new Comment(jsonComment);
            listener.onSuccessComment(post, comment);
        }
        catch(JSONException e) {
            listener.onFailureComment(Constants.JSON_PARSE_ERROR);
        }
    }

    @Override
    public void onFailureComment(int code) {
        listener.onFailureComment(code);
    }

    /*
     * Like
     */
    @Override
    public void likePost(int postId) {
        new LikeTask(this, postId, LikeTask.POST, LikeTask.LIKE).execute();
    }

    @Override
    public void likeComment(int commentId) {
        new LikeTask(this, commentId, LikeTask.COMMENT, LikeTask.LIKE).execute();
    }

    @Override
    public void unlikePost(int postId) {
        new LikeTask(this, postId, LikeTask.POST, LikeTask.UNLIKE).execute();
    }

    @Override
    public void unlikeComment(int commentId) {
        new LikeTask(this, commentId, LikeTask.COMMENT, LikeTask.UNLIKE).execute();
    }

    /*
     * Listener methods
     */
    @Override
    public void onSuccessPostLike(int id) {
        listener.onSuccessPostLike(id);
    }

    @Override
    public void onFailurePostLike(int id) {
        listener.onFailurePostLike(id);
    }

    @Override
    public void onSuccessCommentLike(int id) {
        listener.onSuccessCommentLike(id);
    }

    @Override
    public void onFailureCommentLike(int id) {
        listener.onFailureCommentLike(id);
    }

    @Override
    public void onSuccessPostUnlike(int id) {
        listener.onSuccessPostUnlike(id);
    }

    @Override
    public void onFailurePostUnlike(int id) {
        listener.onFailurePostUnlike(id);
    }

    @Override
    public void onSuccessCommentUnlike(int id) {
        listener.onSuccessCommentUnlike(id);
    }

    @Override
    public void onFailureCommentUnlike(int id) {
        listener.onFailureCommentUnlike(id);
    }
}
