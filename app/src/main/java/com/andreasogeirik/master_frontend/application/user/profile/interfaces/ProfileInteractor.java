package com.andreasogeirik.master_frontend.application.user.profile.interfaces;

import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.model.User;

import java.io.File;
import java.io.InputStream;
import java.util.Set;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public interface ProfileInteractor {
    void findPosts(User user, int start);
    void findFriends(int userId);
    void findImage(String imageUri, File storagePath);

    void findAttendingEvents(User user);

    void updateProfileImage(byte[] byteImage);
    void sampleImage(InputStream inputStream);
}
