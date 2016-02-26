package com.andreasogeirik.master_frontend.util.validation;

import com.andreasogeirik.master_frontend.util.validation.CreateEventStatusCodes;

/**
 * Created by Andreas on 24.02.2016.
 */
public class CreateEventValidationContainer {
    private CreateEventStatusCodes statusCode;
    private String error;

    public CreateEventValidationContainer(CreateEventStatusCodes statusCode) {
        this.statusCode = statusCode;
    }

    public CreateEventValidationContainer(CreateEventStatusCodes statusCode, String error) {
        this.statusCode = statusCode;
        this.error = error;
    }

    public CreateEventStatusCodes getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(CreateEventStatusCodes statusCode) {
        this.statusCode = statusCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
