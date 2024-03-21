package app.android.heartrate.phoneapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import app.android.heartrate.phoneapp.R;
import app.android.heartrate.phoneapp.model.classes.HeartRateData;
import app.android.heartrate.phoneapp.sqlite.SQLiteHealthTracker;
import app.android.heartrate.phoneapp.utils.AppConstants;

public abstract class HeartRateDataAdapter extends RecyclerView.Adapter<HeartRateDataAdapter.HeartRateViewHolder> {
    private final List<HeartRateData> array_data;
    private final Context mContext;
    SQLiteHealthTracker SQLite_health_tracker;
    Animation push_animation;

    public HeartRateDataAdapter(Context context, ArrayList<HeartRateData> arrayList) {
        this.array_data = arrayList;
        this.mContext = context;
        this.push_animation = AnimationUtils.loadAnimation(context, R.anim.view_push);
        SQLiteHealthTracker sQLiteHealthTracker = new SQLiteHealthTracker(this.mContext);
        this.SQLite_health_tracker = sQLiteHealthTracker;
        sQLiteHealthTracker.openToWrite();
    }

    public abstract void onHeartRateAdapterClickItem(int i, View view);

    @Override
    public HeartRateViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new HeartRateViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.row_heart_rate_data, null));
    }

    public void onBindViewHolder(HeartRateViewHolder heartRateViewHolder, @SuppressLint("RecyclerView") final int i) {
        HeartRateData heartRateData = this.array_data.get(i);
        heartRateData.date.trim();
        String trim = String.valueOf(heartRateData.day).trim();
        String trim2 = heartRateData.month_string.trim();
        String trim3 = String.valueOf(heartRateData.year).trim();
        String trim4 = heartRateData.time.trim();
        String trim5 = String.valueOf(heartRateData.heart_rate_value).trim();
        String trim6 = String.valueOf(heartRateData.age).trim();
        String trim7 = heartRateData.gender.trim();
        String trim8 = heartRateData.current_status.trim();
        heartRateData.color.trim();
        String trim9 = heartRateData.result.trim();
        heartRateData.range.trim();
        String trim10 = heartRateData.notes.trim();
        if (trim10.length() == 0) {
            heartRateViewHolder.lin_note.setVisibility(View.GONE);
        } else {
            heartRateViewHolder.lin_note.setVisibility(View.VISIBLE);
        }
        if (trim9.equalsIgnoreCase(AppConstants.hr_result_athlete)) {
            heartRateViewHolder.img_arrow_excellent.setVisibility(View.INVISIBLE);
            heartRateViewHolder.img_arrow_good.setVisibility(View.INVISIBLE);
            heartRateViewHolder.img_arrow_above_average.setVisibility(View.INVISIBLE);
            heartRateViewHolder.img_arrow_average.setVisibility(View.INVISIBLE);
            heartRateViewHolder.img_arrow_below_average.setVisibility(View.INVISIBLE);
            heartRateViewHolder.img_arrow_poor.setVisibility(View.INVISIBLE);
            heartRateViewHolder.img_arrow_athlete.setVisibility(View.VISIBLE);
        } else if (trim9.equalsIgnoreCase(AppConstants.hr_result_excellent)) {
            heartRateViewHolder.img_arrow_athlete.setVisibility(View.INVISIBLE);
            heartRateViewHolder.img_arrow_good.setVisibility(View.INVISIBLE);
            heartRateViewHolder.img_arrow_above_average.setVisibility(View.INVISIBLE);
            heartRateViewHolder.img_arrow_average.setVisibility(View.INVISIBLE);
            heartRateViewHolder.img_arrow_below_average.setVisibility(View.INVISIBLE);
            heartRateViewHolder.img_arrow_poor.setVisibility(View.INVISIBLE);
            heartRateViewHolder.img_arrow_excellent.setVisibility(View.VISIBLE);
        } else if (trim9.equalsIgnoreCase(AppConstants.hr_result_good)) {
            heartRateViewHolder.img_arrow_excellent.setVisibility(View.INVISIBLE);
            heartRateViewHolder.img_arrow_athlete.setVisibility(View.INVISIBLE);
            heartRateViewHolder.img_arrow_above_average.setVisibility(View.INVISIBLE);
            heartRateViewHolder.img_arrow_average.setVisibility(View.INVISIBLE);
            heartRateViewHolder.img_arrow_below_average.setVisibility(View.INVISIBLE);
            heartRateViewHolder.img_arrow_poor.setVisibility(View.INVISIBLE);
            heartRateViewHolder.img_arrow_good.setVisibility(View.VISIBLE);
        } else if (trim9.equalsIgnoreCase(AppConstants.hr_result_above_average)) {
            heartRateViewHolder.img_arrow_excellent.setVisibility(View.INVISIBLE);
            heartRateViewHolder.img_arrow_good.setVisibility(View.INVISIBLE);
            heartRateViewHolder.img_arrow_athlete.setVisibility(View.INVISIBLE);
            heartRateViewHolder.img_arrow_average.setVisibility(View.INVISIBLE);
            heartRateViewHolder.img_arrow_below_average.setVisibility(View.INVISIBLE);
            heartRateViewHolder.img_arrow_poor.setVisibility(View.INVISIBLE);
            heartRateViewHolder.img_arrow_above_average.setVisibility(View.VISIBLE);
        } else if (trim9.equalsIgnoreCase(AppConstants.hr_result_average)) {
            heartRateViewHolder.img_arrow_excellent.setVisibility(View.INVISIBLE);
            heartRateViewHolder.img_arrow_good.setVisibility(View.INVISIBLE);
            heartRateViewHolder.img_arrow_above_average.setVisibility(View.INVISIBLE);
            heartRateViewHolder.img_arrow_athlete.setVisibility(View.INVISIBLE);
            heartRateViewHolder.img_arrow_below_average.setVisibility(View.INVISIBLE);
            heartRateViewHolder.img_arrow_poor.setVisibility(View.INVISIBLE);
            heartRateViewHolder.img_arrow_average.setVisibility(View.VISIBLE);
        } else if (trim9.equalsIgnoreCase(AppConstants.hr_result_below_average)) {
            heartRateViewHolder.img_arrow_excellent.setVisibility(View.INVISIBLE);
            heartRateViewHolder.img_arrow_good.setVisibility(View.INVISIBLE);
            heartRateViewHolder.img_arrow_above_average.setVisibility(View.INVISIBLE);
            heartRateViewHolder.img_arrow_average.setVisibility(View.INVISIBLE);
            heartRateViewHolder.img_arrow_athlete.setVisibility(View.INVISIBLE);
            heartRateViewHolder.img_arrow_poor.setVisibility(View.INVISIBLE);
            heartRateViewHolder.img_arrow_below_average.setVisibility(View.VISIBLE);
        } else if (trim9.equalsIgnoreCase(AppConstants.hr_result_poor)) {
            heartRateViewHolder.img_arrow_athlete.setVisibility(View.INVISIBLE);
            heartRateViewHolder.img_arrow_excellent.setVisibility(View.INVISIBLE);
            heartRateViewHolder.img_arrow_good.setVisibility(View.INVISIBLE);
            heartRateViewHolder.img_arrow_above_average.setVisibility(View.INVISIBLE);
            heartRateViewHolder.img_arrow_average.setVisibility(View.INVISIBLE);
            heartRateViewHolder.img_arrow_below_average.setVisibility(View.INVISIBLE);
            heartRateViewHolder.img_arrow_poor.setVisibility(View.VISIBLE);
        }

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM, yyyy", Locale.ENGLISH);
        // Parse the input date string into a Date object
        Date date = null;
        try {
            date = inputFormat.parse(heartRateData.date);
            // Format the Date object into the desired output format
            String formattedDateStr = outputFormat.format(date);
            heartRateViewHolder.txt_day.setText(formattedDateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

//        heartRateViewHolder.txt_day.setText(trim);
//        TextView textView = heartRateViewHolder.txt_month;
//        textView.setText(trim2 + ",");
//        heartRateViewHolder.txt_year.setText(trim3);
//        heartRateViewHolder.txt_time.setText(trim4);
        heartRateViewHolder.txt_heart_rate_value.setText(trim5);
        heartRateViewHolder.txt_age_value.setText(trim6);
        heartRateViewHolder.txt_current_status.setText(trim8);
        heartRateViewHolder.txt_gender.setText(trim7);
        heartRateViewHolder.txt_result.setText(trim9);
        heartRateViewHolder.txt_note.setText(trim10);
        heartRateViewHolder.rel_edit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                HeartRateDataAdapter.this.onHeartRateAdapterClickItem(i, view);
            }
        });
        heartRateViewHolder.rel_delete.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                HeartRateDataAdapter.this.onHeartRateAdapterClickItem(i, view);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.array_data.size();
    }

    @Override
    public int getItemViewType(int i) {
        return super.getItemViewType(i);
    }

    public static class HeartRateViewHolder extends RecyclerView.ViewHolder {
        ImageView img_arrow_above_average;
        ImageView img_arrow_athlete;
        ImageView img_arrow_average;
        ImageView img_arrow_below_average;
        ImageView img_arrow_excellent;
        ImageView img_arrow_good;
        ImageView img_arrow_poor;
        LinearLayout lin_note;
        RelativeLayout rel_delete;
        RelativeLayout rel_edit;
        TextView txt_age_value;
        TextView txt_current_status;
        TextView txt_day;
        TextView txt_gender;
        TextView txt_heart_rate_value;
        TextView txt_month;
        TextView txt_note;
        TextView txt_result;
        TextView txt_time;
        TextView txt_year;

        public HeartRateViewHolder(View view) {
            super(view);
            this.txt_day = view.findViewById(R.id.row_hr_txt_day);
            this.txt_month = view.findViewById(R.id.row_hr_txt_month);
            this.txt_year = view.findViewById(R.id.row_hr_txt_year);
            this.txt_time = view.findViewById(R.id.row_hr_txt_time);
            this.txt_heart_rate_value = view.findViewById(R.id.row_hr_txt_heart_rate);
            this.txt_age_value = view.findViewById(R.id.row_hr_txt_age);
            this.txt_current_status = view.findViewById(R.id.row_hr_txt_current_status);
            this.txt_gender = view.findViewById(R.id.row_hr_txt_gender);
            this.img_arrow_athlete = view.findViewById(R.id.row_hr_img_athlete);
            this.img_arrow_excellent = view.findViewById(R.id.row_hr_img_excellent);
            this.img_arrow_good = view.findViewById(R.id.row_hr_img_good);
            this.img_arrow_above_average = view.findViewById(R.id.row_hr_img_above_average);
            this.img_arrow_average = view.findViewById(R.id.row_hr_img_average);
            this.img_arrow_below_average = view.findViewById(R.id.row_hr_img_below_average);
            this.img_arrow_poor = view.findViewById(R.id.row_hr_img_poor);
            this.txt_result = view.findViewById(R.id.row_hr_txt_result);
            this.lin_note = view.findViewById(R.id.row_hr_lin_notes);
            this.txt_note = view.findViewById(R.id.row_hr_txt_notes);
            this.rel_edit = view.findViewById(R.id.row_hr_rel_edit);
            this.rel_delete = view.findViewById(R.id.row_hr_rel_delete);
        }
    }
}
