package com.andreasogeirik.master_frontend.application.user;

import com.andreasogeirik.master_frontend.application.user.interfaces.MyProfileInteractor;
import com.andreasogeirik.master_frontend.application.user.interfaces.MyProfilePresenter;
import com.andreasogeirik.master_frontend.application.user.interfaces.MyProfileView;
import com.andreasogeirik.master_frontend.communication.GetPostsTask;
import com.andreasogeirik.master_frontend.listener.OnFinishedLoadingPostsListener;
import com.andreasogeirik.master_frontend.model.Post;
import com.andreasogeirik.master_frontend.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public class MyProfileInteractorImpl implements MyProfileInteractor, OnFinishedLoadingPostsListener
{
    private MyProfilePresenter presenter;

    public MyProfileInteractorImpl(MyProfilePresenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void findPosts(int start) {
        new GetPostsTask(this).execute();
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
}