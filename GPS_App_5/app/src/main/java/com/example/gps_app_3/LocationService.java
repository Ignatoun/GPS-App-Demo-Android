package com.example.gps_app_3;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gps_app_3.database.DBHelper;
import com.example.gps_app_3.database.DatabaseActivity;
import com.example.gps_app_3.model.Location;
import com.example.gps_app_3.model.Trip;
import com.example.gps_app_3.model.UserTrip;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LocationService extends Service {

    double latitude, longitude;
    String date;
    DBHelper dbHelper = new DBHelper(this);
    List<UserTrip> userTripList = new ArrayList<>();

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
            if (locationResult != null && locationResult.getLastLocation() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");

                longitude = locationResult.getLastLocation().getLongitude();
                latitude = locationResult.getLastLocation().getLatitude();
                date = sdf.format(new Date(locationResult.getLastLocation().getTime()));
                ContentValues contentValues = new ContentValues();
                contentValues.put(DBHelper.KEY_LATITUDE, latitude);
                contentValues.put(DBHelper.KEY_LONGITUDE, longitude);
                contentValues.put(DBHelper.KEY_DATE, date);
                sqLiteDatabase.insert(DBHelper.LOCATION_TABLE, null, contentValues);
                Log.d("LOCATION_UPDATE", latitude + ", " + longitude + ", " + date);
                sendMessage();

            }

        }
    };

    private void sendMessage() {
        Intent intent = new Intent("location");
        String strLongitude = String.valueOf(longitude);
        String strLatitude = String.valueOf(latitude);
        intent.putExtra("latitude", strLatitude);
        intent.putExtra("longitude", strLongitude);
        LocalBroadcastManager.getInstance(LocationService.this).sendBroadcast(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("");
    }

    private void startLocationService() {
        String channelId = "location_notification_channel";

        final SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        final ContentValues contentValues = new ContentValues();


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent resultIntent = new Intent();

        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext(),
                channelId
        );
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Location Service");
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setContentText("Running");
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(false);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null &&
                    notificationManager.getNotificationChannel(channelId) == null) {
                NotificationChannel notificationChannel = new NotificationChannel(
                        channelId,
                        "Location Service",
                        NotificationManager.IMPORTANCE_HIGH
                );
                notificationChannel.setDescription("This channel is used by location service");
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(4000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

        startForeground(Constants.LOCATION_SERVICE_ID, builder.build());
    }

    private void stopLocationService() {
        dbHelper.close();
        LocationServices.getFusedLocationProviderClient(this)
                .removeLocationUpdates(locationCallback);
        stopForeground(true);
        stopSelf();
    }

    @SuppressLint("LongLogTag")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null) {

            Boolean isPause = intent.getBooleanExtra("isPause", false);
            String action = intent.getAction();


            if(action != null) {
                if(action.equals(Constants.ACTION_START_LOCATION_SERVICE)) {
                    startLocationService();
                    sendGetRequest();
                    Toast.makeText(this, "Location service started", Toast.LENGTH_SHORT).show();
                } else if(action.equals(Constants.SHOW_DATABASE)){
                    dbHelper = new DBHelper(this);
                    SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");

                    if(sqLiteDatabase.isOpen()) {
                        Cursor cursor = sqLiteDatabase.query(DBHelper.LOCATION_TABLE, null,
                                null, null, null, null, null);

                        String data = "";
                        if (cursor.moveToFirst()) {
                            int id = cursor.getColumnIndex(DBHelper.KEY_ID);
                            int latitudeId = cursor.getColumnIndex(DBHelper.KEY_LATITUDE);
                            int longitudeId = cursor.getColumnIndex(DBHelper.KEY_LONGITUDE);
                            int dateId = cursor.getColumnIndex(DBHelper.KEY_DATE);
                            do {
                                data += cursor.getInt(id) + ". Latitude: " + cursor.getString(latitudeId) +
                                        "; Longitude: " + cursor.getString(longitudeId) +
                                        "; Date: " + cursor.getString(dateId) + ";\n";
                            } while (cursor.moveToNext());
                        } else {
                            data = "No data found";
                        }

                        cursor.close();

                        Intent databaseIntent = new Intent(LocationService.this,
                                DatabaseActivity.class);
                        databaseIntent.putExtra("data", data);
                        startActivity(databaseIntent);
                        dbHelper.close();
                    } else {
                        dbHelper = new DBHelper(this);
                        SQLiteDatabase sqLiteDatabase1 = dbHelper.getWritableDatabase();
                        sqLiteDatabase = dbHelper.getWritableDatabase();

                        Cursor cursor = sqLiteDatabase.query(DBHelper.LOCATION_TABLE, null,
                                null, null, null, null, null);

                        String data = "";
                        if (cursor.moveToFirst()) {
                            int id = cursor.getColumnIndex(DBHelper.KEY_ID);
                            int latitudeId = cursor.getColumnIndex(DBHelper.KEY_LATITUDE);
                            int longitudeId = cursor.getColumnIndex(DBHelper.KEY_LONGITUDE);
                            do {
                                data += cursor.getInt(id) + ". Latitude: " + cursor.getString(latitudeId) +
                                        "; Longitude: " + cursor.getString(longitudeId) + ";\n";
                            } while (cursor.moveToNext());
                        } else {
                            data = "No data found";
                        }

                        cursor.close();

                        Intent databaseIntent = new Intent(LocationService.this,
                                DatabaseActivity.class);
                        databaseIntent.putExtra("data", data);
                        startActivity(databaseIntent);

                        dbHelper.close();
                    }
                } else if(action.equals(Constants.ACTION_PAUSE_LOCATION_SERVICE)) {
                    stopLocationService();
                    Toast.makeText(this, "Location service paused", Toast.LENGTH_SHORT).show();
                } else if(action.equals(Constants.ACTION_RESUME_LOCATION_SERVICE)){
                    startLocationService();
                    Toast.makeText(this, "Location service resumed", Toast.LENGTH_SHORT).show();
                } else if(action.equals(Constants.ACTION_STOP_LOCATION_SERVICE)) {
                    stopLocationService();
                    int index = 0;
                    boolean foundExistingUser = false;


                    ArrayList<String> userNameList = intent.getStringArrayListExtra("userNameList");
                    ArrayList<String> tripNameList = intent.getStringArrayListExtra("tripNameList");
                    Log.d("USERNAMES TO STRING", userNameList.toString());


                    //  GET REQUEST TO SERVER IF THERE ARE EQUAL USERNAMES
                    Log.d("GET REQUEST USERS FORM SERVER", userTripList.toString());

                    for(int i=0; i<userTripList.size(); i++) {
                        if(userTripList.get(i).getUserName().equals(userNameList.get(userNameList.size()-1))) {
                            Log.d("FOUND EXISTING USER", userTripList.get(i).toString());
                            userNameList.remove(userNameList.size() - 1);
                            foundExistingUser = true;
                            index = i;
                            break;
                        }
                    }

                    if(foundExistingUser) {
                        //  CREATE UPDATED USER
                        UserTrip userTripUpdate = userTripList.get(index);

                        //  CREATE NEW TRIP FOR UPDATED USER
                        List<String> dateList = new ArrayList<>();
                        final Trip trip = new Trip(tripNameList.get(tripNameList.size()-1));


                        //  POST REQUEST CODE COPY
                        dbHelper = new DBHelper(this);
                        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
                        sqLiteDatabase = dbHelper.getWritableDatabase();

                        final Cursor cursor = sqLiteDatabase.query(DBHelper.LOCATION_TABLE, null,
                                null, null, null, null, null);

                        if (cursor.moveToFirst()) {
                            int id = cursor.getColumnIndex(DBHelper.KEY_ID);
                            final int latitudeId = cursor.getColumnIndex(DBHelper.KEY_LATITUDE);
                            final int longitudeId = cursor.getColumnIndex(DBHelper.KEY_LONGITUDE);
                            final int dateId = cursor.getColumnIndex(DBHelper.KEY_DATE);
                            do {

                                final String latitude = cursor.getString(latitudeId);
                                final String longitude = cursor.getString(longitudeId);
                                final String date = cursor.getString(dateId);

                                trip.getLocations().add(new Location(latitude, longitude));
                                dateList.add(date);

                            } while (cursor.moveToNext());

                        } else {
                            Log.i("Error.POST", "Something went wrong");
                        }
                        cursor.close();
                        dbHelper.close();


                        trip.setStartDate(dateList.get(0));
                        trip.setFinishDate(dateList.get(dateList.size() - 1));


                        userTripUpdate.getTrips().add(trip);
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                        String mRequestBody = "";
                        ObjectMapper mapper = new ObjectMapper();
                        try { mRequestBody = mapper.writeValueAsString(userTripUpdate);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }

                        String url = "http://10.0.2.2:8080/demo/allUserTrips/" + userTripUpdate.getId();
                        String finalMRequestBody = mRequestBody;

                        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {


                                Log.i("LOG_RESPONSE", response);
                            }
                        }, error -> Log.e("LOG_VOLLEY", error.toString())) {
                            @Override
                            public String getBodyContentType() {
                                return "application/json";
                            }

                            @Override
                            public byte[] getBody() throws AuthFailureError {
                                try {
                                    return finalMRequestBody == null ? null : finalMRequestBody.getBytes("utf-8");
                                } catch (UnsupportedEncodingException uee) {
                                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", finalMRequestBody, "utf-8");
                                    return null;
                                }
                            }

                            @Override
                            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                                String responseString = "";
                                if (response != null) {

                                    responseString = String.valueOf(response.statusCode);

                                }
                                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                            }
                        };
                        requestQueue.add(stringRequest);
                    } else {
                        List<String> dateList = new ArrayList<>();
                        final Trip trip = new Trip(tripNameList.get(tripNameList.size()-1));

                        dbHelper = new DBHelper(this);
                        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
                        sqLiteDatabase = dbHelper.getWritableDatabase();

                        final Cursor cursor = sqLiteDatabase.query(DBHelper.LOCATION_TABLE, null,
                                null, null, null, null, null);

                        if (cursor.moveToFirst()) {
                            int id = cursor.getColumnIndex(DBHelper.KEY_ID);
                            final int latitudeId = cursor.getColumnIndex(DBHelper.KEY_LATITUDE);
                            final int longitudeId = cursor.getColumnIndex(DBHelper.KEY_LONGITUDE);
                            final int dateId = cursor.getColumnIndex(DBHelper.KEY_DATE);
                            do {

                                final String latitude = cursor.getString(latitudeId);
                                final String longitude = cursor.getString(longitudeId);
                                final String date = cursor.getString(dateId);

                                trip.getLocations().add(new Location(latitude, longitude));
                                dateList.add(date);

                            } while (cursor.moveToNext());

                        } else {
                            Log.i("Error.POST", "Something went wrong");
                        }
                        cursor.close();
                        dbHelper.close();


                        trip.setStartDate(dateList.get(0));
                        trip.setFinishDate(dateList.get(dateList.size() - 1));


                        UserTrip userTrip = new UserTrip(userNameList.get(userNameList.size() - 1));
                        userTrip.getTrips().add(trip);
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                        String mRequestBody = "";
                        ObjectMapper mapper = new ObjectMapper();
                        try { mRequestBody = mapper.writeValueAsString(userTrip);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }

                        String url = "http://10.0.2.2:8080/demo/allUserTrips";
                        String finalMRequestBody = mRequestBody;

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {


                                Log.i("LOG_RESPONSE", response);
                            }
                        }, error -> Log.e("LOG_VOLLEY", error.toString())) {
                            @Override
                            public String getBodyContentType() {
                                return "application/json";
                            }

                            @Override
                            public byte[] getBody() throws AuthFailureError {
                                try {
                                    return finalMRequestBody == null ? null : finalMRequestBody.getBytes("utf-8");
                                } catch (UnsupportedEncodingException uee) {
                                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", finalMRequestBody, "utf-8");
                                    return null;
                                }
                            }

                            @Override
                            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                                String responseString = "";
                                if (response != null) {

                                    responseString = String.valueOf(response.statusCode);

                                }
                                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                            }
                        };
                        requestQueue.add(stringRequest);
                    }



                    Toast.makeText(this, "Location service stopped", Toast.LENGTH_SHORT).show();
                    getApplicationContext().deleteDatabase(DBHelper.DATABASE_NAME);
                }
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @SuppressLint("LongLogTag")
    private void sendGetRequest() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://10.0.2.2:8080/demo/allUserTrips";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onResponse(String response) {
                        try {
                            ObjectMapper mapper = new ObjectMapper();
                            CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, UserTrip.class);
                            userTripList = mapper.readValue(response, listType);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
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
