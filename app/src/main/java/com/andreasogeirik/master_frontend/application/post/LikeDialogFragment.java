package com.andreasogeirik.master_frontend.application.post;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.user.profile.ProfileActivity;
import com.andreasogeirik.master_frontend.application.user.profile_others.ProfileOthersActivity;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.layout.adapter.UserListAdapter;
import com.andreasogeirik.master_frontend.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class LikeDialogFragment extends DialogFragment {
    private UserListAdapter adapter;

    public static LikeDialogFragment newInstance(HashSet<User> likers) {
        LikeDialogFragment f = new LikeDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("likers", likers);
        f.setArguments(bundle);

        return f;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.like_dialog_fragment, container, false);

        Dialog dialog = getDialog();
        if(dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        Bundle bundle = this.getArguments();
        if(bundle != null) {
            Set<User> likers = (Set<User>)bundle.getSerializable("likers");

            adapter = new UserListAdapter(getActivity(), new ArrayList<>(likers));
        }
        else {
            adapter = new UserListAdapter(getActivity(), new ArrayList());
        }

        ListView listView = (ListView)v.findViewById(R.id.likers_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                navigateToUser(adapter.getItem(position));
            }
        });

        return v;
    }

    public void setNotifications(Set<User> likers) {
        adapter.setData(likers);
    }

    private void navigateToUser(User user) {
        //if friend or self
        if (CurrentUser.getInstance().getUser().isFriendWith(user) ||
                CurrentUser.getInstance().getUser().equals(user)) {
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            intent.putExtra("user", user.getId());
            getActivity().startActivity(intent);
        }
        //or if not friend
        else {
            Intent intent = new Intent(getActivity(), ProfileOthersActivity.class);
            intent.putExtra("user", user);
            getActivity().startActivity(intent);
        }
    }
}