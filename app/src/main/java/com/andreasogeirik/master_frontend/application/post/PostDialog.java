package com.andreasogeirik.master_frontend.application.post;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.andreasogeirik.master_frontend.R;

import java.io.Serializable;

public class PostDialog extends DialogFragment {
    public interface Listener {
        void post(String msg);
    }

    private Listener callback;
    private Button postBtn;

    public static PostDialog newInstance() {
        return new PostDialog();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            callback = (Listener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement PostDialog.Listener");
        }
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.post_dialog_fragment, container, false);

        Dialog dialog = getDialog();
        if(dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }

        postBtn = (Button)v.findViewById(R.id.post_btn);
        final EditText postMsg = (EditText)v.findViewById(R.id.post_message);

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postMsg.getText().toString() != null && !postMsg.getText().toString().isEmpty()) {
                    callback.post(postMsg.getText().toString());
                    postButtonEnable(false);
                }
            }
        });

        return v;
    }

    public void postButtonEnable(boolean enabled) {
        postBtn.setEnabled(enabled);
    }
}