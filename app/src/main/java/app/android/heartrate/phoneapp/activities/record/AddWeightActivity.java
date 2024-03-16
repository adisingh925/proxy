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

import com.shawnlin.numberpicker.NumberPicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import app.android.heartrate.phoneapp.AdAdmob;
import app.android.heartrate.phoneapp.R;
import app.android.heartrate.phoneapp.adapters.SpinnerProfileAdapter;
import app.android.heartrate.phoneapp.model.ProfileData;
import app.android.heartrate.phoneapp.model.classes.UserProfileData;
import app.android.heartrate.phoneapp.model.classes.WeightData;
import app.android.heartrate.phoneapp.sharedpreferences.SharedPreferences;
import app.android.heartrate.phoneapp.sqlite.SQLiteHealthTracker;
import app.android.heartrate.phoneapp.utils.AppConstants;
import app.android.heartrate.phoneapp.utils.EUGeneralClass;

public class AddWeightActivity extends AppCompatActivity {
    public static Activity activity_add_profile;
    public int weightIndex = 0;
    SQLiteHealthTracker SQLite_health_tracker;
    String date_time = "";
    int day;
    EditText et_notes;
    int hour;
    Context mContext;
    int month;
    NumberPicker np_weight;
    Animation push_animation;
    RelativeLayout rel_select_date;
    RelativeLayout rel_select_time;

    SpinnerProfileAdapter spinner_profile_adapter;
    Spinner spinner_profiles;
    TextView txt_date;
    TextView txt_time;
    TextView spinner_txt_name;
    TextView txt_weight;
    int weight_default_value = 55;
    int weight_max_value = 300;
    int weight_min_value = 1;
    int year;
    private String[] array_weight_range;
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
        setContentView(R.layout.activity_add_weight);

