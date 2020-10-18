package com.example.gps_app_3.trip_activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gps_app_3.R;
import com.example.gps_app_3.model.Trip;
import com.example.gps_app_3.model.UserTrip;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.util.List;

public class TripsActivity extends AppCompatActivity {

    ListView listViewTrips;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);

        userName = getIntent().getStringExtra("userName");
        sendGetRequest(userName);
    }

    private void sendGetRequest(String userName) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://10.0.2.2:8080/demo/allUserTrips/" + userName;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        UserTrip userTrip = new UserTrip();
                        try {
                            userTrip = new ObjectMapper().readValue(response, UserTrip.class);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                        List<Trip> tripList = userTrip.getTrips();

                        listViewTrips = findViewById(R.id.listViewTrips);

                        MainAdapter adapter = new MainAdapter(TripsActivity.this, tripList);

                        listViewTrips.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(TripsActivity.this, TripInfoActivity.class);
                                intent.putExtra("tripName", tripList.get(position).getName());
                                intent.putExtra("startDate", tripList.get(position).getStartDate());
                                intent.putExtra("finishDate", tripList.get(position).getFinishDate());

                                Bundle bundle = new Bundle();
                                bundle.putSerializable("locations", (Serializable)tripList.get(position).getLocations());
                                intent.putExtra("bundle", bundle);

                                startActivity(intent);
                            }
                        });
                        listViewTrips.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR TRIPS", String.valueOf(error));
            }
        });

        queue.add(stringRequest);
    }
}