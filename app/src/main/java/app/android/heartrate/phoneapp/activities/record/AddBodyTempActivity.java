package app.android.heartrate.phoneapp.activities.record;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import app.android.heartrate.phoneapp.AdAdmob;
import app.android.heartrate.phoneapp.R;
import app.android.heartrate.phoneapp.adapters.SpinnerProfileAdapter;
import app.android.heartrate.phoneapp.model.ProfileData;
import app.android.heartrate.phoneapp.model.classes.BodyTempData;
import app.android.heartrate.phoneapp.model.classes.UserProfileData;
import app.android.heartrate.phoneapp.sharedpreferences.SharedPreferences;
import app.android.heartrate.phoneapp.sqlite.SQLiteHealthTracker;
import app.android.heartrate.phoneapp.utils.AppConstants;
import app.android.heartrate.phoneapp.utils.EUGeneralClass;
import app.android.heartrate.phoneapp.utils.StoredPreferencesValue;

public class AddBodyTempActivity extends AppCompatActivity {
    SQLiteHealthTracker SQLite_health_tracker;

    float cel_max_value = 42.0f;
    float cel_min_value = 33.0f;
    float cel_value = 37.0f;
    String current_minute = "";
    String date_time = "";
    int day;
    float fah_max_value = 108.0f;
    float fah_min_value = 91.0f;
    float fah_value = 0.0f;
    int hour;
    ImageView img_info;
    TextView lbl_seek_celsius_max;
    TextView lbl_seek_celsius_min;
    TextView lbl_seek_fahrenheit_max;
    TextView lbl_seek_fahrenheit_min;
    TextView lbl_seek_pulse_max;
    TextView lbl_seek_pulse_min;
    TextView spinner_txt_name;
    Context mContext;
    int month;
    float pulse_max_value = 250.0f;
    float pulse_min_value = 1.0f;
    int pulse_value = 72;
    Animation push_animation;
    RelativeLayout rel_fever_status;
    RelativeLayout rel_select_date;
    RelativeLayout rel_select_time;
    TextView save_txt_date;
    TextView save_txt_time;
    SeekBar seek_celsius;
    SeekBar seek_fahrenheit;
    SeekBar seek_pulse;
    SimpleDateFormat simple_data_format;
    SpinnerProfileAdapter spinner_profile_adapter;
    Spinner spinner_profiles;
    TextView txt_fever_status;
    TextView txt_seek_celsius_progress;
    TextView txt_seek_fahrenheit_progress;
    TextView txt_seek_pulse_progress;
    int year;
    private String[] array_celsius_range;
    private String[] array_fahrenheit_range;
    private String[] array_pulse_range;
    private int celsiusIndex = 0;
    private String current_date_time = "";
    private int fahrenheitIndex = 0;
    private int pulseIndex = 0;
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
        setContentView(R.layout.activity_add_body_temp);

