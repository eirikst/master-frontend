package com.andreasogeirik.master_frontend.application.event.create.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.listener.OnActivityTypeSet;

/**
 * Created by andrena on 05.05.2016.
 */
public class ActivityTypeFragment extends DialogFragment {

    private int currentSelection;

    /**
     * Create a new instance of NotificationDialogFragment, providing "num"
     * as an argument.
     */
    public static ActivityTypeFragment newInstance() {
        ActivityTypeFragment f = new ActivityTypeFragment();
        return f;
    }

    OnActivityTypeSet callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            callback = (OnActivityTypeSet) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnDateSetListener");
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_type_fragment, container, false);
        RadioGroup radioGroup = (RadioGroup) v.findViewById(R.id.radio_group);
        Button btn = (Button) v.findViewById(R.id.submit);
        int checkedId = getArguments().getInt("checkedId");
        RadioButton rBtn;
        switch (checkedId) {
            case 0:
                rBtn = (RadioButton) v.findViewById(R.id.walk);
                rBtn.setChecked(true);
                currentSelection = R.id.walk;
                break;
            case 1:
                rBtn = (RadioButton) v.findViewById(R.id.run);
                rBtn.setChecked(true);
                currentSelection = R.id.run;
                break;
            case 2:
                rBtn = (RadioButton) v.findViewById(R.id.bike);
                rBtn.setChecked(true);
                currentSelection = R.id.bike;
                break;
            case 3:
                rBtn = (RadioButton) v.findViewById(R.id.ski);
                rBtn.setChecked(true);
                currentSelection = R.id.ski;
                break;
            case 4:
                rBtn = (RadioButton) v.findViewById(R.id.swim);
                rBtn.setChecked(true);
                currentSelection = R.id.swim;
                break;
            case 5:
                rBtn = (RadioButton) v.findViewById(R.id.group_workout);
                rBtn.setChecked(true);
                currentSelection = R.id.group_workout;
                break;
            default:
                rBtn = (RadioButton) v.findViewById(R.id.other);
                rBtn.setChecked(true);
                currentSelection = R.id.other;
                break;
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onActivityTypeSet(currentSelection);
                dismiss();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                currentSelection = checkedId;
            }
        });
        return v;
    }


}
