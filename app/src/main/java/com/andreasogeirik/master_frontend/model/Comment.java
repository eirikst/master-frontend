package com.andreasogeirik.master_frontend.model;

/**
 * Created by eirikstadheim on 05/02/16.
 */
public class Comment {
    private int id;
    private String message;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
