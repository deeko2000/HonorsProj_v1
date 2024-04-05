package com.example.honorsproj_v1;

import android.content.Context;
import android.widget.TextView;

import com.example.honorsproj_v1.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.Utils;

public class CustomMarkerView extends MarkerView {

    private TextView tvContent;

    public CustomMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        // find your layout components
        tvContent = findViewById(R.id.tvContent);
    }

    // callbacks every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        if (e instanceof CandleEntry) {
            CandleEntry ce = (CandleEntry) e;
            tvContent.setText("" + Utils.formatNumber(ce.getHigh(), 0, true));
        } else {
            String data = (String) e.getData(); // Get data set previously
            tvContent.setText(data); // Set the content to display
        }
        // this will perform necessary layouting
        super.refreshContent(e, highlight);
    }
}
