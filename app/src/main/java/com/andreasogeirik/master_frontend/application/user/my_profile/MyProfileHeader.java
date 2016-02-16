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
public class MyProfileHeader extends Fragment {
    private MyProfileHeaderListener callback;//the activity that created the fragment

    private int friendCount;

    private TextView friendText;
    private TextView editProfileText;


    // Container Activity must implement this interface
    public interface MyProfileHeaderListener {
        public void onFriendListSelected1();
    }

    /*
     * Creates a new instance of the fragment, adds the friend list
     */
    public static MyProfileHeader newInstance(int friendCount) {
        MyProfileHeader f = new MyProfileHeader();
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
            callback = (MyProfileHeaderListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement MyProfileHeaderListener");
        }
    }


    /*
     * Inflates the layout, sets the views with onclicklisteners
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.my_profile_fragment_header, container, false);

        friendText = (TextView)view.findViewById(R.id.my_profile_friends);
        editProfileText = (TextView)view.findViewById(R.id.my_profile_edit_profile);

        friendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onFriendListSelected1();
            }
        });

        editProfileText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start edit profile activity
            }
        });

        return view;
    }


    /*
     * Updates the friend model
     */
    public void updateFriendCount(int count) {
        this.friendCount = count;
        friendText.setText(friendCount + " venner");//hardcoded string
    }
}
