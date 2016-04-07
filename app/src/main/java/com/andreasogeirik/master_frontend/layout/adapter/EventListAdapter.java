package com.andreasogeirik.master_frontend.layout.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.DateUtility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by eirikstadheim on 05/02/16.
 */
public class EventListAdapter extends ArrayAdapter<Event> {
    public interface Listener {
        public void findImage(String imageUri);
    }

    private User user;
    private Map<String, Bitmap> eventImages;
    private Context context;
    private Listener listener;
    private Bitmap defaultImage;
    private Comparator<Event> comparator;

    public EventListAdapter(Context context, Listener listener) {
        super(context, 0);
        this.context = context;
        this.listener = listener;
        this.eventImages = new HashMap<>();

        /*
         * The comparator sorts the list so that future events comes first, with the earliest first.
         * Past events comes after the future, with the latest first
         */
        comparator = new Comparator<Event>() {
            Calendar cal = new GregorianCalendar();
            @Override
            public int compare(Event lhs, Event rhs) {
                if(lhs.getStartDate().before(cal) && rhs.getStartDate().before(cal)) {
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

        /*
         * Sets the difficulty level drawables based on the event
         */
        View easyEvent = convertView.findViewById(R.id.easy_event);
        View mediumEvent = convertView.findViewById(R.id.medium_event);
        View hardEvent = convertView.findViewById(R.id.hard_event);

        if(event.getDifficulty() == Constants.EVENT_DIFFICULTY_EASY) {
            easyEvent.setVisibility(View.VISIBLE);
            mediumEvent.setVisibility(View.GONE);
            hardEvent.setVisibility(View.GONE);
        }
        else if(event.getDifficulty() == Constants.EVENT_DIFFICULTY_MEDIUM) {
            easyEvent.setVisibility(View.GONE);
            mediumEvent.setVisibility(View.VISIBLE);
            hardEvent.setVisibility(View.GONE);
        }
        else if(event.getDifficulty() == Constants.EVENT_DIFFICULTY_HARD) {
            easyEvent.setVisibility(View.GONE);
            mediumEvent.setVisibility(View.GONE);
            hardEvent.setVisibility(View.VISIBLE);
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
            String participate = "deltar";


            if(event.getStartDate().getTime().before(now)) {
                participate = "deltok";
            }

            if (friendCount > 2) {
                if(!userAttendsEvent) {
                    friends.setText(friendNames.get(0) + " og " + (friendCount - 1) + " andre venner " + participate);
                }
                else {
                    friends.setText("Du og " + (friendCount) + " venner " + participate);
                }
                friends.setVisibility(View.VISIBLE);
            }
            else if(friendCount == 2) {
                if(!userAttendsEvent) {
                    friends.setText(friendNames.get(0) + " og " + friendNames.get(1) + " " + participate);
                }
                else {
                    friends.setText("Du og to venner " + participate);
                }
                friends.setVisibility(View.VISIBLE);
            }
            else if(friendCount == 1) {
                if(!userAttendsEvent) {
                    friends.setText(friendNames.get(0) + " " + participate);
                }
                else {
                    friends.setText("Du og " + friendNames.get(0) + " " + participate);
                }
                friends.setVisibility(View.VISIBLE);
            }
            else {
                if(userAttendsEvent) {
                    friends.setText("Du " + participate);
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


        // Setup imageview
        ImageView image = (ImageView)convertView.findViewById(R.id.event_image);

        if(eventImages.containsKey(event.getImageURI())) {
            System.out.println("Image found and set for " + event.getName());
            image.setImageBitmap(eventImages.get(event.getImageURI()));
        }
        else {
            //no image, use default
            if(event.getImageURI() == null || event.getImageURI().equals("")) {
                System.out.println("Image null or empty for " + event.getName() +
                        ". Setting default image");
                setDefaultImage(image, event.getImageURI());
            }
            //get image from outside
            else {
                System.out.println("Image not found for " + event.getName() + ". Fetching image "
                        + event.getImageURI());
                listener.findImage(event.getImageURI());
            }
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

    /*
     * Set image to null to get default. Name is always needed
     */
    public void addImage(String name, Bitmap image) {
        eventImages.put(name, image);
        notifyDataSetChanged();
    }

    private void setDefaultImage(ImageView image, String imageName) {
        if(defaultImage == null) {
            defaultImage = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.default_event);
        }
        if(defaultImage != null) {
            image.setImageBitmap(defaultImage);
            eventImages.put(imageName, defaultImage);
        }
    }

    public void setUser(User user) {
        this.user  = user;
    }
}