package com.andreasogeirik.master_frontend.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by eirikstadheim on 04/05/16.
 */
public class LogElement {
    private int id;
    private Date time;
    private String content;
    private ContentType type;
    private int contentId;


    public LogElement() {
    }

    public LogElement(JSONObject obj) throws JSONException{
        this.id = obj.getInt("id");
        this.time = new Date(obj.getLong("time"));
        this.content = obj.getString("content");
        this.type = ContentType.fromInt(obj.getInt("type"));
        this.contentId = obj.getInt("contentId");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ContentType getType() {
        return type;
    }

    public void setType(ContentType type) {
        this.type = type;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    @Override
    public String toString() {
        return "LogElement{" +
                "id=" + id +
                ", time=" + time +
                ", content='" + content + '\'' +
                ", type=" + type +
                ", contentId=" + contentId +
                '}';
    }
}
