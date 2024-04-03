package com.example.honorsproj_v1;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.Spinner;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HorseActivity extends AppCompatActivity {


    String horseName;
    String time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horse); // Start with horse details layout
        // Set custom title
        setTitle("Horses Page");

        // Retrieve string (horse name) from intent
        horseName = getIntent().getStringExtra("selected_horse");
        time = convertTo12HourFormat(getIntent().getStringExtra("selected_race"));
        //time = getIntent().getStringExtra("selected_race");
        Log.d("HorseActivity", "Horse sent is " + horseName);

        // Setup spinner with dropdown options
        Spinner spinner = findViewById(R.id.spinner);
        // Set up listener to retrieve selected item
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedOption = (String) parentView.getItemAtPosition(position);
                Log.d("HorseActivity", "SELECTION IS " + selectedOption);

                // Get the context from the spinner's parent
                Context context = parentView.getContext();

                // Inflate selected layout into the container
                LayoutInflater inflater = LayoutInflater.from(context);
                View inflatedLayout = null;
                switch (selectedOption) {
                    case "Horse Details":
                        inflatedLayout = inflater.inflate(R.layout.horse_details_layout, null); // Pass null as the parent
                        horseDetailInformation(inflatedLayout); // Pass inflatedLayout to the method
                        break;
                    case "Horse Comparison":
                        inflatedLayout = inflater.inflate(R.layout.horse_comparison_layout, (ViewGroup) parentView.getParent(), false);
                        break;
                    case "Race Comparison":
                        inflatedLayout = inflater.inflate(R.layout.race_comparison_layout, (ViewGroup) parentView.getParent(), false);
                        fillRaceComparisonLayout(inflatedLayout); // Fill race comparison layout with data
                        break;
                    default:
                        // Handle unknown selection
                        return;
                }

                // Clear previous views from the container
                ((FrameLayout) findViewById(R.id.container)).removeAllViews();

                // Add the inflated layout to the container
                ((FrameLayout) findViewById(R.id.container)).addView(inflatedLayout);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle case where nothing is selected
            }
        });
    }

    private void fillTextViews(JSONObject horseObject, View inflatedLayout) {
        try {
            // Fill TextViews with matching fields
            ((TextView) inflatedLayout.findViewById(R.id.horseName)).setText("Horse: " + horseObject.getString("horse"));
            ((TextView) inflatedLayout.findViewById(R.id.horseAge)).setText("Age: " + horseObject.getString("age"));
            ((TextView) inflatedLayout.findViewById(R.id.horseSex)).setText("Sex: " + horseObject.getString("sex"));
            ((TextView) inflatedLayout.findViewById(R.id.horseColour)).setText("Colour: " + horseObject.getString("colour"));
            ((TextView) inflatedLayout.findViewById(R.id.horseRegion)).setText("Region: " + horseObject.getString("region"));
            ((TextView) inflatedLayout.findViewById(R.id.horseTrainer)).setText("Trainer: " + horseObject.getString("trainer"));
            ((TextView) inflatedLayout.findViewById(R.id.horseOwner)).setText("Owner(s): " + horseObject.getString("owner"));
            ((TextView) inflatedLayout.findViewById(R.id.horseNumber)).setText("Number: " + horseObject.getString("number"));
            ((TextView) inflatedLayout.findViewById(R.id.horseJockey)).setText("Jockey: " + horseObject.getString("jockey"));
            ((TextView) inflatedLayout.findViewById(R.id.horseLastRun)).setText("Last Run: " + horseObject.getString("last_run"));
            //((TextView) inflatedLayout.findViewById(R.id.horseForm)).setText("Form: " + horseObject.getString("form"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillRaceComparisonLayout(View inflatedLayout) {
        Log.d("HorseActivity", "TIME IS " + time);
        // Read the JSON data from the file
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
            return; // If unable to read data, return without further processing
        }

        try {
            // Parse JSON data
            JSONObject jsonObject = new JSONObject(jsonData.toString());
            JSONArray racecardsArray = jsonObject.getJSONArray("racecards");

            // Iterate through racecards to find the race with matching off_time
            for (int i = 0; i < racecardsArray.length(); i++) {
                JSONObject raceObject = racecardsArray.getJSONObject(i);
                String offTime = raceObject.getString("off_time");

                // If off_time matches the time variable, fill the layout with race details
                if (offTime.equals(time)) {
                    // Fill TextViews with race details
                    ((TextView) inflatedLayout.findViewById(R.id.courseName)).setText("Course: " + raceObject.getString("course"));
                    ((TextView) inflatedLayout.findViewById(R.id.raceName)).setText("Race Name: " + raceObject.getString("race_name"));
                    ((TextView) inflatedLayout.findViewById(R.id.distance)).setText("Distance: " + raceObject.getString("distance_f"));
                    ((TextView) inflatedLayout.findViewById(R.id.raceClass)).setText("Class: " + raceObject.getString("race_class"));
                    ((TextView) inflatedLayout.findViewById(R.id.type)).setText("Type: " + raceObject.getString("type"));
                    ((TextView) inflatedLayout.findViewById(R.id.age)).setText("Age Band: " + raceObject.getString("age_band"));
                    ((TextView) inflatedLayout.findViewById(R.id.prize)).setText("Prize: " + raceObject.getString("prize"));
                    ((TextView) inflatedLayout.findViewById(R.id.fieldSize)).setText("Field-Size: " + raceObject.getString("field_size"));
                    ((TextView) inflatedLayout.findViewById(R.id.going)).setText("Going: " + raceObject.getString("going"));
                    ((TextView) inflatedLayout.findViewById(R.id.surface)).setText("Surface: " + raceObject.getString("surface"));
                    break; // Stop iterating once the matching race is found
                }
            }
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





    private void horseDetailInformation(View inflatedLayout) {

        LineChart lineChart = inflatedLayout.findViewById(R.id.lineChart);

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
                        fillTextViews(horseObject, inflatedLayout);
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

    public String convertTo12HourFormat(String time24) {
        try {
            // Split the input time string into hours and minutes
            String[] parts = time24.split(":");
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);

            // Convert hours to 12-hour format
            int hours12 = hours % 12;
            if (hours12 == 0) {
                hours12 = 12; // For 0 hours in 24-hour format, use 12 in 12-hour format
            }

            // Format the time in 12-hour format
            return String.format("%d:%02d", hours12, minutes); // %02d ensures minutes are always 2 digits
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            // Handle parsing exception if the input string is not in valid format
            e.printStackTrace();
            return null;
        }
    }


}
