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


import app.android.heartrate.phoneapp.R;
import app.android.heartrate.phoneapp.model.classes.BloodSugarData;
import app.android.heartrate.phoneapp.sqlite.SQLiteHealthTracker;
import app.android.heartrate.phoneapp.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

public abstract class BloodSugarDataAdapter extends RecyclerView.Adapter<BloodSugarDataAdapter.BloodSugarViewHolder> {
    SQLiteHealthTracker SQLite_health_tracker;
    private final List<BloodSugarData> array_data;
    private final Context mContext;
    Animation push_animation;

    public abstract void onBloodSugarAdapterClickItem(int i, View view);

    public BloodSugarDataAdapter(Context context, ArrayList<BloodSugarData> arrayList) {
        this.array_data = arrayList;
        this.mContext = context;
        this.push_animation = AnimationUtils.loadAnimation(context, R.anim.view_push);
        SQLiteHealthTracker sQLiteHealthTracker = new SQLiteHealthTracker(this.mContext);
        this.SQLite_health_tracker = sQLiteHealthTracker;
        sQLiteHealthTracker.openToWrite();
    }

    @Override
    public BloodSugarViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new BloodSugarViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.row_blood_sugar_data, null));
    }

    public void onBindViewHolder(BloodSugarViewHolder bloodSugarViewHolder, @SuppressLint("RecyclerView") final int i) {
        BloodSugarData bloodSugarData = this.array_data.get(i);
        bloodSugarData.date.trim();
        String trim = String.valueOf(bloodSugarData.day).trim();
        String trim2 = bloodSugarData.month_string.trim();
        String trim3 = String.valueOf(bloodSugarData.year).trim();
        String trim4 = bloodSugarData.time.trim();
        String trim5 = bloodSugarData.current_status.trim();
        String valueOf = String.valueOf(bloodSugarData.sugar_level);
        String valueOf2 = String.valueOf(bloodSugarData.keton_level);
        String valueOf3 = String.valueOf(bloodSugarData.blood_ADAG);
        String valueOf4 = String.valueOf(bloodSugarData.hemoglobin_level);
        String valueOf5 = String.valueOf(bloodSugarData.blood_DCCT);
        String trim6 = bloodSugarData.result.trim();
        String trim7 = bloodSugarData.notes.trim();
        if (trim7.length() == 0) {
            bloodSugarViewHolder.lin_note.setVisibility(View.GONE);
        } else {
            bloodSugarViewHolder.lin_note.setVisibility(View.VISIBLE);
        }
        if (trim6.equalsIgnoreCase(AppConstants.sugar_level_low)) {
            bloodSugarViewHolder.img_arrow_pre_diabetes.setVisibility(View.INVISIBLE);
            bloodSugarViewHolder.img_arrow_diabetes.setVisibility(View.INVISIBLE);
            bloodSugarViewHolder.img_arrow_normal.setVisibility(View.INVISIBLE);
            bloodSugarViewHolder.img_arrow_low.setVisibility(View.VISIBLE);
        } else if (trim6.equalsIgnoreCase(AppConstants.sugar_level_normal)) {
            bloodSugarViewHolder.img_arrow_pre_diabetes.setVisibility(View.INVISIBLE);
            bloodSugarViewHolder.img_arrow_diabetes.setVisibility(View.INVISIBLE);
            bloodSugarViewHolder.img_arrow_low.setVisibility(View.INVISIBLE);
            bloodSugarViewHolder.img_arrow_normal.setVisibility(View.VISIBLE);
        } else if (trim6.equalsIgnoreCase(AppConstants.sugar_level_pre_diabetes)) {
            bloodSugarViewHolder.img_arrow_normal.setVisibility(View.INVISIBLE);
            bloodSugarViewHolder.img_arrow_diabetes.setVisibility(View.INVISIBLE);
            bloodSugarViewHolder.img_arrow_low.setVisibility(View.INVISIBLE);
            bloodSugarViewHolder.img_arrow_pre_diabetes.setVisibility(View.VISIBLE);
        } else if (trim6.equalsIgnoreCase(AppConstants.sugar_level_diabetes)) {
            bloodSugarViewHolder.img_arrow_normal.setVisibility(View.INVISIBLE);
            bloodSugarViewHolder.img_arrow_pre_diabetes.setVisibility(View.INVISIBLE);
            bloodSugarViewHolder.img_arrow_low.setVisibility(View.INVISIBLE);
            bloodSugarViewHolder.img_arrow_diabetes.setVisibility(View.VISIBLE);
        }
        bloodSugarViewHolder.txt_day.setText(trim);
        TextView textView = bloodSugarViewHolder.txt_month;
        textView.setText(trim2 + ",");
        bloodSugarViewHolder.txt_year.setText(trim3);
        bloodSugarViewHolder.txt_time.setText(trim4);
        bloodSugarViewHolder.txt_current_status.setText(trim5);
        bloodSugarViewHolder.txt_sugar_level.setText(valueOf);
        bloodSugarViewHolder.txt_ketone_level.setText(valueOf2);
        bloodSugarViewHolder.txt_adag_value.setText(valueOf3);
        bloodSugarViewHolder.txt_hemoglobin_value.setText(valueOf4);
        bloodSugarViewHolder.txt_dcct_value.setText(valueOf5);
        bloodSugarViewHolder.txt_note.setText(trim7);
        bloodSugarViewHolder.rel_edit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BloodSugarDataAdapter.this.onBloodSugarAdapterClickItem(i, view);
            }
        });
        bloodSugarViewHolder.rel_delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BloodSugarDataAdapter.this.onBloodSugarAdapterClickItem(i, view);
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

    public static class BloodSugarViewHolder extends RecyclerView.ViewHolder {
        ImageView img_arrow_diabetes;
        ImageView img_arrow_low;
        ImageView img_arrow_normal;
        ImageView img_arrow_pre_diabetes;
        LinearLayout lin_note;
        RelativeLayout rel_delete;
        RelativeLayout rel_edit;
        TextView txt_adag_value;
        TextView txt_current_status;
        TextView txt_day;
        TextView txt_dcct_value;
        TextView txt_hemoglobin_value;
        TextView txt_ketone_level;
        TextView txt_month;
        TextView txt_note;
        TextView txt_sugar_level;
        TextView txt_time;
        TextView txt_year;

        public BloodSugarViewHolder(View view) {
            super(view);
            this.txt_day = view.findViewById(R.id.row_bs_txt_day);
            this.txt_month = view.findViewById(R.id.row_bs_txt_month);
            this.txt_year = view.findViewById(R.id.row_bs_txt_year);
            this.txt_time = view.findViewById(R.id.row_bs_txt_time);
            this.txt_sugar_level = view.findViewById(R.id.row_bs_txt_sugar_level);
            this.txt_current_status = view.findViewById(R.id.row_bs_txt_current_status);
            this.txt_ketone_level = view.findViewById(R.id.row_bs_txt_ketone_level);
            this.txt_adag_value = view.findViewById(R.id.row_bs_txt_adag_value);
            this.txt_hemoglobin_value = view.findViewById(R.id.row_bs_txt_hemoglobin_value);
            this.txt_dcct_value = view.findViewById(R.id.row_bs_txt_dcct_value);
            this.img_arrow_low = view.findViewById(R.id.row_bs_img_low_arrow);
            this.img_arrow_normal = view.findViewById(R.id.row_bs_img_normal_arrow);
            this.img_arrow_pre_diabetes = view.findViewById(R.id.row_bs_img_pre_diabetes_arrow);
            this.img_arrow_diabetes = view.findViewById(R.id.row_bs_img_diabetes_arrow);
            this.lin_note = view.findViewById(R.id.row_bs_lin_notes);
            this.txt_note = view.findViewById(R.id.row_bs_txt_notes);
            this.rel_edit = view.findViewById(R.id.row_bs_rel_edit);
            this.rel_delete = view.findViewById(R.id.row_bs_rel_delete);
        }
    }
}
