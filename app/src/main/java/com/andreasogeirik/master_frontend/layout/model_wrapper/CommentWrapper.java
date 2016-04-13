package com.andreasogeirik.master_frontend.layout.model_wrapper;

import com.andreasogeirik.master_frontend.model.Comment;
import com.andreasogeirik.master_frontend.model.Post;

import java.util.Date;

/**
 * Created by eirista on 12.04.2016.
 */
public class CommentWrapper extends PostListElement {
    private Comment comment;
    private Post post;

    public CommentWrapper(Comment comment, Post post) {
        this.comment = comment;
        this.post = post;
    }

    @Override
    public int getId() {
        return comment.getId();
    }

    @Override
    public int getPostId() {
        return post.getId();
    }

    @Override
    public boolean isPost() {
        return false;
    }

    @Override
    public Date getPostTimeCreated() {
        return post.getCreated();
    }

    @Override
    public Date getTimeCreated() {
        return comment.getTimeCreated();
    }

    @Override
    public Object getModel() {
        return comment;
    }
}
