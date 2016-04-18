package com.andreasogeirik.master_frontend.application.event.main;

import android.app.Activity;
import android.content.Intent;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventInteractor;
import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventPresenter;
import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventView;
import com.andreasogeirik.master_frontend.application.general.GeneralPresenter;
import com.andreasogeirik.master_frontend.application.post.PostListInteractor;
import com.andreasogeirik.master_frontend.application.post.PostListInteractorImpl;
import com.andreasogeirik.master_frontend.application.user.profile.ProfileActivity;
import com.andreasogeirik.master_frontend.application.user.profile_others.ProfileOthersActivity;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.model.Comment;
import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.model.Post;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.DateUtility;


import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import static com.andreasogeirik.master_frontend.util.Constants.CLIENT_ERROR;
import static com.andreasogeirik.master_frontend.util.Constants.RESOURCE_ACCESS_ERROR;
import static com.andreasogeirik.master_frontend.util.Constants.UNAUTHORIZED;


/**
 * Created by Andreas on 10.02.2016.
 */
public class EventPresenterImpl extends GeneralPresenter implements EventPresenter,
        PostListInteractorImpl.Listener {
    private EventView eventView;
    private EventInteractor interactor;
    private PostListInteractor postInteractor;
    private Event event;


    public EventPresenterImpl(EventView eventView, Event event) {
        super((Activity) eventView, CHECK_USER_AVAILABLE);
        this.eventView = eventView;
        this.interactor = new EventInteractorImpl(this);
        this.postInteractor = new PostListInteractorImpl(this);
        this.event = event;

        findPosts();
    }

    @Override
    public void findPosts() {
        postInteractor.findPosts(event, event.getPosts().size());
    }

    @Override
    public void post(String msg) {
        interactor.post(event.getId(), msg);
    }

    @Override
    public void successPostsLoad(Set<Post> posts) {
        boolean lastPosts = false;

        if(posts.size() < Constants.NUMBER_OF_POSTS_RETURNED) {
            lastPosts = true;
        }

        event.getPosts().addAll(posts);
        eventView.addPosts(posts, lastPosts);

        if(event.getPosts().isEmpty()) {
            eventView.noPostsToShow();
        }
    }

    @Override
    public void errorPostsLoad(int code) {
        eventView.showErrorMessage("En feil oppsto under lasting av poster");
    }

    @Override
    public void attendEvent() {
        interactor.attendEvent(this.event.getId());
    }


    @Override
    public void unAttendEvent() {
        interactor.unAttendEvent(this.event.getId());
    }

    @Override
    public void attendSuccess(Event event) {
        this.event = event;
        updateView();
    }

    @Override
    public void attendError(int error) {
        this.eventView.hideProgress();
        switch (error) {
            case CLIENT_ERROR:
                eventView.showErrorMessage(getActivity().getResources().getString(R.string.some_error));
                break;
            case RESOURCE_ACCESS_ERROR:
                eventView.showErrorMessage("Fant ikke ressurs. Prøv igjen");
                break;
            case UNAUTHORIZED:
                checkAuth();
                break;
            case Constants.SOME_ERROR:
                this.eventView.showErrorMessage(getActivity().getResources().getString(R.string.some_error));
                break;
        }
    }

    @Override
    public void initGui() {
        this.eventView.initGui(event);
    }

    @Override
    public void setEventAttributes() {

        String participants = "";

        int NoOfParticipants = this.event.getUsers().size();
        if (NoOfParticipants == 1){
            participants = "1 DELTAKER";
        }
        else{
            participants = NoOfParticipants + " DELTAKERE";
        }


        this.eventView.setEventAttributes(event.getName(), event.getLocation(), event.getDescription(), event.getAdmin().getFirstname() + " " + event.getAdmin().getLastname(), DateUtility.formatFull(this.event.getStartDate().getTime()),
                participants);
        if (this.event.getEndDate() != null) {
            this.eventView.updateEndTime(DateUtility.formatFull(this.event.getEndDate().getTime()));
        }

        if (!this.event.getImageUri().isEmpty()) {
            eventView.setImage(event.getImageUri());
        }

        User currentUser = CurrentUser.getInstance().getUser();
        boolean userInEvent = false;

        if (this.event.getStartDate().after(new GregorianCalendar())){
            for (User user : this.event.getUsers()) {
                if (currentUser.getId() == user.getId()) {
                    userInEvent = true;
                    this.eventView.setUnAttendButton();
                    break;
                }
            }
            if (!userInEvent) {
                this.eventView.setAttendButton();
            }
        }

        if (currentUser.getId() == this.event.getAdmin().getId() && this.event.getStartDate().after(new GregorianCalendar())){
            this.eventView.setEditButton();
            this.eventView.setDeleteButton();
        }

        eventView.setDifficultyView(event.getDifficulty());

    }

    @Override
    public void updateView() {
        User currentUser = CurrentUser.getInstance().getUser();
        boolean userInEvent = false;

        for (User user : this.event.getUsers()) {
            if (currentUser.getId() == user.getId()) {
                userInEvent = true;
                this.eventView.setUnAttendButton();
                break;
            }
        }
        if (!userInEvent) {
            this.eventView.setAttendButton();
        }

        String participants = "";

        int NoOfParticipants = this.event.getUsers().size();
        if (NoOfParticipants == 1){
            participants = "1 DELTAKER";
        }
        else{
            participants = NoOfParticipants + " DELTAKERE";
        }

        this.eventView.setParticipants(participants);
    }

    @Override
    public void navigateToParticipants() {
        this.eventView.navigateToParticipants(this.event.getUsers());
    }

    @Override
    public void navigateToEditEvent() {
        this.eventView.navigateToEditEvent(this.event);
    }

    @Override
    public void deleteEvent() {
        this.eventView.showProgress();
        this.interactor.deleteEvent(this.event.getId());
    }

    @Override
    public void deleteSuccess() {
        this.eventView.hideProgress();
        this.eventView.navigateToMain();
    }

    @Override
    public void deleteError(int error) {
        this.eventView.hideProgress();
        switch (error) {
            case CLIENT_ERROR:
                eventView.showErrorMessage(getActivity().getResources().getString(R.string.some_error));
                break;
            case RESOURCE_ACCESS_ERROR:
                eventView.showErrorMessage(getActivity().getResources().getString(R.string.resource_access_error));
                break;
            case UNAUTHORIZED:
                checkAuth();
                break;
            case Constants.SOME_ERROR:
                this.eventView.showErrorMessage(getActivity().getResources().getString(R.string.some_error));
                break;
        }
    }

    /*
     * Comment methods
     */
    @Override
    public void comment(Post post, String message) {
        postInteractor.comment(post, message);
    }

    @Override
    public void onSuccessComment(Post post, Comment comment) {
        for(Post thisPost: event.getPosts()) {
            if(thisPost.getId() == post.getId()) {
                thisPost.getComments().add(comment);
                break;
            }
        }
        eventView.addComment(post, comment);
        eventView.commentFinishedSuccessfully();
    }

    @Override
    public void onFailureComment(int code) {
        eventView.showErrorMessage("En feil skjedde. Prøv igjen");
        eventView.commentFinishedWithError();
    }


    /*
     * Like methods
     */
    @Override
    public void likePost(int postId) {
        postInteractor.likePost(postId);
    }

    @Override
    public void likeComment(int commentId) {
        postInteractor.likeComment(commentId);
    }

    @Override
    public void unlikePost(int postId) {
        postInteractor.unlikePost(postId);
    }

    @Override
    public void unlikeComment(int commentId) {
        postInteractor.unlikeComment(commentId);
    }

    /*
         * Interactor listener methods
         */
    @Override
    public void onSuccessPostLike(int id) {
        eventView.updatePostLike(id, true);
    }

    @Override
    public void onFailurePostLike(int id) {
        eventView.showErrorMessage("Error while liking post");
    }

    @Override
    public void onSuccessCommentLike(int id) {
        eventView.updateCommentLike(id, true);
    }

    @Override
    public void onFailureCommentLike(int id) {
        eventView.showErrorMessage("Error while liking comment");
    }

    @Override
    public void onSuccessPostUnlike(int id) {
        eventView.updatePostLike(id, false);
    }

    @Override
    public void onFailurePostUnlike(int id) {
        eventView.showErrorMessage("Error while unliking post");
    }

    @Override
    public void onSuccessCommentUnlike(int id) {
        eventView.updateCommentLike(id, false);
    }

    @Override
    public void onFailureCommentUnlike(int id) {
        eventView.showErrorMessage("Error while unliking comment");
    }

    @Override
    public void postSuccess(Post post) {
        event.getPosts().add(post);

        Set<Post> postsToAdd = new HashSet<>();
        postsToAdd.add(post);
        eventView.addPosts(postsToAdd, false);
        eventView.postFinishedSuccessfully();
    }

    @Override
    public void postFailure(int code) {
        eventView.showErrorMessage("En feil skjedde. Prøv igjen.");
        eventView.postFinishedWithError();
    }

    @Override
    public void navigateToUser(User user) {
        //if friend or self
        if (CurrentUser.getInstance().getUser().isFriendWith(user) ||
                CurrentUser.getInstance().getUser().equals(user)) {
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            intent.putExtra("user", user.getId());
            getActivity().startActivity(intent);
        }
        //or if not friend
        else {
            Intent intent = new Intent(getActivity(), ProfileOthersActivity.class);
            intent.putExtra("user", user);
            getActivity().startActivity(intent);
        }
    }
}
