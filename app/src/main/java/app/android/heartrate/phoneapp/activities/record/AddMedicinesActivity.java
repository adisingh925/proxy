package app.android.heartrate.phoneapp.activities.record;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import app.android.heartrate.phoneapp.AdAdmob;
import app.android.heartrate.phoneapp.R;
import app.android.heartrate.phoneapp.adapters.SpinnerProfileAdapter;
import app.android.heartrate.phoneapp.model.classes.MedicineData;
import app.android.heartrate.phoneapp.model.classes.MedicineNameData;
import app.android.heartrate.phoneapp.model.classes.UserProfileData;
import app.android.heartrate.phoneapp.sqlite.SQLiteHealthTracker;
import app.android.heartrate.phoneapp.utils.AppConstants;
import app.android.heartrate.phoneapp.utils.EUGeneralClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class AddMedicinesActivity extends AppCompatActivity {
    public static Activity activity_add_medicine;
    SQLiteHealthTracker SQLite_health_tracker;
    ArrayList<String> arrayListUnitOfMeasure = new ArrayList<>(Arrays.asList("mg", "tablet", "unit", "g", "mcg", "ml", "pill", "drop", "capsule"));
    int[] arrayProfileIds;
    String[] arrayProfileNames;
    ArrayList<UserProfileData> array_profiles = new ArrayList<>();

    private String current_date_time = "";
    String date_time = "";
    int day;
    EditText et_medicine_dosage;
    EditText et_medicine_name;
    EditText et_notes;
    EditText et_times_a_day;
    int hour;
    Context mContext;
    int month;
    Animation push_animation;

    RelativeLayout rel_select_date;
    RelativeLayout rel_select_time;
    private int save_entry_day;
    private int save_entry_hour;
    private int save_entry_minute;
    private int save_entry_month;
    private int save_entry_year;
    int selected_user_id;
    String selected_user_name = "";
    SpinnerProfileAdapter spinner_profile_adapter;
    Spinner spinner_profiles;
    Spinner spinner_unit_measure;
    TextView txt_date;
    TextView txt_dosage_unit_title;
    TextView txt_time;
    ArrayAdapter<String> unit_measure_spinner_adapter;
    int year;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        SetView();

        AdAdmob adAdmob = new AdAdmob(this);
        adAdmob.FullscreenAd(this);
        adAdmob.BannerAd(findViewById(R.id.banner), this);
        AppConstants.overridePendingTransitionEnter(this);
    }

    private void SetView() {
        setContentView(R.layout.activity_add_medicine);

        this.mContext = this;
        activity_add_medicine = this;
        this.push_animation = AnimationUtils.loadAnimation(this, R.anim.view_push);
        setUpActionBar();
        SQLiteHealthTracker sQLiteHealthTracker = new SQLiteHealthTracker(this);
        this.SQLite_health_tracker = sQLiteHealthTracker;
        sQLiteHealthTracker.openToWrite();
        this.spinner_profiles = findViewById(R.id.add_medicine_spinner_profiles);
        this.rel_select_date = findViewById(R.id.add_medicine_rel_select_date);
        this.txt_date = findViewById(R.id.add_medicine_txt_date);
        this.rel_select_time = findViewById(R.id.add_medicine_rel_select_time);
        this.txt_time = findViewById(R.id.add_medicine_txt_time);
        this.spinner_unit_measure = findViewById(R.id.add_medicine_spinner_unit_measure);
        this.txt_dosage_unit_title = findViewById(R.id.add_medicine_txt_dosage_unit);
        this.et_medicine_name = findViewById(R.id.add_medicine_et_medicine_name);
        this.et_medicine_dosage = findViewById(R.id.add_medicine_et_dosage);
        this.et_times_a_day = findViewById(R.id.add_medicine_et_times_a_day);
        this.et_notes = findViewById(R.id.add_medicine_et_notes);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_list, this.arrayListUnitOfMeasure);
        this.unit_measure_spinner_adapter = arrayAdapter;
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list);
        this.spinner_unit_measure.setAdapter(this.unit_measure_spinner_adapter);
        TextView textView = this.txt_dosage_unit_title;
        textView.setText("Dosage (" + this.spinner_unit_measure.getSelectedItem().toString() + ") :");
        SetProfileSpinner();
        SetCurrentDateTime();
        if (AppConstants.is_medicine_edit_mode) {
            SetMedicineData();
        }
        this.rel_select_date.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                AddMedicinesActivity.this.hideSoftKeyboard();
                Calendar instance = Calendar.getInstance();
                AddMedicinesActivity.this.save_entry_year = instance.get(1);
                AddMedicinesActivity.this.save_entry_month = instance.get(2);
                AddMedicinesActivity.this.save_entry_day = instance.get(5);
                new DatePickerDialog(AddMedicinesActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {


                    public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                        try {
                            AddMedicinesActivity.this.txt_date.setText(new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("dd/MM/yyyy").parse((i3 + "/" + (i2 + 1) + "/" + i).trim())));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, AddMedicinesActivity.this.save_entry_year, AddMedicinesActivity.this.save_entry_month, AddMedicinesActivity.this.save_entry_day).show();
            }
        });
        this.rel_select_time.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                Calendar instance = Calendar.getInstance();
                AddMedicinesActivity.this.save_entry_hour = instance.get(11);
                AddMedicinesActivity.this.save_entry_minute = instance.get(12);
                new TimePickerDialog(AddMedicinesActivity.this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {


                    public void onTimeSet(TimePicker timePicker, int i, int i2) {
                        try {
                            AddMedicinesActivity.this.txt_time.setText(new SimpleDateFormat("hh:mm aa").format(new SimpleDateFormat("hh:mm").parse(i + ":" + i2)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, AddMedicinesActivity.this.save_entry_hour, AddMedicinesActivity.this.save_entry_minute, false).show();
            }
        });
    }

    private void SetProfileSpinner() {
        this.array_profiles.clear();
        ArrayList<UserProfileData> arrayList = (ArrayList) this.SQLite_health_tracker.GetUserProfileData();
        this.array_profiles = arrayList;
        if (arrayList.size() > 0) {
            this.arrayProfileIds = new int[this.array_profiles.size()];
            this.arrayProfileNames = new String[this.array_profiles.size()];
            for (int i = 0; i < this.array_profiles.size(); i++) {
                this.arrayProfileIds[i] = this.array_profiles.get(i).user_id;
                this.arrayProfileNames[i] = this.array_profiles.get(i).user_name.trim();
            }
            SpinnerProfileAdapter spinnerProfileAdapter = new SpinnerProfileAdapter(this, this.array_profiles);
            this.spinner_profile_adapter = spinnerProfileAdapter;
            this.spinner_profiles.setAdapter(spinnerProfileAdapter);
        }
        this.spinner_profiles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                int i2 = AddMedicinesActivity.this.arrayProfileIds[i];
                String trim = AddMedicinesActivity.this.arrayProfileNames[i].trim();
                Log.e("selected Profile :", "ID :" + i2 + "\nName :" + trim);
            }
        });
    }

    @SuppressLint("WrongConstant")
    public void hideSoftKeyboard() {
        View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
            ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }

    private void SetMedicineData() {
        if (AppConstants.selected_medicine_data != null) {
            MedicineData medicineData = AppConstants.selected_medicine_data;
            String trim = medicineData.date.trim();
            String trim2 = medicineData.time.trim();
            String trim3 = medicineData.medicine_name.trim();
            String trim4 = medicineData.dosage.trim();
            String trim5 = medicineData.measure_unit.trim();
            String trim6 = medicineData.times_day.trim();
            String trim7 = medicineData.notes.trim();
            int i = AppConstants.selected_medicine_data.user_id;
            this.selected_user_id = i;
            String GetProfileNameByID = this.SQLite_health_tracker.GetProfileNameByID(i);
            this.selected_user_name = GetProfileNameByID;
            if (GetProfileNameByID != null) {
                int i2 = 0;
                for (int i3 = 0; i3 < this.array_profiles.size(); i3++) {
                    if (this.array_profiles.get(i3).user_name.trim().equals(this.selected_user_name)) {
                        i2 = i3;
                    }
                }
                this.spinner_profiles.setSelection(i2);
            }
            if (trim5.equals("mg")) {
                this.spinner_unit_measure.setSelection(0);
            } else if (trim5.equals("tablet")) {
                this.spinner_unit_measure.setSelection(1);
            } else if (trim5.equals("unit")) {
                this.spinner_unit_measure.setSelection(2);
            } else if (trim5.equals("g")) {
                this.spinner_unit_measure.setSelection(3);
            } else if (trim5.equals("mcg")) {
                this.spinner_unit_measure.setSelection(4);
            } else if (trim5.equals("ml")) {
                this.spinner_unit_measure.setSelection(5);
            } else if (trim5.equals("pill")) {
                this.spinner_unit_measure.setSelection(6);
            } else if (trim5.equals("drop")) {
                this.spinner_unit_measure.setSelection(7);
            } else if (trim5.equals("capsule")) {
                this.spinner_unit_measure.setSelection(8);
            }
            this.txt_date.setText(trim);
            this.txt_time.setText(trim2);
            this.et_medicine_name.setText(trim3);
            this.et_medicine_dosage.setText(trim4);
            this.et_times_a_day.setText(trim6);
            this.et_notes.setText(trim7);
            return;
        }
        EUGeneralClass.ShowErrorToast(this, "Something went wrong!");
    }


    private void SaveProcess() {
        boolean z = false;
        EditText editText = null;
        try {
            String trim = this.txt_date.getText().toString().trim();
            String trim2 = this.txt_time.getText().toString().trim();
            String trim3 = this.et_medicine_name.getText().toString().trim();
            String trim4 = this.et_medicine_dosage.getText().toString().trim();
            String obj = this.spinner_unit_measure.getSelectedItem().toString();
            String trim5 = this.et_times_a_day.getText().toString().trim();
            String trim6 = this.et_notes.getText().toString().trim();
            boolean z2 = true;
            if (TextUtils.isEmpty(trim3)) {
                this.et_medicine_name.setError(AppConstants.error_field_require);
                editText = this.et_medicine_name;
                z = true;
            }
            if (TextUtils.isEmpty(trim4)) {
                this.et_medicine_dosage.setError(AppConstants.error_field_require);
                editText = this.et_medicine_dosage;
                z = true;
            }
            if (TextUtils.isEmpty(trim5)) {
                this.et_times_a_day.setError(AppConstants.error_field_require);
                editText = this.et_times_a_day;
            } else {
                z2 = z;
            }
            if (z2) {
                editText.requestFocus();
                return;
            }
            MedicineData medicineData = new MedicineData();
            int selectedItemPosition = this.spinner_profiles.getSelectedItemPosition();
            this.selected_user_id = this.arrayProfileIds[selectedItemPosition];
            this.selected_user_name = this.arrayProfileNames[selectedItemPosition];
            if (!AppConstants.is_medicine_edit_mode) {
                medicineData.user_id = this.selected_user_id;
                medicineData.date = trim.trim();
                medicineData.time = trim2.trim();
                medicineData.medicine_name = trim3.trim();
                medicineData.measure_unit = obj.trim();
                medicineData.dosage = trim4.trim();
                medicineData.times_day = trim5.trim();
                medicineData.notes = trim6.trim();
                GetDateTime(trim.trim(), trim2.trim());
                medicineData.dateTime = this.date_time;
                medicineData.day = this.day;
                medicineData.month = this.month;
                medicineData.year = this.year;
                medicineData.hour = this.hour;
                this.SQLite_health_tracker.InsertMedicineData(medicineData);
                if (!this.SQLite_health_tracker.CheckMedicineNameExist(trim3)) {
                    MedicineNameData medicineNameData = new MedicineNameData();
                    medicineNameData.medicine_id = this.SQLite_health_tracker.GetLastMedicineId();
                    medicineNameData.medicine_name = trim3.trim();
                    this.SQLite_health_tracker.InsertAllMedicine(medicineNameData);
                }
                EUGeneralClass.ShowSuccessToast(this, "Medicine Data saved successfully!");
                onBackPressed();
                return;
            }
            int i = AppConstants.selected_medicine_data.row_id;
            medicineData.row_id = i;
            medicineData.user_id = this.selected_user_id;
            medicineData.date = trim.trim();
            medicineData.time = trim2.trim();
            medicineData.medicine_name = trim3.trim();
            medicineData.measure_unit = obj.trim();
            medicineData.dosage = trim4.trim();
            medicineData.times_day = trim5.trim();
            medicineData.notes = trim6.trim();
            GetDateTime(trim.trim(), trim2.trim());
            medicineData.dateTime = this.date_time;
            medicineData.day = this.day;
            medicineData.month = this.month;
            medicineData.year = this.year;
            medicineData.hour = this.hour;
            this.SQLite_health_tracker.UpdateMedicineData(i, this.selected_user_id, medicineData);
            if (!this.SQLite_health_tracker.CheckMedicineNameExist(trim3)) {
                MedicineNameData medicineNameData2 = new MedicineNameData();
                medicineNameData2.medicine_id = this.SQLite_health_tracker.GetLastMedicineId();
                medicineNameData2.medicine_name = trim3.trim();
                this.SQLite_health_tracker.InsertAllMedicine(medicineNameData2);
            }
            EUGeneralClass.ShowSuccessToast(this, "Medicine Data updated successfully!");
            onBackPressed();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SetCurrentDateTime() {
        try {
            Calendar instance = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
            String format = simpleDateFormat.format(instance.getTime());
            this.current_date_time = format;
            Date parse = simpleDateFormat.parse(format);
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("hh:mm aa");
            String format2 = simpleDateFormat2.format(parse);
            simpleDateFormat3.format(parse);
            Log.e("Current Date Time:", this.current_date_time);
            this.txt_date.setText(format2);
            this.txt_time.setText(DateFormat.format("hh:mm aaa", Calendar.getInstance().getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void GetDateTime(String str, String str2) {
        try {
            String str3 = str + " " + str2;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd");
            SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("MM");
            SimpleDateFormat simpleDateFormat4 = new SimpleDateFormat("yyyy");
            SimpleDateFormat simpleDateFormat5 = new SimpleDateFormat("hh:mm aa");
            SimpleDateFormat simpleDateFormat6 = new SimpleDateFormat("hh");
            Date parse = new SimpleDateFormat("dd/MM/yyyy hh:mm aa").parse(str3);
            this.date_time = simpleDateFormat.format(parse) + " " + simpleDateFormat5.format(parse);
            this.day = Integer.parseInt(simpleDateFormat2.format(parse));
            this.month = Integer.parseInt(simpleDateFormat3.format(parse));
            this.year = Integer.parseInt(simpleDateFormat4.format(parse));
            this.hour = Integer.parseInt(simpleDateFormat6.format(parse));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void setUpActionBar() {
        ((TextView) findViewById(R.id.toolbar_txt_title_1)).setText(getResources().getString(R.string.lbl_header_add));
        ((TextView) findViewById(R.id.toolbar_txt_title_2)).setText(getResources().getString(R.string.lbl_header_medicine));
        findViewById(R.id.toolbar_rel_save).setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                AddMedicinesActivity.this.SaveProcess();
            }
        });
        findViewById(R.id.toolbar_rel_back).setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                AddMedicinesActivity.this.onBackPressed();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.blank_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }


    @Override
    public void onResume() {
        super.onResume();


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BackScreen();
    }

    private void BackScreen() {

        finish();
        AppConstants.overridePendingTransitionExit(this);
    }
}
