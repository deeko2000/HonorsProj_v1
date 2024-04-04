package com.example.honorsproj_v1;

import android.content.Context;
import android.widget.TextView;

import com.example.honorsproj_v1.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

public class CustomMarkerView extends MarkerView {

    private TextView tvContent;

    public CustomMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        // Find TextView from the inflated layout
        tvContent = findViewById(R.id.tvContent);
    }

    // This method will be called every time the MarkerView is redrawn
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        // Cast the data associated with the entry to a String and set it as the text of TextView
        String horseForm = (String) e.getData();
        tvContent.setText(horseForm);
        super.refreshContent(e, highlight);
    }



}
