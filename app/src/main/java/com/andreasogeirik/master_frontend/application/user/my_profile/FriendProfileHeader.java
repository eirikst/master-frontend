package com.andreasogeirik.master_frontend.application.user.my_profile;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andreasogeirik.master_frontend.R;


/**
 * Created by eirikstadheim on 12/02/16.
 */
public class FriendProfileHeader extends Fragment {
    private FriendProfileHeaderListener callback;//the activity that created the fragment

    private int friendCount;

    private TextView friendText;


    // Container Activity must implement this interface
    public interface FriendProfileHeaderListener {
        public void onFriendListSelected2();
    }

    /*
     * Creates a new instance of the fragment, adds the friend list
     */
    public static FriendProfileHeader newInstance(int friendCount) {
        FriendProfileHeader f = new FriendProfileHeader();
        f.friendCount = friendCount;
        return f;
    }


    /*
     * Checks that the activity that creates the fragment implements the interface for callback
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            callback = (FriendProfileHeaderListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement FriendProfileHeaderListener");
        }
    }


    /*
     * Inflates the layout, sets the views with onclicklisteners
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.friend_profile_fragment_header, container, false);

        friendText = (TextView)view.findViewById(R.id.friend_profile_friends);

        friendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onFriendListSelected2();
            }
        });

        return view;
    }


    /*
     * Updates the friend model
     */
    public void updateFriendCount(int count) {
        this.friendCount = count;
        if(count == 1) {
            friendText.setText(friendCount + " venn");//hardcoded string
        }
        else {
            friendText.setText(friendCount + " venner");//hardcoded string
        }
    }
}
