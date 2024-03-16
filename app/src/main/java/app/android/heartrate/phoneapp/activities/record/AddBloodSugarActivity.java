package app.android.heartrate.phoneapp.activities.record;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
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
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.utils.Utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import app.android.heartrate.phoneapp.AdAdmob;
import app.android.heartrate.phoneapp.R;
import app.android.heartrate.phoneapp.adapters.SpinnerProfileAdapter;
import app.android.heartrate.phoneapp.model.classes.BloodSugarData;
import app.android.heartrate.phoneapp.model.classes.UserProfileData;
import app.android.heartrate.phoneapp.sqlite.SQLiteHealthTracker;
import app.android.heartrate.phoneapp.sqlite.rulerpicker.RulerValuePicker;
import app.android.heartrate.phoneapp.sqlite.rulerpicker.RulerValuePickerListener;
import app.android.heartrate.phoneapp.utils.AppConstants;
import app.android.heartrate.phoneapp.utils.EUGeneralClass;

public class AddBloodSugarActivity extends AppCompatActivity {
    public static Activity activity_add_blood_sugar;
    SQLiteHealthTracker SQLite_health_tracker;
    String adagValue = "";
    ArrayList<String> arrayListCurrentStatus = new ArrayList<>(Arrays.asList(AppConstants.pre_meal_testing, AppConstants.post_meal_testing, AppConstants.fasting_testing, AppConstants.general_testing));
    int[] arrayProfileIds;
    String[] arrayProfileNames;
    ArrayList<UserProfileData> array_profiles = new ArrayList<>();

    String blood_sugar_result;
    float blood_sugar_value = 0.0f;
    String current_status;
    ArrayAdapter<String> current_status_spinner_adapter;
    String date = "";
    String date_time = "";
    int day;
    String dcctValue = "";
    EditText et_hemoglobin_level;
    EditText et_ketone_level;
    EditText et_notes;
    double hemoLevel = Utils.DOUBLE_EPSILON;
    String hemoglobinLevel = "";
    int hemoglobinLevelLength = 0;
    int hour;
    String ketoneLevel = "";
    Context mContext;
    int month;
    String notes = "";
    Animation push_animation;
    RelativeLayout rel_select_date;
    RelativeLayout rel_select_time;
    RulerValuePicker ruler_sugar_level;
    int selected_user_id;
    String selected_user_name = "";
    Spinner spinner_current_status;
    SpinnerProfileAdapter spinner_profile_adapter;
    Spinner spinner_profiles;
    String status_color = "#8CC63F";
    String time = "";
    String toastMessage = "";
    TextView txt_adag_level;
    TextView txt_bs_level;
    TextView txt_date;
    TextView txt_dcct_level;
    TextView txt_time;
    int year;
    private String current_date_time = "";
    private int save_entry_day;
    private int save_entry_hour;
    private int save_entry_minute;
    private int save_entry_month;
    private int save_entry_year;

    public double CalculateADAG(double d) {
        return (d * 28.7d) - 46.7d;
    }

    public double CalculateDCCT(double d) {
        return (d * 35.6d) - 77.3d;
    }


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        SetView();


