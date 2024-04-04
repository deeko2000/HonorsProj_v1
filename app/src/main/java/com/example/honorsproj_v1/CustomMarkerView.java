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
        // Set the text of TextView to the value of the data point
        tvContent.setText("Horse: " + e.getData() + ", Average Form: " + e.getY());
        super.refreshContent(e, highlight);
    }
}
