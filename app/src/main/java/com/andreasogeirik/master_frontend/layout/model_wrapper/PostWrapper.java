package com.andreasogeirik.master_frontend.layout.model_wrapper;

import com.andreasogeirik.master_frontend.model.Post;

import java.util.Date;

/**
 * Created by eirista on 12.04.2016.
 */
public class PostWrapper extends PostListElement {
    private Post post;

    public PostWrapper(Post post) {
        this.post = post;
    }

    @Override
    public int getPostId() {
        return post.getId();
    }

    @Override
    public boolean isPost() {
        return true;
    }

    @Override
    public Date getPostTimeCreated() {
        return post.getCreated();
    }

    @Override
    public Date getTimeCreated() {
        return post.getCreated();
    }

    @Override
    public Object getModel() {
        return post;
    }
}
