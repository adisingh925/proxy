package app.android.heartrate.phoneapp.activities.record;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import app.android.heartrate.phoneapp.AdAdmob;
import app.android.heartrate.phoneapp.R;
import app.android.heartrate.phoneapp.adapters.SpinnerProfileAdapter;
import app.android.heartrate.phoneapp.model.classes.BloodPressureData;
import app.android.heartrate.phoneapp.model.classes.UserProfileData;
import app.android.heartrate.phoneapp.sqlite.SQLiteHealthTracker;
import app.android.heartrate.phoneapp.utils.AppConstants;
import app.android.heartrate.phoneapp.utils.EUGeneralClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddBloodPressureActivity extends AppCompatActivity {
    public static Activity activity_add_blood_pressure;
    SQLiteHealthTracker SQLite_health_tracker;
    int[] arrayProfileIds;
    String[] arrayProfileNames;
    ArrayList<UserProfileData> array_profiles = new ArrayList<>();

    String blood_pressure_result = "";
    private String current_date_time = "";
    String date = "";
    String date_time = "";
    int day;
    String diastolic_string = "";
    int diastolic_value = 0;
    EditText et_diastolic_level;
    EditText et_notes;
    EditText et_pulse_rate_level;
    EditText et_systolic_level;
    int hour;
    Context mContext;
    String mean_arterial_string = "";
    int mean_arterial_value = 0;
    int month;
    String notes = "";
    String pulse_pressure_string = "";
    int pulse_pressure_value = 0;
    String pulse_rate_string = "";
    int pulse_rate_value = 0;
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
    String status_color = "#8CC63F";
    String systolic_string = "";
    int systolic_value = 0;
    String time = "";
    TextView txt_date;
    TextView txt_mean_arterial_level;
    TextView txt_pulse_pressure_level;
    TextView txt_time;
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
        setContentView(R.layout.activity_add_blood_pressure);

        this.mContext = this;
        activity_add_blood_pressure = this;
        this.push_animation = AnimationUtils.loadAnimation(this, R.anim.view_push);
        setUpActionBar();
        SQLiteHealthTracker sQLiteHealthTracker = new SQLiteHealthTracker(this);
        this.SQLite_health_tracker = sQLiteHealthTracker;
        sQLiteHealthTracker.openToWrite();
        this.spinner_profiles = findViewById(R.id.add_bp_spinner_profiles);
        this.et_systolic_level = findViewById(R.id.add_bp_et_systolic_value);
        this.et_diastolic_level = findViewById(R.id.add_bp_et_diastolic_value);
        this.et_pulse_rate_level = findViewById(R.id.add_bp_et_pulse_rate);
        this.rel_select_date = findViewById(R.id.add_bp_rel_select_date);
        this.txt_date = findViewById(R.id.add_bp_txt_date);
        this.rel_select_time = findViewById(R.id.add_bp_rel_select_time);
        this.txt_time = findViewById(R.id.add_bp_txt_time);
        this.txt_pulse_pressure_level = findViewById(R.id.add_bp_txt_pulse_pressure_value);
        this.txt_mean_arterial_level = findViewById(R.id.add_bp_txt_mean_arterial_value);
        this.et_notes = findViewById(R.id.add_bp_et_notes);
        this.et_systolic_level.setText(String.valueOf(AppConstants.default_systolic_value));
        this.et_diastolic_level.setText(String.valueOf(AppConstants.default_diastolic_value));
        this.et_pulse_rate_level.setText(String.valueOf(AppConstants.default_pulse_rate_value));
        this.txt_pulse_pressure_level.setText(CalculatePulsePressure(AppConstants.default_systolic_value, AppConstants.default_diastolic_value));
        this.txt_mean_arterial_level.setText(CalculateMAP(AppConstants.default_systolic_value, AppConstants.default_diastolic_value));
        SetProfileSpinner();
        SetCurrentDateTime();
        if (AppConstants.is_bp_edit_mode) {
            SetBloodPressureData();
        }
        this.rel_select_date.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                AddBloodPressureActivity.this.hideSoftKeyboard();
                Calendar instance = Calendar.getInstance();
                AddBloodPressureActivity.this.save_entry_year = instance.get(1);
                AddBloodPressureActivity.this.save_entry_month = instance.get(2);
                AddBloodPressureActivity.this.save_entry_day = instance.get(5);
                new DatePickerDialog(AddBloodPressureActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {


                    public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                        try {
                            AddBloodPressureActivity.this.txt_date.setText(new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("dd/MM/yyyy").parse((i3 + "/" + (i2 + 1) + "/" + i).trim())));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, AddBloodPressureActivity.this.save_entry_year, AddBloodPressureActivity.this.save_entry_month, AddBloodPressureActivity.this.save_entry_day).show();
            }
        });
        this.rel_select_time.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                Calendar instance = Calendar.getInstance();
                AddBloodPressureActivity.this.save_entry_hour = instance.get(11);
                AddBloodPressureActivity.this.save_entry_minute = instance.get(12);
                new TimePickerDialog(AddBloodPressureActivity.this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {


                    public void onTimeSet(TimePicker timePicker, int i, int i2) {
                        try {
                            AddBloodPressureActivity.this.txt_time.setText(new SimpleDateFormat("hh:mm aa").format(new SimpleDateFormat("hh:mm").parse(i + ":" + i2)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, AddBloodPressureActivity.this.save_entry_hour, AddBloodPressureActivity.this.save_entry_minute, false).show();
            }
        });
        this.et_systolic_level.addTextChangedListener(new TextWatcher() {


            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                int length = AddBloodPressureActivity.this.et_systolic_level.getText().toString().trim().length();
                int length2 = AddBloodPressureActivity.this.et_diastolic_level.getText().toString().trim().length();
                if (length > 0 && length2 > 0) {
                    int parseInt = Integer.parseInt(AddBloodPressureActivity.this.et_systolic_level.getText().toString().trim());
                    int parseInt2 = Integer.parseInt(AddBloodPressureActivity.this.et_diastolic_level.getText().toString().trim());
                    AddBloodPressureActivity.this.txt_pulse_pressure_level.setText(AddBloodPressureActivity.this.CalculatePulsePressure(parseInt, parseInt2));
                    AddBloodPressureActivity.this.txt_mean_arterial_level.setText(AddBloodPressureActivity.this.CalculateMAP(parseInt, parseInt2));
                }
            }
        });
        this.et_diastolic_level.addTextChangedListener(new TextWatcher() {


            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                int length = AddBloodPressureActivity.this.et_systolic_level.getText().toString().trim().length();
                int length2 = AddBloodPressureActivity.this.et_diastolic_level.getText().toString().trim().length();
                if (length > 0 && length2 > 0) {
                    int parseInt = Integer.parseInt(AddBloodPressureActivity.this.et_systolic_level.getText().toString().trim());
                    int parseInt2 = Integer.parseInt(AddBloodPressureActivity.this.et_diastolic_level.getText().toString().trim());
                    AddBloodPressureActivity.this.txt_pulse_pressure_level.setText(AddBloodPressureActivity.this.CalculatePulsePressure(parseInt, parseInt2));
                    AddBloodPressureActivity.this.txt_mean_arterial_level.setText(AddBloodPressureActivity.this.CalculateMAP(parseInt, parseInt2));
                }
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
                int i2 = AddBloodPressureActivity.this.arrayProfileIds[i];
                String trim = AddBloodPressureActivity.this.arrayProfileNames[i].trim();
                Log.e("selected Profile :", "ID :" + i2 + "\nName :" + trim);
            }
        });
    }

    public void hideSoftKeyboard() {
        View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }

    private void SetBloodPressureData() {
        if (AppConstants.selected_bp_data != null) {
            this.date = AppConstants.selected_bp_data.date.trim();
            this.time = AppConstants.selected_bp_data.time.trim();
            this.systolic_value = AppConstants.selected_bp_data.systolic_value;
            this.diastolic_value = AppConstants.selected_bp_data.diastolic_value;
            this.pulse_rate_value = AppConstants.selected_bp_data.pulse_rate_value;
            this.pulse_pressure_value = AppConstants.selected_bp_data.pulse_pressure_value;
            this.mean_arterial_value = AppConstants.selected_bp_data.mean_arterial_pressure_value;
            this.status_color = AppConstants.selected_bp_data.status_color.trim();
            this.blood_pressure_result = AppConstants.selected_bp_data.result.trim();
            this.notes = AppConstants.selected_bp_data.notes.trim();
            this.txt_date.setText(this.date);
            this.txt_time.setText(this.time);
            this.systolic_string = String.valueOf(this.systolic_value);
            this.diastolic_string = String.valueOf(this.diastolic_value);
            this.pulse_rate_string = String.valueOf(this.pulse_rate_value);
            this.et_systolic_level.setText(this.systolic_string);
            this.et_diastolic_level.setText(this.diastolic_string);
            this.et_pulse_rate_level.setText(this.pulse_rate_string);
            this.txt_pulse_pressure_level.setText(String.valueOf(this.pulse_pressure_value));
            this.txt_mean_arterial_level.setText(String.valueOf(this.mean_arterial_value));
            this.et_notes.setText(this.notes);
            int i = AppConstants.selected_bp_data.user_id;
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
                return;
            }
            return;
        }
        EUGeneralClass.ShowErrorToast(this, "Something went wrong!");
    }


    private void SaveProcess() {
        try {
            this.date = this.txt_date.getText().toString().trim();
            this.time = this.txt_time.getText().toString().trim();
            this.systolic_value = Integer.parseInt(this.et_systolic_level.getText().toString().trim());
            this.diastolic_value = Integer.parseInt(this.et_diastolic_level.getText().toString().trim());
            this.pulse_rate_value = Integer.parseInt(this.et_pulse_rate_level.getText().toString().trim());
            this.systolic_string = String.valueOf(this.systolic_value);
            this.diastolic_string = String.valueOf(this.diastolic_value);
            this.pulse_rate_string = String.valueOf(this.pulse_rate_value);
            String charSequence = this.txt_pulse_pressure_level.getText().toString();
            this.pulse_pressure_string = charSequence;
            this.pulse_pressure_value = Integer.parseInt(charSequence);
            String charSequence2 = this.txt_mean_arterial_level.getText().toString();
            this.mean_arterial_string = charSequence2;
            this.mean_arterial_value = Integer.parseInt(charSequence2);
            this.notes = this.et_notes.getText().toString();
            GetResult();
            BloodPressureData bloodPressureData = new BloodPressureData();
            int selectedItemPosition = this.spinner_profiles.getSelectedItemPosition();
            this.selected_user_id = this.arrayProfileIds[selectedItemPosition];
            this.selected_user_name = this.arrayProfileNames[selectedItemPosition];
            if (!AppConstants.is_bp_edit_mode) {
                bloodPressureData.user_id = this.selected_user_id;
                bloodPressureData.date = this.date.trim();
                bloodPressureData.time = this.time.trim();
                bloodPressureData.systolic_value = this.systolic_value;
                bloodPressureData.diastolic_value = this.diastolic_value;
                bloodPressureData.pulse_pressure_value = this.pulse_pressure_value;
                bloodPressureData.pulse_rate_value = this.pulse_rate_value;
                bloodPressureData.mean_arterial_pressure_value = this.mean_arterial_value;
                bloodPressureData.status_color = this.status_color.trim();
                bloodPressureData.result = this.blood_pressure_result.trim();
                bloodPressureData.notes = this.notes.trim();
                GetDateTime(this.date.trim(), this.time.trim());
                bloodPressureData.dateTime = this.date_time;
                bloodPressureData.day = this.day;
                bloodPressureData.month = this.month;
                bloodPressureData.year = this.year;
                bloodPressureData.hour = this.hour;
                Long insert = SQLite_health_tracker.InsertBloodPressureData(bloodPressureData);
                Log.e(" insert ", " ===> " + insert);
                EUGeneralClass.ShowSuccessToast(this, AppConstants.data_saved_messages);
                onBackPressed();
                return;
            }
            int i = AppConstants.selected_bp_data.row_id;
            bloodPressureData.user_id = this.selected_user_id;
            bloodPressureData.date = this.date.trim();
            bloodPressureData.time = this.time.trim();
            bloodPressureData.systolic_value = this.systolic_value;
            bloodPressureData.diastolic_value = this.diastolic_value;
            bloodPressureData.pulse_pressure_value = this.pulse_pressure_value;
            bloodPressureData.pulse_rate_value = this.pulse_rate_value;
            bloodPressureData.mean_arterial_pressure_value = this.mean_arterial_value;
            bloodPressureData.status_color = this.status_color.trim();
            bloodPressureData.result = this.blood_pressure_result.trim();
            bloodPressureData.notes = this.notes.trim();
            GetDateTime(this.date.trim(), this.time.trim());
            bloodPressureData.dateTime = this.date_time;
            bloodPressureData.day = this.day;
            bloodPressureData.month = this.month;
            bloodPressureData.year = this.year;
            bloodPressureData.hour = this.hour;
            this.SQLite_health_tracker.UpdateBloodPressureData(i, this.selected_user_id, bloodPressureData);
            EUGeneralClass.ShowSuccessToast(this, AppConstants.data_updated_messages);
            onBackPressed();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void GetResult() {
        int i;
        int i2 = this.systolic_value;
        if (i2 < 70 && this.diastolic_value < 40) {
            this.blood_pressure_result = AppConstants.pressure_level_low;
            this.status_color = "#00A99D";
        } else if ((i2 >= 70 && i2 < 90) || ((i = this.diastolic_value) >= 40 && i < 60)) {
            this.blood_pressure_result = AppConstants.pressure_level_low;
            this.status_color = "#00A99D";
        } else if (i2 >= 90 && i2 < 120 && i >= 60 && i < 80) {
            this.blood_pressure_result = AppConstants.pressure_level_normal;
            this.status_color = "#8CC63F";
        } else if ((i2 >= 120 && i2 < 140) || (i >= 80 && i < 90)) {
            this.blood_pressure_result = AppConstants.pressure_level_pre_hypertension;
            this.status_color = "#D9E021";
        } else if ((i2 >= 140 && i2 < 160) || (i >= 90 && i < 100)) {
            this.blood_pressure_result = AppConstants.pressure_level_high_stage_1;
            this.status_color = "#FBB03B";
        } else if ((i2 >= 160 && i2 < 180) || (i >= 100 && i < 110)) {
            this.blood_pressure_result = AppConstants.pressure_level_high_stage_2;
            this.status_color = "#F15A24";
        } else if (i2 >= 180 || i >= 110) {
            this.blood_pressure_result = AppConstants.pressure_level_high_crisis;
            this.status_color = "#ED1C24";
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

    public String CalculatePulsePressure(int i, int i2) {
        return String.valueOf(i - i2);
    }

    public String CalculateMAP(int i, int i2) {
        return String.valueOf((i + (i2 * 2)) / 3);
    }

    private void setUpActionBar() {
        ((TextView) findViewById(R.id.toolbar_txt_title_1)).setText(getResources().getString(R.string.lbl_header_add));
        ((TextView) findViewById(R.id.toolbar_txt_title_2)).setText(getResources().getString(R.string.lbl_header_blood_pressure));
        findViewById(R.id.toolbar_rel_save).setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                AddBloodPressureActivity.this.SaveProcess();
            }
        });
        findViewById(R.id.toolbar_rel_back).setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                AddBloodPressureActivity.this.onBackPressed();
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
