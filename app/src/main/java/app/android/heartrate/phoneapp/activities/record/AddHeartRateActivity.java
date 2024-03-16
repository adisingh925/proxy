package app.android.heartrate.phoneapp.activities.record;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
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

import com.shawnlin.numberpicker.NumberPicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import app.android.heartrate.phoneapp.AdAdmob;
import app.android.heartrate.phoneapp.R;
import app.android.heartrate.phoneapp.adapters.SpinnerProfileAdapter;
import app.android.heartrate.phoneapp.model.classes.HeartRateData;
import app.android.heartrate.phoneapp.model.classes.UserProfileData;
import app.android.heartrate.phoneapp.sqlite.SQLiteHealthTracker;
import app.android.heartrate.phoneapp.utils.AppConstants;
import app.android.heartrate.phoneapp.utils.EUGeneralClass;

public class AddHeartRateActivity extends AppCompatActivity {
    public static Activity activity_add_heart_rate;
    public int ageIndex = 0;
    SQLiteHealthTracker SQLite_health_tracker;
    ArrayAdapter<String> adapter_spinner_gender;
    ArrayAdapter<String> adapter_spinner_status;
    int age_default_value = AppConstants.default_hr_age_value;
    int age_max_value = 120;
    int age_min_value = 1;
    String age_string = "";
    String age_unit = "";
    int age_value = 0;
    ArrayList<String> arrayListCurrentStatus = new ArrayList<>(Arrays.asList(AppConstants.hr_status_resting, AppConstants.hr_status_general, AppConstants.hr_status_after_exercise, AppConstants.hr_status_before_exercise, AppConstants.hr_status_tired, AppConstants.hr_status_unwell, AppConstants.hr_status_surprised, AppConstants.hr_status_sad, AppConstants.hr_status_angry, AppConstants.hr_status_fear_full, AppConstants.hr_status_in_love));
    ArrayList<String> arrayListGender = new ArrayList<>(Arrays.asList(AppConstants.hr_gender_male, AppConstants.hr_gender_female, AppConstants.hr_gender_child));
    int[] arrayProfileIds;
    String[] arrayProfileNames;
    ArrayList<UserProfileData> array_profiles = new ArrayList<>();
    String color_string = "";
    String current_status = "";
    String date = "";
    String date_time = "";
    int day;
    EditText et_heart_rate;
    EditText et_notes;
    String gender = "";
    String heart_rate_string = "";
    int heart_rate_value = 0;
    int hour;
    Context mContext;
    int month;
    String notes = "";
    NumberPicker np_age;
    Animation push_animation;
    String range_string = "";
    RelativeLayout rel_select_date;
    RelativeLayout rel_select_time;
    String result_string = "";
    int selected_user_id;
    String selected_user_name = "";
    Spinner spinner_current_status;
    Spinner spinner_gender;
    SpinnerProfileAdapter spinner_profile_adapter;
    Spinner spinner_profiles;
    String time = "";
    TextView txt_age;
    TextView txt_age_unit;
    TextView txt_date;
    TextView txt_time;
    int year;
    private String[] array_age_range;
    private String current_date_time = "";
    private int save_entry_day;
    private int save_entry_hour;
    private int save_entry_minute;
    private int save_entry_month;
    private int save_entry_year;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        SetView();

