package com.example.honorsproj_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {
    private MainViewModel viewModel;

        //Testing arrays for meetings and races
        String[] meetings = {"Meeting 1", "Meeting 2", "Meeting 3", "Meeting 4", "Meeting 5"};
        String[] races = {"Race 1", "Race 2", "Race 3", "Race 4", "Race 5"};

    LinearLayout linLay1;
    LinearLayout linLay2;
    AutoCompleteTextView autoCompleteTextView1;
    AutoCompleteTextView autoCompleteTextView2;
    ArrayAdapter<String> adapterMeetings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linLay1 = findViewById(R.id.linLay1);
        linLay2 = findViewById(R.id.linLay2);


        autoCompleteTextView1 = findViewById(R.id.auto_complete_raceView);
        autoCompleteTextView2 = findViewById(R.id.auto_complete_meetingView);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // Observe changes in coursesLiveData


        viewModel.getCoursesLiveData().observe(this, new Observer<Map<String, List<String>>>() {
            @Override
            public void onChanged(Map<String, List<String>> coursesMap) {
                // Clear existing buttons
                linLay1.removeAllViews();
                linLay2.removeAllViews();

                // Create a counter for tracking button addition
                int buttonCounter = 0;
                LinearLayout currentLayout = null;

                for (String course : coursesMap.keySet()) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    params.weight = 1;

                    Button button = new Button(MainActivity.this);
                    button.setText(course); // Set button text to course name
                    button.setLayoutParams(params);
                    button.setClickable(false);

                    // Check if buttonCounter is odd
                    if (buttonCounter % 2 == 0) {
                        // Create a new LinearLayout for every two buttons
                        currentLayout = new LinearLayout(MainActivity.this);
                        currentLayout.setOrientation(LinearLayout.HORIZONTAL);
                        linLay2.addView(currentLayout); // Add new LinearLayout to linLay2
                    }

                    // Add button to current layout
                    currentLayout.addView(button);

                    buttonCounter++;
                }

                // Update UI with keys from the coursesMap
                // For example, update an ArrayAdapter for an AutoCompleteTextView
                adapterMeetings = new ArrayAdapter<>(MainActivity.this, R.layout.list_item, new ArrayList<>(coursesMap.keySet()));
                autoCompleteTextView1.setAdapter(adapterMeetings);
            }
        });


        // Make API call
        viewModel.makeApiCallAndSaveToFile();
        // Print file contents
        viewModel.printFileContents();

        // Set item click listener for autoCompleteTextView1
        autoCompleteTextView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedCourse = adapterView.getItemAtPosition(position).toString();
                // Get the list of dates for the selected course from coursesLiveData
                List<String> times = viewModel.getCoursesLiveData().getValue().get(selectedCourse);
                if (times != null) {
                    // Create ArrayAdapter for dates and set it to autoCompleteTextView2
                    ArrayAdapter<String> adapterTimes = new ArrayAdapter<>(MainActivity.this, R.layout.list_item, times);
                    autoCompleteTextView2.setAdapter(adapterTimes);
                } else {
                    // If times is null, clear the adapter for autoCompleteTextView2
                    autoCompleteTextView2.setAdapter(null);
                }
                // Clear the text in autoCompleteTextView2
                autoCompleteTextView2.setText("");
            }
        });



        // Set item click listener for autoCompleteTextView2
        autoCompleteTextView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(MainActivity.this, "Selected time: " + item, Toast.LENGTH_SHORT).show();
            }
        });

        // Button click listener to navigate to MeetingActivity
        Button btn = findViewById(R.id.race_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedCourse = autoCompleteTextView1.getText().toString();
                List<String> timesList = viewModel.getCoursesLiveData().getValue().get(selectedCourse);

                Intent intent = new Intent(MainActivity.this, MeetingActivity.class);
                intent.putExtra("selected_course", selectedCourse);
                intent.putStringArrayListExtra("times_list", new ArrayList<>(timesList));
                startActivity(intent);
            }
        });


    }
}
