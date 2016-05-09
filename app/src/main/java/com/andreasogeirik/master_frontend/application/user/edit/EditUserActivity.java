package com.andreasogeirik.master_frontend.application.user.edit;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.main.MainPageActivity;
import com.andreasogeirik.master_frontend.application.user.edit.fragments.EditPasswordDialogFragment;
import com.andreasogeirik.master_frontend.application.user.edit.interfaces.EditUserPresenter;
import com.andreasogeirik.master_frontend.application.user.edit.interfaces.EditUserView;
import com.andreasogeirik.master_frontend.application.user.profile.ProfileActivity;
import com.andreasogeirik.master_frontend.layout.ProgressBarManager;
import com.andreasogeirik.master_frontend.util.Constants;
import com.desmond.squarecamera.CameraActivity;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Andreas on 07.04.2016.
 */
public class EditUserActivity extends AppCompatActivity implements EditUserView, View.OnClickListener {

    // Toolbarimport android.app.FragmentTransaction;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.progress)
    ProgressBar progressBar;
    @Bind(R.id.attributes_container)
    View mainContainer;
    @Bind(R.id.profile_image)
    ImageView profilePic;
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
    AppCompatButton submit;


    private final int PICK_IMAGE_REQUEST = 1;
    private static int EDIT_EVENT_REQUEST = 1;
    private static final int REQUEST_CAMERA = 0;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int REQUEST_EXTERNAL_STORAGE_WRITE_PERMISSION = 2;
    private static final int REQUEST_EXTERNAL_STORAGE_READ_PERMISSION = 3;


    EditUserPresenter presenter;
    private ProgressBarManager progressBarManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user_activity);
        ButterKnife.bind(this);
        setupToolbar();
        ColorStateList csl = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.teal));
        submit.setSupportBackgroundTintList(csl);
        try {
            this.presenter = new EditUserPresenterImpl(this);
            this.presenter.setUserAttributes();
            this.progressBarManager = new ProgressBarManager(this, mainContainer, progressBar);
            this.profilePic.setOnClickListener(this);
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
    public void updateImage(Bitmap image) {
        this.profilePic.setImageBitmap(image);
    }

    @Override
    public void setUserAttributes(String firstname, String lastname, String location, String imageUri) {
        this.firstname.setText(firstname);
        this.lastname.setText(lastname);
        this.location.setText(location);
        setImage(imageUri);
    }

    public void setImage(String imageUri) {
        //load image
        if(imageUri != null && !imageUri.isEmpty()) {
            Picasso.with(this)
                    .load(imageUri)
                    .error(R.drawable.default_profile)
                    .resize(Constants.USER_IMAGE_WIDTH, Constants.USER_IMAGE_HEIGHT)
                    .centerCrop()
                    .into(profilePic);
        }
        else {
            Picasso.with(this)
                    .load(R.drawable.default_profile)
                    .resize(Constants.USER_IMAGE_WIDTH, Constants.USER_IMAGE_HEIGHT)
                    .centerCrop()
                    .into(profilePic);
        }
    }

    @Override
    public void naviagteToProfileView(int userId) {
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("user", userId);
        i.putExtra("requestCode", EDIT_EVENT_REQUEST);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
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

    @Override
    public void imageError(String message) {
        Toast.makeText(EditUserActivity.this, message, Toast.LENGTH_SHORT).show();
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_image:
                PopupMenu popup = new PopupMenu(this, profilePic);
                popup.getMenuInflater().inflate(R.menu.menu_register, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.select_image:
                                existingImage();
                                return true;
                            case R.id.capture_image:
                                requestForCameraPermission();
                                return true;
                        }
                        return false;
                    }
                });
                popup.show();
                break;
        }
    }

    private void existingImage() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        startActivityForResult(i, PICK_IMAGE_REQUEST);
    }

    // Check for camera permission in MashMallow
    public void requestForCameraPermission() {
        final String permission = Manifest.permission.CAMERA;
        if (ContextCompat.checkSelfPermission(EditUserActivity.this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(EditUserActivity.this, permission)) {
                showPermissionRationaleDialog("Vi trenger tilgang til kamera for å legge til bilde", permission);
            } else {
                requestForPermission(permission);
            }
        } else {
            launch();
        }
    }

    // Check for camera permission in MashMallow
    public void requestForStorageWritePermission() {
        final String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if (ContextCompat.checkSelfPermission(EditUserActivity.this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(EditUserActivity.this, permission)) {
                showPermissionRationaleDialog("Vi trenger tilgang til filsystemet for å legge til bilde", permission);
            } else {
                ActivityCompat.requestPermissions(EditUserActivity.this, new String[]{permission}, REQUEST_CAMERA_PERMISSION);
                requestForPermission(permission);
            }
        } else {
            launch();
        }
    }

    private void showPermissionRationaleDialog(final String message, final String permission) {
        new AlertDialog.Builder(EditUserActivity.this)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditUserActivity.this.requestForPermission(permission);
                    }
                })
                .setNegativeButton("Avbryt", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create()
                .show();
    }

    private void launch() {
        Intent startCustomCameraIntent = new Intent(this, CameraActivity.class);
        startActivityForResult(startCustomCameraIntent, REQUEST_CAMERA);
    }

    private void requestForPermission(final String permission) {
        ActivityCompat.requestPermissions(EditUserActivity.this, new String[]{permission}, REQUEST_CAMERA_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                final int numOfRequest = grantResults.length;
                final boolean isGranted = numOfRequest == 1
                        && PackageManager.PERMISSION_GRANTED == grantResults[numOfRequest - 1];
                if (isGranted) {
                    launch();
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    /**
     * Retrieves the result after selecting a new profile picture, either by camera, image picker or image cropper
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Image picker
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
                Crop.of(data.getData(), destination).asSquare().start(this);
            } else {
                Toast.makeText(EditUserActivity.this, "Kunne ikke finne det valgte bildet", Toast.LENGTH_SHORT).show();
            }
        }
        // Image cropper
        else if (requestCode == Crop.REQUEST_CROP) {
            if (data != null) {
                prepareToSample(Crop.getOutput(data));
            }
        }
        // Image capture
        else if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            Uri photoUri = data.getData();
            prepareToSample(photoUri);
        }
    }

    private void prepareToSample(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            this.presenter.sampleImage(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.home)
    public void navigateToHome() {
        Intent intent = new Intent(this, MainPageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
