package com.example.honorsproj_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

public class MeetingActivity extends AppCompatActivity {


    String[] races = {"Race 1", "Race 2", "Race 3", "Race 4", "Race 5"};
    String[] horses = {"Horse 1", "Horse 2", "Horse 3", "Horse 4", "Horse 5", "Horse 6", "Horse 7", "Horse 8", "Horse 9", "Horse 10", "Horse 11"};

    AutoCompleteTextView autoCompleteTextView1;


    ArrayAdapter<String> adapterRaces;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);

        autoCompleteTextView1 = findViewById(R.id.auto_complete_raceView);

        adapterRaces = new ArrayAdapter<>(this,R.layout.list_item, races);

        autoCompleteTextView1.setAdapter(adapterRaces);


        autoCompleteTextView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
    }
}