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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import app.android.heartrate.phoneapp.AdAdmob;
import app.android.heartrate.phoneapp.R;
import app.android.heartrate.phoneapp.adapters.SpinnerProfileAdapter;
import app.android.heartrate.phoneapp.model.ProfileData;
import app.android.heartrate.phoneapp.model.classes.BloodCountData;
import app.android.heartrate.phoneapp.model.classes.UserProfileData;
import app.android.heartrate.phoneapp.sharedpreferences.SharedPreferences;
import app.android.heartrate.phoneapp.sqlite.SQLiteHealthTracker;
import app.android.heartrate.phoneapp.utils.AppConstants;
import app.android.heartrate.phoneapp.utils.EUGeneralClass;

public class AddBloodCountActivity extends AppCompatActivity {
    public static Activity activity_add_blood_count;
    SQLiteHealthTracker SQLite_health_tracker;
    String date_time = "";
    int day;
    EditText et_hemoglobin_value;
    EditText et_notes;
    EditText et_platelets_value;
    EditText et_rbc_value;
    EditText et_wbc_value;
    int hour;
    Context mContext;
    int month;
    Animation push_animation;
    RelativeLayout rel_select_date;
    RelativeLayout rel_select_time;
    Spinner spinner_profiles;
    TextView txt_date;
    TextView txt_time;
    TextView spinner_txt_name;
    int year;
    private String current_date_time = "";
    private int save_entry_day;
    private int save_entry_hour;
    private int save_entry_minute;
    private int save_entry_month;
    private int save_entry_year;

    private SharedPreferences sharedPreferencesUtils;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        SetView();


