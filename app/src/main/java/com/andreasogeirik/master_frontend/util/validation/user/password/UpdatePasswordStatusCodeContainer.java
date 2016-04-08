package com.andreasogeirik.master_frontend.util.validation.user.password;

/**
 * Created by Andreas on 08.04.2016.
 */

public class UpdatePasswordStatusCodeContainer {
    public UpdatePasswordStatusCodes code;
    public String message;

    public UpdatePasswordStatusCodeContainer(UpdatePasswordStatusCodes code, String message) {
        this.code = code;
        this.message = message;
    }

    public UpdatePasswordStatusCodeContainer(UpdatePasswordStatusCodes code) {
        this.code = code;
    }

    public UpdatePasswordStatusCodes getCode() {
        return code;
    }

    public void setCode(UpdatePasswordStatusCodes code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}


