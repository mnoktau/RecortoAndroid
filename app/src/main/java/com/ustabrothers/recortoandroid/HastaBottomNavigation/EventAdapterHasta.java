package com.ustabrothers.recortoandroid.HastaBottomNavigation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ustabrothers.recortoandroid.HastaBottomNavigation.Model.MyEvents;

import java.util.List;

public class EventAdapterHasta extends ArrayAdapter<MyEvents> {

    private Context context;
    private List<MyEvents> events;

    public EventAdapterHasta(Context context, List<MyEvents> events) {
        super(context, 0, events);
        this.context = context;
        this.events = events;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView eventText = convertView.findViewById(android.R.id.text1);
        eventText.setText(events.get(position).toString());

        return convertView;
    }
}
