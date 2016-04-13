package com.andreasogeirik.master_frontend.layout.model_wrapper;

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

    public int compareTo(PostListElement object) {
        System.out.println(object.toString());
        if(this.getPostTimeCreated().equals(object.getPostTimeCreated())) {
            if(this.getPostId() == object.getPostId()) {
                if(isPost()) {
                    return -1;
                }
                else if(this.getTimeCreated().before(object.getTimeCreated())) {
                    return -1;
                }
                else {
                    return 1;
                }
            }
        }
        else if(this.getPostTimeCreated().before(object.getPostTimeCreated())) {
            return -1;
        }
        return 1;
    }

    @Override
    public String toString() {
        return getModel().toString();
    }
}
