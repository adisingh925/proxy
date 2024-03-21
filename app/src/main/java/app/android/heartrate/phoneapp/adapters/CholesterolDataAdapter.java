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
import app.android.heartrate.phoneapp.model.classes.CholesterolData;
import app.android.heartrate.phoneapp.sqlite.SQLiteHealthTracker;
import app.android.heartrate.phoneapp.utils.AppConstants;

public abstract class CholesterolDataAdapter extends RecyclerView.Adapter<CholesterolDataAdapter.CholesterolViewHolder> {
    private final List<CholesterolData> array_data;
    private final Context mContext;
    SQLiteHealthTracker SQLite_health_tracker;
    Animation push_animation;

    public CholesterolDataAdapter(Context context, ArrayList<CholesterolData> arrayList) {
        this.array_data = arrayList;
        this.mContext = context;
        this.push_animation = AnimationUtils.loadAnimation(context, R.anim.view_push);
        SQLiteHealthTracker sQLiteHealthTracker = new SQLiteHealthTracker(this.mContext);
        this.SQLite_health_tracker = sQLiteHealthTracker;
        sQLiteHealthTracker.openToWrite();
    }

    public abstract void onCholesterolAdapterClickItem(int i, View view);

    @Override
    public CholesterolViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new CholesterolViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.row_cholesterol_data, null));
    }

    public void onBindViewHolder(CholesterolViewHolder cholesterolViewHolder, @SuppressLint("RecyclerView") final int i) {
        CholesterolData cholesterolData = this.array_data.get(i);
        cholesterolData.date.trim();
        String trim = String.valueOf(cholesterolData.day).trim();
        String trim2 = cholesterolData.month_string.trim();
        String trim3 = String.valueOf(cholesterolData.year).trim();
        String trim4 = cholesterolData.time.trim();
        String valueOf = String.valueOf(cholesterolData.cholesterol_value);
        String valueOf2 = String.valueOf(cholesterolData.hdl_value);
        String valueOf3 = String.valueOf(cholesterolData.ldl_value);
        String valueOf4 = String.valueOf(cholesterolData.triglyceride_value);
        String trim5 = cholesterolData.result.trim();
        String trim6 = cholesterolData.notes.trim();
        if (trim6.length() == 0) {
            cholesterolViewHolder.lin_note.setVisibility(View.GONE);
        } else {
            cholesterolViewHolder.lin_note.setVisibility(View.VISIBLE);
        }

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM, yyyy", Locale.ENGLISH);
        // Parse the input date string into a Date object
        Date date = null;
        try {
            date = inputFormat.parse(cholesterolData.date);
            // Format the Date object into the desired output format
            String formattedDateStr = outputFormat.format(date);
            cholesterolViewHolder.txt_day.setText(formattedDateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

//        cholesterolViewHolder.txt_day.setText(trim);
//        TextView textView = cholesterolViewHolder.txt_month;
//        textView.setText(trim2 + ",");
//        cholesterolViewHolder.txt_year.setText(trim3);
        cholesterolViewHolder.txt_time.setText(trim4);
        cholesterolViewHolder.txt_cholesterol.setText(valueOf);
        cholesterolViewHolder.txt_hdl.setText(valueOf2);
        cholesterolViewHolder.txt_ldl.setText(valueOf3);
        cholesterolViewHolder.txt_triglyceride.setText(valueOf4);
        cholesterolViewHolder.txt_result.setText(trim5);
        cholesterolViewHolder.txt_note.setText(trim6);
        if (trim5.equalsIgnoreCase(AppConstants.cholesterol_result_high)) {
            cholesterolViewHolder.img_arrow_high.setVisibility(View.VISIBLE);
            cholesterolViewHolder.img_arrow_borderline.setVisibility(View.INVISIBLE);
            cholesterolViewHolder.img_arrow_good.setVisibility(View.INVISIBLE);
            cholesterolViewHolder.img_arrow_low.setVisibility(View.INVISIBLE);
        } else if (trim5.equalsIgnoreCase(AppConstants.cholesterol_result_borderline)) {
            cholesterolViewHolder.img_arrow_high.setVisibility(View.INVISIBLE);
            cholesterolViewHolder.img_arrow_borderline.setVisibility(View.VISIBLE);
            cholesterolViewHolder.img_arrow_good.setVisibility(View.INVISIBLE);
            cholesterolViewHolder.img_arrow_low.setVisibility(View.INVISIBLE);
        } else if (trim5.equalsIgnoreCase(AppConstants.cholesterol_result_good)) {
            cholesterolViewHolder.img_arrow_high.setVisibility(View.INVISIBLE);
            cholesterolViewHolder.img_arrow_borderline.setVisibility(View.INVISIBLE);
            cholesterolViewHolder.img_arrow_good.setVisibility(View.VISIBLE);
            cholesterolViewHolder.img_arrow_low.setVisibility(View.INVISIBLE);
        } else if (trim5.equalsIgnoreCase(AppConstants.cholesterol_result_low)) {
            cholesterolViewHolder.img_arrow_high.setVisibility(View.INVISIBLE);
            cholesterolViewHolder.img_arrow_borderline.setVisibility(View.INVISIBLE);
            cholesterolViewHolder.img_arrow_good.setVisibility(View.INVISIBLE);
            cholesterolViewHolder.img_arrow_low.setVisibility(View.VISIBLE);
        }
        cholesterolViewHolder.rel_edit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                CholesterolDataAdapter.this.onCholesterolAdapterClickItem(i, view);
            }
        });
        cholesterolViewHolder.rel_delete.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                CholesterolDataAdapter.this.onCholesterolAdapterClickItem(i, view);
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

    public static class CholesterolViewHolder extends RecyclerView.ViewHolder {
        ImageView img_arrow_borderline;
        ImageView img_arrow_good;
        ImageView img_arrow_high;
        ImageView img_arrow_low;
        LinearLayout lin_note;
        RelativeLayout rel_delete;
        RelativeLayout rel_edit;
        TextView txt_cholesterol;
        TextView txt_day;
        TextView txt_hdl;
        TextView txt_ldl;
        TextView txt_month;
        TextView txt_note;
        TextView txt_result;
        TextView txt_time;
        TextView txt_triglyceride;
        TextView txt_year;

        public CholesterolViewHolder(View view) {
            super(view);
            this.txt_day = view.findViewById(R.id.row_cholesterol_txt_day);
            this.txt_month = view.findViewById(R.id.row_cholesterol_txt_month);
            this.txt_year = view.findViewById(R.id.row_cholesterol_txt_year);
            this.txt_time = view.findViewById(R.id.row_cholesterol_txt_time);
            this.txt_cholesterol = view.findViewById(R.id.row_cholesterol_txt_cholesterol);
            this.txt_hdl = view.findViewById(R.id.row_cholesterol_txt_hdl);
            this.txt_ldl = view.findViewById(R.id.row_cholesterol_txt_ldl);
            this.txt_triglyceride = view.findViewById(R.id.row_cholesterol_txt_triglyceride);
            this.txt_result = view.findViewById(R.id.row_cholesterol_txt_result);
            this.img_arrow_high = view.findViewById(R.id.row_cholesterol_img_high_arrow);
            this.img_arrow_borderline = view.findViewById(R.id.row_cholesterol_img_borderline_arrow);
            this.img_arrow_good = view.findViewById(R.id.row_cholesterol_img_good_arrow);
            this.img_arrow_low = view.findViewById(R.id.row_cholesterol_img_low_arrow);
            this.lin_note = view.findViewById(R.id.row_cholesterol_lin_notes);
            this.txt_note = view.findViewById(R.id.row_cholesterol_txt_notes);
            this.rel_edit = view.findViewById(R.id.row_cholesterol_rel_edit);
            this.rel_delete = view.findViewById(R.id.row_cholesterol_rel_delete);
        }
    }
}
