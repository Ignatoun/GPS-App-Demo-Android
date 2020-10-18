package com.example.gps_app_3.trip_activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gps_app_3.R;
import com.example.gps_app_3.model.Location;

import java.io.Serializable;
import java.util.ArrayList;

public class TripInfoActivity extends AppCompatActivity {

    TextView tvTripName, tvStartFinish, tvLocations;
    Button btnMap;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_info);

        tvTripName = findViewById(R.id.tvTripName);
        tvStartFinish = findViewById(R.id.tvStartFinish);
        tvLocations = findViewById(R.id.tvLocations);
        btnMap = findViewById(R.id.btnMap);

        Intent intent = getIntent();
        String tripName = intent.getStringExtra("tripName");
        String startDate = intent.getStringExtra("startDate");
        String finishDate = intent.getStringExtra("finishDate");

        Bundle bundle = intent.getBundleExtra("bundle");
        ArrayList<Location> locations = (ArrayList<Location>) bundle.getSerializable("locations");

        String locationsData = "";
        for(int i=0; i<locations.size(); i++) {
            locationsData += "\t\t" + (i+1) + ". Latitude: " + locations.get(i).getLatitude() +
                    "; Longitude: " + locations.get(i).getLongitude() + ";\n";
        }

        tvTripName.setText(tripName);
        tvStartFinish.setText("Start date: " + startDate + "; \nFinishDate: " + finishDate + ";");
        tvLocations.setText("Locations:\n" + locationsData);



        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TripInfoActivity.this, MapActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("locations", (Serializable)locations);
                intent.putExtra("bundle", bundle);

                startActivity(intent);
            }
        });
    }
}