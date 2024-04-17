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
    private String firstResponseJson = null;
    private String secondResponseJson = null;
    String time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horse);
        setTitle("Horses Page");
        horseName = getIntent().getStringExtra("selected_horse");
        time = convertTo12HourFormat(getIntent().getStringExtra("selected_race"));
        Log.d("HorseActivity", "Horse sent is " + horseName);
        Log.d("HorseActivity", "Time sent is " + getIntent().getStringExtra("selected_race"));
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedOption = (String) parentView.getItemAtPosition(position);
                Log.d("HorseActivity", "SELECTION IS " + selectedOption);
                Context context = parentView.getContext();
                LayoutInflater inflater = LayoutInflater.from(context);
                View inflatedLayout = null;
                switch (selectedOption) {
                    case "Horse Details":
                        inflatedLayout = inflater.inflate(R.layout.horse_details_layout, null);
                        horseDetailInformation(inflatedLayout);
                        break;
                    case "Horse Comparison":
                        inflatedLayout = inflater.inflate(R.layout.horse_comparison_layout, (ViewGroup) parentView.getParent(), false);
                        fillHorseComparisonLayout(inflatedLayout);

                        break;
                    case "Race Comparison":
                        inflatedLayout = inflater.inflate(R.layout.race_comparison_layout, (ViewGroup) parentView.getParent(), false);
                        fillRaceComparisonLayout(inflatedLayout);
                        break;
                    default:
                        return;
                }


                ((FrameLayout) findViewById(R.id.container)).removeAllViews();


                ((FrameLayout) findViewById(R.id.container)).addView(inflatedLayout);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    private void fillTextViews(JSONObject horseObject, View inflatedLayout) {
        try {

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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillHorseComparisonLayout(View inflatedLayout) {
        final String[] form1 = {null};
        final String[] form2 = {null};
        LineChart dbllinechart = inflatedLayout.findViewById(R.id.bothLineChart);


        TextView horseComparisonTextView = inflatedLayout.findViewById(R.id.previousSelectionTextView);
        horseComparisonTextView.setText(horseName);

        HorseViewModel horseViewModel = new ViewModelProvider(this).get(HorseViewModel.class);

        horseViewModel.getHorseId(horseName, new HorseViewModel.OnHorseIdReceivedListener() {
            @Override
            public void onHorseIdReceived(String horseId) {

                Log.d("HorseComparison", "Horse id number is : " + horseId);
                horseViewModel.queryApiWithHorseId(horseId, new HorseViewModel.OnApiDataReceivedListener() {
                    @Override
                    public void onDataReceived(String data) {

                        Log.d("HorseComparison", "API Response: " + data);
                        firstResponseJson = data;
                    }

                    @Override
                    public void onFailure() {

                        Log.d("HorseComparison", "Second API Call failed");
                    }
                });
            }

            @Override
            public void onFailure() {

            }
        });


        form1[0] = extractHorseForm(horseName);
        Log.d("HorseComparison", "FORM IS BAGGING " + form1[0]);
        Log.d("HorseComparison", "FORM IS BAGGING FIXED " + parseForm(form1[0]));
        form1[0] = parseForm(form1[0]).toString();
        plotFormOnLineChart(form1[0],dbllinechart, horseName);





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
            return;
        }

        List<String> remainingHorses = new ArrayList<>();
        try {

            JSONObject jsonObject = new JSONObject(jsonData.toString());
            JSONArray racecardsArray = jsonObject.getJSONArray("racecards");


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

                JSONArray runnersArray = targetRace.getJSONArray("runners");


                JSONArray updatedRunnersArray = new JSONArray();
                for (int i = 0; i < runnersArray.length(); i++) {
                    JSONObject horseObject = runnersArray.getJSONObject(i);
                    String horseNameInRace = horseObject.getString("horse");
                    if (!horseNameInRace.equals(horseName)) {

                        int horseNumber = i + 1;
                        remainingHorses.add(horseNumber + " " + horseNameInRace);

                        JSONObject updatedHorseObject = new JSONObject();
                        updatedHorseObject.put("horse", horseNameInRace);
                        updatedRunnersArray.put(updatedHorseObject);
                    }
                }


                for (int i = 0; i < updatedRunnersArray.length(); i++) {
                    JSONObject horseObject = updatedRunnersArray.getJSONObject(i);
                    String horseName = horseObject.getString("horse");
                    Log.d("HorseComparison", "Horse in the race: " + (i+1) + ". " + horseName);
                }
            } else {
                Log.d("HorseComparison", "No race found at the specified time.");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, remainingHorses);


        AutoCompleteTextView autoCompleteTextView = inflatedLayout.findViewById(R.id.auto_complete_horseCompView);


        autoCompleteTextView.setAdapter(adapter);




        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedHorseWithNumber = (String) parent.getItemAtPosition(position);

                String[] parts = selectedHorseWithNumber.split(" ", 2);

                final String selectedHorse = parts[1];
                Toast.makeText(getApplicationContext(), "Selected: " + selectedHorse, Toast.LENGTH_SHORT).show();

                form2[0] = extractHorseForm(selectedHorse);
                Log.d("HorseComparison", "FORM IS BAGGINGz " + form2[0]);
                Log.d("HorseComparison", "FORM IS BAGGINGz FIXED " + parseForm(form2[0]));
                form2[0] = parseForm(form2[0]).toString();
                Log.d("HorseComparison", "CLICKED");
                Log.d("HorseComparison HORSE NAME IS", horseName);
                Log.d("WHEREAS SELECTED HORSE IS", selectedHorse);
                horseViewModel.getHorseId(selectedHorse, new HorseViewModel.OnHorseIdReceivedListener() {
                    @Override
                    public void onHorseIdReceived(String horseId) {
                        Log.d("HorseComparison", "Horse id number is : " + horseId);
                        horseViewModel.queryApiWithHorseId(horseId, new HorseViewModel.OnApiDataReceivedListener() {
                            @Override
                            public void onDataReceived(String data) {
                                Log.d("HorseComparison", "API Response: " + data);
                                secondResponseJson = data;
                                populateBarchart(testMethod());

                            }

                            @Override
                            public void onFailure() {
                                Log.d("HorseComparison", "Second API Call failed");
                            }
                        });
                    }

                    @Override
                    public void onFailure() {
                    }
                });



                List<String> forms = new ArrayList<>();
                List<String> horseNames = new ArrayList<>();


                if (form1[0] != null && horseName != null) {
                    forms.add(form1[0]);
                    horseNames.add(horseName);
                }


                if (form2[0] != null && selectedHorse != null) {
                    forms.add(form2[0]);
                    horseNames.add(selectedHorse);
                }


                if (!forms.isEmpty() && !horseNames.isEmpty()) {
                    plotMultipleFormsOnLineChart(forms, dbllinechart, horseNames);
                } else {

                }


            }


        });



    }

    public void populateBarchart(Map<String, Double> temp){
        BarChart stackedBarChart = findViewById(R.id.stackedBarChart);

        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        int index = 0;
        for (Map.Entry<String, Double> entry : temp.entrySet()) {
            entries.add(new BarEntry(index++, entry.getValue().floatValue()));
            labels.add(entry.getKey());
        }

        BarDataSet dataSet = new BarDataSet(entries, null);

        dataSet.setColors(new int[] {Color.BLUE, Color.RED});

        dataSet.setValueTextSize(10f);
        dataSet.setValueTextColor(Color.BLACK);

        BarData barData = new BarData(dataSet);

        stackedBarChart.getDescription().setEnabled(false);
        stackedBarChart.getLegend().setEnabled(false);

        stackedBarChart.setData(barData);


        float maxValue = Collections.max(temp.values()).floatValue();
        stackedBarChart.getAxisLeft().setAxisMaximum(maxValue * 1.1f);
        stackedBarChart.getAxisLeft().setAxisMinimum(0f);


        stackedBarChart.getAxisRight().setEnabled(false);
        stackedBarChart.getXAxis().setEnabled(false);

        stackedBarChart.invalidate();

        Log.d("LABELS ARE", labels.toString());

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView blue = findViewById(R.id.bluebox);
                TextView red = findViewById(R.id.redbox);

                Log.d("LABELS ARE", labels.toString());

                blue.setText(" " + labels.get(1));
                red.setText(" " + labels.get(0));
            }
        });
    }






    public Map<String, Double> testMethod() {
        Map<String, Double> horseAverageSP = new HashMap<>();
        try {
            parseHorseJson(firstResponseJson, horseAverageSP);
            System.out.println("FIRST DONE DOING SECOND");
            parseHorseJson(secondResponseJson, horseAverageSP);
            System.out.println("SECOND DONE");


            for (Map.Entry<String, Double> entry : horseAverageSP.entrySet()) {
                System.out.println("Horse: " + entry.getKey() + ", Average SP: " + entry.getValue());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return horseAverageSP;
    }



    public static void parseHorseJson(String horseJson, Map<String, Double> horseAverageSP) throws JSONException {
        JSONObject jsonObject = new JSONObject(horseJson);
        String horseName = jsonObject.getString("horse");

        JSONArray resultsArray = jsonObject.getJSONArray("results");
        double totalSP = 0.0;
        int numRaces = 0;

        for (int i = 0; i < resultsArray.length(); i++) {
            JSONObject resultObject = resultsArray.getJSONObject(i);
            String startingPriceString = resultObject.optString("starting_price", "0.0");


            double startingPrice = Double.parseDouble(startingPriceString);


            totalSP += startingPrice;
            numRaces++;
        }


        double averageSP = numRaces > 0 ? totalSP / numRaces : 0.0;


        horseAverageSP.put(horseName, averageSP);
    }



    public static double calculateAverage(List<Integer> values) {
        if (values.isEmpty()) return 0;
        double sum = 0;
        for (int value : values) {
            sum += value;
        }
        return sum / values.size();
    }

    public static Map<String, Map<String, Double>> calculateAveragePositions(Map<String, Map<String, List<Integer>>> horseData) {
        Map<String, Map<String, Double>> averagePositions = new HashMap<>();

        for (Map.Entry<String, Map<String, List<Integer>>> horseEntry : horseData.entrySet()) {
            Map<String, Double> classAverage = new HashMap<>();
            for (Map.Entry<String, List<Integer>> classEntry : horseEntry.getValue().entrySet()) {
                double average = calculateAverage(classEntry.getValue());
                if (average != -1) {
                    classAverage.put(classEntry.getKey(), average);
                }
            }
            if (!classAverage.isEmpty()) {
                averagePositions.put(horseEntry.getKey(), classAverage);
            }
        }

        return averagePositions;
    }



    public String extractHorseForm(String horseName) {
        StringBuilder form = new StringBuilder();

        try {

            FileInputStream fis = new FileInputStream(getFilesDir().getPath() + "/response_data.json");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder jsonData = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                jsonData.append(line);
            }
            br.close();


            JSONObject jsonObject = new JSONObject(jsonData.toString());
            JSONArray racecardsArray = jsonObject.getJSONArray("racecards");


            for (int i = 0; i < racecardsArray.length(); i++) {
                JSONObject raceObject = racecardsArray.getJSONObject(i);
                JSONArray runnersArray = raceObject.getJSONArray("runners");


                for (int j = 0; j < runnersArray.length(); j++) {
                    JSONObject horseObject = runnersArray.getJSONObject(j);
                    String name = horseObject.getString("horse");


                    if (name.equals(horseName)) {
                        form.append(horseObject.optString("form", ""));
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
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonData.toString());
            JSONArray racecardsArray = jsonObject.getJSONArray("racecards");

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

                JSONArray runnersArray = targetRace.getJSONArray("runners");
                Map<String, List<Integer>> horseForms = new HashMap<>();
                for (int i = 0; i < runnersArray.length(); i++) {
                    JSONObject horseObject = runnersArray.getJSONObject(i);
                    String horseName = horseObject.getString("horse");
                    String form = horseObject.getString("form");
                    List<Integer> formList = parseForm(form);
                    horseForms.put(horseName, formList);
                }

                for (Map.Entry<String, List<Integer>> entry : horseForms.entrySet()) {
                    String horseName = entry.getKey();
                    List<Integer> formList = entry.getValue();
                    double averageForm = calculateAverageForm(formList);
                    Log.d("HorseActivity", "Horse: " + horseName + ", Average Form: " + averageForm);
                }

                ScatterChart scatterChart = inflatedLayout.findViewById(R.id.scatter_chart);


                plotAverageFormOnScatterChart(horseForms, scatterChart);

            } else {
                Log.d("HorseActivity", "No race found at the specified time.");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private List<Integer> parseForm(String form) {
        List<Integer> formList = new ArrayList<>();
        for (int i = 0; i < form.length(); i++) {
            char result = form.charAt(i);
            if (Character.isDigit(result) && result != '0') {
                int value = Character.getNumericValue(result);
                formList.add(value);
            } else {

                if (result == 'P' || result == 'U' || result == '0') {
                    formList.add(9);
                }

                else if (result == 'F' || result == 'R' || result == 'B' || result == 'D') {
                    formList.add(9);
                }
            }
        }
        return formList;
    }

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
        form = form.replace('0', '9');
        form = form.replace('P', '9');
        form = form.replace('U', '9');
        form = form.replace('F', '9');
        form = form.replace('R', '9');
        form = form.replace('B', '9');
        form = form.replace('D', '9');


        if (form.isEmpty()) {
            lineChart.setNoDataText("No recent form available for " + horseName);
            lineChart.setNoDataTextColor(Color.BLACK);
            lineChart.setNoDataTextTypeface(Typeface.DEFAULT_BOLD);
            lineChart.invalidate();
            return;
        }


        ArrayList<Entry> entries = new ArrayList<>();
        int xIndex = 0;


        for (int i = 0; i < form.length(); i++) {
            char result = form.charAt(i);
            if (Character.isDigit(result)) {
                int value = Character.getNumericValue(result);

                entries.add(new Entry(xIndex, value));
                xIndex++;
            } else if (result == '-') {

                continue;
            }
        }


        LineDataSet dataSet = new LineDataSet(entries, horseName + "'s Recent Form");

        dataSet.setColor(Color.RED);
        dataSet.setCircleColor(Color.RED);
        dataSet.setCircleRadius(5f);
        dataSet.setDrawValues(false);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);


        LineData lineData = new LineData(dataSet);


        lineChart.getDescription().setEnabled(false);
        lineChart.getXAxis().setEnabled(false);
        lineChart.getAxisLeft().setGranularity(1f);
        lineChart.getAxisLeft().setAxisMinimum(1f);
        lineChart.getAxisLeft().setAxisMaximum(9f);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getAxisLeft().setInverted(true);


        lineChart.setDrawBorders(true);
        lineChart.setBorderColor(Color.BLACK);
        lineChart.setBorderWidth(2f);


        lineChart.setData(lineData);


        lineChart.invalidate();

    }
    private void plotMultipleFormsOnLineChart(List<String> forms, LineChart lineChart, List<String> horseNames) {

        LineData lineData = new LineData();

        for (int i = 0; i < forms.size(); i++) {
            String form = forms.get(i);
            String horseName = horseNames.get(i);


            ArrayList<Entry> entries = new ArrayList<>();
            int xIndex = 0;


            for (int j = 0; j < form.length(); j++) {
                char result = form.charAt(j);
                if (Character.isDigit(result)) {
                    int value = Character.getNumericValue(result);

                    entries.add(new Entry(xIndex, value));
                    xIndex++;
                } else if (result == '-') {

                    continue;
                }
            }


            LineDataSet dataSet = new LineDataSet(entries, horseName + "'s Recent Form");

            dataSet.setColor(getColorForHorse(i));
            dataSet.setCircleColor(getColorForHorse(i));
            dataSet.setCircleRadius(5f);
            dataSet.setDrawValues(false);
            dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);


            lineData.addDataSet(dataSet);
        }

        lineChart.setData(lineData);

        lineChart.invalidate();
    }

    private int getColorForHorse(int index) {

        int[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.MAGENTA, Color.CYAN};
        return colors[index % colors.length];
    }

    private void plotAverageFormOnScatterChart(Map<String, List<Integer>> horseForms, ScatterChart scatterChart) {

        ArrayList<Entry> entries = new ArrayList<>();


        for (Map.Entry<String, List<Integer>> formEntry : horseForms.entrySet()) {
            String horseName = formEntry.getKey();
            List<Integer> formList = formEntry.getValue();
            double averageForm = calculateAverageForm(formList);
            String data = horseName + ": " + averageForm;
            Entry entry = new Entry(entries.size(), (float) averageForm);
            entry.setData(data);
            entries.add(entry);
        }

        ScatterDataSet dataSet = new ScatterDataSet(entries, "Average placing based on form");
        dataSet.setColor(Color.BLUE);
        dataSet.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
        dataSet.setScatterShapeSize(10f);
        dataSet.setDrawValues(false);

        ScatterData scatterData = new ScatterData(dataSet);

        scatterChart.getDescription().setEnabled(false);
        scatterChart.getXAxis().setEnabled(false);
        scatterChart.getAxisLeft().setGranularity(1f);
        scatterChart.getAxisLeft().setAxisMinimum(0f);
        scatterChart.getAxisLeft().setAxisMaximum(20f);
        scatterChart.getAxisRight().setEnabled(false);
        scatterChart.getAxisLeft().setInverted(true);

        scatterChart.setDrawBorders(true);
        scatterChart.setBorderColor(Color.BLACK);
        scatterChart.setBorderWidth(2f);

        scatterChart.setData(scatterData);

        CustomMarkerView markerView = new CustomMarkerView(this, R.layout.custom_marker_view);
        markerView.setChartView(scatterChart);
        scatterChart.setMarker(markerView);
        scatterChart.invalidate();
    }

    private void horseDetailInformation(View inflatedLayout) {
        LineChart lineChart = inflatedLayout.findViewById(R.id.lineChart);
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
                        fillTextViews(horseObject, inflatedLayout);
                        Log.d("HorseComparison", "PLOTTED FORM IS " + horseObject.getString("form"));
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
            String[] parts = time24.split(":");
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            int hours12 = hours % 12;
            if (hours12 == 0) {
                hours12 = 12;
            }
            return String.format("%d:%02d", hours12, minutes);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
    }

}
