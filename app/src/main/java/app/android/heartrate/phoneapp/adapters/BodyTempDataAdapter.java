package app.android.heartrate.phoneapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import app.android.heartrate.phoneapp.R;
import app.android.heartrate.phoneapp.model.classes.BodyTempData;
import app.android.heartrate.phoneapp.sqlite.SQLiteHealthTracker;
import app.android.heartrate.phoneapp.utils.AppConstants;

public abstract class BodyTempDataAdapter extends RecyclerView.Adapter<BodyTempDataAdapter.ContactViewHolder> {
    private final List<BodyTempData> arrayTempData;
    private final Context mContext;
    SQLiteHealthTracker SQLite_health_tracker;
    Animation push_animation;

    public BodyTempDataAdapter(List<BodyTempData> list, Context context) {
        this.arrayTempData = list;
        this.mContext = context;
        this.push_animation = AnimationUtils.loadAnimation(context, R.anim.view_push);
        SQLiteHealthTracker sQLiteHealthTracker = new SQLiteHealthTracker(this.mContext);
        this.SQLite_health_tracker = sQLiteHealthTracker;
        sQLiteHealthTracker.openToWrite();
    }

    public abstract void onBodyTempAdapterClickItem(int i, View view);

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ContactViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.row_temp_data, null));
    }

    public void onBindViewHolder(ContactViewHolder contactViewHolder, @SuppressLint("RecyclerView") final int i) {
        BodyTempData bodyTempData = this.arrayTempData.get(i);
        bodyTempData.name.trim();
        String trim = bodyTempData.celsius.trim();
        float parseFloat = Float.parseFloat(trim);
        String trim2 = bodyTempData.fahrenheit.trim();
        String trim3 = bodyTempData.pulse.trim();
        bodyTempData.date.trim();
        String trim4 = String.valueOf(bodyTempData.day).trim();
        String.valueOf(bodyTempData.month).trim();
        String trim5 = String.valueOf(bodyTempData.year).trim();
        String trim6 = bodyTempData.month_string.trim();
        String trim7 = bodyTempData.time.trim();
        contactViewHolder.txt_celsius_value.setText(trim);
        TextView textView = contactViewHolder.txt_fahrenheit;
        textView.setText(trim2 + " " + this.mContext.getResources().getString(R.string.lbl_deg_fahrenheit));
        contactViewHolder.txt_pulse.setText(trim3);

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM, yyyy", Locale.ENGLISH);
        // Parse the input date string into a Date object
        Date date = null;
        try {
            date = inputFormat.parse(bodyTempData.date);
            // Format the Date object into the desired output format
            String formattedDateStr = outputFormat.format(date);
            contactViewHolder.txt_day.setText(formattedDateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
//
//        contactViewHolder.txt_day.setText(trim4);
//        TextView textView2 = contactViewHolder.txt_month;
//        textView2.setText(trim6 + ",");
//        contactViewHolder.txt_year.setText(trim5);
//        contactViewHolder.txt_time.setText(trim7);
        if (parseFloat >= 36.5f && ((double) parseFloat) <= 37.5d) {
            contactViewHolder.txt_fever_status.setText(AppConstants.NORMAL);
            contactViewHolder.rel_fever_status.setBackgroundResource(R.drawable.rel_rounded_normal_bg);
        } else if (parseFloat > 37.5f && ((double) parseFloat) <= 38.3d) {
            contactViewHolder.txt_fever_status.setText(AppConstants.FEVER);
            contactViewHolder.rel_fever_status.setBackgroundResource(R.drawable.rel_rounded_fever_bg);
        } else if (parseFloat > 38.3f) {
            contactViewHolder.txt_fever_status.setText(AppConstants.HIGH_FEVER);
            contactViewHolder.rel_fever_status.setBackgroundResource(R.drawable.rel_rounded_high_fever_bg);
        } else if (parseFloat < 35.0f) {
            contactViewHolder.txt_fever_status.setText(AppConstants.HYPOTHERMIA);
            contactViewHolder.rel_fever_status.setBackgroundResource(R.drawable.rel_rounded_hypothermia_bg);
        }
        contactViewHolder.rel_edit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                BodyTempDataAdapter.this.onBodyTempAdapterClickItem(i, view);
            }
        });
        contactViewHolder.rel_delete.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                BodyTempDataAdapter.this.onBodyTempAdapterClickItem(i, view);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.arrayTempData.size();
    }

    @Override
    public int getItemViewType(int i) {
        return super.getItemViewType(i);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rel_delete;
        RelativeLayout rel_edit;
        RelativeLayout rel_fever_status;
        TextView txt_celsius_value;
        TextView txt_day;
        TextView txt_fahrenheit;
        TextView txt_fever_status;
        TextView txt_month;
        TextView txt_pulse;
        TextView txt_time;
        TextView txt_year;

        public ContactViewHolder(View view) {
            super(view);
            this.txt_day = view.findViewById(R.id.bt_row_txt_day);
            this.txt_month = view.findViewById(R.id.bt_row_txt_month);
            this.txt_year = view.findViewById(R.id.bt_row_txt_year);
            this.txt_time = view.findViewById(R.id.bt_row_txt_time);
            this.txt_celsius_value = view.findViewById(R.id.bt_row_txt_celsius_value);
            this.txt_fahrenheit = view.findViewById(R.id.bt_row_txt_fahrenheit);
            this.txt_pulse = view.findViewById(R.id.bt_row_txt_pulse);
            this.rel_edit = view.findViewById(R.id.bt_row_rel_edit);
            this.rel_delete = view.findViewById(R.id.bt_row_rel_delete);
            this.rel_fever_status = view.findViewById(R.id.bt_row_rel_fever_status);
            this.txt_fever_status = view.findViewById(R.id.bt_row_txt_fever_status);
        }
    }
}
