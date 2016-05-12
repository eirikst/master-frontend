package com.andreasogeirik.master_frontend.application.event.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.event.edit.EditEventActivity;
import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventPresenter;
import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventView;
import com.andreasogeirik.master_frontend.application.event.main.participants.ParticipantsActivity;
import com.andreasogeirik.master_frontend.application.main.MainPageActivity;
import com.andreasogeirik.master_frontend.application.post.CommentDialog;
import com.andreasogeirik.master_frontend.application.post.LikeDialogFragment;
import com.andreasogeirik.master_frontend.application.post.PostDialog;
import com.andreasogeirik.master_frontend.application.user.friend.friend_list_widget.UserGridFragment;
import com.andreasogeirik.master_frontend.layout.ProgressBarManager;
import com.andreasogeirik.master_frontend.layout.adapter.PostListAdapter;
import com.andreasogeirik.master_frontend.layout.model_wrapper.PostListElement;
import com.andreasogeirik.master_frontend.listener.OnUserClickListener;
import com.andreasogeirik.master_frontend.model.ActivityType;
import com.andreasogeirik.master_frontend.model.Comment;
import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.model.Post;
import com.andreasogeirik.master_frontend.util.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventActivity extends AppCompatActivity implements EventView, OnClickListener,
        PostListAdapter.PostListCallback, CommentDialog.Listener, PostDialog.Listener, UserGridFragment.OnFragmentInteractionListener {

    private CommentDialog commentFragment;
    private PostDialog postDialog;

    // Containers
    @Bind(R.id.event_progress)
    View progressView;

    @Bind(R.id.event_list_view)
    ListView listView;

    private Button loadPostsButton;

    private ImageView imageView;

    private PostListAdapter adapter;


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.home)
    Button homeBtn;

    private TextView noPosts;
    private View easyDiff;
    private View mediumDiff;
    private View hardDiff;

    private View headerView;
    private AppCompatButton attendButton;
    private AppCompatButton unAttendButton;
    private AppCompatButton editButton;
    private AppCompatButton cancelButton;

    private TextView eventName;
    private TextView startTime;
    private View endTimePanel;
    private TextView endTime;
    private TextView eventLocation;
    private TextView eventDescription;
    private TextView eventAdmin;
    private RelativeLayout adminPanel;

    private ImageView activityTypeIcon;
    private TextView activityTypeLabel;

    private View newPostBtn;
    private TextView newPostText;

    private TextView numberOfParticipants;


    private EventPresenter presenter;
    private ProgressBarManager progressBarManager;

    private static int EDIT_EVENT_REQUEST = 1;
    ColorStateList teal;
    ColorStateList grey;
    ColorStateList red;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_activity);
        ButterKnife.bind(this);
        this.progressBarManager = new ProgressBarManager(this, listView, progressView);
        setupToolbar();
        teal = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.teal));
        red = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.app_red));
        grey = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey_fade));

        Intent intent = getIntent();
        try {
            Object object = getIntent().getSerializableExtra("event");
            if(object != null) {
                this.presenter = new EventPresenterImpl(this, (Event) object,
                        intent.getIntExtra("post", 0), intent.getIntExtra("comment", 0));
            }
            else {
                int eventId = getIntent().getIntExtra("eventId", -1);
                if(eventId == -1) {
                    throw new RuntimeException("Neither event nor eventId put in intent");
                }
                this.presenter = new EventPresenterImpl(this, eventId,
                        intent.getIntExtra("post", 0), intent.getIntExtra("comment", 0));

            }
        } catch (ClassCastException e) {
            throw new ClassCastException(e + "/nObject in Intent bundle cannot " +
                    "be cast to User in " + this.toString());
        }
    }

    /*
     * Toolbar setup
     */
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @OnClick(R.id.home)
    public void navigateToHome() {
        Intent intent = new Intent(this, MainPageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void setEventAttributes(String name, String location, String description, String admin, String startTime, String participants, ActivityType activityType, Collection<User> participantCollection) {

        this.unAttendButton.setVisibility(View.GONE);
        this.attendButton.setVisibility(View.GONE);

        this.eventName.setText(name);
        this.eventLocation.append(location);
        this.eventDescription.append(description);
        this.eventAdmin.setPaintFlags(this.eventAdmin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        this.eventAdmin.append(admin);
        this.startTime.append(startTime);

        this.numberOfParticipants.setText(participants);
        this.numberOfParticipants.setPaintFlags(this.numberOfParticipants.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            switch (activityType) {
            case WALK:
                this.activityTypeIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_directions_walk_red_400_24dp));
                this.activityTypeLabel.append("GÅ");
                break;
            case RUN:
                this.activityTypeIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_directions_run_orange_300_24dp));
                this.activityTypeLabel.append("LØPE");
                break;
            case BIKE:
                this.activityTypeIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_directions_bike_blue_24dp));
                this.activityTypeLabel.append("SYKLE");
                break;
            case SKI:
                this.activityTypeIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_ski_510));
                this.activityTypeLabel.append("SKI");
                break;
            case SWIM:
                this.activityTypeIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_swim_226));
                this.activityTypeLabel.append("SVØMME");
                break;
            case GROUP_WORKOUT:
                this.activityTypeIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_gruppetrening_24dp));
                this.activityTypeLabel.append("GRUPPETRENING");
                break;
            case OTHER:
                this.activityTypeIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_question_24dp));
                this.activityTypeLabel.append("ANNET");
                break;
        }

        ((UserGridFragment)getSupportFragmentManager().findFragmentById(R.id.participants_fragment)).setList(new ArrayList<User>(participantCollection));
    }

    @Override
    public void updateEndTime(String endTime) {
        this.endTime.append(endTime);
        this.endTimePanel.setVisibility(View.VISIBLE);
    }

    @Override
    public void setParticipants(Set<User> users) {
        String participants = "";

        if (users.size() == 1){
            participants = "1 DELTAKER";
        }
        else{
            participants = users.size() + " DELTAKERE";
        }

        this.numberOfParticipants.setText(participants);
        ((UserGridFragment)getSupportFragmentManager().findFragmentById(R.id.participants_fragment)).setList(new ArrayList<User>(users));
    }

    @Override
    public void setAttendButton() {
        this.attendButton.setVisibility(View.VISIBLE);
        this.unAttendButton.setVisibility(View.GONE);
    }

    @Override
    public void setEditButton() {
        this.editButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void setCancelButton() {
        this.cancelButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void setUnAttendButton() {
        this.unAttendButton.setVisibility(View.VISIBLE);
        this.attendButton.setVisibility(View.GONE);
    }

    @Override
    public void setCanceled() {
        this.attendButton.setVisibility(View.GONE);
        this.unAttendButton.setVisibility(View.GONE);
        this.cancelButton.setVisibility(View.GONE);
        this.editButton.setVisibility(View.GONE);

        findViewById(R.id.event_cancel_text).setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorMessage(String error) {
        Toast.makeText(EventActivity.this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        progressBarManager.showProgress(true);
    }

    @Override
    public void hideProgress() {
        progressBarManager.showProgress(false);
    }

    @Override
    public void setImage(String imageUri) {
        if(imageUri != null && !imageUri.isEmpty()) {
            Picasso.with(this)
                    .load(imageUri)
                    .error(R.drawable.default_event)
                    .into(imageView);
        }
        else {
            Picasso.with(this)
                    .load(R.drawable.default_event)
                    .into(imageView);
        }
        this.imageView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setDifficultyView(int difficulty) {
        if(difficulty == Constants.EVENT_DIFFICULTY_MEDIUM) {
            easyDiff.setVisibility(View.GONE);
            mediumDiff.setVisibility(View.VISIBLE);
            hardDiff.setVisibility(View.GONE);

        }
        else if(difficulty == Constants.EVENT_DIFFICULTY_HARD) {
            easyDiff.setVisibility(View.GONE);
            mediumDiff.setVisibility(View.GONE);
            hardDiff.setVisibility(View.VISIBLE);

        }
        else {
            easyDiff.setVisibility(View.VISIBLE);
            mediumDiff.setVisibility(View.GONE);
            hardDiff.setVisibility(View.GONE);
        }
    }

    @Override
    public void initGui(final Event event) {
        headerView = getLayoutInflater().inflate(R.layout.event_list_header, null);
        listView.addHeaderView(headerView);

        this.numberOfParticipants = (TextView) headerView.findViewById(R.id.event_participants);

        this.attendButton = (AppCompatButton) headerView.findViewById(R.id.event_attend);
        attendButton.setSupportBackgroundTintList(teal);

        this.unAttendButton = (AppCompatButton) headerView.findViewById(R.id.event_unattend);
        unAttendButton.setSupportBackgroundTintList(grey);

        this.editButton = (AppCompatButton) headerView.findViewById(R.id.event_edit);
        editButton.setSupportBackgroundTintList(grey);

        this.cancelButton = (AppCompatButton) headerView.findViewById(R.id.event_cancel);
        cancelButton.setSupportBackgroundTintList(red);

        this.numberOfParticipants.setOnClickListener(this);
        this.attendButton.setOnClickListener(this);
        this.unAttendButton.setOnClickListener(this);
        this.editButton.setOnClickListener(this);
        this.cancelButton.setOnClickListener(this);

        this.activityTypeIcon = (ImageView) headerView.findViewById(R.id.event_activity_type_symbol);
        this.activityTypeLabel = (TextView) headerView.findViewById(R.id.event_activity_type_label);

        this.imageView = (ImageView) headerView.findViewById(R.id.event_image);
        this.eventName = (TextView) headerView.findViewById(R.id.event_name);
        this.startTime = (TextView) headerView.findViewById(R.id.event_startTime);
        this.endTime = (TextView) headerView.findViewById(R.id.event_end_time);
        this.endTimePanel = headerView.findViewById(R.id.event_end_time_panel);
        this.eventLocation = (TextView) headerView.findViewById(R.id.event_location);
        this.eventDescription = (TextView) headerView.findViewById(R.id.event_description);
        this.eventAdmin = (TextView) headerView.findViewById(R.id.event_admin);
        this.adminPanel = (RelativeLayout) headerView.findViewById(R.id.event_admin_panel);

        this.easyDiff = headerView.findViewById(R.id.difficulty_easy);
        this.mediumDiff = headerView.findViewById(R.id.difficulty_medium);
        this.hardDiff = headerView.findViewById(R.id.difficulty_hard);

        noPosts = (TextView)headerView.findViewById(R.id.no_posts);
        newPostBtn = headerView.findViewById(R.id.new_post_event);
        newPostText = (TextView)headerView.findViewById(R.id.new_post);
        newPostText.setPaintFlags(newPostText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        adapter = new PostListAdapter(this, new ArrayList<Post>(), this);
        listView.setAdapter(adapter);

        loadPostsButton = (Button)getLayoutInflater().inflate(R.layout.post_list_footer, null);
        listView.addFooterView(loadPostsButton);

        loadPostsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.findPosts();
            }
        });

        newPostBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // DialogFragment.show() will take care of adding the fragment
                // in a transaction.  We also want to remove any currently showing
                // dialog, so make our own transaction and take care of that here.
                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager()
                        .beginTransaction();
                Fragment prev = getSupportFragmentManager().findFragmentByTag("postDialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                // Create and show the dialog.
                postDialog = PostDialog.newInstance();
                postDialog.show(ft, "postDialog");
            }
        });

        //listen to click on user
        OnUserClickListener userClickListener = new OnUserClickListener(new OnUserClickListener.Listener() {
            @Override
            public void userClicked(User user) {
                presenter.navigateToUser(user);
            }
        }, event.getAdmin());
        adminPanel.setOnClickListener(userClickListener);
    }

    @Override
    public void navigateToEditEvent(Event event) {
        Intent i = new Intent(this, EditEventActivity.class);
        i.putExtra("event", event);
        startActivity(i);
    }

    @Override
    public void navigateToMain() {
        Intent i = new Intent(this, MainPageActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    @Override
    public void navigateToParticipants(Set<User> _users) {
        Intent i = new Intent(this, ParticipantsActivity.class);
        HashSet<User> users = new HashSet(_users);
        i.putExtra("users", users);
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!com.andreasogeirik.master_frontend.layout.Toolbar.onOptionsItemSelected(item, this)) {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onBackPressed() {

        Intent i = getIntent();
        int requestCode = i.getIntExtra("requestCode", 0);
        if (requestCode == EDIT_EVENT_REQUEST) {
            Intent intent = new Intent(this, MainPageActivity.class);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.event_cancel:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

                // set dialog message
                alertDialogBuilder
                        .setMessage("Vil du avlyse denne aktiviteten?")
                        .setCancelable(false)
                        .setPositiveButton("Nei", new DialogInterface.OnClickListener() {//this is really negative, wanted to change sides
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Ja", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {//this is really positive, wanted to change sides
                                // if this button is clicked, close
                                // current activity
                                presenter.cancelEvent();
                            }
                        });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
                break;
            case R.id.event_edit:
                presenter.navigateToEditEvent();
                break;
            case R.id.event_participants:
                presenter.navigateToParticipants();
                break;
            case R.id.event_attend:
                presenter.attendEvent();
                break;
            case R.id.event_unattend:
                presenter.unAttendEvent();
                break;
        }
    }

    @Override
    public void addPosts(Collection<Post> posts, boolean lastPosts) {
        adapter.addPosts(posts);
        if(lastPosts) {
            loadPostsButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void noPostsToShow() {
        noPosts.setVisibility(View.VISIBLE);
    }


    @Override
    public void likeComment(int commentId) {
        presenter.likeComment(commentId);
    }

    @Override
    public void likePost(int postId) {
        presenter.likePost(postId);
    }

    @Override
    public void unlikeComment(int commentId) {
        presenter.unlikeComment(commentId);
    }

    @Override
    public void unlikePost(int postId) {
        presenter.unlikePost(postId);
    }

    @Override
    public void showComment(Post post) {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager()
                .beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("commentDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        commentFragment = CommentDialog.newInstance(post);
        commentFragment.show(ft, "commentDialog");

    }

    @Override
    public void updatePostLike(int id, boolean like) {
        adapter.updatePost(id, like);
    }

    @Override
    public void updateCommentLike(int id, boolean like) {
        adapter.updateComment(id, like);
    }

    @Override
    public void addComment(Post post, Comment comment) {
        adapter.addComment(post, comment);
    }

    @Override
    public void commentFinishedSuccessfully() {
        commentFragment.dismiss();
    }

    @Override
    public void commentFinishedWithError() {
        commentFragment.commentButtonEnable(true);
    }

    /*
     * CommentDialog
    */
    @Override
    public void comment(Post post, String message) {
        presenter.comment(post, message);
    }

    @Override
    public void post(String msg) {
        presenter.post(msg);
    }

    /*
     * PostDialog
    */
    @Override
    public void postFinishedSuccessfully() {
        postDialog.dismiss();
    }

    @Override
    public void postFinishedWithError() {
        postDialog.postButtonEnable(true);
    }

    @Override
    public void navigateToUser(User user) {
        presenter.navigateToUser(user);
    }

    @Override
    public void navigateToLikers(Set<User> likers) {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager()
                .beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("likerDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = LikeDialogFragment.newInstance(
                new HashSet<User>(likers));
        newFragment.show(ft, "likerDialog");
    }

    @Override
    public void focusPost(int postId) {
        PostListElement element = adapter.getPost(postId);

        if(element != null) {
            int position = adapter.getPosition(element);

            if(position != -1) {
                listView.setSelection(position+1);//+1 because of header
            }
        }
    }

    @Override
    public void focusComment(int commentId) {
        PostListElement element = adapter.getComment(commentId);

        if(element != null) {
            int position = adapter.getPosition(element);

            if(position != -1) {
                listView.setSelection(position+1);//+1 because of header
            }
        }
    }
}
