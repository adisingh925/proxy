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
import app.android.heartrate.phoneapp.model.classes.WeightData;
import app.android.heartrate.phoneapp.sqlite.SQLiteHealthTracker;

public abstract class WeightDataAdapter extends RecyclerView.Adapter<WeightDataAdapter.WeightViewHolder> {
    private final List<WeightData> array_data;
    private final Context mContext;
    SQLiteHealthTracker SQLite_health_tracker;
    Animation push_animation;

    public WeightDataAdapter(Context context, ArrayList<WeightData> arrayList) {
        this.array_data = arrayList;
        this.mContext = context;
        this.push_animation = AnimationUtils.loadAnimation(context, R.anim.view_push);
        SQLiteHealthTracker sQLiteHealthTracker = new SQLiteHealthTracker(this.mContext);
        this.SQLite_health_tracker = sQLiteHealthTracker;
        sQLiteHealthTracker.openToWrite();
    }

    public abstract void onWeightAdapterClickItem(int i, View view);

    @Override
    public WeightViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new WeightViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.row_weight_data, null));
    }

    @SuppressLint("WrongConstant")
    public void onBindViewHolder(WeightViewHolder weightViewHolder, @SuppressLint("RecyclerView") final int i) {
        WeightData weightData = this.array_data.get(i);
        weightData.date.trim();
        String trim = String.valueOf(weightData.day).trim();
        String trim2 = String.valueOf(weightData.year).trim();
        String trim3 = weightData.month_string.trim();
        String trim4 = weightData.time.trim();
        String valueOf = String.valueOf(weightData.weight);
        float f = weightData.weight;
        String str = weightData.notes;
        if (str.length() == 0) {
            weightViewHolder.lin_note.setVisibility(View.GONE);
        } else {
            weightViewHolder.lin_note.setVisibility(View.VISIBLE);
        }
        weightViewHolder.txt_day.setText(trim);
        TextView textView = weightViewHolder.txt_month;
        textView.setText(trim3 + ",");
        weightViewHolder.txt_year.setText(trim2);
        weightViewHolder.txt_time.setText(trim4);
        weightViewHolder.txt_weight.setText(valueOf);
        weightViewHolder.txt_note.setText(str);
        weightViewHolder.rel_edit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                WeightDataAdapter.this.onWeightAdapterClickItem(i, view);
            }
        });
        weightViewHolder.rel_delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                WeightDataAdapter.this.onWeightAdapterClickItem(i, view);
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

    public static class WeightViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lin_note;
        RelativeLayout rel_delete;
        RelativeLayout rel_edit;
        TextView txt_day;
        TextView txt_month;
        TextView txt_note;
        TextView txt_time;
        TextView txt_weight;
        TextView txt_year;

        public WeightViewHolder(View view) {
            super(view);
            this.txt_day = view.findViewById(R.id.row_weight_txt_day);
            this.txt_month = view.findViewById(R.id.row_weight_txt_month);
            this.txt_year = view.findViewById(R.id.row_weight_txt_year);
            this.txt_time = view.findViewById(R.id.row_weight_txt_time);
            this.txt_weight = view.findViewById(R.id.row_weight_txt_weight);
            this.txt_note = view.findViewById(R.id.row_weight_txt_notes);
            this.lin_note = view.findViewById(R.id.row_weight_lin_notes);
            this.rel_edit = view.findViewById(R.id.row_weight_rel_edit);
            this.rel_delete = view.findViewById(R.id.row_weight_rel_delete);
        }
    }
}
