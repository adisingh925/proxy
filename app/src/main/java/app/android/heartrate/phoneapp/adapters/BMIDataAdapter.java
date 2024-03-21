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
import app.android.heartrate.phoneapp.model.classes.BMIData;
import app.android.heartrate.phoneapp.sqlite.SQLiteHealthTracker;
import app.android.heartrate.phoneapp.utils.AppConstants;

public abstract class BMIDataAdapter extends RecyclerView.Adapter<BMIDataAdapter.BMIViewHolder> {
    private final List<BMIData> array_data;
    private final Context mContext;
    SQLiteHealthTracker SQLite_health_tracker;
    Animation push_animation;

    public BMIDataAdapter(Context context, ArrayList<BMIData> arrayList) {
        this.array_data = arrayList;
        this.mContext = context;
        this.push_animation = AnimationUtils.loadAnimation(context, R.anim.view_push);
        SQLiteHealthTracker sQLiteHealthTracker = new SQLiteHealthTracker(this.mContext);
        this.SQLite_health_tracker = sQLiteHealthTracker;
        sQLiteHealthTracker.openToWrite();
    }

    public abstract void onBMIAdapterClickItem(int i, View view);

    @Override
    public BMIViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new BMIViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.row_bmi_data, null));
    }

    public void onBindViewHolder(BMIViewHolder bMIViewHolder, @SuppressLint("RecyclerView") final int i) {
        BMIData bMIData = this.array_data.get(i);
        bMIData.date.trim();
        String trim = String.valueOf(bMIData.day).trim();
        String trim2 = bMIData.month_string.trim();
        String trim3 = String.valueOf(bMIData.year).trim();
        String trim4 = bMIData.time.trim();
        float parseFloat = Float.parseFloat(bMIData.bmi);
        String trim5 = bMIData.height_unit.trim();
        String trim6 = bMIData.height.trim();
        String trim7 = bMIData.weight_unit.trim();
        String trim8 = bMIData.weight.trim();
//        bMIViewHolder.txt_day.setText(trim);
//        TextView textView = bMIViewHolder.txt_month;
//        textView.setText(trim2 + ",");
//        bMIViewHolder.txt_year.setText(trim3);
//        bMIViewHolder.txt_time.setText(trim4);

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM, yyyy", Locale.ENGLISH);
        // Parse the input date string into a Date object
        Date date = null;
        try {
            date = inputFormat.parse(bMIData
                    .date);
            // Format the Date object into the desired output format
            String formattedDateStr = outputFormat.format(date);
            bMIViewHolder.txt_day.setText(formattedDateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        bMIViewHolder.txt_bmi_value.setText(String.format(Locale.US, "%.2f", Float.valueOf(parseFloat)));
        bMIViewHolder.txt_height_value.setText(trim6);
        TextView textView2 = bMIViewHolder.txt_height_unit;
        textView2.setText(trim5);
        bMIViewHolder.txt_weight_value.setText(trim8);
        TextView textView3 = bMIViewHolder.txt_weight_unit;
        textView3.setText(trim7);
        bMIViewHolder.txt_status.setText(GetBMIStatus(parseFloat, bMIViewHolder));
        bMIViewHolder.lin_bmi_status.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                BMIDataAdapter.this.onBMIAdapterClickItem(i, view);
            }
        });
        bMIViewHolder.rel_edit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                BMIDataAdapter.this.onBMIAdapterClickItem(i, view);
            }
        });
        bMIViewHolder.rel_delete.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                BMIDataAdapter.this.onBMIAdapterClickItem(i, view);
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

    private String GetBMIStatus(float f, BMIViewHolder bMIViewHolder) {
        if (f < 15.0f) {
            String str = AppConstants.bmi_result_very_severely;
            bMIViewHolder.img_arrow_very_severely.setVisibility(View.VISIBLE);
            bMIViewHolder.img_arrow_severely.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_underweight.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_normal.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_overweight.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_moderately_obese.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_severely_obese.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_very_severely_obese.setVisibility(View.INVISIBLE);
            return str;
        } else if (f >= 15.0f && f <= 16.0f) {
            String str2 = AppConstants.bmi_result_severely;
            bMIViewHolder.img_arrow_very_severely.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_severely.setVisibility(View.VISIBLE);
            bMIViewHolder.img_arrow_underweight.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_normal.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_overweight.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_moderately_obese.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_severely_obese.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_very_severely_obese.setVisibility(View.INVISIBLE);
            return str2;
        } else if (f > 16.0f && ((double) f) <= 18.5d) {
            String str3 = AppConstants.bmi_result_underweight;
            bMIViewHolder.img_arrow_very_severely.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_severely.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_underweight.setVisibility(View.VISIBLE);
            bMIViewHolder.img_arrow_normal.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_overweight.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_moderately_obese.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_severely_obese.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_very_severely_obese.setVisibility(View.INVISIBLE);
            return str3;
        } else if (((double) f) > 18.5d && f <= 25.0f) {
            String str4 = AppConstants.bmi_result_normal;
            bMIViewHolder.img_arrow_very_severely.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_severely.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_underweight.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_normal.setVisibility(View.VISIBLE);
            bMIViewHolder.img_arrow_overweight.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_moderately_obese.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_severely_obese.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_very_severely_obese.setVisibility(View.INVISIBLE);
            return str4;
        } else if (f > 25.0f && f <= 30.0f) {
            String str5 = AppConstants.bmi_result_overweight;
            bMIViewHolder.img_arrow_very_severely.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_severely.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_underweight.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_normal.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_overweight.setVisibility(View.VISIBLE);
            bMIViewHolder.img_arrow_moderately_obese.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_severely_obese.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_very_severely_obese.setVisibility(View.INVISIBLE);
            return str5;
        } else if (f > 30.0f && f <= 35.0f) {
            String str6 = AppConstants.bmi_result_moderately_obese;
            bMIViewHolder.img_arrow_very_severely.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_severely.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_underweight.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_normal.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_overweight.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_moderately_obese.setVisibility(View.VISIBLE);
            bMIViewHolder.img_arrow_severely_obese.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_very_severely_obese.setVisibility(View.INVISIBLE);
            return str6;
        } else if (f > 35.0f && f <= 40.0f) {
            String str7 = AppConstants.bmi_result_severely_obese;
            bMIViewHolder.img_arrow_very_severely.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_severely.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_underweight.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_normal.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_overweight.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_moderately_obese.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_severely_obese.setVisibility(View.VISIBLE);
            bMIViewHolder.img_arrow_very_severely_obese.setVisibility(View.INVISIBLE);
            return str7;
        } else if (f <= 40.0f) {
            return "";
        } else {
            String str8 = AppConstants.bmi_result_very_severely_obese;
            bMIViewHolder.img_arrow_very_severely.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_severely.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_underweight.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_normal.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_overweight.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_moderately_obese.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_severely_obese.setVisibility(View.INVISIBLE);
            bMIViewHolder.img_arrow_very_severely_obese.setVisibility(View.VISIBLE);
            return str8;
        }
    }

    public static class BMIViewHolder extends RecyclerView.ViewHolder {
        ImageView img_arrow_moderately_obese;
        ImageView img_arrow_normal;
        ImageView img_arrow_overweight;
        ImageView img_arrow_severely;
        ImageView img_arrow_severely_obese;
        ImageView img_arrow_underweight;
        ImageView img_arrow_very_severely;
        ImageView img_arrow_very_severely_obese;
        LinearLayout lin_bmi_status;
        RelativeLayout rel_delete;
        RelativeLayout rel_edit;
        TextView txt_bmi_value;
        TextView txt_day;
        TextView txt_height_unit;
        TextView txt_height_value;
        TextView txt_month;
        TextView txt_status;
        TextView txt_time;
        TextView txt_weight_unit;
        TextView txt_weight_value;
        TextView txt_year;

        public BMIViewHolder(View view) {
            super(view);
            this.txt_day = view.findViewById(R.id.row_bmi_txt_day);
            this.txt_month = view.findViewById(R.id.row_bmi_txt_month);
            this.txt_year = view.findViewById(R.id.row_bmi_txt_year);
            this.txt_time = view.findViewById(R.id.row_bmi_txt_time);
            this.txt_status = view.findViewById(R.id.row_bmi_txt_status);
            this.txt_bmi_value = view.findViewById(R.id.row_bmi_txt_bmi_value);
            this.txt_weight_unit = view.findViewById(R.id.row_bmi_txt_weight_unit);
            this.txt_weight_value = view.findViewById(R.id.row_bmi_txt_weight);
            this.txt_height_unit = view.findViewById(R.id.row_bmi_txt_height_unit);
            this.txt_height_value = view.findViewById(R.id.row_bmi_txt_height);
            this.lin_bmi_status = view.findViewById(R.id.row_bmi_lin_status);
            this.img_arrow_very_severely = view.findViewById(R.id.row_bmi_img_very_severely);
            this.img_arrow_severely = view.findViewById(R.id.row_bmi_img_severely);
            this.img_arrow_underweight = view.findViewById(R.id.row_bmi_img_underweight);
            this.img_arrow_normal = view.findViewById(R.id.row_bmi_img_normal);
            this.img_arrow_overweight = view.findViewById(R.id.row_bmi_img_overweight);
            this.img_arrow_moderately_obese = view.findViewById(R.id.row_bmi_img_moderately_obese);
            this.img_arrow_severely_obese = view.findViewById(R.id.row_bmi_img_severely_obese);
            this.img_arrow_very_severely_obese = view.findViewById(R.id.row_bmi_img_very_severely_obese);
            this.rel_edit = view.findViewById(R.id.row_bmi_rel_edit);
            this.rel_delete = view.findViewById(R.id.row_bmi_rel_delete);
        }
    }
}
