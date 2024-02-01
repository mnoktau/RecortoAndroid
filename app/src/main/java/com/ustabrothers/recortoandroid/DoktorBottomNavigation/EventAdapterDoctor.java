package com.ustabrothers.recortoandroid.DoktorBottomNavigation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ustabrothers.recortoandroid.DoktorBottomNavigation.Model.MyEvent;
import com.ustabrothers.recortoandroid.HastaBottomNavigation.Model.MyEvents;

import java.util.List;

public class EventAdapterDoctor extends ArrayAdapter<MyEvent> {

    private Context context;
    private List<MyEvent> events;

    public EventAdapterDoctor(Context context, List<MyEvent> events) {
        super(context, 0, events);
        this.context = context;
        this.events = events;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyEvent event = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView eventText = convertView.findViewById(android.R.id.text1);
        eventText.setText(event.toString());

        return convertView;
    }
}

