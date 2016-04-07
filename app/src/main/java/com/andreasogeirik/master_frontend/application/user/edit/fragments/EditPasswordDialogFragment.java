package com.andreasogeirik.master_frontend.application.user.edit.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.support.v4.app.DialogFragment;

import com.andreasogeirik.master_frontend.R;

/**
 * Created by Andreas on 07.04.2016.
 */
public class EditPasswordDialogFragment extends DialogFragment {

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.password_dialog_fragment, container, false);

        TextView currentPass = (TextView) v.findViewById(R.id.current_password);
        TextView newPass = (TextView) v.findViewById(R.id.password);
        TextView rePass = (TextView) v.findViewById(R.id.re_password);

        Button submit = (Button) v.findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return v;
    }
}
