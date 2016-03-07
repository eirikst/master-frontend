package com.andreasogeirik.master_frontend.application.auth.register;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.auth.register.camera.CameraActivity;
import com.andreasogeirik.master_frontend.application.auth.register.interfaces.RegisterPresenter;
import com.andreasogeirik.master_frontend.application.auth.register.interfaces.RegisterView;
import com.andreasogeirik.master_frontend.application.auth.welcome.WelcomeActivity;
import com.andreasogeirik.master_frontend.layout.ProgressBarManager;
import com.andreasogeirik.master_frontend.model.User;
import com.soundcloud.android.crop.Crop;


import org.apache.commons.lang3.RandomStringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity implements RegisterView {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.image_view)
    ImageView imageView;
    @Bind(R.id.register_image)
    Button registerImage;
    @Bind(R.id.email_register)
    EditText emailView;
    @Bind(R.id.password_register)
    EditText passwordView;
    @Bind(R.id.re_password_register)
    EditText rePasswordView;
    @Bind(R.id.firstname)
    EditText firstnameView;
    @Bind(R.id.lastname)
    EditText lastnameView;
    @Bind(R.id.location)
    EditText locationView;
    @Bind(R.id.register_error)
    TextView errorMessage;

    @Bind(R.id.register_form)
    View registerFormView;

    @Bind(R.id.register_progress)
    View progressView;

    @Bind(R.id.register_button)
    Button mRegister_button;

    private RegisterPresenter presenter;
    private ProgressBarManager progressBarManager;
    private int PICK_IMAGE_REQUEST = 1;
    static final int REQUEST_IMAGE_CAPTURE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        ButterKnife.bind(this);
        this.presenter = new RegisterPresenterImpl(this);
        this.progressBarManager = new ProgressBarManager(this, registerFormView, progressView);
        setupToolbar();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        int a = 0;

        // Checks if the returned result comes from the image picker
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                beginCrop(data.getData());
            } else {
                setImageError("Kunne ikke finne det valgte bildet");
            }
        }
        // Checks if the returned result comes from the image cropper
        else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }
        // Checks if the returned result comes from an image capture
        else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");

            saveImage(bitmap);



//            String stringImageURI = data.getStringExtra("image");
//            Uri uri = Uri.parse(stringImageURI);
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
//                setImage(bitmap);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    /*
     * Toolbar setup
     */
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @OnClick(R.id.register_image)
    public void selectImage() {
        PopupMenu popup = new PopupMenu(this, registerImage);
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

    @OnClick(R.id.register_button)
    public void onClick() {
        User user = new User();
        user.setEmail(emailView.getText().toString());
        user.setPassword(passwordView.getText().toString());
        String rePassword = rePasswordView.getText().toString();
        user.setFirstname(firstnameView.getText().toString());
        user.setLastname(lastnameView.getText().toString());
        user.setLocation(locationView.getText().toString());

        presenter.registerUser(user, rePassword);
    }

    @Override
    public void navigateToWelcomeView() {
        Intent i = new Intent(this, WelcomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    @Override
    public void registrationFailed(String error) {
        errorMessage.setText(error);
        errorMessage.setVisibility(View.VISIBLE);
        errorMessage.requestFocus();
    }

    @Override
    public void showProgress() {
        this.progressBarManager.showProgress(true);
    }

    @Override
    public void hideProgress() {
        this.progressBarManager.showProgress(false);
    }

    @Override
    public void setImage(Bitmap image) {
        this.imageView.setImageBitmap(image);
        this.imageView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setEmailError(String error) {
        emailView.setError(error);
        View focusView = emailView;
        focusView.requestFocus();
    }

    @Override
    public void setPasswordError(String error) {
        passwordView.setError(error);
        View focusView = passwordView;
        focusView.requestFocus();
    }

    @Override
    public void setFirstnameError(String error) {
        firstnameView.setError(error);
        View focusView = firstnameView;
        focusView.requestFocus();
    }

    @Override
    public void setLastnameError(String error) {
        lastnameView.setError(error);
        View focusView = lastnameView;
        focusView.requestFocus();
    }

    @Override
    public void setLocationError(String error) {
        locationView.setError(error);
        View focusView = locationView;
        focusView.requestFocus();
    }

    // TODO FIX thIS
    public void setImageError(String error){

    }

    private void existingImage() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        startActivityForResult(i, PICK_IMAGE_REQUEST);
    }

    private void newImage() {
//        Intent i = new Intent(this, CameraActivity.class);
//        startActivityForResult(i, REQUEST_IMAGE_CAPTURE);
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra("android.intent.extras.CAMERA_FACING", 1);
        if (i.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(i, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            this.imageView.setImageDrawable(null);
            this.imageView.setVisibility(View.GONE);
            this.imageView.setImageURI(Crop.getOutput(result));
            this.imageView.setVisibility(View.VISIBLE);
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImage(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteImage = stream.toByteArray();

        String randomFileName = RandomStringUtils.randomAlphanumeric(20);

        File storageDir = getAlbumStorageDir(getApplicationContext(), "tmp");

        File image = new File(storageDir, randomFileName + ".jpg");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(image);
            fileOutputStream.write(byteImage);
            fileOutputStream.close();
            beginCrop(Uri.parse(image.toURI().toString()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getAlbumStorageDir(Context context, String albumName) {
        // Get the directory for the app's private pictures directory.
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), albumName);
        return file;
    }
}
