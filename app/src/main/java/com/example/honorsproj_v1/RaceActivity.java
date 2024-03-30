package com.example.honorsproj_v1;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class RaceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_comparison);
        // Set custom title
        setTitle("Race Comparison");
    }
}
