package app.android.heartrate.phoneapp.utils.customviews;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

import app.android.heartrate.phoneapp.R;

public class MyMarkerView extends MarkerView {
    private final TextView tvContent = findViewById(R.id.txt_marker_text);

    public MyMarkerView(Context context, int i) {
        super(context, i);
    }

    @Override
    public void refreshContent(Entry entry, Highlight highlight) {
        if (entry instanceof CandleEntry) {
            this.tvContent.setText(Utils.formatNumber(((CandleEntry) entry).getHigh(), 0, true));
        } else {
            this.tvContent.setText(Utils.formatNumber(entry.getY(), 0, true));
        }
        super.refreshContent(entry, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF((float) (-(getWidth() / 2)), (float) (-getHeight()));
    }
}
