package com.andreasogeirik.master_frontend.application.event.main.interfaces;

import com.andreasogeirik.master_frontend.model.ActivityType;
import com.andreasogeirik.master_frontend.model.Comment;
import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.model.Post;
import com.andreasogeirik.master_frontend.model.User;

import java.util.Collection;
import java.util.Set;

/**
 * Created by Andreas on 10.02.2016.
 */
public interface EventView {
    void setEventAttributes(String name, String location, String description, String admin, String startTime, String participants, ActivityType activityType);
    void updateEndTime(String endTime);
    void setAttendButton();
    void setParticipants(String participants);
    void setUnAttendButton();
    void setEditButton();
    void setCancelButton();
    void showErrorMessage(String error);
    void showProgress();
    void hideProgress();
    void setDifficultyView(int difficulty);
    void setImage(String imageUri);
    void initGui(Event event);
    void navigateToParticipants(Set<User> users);
    void navigateToEditEvent(Event event);
    void navigateToMain();
    void addPosts(Collection<Post> posts, boolean lastPosts);
    void noPostsToShow();
    void updatePostLike(int id, boolean like);
    void updateCommentLike(int id, boolean like);
    void addComment(Post post, Comment comment);
    void commentFinishedSuccessfully();
    void commentFinishedWithError();
    void postFinishedSuccessfully();
    void postFinishedWithError();
    void focusPost(int postId);
    void focusComment(int commentId);
}
