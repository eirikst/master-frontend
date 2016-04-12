package com.andreasogeirik.master_frontend.application.user.edit.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.user.edit.fragments.interfaces.EditPasswordPresenter;
import com.andreasogeirik.master_frontend.application.user.edit.fragments.interfaces.EditPasswordView;
import com.andreasogeirik.master_frontend.application.user.profile.ProfileActivity;

/**
 * Created by Andreas on 07.04.2016.
 */
public class EditPasswordDialogFragment extends DialogFragment implements EditPasswordView, View.OnClickListener {

    EditPasswordPresenter presenter;

    private EditText currentPass;
    private EditText newPass;
    private EditText rePass;
    private TextView errorMessage;

    /**
     * Create a new instance of NotificationDialogFragment, providing "num"
     * as an argument.
     */
    public static EditPasswordDialogFragment newInstance() {
        EditPasswordDialogFragment f = new EditPasswordDialogFragment();
        return f;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        Dialog dialog = getDialog();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.password_dialog_fragment, container, false);
        this.presenter = new EditPasswordPresenterImpl(this);

        this.currentPass = (EditText) v.findViewById(R.id.current_password);
        this.newPass = (EditText) v.findViewById(R.id.password);
        this.rePass = (EditText) v.findViewById(R.id.re_password);
        this.errorMessage = (TextView) v.findViewById(R.id.error);

        Button submit = (Button) v.findViewById(R.id.submit);
        submit.setOnClickListener(this);
        return v;
    }

    @Override
    public void navigateBack() {
        dismiss();
        Toast.makeText(getActivity(), "Passord endret", Toast.LENGTH_LONG).show();
    }

    @Override
    public void currentPasswordError(String errorMessage) {
        currentPass.setError(errorMessage);
        currentPass.requestFocus();
    }

    @Override
    public void newPasswordError(String errorMessage) {
        newPass.setError(errorMessage);
        newPass.requestFocus();
    }

    @Override
    public void rePasswordError(String errorMessage) {
        rePass.setError(errorMessage);
        rePass.requestFocus();
    }


    @Override
    public void displayErrors(String errorMessage) {
        this.errorMessage.setText(errorMessage);
        this.errorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        this.errorMessage.setVisibility(View.GONE);
        switch (v.getId()) {
            case R.id.submit:
                this.presenter.updatePassword(currentPass.getText().toString(), newPass.getText().
                        toString(), rePass.getText().toString());
                break;
        }
    }
}
