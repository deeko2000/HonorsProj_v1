package com.example.honorsproj_v1;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class HorseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horse);
        // Set custom title
        setTitle("Horses Page");

        // Retrieve string (horse name) from intent
        String horseName = getIntent().getStringExtra("selected_horse");
        Log.d("HorseActivity", "Horse sent is " + horseName);

        LineChart lineChart = findViewById(R.id.lineChart);

        // Read the contents of the file "response_data.txt"
        StringBuilder jsonData = new StringBuilder();
        try {
            FileInputStream fis = openFileInput("response_data.json");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                jsonData.append(line);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Parse JSON data and fill TextViews with matching fields
        try {
            JSONObject jsonObject = new JSONObject(jsonData.toString());
            JSONArray racecardsArray = jsonObject.getJSONArray("racecards");
            for (int i = 0; i < racecardsArray.length(); i++) {
                JSONObject raceObject = racecardsArray.getJSONObject(i);
                JSONArray runnersArray = raceObject.getJSONArray("runners");
                for (int j = 0; j < runnersArray.length(); j++) {
                    JSONObject horseObject = runnersArray.getJSONObject(j);
                    String name = horseObject.getString("horse");
                    if (name.equals(horseName)) {
                        // Fill TextViews with matching fields
                        fillTextViews(horseObject);
                        // Plot the form on the line chart
                        plotFormOnLineChart(horseObject.getString("form"), lineChart, horseName);
                        break;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillTextViews(JSONObject horseObject) {
        try {
            // Fill TextViews with matching fields
            ((TextView) findViewById(R.id.horseName)).setText("Horse: " + horseObject.getString("horse"));
            ((TextView) findViewById(R.id.horseAge)).setText("Age: " + horseObject.getString("age"));
            ((TextView) findViewById(R.id.horseSex)).setText("Sex: " + horseObject.getString("sex"));
            ((TextView) findViewById(R.id.horseColour)).setText("Colour: " + horseObject.getString("colour"));
            ((TextView) findViewById(R.id.horseRegion)).setText("Region: " + horseObject.getString("region"));
            ((TextView) findViewById(R.id.horseTrainer)).setText("Trainer: " + horseObject.getString("trainer"));
            ((TextView) findViewById(R.id.horseOwner)).setText("Owner(s): " + horseObject.getString("owner"));
            ((TextView) findViewById(R.id.horseNumber)).setText("Number: " + horseObject.getString("number"));
            ((TextView) findViewById(R.id.horseJockey)).setText("Jockey: " + horseObject.getString("jockey"));
            ((TextView) findViewById(R.id.horseLastRun)).setText("Last Run: " + horseObject.getString("last_run")  + " days");
            //((TextView) findViewById(R.id.horseForm)).setText("Form: " + horseObject.getString("form"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void plotFormOnLineChart(String form, LineChart lineChart, String horseName) {
        // Replace '0' with '9' in the form variable
        form = form.replace('0', '9');

        // Check if form string is empty
        if (form.isEmpty()) {
            // Display a message in the center of the graph
            lineChart.setNoDataText("No recent form available for " + horseName);
            lineChart.setNoDataTextColor(Color.BLACK);
            lineChart.setNoDataTextTypeface(Typeface.DEFAULT_BOLD);
            lineChart.invalidate();
            return; // Exit the method
        }

        // Create entries for the line chart
        ArrayList<Entry> entries = new ArrayList<>();
        int xIndex = 0; // Start index from 0

        // Parse the form string and create entries
        for (int i = 0; i < form.length(); i++) {
            char result = form.charAt(i);
            if (Character.isDigit(result)) {
                int value = Character.getNumericValue(result);
                // Add an entry for each digit in the form string
                entries.add(new Entry(xIndex, value));
                xIndex++;
            } else if (result == '-') {
                // Skip the '-' character
                continue;
            }
        }

        // Create a dataset from the entries
        LineDataSet dataSet = new LineDataSet(entries, horseName + "'s Recent Form");
        // Set properties for the dataset (e.g., color)
        dataSet.setColor(Color.RED);
        dataSet.setCircleColor(Color.RED);
        dataSet.setCircleRadius(5f);
        dataSet.setDrawValues(false); // Disable drawing values on the data points
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER); // Set bezier curve interpolation

        // Create a LineData object and set the dataset
        LineData lineData = new LineData(dataSet);

        // Customize the appearance of the chart
        lineChart.getDescription().setEnabled(false); // Hide description
        lineChart.getXAxis().setEnabled(false); // Hide x-axis
        lineChart.getAxisLeft().setGranularity(1f); // Set granularity of y-axis
        lineChart.getAxisLeft().setAxisMinimum(1f); // Set minimum value for y-axis
        lineChart.getAxisLeft().setAxisMaximum(9f); // Set maximum value for y-axis
        lineChart.getAxisRight().setEnabled(false); // Hide right y-axis
        lineChart.getAxisLeft().setInverted(true); // Invert y-axis

        // Add black border
        lineChart.setDrawBorders(true);
        lineChart.setBorderColor(Color.BLACK);
        lineChart.setBorderWidth(2f);

        // Set the LineData to the LineChart
        lineChart.setData(lineData);

        // Customize chart appearance
        lineChart.invalidate(); // Refresh chart
    }
}
