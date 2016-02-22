package com.andreasogeirik.master_frontend.application.user.my_profile;

import android.graphics.Bitmap;

import com.andreasogeirik.master_frontend.application.user.my_profile.interfaces.MyProfileInteractor;
import com.andreasogeirik.master_frontend.application.user.my_profile.interfaces.MyProfilePresenter;
import com.andreasogeirik.master_frontend.communication.GetFriendsTask;
import com.andreasogeirik.master_frontend.communication.GetPostsTask;
import com.andreasogeirik.master_frontend.listener.OnFinishedLoadingFriendshipsListener;
import com.andreasogeirik.master_frontend.listener.OnFinishedLoadingPostsListener;
import com.andreasogeirik.master_frontend.model.Friendship;
import com.andreasogeirik.master_frontend.model.Post;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.ImageInteractor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public class MyProfileInteractorImpl implements MyProfileInteractor, OnFinishedLoadingPostsListener,
        OnFinishedLoadingFriendshipsListener, ImageInteractor.OnImageFoundListener
{
    private MyProfilePresenter presenter;

    public MyProfileInteractorImpl(MyProfilePresenter presenter) {
        this.presenter = presenter;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    /*
     * Handling post list
     */
    @Override
    public void findPosts(User user, int start) {
        new GetPostsTask(this, user, start).execute();
    }

    @Override
    public void onSuccessPostsLoad(JSONArray jsonPosts) {
        List<Post> posts = new ArrayList<>();

        try {
            for (int i = 0; i < jsonPosts.length(); i++) {
                posts.add(new Post(jsonPosts.getJSONObject(i)));
            }
        }
        catch (JSONException e) {
            presenter.errorPostsLoad(Constants.CLIENT_ERROR);
        }
        presenter.successPostsLoad(posts);
    }

    @Override
    public void onFailedPostsLoad(int error) {
        presenter.errorPostsLoad(error);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    /*
     * Handling friend list
     */
    @Override
    public void findFriends(int userId) {
        new GetFriendsTask(this, userId).execute();
    }

    @Override
    public void onSuccessFriendshipsLoad(JSONArray friendshipsJson) {
        Set<Friendship> friendships = new HashSet<>();

        try {
            for(int i = 0; i < friendshipsJson.length(); i++) {
                JSONObject friendship = friendshipsJson.getJSONObject(i);

                int id = friendship.getInt("id");
                User friend = new User(friendship.getJSONObject("friend"));
                int status = friendship.getInt("status");
                Date friendsSince = new Date(friendship.getLong("friendsSince"));

                    friendships.add(new Friendship(id, friend, status, friendsSince));
            }
        }
        catch (JSONException e) {
            System.out.println("JSON error: " + e);
            presenter.errorFriendsLoad(Constants.CLIENT_ERROR);
        }

        presenter.successFriendsLoad(friendships);

    }

    @Override
    public void onFailedFriendshipsLoad(int code) {
        presenter.errorFriendsLoad(code);

    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    /*
     * Image handling
     */
    @Override
    public void findImage(String imageUri, File storagePath) {
        ImageInteractor.getInstance().findImage(imageUri, storagePath, this);
    }

    @Override
    public void foundImage(String imageUrl, Bitmap bitmap) {
        presenter.imageFound(imageUrl, bitmap);
    }

    @Override
    public void onProgressChange(int percent) {
        //TODO:handle this maybe maybe not
    }

    @Override
    public void imageNotFound(String imageUri) {
        presenter.imageNotFound();//TODO:return this eller ei?
    }
}
