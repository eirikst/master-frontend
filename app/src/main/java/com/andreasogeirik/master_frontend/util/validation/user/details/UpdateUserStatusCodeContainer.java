package com.andreasogeirik.master_frontend.util.validation.user.details;

/**
 * Created by Andreas on 08.04.2016.
 */

public class UpdateUserStatusCodeContainer {
    public UpdateUserStatusCodes code;
    public String message;

    public UpdateUserStatusCodeContainer(UpdateUserStatusCodes code, String message) {
        this.code = code;
        this.message = message;
    }

    public UpdateUserStatusCodeContainer(UpdateUserStatusCodes code) {
        this.code = code;
    }

    public UpdateUserStatusCodes getCode() {
        return code;
    }

    public void setCode(UpdateUserStatusCodes code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}


