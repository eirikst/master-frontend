package com.andreasogeirik.master_frontend.util.validation.user.password;

import android.text.TextUtils;

/**
 * Created by Andreas on 08.04.2016.
 */
public class UpdatePasswordValidator {

    public static UpdatePasswordStatusCodeContainer validate(String currentPass, String newPass, String rePass) {
        if (TextUtils.isEmpty(currentPass)) {
            return new UpdatePasswordStatusCodeContainer(UpdatePasswordStatusCodes.CURRENT_PASS_ERROR, "Skriv inn ditt nåværende passord");
        } else if (TextUtils.isEmpty(newPass)) {
            return new UpdatePasswordStatusCodeContainer(UpdatePasswordStatusCodes.NEW_PASS_ERROR, "Skriv inn et nytt passord");
        } else if (TextUtils.isEmpty(rePass)) {
            return new UpdatePasswordStatusCodeContainer(UpdatePasswordStatusCodes.RE_PASS_ERROR, "Skriv inn det nye passordet en gang til");
        } else if (newPass.length() < 3) {
            return new UpdatePasswordStatusCodeContainer(UpdatePasswordStatusCodes.NEW_PASS_ERROR, "Passordet må være minst 3 tegn");
        } else if (!TextUtils.equals(newPass, rePass)) {
            return new UpdatePasswordStatusCodeContainer(UpdatePasswordStatusCodes.NEW_PASS_ERROR, "Passordene matcher ikke");
        }
        return new UpdatePasswordStatusCodeContainer(UpdatePasswordStatusCodes.OK);
    }
}
