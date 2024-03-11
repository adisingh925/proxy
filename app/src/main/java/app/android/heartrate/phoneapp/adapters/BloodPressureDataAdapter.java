package app.android.heartrate.phoneapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
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

import app.android.heartrate.phoneapp.model.classes.BloodPressureData;
import app.android.heartrate.phoneapp.utils.AppConstants;
import app.android.heartrate.phoneapp.R;
import app.android.heartrate.phoneapp.sqlite.SQLiteHealthTracker;

import java.util.ArrayList;
import java.util.List;

public abstract class BloodPressureDataAdapter extends RecyclerView.Adapter<BloodPressureDataAdapter.BloodPressureViewHolder> {
    SQLiteHealthTracker SQLite_health_tracker;
    private final List<BloodPressureData> array_data;
    private final Context mContext;
    Animation push_animation;

    public abstract void onBloodPressureAdapterClickItem(int i, View view);

    public BloodPressureDataAdapter(Context context, ArrayList<BloodPressureData> arrayList) {
        this.array_data = arrayList;
        this.mContext = context;
        this.push_animation = AnimationUtils.loadAnimation(context, R.anim.view_push);
        SQLiteHealthTracker sQLiteHealthTracker = new SQLiteHealthTracker(this.mContext);
        this.SQLite_health_tracker = sQLiteHealthTracker;
        sQLiteHealthTracker.openToWrite();
    }