        AppConstants.overridePendingTransitionEnter(this);
    }

    private void SetView() {
        setContentView(R.layout.activity_add_blood_count);
        this.mContext = this;
        activity_add_blood_count = this;
        this.push_animation = AnimationUtils.loadAnimation(this, R.anim.view_push);
        setUpActionBar();
        sharedPreferencesUtils = SharedPreferences.INSTANCE;
        SQLiteHealthTracker sQLiteHealthTracker = new SQLiteHealthTracker(this);
        this.SQLite_health_tracker = sQLiteHealthTracker;
        sQLiteHealthTracker.openToWrite();
        this.spinner_profiles = findViewById(R.id.add_bc_spinner_profiles);
        this.et_rbc_value = findViewById(R.id.add_bc_et_rbc_value);
        this.et_wbc_value = findViewById(R.id.add_bc_et_wbc_value);
        this.et_platelets_value = findViewById(R.id.add_bc_et_platelets_value);
        this.et_hemoglobin_value = findViewById(R.id.add_bc_et_hemoglobin);
        this.et_notes = findViewById(R.id.add_bc_et_notes);
        this.rel_select_date = findViewById(R.id.add_bc_rel_select_date);
        this.txt_date = findViewById(R.id.add_bc_txt_date);
        this.rel_select_time = findViewById(R.id.add_bc_rel_select_time);
        this.txt_time = findViewById(R.id.add_bc_txt_time);
        this.spinner_txt_name = findViewById(R.id.spinner_txt_name);
        SetProfileSpinner();
        SetCurrentDateTime();
        if (AppConstants.is_blood_count_edit_mode) {
            SetBloodCountData();
        }
        this.rel_select_date.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                AddBloodCountActivity.this.hideSoftKeyboard();
                Calendar instance = Calendar.getInstance();
                AddBloodCountActivity.this.save_entry_year = instance.get(1);
                AddBloodCountActivity.this.save_entry_month = instance.get(2);
                AddBloodCountActivity.this.save_entry_day = instance.get(5);
                new DatePickerDialog(AddBloodCountActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {


                    public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                        try {
                            AddBloodCountActivity.this.txt_date.setText(new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("dd/MM/yyyy").parse((i3 + "/" + (i2 + 1) + "/" + i).trim())));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, AddBloodCountActivity.this.save_entry_year, AddBloodCountActivity.this.save_entry_month, AddBloodCountActivity.this.save_entry_day).show();
            }
        });
        this.rel_select_time.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                Calendar instance = Calendar.getInstance();
                AddBloodCountActivity.this.save_entry_hour = instance.get(11);
                AddBloodCountActivity.this.save_entry_minute = instance.get(12);
                new TimePickerDialog(AddBloodCountActivity.this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {


                    public void onTimeSet(TimePicker timePicker, int i, int i2) {
                        try {
                            AddBloodCountActivity.this.txt_time.setText(new SimpleDateFormat("hh:mm aa").format(new SimpleDateFormat("hh:mm").parse(i + ":" + i2)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, AddBloodCountActivity.this.save_entry_hour, AddBloodCountActivity.this.save_entry_minute, false).show();
            }
        });
    }

    private void SetProfileSpinner() {
        String name = sharedPreferencesUtils.getUserName();
        spinner_txt_name.setText(name);
    }

    @SuppressLint("WrongConstant")
    public void hideSoftKeyboard() {
        View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
            ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }

    private void SetBloodCountData() {
        if (AppConstants.selected_blood_count_data != null) {
            String trim = AppConstants.selected_blood_count_data.date.trim();
            String trim2 = AppConstants.selected_blood_count_data.time.trim();
            String valueOf = String.valueOf(AppConstants.selected_blood_count_data.rbc_value);
            String valueOf2 = String.valueOf(AppConstants.selected_blood_count_data.wbc_value);
            String valueOf3 = String.valueOf(AppConstants.selected_blood_count_data.platelets_value);
            String valueOf4 = String.valueOf(AppConstants.selected_blood_count_data.hemoglobin_value);
            String trim3 = AppConstants.selected_blood_count_data.notes.trim();
            this.txt_date.setText(trim);
            this.txt_time.setText(trim2);
            this.et_rbc_value.setText(valueOf);
            this.et_wbc_value.setText(valueOf2);
            this.et_platelets_value.setText(valueOf3);
            this.et_hemoglobin_value.setText(valueOf4);
            this.et_notes.setText(trim3);
            EditText editText = this.et_rbc_value;
            editText.setSelection(editText.getText().toString().trim().length());
            EditText editText2 = this.et_wbc_value;
            editText2.setSelection(editText2.getText().toString().trim().length());
            EditText editText3 = this.et_platelets_value;
            editText3.setSelection(editText3.getText().toString().trim().length());
            EditText editText4 = this.et_hemoglobin_value;
            editText4.setSelection(editText4.getText().toString().trim().length());
            EditText editText5 = this.et_notes;
            editText5.setSelection(editText5.getText().toString().trim().length());
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
            String trim3 = this.et_rbc_value.getText().toString().trim();
            String trim4 = this.et_wbc_value.getText().toString().trim();
            String trim5 = this.et_platelets_value.getText().toString().trim();
            String trim6 = this.et_hemoglobin_value.getText().toString().trim();
            String trim7 = this.et_notes.getText().toString().trim();
            if (TextUtils.isEmpty(trim3)) {
                this.et_rbc_value.setError(AppConstants.error_field_require);
                editText = this.et_rbc_value;
                z = true;
            }
            if (z) {
                editText.requestFocus();
                return;
            }
            BloodCountData bloodCountData = new BloodCountData();
            if (!AppConstants.is_blood_count_edit_mode) {
                bloodCountData.user_id = sharedPreferencesUtils.getUserId();
                bloodCountData.date = trim.trim();
                bloodCountData.time = trim2.trim();
                bloodCountData.rbc_value = Float.parseFloat(trim3.trim());
                bloodCountData.wbc_value = Float.parseFloat(trim4.trim());
                bloodCountData.platelets_value = Float.parseFloat(trim5.trim());
                bloodCountData.hemoglobin_value = Float.parseFloat(trim6.trim());
                bloodCountData.notes = trim7.trim();
                GetDateTime(trim.trim(), trim2.trim());
                bloodCountData.dateTime = this.date_time;
                bloodCountData.day = this.day;
                bloodCountData.month = this.month;
                bloodCountData.year = this.year;
                bloodCountData.hour = this.hour;
                this.SQLite_health_tracker.InsertBloodCountData(bloodCountData);
                EUGeneralClass.ShowSuccessToast(this, "Blood Count Data saved successfully!");
                onBackPressed();
                return;
            }
            int i = AppConstants.selected_blood_count_data.row_id;
            bloodCountData.user_id = this.sharedPreferencesUtils.getUserId();
            bloodCountData.date = trim.trim();
            bloodCountData.time = trim2.trim();
            bloodCountData.rbc_value = Float.parseFloat(trim3.trim());
            bloodCountData.wbc_value = Float.parseFloat(trim4.trim());
            bloodCountData.platelets_value = Float.parseFloat(trim5.trim());
            bloodCountData.hemoglobin_value = Float.parseFloat(trim6.trim());
            bloodCountData.notes = trim7.trim();
            GetDateTime(trim.trim(), trim2.trim());
            bloodCountData.dateTime = this.date_time;
            bloodCountData.day = this.day;
            bloodCountData.month = this.month;
            bloodCountData.year = this.year;
            bloodCountData.hour = this.hour;
            bloodCountData.minute = this.hour;
            this.SQLite_health_tracker.UpdateBloodCountData(i, this.sharedPreferencesUtils.getUserId(), bloodCountData);
            EUGeneralClass.ShowSuccessToast(this, "Blood Count Data updated successfully!");
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
        ((TextView) findViewById(R.id.toolbar_txt_title_2)).setText(getResources().getString(R.string.lbl_header_blood_count_data));
        findViewById(R.id.toolbar_rel_save).setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                AddBloodCountActivity.this.SaveProcess();
            }
        });
        findViewById(R.id.toolbar_rel_back).setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                AddBloodCountActivity.this.onBackPressed();
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
