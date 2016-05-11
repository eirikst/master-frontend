package com.andreasogeirik.master_frontend.application.event.main.interfaces;

import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.model.Post;
import com.andreasogeirik.master_frontend.model.User;


/**
 * Created by Andreas on 10.02.2016.
 */
public interface EventPresenter {
    void findPosts();
    void post(String msg);

    void comment(Post post, String message);

    void likePost(int postId);
    void likeComment(int commentId);
    void unlikePost(int postId);
    void unlikeComment(int commentId);

    void attendEvent();
    void unAttendEvent();
    void attendSuccess(Event event);
    void attendError(int error);
    void initGui();
    void setEventAttributes();
    void navigateToParticipants();
    void updateView();
    void navigateToEditEvent();
    void cancelEvent();
    void deleteSuccess();
    void deleteError(int error);

    void postSuccess(Post post);
    void postFailure(int code);

    void navigateToUser(User user);

    void eventSuccess(Event event);
    void eventFailure(int code);
}
