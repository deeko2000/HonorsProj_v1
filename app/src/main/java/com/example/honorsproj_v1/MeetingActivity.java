package com.example.honorsproj_v1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MeetingActivity extends AppCompatActivity {
    private MeetingViewModel viewModel;
    AutoCompleteTextView autoCompleteRaceView;
    String selectedTime = null;
    ArrayAdapter<String> adapterRaceview;
    ArrayAdapter<String> adapterListView;
    ListView listView;
    Map<String, List<String>> horseMap; // Declare horseMap as a member variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);
        setTitle("Meeting Page");

        Intent intent = getIntent();
        ArrayList<String> timesList = null;
        if (intent != null) {
            String selectedCourse = intent.getStringExtra("selected_course");
            selectedTime = intent.getStringExtra("selected_time");
            Button myButton = findViewById(R.id.meetingBtn); // Replace R.id.my_button with the ID of your button
            myButton.setText(selectedCourse);
            timesList = intent.getStringArrayListExtra("times_list");
        }

        // Initialize adapter for AutoCompleteTextView
        adapterRaceview = new ArrayAdapter<>(this, R.layout.list_item, timesList);

        autoCompleteRaceView = findViewById(R.id.auto_complete_raceView);
        autoCompleteRaceView.setText(selectedTime);
        autoCompleteRaceView.setAdapter(adapterRaceview);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(MeetingViewModel.class);

        // Initialize ListView and its adapter
        listView = findViewById(R.id.listViewHorses);
        adapterListView = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapterListView);

        // Set listener for AutoCompleteTextView selection changes
        autoCompleteRaceView.setOnItemClickListener((parent, view, position, id) -> {
            // Get the selected time from AutoCompleteTextView
            String selectedTime = (String) parent.getItemAtPosition(position);

            // Update the ListView based on the selected time
            updateListView(selectedTime);
        });

        // Set click listener for ListView items
        listView.setOnItemClickListener((adapterView, view, position, id) -> {
            // Get the clicked item
            String selectedHorse = (String) adapterView.getItemAtPosition(position);

            // Create a new Intent
            Intent horseDetailsIntent = new Intent(MeetingActivity.this, HorseActivity.class);
            // Pass any necessary data to the intent
            horseDetailsIntent.putExtra("selected_horse", selectedHorse);

            // Start the activity with the intent
            startActivity(horseDetailsIntent);
        });

        // Make API call and save data to file
        viewModel.makeApiCallAndSaveToFile();
        viewModel.printFileContents();

        // Observe the LiveData containing the HashMap from the ViewModel
        viewModel.getHorsesLiveData().observe(this, map -> {
            // Assign horseMap and update ListView based on initial selectedTime
            horseMap = map;
            updateListView(selectedTime);
        });
    }

    private void updateListView(String selectedTime) {
        // Clear the ListView adapter
        adapterListView.clear();

        // Check if horseMap is null or the selected time is not available
        if (horseMap != null && horseMap.containsKey(selectedTime)) {
            // Get the list of horses associated with the selected time
            List<String> horses = horseMap.get(selectedTime);

            // Add each horse to the ListView adapter with numbers starting from 1
            int count = 1;
            for (String horse : horses) {
                adapterListView.add(count + "           " + horse);
                count++;
            }
        }
    }



}

