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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.security.auth.callback.Callback;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HorseActivity extends AppCompatActivity implements LifecycleOwner {


    String tempDataHorse1json ="";

    String tempDataHorse2json = "{\n" +
            "  \"horse\": \"Sporting Hero\",\n" +
            "  \"id_horse\": \"266226\",\n" +
            "  \"results\": [\n" +
            "    {\n" +
            "      \"date\": \"05-04-2024\",\n" +
            "      \"position\": \"11\",\n" +
            "      \"course\": \"Dundalk\",\n" +
            "      \"distance\": \"6f\",\n" +
            "      \"class\": \"\",\n" +
            "      \"weight\": \"10-2\",\n" +
            "      \"starting_price\": \"34\",\n" +
            "      \"jockey\": \"D. King\",\n" +
            "      \"trainer\": \"Denis Hogan, Ireland\",\n" +
            "      \"OR\": \"82\",\n" +
            "      \"race\": \"BOOK ONLINE AT dundalkstadium.com HANDICAP\",\n" +
            "      \"prize\": \"€12000\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"date\": \"03-11-2023\",\n" +
            "      \"position\": \"7\",\n" +
            "      \"course\": \"Newcastle\",\n" +
            "      \"distance\": \"6f\",\n" +
            "      \"class\": \"3\",\n" +
            "      \"weight\": \"9-4\",\n" +
            "      \"starting_price\": \"12\",\n" +
            "      \"jockey\": \"Billy Loughnane\",\n" +
            "      \"trainer\": \"George Boughey\",\n" +
            "      \"OR\": \"83\",\n" +
            "      \"race\": \"BETUK. HOME OF THE ACCA-FENWA HANDICAP (3)\",\n" +
            "      \"prize\": \"£8208\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"date\": \"07-10-2023\",\n" +
            "      \"position\": \"8\",\n" +
            "      \"course\": \"Wolverhampton\",\n" +
            "      \"distance\": \"6f\",\n" +
            "      \"class\": \"3\",\n" +
            "      \"weight\": \"9-5\",\n" +
            "      \"starting_price\": \"13\",\n" +
            "      \"jockey\": \"Billy Loughnane\",\n" +
            "      \"trainer\": \"George Boughey\",\n" +
            "      \"OR\": \"84\",\n" +
            "      \"race\": \"bet365 HANDICAP (3)\",\n" +
            "      \"prize\": \"£7956\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"date\": \"13-09-2023\",\n" +
            "      \"position\": \"3\",\n" +
            "      \"course\": \"Southwell\",\n" +
            "      \"distance\": \"4f\",\n" +
            "      \"class\": \"3\",\n" +
            "      \"weight\": \"8-12\",\n" +
            "      \"starting_price\": \"9\",\n" +
            "      \"jockey\": \"Daniel Muscutt\",\n" +
            "      \"trainer\": \"George Boughey\",\n" +
            "      \"OR\": \"82\",\n" +
            "      \"race\": \"WILLIAM HILL EXTRA PLACE RACES DAILY RACING LEAGUE RACE 41 HANDICAP (3)\",\n" +
            "      \"prize\": \"£25770\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"date\": \"20-05-2023\",\n" +
            "      \"position\": \"7\",\n" +
            "      \"course\": \"Doncaster\",\n" +
            "      \"distance\": \"6f\",\n" +
            "      \"class\": \"4\",\n" +
            "      \"weight\": \"9-9\",\n" +
            "      \"starting_price\": \"11\",\n" +
            "      \"jockey\": \"Callum Shepherd\",\n" +
            "      \"trainer\": \"George Boughey\",\n" +
            "      \"OR\": \"85\",\n" +
            "      \"race\": \"SKY SPORTS RACING SKY 415 HANDICAP (4)\",\n" +
            "      \"prize\": \"£5653\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"date\": \"06-05-2023\",\n" +
            "      \"position\": \"6\",\n" +
            "      \"course\": \"Thirsk\",\n" +
            "      \"distance\": \"5f\",\n" +
            "      \"class\": \"3\",\n" +
            "      \"weight\": \"9-2\",\n" +
            "      \"starting_price\": \"4\",\n" +
            "      \"jockey\": \"Jason Watson\",\n" +
            "      \"trainer\": \"George Boughey\",\n" +
            "      \"OR\": \"85\",\n" +
            "      \"race\": \"VICKERS.BET CORONATION TROPHY HANDICAP (3)\",\n" +
            "      \"prize\": \"£10800\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"date\": \"11-10-2022\",\n" +
            "      \"position\": \"1\",\n" +
            "      \"course\": \"Newcastle\",\n" +
            "      \"distance\": \"5f\",\n" +
            "      \"class\": \"5\",\n" +
            "      \"weight\": \"9-11\",\n" +
            "      \"starting_price\": \"1.9\",\n" +
            "      \"jockey\": \"Jamie Spencer\",\n" +
            "      \"trainer\": \"George Boughey\",\n" +
            "      \"OR\": \"\",\n" +
            "      \"race\": \"EBF NOVICE STAKES (5)\",\n" +
            "      \"prize\": \"£3672\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"date\": \"31-08-2022\",\n" +
            "      \"position\": \"1\",\n" +
            "      \"course\": \"Lingfield Park\",\n" +
            "      \"distance\": \"5f\",\n" +
            "      \"class\": \"5\",\n" +
            "      \"weight\": \"9-7\",\n" +
            "      \"starting_price\": \"3.25\",\n" +
            "      \"jockey\": \"Andrea Atzeni\",\n" +
            "      \"trainer\": \"George Boughey\",\n" +
            "      \"OR\": \"\",\n" +
            "      \"race\": \"LEGER FESTIVAL ON SKY SPORTS RACING NOVICE STAKES (5)\",\n" +
            "      \"prize\": \"£3672\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"date\": \"29-07-2022\",\n" +
            "      \"position\": \"5\",\n" +
            "      \"course\": \"Newmarket\",\n" +
            "      \"distance\": \"6f\",\n" +
            "      \"class\": \"4\",\n" +
            "      \"weight\": \"9-4\",\n" +
            "      \"starting_price\": \"21\",\n" +
            "      \"jockey\": \"Jamie Spencer\",\n" +
            "      \"trainer\": \"George Boughey\",\n" +
            "      \"OR\": \"\",\n" +
            "      \"race\": \"WATCH LIVE ON RACING TV BRITISH EBF NOVICE STAKES (4)\",\n" +
            "      \"prize\": \"£4320\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";

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
        Log.d("HorseActivity", "Time sent is " + getIntent().getStringExtra("selected_race"));

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
                        fillHorseComparisonLayout(inflatedLayout);

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

    private void fillHorseComparisonLayout(View inflatedLayout) {
        // You can put your code here to fill the horse comparison layout
        // For example, you can retrieve the data of the selected horse from your data source
        // and populate the layout accordingly.

        TextView horseComparisonTextView = inflatedLayout.findViewById(R.id.previousSelectionTextView);
        horseComparisonTextView.setText(horseName);

        // Initialize the HorseViewModel
        HorseViewModel horseViewModel = new ViewModelProvider(this).get(HorseViewModel.class);

        horseViewModel.getHorseId(horseName, new HorseViewModel.OnHorseIdReceivedListener() {
            @Override
            public void onHorseIdReceived(String horseId) {
                // Handle the received horse ID here
                Log.d("HorseComparison", "Horse id number is : " + horseId);
                horseViewModel.queryApiWithHorseId(horseId);
            }
            @Override
            public void onFailure() {
                // Handle failure here
            }
        });




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

        List<String> remainingHorses = new ArrayList<>(); // List to store remaining horses with numbers
        try {
            // Parse JSON data
            JSONObject jsonObject = new JSONObject(jsonData.toString());
            JSONArray racecardsArray = jsonObject.getJSONArray("racecards");

            // Find the race with matching off_time
            JSONObject targetRace = null;
            for (int i = 0; i < racecardsArray.length(); i++) {
                JSONObject raceObject = racecardsArray.getJSONObject(i);
                String offTime = raceObject.getString("off_time");
                if (offTime.equals(time)) {
                    targetRace = raceObject;
                    break;
                }
            }

            if (targetRace != null) {
                // Get the array of runners in the race
                JSONArray runnersArray = targetRace.getJSONArray("runners");

                // Remove the horse with matching name
                JSONArray updatedRunnersArray = new JSONArray();
                for (int i = 0; i < runnersArray.length(); i++) {
                    JSONObject horseObject = runnersArray.getJSONObject(i);
                    String horseNameInRace = horseObject.getString("horse");
                    if (!horseNameInRace.equals(horseName)) {
                        // Add the horse name with number to the list
                        int horseNumber = i + 1;
                        remainingHorses.add(horseNumber + " " + horseNameInRace);
                        // Add the horse to the updated runners array
                        JSONObject updatedHorseObject = new JSONObject();
                        updatedHorseObject.put("horse", horseNameInRace);
                        updatedRunnersArray.put(updatedHorseObject);
                    }
                }

                // Print the names of remaining horses in the race
                for (int i = 0; i < updatedRunnersArray.length(); i++) {
                    JSONObject horseObject = updatedRunnersArray.getJSONObject(i);
                    String horseName = horseObject.getString("horse");
                    Log.d("HorseComparison", "Horse in the race: " + (i+1) + ". " + horseName);
                    // You can do further processing with the remaining horse names as needed
                }
            } else {
                Log.d("HorseComparison", "No race found at the specified time.");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create an ArrayAdapter with the list of remaining horse names
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, remainingHorses);

        // Find the AutoCompleteTextView
        AutoCompleteTextView autoCompleteTextView = inflatedLayout.findViewById(R.id.auto_complete_horseCompView);

        // Set the adapter to the AutoCompleteTextView
        autoCompleteTextView.setAdapter(adapter);

        // Set OnClickListener for the AutoCompleteTextView


        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedHorseWithNumber = (String) parent.getItemAtPosition(position);
                // Split the string based on space
                String[] parts = selectedHorseWithNumber.split(" ", 2);
                // Extract only the text part (excluding the number)
                final String selectedHorse = parts[1];
                // Perform actions based on the selected horse
                // For example:
                Toast.makeText(getApplicationContext(), "Selected: " + selectedHorse, Toast.LENGTH_SHORT).show();

                // Make the API call again with the selected horse
                horseViewModel.getHorseId(selectedHorse, new HorseViewModel.OnHorseIdReceivedListener() {
                    @Override
                    public void onHorseIdReceived(String horseId) {
                        // Handle the received horse ID here
                        Log.d("HorseComparison", "Horse id number is : " + horseId);
                        horseViewModel.queryApiWithHorseId(horseId);
                    }
                    @Override
                    public void onFailure() {
                        // Handle failure here
                    }
                });
            }
        });

        try {
            // Parse data from the first JSON string
            JSONObject horse1Json = new JSONObject(tempDataHorse1json);
            String horse1Name = horse1Json.getString("horse");
            JSONArray horse1Results = horse1Json.getJSONArray("results");

            // Parse data from the second JSON string
            JSONObject horse2Json = new JSONObject(tempDataHorse2json);
            String horse2Name = horse2Json.getString("horse");
            JSONArray horse2Results = horse2Json.getJSONArray("results");

            // Combine race results of both horses
            JSONArray combinedResults = new JSONArray();
            for (int i = 0; i < horse1Results.length(); i++) {
                combinedResults.put(horse1Results.getJSONObject(i));
            }
            for (int i = 0; i < horse2Results.length(); i++) {
                combinedResults.put(horse2Results.getJSONObject(i));
            }

            // Calculate average positions for each horse across different race distances
            Map<String, Map<String, List<Integer>>> horseAveragePositions = new HashMap<>();
            for (int i = 0; i < combinedResults.length(); i++) {
                JSONObject raceResult = combinedResults.getJSONObject(i);
                String horseName = raceResult.getString("horse");
                String distance = raceResult.getString("distance");
                int position = Integer.parseInt(raceResult.getString("position"));

                // Initialize the inner map if it doesn't exist
                horseAveragePositions.putIfAbsent(horseName, new HashMap<>());
                // Initialize the list if it doesn't exist for the specific distance
                horseAveragePositions.get(horseName).putIfAbsent(distance, new ArrayList<>());
                // Add the position to the list for the specific distance
                horseAveragePositions.get(horseName).get(distance).add(position);
            }

            // Calculate the average position for each horse at each race distance
            Map<String, Map<String, Double>> horseAveragePositionsResult = new HashMap<>();
            for (Map.Entry<String, Map<String, List<Integer>>> horseEntry : horseAveragePositions.entrySet()) {
                String horseName = horseEntry.getKey();
                Map<String, List<Integer>> distancesMap = horseEntry.getValue();
                Map<String, Double> avgPositionMap = new HashMap<>();
                for (Map.Entry<String, List<Integer>> distanceEntry : distancesMap.entrySet()) {
                    String distance = distanceEntry.getKey();
                    List<Integer> positions = distanceEntry.getValue();
                    double avgPosition = positions.stream().mapToInt(Integer::intValue).average().orElse(0.0);
                    avgPositionMap.put(distance, avgPosition);
                }
                horseAveragePositionsResult.put(horseName, avgPositionMap);
            }

            // Print the calculated average positions
            for (Map.Entry<String, Map<String, Double>> horseEntry : horseAveragePositionsResult.entrySet()) {
                String horseName = horseEntry.getKey();
                Map<String, Double> avgPositionMap = horseEntry.getValue();
                System.out.println("Average positions for " + horseName + ":");
                for (Map.Entry<String, Double> distanceEntry : avgPositionMap.entrySet()) {
                    String distance = distanceEntry.getKey();
                    double avgPosition = distanceEntry.getValue();
                    System.out.println(" - Distance: " + distance + ", Average Position: " + avgPosition);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Handle the exception or return gracefully
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

            // Find the race with matching off_time
            JSONObject targetRace = null;
            for (int i = 0; i < racecardsArray.length(); i++) {
                JSONObject raceObject = racecardsArray.getJSONObject(i);
                String offTime = raceObject.getString("off_time");
                if (offTime.equals(time)) {
                    targetRace = raceObject;
                    break;
                }
            }

            if (targetRace != null) {
                // Fill layout with race details
                ((TextView) inflatedLayout.findViewById(R.id.courseName)).setText("Course: " + targetRace.getString("course"));
                ((TextView) inflatedLayout.findViewById(R.id.raceName)).setText("Race Name: " + targetRace.getString("race_name"));
                ((TextView) inflatedLayout.findViewById(R.id.distance)).setText("Distance: " + targetRace.getString("distance_f"));
                ((TextView) inflatedLayout.findViewById(R.id.raceClass)).setText("Class: " + targetRace.getString("race_class"));
                ((TextView) inflatedLayout.findViewById(R.id.type)).setText("Type: " + targetRace.getString("type"));
                ((TextView) inflatedLayout.findViewById(R.id.age)).setText("Age Band: " + targetRace.getString("age_band"));
                ((TextView) inflatedLayout.findViewById(R.id.prize)).setText("Prize: " + targetRace.getString("prize"));
                ((TextView) inflatedLayout.findViewById(R.id.fieldSize)).setText("Field-Size: " + targetRace.getString("field_size"));
                ((TextView) inflatedLayout.findViewById(R.id.going)).setText("Going: " + targetRace.getString("going"));
                ((TextView) inflatedLayout.findViewById(R.id.surface)).setText("Surface: " + targetRace.getString("surface"));

                // Calculate average form of each horse in the race
                JSONArray runnersArray = targetRace.getJSONArray("runners");
                Map<String, List<Integer>> horseForms = new HashMap<>();
                for (int i = 0; i < runnersArray.length(); i++) {
                    JSONObject horseObject = runnersArray.getJSONObject(i);
                    String horseName = horseObject.getString("horse");
                    String form = horseObject.getString("form");
                    List<Integer> formList = parseForm(form);
                    horseForms.put(horseName, formList);
                }

                // Display average form of each horse
                for (Map.Entry<String, List<Integer>> entry : horseForms.entrySet()) {
                    String horseName = entry.getKey();
                    List<Integer> formList = entry.getValue();
                    double averageForm = calculateAverageForm(formList);
                    Log.d("HorseActivity", "Horse: " + horseName + ", Average Form: " + averageForm);
                }
                // Get reference to the ScatterChart
                ScatterChart scatterChart = inflatedLayout.findViewById(R.id.scatter_chart);

                // Plot average form values on ScatterChart
                plotAverageFormOnScatterChart(horseForms, scatterChart);

            } else {
                Log.d("HorseActivity", "No race found at the specified time.");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Method to parse form string into a list of integers
    private List<Integer> parseForm(String form) {
        List<Integer> formList = new ArrayList<>();
        for (int i = 0; i < form.length(); i++) {
            char result = form.charAt(i);
            if (Character.isDigit(result) && result != '0') {
                int value = Character.getNumericValue(result);
                formList.add(value);
            } else {
                // Check if the character represents poor performance
                if (result == 'P' || result == 'U' || result == '0') {
                    formList.add(9);
                }
                // Check if the character represents non-participation or unknown performance
                else if (result == 'F' || result == 'R' || result == 'B' || result == 'D') {
                    formList.add(9);
                }
            }
        }
        return formList;
    }


    // Method to calculate average form of a horse
    private double calculateAverageForm(List<Integer> formList) {
        if (formList.isEmpty()) {
            return 0;
        }
        int sum = 0;
        for (int form : formList) {
            sum += form;
        }
        return (double) sum / formList.size();
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

    private void plotAverageFormOnScatterChart(Map<String, List<Integer>> horseForms, ScatterChart scatterChart) {
        // Create entries for the scatter chart
        ArrayList<Entry> entries = new ArrayList<>();

        // Iterate over the horse names and their corresponding form lists
        for (Map.Entry<String, List<Integer>> formEntry : horseForms.entrySet()) {
            String horseName = formEntry.getKey();
            List<Integer> formList = formEntry.getValue();

            // Calculate the average form value
            double averageForm = calculateAverageForm(formList);

            // Concatenate horse's name and form into a single string
            String data = horseName + ": " + averageForm;

            // Add an entry for each horse with its average form value and horse name as data
            Entry entry = new Entry(entries.size(), (float) averageForm);
            entry.setData(data);
            entries.add(entry);
        }

        // Create a dataset from the entries
        ScatterDataSet dataSet = new ScatterDataSet(entries, "Average placing based on form");
        // Set properties for the dataset (e.g., color)
        dataSet.setColor(Color.BLUE);
        dataSet.setScatterShape(ScatterChart.ScatterShape.CIRCLE); // Set scatter shape to circle
        dataSet.setScatterShapeSize(10f); // Set size of scatter shape
        dataSet.setDrawValues(false); // Disable drawing values on the data points

        // Create a ScatterData object and set the dataset
        ScatterData scatterData = new ScatterData(dataSet);

        // Customize the appearance of the chart
        scatterChart.getDescription().setEnabled(false); // Hide description
        scatterChart.getXAxis().setEnabled(false); // Hide x-axis
        scatterChart.getAxisLeft().setGranularity(1f); // Set granularity of y-axis
        scatterChart.getAxisLeft().setAxisMinimum(0f); // Set minimum value for y-axis
        scatterChart.getAxisLeft().setAxisMaximum(20f); // Set maximum value for y-axis
        scatterChart.getAxisRight().setEnabled(false); // Hide right y-axis
        scatterChart.getAxisLeft().setInverted(true); // Invert y-axis

        // Add black border
        scatterChart.setDrawBorders(true);
        scatterChart.setBorderColor(Color.BLACK);
        scatterChart.setBorderWidth(2f);

        // Set the ScatterData to the ScatterChart
        scatterChart.setData(scatterData);

        // Set custom marker view
        CustomMarkerView markerView = new CustomMarkerView(this, R.layout.custom_marker_view);
        markerView.setChartView(scatterChart);
        scatterChart.setMarker(markerView);

        // Customize chart appearance
        scatterChart.invalidate(); // Refresh chart
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
