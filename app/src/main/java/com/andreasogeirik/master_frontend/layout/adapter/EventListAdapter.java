package com.andreasogeirik.master_frontend.layout.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.layout.transformation.CircleTransform;
import com.andreasogeirik.master_frontend.model.ActivityType;
import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.DateUtility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by eirikstadheim on 05/02/16.
 */
public class EventListAdapter extends ArrayAdapter<Event> {

    private User user;
    private Context context;
    private Comparator<Event> comparator;
    private CircleTransform circleTransform = new CircleTransform();


    public EventListAdapter(Context context) {
        super(context, 0);
        this.context = context;

        /*
         * The comparator sorts the list so that future events comes first, with the earliest first.
         * Past events comes after the future, with the latest first
         */
        comparator = new Comparator<Event>() {
            Calendar cal = new GregorianCalendar();
            @Override
            public int compare(Event lhs, Event rhs) {
                if(lhs.getStartDate().equals(rhs.getStartDate())) {
                    if(lhs.getId() < rhs.getId()) {
                        return -1;
                    }
                    return 1;
                }
                else if(lhs.getStartDate().before(cal) && rhs.getStartDate().before(cal)) {
                    return lhs.compareTo(rhs);
                }
                else if(lhs.getStartDate().after(cal) && rhs.getStartDate().after(cal)) {
                    return -lhs.compareTo(rhs);
                }
                else {
                    return lhs.compareTo(rhs);
                }
            }
        };
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Event event = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.event_list_layout, parent, false);
        }

        //Set activity type
        ImageView activityTypeImage = (ImageView)convertView.findViewById(R.id.activity_type);

        switch (event.getActivityType()) {
            case WALK:
                activityTypeImage.setImageDrawable(ContextCompat.getDrawable(getContext(),
                        R.drawable.ic_directions_walk_red_400_24dp));
                break;
            case RUN:
            activityTypeImage.setImageDrawable(ContextCompat.getDrawable(getContext(),
                    R.drawable.ic_directions_run_orange_300_24dp));
                break;
            case SKI:
                activityTypeImage.setImageDrawable(ContextCompat.getDrawable(getContext(),
                        R.drawable.ic_ski_510));
                break;
            case BIKE:
                activityTypeImage.setImageDrawable(ContextCompat.getDrawable(getContext(),
                        R.drawable.ic_directions_bike_blue_24dp));
                break;
            case SWIM:
                activityTypeImage.setImageDrawable(ContextCompat.getDrawable(getContext(),
                        R.drawable.ic_swim_226));
                break;
            case GROUP_WORKOUT:
                activityTypeImage.setImageDrawable(ContextCompat.getDrawable(getContext(),
                        R.drawable.ic_gruppetrening_24dp));
                break;
            case OTHER:
                activityTypeImage.setImageDrawable(null);
                break;
            default:
                activityTypeImage.setImageDrawable(null);
                break;
        }

        /*
         * Adds a past view on the first list element with an event from the past
         * Adds a future view on the first list element with an event from the future
         */
        TextView pastView = (TextView) convertView.findViewById(R.id.past_header);
        TextView futureView = (TextView) convertView.findViewById(R.id.future_header);

        Date now = new Date();

        if (position == 0) {
            if (event.getStartDate().getTime().after(now)) {
                futureView.setVisibility(View.VISIBLE);
                pastView.setVisibility(View.GONE);
            } else {
                pastView.setVisibility(View.VISIBLE);
                futureView.setVisibility(View.GONE);
            }
        } else if (position > 0) {
            if (event.getStartDate().getTime().before(now) && getItem(position - 1).getStartDate().getTime().after(now)) {
                pastView.setVisibility(View.VISIBLE);
                futureView.setVisibility(View.GONE);
            } else {
                pastView.setVisibility(View.GONE);
                futureView.setVisibility(View.GONE);
            }
        } else {
            futureView.setVisibility(View.GONE);
            pastView.setVisibility(View.GONE);
        }


        /*
         * Sets the data for the view
         */
        TextView name = (TextView) convertView.findViewById(R.id.event_name);
        TextView timeStart = (TextView) convertView.findViewById(R.id.event_time_start);
        TextView participants = (TextView) convertView.findViewById(R.id.event_participants_nr);
        TextView friends = (TextView) convertView.findViewById(R.id.event_friends);

        if (user != null) {
            int friendCount = 0;
            List<String> friendNames = new ArrayList<>();

            for (User eventUser : event.getUsers()) {
                if (user.isFriendWith(eventUser)) {
                    friendCount++;
                    friendNames.add(eventUser.getFirstname());
                }
            }

            boolean userAttendsEvent = event.attends(user);

            //Litt weird, men ok...
            String participate = "attend";


            if(event.getStartDate().getTime().before(now)) {
                participate = "attended";
            }

            if (friendCount > 2) {
                if(!userAttendsEvent) {
                    friends.setText(friendNames.get(0) + " and " + (friendCount - 1) + " other friends" + participate);
                }
                else {
                    friends.setText("You and " + (friendCount) + " friends " + participate);
                }
                friends.setVisibility(View.VISIBLE);
            }
            else if(friendCount == 2) {
                if(!userAttendsEvent) {
                    friends.setText(friendNames.get(0) + " and " + friendNames.get(1) + " " + participate);
                }
                else {
                    friends.setText("You and two friends " + participate);
                }
                friends.setVisibility(View.VISIBLE);
            }
            else if(friendCount == 1) {
                if(!userAttendsEvent) {
                    friends.setText(friendNames.get(0) + " " + participate);
                }
                else {
                    friends.setText("You and " + friendNames.get(0) + " " + participate);
                }
                friends.setVisibility(View.VISIBLE);
            }
            else {
                if(userAttendsEvent) {
                    friends.setText("You " + participate);
                    friends.setVisibility(View.VISIBLE);
                }
                else {
                    friends.setVisibility(View.GONE);
                }
            }
        }
        else {
            friends.setVisibility(View.GONE);
        }

        // Populate the data using the events
        name.setText(event.getName());
        timeStart.setText(DateUtility.formatFull(event.getStartDate().getTime()));
        participants.setText(event.getUsers().size() + "");

        //if cancelled
        if(!event.isEnabled()) {
            convertView.findViewById(R.id.cancel_panel).setVisibility(View.VISIBLE);
        }
        else {
            convertView.findViewById(R.id.cancel_panel).setVisibility(View.GONE);
        }

        // Setup imageview
        ImageView image = (ImageView)convertView.findViewById(R.id.event_image);

        //load image
        if(event.getThumbUri() != null && !event.getThumbUri().isEmpty()) {
            Picasso.with(context)
                    .load(event.getThumbUri())
                    .resize(Constants.LIST_IMAGE_WIDTH, Constants.LIST_IMAGE_HEIGHT)
                    .centerCrop()
                    .transform(circleTransform)
                    .error(R.drawable.default_event)
                    .into(image);
        }
        else {
            Picasso.with(context)
                    .load(R.drawable.default_event)
                    .resize(Constants.LIST_IMAGE_WIDTH, Constants.LIST_IMAGE_HEIGHT)
                    .centerCrop()
                    .transform(circleTransform)
                    .into(image);
        }

        // Return view for rendering
        return convertView;
    }

    @Override
    public void add(Event object) {
        super.add(object);
        sort(comparator);
    }

    @Override
    public void addAll(Collection<? extends Event> collection) {
        super.addAll(collection);
        sort(comparator);
    }

    @Override
    public void addAll(Event... items) {
        super.addAll(items);
        sort(comparator);
    }

    public void setUser(User user) {
        this.user  = user;
    }
}