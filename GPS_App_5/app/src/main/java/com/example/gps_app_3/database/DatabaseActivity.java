package com.example.gps_app_3.database;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.gps_app_3.R;

public class DatabaseActivity extends AppCompatActivity {

    TextView tvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        tvData = findViewById(R.id.tvData);

        String data = getIntent().getStringExtra("data");
        tvData.setText(data);
    }
}