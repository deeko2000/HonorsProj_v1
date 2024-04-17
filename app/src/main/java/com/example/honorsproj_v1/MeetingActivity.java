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
    Map<String, List<String>> horseMap;

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
            Button myButton = findViewById(R.id.meetingBtn);
            myButton.setText(selectedCourse);
            timesList = intent.getStringArrayListExtra("times_list");
        }


        adapterRaceview = new ArrayAdapter<>(this, R.layout.list_item, timesList);

        autoCompleteRaceView = findViewById(R.id.auto_complete_raceView);
        autoCompleteRaceView.setText(selectedTime);
        autoCompleteRaceView.setAdapter(adapterRaceview);


        viewModel = new ViewModelProvider(this).get(MeetingViewModel.class);


        listView = findViewById(R.id.listViewHorses);
        adapterListView = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapterListView);


        autoCompleteRaceView.setOnItemClickListener((parent, view, position, id) -> {

            String selectedTime = (String) parent.getItemAtPosition(position);


            updateListView(selectedTime);
        });


        listView.setOnItemClickListener((adapterView, view, position, id) -> {

            String selectedHorseWithNumber = (String) adapterView.getItemAtPosition(position);

            String selectedHorse = selectedHorseWithNumber.replaceAll("^\\d+\\s+", "");



            Intent horseDetailsIntent = new Intent(MeetingActivity.this, HorseActivity.class);

            horseDetailsIntent.putExtra("selected_horse", selectedHorse);
            horseDetailsIntent.putExtra("selected_race", selectedTime);


            startActivity(horseDetailsIntent);
        });



        viewModel.makeApiCallAndSaveToFile();
        viewModel.printFileContents();


        viewModel.getHorsesLiveData().observe(this, map -> {

            horseMap = map;
            updateListView(selectedTime);
        });
    }

    private void updateListView(String selectedTime) {
        adapterListView.clear();
        if (horseMap != null && horseMap.containsKey(selectedTime)) {
            List<String> horses = horseMap.get(selectedTime);
            int count = 1;
            for (String horse : horses) {
                adapterListView.add(count + "           " + horse);
                count++;
            }
        }
    }



}

