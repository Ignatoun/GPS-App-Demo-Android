package com.example.gps_app_3;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.gps_app_3.database.DBHelper;
import com.example.gps_app_3.notification_bar.CreateNotification;
import com.example.gps_app_3.notification_bar.OnCLearFromRecentService;
import com.example.gps_app_3.trip_activity.TripsActivity;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSION = 21452;
    Button btnStartLocationScan, btnPauseResumeLocationScan, btnShowDatabase, btnStopLocationScan, btnShowTrips;
    TextView tvLatitude, tvLongitude;
    BroadcastReceiver broadcastReceiver;
    DBHelper dbHelper;
    String latitude, longitude;
    EditText editTextPersonName, editTextTripName;

    static NotificationManager notificationManager;

    boolean pause = false;

    List<String> userNames = new ArrayList<>();
    List<String> tripNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStartLocationScan = findViewById(R.id.btnStart);
        btnPauseResumeLocationScan = findViewById(R.id.btnPauseResumeLocation);
        tvLatitude = findViewById(R.id.tvLatitude);
        tvLongitude = findViewById(R.id.tvLongitude);
        dbHelper = new DBHelper(this);
        btnShowDatabase = findViewById(R.id.btnDatabase);
        btnStopLocationScan = findViewById(R.id.btnStopLocation);
        editTextPersonName = findViewById(R.id.editTextPersonName);
        editTextTripName = findViewById(R.id.editTextTripName);
        btnShowTrips = findViewById(R.id.btnTrips);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
            registerReceiver(broadcastReceiverNotification, new IntentFilter("LOCATION_SERVICE"));
            startService(new Intent(getBaseContext(), OnCLearFromRecentService.class));
        }
        CreateNotification.createNotification(MainActivity.this, pause);


        btnShowTrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextPersonName.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter Name field", Toast.LENGTH_SHORT).show();
                } else {
                    Intent tripIntent = new Intent(MainActivity.this,
                            TripsActivity.class);
                    tripIntent.putExtra("userName", editTextPersonName.getText().toString());
                    startActivity(tripIntent);
                }
            }
        });


        btnStartLocationScan.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_PERMISSION);
                } else if (editTextPersonName.getText().toString().equals("") ||
                                editTextTripName.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Enter all fields", Toast.LENGTH_SHORT).show();
                } else {
                    userNames.add(editTextPersonName.getText().toString());
                    tripNames.add(editTextTripName.getText().toString());
                    startLocationService(userNames.get(userNames.size() - 1),
                            tripNames.get(tripNames.size() - 1));
                }
            }
        });

        broadcastReceiver = new BroadcastReceiver() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onReceive(Context context, Intent intent) {
                latitude = intent.getStringExtra("latitude");
                longitude = intent.getStringExtra("longitude");
                tvLatitude.setVisibility(View.VISIBLE);
                tvLongitude.setVisibility(View.VISIBLE);
                tvLatitude.setText("Latitude: " + latitude);
                tvLongitude.setText("Longitude: " + longitude);
            }
        };

        btnStopLocationScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopLocationService();
                tvLatitude.setVisibility(View.GONE);
                tvLongitude.setVisibility(View.GONE);
            }
        });

        btnShowDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatabase();
            }
        });

        btnPauseResumeLocationScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!pause) {
                    pauseLocationScan();
                } else {
                    resumeLocationScan();
                }
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter("location"));
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CreateNotification.CHANNEL_ID,
                    "channelID", NotificationManager.IMPORTANCE_LOW);

            notificationManager = getSystemService(NotificationManager.class);
            if(notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void pauseLocationScan() {
        if(isLocationServiceRunning()) {
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.setAction(Constants.ACTION_PAUSE_LOCATION_SERVICE);
            pause = true;
            btnPauseResumeLocationScan.setText("Resume Location Scan");
            startService(intent);
        }
    }

    @SuppressLint("SetTextI18n")
    private void resumeLocationScan() {
        if(!isLocationServiceRunning()) {
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.setAction(Constants.ACTION_RESUME_LOCATION_SERVICE);
            pause = false;
            btnPauseResumeLocationScan.setText("Pause Location Scan");
            startService(intent);
        }
    }

    private void showDatabase() {
        Intent intent = new Intent(getApplicationContext(), LocationService.class);
        intent.setAction(Constants.SHOW_DATABASE);
        startService(intent);
        Toast.makeText(this, "Database shown", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                editTextPersonName = findViewById(R.id.editTextPersonName);
                editTextTripName = findViewById(R.id.editTextTripName);

                if(editTextPersonName.getText().toString().equals("") ||
                        editTextTripName.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter all fields", Toast.LENGTH_SHORT).show();
                } else {
                    userNames.add(editTextPersonName.getText().toString());
                    tripNames.add(editTextTripName.getText().toString());
                    startLocationService(userNames.get(userNames.size() - 1),
                            tripNames.get(tripNames.size() - 1));
                }
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isLocationServiceRunning() {
        ActivityManager activityManager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            for (ActivityManager.RunningServiceInfo service :
                    activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (LocationService.class.getName().equals(service.service.getClassName())) {
                    if (service.foreground) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    private void startLocationService(String userName, String tripName) {
        if (!isLocationServiceRunning()) {
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.setAction(Constants.ACTION_START_LOCATION_SERVICE);
            startService(intent);
        }
    }

    private void stopLocationService() {
        if (isLocationServiceRunning()) {
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.setAction(Constants.ACTION_STOP_LOCATION_SERVICE);
            intent.putStringArrayListExtra("userNameList", (ArrayList<String>) userNames);
            intent.putStringArrayListExtra("tripNameList", (ArrayList<String>) tripNames);
            startService(intent);
        }
    }

    BroadcastReceiver broadcastReceiverNotification = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getExtras().getString("actionname");

            if(action != null) {
                switch (action) {
                    case CreateNotification.ACTION_START:
                        editTextPersonName = findViewById(R.id.editTextPersonName);
                        editTextTripName = findViewById(R.id.editTextTripName);

                        if(editTextPersonName.getText().toString().equals("") ||
                                editTextTripName.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(), "Enter all fields", Toast.LENGTH_SHORT).show();
                        } else {
                            userNames.add(editTextPersonName.getText().toString());
                            tripNames.add(editTextTripName.getText().toString());
                            startLocationService(userNames.get(userNames.size() - 1),
                                    tripNames.get(tripNames.size() - 1));
                        }
                        break;
                    case CreateNotification.ACTION_STOP:
                        stopLocationService();
                        break;
                    case CreateNotification.ACTION_PAUSE_OR_RESUME:
                        if (!pause) {
                            pauseLocationScan();
                            CreateNotification.createNotification(MainActivity.this, pause);
                        } else {
                            resumeLocationScan();
                            CreateNotification.createNotification(MainActivity.this, pause);
                        }
                        break;
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.cancelAll();
        }

        unregisterReceiver(broadcastReceiverNotification);
    }
}