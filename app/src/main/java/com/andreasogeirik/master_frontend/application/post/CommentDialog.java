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
import android.widget.Toast;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.model.Post;

import java.util.HashSet;

public class CommentDialog extends DialogFragment {
    public interface Listener {
        void comment(Post post, String message);
    }

    private Listener callback;
    private Post post;
    private Button commentBtn;

    public static CommentDialog newInstance(Post post) {
        CommentDialog f = new CommentDialog();

        Bundle bundle = new Bundle();
        bundle.putSerializable("post", post);
        f.setArguments(bundle);

        return f;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            callback = (Listener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement AttendingEventsListener");
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
        View v = inflater.inflate(R.layout.comment_dialog_fragment, container, false);

        Dialog dialog = getDialog();
        if(dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }

        Bundle bundle = this.getArguments();
        if(bundle != null) {
            try {
                post = (Post) bundle.getSerializable("post");
            }
            catch(ClassCastException e) {
                throw new ClassCastException("Bundle should contain a post");
            }
        }
        else {
            throw new RuntimeException("Bundle has no data");
        }

        commentBtn = (Button)v.findViewById(R.id.comment_btn);
        final EditText commentMsg = (EditText)v.findViewById(R.id.comment_message);

        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(commentMsg.getText().toString() != null && !commentMsg.getText().toString().isEmpty()) {
                    callback.comment(post, commentMsg.getText().toString());
                    commentButtonEnable(false);
                }
            }
        });

        return v;
    }

    public void commentButtonEnable(boolean enabled) {
        commentBtn.setEnabled(enabled);
    }
}