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
import java.io.FileReader;
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

    String horseName;
    private String firstResponseJson = "{\"horse\":\"Hawkseye View\",\"id_horse\":\"276810\",\"results\":[{\"date\":\"10-04-2024\",\"position\":\"\",\"course\":\"Market Rasen\",\"distance\":\"2m4f\",\"class\":\"5\",\"weight\":\"12-0\",\"starting_price\":\"15\",\"jockey\":\"William Maggs\",\"trainer\":\"Sue Smith\",\"OR\":\"100\",\"race\":\"RASEN ROCKS FEATURING ELLA HENDERSON HANDICAP HURDLE (5)\",\"prize\":\"\\u00a34093\"},{\"date\":\"28-03-2024\",\"position\":\"10\",\"course\":\"Wetherby\",\"distance\":\"2m5f\",\"class\":\"4\",\"weight\":\"10-11\",\"starting_price\":\"34\",\"jockey\":\"Nick Scholfield\",\"trainer\":\"Sue Smith\",\"OR\":\"103\",\"race\":\"FULL CIRCLE SERIES AT PUNCHESTOWN FESTIVAL HANDICAP HURDLE (4)\",\"prize\":\"\\u00a33961\"},{\"date\":\"23-02-2024\",\"position\":\"4\",\"course\":\"Sedgefield\",\"distance\":\"2m3f\",\"class\":\"4\",\"weight\":\"11-9\",\"starting_price\":\"12\",\"jockey\":\"Ross Chapman\",\"trainer\":\"Sue Smith\",\"OR\":\"105\",\"race\":\"LIVE STREAMING AT YEEEHAAA.BET HANDICAP HURDLE (Qualifier) (4)\",\"prize\":\"\\u00a34066\"},{\"date\":\"28-01-2024\",\"position\":\"6\",\"course\":\"Doncaster\",\"distance\":\"2m3f\",\"class\":\"3\",\"weight\":\"11-10\",\"starting_price\":\"41\",\"jockey\":\"Tom Midgley\",\"trainer\":\"Sue Smith\",\"OR\":\"\",\"race\":\"SBK EBF NATIONAL HUNT MAIDEN HURDLE (Qualifier) (3)\",\"prize\":\"\\u00a36317\"},{\"date\":\"01-01-2024\",\"position\":\"2\",\"course\":\"Catterick Bridge\",\"distance\":\"2m3f\",\"class\":\"4\",\"weight\":\"11-5\",\"starting_price\":\"2.62\",\"jockey\":\"Tom Midgley\",\"trainer\":\"Sue Smith\",\"OR\":\"\",\"race\":\"HAPPY NEW YEAR NOVICES HURDLE (4)\",\"prize\":\"\\u00a34356\"},{\"date\":\"19-12-2023\",\"position\":\"3\",\"course\":\"Catterick Bridge\",\"distance\":\"2m3f\",\"class\":\"4\",\"weight\":\"11-0\",\"starting_price\":\"29\",\"jockey\":\"Tom Midgley\",\"trainer\":\"Sue Smith\",\"OR\":\"\",\"race\":\"BEST ODDS GUARANTEED AT VICKERS.BET NOVICES HURDLE (4)\",\"prize\":\"\\u00a34357\"},{\"date\":\"06-10-2023\",\"position\":\"5\",\"course\":\"Hexham\",\"distance\":\"2m\",\"class\":\"4\",\"weight\":\"10-13\",\"starting_price\":\"19\",\"jockey\":\"Sean Quinlan\",\"trainer\":\"Sue Smith\",\"OR\":\"\",\"race\":\"LANGLEY CASTLE AN OUTSTANDING SMALL HOTEL NATIONAL HUNT NOVICES HURDLE (4)\",\"prize\":\"\\u00a34901\"}]}";
    private String secondResponseJson = "{ \"horse\": \"Dexperado(FR)\", \"id_horse\": \"270546\", \"results\": [ { \"date\": \"10-04-2024\", \"position\": \"2\", \"course\": \"Market Rasen\", \"distance\": \"2m4f\", \"class\": \"5\", \"weight\": \"11-4\", \"starting_price\": \"6.5\", \"jockey\": \"Charlie Hammond\", \"trainer\": \"Nick Kent\", \"OR\": \"90\", \"race\": \"RASEN ROCKS FEATURING ELLA HENDERSON HANDICAP HURDLE (5)\", \"prize\": \"£4093\" }, { \"date\": \"20-03-2024\", \"position\": \"2\", \"course\": \"Market Rasen\", \"distance\": \"2m4f\", \"class\": \"5\", \"weight\": \"11-5\", \"starting_price\": \"7\", \"jockey\": \"Charlie Hammond\", \"trainer\": \"Nick Kent\", \"OR\": \"86\", \"race\": \"FAMILY EXTRAVAGANZA RACEDAY SUNDAY 7th JULY NOVICES LIMITED HANDICAP HURDLE (5)\", \"prize\": \"£7000\" }, { \"date\": \"10-01-2024\", \"position\": \"9\", \"course\": \"Doncaster\", \"distance\": \"2m\", \"class\": \"4\", \"weight\": \"11-4\", \"starting_price\": \"126\", \"jockey\": \"Charlie Hammond\", \"trainer\": \"Nick Kent\", \"OR\": \"\", \"race\": \"DOWNLOAD THE AT THE RACES APP NOVICES HURDLE (4) (D.II)\", \"prize\": \"£4193\" }, { \"date\": \"13-10-2023\", \"position\": \"11\", \"course\": \"Chepstow\", \"distance\": \"2m\", \"class\": \"4\", \"weight\": \"10-13\", \"starting_price\": \"126\", \"jockey\": \"Charlie Hammond\", \"trainer\": \"Nick Kent\", \"OR\": \"\", \"race\": \"CHEPSTOW PLANT INTERNATIONAL NOVICES HURDLE (4)\", \"prize\": \"£4901\" }, { \"date\": \"13-05-2023\", \"position\": \"6\", \"course\": \"Warwick\", \"distance\": \"2m\", \"class\": \"4\", \"weight\": \"10-12\", \"starting_price\": \"34\", \"jockey\": \"Mr Tom Broughton\", \"trainer\": \"Nick Kent\", \"OR\": \"\", \"race\": \"HAPPY HEAVENLY BIRTHDAY FRANK JORDAN NATIONAL HUNT MAIDEN HURDLE (4) (D.I)\", \"prize\": \"£4084\" }, { \"date\": \"06-02-2023\", \"position\": \"6\", \"course\": \"Carlisle\", \"distance\": \"2m1f\", \"class\": \"5\", \"weight\": \"10-9\", \"starting_price\": \"4\", \"jockey\": \"Charlie Hammond\", \"trainer\": \"Nick Kent\", \"OR\": \"\", \"race\": \"LYNE OPEN NATIONAL HUNT FLAT (Qualifier) (5)\", \"prize\": \"£2669\"}]}";

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
        final String[] form1 = {null};
        final String[] form2 = {null};
        LineChart dbllinechart = inflatedLayout.findViewById(R.id.bothLineChart);
        // You can put your code here to fill the horse comparison layout
        // For example, you can retrieve the data of the selected horse from your data source
        // and populate the layout accordingly.

        TextView horseComparisonTextView = inflatedLayout.findViewById(R.id.previousSelectionTextView);
        horseComparisonTextView.setText(horseName);

        // Initialize the HorseViewModel
        HorseViewModel horseViewModel = new ViewModelProvider(this).get(HorseViewModel.class);

