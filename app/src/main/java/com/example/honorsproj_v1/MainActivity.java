package com.example.honorsproj_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {
    private MainViewModel viewModel;

    String[] meetings = {"Meeting 1", "Meeting 2", "Meeting 3", "Meeting 4", "Meeting 5"};
    String[] races = {"Race 1", "Race 2", "Race 3", "Race 4", "Race 5"};

    AutoCompleteTextView autoCompleteTextView1;
    AutoCompleteTextView autoCompleteTextView2;

    ArrayAdapter<String> adapterRaces;
    ArrayAdapter<String> adapterMeetings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autoCompleteTextView1 = findViewById(R.id.auto_complete_raceView);
        autoCompleteTextView2 = findViewById(R.id.auto_complete_meetingView);

        adapterRaces = new ArrayAdapter<>(this,R.layout.list_item, races);

        autoCompleteTextView1.setAdapter(adapterRaces);
        autoCompleteTextView2.setAdapter(adapterMeetings);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // Observe LiveData
//        viewModel.getApiResponseLiveData().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(String responseData) {
//                // Update UI with responseData
//            }
//        });

        // Observe LiveData
        viewModel.getCoursesLiveData().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> coursesList) {
                // Update UI with coursesList
                // For example, update an ArrayAdapter for an AutoCompleteTextView
                adapterMeetings = new ArrayAdapter<>(MainActivity.this, R.layout.list_item, coursesList);
                autoCompleteTextView1.setAdapter(adapterMeetings);
            }
        });

        // Make API call
        //viewModel.clearSavedData();
        viewModel.makeApiCallAndSaveToFile();
        // Print file contents
        viewModel.printFileContents();

        autoCompleteTextView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(MainActivity.this, "items   " + item, Toast.LENGTH_SHORT).show();
            }
        });
        autoCompleteTextView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(MainActivity.this, "item   " + item, Toast.LENGTH_SHORT).show();
            }
        });

        Button btn = (Button)findViewById(R.id.race_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MeetingActivity.class));
            }
        });

    }
}