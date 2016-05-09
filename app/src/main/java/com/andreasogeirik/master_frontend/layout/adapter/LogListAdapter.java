package com.andreasogeirik.master_frontend.layout.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.model.ContentType;
import com.andreasogeirik.master_frontend.model.LogElement;
import com.andreasogeirik.master_frontend.util.DateUtility;

import java.util.Collection;
import java.util.Comparator;

/**
 * Created by eirikstadheim on 05/02/16.
 */
public class LogListAdapter extends ArrayAdapter<LogElement> {
    private String tag = getClass().getSimpleName();

    private Context context;
    private Comparator<LogElement> comparator;


    public LogListAdapter(Context context) {
        super(context, 0);
        this.context = context;


        comparator = new Comparator<LogElement>() {
            @Override
            public int compare(LogElement lhs, LogElement rhs) {
                if(lhs.getTime().equals(rhs.getTime())) {
                    if(lhs.getId() < rhs.getId()) {
                        return -1;
                    }
                    return 1;
                }
                if (lhs.getTime().after(rhs.getTime())) {
                    return -1;
                }
                return 1;
            }
        };
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LogElement element = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.log_list_layout, parent, false);
        }

        TextView content = (TextView)convertView.findViewById(R.id.content);
        content.setText(Html.fromHtml("<i>" + DateUtility.formatTimeDiff(element.getTime()) +
                ":</i> " + element.getContent()) + " og klokken er " + DateUtility.formatFull(element.getTime()));

        ImageView symbol = (ImageView)convertView.findViewById(R.id.log_symbol);

        if(element.getType() == ContentType.CREATE_EVENT || element.getType() == ContentType.
                MODIFY_EVENT || element.getType() == ContentType.PARTICIPATE_EVENT) {
            symbol.setImageResource(R.drawable.ic_event_black_24dp);
        }
        else if(element.getType() == ContentType.POST_EVENT || element.getType() ==
                ContentType.POST_USER) {
            symbol.setImageResource(R.drawable.ic_message_black_24dp);
        }
        else if(element.getType() == ContentType.COMMENT_EVENT || element.getType() ==
                ContentType.COMMENT_USER) {
            symbol.setImageResource(R.drawable.ic_comment_black_24dp);
        }
        else if(element.getType() == ContentType.FRIENDSHIP) {
            symbol.setImageResource(R.drawable.ic_people_black_24dp);
        }
        else if(element.getType() == ContentType.USER_REGISTERED) {
            symbol.setImageResource(R.drawable.ic_person_black_24dp);
        }
        else if(element.getType() == ContentType.LIKE_EVENT_COMMENT || element.getType() == ContentType.LIKE_EVENT_POST ||
                element.getType() == ContentType.LIKE_USER_COMMENT || element.getType() == ContentType.LIKE_USER_POST) {
            symbol.setImageResource(R.drawable.ic_thumb_up_black_24dp);
        }

        // Return view for rendering
        return convertView;
    }

    @Override
    public void add(LogElement object) {
        super.add(object);
        sort(comparator);
    }

    @Override
    public void addAll(Collection<? extends LogElement> collection) {
        super.addAll(collection);
        sort(comparator);
    }

    @Override
    public void addAll(LogElement... items) {
        super.addAll(items);
        sort(comparator);
    }
}