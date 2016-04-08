package com.andreasogeirik.master_frontend.util.validation.user.details;

import android.text.TextUtils;

/**
 * Created by Andreas on 08.04.2016.
 */
public class UpdateUserValidator {

    public static UpdateUserStatusCodeContainer validate(String firstname, String lastname, String location) {
        if (TextUtils.isEmpty(firstname)) {
            return new UpdateUserStatusCodeContainer(UpdateUserStatusCodes.FIRST_NAME_ERROR, "Fornavn kan ikke være tomt");
        } else if (TextUtils.isEmpty(lastname)) {
            return new UpdateUserStatusCodeContainer(UpdateUserStatusCodes.LAST_NAME_ERROR, "Etternavn kan ikke være tomt");
        } else if (TextUtils.isEmpty(location)) {
            return new UpdateUserStatusCodeContainer(UpdateUserStatusCodes.LOCATION_ERROR, "Sted kan ikke være tomt");
        }
        return new UpdateUserStatusCodeContainer(UpdateUserStatusCodes.OK);
    }
}