        this.mContext = this;
        activity_add_profile = this;
        this.push_animation = AnimationUtils.loadAnimation(this, R.anim.view_push);
        setUpActionBar();
        sharedPreferencesUtils = SharedPreferences.INSTANCE;
        SQLiteHealthTracker sQLiteHealthTracker = new SQLiteHealthTracker(this);
        this.SQLite_health_tracker = sQLiteHealthTracker;
        sQLiteHealthTracker.openToWrite();
        this.spinner_profiles = findViewById(R.id.weight_spinner_profiles);
        this.np_weight = findViewById(R.id.add_weight_np_weight_value);
        this.txt_weight = findViewById(R.id.add_weight_txt_weight);
        this.spinner_txt_name = findViewById(R.id.spinner_txt_name);
        this.et_notes = findViewById(R.id.add_weight_et_notes);
        this.rel_select_date = findViewById(R.id.weight_rel_select_date);
        this.txt_date = findViewById(R.id.weight_txt_date);
        this.rel_select_time = findViewById(R.id.weight_rel_select_time);
        this.txt_time = findViewById(R.id.weight_txt_time);
        SetProfileSpinner();
        SetCurrentDateTime();
        SetWeightPicker();
        if (AppConstants.is_weight_edit_mode) {
            SetWeightData();
        }
        this.rel_select_date.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                AddWeightActivity.this.hideSoftKeyboard();
                Calendar instance = Calendar.getInstance();
                AddWeightActivity.this.save_entry_year = instance.get(1);
                AddWeightActivity.this.save_entry_month = instance.get(2);
                AddWeightActivity.this.save_entry_day = instance.get(5);
                new DatePickerDialog(AddWeightActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {


                    public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                        try {
                            AddWeightActivity.this.txt_date.setText(new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("dd/MM/yyyy").parse((i3 + "/" + (i2 + 1) + "/" + i).trim())));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, AddWeightActivity.this.save_entry_year, AddWeightActivity.this.save_entry_month, AddWeightActivity.this.save_entry_day).show();
            }
        });
        this.rel_select_time.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                Calendar instance = Calendar.getInstance();
                AddWeightActivity.this.save_entry_hour = instance.get(11);
                AddWeightActivity.this.save_entry_minute = instance.get(12);
                new TimePickerDialog(AddWeightActivity.this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {


                    public void onTimeSet(TimePicker timePicker, int i, int i2) {
                        try {
                            AddWeightActivity.this.txt_time.setText(new SimpleDateFormat("hh:mm aa").format(new SimpleDateFormat("hh:mm").parse(i + ":" + i2)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, AddWeightActivity.this.save_entry_hour, AddWeightActivity.this.save_entry_minute, false).show();
            }
        });
    }

    private void SetWeightPicker() {
        this.array_weight_range = new String[300];
        int i = 0;
        while (true) {
            String[] strArr = this.array_weight_range;
            if (i >= strArr.length) {
                break;
            }
            int i2 = i + 1;
            strArr[i] = String.valueOf(i2);
            i = i2;
        }
        int i3 = 0;
        while (true) {
            String[] strArr2 = this.array_weight_range;
            if (i3 >= strArr2.length) {
                break;
            } else if (strArr2[i3].equalsIgnoreCase(String.valueOf(this.weight_default_value))) {
                this.weightIndex = i3;
                break;
            } else {
                i3++;
            }
        }
        this.np_weight.setMaxValue(this.weight_max_value);
        this.np_weight.setMinValue(this.weight_min_value);
        this.np_weight.setDisplayedValues(this.array_weight_range);
        this.np_weight.setWrapSelectorWheel(false);
        this.np_weight.setValue(this.weight_default_value);
        this.np_weight.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {


            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i2) {
                AddWeightActivity.this.weightIndex = i2;
                AddWeightActivity.this.txt_weight.setText(String.valueOf(numberPicker.getValue()).trim());
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

    private void SetWeightData() {
        if (AppConstants.selected_weight_data != null) {
            String trim = AppConstants.selected_weight_data.date.trim();
            String trim2 = AppConstants.selected_weight_data.time.trim();
            String.valueOf(AppConstants.selected_weight_data.weight);
            String trim3 = AppConstants.selected_weight_data.notes.trim();
            this.weight_default_value = (int) AppConstants.selected_weight_data.weight;
            this.txt_date.setText(trim);
            this.txt_time.setText(trim2);
            int i = 0;
            while (true) {
                String[] strArr = this.array_weight_range;
                if (i >= strArr.length) {
                    break;
                } else if (strArr[i].equalsIgnoreCase(String.valueOf(this.weight_default_value))) {
                    this.weightIndex = i;
                    break;
                } else {
                    i++;
                }
            }
            this.np_weight.setMaxValue(this.weight_max_value);
            this.np_weight.setMinValue(this.weight_min_value);
            this.np_weight.setDisplayedValues(this.array_weight_range);
            this.np_weight.setWrapSelectorWheel(false);
            this.np_weight.setValue(this.weightIndex + 1);
            this.txt_weight.setText(String.valueOf(this.weight_default_value));
            this.et_notes.setText(trim3);
            return;
        }
        EUGeneralClass.ShowErrorToast(this, "Something went wrong!");
    }


    private void SaveProcess() {
        boolean z = false;
        TextView textView = null;
        try {
            String trim = this.txt_date.getText().toString().trim();
            String trim2 = this.txt_time.getText().toString().trim();
            String trim3 = this.txt_weight.getText().toString().trim();
            String trim4 = this.et_notes.getText().toString().trim();
            if (TextUtils.isEmpty(trim3)) {
                this.txt_weight.setError(AppConstants.error_field_require);
                textView = this.txt_weight;
                z = true;
            }
            if (z) {
                textView.requestFocus();
                return;
            }
            WeightData weightData = new WeightData();
            if (!AppConstants.is_weight_edit_mode) {
                weightData.user_id = this.sharedPreferencesUtils.getUserId();
                weightData.date = trim.trim();
                weightData.time = trim2.trim();
                weightData.weight = Float.parseFloat(trim3.trim());
                weightData.notes = trim4.trim();
                GetDateTime(trim.trim(), trim2.trim());
                weightData.dateTime = this.date_time;
                weightData.day = this.day;
                weightData.month = this.month;
                weightData.year = this.year;
                weightData.hour = this.hour;
                this.SQLite_health_tracker.InsertWeightData(weightData);
                EUGeneralClass.ShowSuccessToast(this, "Weight Data saved successfully!");
                onBackPressed();
                return;
            }
            int i = AppConstants.selected_weight_data.row_id;
            weightData.row_id = i;
            weightData.user_id = this.sharedPreferencesUtils.getUserId();
            weightData.date = trim.trim();
            weightData.time = trim2.trim();
            weightData.weight = Float.parseFloat(trim3.trim());
            weightData.notes = trim4.trim();
            GetDateTime(trim.trim(), trim2.trim());
            weightData.dateTime = this.date_time;
            weightData.day = this.day;
            weightData.month = this.month;
            weightData.year = this.year;
            weightData.hour = this.hour;
            this.SQLite_health_tracker.UpdateWeightData(i, this.sharedPreferencesUtils.getUserId(), weightData);
            EUGeneralClass.ShowSuccessToast(this, "Weight Data updated successfully!");
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
        ((TextView) findViewById(R.id.toolbar_txt_title_2)).setText(getResources().getString(R.string.lbl_header_weight));
        findViewById(R.id.toolbar_rel_save).setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                AddWeightActivity.this.SaveProcess();
            }
        });
        findViewById(R.id.toolbar_rel_back).setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                AddWeightActivity.this.onBackPressed();
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
