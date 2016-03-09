package com.andreasogeirik.master_frontend.application.user.photo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.main.MainPageActivity;
import com.andreasogeirik.master_frontend.application.user.photo.interfaces.PhotoPresenter;
import com.andreasogeirik.master_frontend.application.user.photo.interfaces.PhotoView;
import com.desmond.squarecamera.CameraActivity;
import com.soundcloud.android.crop.Crop;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhotoActivity extends AppCompatActivity implements PhotoView {

    @Bind(R.id.profile_pic)
    Button profilePic;
    @Bind(R.id.submit)
    Button done;
    @Bind(R.id.my_profile_image)
    ImageView profilePicView;

    private int PICK_IMAGE_REQUEST = 1;
    static final int REQUEST_IMAGE_CAPTURE = 2;
    private byte[] byteImage;

    PhotoPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_activity);
        ButterKnife.bind(this);
        this.presenter = new PhotoPresenterImpl(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Image picker
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                beginCrop(data.getData());
            } else {
                setImageError("Kunne ikke finne det valgte bildet");
            }
        }
        // Image cropper
        else if (requestCode == Crop.REQUEST_CROP) {
            if (data != null){
                sampleImage(Crop.getOutput(data));
            }
        }
        // Image capture
        else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Uri photoUri = data.getData();
            try {
                InputStream stream = getContentResolver().openInputStream(photoUri);
                this.byteImage = IOUtils.toByteArray(stream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.profilePicView.setImageURI(photoUri);
        }
    }

    @OnClick(R.id.profile_pic)
    public void selectImage() {
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
                        newImage();
                        return true;
                }
                return false;
            }
        });
        popup.show();
    }

    @OnClick(R.id.submit)
    public void submit(){
        if (byteImage != null){
            this.presenter.uploadPhoto(byteImage);
        }
        Intent intent = new Intent(this, MainPageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void existingImage() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        startActivityForResult(i, PICK_IMAGE_REQUEST);
    }

    private void newImage() {
        Intent startCustomCameraIntent = new Intent(this, CameraActivity.class);
        startActivityForResult(startCustomCameraIntent, REQUEST_IMAGE_CAPTURE);
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
    public void setImageError(String error) {

    }

    @Override
    public void setImage(byte[] byteImage, Bitmap bitmap) {
        this.byteImage = byteImage;
        this.profilePicView.setImageBitmap(bitmap);
    }
}
