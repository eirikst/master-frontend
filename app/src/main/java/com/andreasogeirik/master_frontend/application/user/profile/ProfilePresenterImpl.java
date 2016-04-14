package com.andreasogeirik.master_frontend.application.user.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.andreasogeirik.master_frontend.application.general.GeneralPresenter;
import com.andreasogeirik.master_frontend.application.post.PostInteractor;
import com.andreasogeirik.master_frontend.application.post.PostListInteractorImpl;
import com.andreasogeirik.master_frontend.application.user.friend.FriendListActivity;
import com.andreasogeirik.master_frontend.application.user.profile.interfaces.ProfileInteractor;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.model.Comment;
import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.model.Friendship;
import com.andreasogeirik.master_frontend.model.Post;
import com.andreasogeirik.master_frontend.application.user.profile.interfaces.ProfilePresenter;
import com.andreasogeirik.master_frontend.application.user.profile.interfaces.ProfileView;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public class ProfilePresenterImpl extends GeneralPresenter implements ProfilePresenter,
PostListInteractorImpl.Listener {
    private ProfileView view;
    private ProfileInteractor interactor;
    private PostInteractor postInteractor;
    boolean thisIsMyProfile;

    //model
    private User user;

    public ProfilePresenterImpl(ProfileView view, int userId) {
        super((Activity)view, CHECK_USER_AVAILABLE);

        this.user = new User(userId);
        this.view = view;
        this.interactor = new ProfileInteractorImpl(this);
        this.postInteractor = new PostListInteractorImpl(this);

        //init gui
        thisIsMyProfile = CurrentUser.getInstance().getUser().equals(user);
        view.initView();

        //init model
        findUser(userId);
        findPosts();//get first posts
        findFriends(this.user.getId());
        findAttendingEvents();
    }


    /*
     * Get user
     */
    @Override
    public void findUser(int userId) {
        interactor.findUser(userId);
    }

    @Override
    public void onSuccessUserLoad(User user) {
        this.user.copyUser(user);
        view.initUser(user, thisIsMyProfile);
        view.setProfileImage(user.getImageUri());
    }

    @Override
    public void onFailedUserLoad(int code) {
        if(code == Constants.UNAUTHORIZED) {
            checkAuth();
        }
        else {
            view.displayMessage("Feil ved lasting av bruker");
        }
    }

    /*
     * Handling set of posts
     */
    @Override
    public void findPosts() {
        interactor.findPosts(user, user.getPosts().size());
    }

    @Override
    public void successPostsLoad(Set<Post> posts) {
        user.addPosts(posts);
        view.addPosts(posts);
        if(posts.size() < Constants.NUMBER_OF_POSTS_RETURNED) {
            view.displayLoadPostsButton(false);
        }
    }

    @Override
    public void errorPostsLoad(int code) {
        if(code == Constants.UNAUTHORIZED) {
            checkAuth();
        }
        else {
            view.displayMessage("Feil ved lasting av poster");
            Log.w(getClass().getSimpleName(), "Error while loading posts. Code " + code);
        }
    }

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
        view.updatePostLike(id, true);
    }

    @Override
    public void onFailurePostLike(int id) {
        view.displayMessage("Error while liking post");
    }

    @Override
    public void onSuccessCommentLike(int id) {
        view.updateCommentLike(id, true);
    }

    @Override
    public void onFailureCommentLike(int id) {
        view.displayMessage("Error while liking comment");
    }

    @Override
    public void onSuccessPostUnlike(int id) {
        view.updatePostLike(id, false);
    }

    @Override
    public void onFailurePostUnlike(int id) {
        view.displayMessage("Error while unliking post");
    }

    @Override
    public void onSuccessCommentUnlike(int id) {
        view.updateCommentLike(id, false);
    }

    @Override
    public void onFailureCommentUnlike(int id) {
        view.displayMessage("Error while unliking comment");
    }


    @Override
    public void findAttendingEvents() {
        interactor.findAttendingEvents(user);
    }

    @Override
    public void successAttendingEvents(Set<Event> events) {
        if(events.size() == 0) {
            view.setEventButtonText("Se aktiviteter");
        }
        else if(events.size() == 1) {
            for(Event e: events) {
                view.setEventButtonText("Deltar på " + e.getName());
            }
        }
        else {
            view.setEventButtonText("Deltar på " + events.size() + " aktiviteter");
        }
    }

    @Override
    public void failureAttendingEvents(int code) {
        view.displayMessage("Error loading user's events");
    }

    /*
         * Handling set of friends
         */
    private void findFriends(int userId) {
        interactor.findFriends(userId);
    }

    @Override
    public void successFriendsLoad(Set<Friendship> friends) {
        user.addFriends(friends);
        view.setFriendCount(user.getFriends().size());
    }

    @Override
    public void errorFriendsLoad(int code) {
        if(code == Constants.UNAUTHORIZED) {
            checkAuth();
        }
        else {
            view.displayMessage("Feil ved lasting av venner");
        }
    }

    /*
     * Go to user's friend list view
     */
    @Override
    public void friendListSelected() {
        Intent intent = new Intent(getActivity(), FriendListActivity.class);
        intent.putExtra("friends", new ArrayList<Friendship>(user.getFriends()));
        getActivity().startActivity(intent);
    }

    /*
     * Save instance state
     */
    @Override
    public void saveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable("user", user);
    }

    @Override
    public void accessEvents() {
        Intent intent = new Intent(getActivity(), AttendingEventsActivity.class);
        intent.putExtra("user", user);
        getActivity().startActivity(intent);
    }

    @Override
    public void comment(Post post, String message) {
        postInteractor.comment(post, message);
    }

    @Override
    public void onSuccessComment(Post post, Comment comment) {
        for(Post thisPost: user.getPosts()) {
            if(thisPost.getId() == post.getId()) {
                thisPost.getComments().add(comment);
                break;
            }
        }
        view.addComment(post, comment);
        view.commentFinished();
    }

    @Override
    public void onFailureComment(int code) {
        view.displayMessage("En feil skjedde. Prøv igjen");
    }
}