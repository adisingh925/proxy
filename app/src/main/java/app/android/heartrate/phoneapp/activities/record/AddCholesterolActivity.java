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
import app.android.heartrate.phoneapp.model.classes.CholesterolData;
import app.android.heartrate.phoneapp.model.classes.UserProfileData;
import app.android.heartrate.phoneapp.sharedpreferences.SharedPreferences;
import app.android.heartrate.phoneapp.sqlite.SQLiteHealthTracker;
import app.android.heartrate.phoneapp.utils.AppConstants;
import app.android.heartrate.phoneapp.utils.EUGeneralClass;

public class AddCholesterolActivity extends AppCompatActivity {
    public static Activity activity_add_cholesterol;
    SQLiteHealthTracker SQLite_health_tracker;
    String date_time = "";
    int day;
    EditText et_hdl_value;
    EditText et_ldl_value;
    EditText et_notes;
    EditText et_total_cholesterol;
    EditText et_triglyceride_value;
    int hour;
    Context mContext;
    int month;
    Animation push_animation;
    RelativeLayout rel_select_date;
    RelativeLayout rel_select_time;
    SpinnerProfileAdapter spinner_profile_adapter;
    Spinner spinner_profiles;
    TextView txt_date;
    TextView spinner_txt_name;
    TextView txt_time;
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
        setContentView(R.layout.activity_add_cholesterol);

