package com.andreasogeirik.master_frontend.layout.model_wrapper;

import com.andreasogeirik.master_frontend.model.Comment;

import java.util.Date;

/**
 * Created by eirista on 12.04.2016.
 */
public abstract class PostListElement {
    public abstract int getId();
    public abstract int getPostId();
    public abstract Date getPostTimeCreated();
    public abstract Date getTimeCreated();
    public abstract boolean isPost();
    public abstract Object getModel();
    public abstract PostWrapper getPost();

    private static final int BEFORE = -1;
    private static final int AFTER = 1;

    // -1 means first
    public int compareTo(PostListElement object) {
        if(this.isPost() && object.isPost()) {
            if(this.getPostTimeCreated().equals(object.getPostTimeCreated())) {
                if(this.getId() < object.getId()) {
                    return AFTER;
                }
                else {
                    return BEFORE;
                }
            }
            else if(this.getPostTimeCreated().before(object.getPostTimeCreated())) {
                return AFTER;
            }
            else {
                return BEFORE;
            }
        }
        else if(this.isPost() && !object.isPost()) {
            return compareTo(object.getPost());
        }
        else if(!this.isPost() && object.isPost()) {
            return -object.compareTo(this.getPost());
        }
        else {
            if (this.getPostId() == object.getPostId()) {
                if (this.getTimeCreated().before(object.getTimeCreated())) {
                    return BEFORE;
                } else {
                    return AFTER;
                }
            } else {
                return this.getPost().compareTo(object.getPost());
            }
        }
    }

    @Override
    public String toString() {
        return getModel().toString();
    }
}
