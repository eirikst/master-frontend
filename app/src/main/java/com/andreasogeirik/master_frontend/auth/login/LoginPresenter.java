package com.andreasogeirik.master_frontend.auth.login;

/**
 * Created by Andreas on 26.01.2016.
 */
public class LoginPresenter implements ILoginPresenter, OnFinishedListe {

    private ILoginView view;

    public LoginPresenter(ILoginView loginView){
        this.view = loginView;
    }
    @Override
    public void attemptLogin(String username, String password) {

    }
}
