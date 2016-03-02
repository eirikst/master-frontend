package com.andreasogeirik.master_frontend.layout.adapter;

import android.app.Activity;
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
import com.andreasogeirik.master_frontend.util.DateUtility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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

    private List<Event> events;
    private Map<String, Bitmap> eventImages;
    private Context context;
    private Listener listener;
    private Bitmap defaultImage;
    private Comparator<Event> comparator;

    private List<Event> future = new ArrayList<>();
    private List<Event> past = new ArrayList<>();

    public EventListAdapter(Context context, Listener listener) {
        super(context, 0);
        this.context = context;
        this.events = new ArrayList<Event>();
        this.listener = listener;
        this.eventImages = new HashMap<>();

        /*
         * The comparator sorts the list so that future events comes first, with the earliest first.
         * Past events comes after the future, with the latest first
         */
        comparator = new Comparator<Event>() {
            private Calendar cal = new GregorianCalendar();
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
                    R.layout.attending_events_list_layout, parent, false);
        }

        /*
         * Adds a past view on the first list element with an event from the past
         * Adds a future view on the first list element with an event from the future
         */
        TextView pastView = (TextView)convertView.findViewById(R.id.past_header);
        TextView futureView = (TextView)convertView.findViewById(R.id.future_header);

        if(!future.isEmpty() && event.equals(future.get(0))) {
            futureView.setVisibility(View.VISIBLE);
            pastView.setVisibility(View.GONE);
        }
        else if(!past.isEmpty() && event.equals(past.get(0))) {
            pastView.setVisibility(View.VISIBLE);
            futureView.setVisibility(View.GONE);
        }
        else {
            futureView.setVisibility(View.GONE);
            pastView.setVisibility(View.GONE);
        }

        /*
         * Sets the data for the view
         */
        TextView name = (TextView)convertView.findViewById(R.id.event_name);
        TextView timeStart = (TextView)convertView.findViewById(R.id.event_time_start);
        TextView participants = (TextView)convertView.findViewById(R.id.event_participants);

        // Populate the data using the events
        name.setText(event.getName());
        timeStart.setText(DateUtility.formatFull(event.getStartDate().getTime()));
        if(event.getUsers().size() == 1) {
            participants.setText(event.getUsers().size() + " bruker deltar");
        }
        else {
            participants.setText(event.getUsers().size() + " brukere deltar");
        }

        // Setup imageview
        ImageView image = (ImageView)convertView.findViewById(R.id.event_image);

        if(eventImages.containsKey(event.getImageURI())) {
            System.out.println("Image found and set for " + event.getName());
            image.setImageBitmap(eventImages.get(event.getImageURI()));
        }
        else {
            //no image, user standard
            if(event.getImageURI() == null || event.getImageURI().equals("")) {
                System.out.println("Image null or empty for " + event.getName() +
                        ". Setting standard image");
                setDefaultImage(image, event.getImageURI());
            }
            //get image from outside
            else {
                System.out.println("Image not found for " + event.getName() + ". Fetching image " + event.getImageURI());
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

        Calendar cal = new GregorianCalendar();

        if(object.getStartDate().before(cal)) {
            past.add(object);
        }
        else {
            future.add(object);
        }
        Collections.sort(past, comparator);
        Collections.sort(future, comparator);
    }

    @Override
    public void addAll(Collection<? extends Event> collection) {
        super.addAll(collection);
        sort(comparator);

        Calendar cal = new GregorianCalendar();
        for(Event e: collection) {
            if(e.getStartDate().before(cal)) {
                past.add(e);
            }
            else {
                future.add(e);
            }
        }
        Collections.sort(past, comparator);
        Collections.sort(future, comparator);
    }

    @Override
    public void addAll(Event... items) {
        super.addAll(items);
        sort(comparator);

        Calendar cal = new GregorianCalendar();
        for(Event e: items) {
            if(e.getStartDate().before(cal)) {
                past.add(e);
            }
            else {
                future.add(e);
            }
        }
        Collections.sort(past, comparator);
        Collections.sort(future, comparator);
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
}