//        horseViewModel.getHorseId(horseName, new HorseViewModel.OnHorseIdReceivedListener() {
//            @Override
//            public void onHorseIdReceived(String horseId) {
//                // Handle the received horse ID here
//                Log.d("HorseComparison", "Horse id number is : " + horseId);
//                horseViewModel.queryApiWithHorseId(horseId, new HorseViewModel.OnApiDataReceivedListener() {
//                    @Override
//                    public void onDataReceived(String data) {
//                        // Handle API data received here
//                        Log.d("HorseComparison", "API Response: " + data);
//                        //firstResponseJson = data;
//                    }
//
//                    @Override
//                    public void onFailure() {
//                        // Handle failure here
//                        Log.d("HorseComparison", "Second API Call failed");
//                    }
//                });
//            }
//
//            @Override
//            public void onFailure() {
//                // Handle failure here
//            }
//        });


        form1[0] = extractHorseForm(horseName);
        Log.d("HorseComparison", "FORM IS BAGGING " + form1[0]);
        Log.d("HorseComparison", "FORM IS BAGGING FIXED " + parseForm(form1[0]));
        form1[0] = parseForm(form1[0]).toString();
        plotFormOnLineChart(form1[0],dbllinechart, horseName);




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

                form2[0] = extractHorseForm(selectedHorse);
                Log.d("HorseComparison", "FORM IS BAGGINGz " + form2[0]);
                Log.d("HorseComparison", "FORM IS BAGGINGz FIXED " + parseForm(form2[0]));
                form2[0] = parseForm(form2[0]).toString();
                // Make the API call again with the selected horse
