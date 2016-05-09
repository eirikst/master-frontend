package com.andreasogeirik.master_frontend.application.user.photo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.main.MainPageActivity;
import com.andreasogeirik.master_frontend.application.user.edit.EditUserActivity;
import com.andreasogeirik.master_frontend.application.user.photo.interfaces.PhotoPresenter;
import com.andreasogeirik.master_frontend.application.user.photo.interfaces.PhotoView;
import com.andreasogeirik.master_frontend.layout.ProgressBarManager;
import com.desmond.squarecamera.CameraActivity;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhotoActivity extends AppCompatActivity implements PhotoView {

    @Bind(R.id.container)
    View container;
    @Bind(R.id.error)
    TextView error;
    @Bind(R.id.submit)
    AppCompatButton done;
    @Bind(R.id.my_profile_image)
    ImageView profilePicView;

    private int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_CAMERA = 0;
    private static final int REQUEST_CAMERA_PERMISSION = 1;

    PhotoPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_activity);
        ButterKnife.bind(this);
        this.presenter = new PhotoPresenterImpl(this);
        ColorStateList csl = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.teal));
        done.setSupportBackgroundTintList(csl);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Image picker
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                beginCrop(data.getData());
            } else {
                imageError("Kunne ikke finne det valgte bildet");
            }
        }
        // Image cropper
        else if (requestCode == Crop.REQUEST_CROP) {
            if (data != null){
                prepareToSample(Crop.getOutput(data));
            }
        }
        // Image capture
        else if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            Uri photoUri = data.getData();
            prepareToSample(photoUri);
        }
    }

    @OnClick(R.id.my_profile_image)
    public void selectImage() {
        PopupMenu popup = new PopupMenu(this, profilePicView);
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
    }

    // Check for camera permission in MashMallow
    public void requestForCameraPermission() {
        final String permission = Manifest.permission.CAMERA;
        if (ContextCompat.checkSelfPermission(PhotoActivity.this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(PhotoActivity.this, permission)) {
                showPermissionRationaleDialog("Vi trenger tilgang til kamera for å legge til bilde", permission);
            } else {
                requestForPermission(permission);
            }
        } else {
            launch();
        }
    }

    private void launch() {
        Intent startCustomCameraIntent = new Intent(this, CameraActivity.class);
        startActivityForResult(startCustomCameraIntent, REQUEST_CAMERA);
    }

    private void requestForPermission(final String permission) {
        ActivityCompat.requestPermissions(PhotoActivity.this, new String[]{permission}, REQUEST_CAMERA_PERMISSION);
    }

    private void showPermissionRationaleDialog(final String message, final String permission) {
        new AlertDialog.Builder(PhotoActivity.this)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PhotoActivity.this.requestForPermission(permission);
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

    @OnClick(R.id.submit)
    public void submit(){
        this.presenter.submit();
    }

    private void existingImage() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        startActivityForResult(i, PICK_IMAGE_REQUEST);
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void sampleImage(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            this.presenter.samplePhoto(inputStream);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void navigateToMainView() {
        Intent intent = new Intent(this, MainPageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void imageError(String error) {
        Toast.makeText(PhotoActivity.this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setImage(Bitmap bitmap) {
        this.profilePicView.setImageBitmap(bitmap);
    }

    @Override
    public void sendMessage(String message) {
        Intent intent = new Intent("sendPhotoMessage");
        intent.putExtra("photoMessage", message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void updateError(String error) {
        this.error.setText(error);
        this.error.setVisibility(View.VISIBLE);
    }

    private void prepareToSample(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            this.presenter.samplePhoto(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