        AppConstants.overridePendingTransitionEnter(this);
    }

    private void SetView() {
        setContentView(R.layout.activity_add_heart_rate);

        this.mContext = this;
        activity_add_heart_rate = this;
        this.push_animation = AnimationUtils.loadAnimation(this, R.anim.view_push);
        setUpActionBar();
        SQLiteHealthTracker sQLiteHealthTracker = new SQLiteHealthTracker(this);
        this.SQLite_health_tracker = sQLiteHealthTracker;
        sQLiteHealthTracker.openToWrite();
        this.spinner_profiles = findViewById(R.id.add_hr_spinner_profiles);
        this.spinner_gender = findViewById(R.id.add_hr_spinner_gender);
        this.spinner_current_status = findViewById(R.id.add_hr_spinner_current_status);
        this.txt_age = findViewById(R.id.add_hr_txt_age);
        this.txt_age_unit = findViewById(R.id.add_hr_txt_age_unit);
        this.np_age = findViewById(R.id.add_hr_np_age);
        EditText editText = findViewById(R.id.add_hr_et_heart_rate);
        this.et_heart_rate = editText;
        editText.setText(String.valueOf(AppConstants.default_hr_value));
        EditText editText2 = this.et_heart_rate;
        editText2.setSelection(editText2.getText().toString().trim().length());
        this.rel_select_date = findViewById(R.id.add_hr_rel_select_date);
        this.txt_date = findViewById(R.id.add_hr_txt_date);
        this.rel_select_time = findViewById(R.id.add_hr_rel_select_time);
        this.txt_time = findViewById(R.id.add_hr_txt_time);
        this.et_notes = findViewById(R.id.add_hr_et_notes);
        SetProfileSpinner();
        SetAgePicker();
        SetGenderSpinner();
        SetCurrentStatusSpinner();
        SetCurrentDateTime();
        if (AppConstants.is_heart_rate_edit_mode) {
            SetHeartRateData();
        }
        this.rel_select_date.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                AddHeartRateActivity.this.hideSoftKeyboard();
                Calendar instance = Calendar.getInstance();
                AddHeartRateActivity.this.save_entry_year = instance.get(1);
                AddHeartRateActivity.this.save_entry_month = instance.get(2);
                AddHeartRateActivity.this.save_entry_day = instance.get(5);
                new DatePickerDialog(AddHeartRateActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {


                    public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                        try {
                            AddHeartRateActivity.this.txt_date.setText(new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("dd/MM/yyyy").parse((i3 + "/" + (i2 + 1) + "/" + i).trim())));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, AddHeartRateActivity.this.save_entry_year, AddHeartRateActivity.this.save_entry_month, AddHeartRateActivity.this.save_entry_day).show();
            }
        });
        this.rel_select_time.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                Calendar instance = Calendar.getInstance();
                AddHeartRateActivity.this.save_entry_hour = instance.get(11);
                AddHeartRateActivity.this.save_entry_minute = instance.get(12);
                new TimePickerDialog(AddHeartRateActivity.this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {


                    public void onTimeSet(TimePicker timePicker, int i, int i2) {
                        try {
                            AddHeartRateActivity.this.txt_time.setText(new SimpleDateFormat("hh:mm aa").format(new SimpleDateFormat("hh:mm").parse(i + ":" + i2)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, AddHeartRateActivity.this.save_entry_hour, AddHeartRateActivity.this.save_entry_minute, false).show();
            }
        });
    }

    private void SetAgePicker() {
        this.array_age_range = new String[this.age_max_value];
        int i = 0;
        while (true) {
            String[] strArr = this.array_age_range;
            if (i >= strArr.length) {
                break;
            }
            int i2 = i + 1;
            strArr[i] = String.valueOf(i2);
            i = i2;
        }
        int i3 = 0;
        while (true) {
            String[] strArr2 = this.array_age_range;
            if (i3 >= strArr2.length) {
                break;
            } else if (strArr2[i3].equalsIgnoreCase(String.valueOf(this.age_default_value))) {
                this.ageIndex = i3;
                break;
            } else {
                i3++;
            }
        }
        this.np_age.setMaxValue(this.age_max_value);
        this.np_age.setMinValue(this.age_min_value);
        this.np_age.setDisplayedValues(this.array_age_range);
        this.np_age.setWrapSelectorWheel(false);
        this.np_age.setValue(this.age_default_value);
        this.np_age.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {


            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i2) {
                AddHeartRateActivity.this.ageIndex = i2;
                AddHeartRateActivity.this.txt_age.setText(String.valueOf(numberPicker.getValue()).trim());
                if (numberPicker.getValue() == 1) {
                    AddHeartRateActivity.this.age_unit = AppConstants.age_unit_year;
                    AddHeartRateActivity.this.txt_age_unit.setText(AddHeartRateActivity.this.age_unit);
                } else if (numberPicker.getValue() > 1) {
                    AddHeartRateActivity.this.age_unit = AppConstants.age_unit_years;
                    AddHeartRateActivity.this.txt_age_unit.setText(AddHeartRateActivity.this.age_unit);
                }
            }
        });
        this.txt_age.setText(String.valueOf(AppConstants.default_hr_age_value));
        this.txt_age_unit.setText(AppConstants.age_unit_years);
    }

    private void SetGenderSpinner() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_list, this.arrayListGender);
        this.adapter_spinner_gender = arrayAdapter;
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list);
        this.spinner_gender.setAdapter(this.adapter_spinner_gender);
    }

    private void SetCurrentStatusSpinner() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_list, this.arrayListCurrentStatus);
        this.adapter_spinner_status = arrayAdapter;
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list);
        this.spinner_current_status.setAdapter(this.adapter_spinner_status);
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
                int i2 = AddHeartRateActivity.this.arrayProfileIds[i];
                String trim = AddHeartRateActivity.this.arrayProfileNames[i].trim();
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

    private void SetHeartRateData() {
        if (AppConstants.selected_heart_rate_data != null) {
            this.date = AppConstants.selected_heart_rate_data.date.trim();
            this.time = AppConstants.selected_heart_rate_data.time.trim();
            int i = AppConstants.selected_heart_rate_data.age;
            this.age_value = i;
            this.age_string = String.valueOf(i).trim();
            int i2 = AppConstants.selected_heart_rate_data.heart_rate_value;
            this.heart_rate_value = i2;
            this.heart_rate_string = String.valueOf(i2).trim();
            this.gender = AppConstants.selected_heart_rate_data.gender.trim();
            this.current_status = AppConstants.selected_heart_rate_data.current_status.trim();
            this.color_string = AppConstants.selected_heart_rate_data.color.trim();
            this.result_string = AppConstants.selected_heart_rate_data.result.trim();
            this.range_string = AppConstants.selected_heart_rate_data.range.trim();
            this.notes = AppConstants.selected_heart_rate_data.notes.trim();
            int i3 = 0;
            while (true) {
                String[] strArr = this.array_age_range;
                if (i3 >= strArr.length) {
                    break;
                } else if (strArr[i3].equalsIgnoreCase(String.valueOf(this.age_value))) {
                    this.ageIndex = i3;
                    break;
                } else {
                    i3++;
                }
            }
            this.np_age.setMaxValue(this.age_max_value);
            this.np_age.setMinValue(this.age_min_value);
            this.np_age.setDisplayedValues(this.array_age_range);
            this.np_age.setWrapSelectorWheel(false);
            this.np_age.setValue(this.ageIndex + 1);
            this.txt_age.setText(this.age_string);
            int i4 = this.age_value;
            if (i4 == 1) {
                String str = AppConstants.age_unit_year;
                this.age_unit = str;
                this.txt_age_unit.setText(str);
            } else if (i4 > 1) {
                String str2 = AppConstants.age_unit_years;
                this.age_unit = str2;
                this.txt_age_unit.setText(str2);
            }
            this.et_heart_rate.setText(this.heart_rate_string);
            EditText editText = this.et_heart_rate;
            editText.setSelection(editText.getText().toString().trim().length());
            if (this.gender.equalsIgnoreCase(AppConstants.hr_gender_male)) {
                this.spinner_gender.setSelection(0);
            } else if (this.gender.equalsIgnoreCase(AppConstants.hr_gender_female)) {
                this.spinner_gender.setSelection(1);
            } else if (this.gender.equalsIgnoreCase(AppConstants.hr_gender_child)) {
                this.spinner_gender.setSelection(2);
            }
            if (this.current_status.equalsIgnoreCase(AppConstants.hr_status_resting)) {
                this.spinner_current_status.setSelection(0);
            } else if (this.current_status.equalsIgnoreCase(AppConstants.hr_status_general)) {
                this.spinner_current_status.setSelection(1);
            } else if (this.current_status.equalsIgnoreCase(AppConstants.hr_status_after_exercise)) {
                this.spinner_current_status.setSelection(2);
            } else if (this.current_status.equalsIgnoreCase(AppConstants.hr_status_before_exercise)) {
                this.spinner_current_status.setSelection(3);
            } else if (this.current_status.equalsIgnoreCase(AppConstants.hr_status_tired)) {
                this.spinner_current_status.setSelection(4);
            } else if (this.current_status.equalsIgnoreCase(AppConstants.hr_status_unwell)) {
                this.spinner_current_status.setSelection(5);
            } else if (this.current_status.equalsIgnoreCase(AppConstants.hr_status_surprised)) {
                this.spinner_current_status.setSelection(6);
            } else if (this.current_status.equalsIgnoreCase(AppConstants.hr_status_sad)) {
                this.spinner_current_status.setSelection(7);
            } else if (this.current_status.equalsIgnoreCase(AppConstants.hr_status_angry)) {
                this.spinner_current_status.setSelection(8);
            } else if (this.current_status.equalsIgnoreCase(AppConstants.hr_status_fear_full)) {
                this.spinner_current_status.setSelection(9);
            } else if (this.current_status.equalsIgnoreCase(AppConstants.hr_status_in_love)) {
                this.spinner_current_status.setSelection(10);
            }
            this.txt_date.setText(this.date);
            this.txt_time.setText(this.time);
            this.et_notes.setText(this.notes);
            int i5 = AppConstants.selected_heart_rate_data.user_id;
            this.selected_user_id = i5;
            String GetProfileNameByID = this.SQLite_health_tracker.GetProfileNameByID(i5);
            this.selected_user_name = GetProfileNameByID;
            if (GetProfileNameByID != null) {
                int i6 = 0;
                for (int i7 = 0; i7 < this.array_profiles.size(); i7++) {
                    if (this.array_profiles.get(i7).user_name.trim().equals(this.selected_user_name)) {
                        i6 = i7;
                    }
                }
                this.spinner_profiles.setSelection(i6);
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
            this.age_string = this.txt_age.getText().toString().trim();
            this.age_value = this.np_age.getValue();
            String trim = this.et_heart_rate.getText().toString().trim();
            this.heart_rate_string = trim;
            this.heart_rate_value = Integer.parseInt(trim);
            this.gender = this.spinner_gender.getSelectedItem().toString();
            this.current_status = this.spinner_current_status.getSelectedItem().toString();
            this.notes = this.et_notes.getText().toString().trim();
            HeartRateData heartRateData = new HeartRateData();
            GetResult();
            int selectedItemPosition = this.spinner_profiles.getSelectedItemPosition();
            this.selected_user_id = this.arrayProfileIds[selectedItemPosition];
            this.selected_user_name = this.arrayProfileNames[selectedItemPosition];
            if (!AppConstants.is_heart_rate_edit_mode) {
                heartRateData.user_id = this.selected_user_id;
                heartRateData.date = this.date.trim();
                heartRateData.time = this.time.trim();
                heartRateData.age = this.age_value;
                heartRateData.heart_rate_value = this.heart_rate_value;
                heartRateData.gender = this.gender.trim();
                heartRateData.current_status = this.current_status.trim();
                heartRateData.color = this.color_string.trim();
                heartRateData.result = this.result_string;
                heartRateData.range = this.range_string;
                heartRateData.notes = this.notes.trim();
                GetDateTime(this.date.trim(), this.time.trim());
                heartRateData.dateTime = this.date_time;
                heartRateData.day = this.day;
                heartRateData.month = this.month;
                heartRateData.year = this.year;
                heartRateData.hour = this.hour;
                this.SQLite_health_tracker.InsertHeartRateData(heartRateData);
                EUGeneralClass.ShowSuccessToast(this, "Heart Rate Data saved successfully!");
                onBackPressed();
                return;
            }
            int i = AppConstants.selected_heart_rate_data.row_id;
            heartRateData.user_id = this.selected_user_id;
            heartRateData.date = this.date.trim();
            heartRateData.time = this.time.trim();
            heartRateData.heart_rate_value = this.heart_rate_value;
            heartRateData.age = this.age_value;
            heartRateData.gender = this.gender.trim();
            heartRateData.current_status = this.current_status.trim();
            heartRateData.color = this.color_string.trim();
            heartRateData.result = this.result_string;
            heartRateData.range = this.range_string;
            heartRateData.notes = this.notes.trim();
            GetDateTime(this.date.trim(), this.time.trim());
            heartRateData.dateTime = this.date_time;
            heartRateData.day = this.day;
            heartRateData.month = this.month;
            heartRateData.year = this.year;
            heartRateData.hour = this.hour;
            this.SQLite_health_tracker.UpdateHeartRateData(i, this.selected_user_id, heartRateData);
            EUGeneralClass.ShowSuccessToast(this, "Heart Rate Data updated successfully!");
            onBackPressed();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void GetResult() {
        if (this.gender.equalsIgnoreCase(AppConstants.hr_gender_child)) {
            int i = this.age_value;
            if (i >= 1 && i <= 3) {
                int i2 = this.heart_rate_value;
                if (i2 == 70 || i2 <= 110) {
                    this.color_string = "#F7931E";
                    this.result_string = AppConstants.hr_result_average;
                    this.range_string = "70-110";
                }
            } else if (i > 3 && i <= 6) {
                int i3 = this.heart_rate_value;
                if (i3 == 65 || i3 <= 110) {
                    this.color_string = "#F7931E";
                    this.result_string = AppConstants.hr_result_average;
                    this.range_string = "70-110";
                }
            } else if (i > 6 && i <= 12) {
                int i4 = this.heart_rate_value;
                if (i4 == 60 || i4 <= 95) {
                    this.color_string = "#F7931E";
                    this.result_string = AppConstants.hr_result_average;
                    this.range_string = "70-110";
                }
            } else if (i > 12 && i < 18) {
                int i5 = this.heart_rate_value;
                if (i5 == 55 || i5 <= 85) {
                    this.color_string = "#F7931E";
                    this.result_string = AppConstants.hr_result_average;
                    this.range_string = "70-110";
                }
            }
        } else if (this.gender.equalsIgnoreCase(AppConstants.hr_gender_male)) {
            int i6 = this.age_value;
            if (i6 == 18 || i6 <= 25) {
                int i7 = this.heart_rate_value;
                if (i7 == 49 || i7 <= 55) {
                    this.color_string = "#00A99D";
                    this.result_string = AppConstants.hr_result_athlete;
                    this.range_string = "70-73";
                } else if (i7 >= 56 && i7 <= 61) {
                    this.color_string = "#8CC63F";
                    this.result_string = AppConstants.hr_result_excellent;
                    this.range_string = "70-73";
                } else if (i7 >= 62 && i7 <= 65) {
                    this.color_string = "#D9E021";
                    this.result_string = AppConstants.hr_result_good;
                    this.range_string = "70-73";
                } else if (i7 >= 66 && i7 <= 69) {
                    this.color_string = "#FBB03B";
                    this.result_string = AppConstants.hr_result_above_average;
                    this.range_string = "70-73";
                } else if (i7 >= 70 && i7 <= 73) {
                    this.color_string = "#F7931E";
                    this.result_string = AppConstants.hr_result_average;
                    this.range_string = "70-73";
                } else if (i7 >= 74 && i7 <= 81) {
                    this.color_string = "#F15A24";
                    this.result_string = AppConstants.hr_result_below_average;
                    this.range_string = "70-73";
                } else if (i7 >= 82) {
                    this.color_string = "#ED1C24";
                    this.result_string = AppConstants.hr_result_poor;
                    this.range_string = "70-73";
                }
            } else if (i6 >= 26 && i6 <= 35) {
                int i8 = this.heart_rate_value;
                if (i8 == 49 || i8 <= 54) {
                    this.color_string = "#00A99D";
                    this.result_string = AppConstants.hr_result_athlete;
                    this.range_string = "71-74";
                } else if (i8 >= 55 && i8 <= 61) {
                    this.color_string = "#8CC63F";
                    this.result_string = AppConstants.hr_result_excellent;
                    this.range_string = "71-74";
                } else if (i8 >= 62 && i8 <= 65) {
                    this.color_string = "#D9E021";
                    this.result_string = AppConstants.hr_result_good;
                    this.range_string = "71-74";
                } else if (i8 >= 66 && i8 <= 70) {
                    this.color_string = "#FBB03B";
                    this.result_string = AppConstants.hr_result_above_average;
                    this.range_string = "71-74";
                } else if (i8 >= 71 && i8 <= 74) {
                    this.color_string = "#F7931E";
                    this.result_string = AppConstants.hr_result_average;
                    this.range_string = "71-74";
                } else if (i8 >= 75 && i8 <= 81) {
                    this.color_string = "#F15A24";
                    this.result_string = AppConstants.hr_result_below_average;
                    this.range_string = "71-74";
                } else if (i8 >= 82) {
                    this.color_string = "#ED1C24";
                    this.result_string = AppConstants.hr_result_poor;
                    this.range_string = "71-74";
                }
            } else if (i6 >= 36 && i6 <= 45) {
                int i9 = this.heart_rate_value;
                if (i9 == 50 || i9 <= 56) {
                    this.color_string = "#00A99D";
                    this.result_string = AppConstants.hr_result_athlete;
                    this.range_string = "71-75";
                } else if (i9 >= 57 && i9 <= 62) {
                    this.color_string = "#8CC63F";
                    this.result_string = AppConstants.hr_result_excellent;
                    this.range_string = "71-75";
                } else if (i9 >= 63 && i9 <= 66) {
                    this.color_string = "#D9E021";
                    this.result_string = AppConstants.hr_result_good;
                    this.range_string = "71-75";
                } else if (i9 >= 67 && i9 <= 70) {
                    this.color_string = "#FBB03B";
                    this.result_string = AppConstants.hr_result_above_average;
                    this.range_string = "71-75";
                } else if (i9 >= 71 && i9 <= 75) {
                    this.color_string = "#F7931E";
                    this.result_string = AppConstants.hr_result_average;
                    this.range_string = "71-75";
                } else if (i9 >= 76 && i9 <= 82) {
                    this.color_string = "#F15A24";
                    this.result_string = AppConstants.hr_result_below_average;
                    this.range_string = "71-75";
                } else if (i9 >= 83) {
                    this.color_string = "#ED1C24";
                    this.result_string = AppConstants.hr_result_poor;
                    this.range_string = "71-75";
                }
            } else if (i6 >= 46 && i6 <= 55) {
                int i10 = this.heart_rate_value;
                if (i10 == 50 || i10 <= 57) {
                    this.color_string = "#00A99D";
                    this.result_string = AppConstants.hr_result_athlete;
                    this.range_string = "72-76";
                } else if (i10 >= 58 && i10 <= 63) {
                    this.color_string = "#8CC63F";
                    this.result_string = AppConstants.hr_result_excellent;
                    this.range_string = "72-76";
                } else if (i10 >= 64 && i10 <= 67) {
                    this.color_string = "#D9E021";
                    this.result_string = AppConstants.hr_result_good;
                    this.range_string = "72-76";
                } else if (i10 >= 68 && i10 <= 71) {
                    this.color_string = "#FBB03B";
                    this.result_string = AppConstants.hr_result_above_average;
                    this.range_string = "72-76";
                } else if (i10 >= 72 && i10 <= 76) {
                    this.color_string = "#F7931E";
                    this.result_string = AppConstants.hr_result_average;
                    this.range_string = "72-76";
                } else if (i10 >= 77 && i10 <= 83) {
                    this.color_string = "#F15A24";
                    this.result_string = AppConstants.hr_result_below_average;
                    this.range_string = "72-76";
                } else if (i10 >= 84) {
                    this.color_string = "#ED1C24";
                    this.result_string = AppConstants.hr_result_poor;
                    this.range_string = "72-76";
                }
            } else if (i6 >= 56 && i6 <= 65) {
                int i11 = this.heart_rate_value;
                if (i11 == 51 || i11 <= 56) {
                    this.color_string = "#00A99D";
                    this.result_string = AppConstants.hr_result_athlete;
                    this.range_string = "72-75";
                } else if (i11 >= 57 && i11 <= 61) {
                    this.color_string = "#8CC63F";
                    this.result_string = AppConstants.hr_result_excellent;
                    this.range_string = "72-75";
                } else if (i11 >= 62 && i11 <= 67) {
                    this.color_string = "#D9E021";
                    this.result_string = AppConstants.hr_result_good;
                    this.range_string = "72-75";
                } else if (i11 >= 68 && i11 <= 71) {
                    this.color_string = "#FBB03B";
                    this.result_string = AppConstants.hr_result_above_average;
                    this.range_string = "72-75";
                } else if (i11 >= 72 && i11 <= 75) {
                    this.color_string = "#F7931E";
                    this.result_string = AppConstants.hr_result_average;
                    this.range_string = "72-75";
                } else if (i11 >= 76 && i11 <= 81) {
                    this.color_string = "#F15A24";
                    this.result_string = AppConstants.hr_result_below_average;
                    this.range_string = "72-75";
                } else if (i11 >= 82) {
                    this.color_string = "#ED1C24";
                    this.result_string = AppConstants.hr_result_poor;
                    this.range_string = "72-75";
                }
            } else if (i6 > 65) {
                int i12 = this.heart_rate_value;
                if (i12 == 50 || i12 <= 55) {
                    this.color_string = "#00A99D";
                    this.result_string = AppConstants.hr_result_athlete;
                    this.range_string = "70-73";
                } else if (i12 >= 56 && i12 <= 61) {
                    this.color_string = "#8CC63F";
                    this.result_string = AppConstants.hr_result_excellent;
                    this.range_string = "70-73";
                } else if (i12 >= 62 && i12 <= 65) {
                    this.color_string = "#D9E021";
                    this.result_string = AppConstants.hr_result_good;
                    this.range_string = "70-73";
                } else if (i12 >= 66 && i12 <= 69) {
                    this.color_string = "#FBB03B";
                    this.result_string = AppConstants.hr_result_above_average;
                    this.range_string = "70-73";
                } else if (i12 >= 70 && i12 <= 73) {
                    this.color_string = "#F7931E";
                    this.result_string = AppConstants.hr_result_average;
                    this.range_string = "70-73";
                } else if (i12 >= 74 && i12 <= 79) {
                    this.color_string = "#F15A24";
                    this.result_string = AppConstants.hr_result_below_average;
                    this.range_string = "70-73";
                } else if (i12 >= 80) {
                    this.color_string = "#ED1C24";
                    this.result_string = AppConstants.hr_result_poor;
                    this.range_string = "70-73";
                }
            }
        } else if (this.gender.equalsIgnoreCase(AppConstants.hr_gender_female)) {
            int i13 = this.age_value;
            if (i13 == 18 || i13 <= 25) {
                int i14 = this.heart_rate_value;
                if (i14 == 54 || i14 <= 60) {
                    this.color_string = "#00A99D";
                    this.result_string = AppConstants.hr_result_athlete;
                    this.range_string = "74-78";
                } else if (i14 >= 61 && i14 <= 65) {
                    this.color_string = "#8CC63F";
                    this.result_string = AppConstants.hr_result_excellent;
                    this.range_string = "74-78";
                } else if (i14 >= 66 && i14 <= 69) {
                    this.color_string = "#D9E021";
                    this.result_string = AppConstants.hr_result_good;
                    this.range_string = "74-78";
                } else if (i14 >= 70 && i14 <= 73) {
                    this.color_string = "#FBB03B";
                    this.result_string = AppConstants.hr_result_above_average;
                    this.range_string = "74-78";
                } else if (i14 >= 74 && i14 <= 78) {
                    this.color_string = "#F7931E";
                    this.result_string = AppConstants.hr_result_average;
                    this.range_string = "74-78";
                } else if (i14 >= 79 && i14 <= 84) {
                    this.color_string = "#F15A24";
                    this.result_string = AppConstants.hr_result_below_average;
                    this.range_string = "74-78";
                } else if (i14 >= 85) {
                    this.color_string = "#ED1C24";
                    this.result_string = AppConstants.hr_result_poor;
                    this.range_string = "74-78";
                }
            } else if (i13 >= 26 && i13 <= 35) {
                int i15 = this.heart_rate_value;
                if (i15 == 54 || i15 <= 59) {
                    this.color_string = "#00A99D";
                    this.result_string = AppConstants.hr_result_athlete;
                    this.range_string = "73-76";
                } else if (i15 >= 60 && i15 <= 64) {
                    this.color_string = "#8CC63F";
                    this.result_string = AppConstants.hr_result_excellent;
                    this.range_string = "73-76";
                } else if (i15 >= 65 && i15 <= 68) {
                    this.color_string = "#D9E021";
                    this.result_string = AppConstants.hr_result_good;
                    this.range_string = "73-76";
                } else if (i15 >= 69 && i15 <= 72) {
                    this.color_string = "#FBB03B";
                    this.result_string = AppConstants.hr_result_above_average;
                    this.range_string = "73-76";
                } else if (i15 >= 73 && i15 <= 76) {
                    this.color_string = "#F7931E";
                    this.result_string = AppConstants.hr_result_average;
                    this.range_string = "73-76";
                } else if (i15 >= 77 && i15 <= 82) {
                    this.color_string = "#F15A24";
                    this.result_string = AppConstants.hr_result_below_average;
                    this.range_string = "73-76";
                } else if (i15 >= 83) {
                    this.color_string = "#ED1C24";
                    this.result_string = AppConstants.hr_result_poor;
                    this.range_string = "73-76";
                }
            } else if (i13 >= 36 && i13 <= 45) {
                int i16 = this.heart_rate_value;
                if (i16 == 54 || i16 <= 59) {
                    this.color_string = "#00A99D";
                    this.result_string = AppConstants.hr_result_athlete;
                    this.range_string = "74-78";
                } else if (i16 >= 60 && i16 <= 64) {
                    this.color_string = "#8CC63F";
                    this.result_string = AppConstants.hr_result_excellent;
                    this.range_string = "74-78";
                } else if (i16 >= 65 && i16 <= 69) {
                    this.color_string = "#D9E021";
                    this.result_string = AppConstants.hr_result_good;
                    this.range_string = "74-78";
                } else if (i16 >= 70 && i16 <= 73) {
                    this.color_string = "#FBB03B";
                    this.result_string = AppConstants.hr_result_above_average;
                    this.range_string = "74-78";
                } else if (i16 >= 74 && i16 <= 78) {
                    this.color_string = "#F7931E";
                    this.result_string = AppConstants.hr_result_average;
                    this.range_string = "74-78";
                } else if (i16 >= 79 && i16 <= 84) {
                    this.color_string = "#F15A24";
                    this.result_string = AppConstants.hr_result_below_average;
                    this.range_string = "74-78";
                } else if (i16 >= 85) {
                    this.color_string = "#ED1C24";
                    this.result_string = AppConstants.hr_result_poor;
                    this.range_string = "74-78";
                }
            } else if (i13 >= 46 && i13 <= 55) {
                int i17 = this.heart_rate_value;
                if (i17 == 54 || i17 <= 60) {
                    this.color_string = "#00A99D";
                    this.result_string = AppConstants.hr_result_athlete;
                    this.range_string = "74-77";
                } else if (i17 >= 61 && i17 <= 65) {
                    this.color_string = "#8CC63F";
                    this.result_string = AppConstants.hr_result_excellent;
                    this.range_string = "74-77";
                } else if (i17 >= 66 && i17 <= 69) {
                    this.color_string = "#D9E021";
                    this.result_string = AppConstants.hr_result_good;
                    this.range_string = "74-77";
                } else if (i17 >= 70 && i17 <= 73) {
                    this.color_string = "#FBB03B";
                    this.result_string = AppConstants.hr_result_above_average;
                    this.range_string = "74-77";
                } else if (i17 >= 74 && i17 <= 77) {
                    this.color_string = "#F7931E";
                    this.result_string = AppConstants.hr_result_average;
                    this.range_string = "74-77";
                } else if (i17 >= 78 && i17 <= 83) {
                    this.color_string = "#F15A24";
                    this.result_string = AppConstants.hr_result_below_average;
                    this.range_string = "74-77";
                } else if (i17 >= 84) {
                    this.color_string = "#ED1C24";
                    this.result_string = AppConstants.hr_result_poor;
                    this.range_string = "74-77";
                }
            } else if (i13 >= 56 && i13 <= 65) {
                int i18 = this.heart_rate_value;
                if (i18 == 54 || i18 <= 59) {
                    this.color_string = "#00A99D";
                    this.result_string = AppConstants.hr_result_athlete;
                    this.range_string = "74-77";
                } else if (i18 >= 60 && i18 <= 64) {
                    this.color_string = "#8CC63F";
                    this.result_string = AppConstants.hr_result_excellent;
                    this.range_string = "74-77";
                } else if (i18 >= 65 && i18 <= 68) {
                    this.color_string = "#D9E021";
                    this.result_string = AppConstants.hr_result_good;
                    this.range_string = "74-77";
                } else if (i18 >= 69 && i18 <= 73) {
                    this.color_string = "#FBB03B";
                    this.result_string = AppConstants.hr_result_above_average;
                    this.range_string = "74-77";
                } else if (i18 >= 74 && i18 <= 77) {
                    this.color_string = "#F7931E";
                    this.result_string = AppConstants.hr_result_average;
                    this.range_string = "74-77";
                } else if (i18 >= 78 && i18 <= 83) {
                    this.color_string = "#F15A24";
                    this.result_string = AppConstants.hr_result_below_average;
                    this.range_string = "74-77";
                } else if (i18 >= 84) {
                    this.color_string = "#ED1C24";
                    this.result_string = AppConstants.hr_result_poor;
                    this.range_string = "74-77";
                }
            } else if (i13 > 65) {
                int i19 = this.heart_rate_value;
                if (i19 == 54 || i19 <= 59) {
                    this.color_string = "#00A99D";
                    this.result_string = AppConstants.hr_result_athlete;
                    this.range_string = "73-76";
                } else if (i19 >= 60 && i19 <= 64) {
                    this.color_string = "#8CC63F";
                    this.result_string = AppConstants.hr_result_excellent;
                    this.range_string = "73-76";
                } else if (i19 >= 65 && i19 <= 68) {
                    this.color_string = "#D9E021";
                    this.result_string = AppConstants.hr_result_good;
                    this.range_string = "73-76";
                } else if (i19 >= 69 && i19 <= 72) {
                    this.color_string = "#FBB03B";
                    this.result_string = AppConstants.hr_result_above_average;
                    this.range_string = "73-76";
                } else if (i19 >= 73 && i19 <= 76) {
                    this.color_string = "#F7931E";
                    this.result_string = AppConstants.hr_result_average;
                    this.range_string = "73-76";
                } else if (i19 >= 77 && i19 <= 84) {
                    this.color_string = "#F15A24";
                    this.result_string = AppConstants.hr_result_below_average;
                    this.range_string = "73-76";
                } else if (i19 >= 85) {
                    this.color_string = "#ED1C24";
                    this.result_string = AppConstants.hr_result_poor;
                    this.range_string = "73-76";
                }
            }
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
        ((TextView) findViewById(R.id.toolbar_txt_title_2)).setText(getResources().getString(R.string.lbl_header_heart_rate));
        findViewById(R.id.toolbar_rel_save).setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                AddHeartRateActivity.this.SaveProcess();
            }
        });
        findViewById(R.id.toolbar_rel_back).setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                AddHeartRateActivity.this.onBackPressed();
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
