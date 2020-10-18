package com.example.gps_app_3.trip_activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.gps_app_3.R;
import com.example.gps_app_3.model.Trip;

import java.util.List;

public class MainAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<Trip> tripList;

    public MainAdapter(Context c, List<Trip> tripList) {
        context = c;
        this.tripList = tripList;
    }

    @Override
    public int getCount() {
        return tripList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.
                    LAYOUT_INFLATER_SERVICE);
        }

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, null);
        }

        TextView tripName = convertView.findViewById(R.id.tripName);
        TextView tripStartFinish = convertView.findViewById(R.id.tripStartFinish);

        tripName.setText(position+1 + ". " + tripList.get(position).getName());
        tripStartFinish.setText("Start: " + tripList.get(position).getStartDate() +
                "\nFinish: " + tripList.get(position).getFinishDate());

        return convertView;
    }
}