        AppConstants.overridePendingTransitionEnter(this);
    }

    private void SetView() {
        setContentView(R.layout.activity_add_blood_sugar);

        this.mContext = this;
        activity_add_blood_sugar = this;
        this.push_animation = AnimationUtils.loadAnimation(this, R.anim.view_push);
        setUpActionBar();
        SQLiteHealthTracker sQLiteHealthTracker = new SQLiteHealthTracker(this);
        this.SQLite_health_tracker = sQLiteHealthTracker;
        sQLiteHealthTracker.openToWrite();
        this.spinner_profiles = findViewById(R.id.add_bs_spinner_profiles);
        this.txt_bs_level = findViewById(R.id.add_bs_txt_bs_level);
        RulerValuePicker rulerValuePicker = findViewById(R.id.add_bs_ruler_sugar_level);
        this.ruler_sugar_level = rulerValuePicker;
        rulerValuePicker.selectValue(AppConstants.default_sugar_level_value);
        this.ruler_sugar_level.setIndicatorHeight(AppConstants.ruler_long_height_ration, AppConstants.ruler_short_height_ration);
        this.txt_bs_level.setText(String.valueOf(AppConstants.default_sugar_level_value));
        this.spinner_current_status = findViewById(R.id.add_bs_spinner_current_status);
        this.et_ketone_level = findViewById(R.id.add_bs_et_ketone_level);
        this.et_hemoglobin_level = findViewById(R.id.add_bs_et_hemoglobin_level);
        this.txt_adag_level = findViewById(R.id.add_bs_txt_adag_value);
        this.txt_dcct_level = findViewById(R.id.add_bs_txt_dcct_value);
        this.et_notes = findViewById(R.id.add_bs_et_notes);
        this.rel_select_date = findViewById(R.id.add_bs_rel_select_date);
        this.txt_date = findViewById(R.id.add_bs_txt_date);
        this.rel_select_time = findViewById(R.id.add_bs_rel_select_time);
        this.txt_time = findViewById(R.id.add_bs_txt_time);
        SetProfileSpinner();
        SetCurrentStatusSpinner();
        SetCurrentDateTime();
        if (AppConstants.is_bs_edit_mode) {
            SetBloodSugarData();
        }
        this.rel_select_date.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                AddBloodSugarActivity.this.hideSoftKeyboard();
                Calendar instance = Calendar.getInstance();
                AddBloodSugarActivity.this.save_entry_year = instance.get(1);
                AddBloodSugarActivity.this.save_entry_month = instance.get(2);
                AddBloodSugarActivity.this.save_entry_day = instance.get(5);
                new DatePickerDialog(AddBloodSugarActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {


                    public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                        try {
                            AddBloodSugarActivity.this.txt_date.setText(new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("dd/MM/yyyy").parse((i3 + "/" + (i2 + 1) + "/" + i).trim())));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, AddBloodSugarActivity.this.save_entry_year, AddBloodSugarActivity.this.save_entry_month, AddBloodSugarActivity.this.save_entry_day).show();
            }
        });
        this.rel_select_time.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                Calendar instance = Calendar.getInstance();
                AddBloodSugarActivity.this.save_entry_hour = instance.get(11);
                AddBloodSugarActivity.this.save_entry_minute = instance.get(12);
                new TimePickerDialog(AddBloodSugarActivity.this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {


                    public void onTimeSet(TimePicker timePicker, int i, int i2) {
                        try {
                            AddBloodSugarActivity.this.txt_time.setText(new SimpleDateFormat("hh:mm aa").format(new SimpleDateFormat("hh:mm").parse(i + ":" + i2)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, AddBloodSugarActivity.this.save_entry_hour, AddBloodSugarActivity.this.save_entry_minute, false).show();
            }
        });
        this.et_hemoglobin_level.addTextChangedListener(new TextWatcher() {


            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (charSequence.toString().length() > 0) {
                    DecimalFormat decimalFormat = new DecimalFormat("##");
                    AddBloodSugarActivity.this.txt_adag_level.setText(decimalFormat.format(AddBloodSugarActivity.this.CalculateADAG(Double.parseDouble(charSequence.toString()))));
                    AddBloodSugarActivity.this.txt_dcct_level.setText(decimalFormat.format(AddBloodSugarActivity.this.CalculateDCCT(Double.parseDouble(charSequence.toString()))));
                } else if (charSequence.toString().length() == 0) {
                    AddBloodSugarActivity.this.txt_adag_level.setText("");
                    AddBloodSugarActivity.this.txt_dcct_level.setText("");
                }
            }
        });
        this.ruler_sugar_level.setValuePickerListener(new RulerValuePickerListener() {


            @Override
            public void onIntermediateValueChange(int i) {
            }

            @Override
            public void onValueChange(int i) {
                AddBloodSugarActivity.this.txt_bs_level.setText(String.valueOf(i));
            }
        });
    }

    private void SetCurrentStatusSpinner() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_list, this.arrayListCurrentStatus);
        this.current_status_spinner_adapter = arrayAdapter;
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list);
        this.spinner_current_status.setAdapter(this.current_status_spinner_adapter);
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
                int i2 = AddBloodSugarActivity.this.arrayProfileIds[i];
                String trim = AddBloodSugarActivity.this.arrayProfileNames[i].trim();
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

    private void SetBloodSugarData() {
        if (AppConstants.selected_bs_data != null) {
            this.date = AppConstants.selected_bs_data.date.trim();
            this.time = AppConstants.selected_bs_data.time.trim();
            this.current_status = AppConstants.selected_bs_data.current_status.trim();
            this.blood_sugar_value = AppConstants.selected_bs_data.sugar_level;
            this.ketoneLevel = String.valueOf(AppConstants.selected_bs_data.keton_level);
            this.hemoglobinLevel = String.valueOf(AppConstants.selected_bs_data.hemoglobin_level);
            this.adagValue = String.valueOf(AppConstants.selected_bs_data.blood_ADAG);
            this.dcctValue = String.valueOf(AppConstants.selected_bs_data.blood_DCCT);
            this.status_color = AppConstants.selected_bs_data.status_color.trim();
            this.blood_sugar_result = AppConstants.selected_bs_data.result.trim();
            this.notes = AppConstants.selected_bs_data.notes.trim();
            this.ruler_sugar_level.selectValue((int) this.blood_sugar_value);
            this.ruler_sugar_level.setIndicatorHeight(AppConstants.ruler_long_height_ration, AppConstants.ruler_short_height_ration);
            this.txt_bs_level.setText(String.valueOf(this.blood_sugar_value));
            if (this.current_status.equals(AppConstants.pre_meal_testing)) {
                this.spinner_current_status.setSelection(0);
            } else if (this.current_status.equals(AppConstants.post_meal_testing)) {
                this.spinner_current_status.setSelection(1);
            } else if (this.current_status.equals(AppConstants.fasting_testing)) {
                this.spinner_current_status.setSelection(2);
            } else if (this.current_status.equals(AppConstants.general_testing)) {
                this.spinner_current_status.setSelection(3);
            }
            this.txt_date.setText(this.date);
            this.txt_time.setText(this.time);
            this.et_ketone_level.setText(this.ketoneLevel);
            this.et_hemoglobin_level.setText(this.hemoglobinLevel);
            this.txt_adag_level.setText(this.adagValue);
            this.txt_dcct_level.setText(this.dcctValue);
            this.et_notes.setText(this.notes);
            int i = AppConstants.selected_bs_data.user_id;
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
        boolean z = false;
        EditText editText = null;
        try {
            this.date = this.txt_date.getText().toString().trim();
            this.time = this.txt_time.getText().toString().trim();
            this.ketoneLevel = this.et_ketone_level.getText().toString().trim();
            this.hemoglobinLevel = this.et_hemoglobin_level.getText().toString().trim();
            this.adagValue = this.txt_adag_level.getText().toString().trim();
            this.dcctValue = this.txt_dcct_level.getText().toString().trim();
            this.notes = this.et_notes.getText().toString().trim();
            this.hemoglobinLevelLength = this.hemoglobinLevel.length();
            if (TextUtils.isEmpty(this.ketoneLevel)) {
                this.et_ketone_level.setError(AppConstants.error_field_require);
                editText = this.et_ketone_level;
                z = true;
            }
            if (z) {
                editText.requestFocus();
                return;
            }
            BloodSugarData bloodSugarData = new BloodSugarData();
            this.blood_sugar_value = (float) this.ruler_sugar_level.getCurrentValue();
            this.current_status = this.spinner_current_status.getSelectedItem().toString();
            if (this.hemoglobinLevel.length() == 0) {
                this.hemoLevel = Utils.DOUBLE_EPSILON;
            } else {
                this.hemoLevel = Double.parseDouble(this.hemoglobinLevel);
            }
            GetResult();
            String str = this.blood_sugar_result;
            if (str != null) {
                if (str.length() != 0) {
                    int selectedItemPosition = this.spinner_profiles.getSelectedItemPosition();
                    this.selected_user_id = this.arrayProfileIds[selectedItemPosition];
                    this.selected_user_name = this.arrayProfileNames[selectedItemPosition];
                    if (!AppConstants.is_bs_edit_mode) {
                        bloodSugarData.user_id = this.selected_user_id;
                        bloodSugarData.date = this.date.trim();
                        bloodSugarData.time = this.time.trim();
                        bloodSugarData.current_status = this.current_status.trim();
                        bloodSugarData.sugar_level = this.blood_sugar_value;
                        bloodSugarData.keton_level = Float.parseFloat(this.ketoneLevel.trim());
                        bloodSugarData.hemoglobin_level = Float.parseFloat(this.hemoglobinLevel.trim());
                        bloodSugarData.blood_ADAG = Float.parseFloat(this.adagValue.trim());
                        bloodSugarData.blood_DCCT = Float.parseFloat(this.dcctValue.trim());
                        bloodSugarData.status_color = this.status_color.trim();
                        bloodSugarData.result = this.blood_sugar_result.trim();
                        bloodSugarData.notes = this.notes.trim();
                        GetDateTime(this.date.trim(), this.time.trim());
                        bloodSugarData.dateTime = this.date_time;
                        bloodSugarData.day = this.day;
                        bloodSugarData.month = this.month;
                        bloodSugarData.year = this.year;
                        bloodSugarData.hour = this.hour;
                        if ("".equalsIgnoreCase(AppConstants.values_not_natural)) {
                            EUGeneralClass.ShowErrorToast(this, "");
                            return;
                        }
                        this.SQLite_health_tracker.InsertBloodSugarData(bloodSugarData);
                        EUGeneralClass.ShowSuccessToast(this, "Blood Sugar Data saved successfully!");
                        onBackPressed();
                        return;
                    }


                    int i = AppConstants.selected_bs_data.row_id;
                    bloodSugarData.user_id = this.selected_user_id;
                    bloodSugarData.date = this.date.trim();
                    bloodSugarData.time = this.time.trim();
                    bloodSugarData.current_status = this.current_status.trim();
                    bloodSugarData.sugar_level = this.blood_sugar_value;
                    bloodSugarData.keton_level = Float.parseFloat(this.ketoneLevel.trim());
                    bloodSugarData.hemoglobin_level = Float.parseFloat(this.hemoglobinLevel.trim());
                    bloodSugarData.blood_ADAG = Float.parseFloat(this.adagValue.trim());
                    bloodSugarData.blood_DCCT = Float.parseFloat(this.dcctValue.trim());
                    bloodSugarData.status_color = this.status_color.trim();
                    bloodSugarData.result = this.blood_sugar_result.trim();
                    bloodSugarData.notes = this.notes.trim();
                    GetDateTime(this.date.trim(), this.time.trim());
                    bloodSugarData.dateTime = this.date_time;
                    bloodSugarData.day = this.day;
                    bloodSugarData.month = this.month;
                    bloodSugarData.year = this.year;
                    bloodSugarData.hour = this.hour;
                    if ("".equalsIgnoreCase(AppConstants.values_not_natural)) {
                        EUGeneralClass.ShowErrorToast(this, "");
                        return;
                    }
                    this.SQLite_health_tracker.UpdateBloodSugarData(i, this.selected_user_id, bloodSugarData);
                    EUGeneralClass.ShowSuccessToast(this, "Blood Sugar Data updated successfully!");
                    onBackPressed();
                    return;
                }
            }
            EUGeneralClass.ShowErrorToast(this, "Values are not natural! Please enter natural value as par condition!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void GetResult() {
        if (this.hemoglobinLevel.equalsIgnoreCase("") || this.hemoglobinLevel.equalsIgnoreCase(null) || this.hemoglobinLevel.isEmpty()) {
            if (this.current_status.equals(AppConstants.pre_meal_testing)) {
                float f = this.blood_sugar_value;
                if (f < 70.0f) {
                    this.blood_sugar_result = AppConstants.sugar_level_low;
                    this.status_color = "#00A99D";
                } else if (f >= 70.0f && f <= 100.0f) {
                    this.blood_sugar_result = AppConstants.sugar_level_normal;
                    this.status_color = "#8CC63F";
                } else if (f >= 101.0f && f < 126.0f) {
                    this.blood_sugar_result = AppConstants.sugar_level_pre_diabetes;
                    this.status_color = "#FBB03B";
                } else if (f >= 126.0f) {
                    this.blood_sugar_result = AppConstants.sugar_level_diabetes;
                    this.status_color = "#F15A24";
                } else {
                    this.toastMessage = AppConstants.values_not_natural;
                }
            } else if (this.current_status.equals(AppConstants.post_meal_testing)) {
                float f2 = this.blood_sugar_value;
                if (f2 < 70.0f) {
                    this.blood_sugar_result = AppConstants.sugar_level_low;
                    this.status_color = "#00A99D";
                } else if (f2 >= 70.0f && f2 <= 140.0f) {
                    this.blood_sugar_result = AppConstants.sugar_level_normal;
                    this.status_color = "#8CC63F";
                } else if (f2 >= 141.0f && f2 < 200.0f) {
                    this.blood_sugar_result = AppConstants.sugar_level_pre_diabetes;
                    this.status_color = "#FBB03B";
                } else if (f2 >= 200.0f) {
                    this.blood_sugar_result = AppConstants.sugar_level_diabetes;
                    this.status_color = "#F15A24";
                } else {
                    this.toastMessage = AppConstants.values_not_natural;
                }
            } else if (this.current_status.equals(AppConstants.fasting_testing)) {
                float f3 = this.blood_sugar_value;
                if (f3 < 70.0f) {
                    this.blood_sugar_result = AppConstants.sugar_level_low;
                    this.status_color = "#00A99D";
                } else if (f3 >= 70.0f && f3 <= 99.0f) {
                    this.blood_sugar_result = AppConstants.sugar_level_normal;
                    this.status_color = "#8CC63F";
                } else if (f3 >= 100.0f && f3 <= 125.0f) {
                    this.blood_sugar_result = AppConstants.sugar_level_pre_diabetes;
                    this.status_color = "#FBB03B";
                } else if (f3 > 125.0f) {
                    this.blood_sugar_result = AppConstants.sugar_level_diabetes;
                    this.status_color = "#F15A24";
                } else {
                    this.toastMessage = AppConstants.values_not_natural;
                }
            } else if (this.current_status.equals(AppConstants.general_testing)) {
                float f4 = this.blood_sugar_value;
                if (f4 < 70.0f) {
                    this.blood_sugar_result = AppConstants.sugar_level_low;
                    this.status_color = "#00A99D";
                } else if (f4 >= 70.0f && f4 <= 140.0f) {
                    this.blood_sugar_result = AppConstants.sugar_level_normal;
                    this.status_color = "#8CC63F";
                } else if (f4 >= 141.0f && f4 < 200.0f) {
                    this.blood_sugar_result = AppConstants.sugar_level_pre_diabetes;
                    this.status_color = "#FBB03B";
                } else if (f4 >= 200.0f) {
                    this.blood_sugar_result = AppConstants.sugar_level_diabetes;
                    this.status_color = "#F15A24";
                } else {
                    this.toastMessage = AppConstants.values_not_natural;
                }
            }
        } else if (this.hemoglobinLevel.equalsIgnoreCase("") && this.hemoglobinLevel.equalsIgnoreCase(null) && this.hemoglobinLevel.isEmpty()) {
        } else {
            if (this.current_status.equals(AppConstants.pre_meal_testing)) {
                float f5 = this.blood_sugar_value;
                if (f5 < 70.0f) {
                    double d = this.hemoLevel;
                    if (d < 4.0d || (d >= 4.0d && d <= 5.1d)) {
                        this.blood_sugar_result = AppConstants.sugar_level_low;
                        this.status_color = "#00A99D";
                        return;
                    }
                }
                if (f5 >= 70.0f && f5 <= 100.0f) {
                    double d2 = this.hemoLevel;
                    if (d2 >= 5.2d && d2 <= 5.6d) {
                        this.blood_sugar_result = AppConstants.sugar_level_normal;
                        this.status_color = "#8CC63F";
                        return;
                    }
                }
                if (f5 >= 101.0f && f5 < 126.0f) {
                    double d3 = this.hemoLevel;
                    if (d3 >= 5.7d && d3 <= 6.4d) {
                        this.blood_sugar_result = AppConstants.sugar_level_pre_diabetes;
                        this.status_color = "#FBB03B";
                        return;
                    }
                }
                if (f5 >= 126.0f) {
                    double d4 = this.hemoLevel;
                    if ((d4 >= 6.5d && d4 <= 15.0d) || d4 > 15.0d) {
                        this.blood_sugar_result = AppConstants.sugar_level_diabetes;
                        this.status_color = "#F15A24";
                        return;
                    }
                }
                this.toastMessage = AppConstants.values_not_natural;
            } else if (this.current_status.equals(AppConstants.post_meal_testing)) {
                float f6 = this.blood_sugar_value;
                if (f6 < 70.0f) {
                    double d5 = this.hemoLevel;
                    if (d5 < 4.0d || (d5 >= 4.0d && d5 <= 5.1d)) {
                        this.blood_sugar_result = AppConstants.sugar_level_low;
                        this.status_color = "#00A99D";
                        return;
                    }
                }
                if (f6 >= 70.0f && f6 <= 140.0f) {
                    double d6 = this.hemoLevel;
                    if (d6 >= 5.2d && d6 <= 5.6d) {
                        this.blood_sugar_result = AppConstants.sugar_level_normal;
                        this.status_color = "#8CC63F";
                        return;
                    }
                }
                if (f6 >= 141.0f && f6 < 200.0f) {
                    double d7 = this.hemoLevel;
                    if (d7 >= 5.7d && d7 <= 6.4d) {
                        this.blood_sugar_result = AppConstants.sugar_level_pre_diabetes;
                        this.status_color = "#FBB03B";
                        return;
                    }
                }
                if (f6 >= 200.0f) {
                    double d8 = this.hemoLevel;
                    if ((d8 >= 6.5d && d8 <= 15.0d) || d8 > 15.0d) {
                        this.blood_sugar_result = AppConstants.sugar_level_diabetes;
                        this.status_color = "#F15A24";
                        return;
                    }
                }
                this.toastMessage = AppConstants.values_not_natural;
            } else if (this.current_status.equals(AppConstants.fasting_testing)) {
                float f7 = this.blood_sugar_value;
                if (f7 < 70.0f) {
                    double d9 = this.hemoLevel;
                    if (d9 < 4.0d || (d9 >= 4.0d && d9 <= 5.1d)) {
                        this.blood_sugar_result = AppConstants.sugar_level_low;
                        this.status_color = "#00A99D";
                        return;
                    }
                }
                if (f7 >= 70.0f && f7 <= 99.0f) {
                    double d10 = this.hemoLevel;
                    if (d10 >= 5.2d && d10 <= 5.6d) {
                        this.blood_sugar_result = AppConstants.sugar_level_normal;
                        this.status_color = "#8CC63F";
                        return;
                    }
                }
                if (f7 >= 100.0f && f7 <= 125.0f) {
                    double d11 = this.hemoLevel;
                    if (d11 >= 5.7d && d11 <= 6.4d) {
                        this.blood_sugar_result = AppConstants.sugar_level_pre_diabetes;
                        this.status_color = "#FBB03B";
                        return;
                    }
                }
                if (f7 > 125.0f) {
                    double d12 = this.hemoLevel;
                    if ((d12 >= 6.5d && d12 <= 15.0d) || d12 > 15.0d) {
                        this.blood_sugar_result = AppConstants.sugar_level_diabetes;
                        this.status_color = "#F15A24";
                        return;
                    }
                }
                this.toastMessage = AppConstants.values_not_natural;
            } else if (this.current_status.equals(AppConstants.general_testing)) {
                float f8 = this.blood_sugar_value;
                if (f8 < 70.0f) {
                    double d13 = this.hemoLevel;
                    if (d13 < 4.0d || (d13 >= 4.0d && d13 <= 5.1d)) {
                        this.blood_sugar_result = AppConstants.sugar_level_low;
                        this.status_color = "#00A99D";
                        return;
                    }
                }
                if (f8 >= 70.0f && f8 <= 140.0f) {
                    double d14 = this.hemoLevel;
                    if (d14 >= 5.2d && d14 <= 5.6d) {
                        this.blood_sugar_result = AppConstants.sugar_level_normal;
                        this.status_color = "#8CC63F";
                        return;
                    }
                }
                if (f8 >= 141.0f && f8 < 200.0f) {
                    double d15 = this.hemoLevel;
                    if (d15 >= 5.7d && d15 <= 6.4d) {
                        this.blood_sugar_result = AppConstants.sugar_level_pre_diabetes;
                        this.status_color = "#FBB03B";
                        return;
                    }
                }
                if (f8 >= 200.0f) {
                    double d16 = this.hemoLevel;
                    if ((d16 >= 6.5d && d16 <= 15.0d) || d16 > 15.0d) {
                        this.blood_sugar_result = AppConstants.sugar_level_diabetes;
                        this.status_color = "#F15A24";
                        return;
                    }
                }
                this.toastMessage = AppConstants.values_not_natural;
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
        ((TextView) findViewById(R.id.toolbar_txt_title_2)).setText(getResources().getString(R.string.lbl_header_blood_sugar));
        findViewById(R.id.toolbar_rel_save).setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                AddBloodSugarActivity.this.SaveProcess();
            }
        });
        findViewById(R.id.toolbar_rel_back).setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                AddBloodSugarActivity.this.onBackPressed();
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