    @Override
    public BloodPressureViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new BloodPressureViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.row_blood_pressure_data, null));
    }

    public void onBindViewHolder(BloodPressureViewHolder bloodPressureViewHolder, @SuppressLint("RecyclerView") final int i) {
        BloodPressureData bloodPressureData = this.array_data.get(i);
        bloodPressureData.date.trim();
        String trim = String.valueOf(bloodPressureData.day).trim();
        String trim2 = bloodPressureData.month_string.trim();
        String trim3 = String.valueOf(bloodPressureData.year).trim();
        String trim4 = bloodPressureData.time.trim();
        String valueOf = String.valueOf(bloodPressureData.systolic_value);
        String valueOf2 = String.valueOf(bloodPressureData.diastolic_value);
        String valueOf3 = String.valueOf(bloodPressureData.pulse_rate_value);
        String valueOf4 = String.valueOf(bloodPressureData.pulse_pressure_value);
        String valueOf5 = String.valueOf(bloodPressureData.mean_arterial_pressure_value);
        String trim5 = bloodPressureData.result.trim();
        String trim6 = bloodPressureData.notes.trim();
        if (trim6.length() == 0) {
            bloodPressureViewHolder.lin_note.setVisibility(View.GONE);
        } else {
            bloodPressureViewHolder.lin_note.setVisibility(View.VISIBLE);
            bloodPressureViewHolder.txt_note.setText(trim6);
        }
        bloodPressureViewHolder.txt_day.setText(trim);
        TextView textView = bloodPressureViewHolder.txt_month;
        textView.setText(trim2 + ",");
        bloodPressureViewHolder.txt_year.setText(trim3);
        bloodPressureViewHolder.txt_time.setText(trim4);
        bloodPressureViewHolder.txt_systolic_value.setText(valueOf);
        bloodPressureViewHolder.txt_diastolic_value.setText(valueOf2);
        bloodPressureViewHolder.txt_pulse_rate_value.setText(valueOf3);
        bloodPressureViewHolder.txt_pulse_pressure_value.setText(valueOf4);
        bloodPressureViewHolder.txt_mean_arterial_value.setText(valueOf5);
        if (trim5.equalsIgnoreCase(AppConstants.pressure_level_low)) {
            bloodPressureViewHolder.img_arrow_normal.setVisibility(View.INVISIBLE);
            bloodPressureViewHolder.img_arrow_pre_hypertension.setVisibility(View.INVISIBLE);
            bloodPressureViewHolder.img_arrow_high_stage_1.setVisibility(View.INVISIBLE);
            bloodPressureViewHolder.img_arrow_high_stage_2.setVisibility(View.INVISIBLE);
            bloodPressureViewHolder.img_arrow_high_crisis.setVisibility(View.INVISIBLE);
            bloodPressureViewHolder.img_arrow_low_hypotension.setVisibility(View.VISIBLE);  
        } else if (trim5.equalsIgnoreCase(AppConstants.pressure_level_normal)) {
            bloodPressureViewHolder.img_arrow_low_hypotension.setVisibility(View.INVISIBLE);
            bloodPressureViewHolder.img_arrow_pre_hypertension.setVisibility(View.INVISIBLE);
            bloodPressureViewHolder.img_arrow_high_stage_1.setVisibility(View.INVISIBLE);
            bloodPressureViewHolder.img_arrow_high_stage_2.setVisibility(View.INVISIBLE);
            bloodPressureViewHolder.img_arrow_high_crisis.setVisibility(View.INVISIBLE);
            bloodPressureViewHolder.img_arrow_normal.setVisibility(View.VISIBLE);
        } else if (trim5.equalsIgnoreCase(AppConstants.pressure_level_pre_hypertension)) {
            bloodPressureViewHolder.img_arrow_normal.setVisibility(View.INVISIBLE);
            bloodPressureViewHolder.img_arrow_low_hypotension.setVisibility(View.INVISIBLE);
            bloodPressureViewHolder.img_arrow_high_stage_1.setVisibility(View.INVISIBLE);
            bloodPressureViewHolder.img_arrow_high_stage_2.setVisibility(View.INVISIBLE);
            bloodPressureViewHolder.img_arrow_high_crisis.setVisibility(View.INVISIBLE);
            bloodPressureViewHolder.img_arrow_pre_hypertension.setVisibility(View.VISIBLE);
        } else if (trim5.equalsIgnoreCase(AppConstants.pressure_level_high_stage_1)) {
            bloodPressureViewHolder.img_arrow_normal.setVisibility(View.INVISIBLE);
            bloodPressureViewHolder.img_arrow_pre_hypertension.setVisibility(View.INVISIBLE);
            bloodPressureViewHolder.img_arrow_low_hypotension.setVisibility(View.INVISIBLE);
            bloodPressureViewHolder.img_arrow_high_stage_2.setVisibility(View.INVISIBLE);
            bloodPressureViewHolder.img_arrow_high_crisis.setVisibility(View.INVISIBLE);
            bloodPressureViewHolder.img_arrow_high_stage_1.setVisibility(View.VISIBLE);
        } else if (trim5.equalsIgnoreCase(AppConstants.pressure_level_high_stage_2)) {
            bloodPressureViewHolder.img_arrow_normal.setVisibility(View.INVISIBLE);
            bloodPressureViewHolder.img_arrow_pre_hypertension.setVisibility(View.INVISIBLE);
            bloodPressureViewHolder.img_arrow_high_stage_1.setVisibility(View.INVISIBLE);
            bloodPressureViewHolder.img_arrow_low_hypotension.setVisibility(View.INVISIBLE);
            bloodPressureViewHolder.img_arrow_high_crisis.setVisibility(View.INVISIBLE);
            bloodPressureViewHolder.img_arrow_high_stage_2.setVisibility(View.VISIBLE);
        } else if (trim5.equalsIgnoreCase(AppConstants.pressure_level_high_crisis)) {
            bloodPressureViewHolder.img_arrow_normal.setVisibility(View.INVISIBLE);
            bloodPressureViewHolder.img_arrow_pre_hypertension.setVisibility(View.INVISIBLE);
            bloodPressureViewHolder.img_arrow_high_stage_1.setVisibility(View.INVISIBLE);
            bloodPressureViewHolder.img_arrow_high_stage_2.setVisibility(View.INVISIBLE);
            bloodPressureViewHolder.img_arrow_low_hypotension.setVisibility(View.INVISIBLE);
            bloodPressureViewHolder.img_arrow_high_crisis.setVisibility(View.VISIBLE);
        }
        bloodPressureViewHolder.rel_edit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                BloodPressureDataAdapter.this.onBloodPressureAdapterClickItem(i, view);
            }
        });
        bloodPressureViewHolder.rel_delete.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                BloodPressureDataAdapter.this.onBloodPressureAdapterClickItem(i, view);
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

    public static class BloodPressureViewHolder extends RecyclerView.ViewHolder {
        ImageView img_arrow_high_crisis;
        ImageView img_arrow_high_stage_1;
        ImageView img_arrow_high_stage_2;
        ImageView img_arrow_low_hypotension;
        ImageView img_arrow_normal;
        ImageView img_arrow_pre_hypertension;
        LinearLayout lin_note;
        RelativeLayout rel_delete;
        RelativeLayout rel_edit;
        TextView txt_day;
        TextView txt_diastolic_value;
        TextView txt_mean_arterial_value;
        TextView txt_month;
        TextView txt_note;
        TextView txt_pulse_pressure_value;
        TextView txt_pulse_rate_value;
        TextView txt_systolic_value;
        TextView txt_time;
        TextView txt_year;

        public BloodPressureViewHolder(View view) {
            super(view);
            this.txt_day = view.findViewById(R.id.row_bp_txt_day);
            this.txt_month = view.findViewById(R.id.row_bp_txt_month);
            this.txt_year = view.findViewById(R.id.row_bp_txt_year);
            this.txt_time = view.findViewById(R.id.row_bp_txt_time);
            this.txt_systolic_value = view.findViewById(R.id.row_bp_txt_systolic_value);
            this.txt_diastolic_value = view.findViewById(R.id.row_bp_txt_diastolic_value);
            this.txt_pulse_rate_value = view.findViewById(R.id.row_bp_txt_pulse_rate_value);
            this.txt_pulse_pressure_value = view.findViewById(R.id.row_bp_txt_pulse_pressure_value);
            this.txt_mean_arterial_value = view.findViewById(R.id.row_bp_txt_mean_arterial_value);
            this.img_arrow_low_hypotension = view.findViewById(R.id.row_bp_img_low_hypotension);
            this.img_arrow_normal = view.findViewById(R.id.row_bp_img_normal);
            this.img_arrow_pre_hypertension = view.findViewById(R.id.row_bp_img_pre_hypertension);
            this.img_arrow_high_stage_1 = view.findViewById(R.id.row_bp_img_high_stage_1);
            this.img_arrow_high_stage_2 = view.findViewById(R.id.row_bp_img_high_stage_2);
            this.img_arrow_high_crisis = view.findViewById(R.id.row_bp_img_high_crisis);
            this.lin_note = view.findViewById(R.id.row_bp_lin_notes);
            this.txt_note = view.findViewById(R.id.row_bp_txt_notes);
            this.rel_edit = view.findViewById(R.id.row_bp_rel_edit);
            this.rel_delete = view.findViewById(R.id.row_bp_rel_delete);
        }
    }
}
