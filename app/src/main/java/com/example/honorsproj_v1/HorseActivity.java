package com.example.honorsproj_v1;

import android.graphics.Color;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import java.util.ArrayList;
import java.util.Random;


public class HorseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horse);
        // Set custom title
        setTitle("Horses Page");

        LineChart lineChart = findViewById(R.id.lineChart);
        RadarChart radarChart = findViewById(R.id.radarChart);

        // Populate LineChart with sample data
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 5));
        entries.add(new Entry(1, 10));
        entries.add(new Entry(2, 15));
        entries.add(new Entry(3, 20));
        entries.add(new Entry(4, 25));
        LineDataSet dataSet = new LineDataSet(entries, "Line Chart Data");
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate();

        // Generate random data for RadarChart
        ArrayList<RadarEntry> radarEntries = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 5; i++) { // Add 5 random data points
            float value = random.nextFloat() * 100; // Random value between 0 and 100
            radarEntries.add(new RadarEntry(value));
        }
        RadarDataSet radarDataSet = new RadarDataSet(radarEntries, "Radar Chart Data");
        radarDataSet.setColor(Color.BLUE);
        radarDataSet.setFillColor(Color.BLUE);
        radarDataSet.setDrawFilled(true);

        // Create RadarData object and set RadarDataSet to it
        RadarData radarData = new RadarData(radarDataSet);

        // Set RadarData to RadarChart
        radarChart.setData(radarData);
        radarChart.invalidate();
    }
}