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


        viewModel.getCoursesLiveData().observe(this, new Observer<Map<String, List<String>>>() {
            @Override
            public void onChanged(Map<String, List<String>> coursesMap) {
                linLay1.removeAllViews();
                linLay2.removeAllViews();


                int buttonCounter = 0;
                LinearLayout currentLayout = null;

                for (String course : coursesMap.keySet()) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    params.weight = 1;

                    Button button = new Button(MainActivity.this);
                    button.setText(course);
                    button.setLayoutParams(params);
                    button.setClickable(false);


                    if (buttonCounter % 2 == 0) {

                        currentLayout = new LinearLayout(MainActivity.this);
                        currentLayout.setOrientation(LinearLayout.HORIZONTAL);
                        linLay2.addView(currentLayout);
                    }


                    currentLayout.addView(button);

                    buttonCounter++;
                }


                adapterMeetings = new ArrayAdapter<>(MainActivity.this, R.layout.list_item, new ArrayList<>(coursesMap.keySet()));
                autoCompleteTextView1.setAdapter(adapterMeetings);
            }
        });



        viewModel.makeApiCallAndSaveToFile();

        viewModel.printFileContents();


        autoCompleteTextView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedCourse = adapterView.getItemAtPosition(position).toString();

                List<String> times = viewModel.getCoursesLiveData().getValue().get(selectedCourse);
                if (times != null) {

                    ArrayAdapter<String> adapterTimes = new ArrayAdapter<>(MainActivity.this, R.layout.list_item, times);
                    autoCompleteTextView2.setAdapter(adapterTimes);
                } else {

                    autoCompleteTextView2.setAdapter(null);
                }

                autoCompleteTextView2.setText("");
            }
        });




        autoCompleteTextView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(MainActivity.this, "Selected time: " + item, Toast.LENGTH_SHORT).show();
            }
        });


        Button btn = findViewById(R.id.race_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedCourse = autoCompleteTextView1.getText().toString();
                String selectedTime = autoCompleteTextView2.getText().toString();
                List<String> timesList = viewModel.getCoursesLiveData().getValue().get(selectedCourse);


                if (!selectedCourse.isEmpty() && !selectedTime.isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, MeetingActivity.class);
                    intent.putExtra("selected_course", selectedCourse);
                    intent.putExtra("selected_time", selectedTime);
                    intent.putStringArrayListExtra("times_list", new ArrayList<>(timesList));
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Please select both course and time", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
