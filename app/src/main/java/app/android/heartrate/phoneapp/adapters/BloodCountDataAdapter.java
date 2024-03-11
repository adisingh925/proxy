package app.android.heartrate.phoneapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import app.android.heartrate.phoneapp.R;
import app.android.heartrate.phoneapp.model.classes.BloodCountData;
import app.android.heartrate.phoneapp.sqlite.SQLiteHealthTracker;


public abstract class BloodCountDataAdapter extends RecyclerView.Adapter<BloodCountDataAdapter.BloodCountViewHolder> {
    SQLiteHealthTracker SQLite_health_tracker;
    private final List<BloodCountData> array_data;
    private final Context mContext;
    Animation push_animation;

    public abstract void onBloodCountAdapterClickItem(int i, View view);

    public BloodCountDataAdapter(Context context, ArrayList<BloodCountData> arrayList) {
        this.array_data = arrayList;
        this.mContext = context;
        this.push_animation = AnimationUtils.loadAnimation(context, R.anim.view_push);
        SQLiteHealthTracker sQLiteHealthTracker = new SQLiteHealthTracker(this.mContext);
        this.SQLite_health_tracker = sQLiteHealthTracker;
        sQLiteHealthTracker.openToWrite();
    }

    @Override
    public BloodCountViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new BloodCountViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.row_blood_count_data, null));
    }

    public void onBindViewHolder(BloodCountViewHolder bloodCountViewHolder, @SuppressLint("RecyclerView") final int i) {
        BloodCountData bloodCountData = this.array_data.get(i);
        bloodCountData.date.trim();
        String trim = String.valueOf(bloodCountData.day).trim();
        String trim2 = bloodCountData.month_string.trim();
        String trim3 = String.valueOf(bloodCountData.year).trim();
        String trim4 = bloodCountData.time.trim();
        String valueOf = String.valueOf(bloodCountData.rbc_value);
        String valueOf2 = String.valueOf(bloodCountData.wbc_value);
        String valueOf3 = String.valueOf(bloodCountData.platelets_value);
        String valueOf4 = String.valueOf(bloodCountData.hemoglobin_value);
        String str = bloodCountData.notes;
        if (str.length() == 0) {
            bloodCountViewHolder.lin_note.setVisibility(View.GONE);
        } else {
            bloodCountViewHolder.lin_note.setVisibility(View.VISIBLE);
        }
        bloodCountViewHolder.txt_day.setText(trim);
        TextView textView = bloodCountViewHolder.txt_month;
        textView.setText(trim2 + ",");
        bloodCountViewHolder.txt_year.setText(trim3);
        bloodCountViewHolder.txt_time.setText(trim4);
        bloodCountViewHolder.txt_rbc.setText(valueOf);
        bloodCountViewHolder.txt_wbc.setText(valueOf2);
        bloodCountViewHolder.txt_platelets.setText(valueOf3);
        bloodCountViewHolder.txt_hemoglobin.setText(valueOf4);
        bloodCountViewHolder.txt_note.setText(str);
        bloodCountViewHolder.rel_edit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                BloodCountDataAdapter.this.onBloodCountAdapterClickItem(i, view);
            }
        });
        bloodCountViewHolder.rel_delete.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                BloodCountDataAdapter.this.onBloodCountAdapterClickItem(i, view);
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

    public static class BloodCountViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lin_note;
        RelativeLayout rel_delete;
        RelativeLayout rel_edit;
        TextView txt_day;
        TextView txt_hemoglobin;
        TextView txt_month;
        TextView txt_note;
        TextView txt_platelets;
        TextView txt_rbc;
        TextView txt_time;
        TextView txt_wbc;
        TextView txt_year;

        public BloodCountViewHolder(View view) {
            super(view);
            this.txt_day = view.findViewById(R.id.row_bc_txt_day);
            this.txt_month = view.findViewById(R.id.row_bc_txt_month);
            this.txt_year = view.findViewById(R.id.row_bc_txt_year);
            this.txt_time = view.findViewById(R.id.row_bc_txt_time);
            this.txt_rbc = view.findViewById(R.id.row_bc_txt_rbc);
            this.txt_wbc = view.findViewById(R.id.row_bc_txt_wbc);
            this.txt_platelets = view.findViewById(R.id.row_bc_txt_platelets);
            this.txt_hemoglobin = view.findViewById(R.id.row_bc_txt_haemoglobin);
            this.lin_note = view.findViewById(R.id.row_bc_lin_notes);
            this.txt_note = view.findViewById(R.id.row_bc_txt_notes);
            this.rel_edit = view.findViewById(R.id.row_bc_rel_edit);
            this.rel_delete = view.findViewById(R.id.row_bc_rel_delete);
        }
    }
}