        this.mContext = this;
        activity_add_cholesterol = this;
        this.push_animation = AnimationUtils.loadAnimation(this, R.anim.view_push);
        setUpActionBar();
        sharedPreferencesUtils = SharedPreferences.INSTANCE;
        SQLiteHealthTracker sQLiteHealthTracker = new SQLiteHealthTracker(this);
        this.SQLite_health_tracker = sQLiteHealthTracker;
        sQLiteHealthTracker.openToWrite();
        this.spinner_profiles = findViewById(R.id.add_cholesterol_spinner_profiles);
        this.et_total_cholesterol = findViewById(R.id.add_cholesterol_et_total_value);
        this.et_hdl_value = findViewById(R.id.add_cholesterol_et_hdl_value);
        this.et_ldl_value = findViewById(R.id.add_cholesterol_et_ldl_value);
        this.et_triglyceride_value = findViewById(R.id.add_cholesterol_et_triglyceride);
        this.et_notes = findViewById(R.id.add_cholesterol_et_notes);
        this.rel_select_date = findViewById(R.id.add_cholesterol_rel_select_date);
        this.txt_date = findViewById(R.id.add_cholesterol_txt_date);
        this.rel_select_time = findViewById(R.id.add_cholesterol_rel_select_time);
        this.txt_time = findViewById(R.id.add_cholesterol_txt_time);
        this.spinner_txt_name = findViewById(R.id.spinner_txt_name);
        SetProfileSpinner();
        SetCurrentDateTime();
        if (AppConstants.is_cholesterol_edit_mode) {
            SetCholesterolData();
        }
        this.rel_select_date.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                AddCholesterolActivity.this.hideSoftKeyboard();
                Calendar instance = Calendar.getInstance();
                AddCholesterolActivity.this.save_entry_year = instance.get(1);
                AddCholesterolActivity.this.save_entry_month = instance.get(2);
                AddCholesterolActivity.this.save_entry_day = instance.get(5);
                new DatePickerDialog(AddCholesterolActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {


                    public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                        try {
                            AddCholesterolActivity.this.txt_date.setText(new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("dd/MM/yyyy").parse((i3 + "/" + (i2 + 1) + "/" + i).trim())));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, AddCholesterolActivity.this.save_entry_year, AddCholesterolActivity.this.save_entry_month, AddCholesterolActivity.this.save_entry_day).show();
            }
        });
        this.rel_select_time.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                Calendar instance = Calendar.getInstance();
                AddCholesterolActivity.this.save_entry_hour = instance.get(11);
                AddCholesterolActivity.this.save_entry_minute = instance.get(12);
                new TimePickerDialog(AddCholesterolActivity.this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {


                    public void onTimeSet(TimePicker timePicker, int i, int i2) {
                        try {
                            AddCholesterolActivity.this.txt_time.setText(new SimpleDateFormat("hh:mm aa").format(new SimpleDateFormat("hh:mm").parse(i + ":" + i2)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, AddCholesterolActivity.this.save_entry_hour, AddCholesterolActivity.this.save_entry_minute, false).show();
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

    private void SetCholesterolData() {
        if (AppConstants.selected_cholesterol_data != null) {
            String trim = AppConstants.selected_cholesterol_data.date.trim();
            String trim2 = AppConstants.selected_cholesterol_data.time.trim();
            String valueOf = String.valueOf(AppConstants.selected_cholesterol_data.cholesterol_value);
            String valueOf2 = String.valueOf(AppConstants.selected_cholesterol_data.hdl_value);
            String valueOf3 = String.valueOf(AppConstants.selected_cholesterol_data.ldl_value);
            String valueOf4 = String.valueOf(AppConstants.selected_cholesterol_data.triglyceride_value);
            String trim3 = AppConstants.selected_cholesterol_data.notes.trim();
            this.txt_date.setText(trim);
            this.txt_time.setText(trim2);
            this.et_total_cholesterol.setText(valueOf);
            this.et_hdl_value.setText(valueOf2);
            this.et_ldl_value.setText(valueOf3);
            this.et_triglyceride_value.setText(valueOf4);
            this.et_notes.setText(trim3);
            EditText editText = this.et_total_cholesterol;
            editText.setSelection(editText.getText().toString().trim().length());
            EditText editText2 = this.et_hdl_value;
            editText2.setSelection(editText2.getText().toString().trim().length());
            EditText editText3 = this.et_ldl_value;
            editText3.setSelection(editText3.getText().toString().trim().length());
            EditText editText4 = this.et_triglyceride_value;
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
            String trim3 = this.et_total_cholesterol.getText().toString().trim();
            String trim4 = this.et_hdl_value.getText().toString().trim();
            String trim5 = this.et_ldl_value.getText().toString().trim();
            String trim6 = this.et_triglyceride_value.getText().toString().trim();
            String GetCholesterolResultText = AppConstants.GetCholesterolResultText(Float.parseFloat(trim3));
            String trim7 = this.et_notes.getText().toString().trim();
            float parseFloat = Float.parseFloat(trim3.trim());
            if (TextUtils.isEmpty(trim3)) {
                this.et_total_cholesterol.setError(AppConstants.error_field_require);
                editText = this.et_total_cholesterol;
                z = true;
            }
            if (z) {
                editText.requestFocus();
                return;
            }
            CholesterolData cholesterolData = new CholesterolData();
            if (!AppConstants.is_cholesterol_edit_mode) {
                cholesterolData.user_id = sharedPreferencesUtils.getUserId();
                cholesterolData.date = trim.trim();
                cholesterolData.time = trim2.trim();
                cholesterolData.cholesterol_value = parseFloat;
                cholesterolData.hdl_value = Float.parseFloat(trim4.trim());
                cholesterolData.ldl_value = Float.parseFloat(trim5.trim());
                cholesterolData.triglyceride_value = Float.parseFloat(trim6.trim());
                cholesterolData.result = GetCholesterolResultText.trim();
                cholesterolData.notes = trim7.trim();
                GetDateTime(trim.trim(), trim2.trim());
                cholesterolData.dateTime = this.date_time;
                cholesterolData.day = this.day;
                cholesterolData.month = this.month;
                cholesterolData.year = this.year;
                cholesterolData.hour = this.hour;
                this.SQLite_health_tracker.InsertCholesterolData(cholesterolData);
                EUGeneralClass.ShowSuccessToast(this, "Cholesterol Data saved successfully!");
                onBackPressed();
                return;
            }
            int i2 = AppConstants.selected_cholesterol_data.row_id;
            cholesterolData.user_id = AppConstants.selected_cholesterol_data.user_id;
            cholesterolData.date = trim.trim();
            cholesterolData.time = trim2.trim();
            cholesterolData.cholesterol_value = Float.parseFloat(trim3.trim());
            cholesterolData.hdl_value = Float.parseFloat(trim4.trim());
            cholesterolData.ldl_value = Float.parseFloat(trim5.trim());
            cholesterolData.triglyceride_value = Float.parseFloat(trim6.trim());
            cholesterolData.result = GetCholesterolResultText.trim();
            cholesterolData.notes = trim7.trim();
            GetDateTime(trim.trim(), trim2.trim());
            cholesterolData.dateTime = this.date_time;
            cholesterolData.day = this.day;
            cholesterolData.month = this.month;
            cholesterolData.year = this.year;
            cholesterolData.hour = this.hour;
            this.SQLite_health_tracker.UpdateCholesterolData(i2, this.sharedPreferencesUtils.getUserId(), cholesterolData);
            EUGeneralClass.ShowSuccessToast(this, "Cholesterol Data updated successfully!");
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
        ((TextView) findViewById(R.id.toolbar_txt_title_2)).setText(getResources().getString(R.string.lbl_header_cholesterol_data));
        findViewById(R.id.toolbar_rel_save).setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                AddCholesterolActivity.this.SaveProcess();
            }
        });
        findViewById(R.id.toolbar_rel_back).setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                AddCholesterolActivity.this.onBackPressed();
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
