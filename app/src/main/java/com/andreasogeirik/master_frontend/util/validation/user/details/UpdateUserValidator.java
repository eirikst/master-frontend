package com.andreasogeirik.master_frontend.util.validation.user.details;

import android.text.TextUtils;

/**
 * Created by Andreas on 08.04.2016.
 */
public class UpdateUserValidator {

    public static UpdateUserStatusCodeContainer validate(String firstname, String lastname, String location) {
        if (TextUtils.isEmpty(firstname)) {
            return new UpdateUserStatusCodeContainer(UpdateUserStatusCodes.FIRST_NAME_ERROR, "The first name cannot be empty");
        } else if (TextUtils.isEmpty(lastname)) {
            return new UpdateUserStatusCodeContainer(UpdateUserStatusCodes.LAST_NAME_ERROR, "The surname cannot be empty");
        } else if (TextUtils.isEmpty(location)) {
            return new UpdateUserStatusCodeContainer(UpdateUserStatusCodes.LOCATION_ERROR, "The location cannot be empty");
        }
        return new UpdateUserStatusCodeContainer(UpdateUserStatusCodes.OK);
    }
}
