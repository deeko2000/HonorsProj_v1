package com.example.honorsproj_v1;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MeetingActivity extends AppCompatActivity {


    String[] races = {"Race 1", "Race 2", "Race 3", "Race 4", "Race 5"};
    String[] horses = {"Horse 1", "Horse 2", "Horse 3", "Horse 4", "Horse 5", "Horse 6", "Horse 7", "Horse 8", "Horse 9", "Horse 10", "Horse 11"};

    AutoCompleteTextView autoCompleteRaceView;


    ArrayAdapter<String> adapterRaces;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);
        setTitle("Meeting Page");
        Intent intent = getIntent();
        ArrayList<String> timesList = null;
        if (intent != null) {
            String selectedCourse = intent.getStringExtra("selected_course");
            Button myButton = findViewById(R.id.meetingBtn); // Replace R.id.my_button with the ID of your button
            myButton.setText(selectedCourse);
            timesList = intent.getStringArrayListExtra("times_list");
        }
        //PLEASE REMEMBER THAT THE WORSE API HAS ALL HORSES RUNNING IN RACECARDS
        //CALL WORSE API FOR HORSE INFO TO DISPLAY (NAMES) -> GET HORSE NAME STRING -> QUERY BETTER API FOR HORSE ID FROM NAME STRING -> SEARCH FOR HORSE BY ID
        //PLAN IS TO MINIMIZE API CALLS
        //(MAYBE LOOK AT WORSE API'S DATA AS COULD VISAULISE INFO ABOUT JUST TODAYS RACE ASWELL)


        autoCompleteRaceView = findViewById(R.id.auto_complete_raceView);


        autoCompleteRaceView.setAdapter(adapterRaces);



        adapterRaces = new ArrayAdapter<>(this, R.layout.list_item, timesList);

        // Find AutoCompleteTextView by ID
        autoCompleteRaceView = findViewById(R.id.auto_complete_raceView);


        // Create ArrayAdapter with the timesList
        if (timesList != null) {
            adapterRaces = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, timesList);

            // Set the adapter to the AutoCompleteTextView
            autoCompleteRaceView.setAdapter(adapterRaces);
        }


        autoCompleteRaceView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(MeetingActivity.this, "item   " + item, Toast.LENGTH_SHORT).show();
            }
        });

        // Create ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, horses);

        // Set adapter to ListView
        ListView listView = findViewById(R.id.listViewHorses);
        listView.setAdapter(adapter);

        // Set OnItemClickListener to ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                // Retrieve the clicked item
                String selectedHorse = (String) adapterView.getItemAtPosition(position);

                // Start a new activity, passing selectedHorse data to it
                Intent intent = new Intent(MeetingActivity.this, HorseActivity.class);
                intent.putExtra("selectedHorse", selectedHorse);
                startActivity(intent);
            }
        });
    }
}