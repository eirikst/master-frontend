package com.andreasogeirik.master_frontend.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.andreasogeirik.master_frontend.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

public class RegistrationIntentService extends IntentService {
    private String token;

    public RegistrationIntentService() {
        super("Intent service");
    }

    public RegistrationIntentService(String name) {
        super(name);
    }

    @Override
    public void onHandleIntent(Intent intent) {
        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            GcmTokenSingleton.getInstance().setToken(token);
        }
        catch(IOException e) {
            Log.w(getClass().getSimpleName(), "Exception onHandleIntent");
        }
    }
}