        this.mContext = this;
        this.push_animation = AnimationUtils.loadAnimation(this, R.anim.view_push);
        setUpActionBar();
        sharedPreferencesUtils = SharedPreferences.INSTANCE;
        this.simple_data_format = new SimpleDateFormat("dd/MM/yyy hh:mm:ss aa");
        SQLiteHealthTracker sQLiteHealthTracker = new SQLiteHealthTracker(this);
        this.SQLite_health_tracker = sQLiteHealthTracker;
        sQLiteHealthTracker.openToWrite();
        this.array_celsius_range = new String[91];
        this.array_fahrenheit_range = new String[91];
        this.array_pulse_range = new String[ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION];
        int i = 0;
        int i2 = 0;
        while (true) {
            String[] strArr = this.array_celsius_range;
            if (i2 >= strArr.length) {
                break;
            }
            float f = ((float) ((i2) + 330)) / 10.0f;
            strArr[i2] = String.valueOf(f);
            this.array_fahrenheit_range[i2] = String.valueOf(((float) Math.round(((f * 1.8f) + 32.0f) * 10.0f)) / 10.0f);
            i2++;
        }
        while (true) {
            String[] strArr2 = this.array_pulse_range;
            if (i >= strArr2.length) {
                break;
            }
            int i3 = i + 1;
            strArr2[i] = String.valueOf(i3);
            i = i3;
        }
        this.img_info = findViewById(R.id.add_temp_img_info);
        this.spinner_profiles = findViewById(R.id.add_temp_spinner_profiles);
        this.rel_fever_status = findViewById(R.id.add_temp_rel_fever_status);
        this.txt_fever_status = findViewById(R.id.add_temp_txt_fever_status);
        this.rel_select_date = findViewById(R.id.add_temp_rel_select_date);
        this.save_txt_date = findViewById(R.id.add_temp_txt_date);
        this.rel_select_time = findViewById(R.id.add_temp_rel_select_time);
        this.save_txt_time = findViewById(R.id.add_temp_txt_time);
        this.seek_pulse = findViewById(R.id.add_temp_seek_pulse);
        this.seek_celsius = findViewById(R.id.add_temp_seek_cel);
        this.seek_fahrenheit = findViewById(R.id.add_temp_seek_fah);
        this.lbl_seek_pulse_min = findViewById(R.id.add_temp_lbl_pulse_min);
        this.lbl_seek_pulse_max = findViewById(R.id.add_temp_lbl_pulse_max);
        this.txt_seek_pulse_progress = findViewById(R.id.add_temp_txt_pulse_progress);
        this.lbl_seek_celsius_min = findViewById(R.id.add_temp_lbl_cel_min);
        this.lbl_seek_celsius_max = findViewById(R.id.add_temp_lbl_cel_max);
        this.txt_seek_celsius_progress = findViewById(R.id.add_temp_txt_cel_progress);
        this.lbl_seek_fahrenheit_min = findViewById(R.id.add_temp_lbl_fah_min);
        this.lbl_seek_fahrenheit_max = findViewById(R.id.add_temp_lbl_fah_max);
        this.spinner_txt_name = findViewById(R.id.spinner_txt_name);
        this.txt_seek_fahrenheit_progress = findViewById(R.id.add_temp_txt_fah_progress);
        SetProfileSpinner();
        SetNumberPickers();
        SetCurrentDateTime();
        if (AppConstants.is_body_temp_edit_mode) {
            SetBodyTempData();
        }
        this.rel_select_date.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                Calendar instance = Calendar.getInstance();
                AddBodyTempActivity.this.save_entry_year = instance.get(1);
                AddBodyTempActivity.this.save_entry_month = instance.get(2);
                AddBodyTempActivity.this.save_entry_day = instance.get(5);
                new DatePickerDialog(AddBodyTempActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {


                    public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                        try {
                            AddBodyTempActivity.this.save_txt_date.setText(new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("dd/MM/yyyy").parse((i3 + "/" + (i2 + 1) + "/" + i).trim())));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, AddBodyTempActivity.this.save_entry_year, AddBodyTempActivity.this.save_entry_month, AddBodyTempActivity.this.save_entry_day).show();
            }
        });
        this.rel_select_time.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                Calendar instance = Calendar.getInstance();
                AddBodyTempActivity.this.save_entry_hour = instance.get(11);
                AddBodyTempActivity.this.save_entry_minute = instance.get(12);
                new TimePickerDialog(AddBodyTempActivity.this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {


                    public void onTimeSet(TimePicker timePicker, int i, int i2) {
                        try {
                            AddBodyTempActivity.this.save_txt_time.setText(new SimpleDateFormat("hh:mm aa").format(new SimpleDateFormat("hh:mm").parse(i + ":" + i2)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, AddBodyTempActivity.this.save_entry_hour, AddBodyTempActivity.this.save_entry_minute, false).show();
            }
        });
        this.img_info.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                view.startAnimation(AddBodyTempActivity.this.push_animation);
                AddBodyTempActivity.this.InfoDialog();
            }
        });
    }

    @SuppressLint("NewApi")
    private void SetBodyTempData() {
        if (AppConstants.selected_body_temp_data != null) {
            this.celsiusIndex = AppConstants.selected_body_temp_data.celsius_index;
            this.fahrenheitIndex = AppConstants.selected_body_temp_data.fahrenheit_index;
            this.pulseIndex = AppConstants.selected_body_temp_data.pulse_index;
            this.cel_value = AppConstants.selected_body_temp_data.celsius_value;
            this.fah_value = AppConstants.selected_body_temp_data.fahrenheit_value;
            this.pulse_value = (int) AppConstants.selected_body_temp_data.pulse_value;
            this.seek_pulse.setMin((int) this.pulse_min_value);
            this.seek_pulse.setMax((int) this.pulse_max_value);
            this.lbl_seek_pulse_min.setText(String.valueOf(this.pulse_min_value));
            this.lbl_seek_pulse_max.setText(String.valueOf(this.pulse_max_value));
            this.seek_celsius.setMin((int) this.cel_min_value);
            this.seek_celsius.setMax((int) this.cel_max_value);
            this.lbl_seek_celsius_min.setText(String.valueOf(this.cel_min_value));
            this.lbl_seek_celsius_max.setText(String.valueOf(this.cel_max_value));
            this.seek_fahrenheit.setMin((int) this.fah_min_value);
            this.seek_fahrenheit.setMax((int) this.fah_max_value);
            this.lbl_seek_fahrenheit_min.setText(String.valueOf(this.fah_min_value));
            this.lbl_seek_fahrenheit_max.setText(String.valueOf(this.fah_max_value));
            this.fah_value = AppConstants.convertCelsiusToFahrenheit(this.cel_value);
            this.seek_pulse.setProgress(this.pulse_value);
            this.seek_celsius.setProgress((int) this.cel_value);
            this.seek_fahrenheit.setProgress((int) this.fah_value);
            this.txt_seek_pulse_progress.setText(String.valueOf(this.pulse_value));
            this.txt_seek_celsius_progress.setText(String.valueOf(this.cel_value));
            this.txt_seek_fahrenheit_progress.setText(String.valueOf(this.fah_value));
            SetEditDateTime(this.save_txt_date, this.save_txt_time, AppConstants.selected_body_temp_data.dateTime.trim());
            SetFeverStatus(this.cel_value);
            return;
        }
        EUGeneralClass.ShowErrorToast(this, "Something went wrong!");
    }

    private void SetProfileSpinner() {
        String name = sharedPreferencesUtils.getUserName();
        spinner_txt_name.setText(name);
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
            this.save_txt_date.setText(format2);
            this.save_txt_time.setText(DateFormat.format("hh:mm aaa", Calendar.getInstance().getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void SetEditDateTime(TextView textView, TextView textView2, String str) {
        try {
            Date parse = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa").parse(str);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("hh:mm aa");
            SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("mm");
            String format = simpleDateFormat.format(parse);
            String format2 = simpleDateFormat2.format(parse);
            this.current_minute = simpleDateFormat3.format(parse);
            Log.e("Current Date:", format);
            Log.e("Current Time:", format2);
            Log.e("Current Date Time:", this.current_date_time);
            textView.setText(format);
            textView2.setText(format2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    private void SetNumberPickers() {
        this.celsiusIndex = StoredPreferencesValue.getDefaultCelsiusIndex(StoredPreferencesValue.CELSIUS_KEY, this);
        this.fahrenheitIndex = StoredPreferencesValue.getDefaultFahrenheitIndex(StoredPreferencesValue.FAHRENHEIT_KEY, this);
        this.pulseIndex = StoredPreferencesValue.getDefaultPulseIndex(StoredPreferencesValue.PULSE_KEY, this);
        this.cel_value = StoredPreferencesValue.getDefaultCelsiusValue(this);
        this.fah_value = StoredPreferencesValue.getDefaultFahrenheitValue(this);
        this.pulse_value = StoredPreferencesValue.getDefaultPulseValue(this);
        this.seek_pulse.setMin((int) this.pulse_min_value);
        this.seek_pulse.setMax((int) this.pulse_max_value);
        this.lbl_seek_pulse_min.setText(String.valueOf(this.pulse_min_value));
        this.lbl_seek_pulse_max.setText(String.valueOf(this.pulse_max_value));
        this.seek_celsius.setMin((int) this.cel_min_value);
        this.seek_celsius.setMax((int) this.cel_max_value);
        this.lbl_seek_celsius_min.setText(String.valueOf(this.cel_min_value));
        this.lbl_seek_celsius_max.setText(String.valueOf(this.cel_max_value));
        this.seek_fahrenheit.setMin((int) this.fah_min_value);
        this.seek_fahrenheit.setMax((int) this.fah_max_value);
        this.lbl_seek_fahrenheit_min.setText(String.valueOf(this.fah_min_value));
        this.lbl_seek_fahrenheit_max.setText(String.valueOf(this.fah_max_value));
        this.fah_value = AppConstants.convertCelsiusToFahrenheit(this.cel_value);
        this.seek_pulse.setProgress(this.pulse_value);
        this.seek_celsius.setProgress((int) this.cel_value);
        this.seek_fahrenheit.setProgress((int) this.fah_value);
        this.txt_seek_pulse_progress.setText(String.valueOf(this.pulse_value));
        this.txt_seek_celsius_progress.setText(String.valueOf(this.cel_value));
        this.txt_seek_fahrenheit_progress.setText(String.valueOf(this.fah_value));
        this.seek_pulse.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = AddBodyTempActivity.this.seek_pulse.getProgress();
                Log.e("Pulse Value :", String.valueOf(progress));
                AddBodyTempActivity.this.pulse_value = progress;
                AddBodyTempActivity.this.txt_seek_pulse_progress.setText(String.valueOf(AddBodyTempActivity.this.pulse_value));
            }
        });
        this.seek_celsius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = AddBodyTempActivity.this.seek_celsius.getProgress();
                Log.e("Celsius Value :", String.valueOf(progress));
                AddBodyTempActivity.this.cel_value = (float) progress;
                AddBodyTempActivity addBodyTempActivity = AddBodyTempActivity.this;
                addBodyTempActivity.fah_value = AppConstants.convertCelsiusToFahrenheit(addBodyTempActivity.cel_value);
                AddBodyTempActivity.this.seek_fahrenheit.setProgress((int) AddBodyTempActivity.this.fah_value);
                AddBodyTempActivity addBodyTempActivity2 = AddBodyTempActivity.this;
                addBodyTempActivity2.SetFeverStatus(addBodyTempActivity2.cel_value);
                AddBodyTempActivity.this.txt_seek_celsius_progress.setText(String.valueOf(AddBodyTempActivity.this.cel_value));
                AddBodyTempActivity.this.txt_seek_fahrenheit_progress.setText(String.valueOf(AddBodyTempActivity.this.fah_value));
            }
        });
        this.seek_fahrenheit.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = AddBodyTempActivity.this.seek_fahrenheit.getProgress();
                Log.e("Fah Value :", String.valueOf(progress));
                AddBodyTempActivity.this.fah_value = (float) progress;
                AddBodyTempActivity addBodyTempActivity = AddBodyTempActivity.this;
                addBodyTempActivity.cel_value = AppConstants.convertFahrenheitToCelsius(addBodyTempActivity.fah_value);
                AddBodyTempActivity.this.seek_celsius.setProgress((int) AddBodyTempActivity.this.cel_value);
                AddBodyTempActivity addBodyTempActivity2 = AddBodyTempActivity.this;
                addBodyTempActivity2.SetFeverStatus(addBodyTempActivity2.cel_value);
                AddBodyTempActivity.this.txt_seek_celsius_progress.setText(String.valueOf(AddBodyTempActivity.this.cel_value));
                AddBodyTempActivity.this.txt_seek_fahrenheit_progress.setText(String.valueOf(AddBodyTempActivity.this.fah_value));
            }
        });
        SetFeverStatus(this.cel_value);
    }


    private void SaveProcess() {
        try {
            String trim = String.valueOf(this.array_celsius_range[this.celsiusIndex]).trim();
            String trim2 = String.valueOf(this.array_fahrenheit_range[this.fahrenheitIndex]).trim();
            String trim3 = String.valueOf(this.array_pulse_range[this.pulseIndex]).trim();
            String trim4 = this.save_txt_date.getText().toString().trim();
            String trim5 = this.save_txt_time.getText().toString().trim();
            BodyTempData bodyTempData = new BodyTempData();
            if (!AppConstants.is_body_temp_edit_mode) {
                bodyTempData.user_id = this.sharedPreferencesUtils.getUserId();
                bodyTempData.name = this.sharedPreferencesUtils.getUserName();
                bodyTempData.date = this.date_time.trim();
                bodyTempData.celsius = trim;
                bodyTempData.fahrenheit = trim2;
                bodyTempData.pulse = trim3;
                bodyTempData.celsius_value = this.cel_value;
                bodyTempData.fahrenheit_value = this.fah_value;
                bodyTempData.pulse_value = (float) this.pulse_value;
                if (trim.equals("")) {
                    bodyTempData.celsius = "-";
                }
                if (trim2.equals("")) {
                    bodyTempData.fahrenheit = "-";
                }
                if (trim3.equals("")) {
                    bodyTempData.pulse = "-";
                }
                GetDateTime(trim4.trim(), trim5.trim());
                bodyTempData.date = trim4.trim();
                bodyTempData.time = trim5.trim();
                bodyTempData.dateTime = this.date_time.trim();
                bodyTempData.day = this.day;
                bodyTempData.month = this.month;
                bodyTempData.year = this.year;
                bodyTempData.hour = this.hour;
                bodyTempData.tags = this.sharedPreferencesUtils.getUserName();
                this.SQLite_health_tracker.InsertTemperatureData(bodyTempData);
                EUGeneralClass.ShowSuccessToast(this, AppConstants.data_saved_messages);
            } else {
                int i = AppConstants.selected_body_temp_data.row_id;
                bodyTempData.user_id = this.sharedPreferencesUtils.getUserId();
                bodyTempData.name = this.sharedPreferencesUtils.getUserName();
                bodyTempData.celsius = trim;
                bodyTempData.fahrenheit = trim2;
                bodyTempData.pulse = trim3;
                bodyTempData.celsius_value = this.cel_value;
                bodyTempData.fahrenheit_value = this.fah_value;
                bodyTempData.pulse_value = (float) this.pulse_value;
                if (trim.equals("")) {
                    bodyTempData.celsius = "-";
                }
                if (trim2.equals("")) {
                    bodyTempData.fahrenheit = "-";
                }
                if (trim3.equals("")) {
                    bodyTempData.pulse = "-";
                }
                GetDateTime(trim4.trim(), trim5.trim());
                bodyTempData.date = trim4.trim();
                bodyTempData.time = trim5.trim();
                bodyTempData.dateTime = this.date_time.trim();
                bodyTempData.day = this.day;
                bodyTempData.month = this.month;
                bodyTempData.year = this.year;
                bodyTempData.hour = this.hour;
                bodyTempData.tags = this.sharedPreferencesUtils.getUserName();
                this.SQLite_health_tracker.UpdateTemperatureData(i, this.sharedPreferencesUtils.getUserId(), bodyTempData);
                EUGeneralClass.ShowSuccessToast(this, AppConstants.data_updated_messages);
            }
            StoredPreferencesValue.setDefaultCelsiusValue(this.cel_value, this);
            StoredPreferencesValue.setDefaultFahrenheitValue(this.fah_value, this);
            StoredPreferencesValue.setDefaultPulseValue(this.pulse_value, this);
            StoredPreferencesValue.setDefaultCelsiusIndex(StoredPreferencesValue.CELSIUS_KEY, this.celsiusIndex, this);
            StoredPreferencesValue.setDefaultFahrenheitIndex(StoredPreferencesValue.FAHRENHEIT_KEY, this.fahrenheitIndex, this);
            StoredPreferencesValue.setDefaultPulseIndex(StoredPreferencesValue.PULSE_KEY, this.pulseIndex, this);
            onBackPressed();
        } catch (Exception e) {
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
            SimpleDateFormat simpleDateFormat5 = new SimpleDateFormat("hh:mm:ss aa");
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


    private void SetFeverStatus(float f) {
        if (f >= 36.5f && ((double) f) <= 37.5d) {
            this.txt_fever_status.setText(AppConstants.NORMAL);
            this.rel_fever_status.setBackgroundResource(R.drawable.rel_normal_bg);
        } else if (f > 37.5f && ((double) f) <= 38.3d) {
            this.txt_fever_status.setText(AppConstants.FEVER);
            this.rel_fever_status.setBackgroundResource(R.drawable.rel_fever_bg);
        } else if (f > 38.3f) {
            this.txt_fever_status.setText(AppConstants.HIGH_FEVER);
            this.rel_fever_status.setBackgroundResource(R.drawable.rel_high_fever_bg);
        } else if (f < 35.0f) {
            this.txt_fever_status.setText(AppConstants.HYPOTHERMIA);
            this.rel_fever_status.setBackgroundResource(R.drawable.rel_hypothermia_bg);
        }
    }

    private void setUpActionBar() {
        ((TextView) findViewById(R.id.toolbar_txt_title_1)).setText(getResources().getString(R.string.lbl_header_add));
        ((TextView) findViewById(R.id.toolbar_txt_title_2)).setText(getResources().getString(R.string.lbl_header_body_temp_data));
        findViewById(R.id.toolbar_rel_save).setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                AddBodyTempActivity.this.SaveProcess();
            }
        });
        findViewById(R.id.toolbar_rel_back).setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                AddBodyTempActivity.this.onBackPressed();
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


    private void InfoDialog() {
        final Dialog dialog = new Dialog(this.mContext, R.style.TransparentBackground);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_rate);
        Button button = dialog.findViewById(R.id.dialog_conform_btn_yes);
        Button button2 = dialog.findViewById(R.id.dialog_conform_btn_no);
        String string = getResources().getString(R.string.app_temp_info);
        ((TextView) dialog.findViewById(R.id.dialog_conform_txt_header)).setText("Body Temperature");
        ((TextView) dialog.findViewById(R.id.dialog_conform_txt_message)).setText(string);
        button.setText("OK");
        button2.setText("Cancel");
        button2.setVisibility(View.GONE);
        button.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
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
