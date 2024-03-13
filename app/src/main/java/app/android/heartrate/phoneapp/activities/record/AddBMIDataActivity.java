package app.android.heartrate.phoneapp.activities.record;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import app.android.heartrate.phoneapp.R;
import app.android.heartrate.phoneapp.adapters.SpinnerProfileAdapter;
import app.android.heartrate.phoneapp.model.classes.BMICalcForKg;
import app.android.heartrate.phoneapp.model.classes.BMICalcForPound;
import app.android.heartrate.phoneapp.model.classes.BMIData;
import app.android.heartrate.phoneapp.model.classes.IBMICalc;
import app.android.heartrate.phoneapp.model.classes.UserProfileData;
import app.android.heartrate.phoneapp.sqlite.SQLiteHealthTracker;
import app.android.heartrate.phoneapp.utils.AppConstants;
import app.android.heartrate.phoneapp.utils.EUGeneralClass;

public class AddBMIDataActivity extends AppCompatActivity {
    public static final int IMPERIAL_SYSTEM_INDEX = 2;
    public static final int METRIC_SYSTEM_INDEX = 1;
    public static Activity activity_add_bmi = null;
    public static String toastMessage = "";
    SQLiteHealthTracker SQLite_health_tracker;
    String age_unit = "";
    int age_value = 0;
    int[] arrayProfileIds;
    String[] arrayProfileNames;
    ArrayList<UserProfileData> array_profiles = new ArrayList<>();
    String current_date = "";
    String current_time = "";
    String date = "";
    String date_time = "";
    int day;
    int default_age = 20;
    int default_weight = 40;
    EditText et_age;
    EditText et_height;
    EditText et_weight;
    String heightUnit = "";
    float height_value = 0.0f;
    int hour;
    TextView lbl_imperial;
    TextView lbl_metric;
    LinearLayout lin_imperial;
    LinearLayout lin_metric;
    Context mContext;
    int month;
    Animation push_animation;
    RelativeLayout rel_select_date;
    RelativeLayout rel_select_time;
    int selected_user_id;
    String selected_user_name = "";
    SpinnerProfileAdapter spinner_profile_adapter;
    Spinner spinner_profiles;
    String time = "";
    TextView txt_age_unit;
    TextView txt_date;
    TextView txt_height_unit;
    TextView txt_time;
    TextView txt_weight_unit;
    View view_line_imperial;
    View view_line_metric;
    String weightUnit = "";
    float weight_value = 0.0f;
    int year;
    private String current_date_time = "";
    private int indexOfCheckedItemInMenu;
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
        setContentView(R.layout.activity_add_bmi);