//                horseViewModel.getHorseId(horseName, new HorseViewModel.OnHorseIdReceivedListener() {
//                    @Override
//                    public void onHorseIdReceived(String horseId) {
//                        // Handle the received horse ID here
//                        Log.d("HorseComparison", "Horse id number is : " + horseId);
//                        horseViewModel.queryApiWithHorseId(horseId, new HorseViewModel.OnApiDataReceivedListener() {
//                            @Override
//                            public void onDataReceived(String data) {
//                                // Handle API data received here
//                                Log.d("HorseComparison", "API Response: " + data);
//                                //secondResponseJson = data;
//                            }
//
//                            @Override
//                            public void onFailure() {
//                                // Handle failure here
//                                Log.d("HorseComparison", "Second API Call failed");
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onFailure() {
//                        // Handle failure here
//                    }
//                });


                // Initialize lists to store forms and horse names
                List<String> forms = new ArrayList<>();
                List<String> horseNames = new ArrayList<>();

                // Add the first horse's form and name to the lists
                if (form1[0] != null && horseName != null) {
                    forms.add(form1[0]);
                    horseNames.add(horseName);
                }

                // Add the second horse's form and name to the lists
                if (form2[0] != null && selectedHorse != null) {
                    forms.add(form2[0]);
                    horseNames.add(selectedHorse);
                }

                // Plot multiple forms on the line chart if forms and horseNames are not empty
                if (!forms.isEmpty() && !horseNames.isEmpty()) {
                    plotMultipleFormsOnLineChart(forms, dbllinechart, horseNames);
                } else {
                    // Handle the case when forms or horseNames is empty
                }

                Map<String, Map<String, Map<Integer, String>>> horseData = parseHorseData(firstResponseJson, secondResponseJson);

                // Printing the parsed data
                for (Map.Entry<String, Map<String, Map<Integer, String>>> entry : horseData.entrySet()) {
                    System.out.println("Horse: " + entry.getKey());
                    Map<String, Map<Integer, String>> raceData = entry.getValue();
                    for (Map.Entry<String, Map<Integer, String>> raceEntry : raceData.entrySet()) {
                        System.out.println("Race: " + raceEntry.getKey());
                        Map<Integer, String> positionDistanceMap = raceEntry.getValue();
                        for (Map.Entry<Integer, String> positionDistanceEntry : positionDistanceMap.entrySet()) {
                            System.out.println("Position: " + positionDistanceEntry.getKey() + ", Distance: " + positionDistanceEntry.getValue());
                        }
                    }
                }


            }
        });

    }

    public static Map<String, Map<String, Map<Integer, String>>> parseHorseData(String firstResponseJson, String secondResponseJson) {
        Map<String, Map<String, Map<Integer, String>>> horseData = new HashMap<>();

        try {
            parseHorseJson(firstResponseJson, horseData);
            parseHorseJson(secondResponseJson, horseData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return horseData;
    }


    public static void parseHorseJson(String horseJson, Map<String, Map<String, Map<Integer, String>>> horseData) throws JSONException {
        JSONObject jsonObject = new JSONObject(horseJson);
        String horseName = jsonObject.getString("horse");

        JSONArray resultsArray = jsonObject.getJSONArray("results");
        Map<String, Map<Integer, String>> raceData = horseData.getOrDefault(horseName, new HashMap<>());

        for (int i = 0; i < resultsArray.length(); i++) {
            JSONObject resultObject = resultsArray.getJSONObject(i);
            String raceName = resultObject.getString("race");
            String position = resultObject.getString("position");
            String distance = resultObject.getString("distance");

            Map<Integer, String> positionDistanceMap = raceData.getOrDefault(raceName, new HashMap<>());
            positionDistanceMap.put(parsePosition(position), distance);
            raceData.put(raceName, positionDistanceMap);
        }

        horseData.put(horseName, raceData);
    }

    public static int parsePosition(String positionString) {
        if (positionString.isEmpty()) {
            return -1; // If position is empty, indicating the horse didn't finish the race
        } else {
            return Integer.parseInt(positionString);
        }
    }


    // Method to extract the form of a horse from response_data.json
    public String extractHorseForm(String horseName) {
        StringBuilder form = new StringBuilder();

        try {
            // Read the JSON data from the file
            FileInputStream fis = new FileInputStream(getFilesDir().getPath() + "/response_data.json");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder jsonData = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                jsonData.append(line);
            }
            br.close();

            // Parse JSON data
            JSONObject jsonObject = new JSONObject(jsonData.toString());
            JSONArray racecardsArray = jsonObject.getJSONArray("racecards");

            // Iterate through racecards to find the horse form
            for (int i = 0; i < racecardsArray.length(); i++) {
                JSONObject raceObject = racecardsArray.getJSONObject(i);
                JSONArray runnersArray = raceObject.getJSONArray("runners");

                // Iterate through runners to find the horse
                for (int j = 0; j < runnersArray.length(); j++) {
                    JSONObject horseObject = runnersArray.getJSONObject(j);
                    String name = horseObject.getString("horse");

                    // Check if the horse name matches the passed-in horseName
                    if (name.equals(horseName)) {
                        // Extract the form of the horse
                        form.append(horseObject.optString("form", ""));
                        // Break the loop as we found the horse
                        break;
                    }
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return form.toString();
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
        form = form.replace('P', '9');
        form = form.replace('U', '9');
        form = form.replace('F', '9');
        form = form.replace('R', '9');
        form = form.replace('B', '9');
        form = form.replace('D', '9');

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
    private void plotMultipleFormsOnLineChart(List<String> forms, LineChart lineChart, List<String> horseNames) {
        // Create a LineData object to hold all LineDataSet objects
        LineData lineData = new LineData();

        // Iterate through each form and horse name
        for (int i = 0; i < forms.size(); i++) {
            String form = forms.get(i);
            String horseName = horseNames.get(i);

            // Create entries for the line chart
            ArrayList<Entry> entries = new ArrayList<>();
            int xIndex = 0; // Start index from 0

            // Parse the form string and create entries
            for (int j = 0; j < form.length(); j++) {
                char result = form.charAt(j);
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
            dataSet.setColor(getColorForHorse(i)); // You may need to implement getColorForHorse method to get different colors for each horse
            dataSet.setCircleColor(getColorForHorse(i));
            dataSet.setCircleRadius(5f);
            dataSet.setDrawValues(false); // Disable drawing values on the data points
            dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER); // Set bezier curve interpolation

            // Add the dataset to the LineData object
            lineData.addDataSet(dataSet);
        }

        // Set the LineData to the LineChart
        lineChart.setData(lineData);

        // Refresh chart
        lineChart.invalidate();
    }

    private int getColorForHorse(int index) {
        // Define an array of colors
        int[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.MAGENTA, Color.CYAN};

        // Return the color based on the index
        return colors[index % colors.length];
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
                        Log.d("HorseComparison", "PLOTTED FORM IS " + horseObject.getString("form"));
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
