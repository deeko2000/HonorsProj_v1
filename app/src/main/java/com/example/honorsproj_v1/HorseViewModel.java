package com.example.honorsproj_v1;

import androidx.lifecycle.ViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class HorseViewModel extends ViewModel {
    private OkHttpClient client;

    public HorseViewModel() {
        client = new OkHttpClient();
    }


    public void getHorseId(String horseName, final OnHorseIdReceivedListener listener) {

        Request request = new Request.Builder()
                .url("https://horse-racing.p.rapidapi.com/query-horses?name=" + horseName)
                .get()
                .addHeader("X-RapidAPI-Key", "d70a23cef9mshfe1d96275f11100p155f05jsna9862e3a7d1c")
                .addHeader("X-RapidAPI-Host", "horse-racing.p.rapidapi.com")
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                e.printStackTrace();
                listener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {

                    String responseBody = response.body().string();
                    String horseId = parseHorseId(responseBody);
                    listener.onHorseIdReceived(horseId);
                } else {

                    listener.onFailure();
                }
            }
        });
    }


    void queryApiWithHorseId(String horseId, final OnApiDataReceivedListener listener) {

        Request request = new Request.Builder()
                .url("https://horse-racing.p.rapidapi.com/horse-stats/" + horseId)
                .get()
                .addHeader("X-RapidAPI-Key", "d70a23cef9mshfe1d96275f11100p155f05jsna9862e3a7d1c")
                .addHeader("X-RapidAPI-Host", "horse-racing.p.rapidapi.com")
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                e.printStackTrace();
                listener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    listener.onDataReceived(responseData);
                } else {
                    listener.onFailure();
                }
            }
        });
    }


    public interface OnHorseIdReceivedListener {
        void onHorseIdReceived(String horseId);
        void onFailure();
    }


    public interface OnApiDataReceivedListener {
        void onDataReceived(String data);
        void onFailure();
    }


    private String parseHorseId(String responseBody) {
        try {

            JSONArray jsonArray = new JSONArray(responseBody);


            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);


                String horseId = jsonObject.getString("id_horse");
                System.out.println("Horse ID: " + horseId);
                return horseId;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}


