package com.andreasogeirik.master_frontend.application.user.edit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.user.edit.fragments.EditPasswordDialogFragment;
import com.andreasogeirik.master_frontend.application.user.edit.interfaces.EditUserPresenter;
import com.andreasogeirik.master_frontend.application.user.edit.interfaces.EditUserView;
import com.andreasogeirik.master_frontend.application.user.profile.ProfileActivity;
import com.andreasogeirik.master_frontend.application.user.profile.ProfilePresenterImpl;
import com.andreasogeirik.master_frontend.layout.ProgressBarManager;
import com.andreasogeirik.master_frontend.model.User;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Andreas on 07.04.2016.
 */
public class EditUserActivity extends AppCompatActivity implements EditUserView {

    // Toolbarimport android.app.FragmentTransaction;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.firstname)
    EditText firstname;
    @Bind(R.id.lastname)
    EditText lastname;
    @Bind(R.id.location)
    EditText location;
    @Bind(R.id.home)
    Button homeBtn;
    @Bind(R.id.edit_password_btn)
    RelativeLayout editPasswordBtn;
    @Bind(R.id.error)
    TextView error;
    @Bind(R.id.submit)
    Button submit;


    EditUserPresenter presenter;
    private ProgressBarManager progressBarManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user_activity);

        ButterKnife.bind(this);
        setupToolbar();
        try {
            this.presenter = new EditUserPresenterImpl(this);
            this.presenter.setUserAttributes();
        } catch (ClassCastException e) {
            throw new ClassCastException(e + "/nObject in Intent bundle cannot " +
                    "be cast to User in " + this.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!com.andreasogeirik.master_frontend.layout.Toolbar.onOptionsItemSelected(item, this)) {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @OnClick(R.id.edit_password_btn)
    public void editPassword() {
        showPasswordCenter();
    }

    @OnClick(R.id.submit)
    public void submit(){
        this.error.setVisibility(View.GONE);
        String firstname = this.firstname.getText().toString();
        String lastname = this.lastname.getText().toString();
        String location = this.location.getText().toString();

        this.presenter.updateUser(firstname, lastname, location);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public void showPasswordCenter() {
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().
                beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("passwordDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        DialogFragment newFragment = EditPasswordDialogFragment.newInstance();
        newFragment.show(ft, "passwordDialog");

    }

    @Override
    public void setUserAttributes(String firstname, String lastname, String location) {
        this.firstname.setText(firstname);
        this.lastname.setText(lastname);
        this.location.setText(location);
    }

    @Override
    public void naviagteToProfileView(int userId) {
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("user", userId);

        startActivity(i);
    }

    @Override
    public void displayUpdateError(String message) {
        this.error.setText(message);
        this.error.setVisibility(View.VISIBLE);
    }

    @Override
    public void firstnameError(String message) {
        firstname.setError(message);
        firstname.requestFocus();
    }

    @Override
    public void lastnameError(String message) {
        lastname.setError(message);
        lastname.requestFocus();
    }

    @Override
    public void locationError(String message) {
        location.setError(message);
        location.requestFocus();
    }
}
