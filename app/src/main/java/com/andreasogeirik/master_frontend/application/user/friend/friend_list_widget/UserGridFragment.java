package com.andreasogeirik.master_frontend.application.user.friend.friend_list_widget;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.layout.transformation.CircleTransform;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserGridFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserGridFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserGridFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    private View panel1;
    private View panel2;
    private View panel3;
    private View panel4;
    private View panel5;
    private View panel6;
    private View panel7;
    private View panel8;
    private View panel9;
    private View panel10;

    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ImageView img4;
    private ImageView img5;
    private ImageView img6;
    private ImageView img7;
    private ImageView img8;
    private ImageView img9;
    private ImageView img10;

    private TextView text1;
    private TextView text2;
    private TextView text3;
    private TextView text4;
    private TextView text5;
    private TextView text6;
    private TextView text7;
    private TextView text8;
    private TextView text9;
    private TextView text10;

    public UserGridFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserGridFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserGridFragment newInstance() {
        UserGridFragment fragment = new UserGridFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_grid, container, false);
        initView(v);

        return v;
    }

    public void setList(final List<User> users) {
        if(users.size() > 0) {
            panel1.setVisibility(View.VISIBLE);
            setImg(img1, users.get(0).getThumbUri());
            text1.setText(users.get(0).getFirstname());

            panel1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.navigateToUser(users.get(0));
                }
            });
        }
        else {
            panel1.setVisibility(View.GONE);
        }
        if(users.size() > 1) {
            panel2.setVisibility(View.VISIBLE);
            setImg(img2, users.get(1).getThumbUri());
            text2.setText(users.get(1).getFirstname());

            panel2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.navigateToUser(users.get(1));
                }
            });
        }
        else {
            panel2.setVisibility(View.GONE);
        }
        if(users.size() > 2) {
            panel3.setVisibility(View.VISIBLE);
            setImg(img3, users.get(2).getThumbUri());
            text3.setText(users.get(2).getFirstname());

            panel3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.navigateToUser(users.get(2));
                }
            });
        }
        else {
            panel3.setVisibility(View.GONE);
        }
        if(users.size() > 3) {
            panel4.setVisibility(View.VISIBLE);
            setImg(img4, users.get(3).getThumbUri());
            text4.setText(users.get(3).getFirstname());

            panel4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.navigateToUser(users.get(3));
                }
            });
        }
        else {
            panel4.setVisibility(View.GONE);
        }
        if(users.size() > 4) {
            panel5.setVisibility(View.VISIBLE);
            setImg(img5, users.get(4).getThumbUri());
            text5.setText(users.get(4).getFirstname());

            panel5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.navigateToUser(users.get(4));
                }
            });
        }
        else {
            panel5.setVisibility(View.GONE);
        }
        if(users.size() > 5) {
            panel6.setVisibility(View.VISIBLE);
            setImg(img6, users.get(5).getThumbUri());
            text6.setText(users.get(5).getFirstname());

            panel6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.navigateToUser(users.get(5));
                }
            });
        }
        else {
            panel6.setVisibility(View.GONE);
        }
        if(users.size() > 6) {
            panel7.setVisibility(View.VISIBLE);
            setImg(img7, users.get(6).getThumbUri());
            text7.setText(users.get(6).getFirstname());

            panel7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.navigateToUser(users.get(6));
                }
            });
        }
        else {
            panel7.setVisibility(View.GONE);
        }
        if(users.size() > 7) {
            panel8.setVisibility(View.VISIBLE);
            setImg(img8, users.get(7).getThumbUri());
            text8.setText(users.get(7).getFirstname());

            panel8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.navigateToUser(users.get(7));
                }
            });
        }
        else {
            panel8.setVisibility(View.GONE);
        }
        if(users.size() > 8) {
            panel9.setVisibility(View.VISIBLE);
            setImg(img9, users.get(8).getThumbUri());
            text9.setText(users.get(8).getFirstname());

            panel9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.navigateToUser(users.get(8));
                }
            });
        }
        else {
            panel9.setVisibility(View.GONE);
        }
        if(users.size() > 9) {
            panel10.setVisibility(View.VISIBLE);
            setImg(img10, users.get(9).getThumbUri());
            text10.setText(users.get(9).getFirstname());

            panel10.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.navigateToUser(users.get(9));
                }
            });
        }
        else {
            panel10.setVisibility(View.GONE);
        }
    }

    private void initView(View view) {
        panel1 = view.findViewById(R.id.imgPanel1);
        panel2 = view.findViewById(R.id.imgPanel2);
        panel3 = view.findViewById(R.id.imgPanel3);
        panel4 = view.findViewById(R.id.imgPanel4);
        panel5 = view.findViewById(R.id.imgPanel5);
        panel6 = view.findViewById(R.id.imgPanel6);
        panel7 = view.findViewById(R.id.imgPanel7);
        panel8 = view.findViewById(R.id.imgPanel8);
        panel9 = view.findViewById(R.id.imgPanel9);
        panel10 = view.findViewById(R.id.imgPanel10);

        img1 = (ImageView)view.findViewById(R.id.img1);
        img2 = (ImageView)view.findViewById(R.id.img2);
        img3 = (ImageView)view.findViewById(R.id.img3);
        img4 = (ImageView)view.findViewById(R.id.img4);
        img5 = (ImageView)view.findViewById(R.id.img5);
        img6 = (ImageView)view.findViewById(R.id.img6);
        img7 = (ImageView)view.findViewById(R.id.img7);
        img8 = (ImageView)view.findViewById(R.id.img8);
        img9 = (ImageView)view.findViewById(R.id.img9);
        img10 = (ImageView)view.findViewById(R.id.img10);

        text1 = (TextView)view.findViewById(R.id.imgText1);
        text2 = (TextView)view.findViewById(R.id.imgText2);
        text3 = (TextView)view.findViewById(R.id.imgText3);
        text4 = (TextView)view.findViewById(R.id.imgText4);
        text5 = (TextView)view.findViewById(R.id.imgText5);
        text6 = (TextView)view.findViewById(R.id.imgText6);
        text7 = (TextView)view.findViewById(R.id.imgText7);
        text8 = (TextView)view.findViewById(R.id.imgText8);
        text9 = (TextView)view.findViewById(R.id.imgText9);
        text10 = (TextView)view.findViewById(R.id.imgText10);
    }

    private void setImg(ImageView imageView, String imageUri) {
        if(imageUri != null && !imageUri.isEmpty()) {
            Picasso.with(getContext())
                    .load(imageUri)
                    .resize(Constants.LIST_IMAGE_WIDTH, Constants.LIST_IMAGE_HEIGHT)
                    .centerCrop()
                    .transform(new CircleTransform())
                    .error(R.drawable.default_event)
                    .into(imageView);
        }
        else {
            Picasso.with(getContext())
                    .load(R.drawable.default_event)
                    .resize(Constants.LIST_IMAGE_WIDTH, Constants.LIST_IMAGE_HEIGHT)
                    .transform(new CircleTransform())
                    .centerCrop()
                    .into(imageView);
        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void navigateToUser(User user);
    }
}