        this.mContext = this;
        activity_add_bmi = this;
        this.push_animation = AnimationUtils.loadAnimation(this, R.anim.view_push);
        setUpActionBar();
        this.indexOfCheckedItemInMenu = 1;
        SQLiteHealthTracker sQLiteHealthTracker = new SQLiteHealthTracker(this);
        this.SQLite_health_tracker = sQLiteHealthTracker;
        sQLiteHealthTracker.openToWrite();
        this.spinner_profiles = findViewById(R.id.add_bmi_spinner_profiles);
        this.rel_select_date = findViewById(R.id.add_bmi_rel_select_date);
        this.txt_date = findViewById(R.id.add_bmi_txt_date);
        this.rel_select_time = findViewById(R.id.add_bmi_rel_select_time);
        this.txt_time = findViewById(R.id.add_bmi_txt_time);
        this.lin_metric = findViewById(R.id.add_bmi_lin_metric);
        this.lin_imperial = findViewById(R.id.add_bmi_lin_imperial);
        this.lbl_metric = findViewById(R.id.add_bmi_lbl_metric);
        this.lbl_imperial = findViewById(R.id.add_bmi_lbl_imperial);
        this.view_line_metric = findViewById(R.id.add_bmi_hr_metric);
        this.view_line_imperial = findViewById(R.id.add_bmi_hr_imperial);
        this.txt_age_unit = findViewById(R.id.add_bmi_txt_age_unit);
        this.et_age = findViewById(R.id.add_bmi_et_age);
        this.et_weight = findViewById(R.id.add_bmi_et_weight);
        this.txt_age_unit.setText(AppConstants.age_unit_years);
        TextView textView = findViewById(R.id.add_bmi_txt_weight_unit);
        this.txt_weight_unit = textView;
        textView.setText("Kilogram");
        this.txt_height_unit = findViewById(R.id.add_bmi_txt_height_unit);
        this.et_height = findViewById(R.id.add_bmi_et_height);
        this.et_age.setText(String.valueOf(AppConstants.default_age_value));
        this.et_weight.setText(String.valueOf(AppConstants.default_weight_value));
        SetMetricSystemUI();
        SetProfileSpinner();
        SetCurrentDateTime();
        if (AppConstants.is_bmi_edit_mode) {
            SetBMIData();
        }
        this.rel_select_date.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                AddBMIDataActivity.this.hideSoftKeyboard();
                Calendar instance = Calendar.getInstance();
                AddBMIDataActivity.this.save_entry_year = instance.get(1);
                AddBMIDataActivity.this.save_entry_month = instance.get(2);
                AddBMIDataActivity.this.save_entry_day = instance.get(5);
                new DatePickerDialog(AddBMIDataActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {


                    public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                        try {
                            AddBMIDataActivity.this.txt_date.setText(new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("dd/MM/yyyy").parse((i3 + "/" + (i2 + 1) + "/" + i).trim())));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, AddBMIDataActivity.this.save_entry_year, AddBMIDataActivity.this.save_entry_month, AddBMIDataActivity.this.save_entry_day).show();
            }
        });
        this.rel_select_time.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                Calendar instance = Calendar.getInstance();
                AddBMIDataActivity.this.save_entry_hour = instance.get(11);
                AddBMIDataActivity.this.save_entry_minute = instance.get(12);
                new TimePickerDialog(AddBMIDataActivity.this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {


                    public void onTimeSet(TimePicker timePicker, int i, int i2) {
                        try {
                            AddBMIDataActivity.this.txt_time.setText(new SimpleDateFormat("hh:mm aa").format(new SimpleDateFormat("hh:mm").parse(i + ":" + i2)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, AddBMIDataActivity.this.save_entry_hour, AddBMIDataActivity.this.save_entry_minute, false).show();
            }
        });
        this.lin_metric.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                AddBMIDataActivity.this.indexOfCheckedItemInMenu = 1;
                AddBMIDataActivity.this.SetMetricSystemUI();
            }
        });
        this.lin_imperial.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                AddBMIDataActivity.this.indexOfCheckedItemInMenu = 2;
                AddBMIDataActivity.this.SetImperialSystemUI();
            }
        });
    }


    private void SetMetricSystemUI() {
        this.lin_metric.setBackgroundResource(R.drawable.radio_unselected_bg);
        this.lin_imperial.setBackgroundResource(R.drawable.radio_unselected_bg);
        this.lbl_metric.setTextColor(getResources().getColor(R.color.bmi_tab_selected_text_color));
        this.lbl_imperial.setTextColor(getResources().getColor(R.color.bmi_tab_normal_text_color));
        this.view_line_metric.setVisibility(View.VISIBLE);
        this.view_line_imperial.setVisibility(View.INVISIBLE);
        this.txt_weight_unit.setText("Kilogram");
        this.txt_height_unit.setText("Height (Meter) : ");
        this.et_age.setText(String.valueOf(this.default_age));
        this.et_weight.setText(String.valueOf(this.default_weight));
        this.et_height.setText("1.1");
        EditText editText = this.et_height;
        editText.setSelection(editText.getText().toString().length());
    }


    private void SetImperialSystemUI() {
        this.lin_metric.setBackgroundResource(R.drawable.radio_unselected_bg);
        this.lin_imperial.setBackgroundResource(R.drawable.radio_unselected_bg);
        this.lbl_metric.setTextColor(getResources().getColor(R.color.bmi_tab_normal_text_color));
        this.lbl_imperial.setTextColor(getResources().getColor(R.color.bmi_tab_selected_text_color));
        this.view_line_metric.setVisibility(View.INVISIBLE);
        this.view_line_imperial.setVisibility(View.VISIBLE);
        this.txt_weight_unit.setText("Pound (Lb)");
        this.txt_height_unit.setText("Height (Inch) : ");
        this.et_age.setText(String.valueOf(this.default_age));
        this.et_weight.setText(String.valueOf(this.default_weight));
        this.et_height.setText("43.3");
        EditText editText = this.et_height;
        editText.setSelection(editText.getText().toString().length());
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
                int i2 = AddBMIDataActivity.this.arrayProfileIds[i];
                String trim = AddBMIDataActivity.this.arrayProfileNames[i].trim();
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

    private void SetBMIData() {
        if (AppConstants.selected_bmi_data != null) {
            String trim = AppConstants.selected_bmi_data.weight.trim();
            String trim2 = AppConstants.selected_bmi_data.weight_unit.trim();
            String trim3 = AppConstants.selected_bmi_data.height.trim();
            AppConstants.selected_bmi_data.height_unit.trim();
            String trim4 = AppConstants.selected_bmi_data.age.trim();
            AppConstants.selected_bmi_data.bmi.trim();
            AppConstants.selected_bmi_data.birth_date.trim();
            if (trim2.equalsIgnoreCase(AppConstants.bmi_unit_kg)) {
                this.indexOfCheckedItemInMenu = 1;
                SetMetricSystemUI();
            } else if (trim2.equalsIgnoreCase(AppConstants.bmi_unit_lb)) {
                this.indexOfCheckedItemInMenu = 2;
                SetImperialSystemUI();
            }
            this.age_value = Integer.parseInt(trim4);
            this.et_age.setText(trim4);
            this.et_weight.setText(trim);
            int i = this.age_value;
            if (i == 1) {
                String str = AppConstants.age_unit_year;
                this.age_unit = str;
                this.txt_age_unit.setText(str);
            } else if (i > 1) {
                String str2 = AppConstants.age_unit_years;
                this.age_unit = str2;
                this.txt_age_unit.setText(str2);
            }
            this.et_height.setText(trim3);
            EditText editText = this.et_height;
            editText.setSelection(editText.getText().toString().length());
            int i2 = AppConstants.selected_bmi_data.user_id;
            this.selected_user_id = i2;
            String GetProfileNameByID = this.SQLite_health_tracker.GetProfileNameByID(i2);
            this.selected_user_name = GetProfileNameByID;
            if (GetProfileNameByID != null) {
                int i3 = 0;
                for (int i4 = 0; i4 < this.array_profiles.size(); i4++) {
                    if (this.array_profiles.get(i4).user_name.trim().equals(this.selected_user_name)) {
                        i3 = i4;
                    }
                }
                this.spinner_profiles.setSelection(i3);
                return;
            }
            return;
        }
        EUGeneralClass.ShowErrorToast(this, "Something went wrong!");
    }


    private void SaveProcess() {
        boolean z;
        try {
            toastMessage = "";
            EditText editText = null;
            this.date = this.txt_date.getText().toString().trim();
            this.time = this.txt_time.getText().toString().trim();
            String trim = this.et_age.getText().toString().trim();
            String trim2 = this.et_weight.getText().toString().trim();
            String obj = this.et_height.getText().toString();
            boolean z2 = false;
            if (trim.length() == 0) {
                this.et_age.setError(AppConstants.error_field_require);
                editText = this.et_age;
                z = true;
            } else {
                this.age_value = Integer.parseInt(this.et_age.getText().toString().trim());
                z = false;
            }
            if (trim2.length() == 0) {
                this.et_weight.setError(AppConstants.error_field_require);
                editText = this.et_weight;
                z = true;
            } else {
                this.weight_value = Float.parseFloat(this.et_weight.getText().toString().trim());
            }
            if (obj.length() == 0) {
                this.et_height.setError(AppConstants.error_field_require);
                editText = this.et_height;
            } else {
                float parseFloat = Float.parseFloat(obj);
                this.height_value = parseFloat;
                int i = this.indexOfCheckedItemInMenu;
                if (i != 1) {
                    if (i != 2) {
                        z2 = z;
                    } else if (((double) parseFloat) < 19.68d || ((double) parseFloat) > 98.42d) {
                        this.et_height.setError(AppConstants.error_height_range_pound);
                        editText = this.et_height;
                    }
                    if (z2) {
                    }
                } else if (((double) parseFloat) < 0.5d || ((double) parseFloat) > 2.5d) {
                    this.et_height.setError(AppConstants.error_height_range_kg);
                    editText = this.et_height;
                } else if (z2) {
                    editText.requestFocus();
                    return;
                } else {
                    BMIData bMIData = new BMIData();
                    float countBMI = getBMICalc().countBMI(this.weight_value, this.height_value);
                    int i2 = (countBMI > -1.0f ? 1 : (countBMI == -1.0f ? 0 : -1));
                    if (i2 != 0) {
                        int i3 = this.indexOfCheckedItemInMenu;
                        if (i3 == 1) {
                            this.weightUnit = "kg";
                            this.heightUnit = "m";
                        } else if (i3 == 2) {
                            this.weightUnit = "lb";
                            this.heightUnit = "in";
                        }
                        SetCurrentDateTime();
                        String format = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
                        int selectedItemPosition = this.spinner_profiles.getSelectedItemPosition();
                        this.selected_user_id = this.arrayProfileIds[selectedItemPosition];
                        this.selected_user_name = this.arrayProfileNames[selectedItemPosition];
                        if (!AppConstants.is_bmi_edit_mode) {
                            bMIData.user_id = this.selected_user_id;
                            bMIData.date = this.date.trim();
                            bMIData.time = this.time.trim();
                            bMIData.weight = String.valueOf(this.weight_value);
                            bMIData.weight_unit = this.weightUnit.trim();
                            bMIData.height = String.valueOf(this.height_value);
                            bMIData.height_unit = this.heightUnit.trim();
                            bMIData.age = String.valueOf(this.age_value);
                            bMIData.bmi = String.valueOf(countBMI);
                            bMIData.birth_date = format.trim();
                            GetDateTime(this.date.trim(), this.time.trim());
                            bMIData.dateTime = this.date_time;
                            bMIData.day = this.day;
                            bMIData.month = this.month;
                            bMIData.year = this.year;
                            bMIData.hour = this.hour;
                            if (toastMessage.equalsIgnoreCase(AppConstants.values_not_natural)) {
                                EUGeneralClass.ShowErrorToast(this, toastMessage);
                                return;
                            } else if (i2 != 0) {
                                this.SQLite_health_tracker.InsertBMIData(bMIData);
                                toastMessage = "BMI Data saved successfully!";
                                EUGeneralClass.ShowSuccessToast(this, "BMI Data saved successfully!");
                                BMIResultScreen(countBMI);
                                return;
                            } else {
                                toastMessage = "Wrong values are give so problem in update data!";
                                EUGeneralClass.ShowErrorToast(this, "Wrong values are give so problem in update data!");
                                return;
                            }
                        } else {
                            int i4 = AppConstants.selected_bmi_data.row_id;
                            bMIData.user_id = this.selected_user_id;
                            bMIData.date = this.date.trim();
                            bMIData.time = this.time.trim();
                            bMIData.weight = String.valueOf(this.weight_value);
                            bMIData.weight_unit = this.weightUnit.trim();
                            bMIData.height = String.valueOf(this.height_value);
                            bMIData.height_unit = this.heightUnit.trim();
                            bMIData.age = String.valueOf(this.age_value);
                            bMIData.bmi = String.valueOf(countBMI);
                            bMIData.birth_date = format.trim();
                            GetDateTime(this.date.trim(), this.time.trim());
                            bMIData.dateTime = this.date_time;
                            bMIData.day = this.day;
                            bMIData.month = this.month;
                            bMIData.year = this.year;
                            bMIData.hour = this.hour;
                            if (toastMessage.equalsIgnoreCase(AppConstants.values_not_natural)) {
                                EUGeneralClass.ShowErrorToast(this, toastMessage);
                                return;
                            } else if (i2 != 0) {
                                this.SQLite_health_tracker.UpdateBMIData(i4, this.selected_user_id, bMIData);
                                toastMessage = "BMI Data updated successfully!";
                                EUGeneralClass.ShowSuccessToast(this, "BMI Data updated successfully!");
                                onBackPressed();
                                return;
                            } else {
                                toastMessage = "Wrong values are give so problem in update data!";
                                EUGeneralClass.ShowErrorToast(this, "Wrong values are give so problem in update data!");
                                return;
                            }
                        }
                    } else {
                        this.et_age.setText(String.valueOf(this.default_age));
                        this.et_weight.setText(String.valueOf(this.default_weight));
                        this.et_height.setText("1.1");
                        EditText editText2 = this.et_height;
                        editText2.setSelection(editText2.getText().toString().length());
                        toastMessage = "Something went wrong for insert data!";
                        EUGeneralClass.ShowSuccessToast(this, "Something went wrong for insert data!");
                        return;
                    }
                }
            }
            z2 = true;
            if (z2) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void BMIResultScreen(float f) {
        Intent intent = new Intent(this, BMIResultActivity.class);
        intent.putExtra("ResultText", AppConstants.setResultText(f));
        intent.putExtra("BMI", String.valueOf(f));
        startActivity(intent);
        finish();
    }

    private IBMICalc getBMICalc() {
        int i = this.indexOfCheckedItemInMenu;
        if (i == 1) {
            return new BMICalcForKg();
        }
        if (i == 2) {
            return new BMICalcForPound();
        }
        toastMessage = AppConstants.values_not_natural;
        throw new IllegalArgumentException("Unknown checked index in menu");
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
            this.current_date = simpleDateFormat2.format(parse);
            this.current_time = simpleDateFormat3.format(parse);
            this.current_time = (String) DateFormat.format("hh:mm aaa", Calendar.getInstance().getTime());
            this.txt_date.setText(this.current_date);
            this.txt_time.setText(this.current_time);
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
        ((TextView) findViewById(R.id.toolbar_txt_title_2)).setText(getResources().getString(R.string.lbl_header_bmi));
        findViewById(R.id.toolbar_rel_save).setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                AddBMIDataActivity.this.SaveProcess();
            }
        });
        findViewById(R.id.toolbar_rel_back).setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                AddBMIDataActivity.this.onBackPressed();
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
