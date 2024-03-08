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

public class MainActivity extends AppCompatActivity {


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
        adapterMeetings = new ArrayAdapter<>(this,R.layout.list_item, meetings);

        autoCompleteTextView1.setAdapter(adapterRaces);
        autoCompleteTextView2.setAdapter(adapterMeetings);

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
        //makeApiCall();
    }

    private void makeApiCall() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://andruxnet-random-famous-quotes.p.rapidapi.com/?cat=movies&count=10")
                .addHeader("X-RapidAPI-Key", "14592a19b7msha506b13166c4e3fp16bc30jsn1ca5f22e6ec7")
                .addHeader("X-RapidAPI-Host", "andruxnet-random-famous-quotes.p.rapidapi.com")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle failure
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Handle successful response
                    String responseData = response.body().string();
                    System.out.println(responseData);
                    // Process the response data here
                } else {
                    // Handle error response
                    // You might want to handle different HTTP error codes differently
                }
            }
        });
    }
}