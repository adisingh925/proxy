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
import app.android.heartrate.phoneapp.model.classes.MedicineData;
import app.android.heartrate.phoneapp.sqlite.SQLiteHealthTracker;


public abstract class MedicineDataAdapter extends RecyclerView.Adapter<MedicineDataAdapter.MedicineViewHolder> {
    private final List<MedicineData> array_data;
    private final Context mContext;
    SQLiteHealthTracker SQLite_health_tracker;
    Animation push_animation;

    public MedicineDataAdapter(Context context, ArrayList<MedicineData> arrayList) {
        this.array_data = arrayList;
        this.mContext = context;
        this.push_animation = AnimationUtils.loadAnimation(context, R.anim.view_push);
        SQLiteHealthTracker sQLiteHealthTracker = new SQLiteHealthTracker(this.mContext);
        this.SQLite_health_tracker = sQLiteHealthTracker;
        sQLiteHealthTracker.openToWrite();
    }

    public abstract void onMedicineAdapterClickItem(int i, View view);

    @Override
    public MedicineViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MedicineViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.row_medicine_data, null));
    }

    public void onBindViewHolder(MedicineViewHolder medicineViewHolder, @SuppressLint("RecyclerView") final int i) {
        MedicineData medicineData = this.array_data.get(i);
        medicineData.date.trim();
        String trim = String.valueOf(medicineData.day).trim();
        String trim2 = medicineData.month_string.trim();
        String trim3 = String.valueOf(medicineData.year).trim();
        String trim4 = medicineData.time.trim();
        String trim5 = medicineData.medicine_name.trim();
        String trim6 = medicineData.dosage.trim();
        String trim7 = medicineData.measure_unit.trim();
        String trim8 = medicineData.times_day.trim();
        String trim9 = medicineData.notes.trim();
        String str = trim6 + " " + trim7 + " " + trim8 + " times a day";
        if (trim9.length() == 0) {
            medicineViewHolder.lin_note.setVisibility(View.GONE);
        } else {
            medicineViewHolder.lin_note.setVisibility(View.VISIBLE);
        }
        medicineViewHolder.txt_day.setText(trim);
        medicineViewHolder.txt_month.setText(trim2 + ",");
        medicineViewHolder.txt_year.setText(trim3);
        medicineViewHolder.txt_time.setText(trim4);
        medicineViewHolder.txt_medicine_name.setText(trim5);
        medicineViewHolder.txt_medicine_detail.setText(str);
        medicineViewHolder.txt_note.setText(trim9);
        medicineViewHolder.rel_edit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                MedicineDataAdapter.this.onMedicineAdapterClickItem(i, view);
            }
        });
        medicineViewHolder.rel_delete.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                MedicineDataAdapter.this.onMedicineAdapterClickItem(i, view);
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

    public static class MedicineViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lin_note;
        RelativeLayout rel_delete;
        RelativeLayout rel_edit;
        TextView txt_day;
        TextView txt_medicine_detail;
        TextView txt_medicine_name;
        TextView txt_month;
        TextView txt_note;
        TextView txt_time;
        TextView txt_year;

        public MedicineViewHolder(View view) {
            super(view);
            this.txt_day = view.findViewById(R.id.row_medicine_txt_day);
            this.txt_month = view.findViewById(R.id.row_medicine_txt_month);
            this.txt_year = view.findViewById(R.id.row_medicine_txt_year);
            this.txt_time = view.findViewById(R.id.row_medicine_txt_time);
            this.txt_medicine_name = view.findViewById(R.id.row_medicine_txt_name);
            this.txt_medicine_detail = view.findViewById(R.id.row_medicine_txt_details);
            this.lin_note = view.findViewById(R.id.row_medicine_lin_notes);
            this.txt_note = view.findViewById(R.id.row_medicine_txt_notes);
            this.rel_edit = view.findViewById(R.id.row_medicine_rel_edit);
            this.rel_delete = view.findViewById(R.id.row_medicine_rel_delete);
        }
    }
}
