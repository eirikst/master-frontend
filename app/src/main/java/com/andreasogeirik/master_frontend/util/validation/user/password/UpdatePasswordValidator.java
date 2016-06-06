package com.andreasogeirik.master_frontend.util.validation.user.password;

import android.text.TextUtils;

/**
 * Created by Andreas on 08.04.2016.
 */
public class UpdatePasswordValidator {

    public static UpdatePasswordStatusCodeContainer validate(String currentPass, String newPass, String rePass) {
        if (TextUtils.isEmpty(currentPass)) {
            return new UpdatePasswordStatusCodeContainer(UpdatePasswordStatusCodes.CURRENT_PASS_ERROR, "Enter your current password");
        } else if (TextUtils.isEmpty(newPass)) {
            return new UpdatePasswordStatusCodeContainer(UpdatePasswordStatusCodes.NEW_PASS_ERROR, "Enter a new password");
        } else if (TextUtils.isEmpty(rePass)) {
            return new UpdatePasswordStatusCodeContainer(UpdatePasswordStatusCodes.RE_PASS_ERROR, "Enter the new password one more time");
        } else if (newPass.length() < 3) {
            return new UpdatePasswordStatusCodeContainer(UpdatePasswordStatusCodes.NEW_PASS_ERROR, "The password must be at least 3 characters long");
        } else if (!TextUtils.equals(newPass, rePass)) {
            return new UpdatePasswordStatusCodeContainer(UpdatePasswordStatusCodes.NEW_PASS_ERROR, "The passwords don't match");
        }
        return new UpdatePasswordStatusCodeContainer(UpdatePasswordStatusCodes.OK);
    }
}
