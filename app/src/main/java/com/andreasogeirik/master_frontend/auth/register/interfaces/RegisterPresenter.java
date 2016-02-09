package com.andreasogeirik.master_frontend.auth.register.interfaces;

/**
 * Created by Andreas on 05.02.2016.
 */
public interface RegisterPresenter {
    void validateCredentials(String email, String password, String rePassword, String firstname, String lastname, String location);
}
