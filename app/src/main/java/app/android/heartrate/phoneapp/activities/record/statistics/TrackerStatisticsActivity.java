package app.android.heartrate.phoneapp.activities.record.statistics;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import app.android.heartrate.phoneapp.R;
import app.android.heartrate.phoneapp.adapters.SpinnerProfileAdapter;
import app.android.heartrate.phoneapp.model.ProfileData;
import app.android.heartrate.phoneapp.model.bloodcount.chart.BloodCountChartRequest;
import app.android.heartrate.phoneapp.model.bloodcount.chart.BloodCountChartResponse;
import app.android.heartrate.phoneapp.model.bloodpressure.BloodPressureChartResponse;
import app.android.heartrate.phoneapp.model.bloodsugar.BloodSugarChartResponse;
import app.android.heartrate.phoneapp.model.bmi.BMIChartResponse;
import app.android.heartrate.phoneapp.model.cholesterol.CholesterolChartResponse;
import app.android.heartrate.phoneapp.model.classes.BMIChartData;
import app.android.heartrate.phoneapp.model.classes.BloodCountChartData;
import app.android.heartrate.phoneapp.model.classes.BloodPressureChartData;
import app.android.heartrate.phoneapp.model.classes.BloodSugarChartData;
import app.android.heartrate.phoneapp.model.classes.BodyTempChartAllData;
import app.android.heartrate.phoneapp.model.classes.CholesterolChartData;
import app.android.heartrate.phoneapp.model.classes.HeartRateChartData;
import app.android.heartrate.phoneapp.model.classes.WeightChartData;
import app.android.heartrate.phoneapp.model.classes.WeightData;
import app.android.heartrate.phoneapp.model.heartrate.HeartRateChartResponse;
import app.android.heartrate.phoneapp.model.temperature.BodyTempChartResponse;
import app.android.heartrate.phoneapp.model.weight.WeightChartRequest;
import app.android.heartrate.phoneapp.model.weight.WeightChartResponse;
import app.android.heartrate.phoneapp.retrofit.ApiClient;
import app.android.heartrate.phoneapp.sharedpreferences.SharedPreferences;
import app.android.heartrate.phoneapp.sqlite.SQLiteHealthTracker;
import app.android.heartrate.phoneapp.utils.AppConstants;
import app.android.heartrate.phoneapp.utils.customviews.MyMarkerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackerStatisticsActivity extends AppCompatActivity {
    public static Activity statistics_activity;
    SQLiteHealthTracker SQLite_health_tracker;

    ArrayAdapter<String> adapter_report_duration = null;
    ArrayAdapter<String> adapter_report_type = null;
    String[] arrayReportDuration;
    String[] arrayReportTypes;

    ArrayList<WeightData> array_temp_saved_data;

    float bar_width = 0.25f;
    String blood_count_legend_text = "RCB";
    String blood_sugar_legend_text = "Sugar Level";
    String bmi_legend_text = "BMI";
    String celsius_legend_text = "Celsius";
    int chart_bottom_color;
    int chart_bottom_color_1;
    int chart_bottom_color_2;
    int chart_bottom_color_3;
    int chart_bottom_color_4;
    int chart_top_color;
    int chart_top_color_1;
    int chart_top_color_2;
    int chart_top_color_3;
    int chart_top_color_4;
    String cholesterol_legend_text = "Cholesterol";

    String current_report_duration = "All";
    String current_report_type;

    String custom_end_date = "";
    String custom_start_date = "";
    ArrayList<IBarDataSet> dataSets = new ArrayList<>();
    float defaultBarWidth = -1.0f;
    String diastolic_legend_text = "Diastolic";
    int extra_y_axis_value = 20;
    String fahrenheit_legend_text = "Fahrenheit";
    float graph_left_axis_text_size = 12.0f;
    float graph_legend_text_size = 12.0f;
    float graph_x_axis_text_size = 12.0f;
    String hdl_legend_text = "HDL";
    String heart_rate_legend_text = "Heart Rate";
    boolean is_chart_legend_visible = true;
    boolean is_user_interact = false;
    TextView lbl_no_data;
    String ldl_legend_text = "LDL";
    LinearLayout lin_bottom_label;
    String pulse_legend_text = "Pulse";
    Animation push_animation;
    int saved_temp_data_count = 0;
    SpinnerProfileAdapter spinner_profile_adapter;
    Spinner spinner_profiles;
    Spinner spinner_report_duration;
    Spinner spinner_report_types;
    String systolic_legend_text = "Systolic";
    Typeface tf_quick_bold;
    Typeface tf_roboto;
    String triglyceride_legend_text = "Triglyceride";
    TextView txt_chart_type;
    TextView spinner_txt_name;
    String weight_legend_text = "Weight";
    private BarChart graph_view_all;
    private BarChart graph_view_custom;
    private BarChart graph_view_monthly;
    private BarChart graph_view_today;
    private BarChart graph_view_yearly;
    private int start_date_day;
    private int start_date_month;
    private int start_date_year;

    private SharedPreferences sharedPreferencesUtils;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        SetView();


        AppConstants.overridePendingTransitionEnter(this);
    }

    private void SetView() {
        setContentView(R.layout.activity_tracker_statistics);

        statistics_activity = this;
        setUpActionBar();
        sharedPreferencesUtils = SharedPreferences.INSTANCE;
        this.push_animation = AnimationUtils.loadAnimation(this, R.anim.view_push);
        this.tf_roboto = Typeface.createFromAsset(getAssets(), AppConstants.roboto_font_path);
        this.tf_quick_bold = Typeface.createFromAsset(getAssets(), AppConstants.quick_bold_font_path);
        SQLiteHealthTracker sQLiteHealthTracker = new SQLiteHealthTracker(this);
        this.SQLite_health_tracker = sQLiteHealthTracker;
        sQLiteHealthTracker.openToWrite();
        TextView textView = findViewById(R.id.lbl_no_data);
        this.lbl_no_data = textView;
        textView.setVisibility(View.GONE);
        this.txt_chart_type = findViewById(R.id.statistics_txt_tracker_type);
        this.spinner_txt_name = findViewById(R.id.spinner_txt_name);
        this.chart_bottom_color = ContextCompat.getColor(this, R.color.chart_bar_gradient_color_bottom);
        this.chart_top_color = ContextCompat.getColor(this, R.color.chart_bar_gradient_color_top);
        this.chart_bottom_color_1 = ContextCompat.getColor(this, R.color.bar_gradient_color_bottom_1);
        this.chart_top_color_1 = ContextCompat.getColor(this, R.color.bar_gradient_color_top_1);
        this.chart_bottom_color_2 = ContextCompat.getColor(this, R.color.bar_gradient_color_bottom_2);
        this.chart_top_color_2 = ContextCompat.getColor(this, R.color.bar_gradient_color_top_2);
        this.chart_bottom_color_3 = ContextCompat.getColor(this, R.color.bar_gradient_color_bottom_3);
        this.chart_top_color_3 = ContextCompat.getColor(this, R.color.bar_gradient_color_top_3);
        this.chart_bottom_color_4 = ContextCompat.getColor(this, R.color.bar_gradient_color_bottom_4);
        this.chart_top_color_4 = ContextCompat.getColor(this, R.color.bar_gradient_color_top_4);
        this.blood_count_legend_text = "RBC (" + getResources().getString(R.string.lbl_m_mm3) + ")";
        this.blood_sugar_legend_text = "Sugar Level (" + getResources().getString(R.string.lbl_mg_dl) + ")";
        this.spinner_profiles = findViewById(R.id.statistics_spinner_profiles);
        this.spinner_report_types = findViewById(R.id.statistics_spinner_report_type);
        this.spinner_report_duration = findViewById(R.id.statistics_spinner_report_duration);
        this.graph_view_today = findViewById(R.id.statistics_bar_chart_today);
        this.graph_view_monthly = findViewById(R.id.statistics_bar_chart_month);
        this.graph_view_yearly = findViewById(R.id.statistics_bar_chart_year);
        this.graph_view_all = findViewById(R.id.statistics_bar_chart_all);
        this.graph_view_custom = findViewById(R.id.statistics_bar_chart_custom);
        this.graph_view_today.setVisibility(View.GONE);
        this.graph_view_monthly.setVisibility(View.GONE);
        this.graph_view_yearly.setVisibility(View.GONE);
        this.graph_view_all.setVisibility(View.VISIBLE);
        this.graph_view_custom.setVisibility(View.GONE);
        this.lin_bottom_label = findViewById(R.id.diagram_lin_bottom_label);
        SetProfileSpinner();
        SetReportTypesSpinner();
        SetReportDurationSpinner();
        this.current_report_duration = getResources().getString(R.string.lbl_today);
        this.spinner_profiles.setSelected(false);
        this.spinner_profiles.setSelection(0, true);
        this.spinner_report_types.setSelected(false);
        this.spinner_report_types.setSelection(0, true);
        ArrayList<WeightData> arrayList = (ArrayList) this.SQLite_health_tracker.GetWeightData();
        this.array_temp_saved_data = arrayList;
        this.saved_temp_data_count = arrayList.size();
    }

    public void onUserInteraction() {
        super.onUserInteraction();
        this.is_user_interact = true;
    }

    private void SetProfileSpinner() {
        ProfileData profileData = SQLite_health_tracker.GetUserProfileData();
        String name = profileData.getFirstName() + " " + profileData.getLastName();
        spinner_txt_name.setText(name);
    }

    private void SetReportTypesSpinner() {
        String[] stringArray = getResources().getStringArray(R.array.tracker_chart_tools);
        this.arrayReportTypes = stringArray;
        if (stringArray.length > 0) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_list_dark, this.arrayReportTypes);
            this.adapter_report_type = arrayAdapter;
            arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list_dark);
            this.spinner_report_types.setAdapter(this.adapter_report_type);
        }
        this.spinner_report_types.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                if (TrackerStatisticsActivity.this.is_user_interact) {
                    int selectedItemPosition2 = TrackerStatisticsActivity.this.spinner_report_types.getSelectedItemPosition();
                    TrackerStatisticsActivity trackerStatisticsActivity3 = TrackerStatisticsActivity.this;
                    trackerStatisticsActivity3.current_report_type = trackerStatisticsActivity3.arrayReportTypes[selectedItemPosition2].trim();
                    int selectedItemPosition3 = TrackerStatisticsActivity.this.spinner_report_duration.getSelectedItemPosition();
                    TrackerStatisticsActivity trackerStatisticsActivity4 = TrackerStatisticsActivity.this;
                    trackerStatisticsActivity4.current_report_duration = trackerStatisticsActivity4.arrayReportDuration[selectedItemPosition3].trim();
                    TrackerStatisticsActivity trackerStatisticsActivity5 = TrackerStatisticsActivity.this;
                    trackerStatisticsActivity5.SetChartByUserIDReportType(trackerStatisticsActivity5.sharedPreferencesUtils.getUserId(), TrackerStatisticsActivity.this.current_report_type, TrackerStatisticsActivity.this.current_report_duration);
                }
            }
        });
    }

    private void SetReportDurationSpinner() {
        String[] stringArray = getResources().getStringArray(R.array.report_duration);
        this.arrayReportDuration = stringArray;
        if (stringArray.length > 0) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_list_dark, this.arrayReportDuration);
            this.adapter_report_duration = arrayAdapter;
            arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list_dark);
            this.spinner_report_duration.setAdapter(this.adapter_report_duration);
        }
        this.spinner_report_duration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                if (TrackerStatisticsActivity.this.is_user_interact) {
                    int selectedItemPosition2 = TrackerStatisticsActivity.this.spinner_report_types.getSelectedItemPosition();
                    TrackerStatisticsActivity trackerStatisticsActivity3 = TrackerStatisticsActivity.this;
                    trackerStatisticsActivity3.current_report_type = trackerStatisticsActivity3.arrayReportTypes[selectedItemPosition2].trim();
                    int selectedItemPosition3 = TrackerStatisticsActivity.this.spinner_report_duration.getSelectedItemPosition();
                    TrackerStatisticsActivity trackerStatisticsActivity4 = TrackerStatisticsActivity.this;
                    trackerStatisticsActivity4.current_report_duration = trackerStatisticsActivity4.arrayReportDuration[selectedItemPosition3].trim();
                    if (TrackerStatisticsActivity.this.current_report_duration.equalsIgnoreCase(TrackerStatisticsActivity.this.getResources().getString(R.string.lbl_custom))) {
                        TrackerStatisticsActivity.this.CustomDurationDialog();
                    } else {
                        TrackerStatisticsActivity.this.GetDefaultSpinnerValues();
                    }
                }
            }
        });
    }


    private void CustomDurationDialog() {
        final Dialog dialog = new Dialog(this, R.style.TransparentBackground);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_custom_chart_duration);
        dialog.setCancelable(false);
        Button button = dialog.findViewById(R.id.dialog_duration_btn_continue);
        Button button2 = dialog.findViewById(R.id.dialog_duration_btn_cancel);
        final TextView textView = dialog.findViewById(R.id.dialog_duration_txt_start_date);
        final TextView textView2 = dialog.findViewById(R.id.dialog_duration_txt_end_date);
        button.setText(getResources().getString(R.string.lbl_done));
        button2.setText(getResources().getString(R.string.lbl_cancel));
        dialog.findViewById(R.id.dialog_duration_rel_start_date).setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                Calendar instance = Calendar.getInstance();
                instance.set(5, instance.getActualMinimum(5));
                TrackerStatisticsActivity.this.start_date_year = instance.get(1);
                TrackerStatisticsActivity.this.start_date_month = instance.get(2);
                TrackerStatisticsActivity.this.start_date_day = instance.get(5);
                new DatePickerDialog(TrackerStatisticsActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {


                    public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                        try {
                            textView.setText(new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("dd/MM/yyyy").parse((i3 + "/" + (i2 + 1) + "/" + i).trim())));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, TrackerStatisticsActivity.this.start_date_year, TrackerStatisticsActivity.this.start_date_month, TrackerStatisticsActivity.this.start_date_day).show();
            }
        });
        dialog.findViewById(R.id.dialog_duration_rel_end_date).setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                Calendar instance = Calendar.getInstance();
                TrackerStatisticsActivity.this.start_date_year = instance.get(1);
                TrackerStatisticsActivity.this.start_date_month = instance.get(2);
                TrackerStatisticsActivity.this.start_date_day = instance.get(5);
                new DatePickerDialog(TrackerStatisticsActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {


                    public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                        try {
                            textView2.setText(new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("dd/MM/yyyy").parse((i3 + "/" + (i2 + 1) + "/" + i).trim())));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, TrackerStatisticsActivity.this.start_date_year, TrackerStatisticsActivity.this.start_date_month, TrackerStatisticsActivity.this.start_date_day).show();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                TrackerStatisticsActivity.this.custom_start_date = textView.getText().toString().trim();
                TrackerStatisticsActivity.this.custom_end_date = textView2.getText().toString().trim();
                TrackerStatisticsActivity.this.GetDefaultSpinnerValues();
                dialog.dismiss();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                TrackerStatisticsActivity.this.spinner_report_duration.setSelection(0);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void SetBloodCountAllChart(List<BloodCountChartData> bloodCountChartData, float RBC) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        instance.get(1);
        instance.get(2);
        instance.get(5);
        ArrayList<BloodCountChartData> arrayList = (ArrayList) bloodCountChartData;
        if (arrayList.size() == 0) {
            this.lbl_no_data.setVisibility(View.VISIBLE);
            this.lbl_no_data.setText("No data available for All!");
        } else {
            this.lbl_no_data.setVisibility(View.GONE);
        }
        List<BarEntry> GetBloodCountEntries = GetBloodCountEntries(arrayList);
        this.dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(GetBloodCountEntries, this.blood_count_legend_text);
        barDataSet.setColor(getResources().getColor(R.color.chart_bar_legend_color));
        barDataSet.setValueTextColor(getResources().getColor(R.color.chart_label_color));
        barDataSet.setValueTextSize(10.0f);
        barDataSet.setGradientColor(this.chart_bottom_color, this.chart_top_color);
        this.dataSets.add(barDataSet);
        BarData barData = new BarData(this.dataSets);
        this.graph_view_all.setData(barData);
        this.graph_view_all.getAxisLeft().setAxisMinimum(0.0f);
        this.graph_view_all.getDescription().setEnabled(false);
        this.graph_view_all.getAxisRight().setAxisMinimum(0.0f);
        this.graph_view_all.setDrawBarShadow(false);
        this.graph_view_all.setDrawValueAboveBar(true);
        this.graph_view_all.setMaxVisibleValueCount(10);
        this.graph_view_all.setPinchZoom(false);
        this.graph_view_all.setDrawGridBackground(false);
        this.graph_view_all.setExtraBottomOffset(5.0f);
        this.graph_view_all.setDrawBorders(false);
        MyMarkerView myMarkerView = new MyMarkerView(this, R.layout.custom_marker_view);
        myMarkerView.setChartView(this.graph_view_all);
        this.graph_view_all.setMarker(myMarkerView);
        this.graph_view_all.getLegend().setEnabled(this.is_chart_legend_visible);
        Legend legend = this.graph_view_all.getLegend();
        legend.setTypeface(this.tf_quick_bold);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(this.graph_legend_text_size);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setYOffset(0.0f);
        legend.setXOffset(0.0f);
        XAxis xAxis = this.graph_view_all.getXAxis();
        xAxis.setTypeface(this.tf_quick_bold);
        xAxis.setGranularity(1.0f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(0.0f);
        xAxis.setTextSize(this.graph_x_axis_text_size);
        xAxis.setTextColor(getResources().getColor(R.color.axis_label_color));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum((float) GetBloodCountEntries(arrayList).size());
        xAxis.setCenterAxisLabels(true);
        ArrayList arrayList2 = new ArrayList();
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            arrayList2.add(arrayList.get(i2).date.trim());
        }
        this.graph_view_all.getXAxis().setValueFormatter(new IndexAxisValueFormatter(arrayList2));
        YAxis axisLeft = this.graph_view_all.getAxisLeft();
        axisLeft.removeAllLimitLines();
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeft.setTypeface(this.tf_quick_bold);
        axisLeft.setTextSize(this.graph_left_axis_text_size);
        axisLeft.setTextColor(getResources().getColor(R.color.axis_label_color));
        axisLeft.setDrawGridLines(false);
//        axisLeft.setAxisMaximum((float) (this.SQLite_health_tracker.GetMaxBloodCountValue(i) + this.extra_y_axis_value));
        axisLeft.setAxisMaximum((float) (RBC + this.extra_y_axis_value));
        this.graph_view_all.getAxisRight().setEnabled(false);
        SetBloodCountBarWidth(this.graph_view_all, barData, arrayList);
        this.graph_view_all.invalidate();
    }

    private void SetBloodCountTodayChart(List<BloodCountChartData> bloodCountChartData, float RBC) {

//        ArrayList<BloodCountChartData> arrayList = (ArrayList) this.SQLite_health_tracker.GetBloodCountChartTodayData(i, i3, instance.get(2) + 1, i2);
        ArrayList<BloodCountChartData> arrayList = (ArrayList) bloodCountChartData;
        if (arrayList.size() == 0) {
            this.lbl_no_data.setVisibility(View.VISIBLE);
            this.lbl_no_data.setText("No data available for Today!");
        } else {
            this.lbl_no_data.setVisibility(View.GONE);
        }
        List<BarEntry> GetBloodCountEntries = GetBloodCountEntries(arrayList);
        this.dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(GetBloodCountEntries, this.blood_count_legend_text);
        barDataSet.setColor(getResources().getColor(R.color.chart_bar_legend_color));
        barDataSet.setValueTextColor(getResources().getColor(R.color.chart_label_color));
        barDataSet.setValueTextSize(10.0f);
        barDataSet.setGradientColor(this.chart_bottom_color, this.chart_top_color);
        this.dataSets.add(barDataSet);
        BarData barData = new BarData(this.dataSets);
        this.graph_view_today.setData(barData);
        this.graph_view_today.getAxisLeft().setAxisMinimum(0.0f);
        this.graph_view_today.getDescription().setEnabled(false);
        this.graph_view_today.getAxisRight().setAxisMinimum(0.0f);
        this.graph_view_today.setDrawBarShadow(false);
        this.graph_view_today.setDrawValueAboveBar(true);
        this.graph_view_today.setMaxVisibleValueCount(10);
        this.graph_view_today.setPinchZoom(false);
        this.graph_view_today.setDrawGridBackground(false);
        this.graph_view_today.setExtraBottomOffset(5.0f);
        this.graph_view_today.setDrawBorders(false);
        MyMarkerView myMarkerView = new MyMarkerView(this, R.layout.custom_marker_view);
        myMarkerView.setChartView(this.graph_view_today);
        this.graph_view_today.setMarker(myMarkerView);
        this.graph_view_today.getLegend().setEnabled(this.is_chart_legend_visible);
        Legend legend = this.graph_view_today.getLegend();
        legend.setTypeface(this.tf_quick_bold);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(this.graph_legend_text_size);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setYOffset(0.0f);
        legend.setXOffset(0.0f);
        XAxis xAxis = this.graph_view_today.getXAxis();
        xAxis.setTypeface(this.tf_quick_bold);
        xAxis.setGranularity(1.0f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(0.0f);
        xAxis.setTextSize(this.graph_x_axis_text_size);
        xAxis.setTextColor(getResources().getColor(R.color.axis_label_color));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum((float) GetBloodCountEntries(arrayList).size());
        xAxis.setCenterAxisLabels(true);
        ArrayList arrayList2 = new ArrayList();
        for (int i4 = 0; i4 < arrayList.size(); i4++) {
            try {
                arrayList2.add(new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("yyyy-MM-dd hh:mm a").parse(String.valueOf(arrayList.get(i4).dateTime))).trim());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        this.graph_view_today.getXAxis().setValueFormatter(new IndexAxisValueFormatter(arrayList2));
        YAxis axisLeft = this.graph_view_today.getAxisLeft();
        axisLeft.removeAllLimitLines();
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeft.setTypeface(this.tf_quick_bold);
        axisLeft.setTextSize(this.graph_left_axis_text_size);
        axisLeft.setTextColor(getResources().getColor(R.color.axis_label_color));
        axisLeft.setDrawGridLines(false);
        axisLeft.setAxisMaximum((float) (RBC + this.extra_y_axis_value));
        this.graph_view_today.getAxisRight().setEnabled(false);
        SetBloodCountBarWidth(this.graph_view_today, barData, arrayList);
        this.graph_view_today.invalidate();
    }

    private void SetBloodCountMonthlyChart(List<BloodCountChartData> bloodCountChartData, float RBC) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        int i2 = instance.get(1);
        instance.get(5);
        ArrayList<BloodCountChartData> arrayList = (ArrayList) bloodCountChartData;
//                (ArrayList) this.SQLite_health_tracker.GetBloodCountChartMonthlyData(i, instance.get(2) + 1, i2);
        if (arrayList.size() == 0) {
            this.lbl_no_data.setVisibility(View.VISIBLE);
            this.lbl_no_data.setText("No Monthly data available!");
        } else {
            this.lbl_no_data.setVisibility(View.GONE);
        }
        List<BarEntry> GetBloodCountEntries = GetBloodCountEntries(arrayList);
        this.dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(GetBloodCountEntries, this.blood_count_legend_text);
        barDataSet.setColor(getResources().getColor(R.color.chart_bar_legend_color));
        barDataSet.setValueTextColor(getResources().getColor(R.color.chart_label_color));
        barDataSet.setValueTextSize(10.0f);
        barDataSet.setGradientColor(this.chart_bottom_color, this.chart_top_color);
        this.dataSets.add(barDataSet);
        BarData barData = new BarData(this.dataSets);
        this.graph_view_monthly.setData(barData);
        this.graph_view_monthly.getAxisLeft().setAxisMinimum(0.0f);
        this.graph_view_monthly.getDescription().setEnabled(false);
        this.graph_view_monthly.getAxisRight().setAxisMinimum(0.0f);
        this.graph_view_monthly.setDrawBarShadow(false);
        this.graph_view_monthly.setDrawValueAboveBar(true);
        this.graph_view_monthly.setMaxVisibleValueCount(10);
        this.graph_view_monthly.setPinchZoom(false);
        this.graph_view_monthly.setDrawGridBackground(false);
        this.graph_view_monthly.setExtraBottomOffset(20.0f);
        this.graph_view_monthly.setDrawBorders(false);
        MyMarkerView myMarkerView = new MyMarkerView(this, R.layout.custom_marker_view);
        myMarkerView.setChartView(this.graph_view_monthly);
        this.graph_view_monthly.setMarker(myMarkerView);
        this.graph_view_monthly.getLegend().setEnabled(this.is_chart_legend_visible);
        Legend legend = this.graph_view_monthly.getLegend();
        legend.setTypeface(this.tf_quick_bold);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(this.graph_legend_text_size);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setYOffset(0.0f);
        legend.setXOffset(0.0f);
        XAxis xAxis = this.graph_view_monthly.getXAxis();
        xAxis.setTypeface(this.tf_quick_bold);
        xAxis.setGranularity(1.0f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(0.0f);
        xAxis.setTextSize(this.graph_x_axis_text_size);
        xAxis.setTextColor(getResources().getColor(R.color.axis_label_color));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum((float) GetBloodCountEntries(arrayList).size());
        ArrayList arrayList2 = new ArrayList();
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            try {
                Date parse = new SimpleDateFormat("yyyy-MM-dd hh:mm a").parse(String.valueOf(arrayList.get(i3).dateTime));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm");
                String trim = simpleDateFormat.format(parse).trim();
                String trim2 = simpleDateFormat2.format(parse).trim();
                arrayList2.add(trim + " " + trim2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        this.graph_view_monthly.getXAxis().setValueFormatter(new IndexAxisValueFormatter(arrayList2));
        YAxis axisLeft = this.graph_view_monthly.getAxisLeft();
        axisLeft.removeAllLimitLines();
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeft.setTypeface(this.tf_quick_bold);
        axisLeft.setTextSize(this.graph_left_axis_text_size);
        axisLeft.setTextColor(getResources().getColor(R.color.axis_label_color));
        axisLeft.setDrawGridLines(false);
        axisLeft.setAxisMaximum((float) (RBC + this.extra_y_axis_value));
        this.graph_view_monthly.getAxisRight().setEnabled(false);
        SetBloodCountBarWidth(this.graph_view_monthly, barData, arrayList);
        this.graph_view_monthly.invalidate();
    }

    private void SetBloodCountYearlyChart(List<BloodCountChartData> bloodCountChartData, float RBC) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        int i2 = instance.get(1);
        instance.get(2);
        instance.get(5);
        ArrayList<BloodCountChartData> arrayList = (ArrayList) bloodCountChartData;
//                (ArrayList) this.SQLite_health_tracker.GetBloodCountChartYearlyData(i, i2);
        if (arrayList.size() == 0) {
            this.lbl_no_data.setVisibility(View.VISIBLE);
            this.lbl_no_data.setText("No Yearly data available!");
        } else {
            this.lbl_no_data.setVisibility(View.GONE);
        }
        List<BarEntry> GetBloodCountEntries = GetBloodCountEntries(arrayList);
        this.dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(GetBloodCountEntries, this.blood_count_legend_text);
        barDataSet.setColor(getResources().getColor(R.color.chart_bar_legend_color));
        barDataSet.setValueTextColor(getResources().getColor(R.color.chart_label_color));
        barDataSet.setValueTextSize(10.0f);
        barDataSet.setGradientColor(this.chart_bottom_color, this.chart_top_color);
        this.dataSets.add(barDataSet);
        BarData barData = new BarData(this.dataSets);
        this.graph_view_yearly.setData(barData);
        this.graph_view_yearly.getAxisLeft().setAxisMinimum(0.0f);
        this.graph_view_yearly.getDescription().setEnabled(false);
        this.graph_view_yearly.getAxisRight().setAxisMinimum(0.0f);
        this.graph_view_yearly.setDrawBarShadow(false);
        this.graph_view_yearly.setDrawValueAboveBar(true);
        this.graph_view_yearly.setMaxVisibleValueCount(10);
        this.graph_view_yearly.setPinchZoom(false);
        this.graph_view_yearly.setDrawGridBackground(false);
        this.graph_view_yearly.setExtraBottomOffset(5.0f);
        this.graph_view_yearly.setDrawBorders(false);
        MyMarkerView myMarkerView = new MyMarkerView(this, R.layout.custom_marker_view);
        myMarkerView.setChartView(this.graph_view_yearly);
        this.graph_view_yearly.setMarker(myMarkerView);
        this.graph_view_yearly.getLegend().setEnabled(this.is_chart_legend_visible);
        Legend legend = this.graph_view_yearly.getLegend();
        legend.setTypeface(this.tf_quick_bold);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(this.graph_legend_text_size);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setYOffset(0.0f);
        legend.setXOffset(0.0f);
        XAxis xAxis = this.graph_view_yearly.getXAxis();
        xAxis.setTypeface(this.tf_quick_bold);
        xAxis.setGranularity(1.0f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(0.0f);
        xAxis.setTextSize(this.graph_x_axis_text_size);
        xAxis.setTextColor(getResources().getColor(R.color.axis_label_color));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum((float) GetBloodCountEntries(arrayList).size());
        ArrayList arrayList2 = new ArrayList();
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            try {
                Date parse = new SimpleDateFormat("yyyy-MM-dd hh:mm a").parse(String.valueOf(arrayList.get(i3).dateTime));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd" +
                        "");
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm");
                String trim = simpleDateFormat.format(parse).trim();
                simpleDateFormat2.format(parse).trim();
                arrayList2.add(trim);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        this.graph_view_yearly.getXAxis().setValueFormatter(new IndexAxisValueFormatter(arrayList2));
        YAxis axisLeft = this.graph_view_yearly.getAxisLeft();
        axisLeft.removeAllLimitLines();
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeft.setTypeface(this.tf_quick_bold);
        axisLeft.setTextSize(this.graph_left_axis_text_size);
        axisLeft.setTextColor(getResources().getColor(R.color.axis_label_color));
        axisLeft.setDrawGridLines(false);
        axisLeft.setAxisMaximum((float) (RBC + this.extra_y_axis_value));
        this.graph_view_yearly.getAxisRight().setEnabled(false);
        SetBloodCountBarWidth(this.graph_view_yearly, barData, arrayList);
        this.graph_view_yearly.invalidate();
    }

    private void SetBloodCountCustomChart(List<BloodCountChartData> bloodCountChartData, float RBC) {
        ArrayList<BloodCountChartData> arrayList = (ArrayList) bloodCountChartData;
//                this.SQLite_health_tracker.GetBloodCountChartCustomData(i, this.custom_start_date, this.custom_end_date);
        if (arrayList.size() == 0) {
            this.lbl_no_data.setVisibility(View.VISIBLE);
            this.lbl_no_data.setText("No data available for that range!");
        } else {
            this.lbl_no_data.setVisibility(View.GONE);
        }
        List<BarEntry> GetBloodCountEntries = GetBloodCountEntries(arrayList);
        this.dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(GetBloodCountEntries, this.blood_count_legend_text);
        barDataSet.setColor(getResources().getColor(R.color.chart_bar_legend_color));
        barDataSet.setValueTextColor(getResources().getColor(R.color.chart_label_color));
        barDataSet.setValueTextSize(10.0f);
        barDataSet.setGradientColor(this.chart_bottom_color, this.chart_top_color);
        this.dataSets.add(barDataSet);
        BarData barData = new BarData(this.dataSets);
        this.graph_view_custom.setData(barData);
        this.graph_view_custom.getAxisLeft().setAxisMinimum(0.0f);
        this.graph_view_custom.getDescription().setEnabled(false);
        this.graph_view_custom.getAxisRight().setAxisMinimum(0.0f);
        this.graph_view_custom.setDrawBarShadow(false);
        this.graph_view_custom.setDrawValueAboveBar(true);
        this.graph_view_custom.setMaxVisibleValueCount(10);
        this.graph_view_custom.setPinchZoom(false);
        this.graph_view_custom.setDrawGridBackground(false);
        this.graph_view_custom.setExtraBottomOffset(5.0f);
        this.graph_view_custom.setDrawBorders(false);
        MyMarkerView myMarkerView = new MyMarkerView(this, R.layout.custom_marker_view);
        myMarkerView.setChartView(this.graph_view_custom);
        this.graph_view_custom.setMarker(myMarkerView);
        this.graph_view_custom.getLegend().setEnabled(this.is_chart_legend_visible);
        Legend legend = this.graph_view_custom.getLegend();
        legend.setTypeface(this.tf_quick_bold);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(this.graph_legend_text_size);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setYOffset(0.0f);
        legend.setXOffset(0.0f);
        XAxis xAxis = this.graph_view_custom.getXAxis();
        xAxis.setTypeface(this.tf_quick_bold);
        xAxis.setGranularity(1.0f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(0.0f);
        xAxis.setTextSize(this.graph_x_axis_text_size);
        xAxis.setTextColor(getResources().getColor(R.color.axis_label_color));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum((float) GetBloodCountEntries(arrayList).size());
        ArrayList arrayList2 = new ArrayList();
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            try {
                arrayList2.add(arrayList.get(i2).date.trim());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.graph_view_custom.getXAxis().setValueFormatter(new IndexAxisValueFormatter(arrayList2));
        YAxis axisLeft = this.graph_view_custom.getAxisLeft();
        axisLeft.removeAllLimitLines();
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeft.setTypeface(this.tf_quick_bold);
        axisLeft.setTextSize(this.graph_left_axis_text_size);
        axisLeft.setTextColor(getResources().getColor(R.color.axis_label_color));
        axisLeft.setDrawGridLines(false);
        axisLeft.setAxisMaximum((float) (RBC + this.extra_y_axis_value));
        this.graph_view_custom.getAxisRight().setEnabled(false);
        SetBloodCountBarWidth(this.graph_view_custom, barData, arrayList);
        this.graph_view_custom.invalidate();
    }

    private void SetBloodCountBarWidth(BarChart barChart, BarData barData, ArrayList<BloodCountChartData> arrayList) {
        if (this.dataSets.size() > 1) {
            float size = (0.7f / ((float) this.dataSets.size())) - 0.02f;
            this.defaultBarWidth = size;
            if (size >= 0.0f) {
                barData.setBarWidth(size);
            }
            int size2 = GetBloodCountEntries(arrayList).size();
            if (size2 != -1) {
                barChart.getXAxis().setAxisMinimum(0.0f);
                barChart.getXAxis().setAxisMaximum((barChart.getBarData().getGroupWidth(0.3f, 0.02f) * ((float) size2)) + 0.0f);
                barChart.getXAxis().setCenterAxisLabels(true);
            }
            barChart.groupBars(0.0f, 0.3f, 0.02f);
            barChart.invalidate();
            return;
        }
        barData.setBarWidth(this.bar_width);
        barChart.getXAxis().setCenterAxisLabels(false);
        barChart.getXAxis().resetAxisMaximum();
        barChart.getXAxis().resetAxisMinimum();
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.invalidate();
    }

    private List<BarEntry> GetBloodCountEntries(ArrayList<BloodCountChartData> arrayList) {
        ArrayList arrayList2 = new ArrayList();
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList2.add(new BarEntry((float) i, arrayList.get(i).rbc_value));
        }
        return arrayList2.subList(0, arrayList.size());
    }

    private void SetBloodPressureAllChart(float MaxBloodPressure, List<BloodPressureChartData> bloodPressureChartDataList) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        instance.get(1);
        instance.get(2);
        instance.get(5);
        ArrayList<BloodPressureChartData> arrayList = (ArrayList) bloodPressureChartDataList;
//                (ArrayList) this.SQLite_health_tracker.GetBloodPressureChartAllData(i);
        if (arrayList.size() == 0) {
            this.lbl_no_data.setVisibility(View.VISIBLE);
            this.lbl_no_data.setText("No data available for All!");
        } else {
            this.lbl_no_data.setVisibility(View.GONE);
        }
        List<BarEntry> GetSystolicEntries = GetSystolicEntries(arrayList);
        List<BarEntry> GetDiastolicEntries = GetDiastolicEntries(arrayList);
        this.dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(GetSystolicEntries, this.systolic_legend_text);
        barDataSet.setColor(getResources().getColor(R.color.systolic_bar_legend_color));
        barDataSet.setValueTextColor(getResources().getColor(R.color.systolic_bar_legend_color));
        barDataSet.setValueTextSize(10.0f);
        barDataSet.setGradientColor(this.chart_bottom_color_1, this.chart_top_color_1);
        BarDataSet barDataSet2 = new BarDataSet(GetDiastolicEntries, this.diastolic_legend_text);
        barDataSet2.setColors(getResources().getColor(R.color.diastolic_bar_legend_color));
        barDataSet2.setValueTextColor(getResources().getColor(R.color.diastolic_bar_legend_color));
        barDataSet2.setValueTextSize(10.0f);
        barDataSet2.setGradientColor(this.chart_bottom_color_2, this.chart_top_color_2);
        this.dataSets.add(barDataSet);
        this.dataSets.add(barDataSet2);
        BarData barData = new BarData(this.dataSets);
        this.graph_view_all.setData(barData);
        this.graph_view_all.getAxisLeft().setAxisMinimum(0.0f);
        this.graph_view_all.getDescription().setEnabled(false);
        this.graph_view_all.getAxisRight().setAxisMinimum(0.0f);
        this.graph_view_all.setDrawBarShadow(false);
        this.graph_view_all.setDrawValueAboveBar(true);
        this.graph_view_all.setMaxVisibleValueCount(10);
        this.graph_view_all.setPinchZoom(false);
        this.graph_view_all.setDrawGridBackground(false);
        this.graph_view_all.setExtraBottomOffset(5.0f);
        this.graph_view_all.setDrawBorders(false);
        MyMarkerView myMarkerView = new MyMarkerView(this, R.layout.custom_marker_view);
        myMarkerView.setChartView(this.graph_view_all);
        this.graph_view_all.setMarker(myMarkerView);
        this.graph_view_all.getLegend().setEnabled(this.is_chart_legend_visible);
        Legend legend = this.graph_view_all.getLegend();
        legend.setTypeface(this.tf_quick_bold);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(this.graph_legend_text_size);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setYOffset(0.0f);
        legend.setXOffset(0.0f);
        XAxis xAxis = this.graph_view_all.getXAxis();
        xAxis.setTypeface(this.tf_quick_bold);
        xAxis.setGranularity(1.0f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(0.0f);
        xAxis.setTextSize(this.graph_x_axis_text_size);
        xAxis.setTextColor(getResources().getColor(R.color.axis_label_color));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum((float) GetSystolicEntries(arrayList).size());
        ArrayList arrayList2 = new ArrayList();
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            arrayList2.add(arrayList.get(i2).result.trim());
        }
        this.graph_view_all.getXAxis().setValueFormatter(new IndexAxisValueFormatter(arrayList2));
        YAxis axisLeft = this.graph_view_all.getAxisLeft();
        axisLeft.removeAllLimitLines();
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeft.setTypeface(this.tf_quick_bold);
        axisLeft.setTextSize(this.graph_left_axis_text_size);
        axisLeft.setTextColor(getResources().getColor(R.color.axis_label_color));
        axisLeft.setDrawGridLines(false);
//        axisLeft.setAxisMaximum((float) (this.SQLite_health_tracker.GetMaxBloodPressureValue(i) + this.extra_y_axis_value));
        axisLeft.setAxisMaximum((float) (MaxBloodPressure + this.extra_y_axis_value));
        this.graph_view_all.getAxisRight().setEnabled(false);
        SetBloodPressureBarWidth(this.graph_view_all, barData, arrayList);
        this.graph_view_all.invalidate();
    }

    private void SetBloodPressureTodayChart(float MaxBloodPressure, List<BloodPressureChartData> bloodPressureChartDataList) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        int i2 = instance.get(1);
        int i3 = instance.get(5);
        ArrayList<BloodPressureChartData> arrayList = (ArrayList) bloodPressureChartDataList;
//                (ArrayList) this.SQLite_health_tracker.GetBloodPressureChartTodayData(i, i3, instance.get(2) + 1, i2);
        if (arrayList.size() == 0) {
            this.lbl_no_data.setVisibility(View.VISIBLE);
            this.lbl_no_data.setText("No data available for Today!");
        } else {
            this.lbl_no_data.setVisibility(View.GONE);
        }
        List<BarEntry> GetSystolicEntries = GetSystolicEntries(arrayList);
        List<BarEntry> GetDiastolicEntries = GetDiastolicEntries(arrayList);
        this.dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(GetSystolicEntries, this.systolic_legend_text);
        barDataSet.setColor(getResources().getColor(R.color.systolic_bar_legend_color));
        barDataSet.setValueTextColor(getResources().getColor(R.color.systolic_bar_legend_color));
        barDataSet.setValueTextSize(10.0f);
        barDataSet.setGradientColor(this.chart_bottom_color_1, this.chart_top_color_1);
        BarDataSet barDataSet2 = new BarDataSet(GetDiastolicEntries, this.diastolic_legend_text);
        barDataSet2.setColors(getResources().getColor(R.color.diastolic_bar_legend_color));
        barDataSet2.setValueTextColor(getResources().getColor(R.color.diastolic_bar_legend_color));
        barDataSet2.setValueTextSize(10.0f);
        barDataSet2.setGradientColor(this.chart_bottom_color_2, this.chart_top_color_2);
        this.dataSets.add(barDataSet);
        this.dataSets.add(barDataSet2);
        BarData barData = new BarData(this.dataSets);
        this.graph_view_today.setData(barData);
        this.graph_view_today.getAxisLeft().setAxisMinimum(0.0f);
        this.graph_view_today.getDescription().setEnabled(false);
        this.graph_view_today.getAxisRight().setAxisMinimum(0.0f);
        this.graph_view_today.setDrawBarShadow(false);
        this.graph_view_today.setDrawValueAboveBar(true);
        this.graph_view_today.setMaxVisibleValueCount(10);
        this.graph_view_today.setPinchZoom(false);
        this.graph_view_today.setDrawGridBackground(false);
        this.graph_view_today.setExtraBottomOffset(5.0f);
        this.graph_view_today.setDrawBorders(false);
        MyMarkerView myMarkerView = new MyMarkerView(this, R.layout.custom_marker_view);
        myMarkerView.setChartView(this.graph_view_today);
        this.graph_view_today.setMarker(myMarkerView);
        this.graph_view_today.getLegend().setEnabled(this.is_chart_legend_visible);
        Legend legend = this.graph_view_today.getLegend();
        legend.setTypeface(this.tf_quick_bold);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(this.graph_legend_text_size);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setYOffset(0.0f);
        legend.setXOffset(0.0f);
        XAxis xAxis = this.graph_view_today.getXAxis();
        xAxis.setTypeface(this.tf_quick_bold);
        xAxis.setGranularity(1.0f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(0.0f);
        xAxis.setTextSize(this.graph_x_axis_text_size);
        xAxis.setTextColor(getResources().getColor(R.color.axis_label_color));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum((float) GetSystolicEntries(arrayList).size());
        ArrayList arrayList2 = new ArrayList();
        for (int i4 = 0; i4 < arrayList.size(); i4++) {
            try {
                arrayList2.add(new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("yyyy-MM-dd hh:mm a").parse(String.valueOf(arrayList.get(i4).dateTime))).trim());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        this.graph_view_today.getXAxis().setValueFormatter(new IndexAxisValueFormatter(arrayList2));
        YAxis axisLeft = this.graph_view_today.getAxisLeft();
        axisLeft.removeAllLimitLines();
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeft.setTypeface(this.tf_quick_bold);
        axisLeft.setTextSize(this.graph_left_axis_text_size);
        axisLeft.setTextColor(getResources().getColor(R.color.axis_label_color));
        axisLeft.setDrawGridLines(false);
        axisLeft.setAxisMaximum((float) (MaxBloodPressure + this.extra_y_axis_value));
        this.graph_view_today.getAxisRight().setEnabled(false);
        SetBloodPressureBarWidth(this.graph_view_today, barData, arrayList);
        this.graph_view_today.invalidate();
    }

    private void SetBloodPressureMonthlyChart(float MaxBloodPressure, List<BloodPressureChartData> bloodPressureChartDataList) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        int i2 = instance.get(1);
        instance.get(5);
        ArrayList<BloodPressureChartData> arrayList = (ArrayList) bloodPressureChartDataList;
//                (ArrayList) this.SQLite_health_tracker.GetBloodPressureChartMonthlyData(i, instance.get(2) + 1, i2);
        if (arrayList.size() == 0) {
            this.lbl_no_data.setVisibility(View.VISIBLE);
            this.lbl_no_data.setText("No Monthly data available!");
        } else {
            this.lbl_no_data.setVisibility(View.GONE);
        }
        List<BarEntry> GetSystolicEntries = GetSystolicEntries(arrayList);
        List<BarEntry> GetDiastolicEntries = GetDiastolicEntries(arrayList);
        this.dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(GetSystolicEntries, this.systolic_legend_text);
        barDataSet.setColor(getResources().getColor(R.color.systolic_bar_legend_color));
        barDataSet.setValueTextColor(getResources().getColor(R.color.systolic_bar_legend_color));
        barDataSet.setValueTextSize(10.0f);
        barDataSet.setGradientColor(this.chart_bottom_color_1, this.chart_top_color_1);
        BarDataSet barDataSet2 = new BarDataSet(GetDiastolicEntries, this.diastolic_legend_text);
        barDataSet2.setColors(getResources().getColor(R.color.diastolic_bar_legend_color));
        barDataSet2.setValueTextColor(getResources().getColor(R.color.diastolic_bar_legend_color));
        barDataSet2.setValueTextSize(10.0f);
        barDataSet2.setGradientColor(this.chart_bottom_color_2, this.chart_top_color_2);
        this.dataSets.add(barDataSet);
        this.dataSets.add(barDataSet2);
        BarData barData = new BarData(this.dataSets);
        this.graph_view_monthly.setData(barData);
        this.graph_view_monthly.getAxisLeft().setAxisMinimum(0.0f);
        this.graph_view_monthly.getDescription().setEnabled(false);
        this.graph_view_monthly.getAxisRight().setAxisMinimum(0.0f);
        this.graph_view_monthly.setDrawBarShadow(false);
        this.graph_view_monthly.setDrawValueAboveBar(true);
        this.graph_view_monthly.setMaxVisibleValueCount(10);
        this.graph_view_monthly.setPinchZoom(false);
        this.graph_view_monthly.setDrawGridBackground(false);
        this.graph_view_monthly.setExtraBottomOffset(20.0f);
        this.graph_view_monthly.setDrawBorders(false);
        MyMarkerView myMarkerView = new MyMarkerView(this, R.layout.custom_marker_view);
        myMarkerView.setChartView(this.graph_view_monthly);
        this.graph_view_monthly.setMarker(myMarkerView);
        this.graph_view_monthly.getLegend().setEnabled(this.is_chart_legend_visible);
        Legend legend = this.graph_view_monthly.getLegend();
        legend.setTypeface(this.tf_quick_bold);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(this.graph_legend_text_size);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setYOffset(0.0f);
        legend.setXOffset(0.0f);
        XAxis xAxis = this.graph_view_monthly.getXAxis();
        xAxis.setTypeface(this.tf_quick_bold);
        xAxis.setGranularity(1.0f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(0.0f);
        xAxis.setTextSize(this.graph_x_axis_text_size);
        xAxis.setTextColor(getResources().getColor(R.color.axis_label_color));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum((float) GetSystolicEntries(arrayList).size());
        ArrayList arrayList2 = new ArrayList();
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            try {
                Date parse = new SimpleDateFormat("yyyy-MM-dd hh:mm a").parse(String.valueOf(arrayList.get(i3).dateTime));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy");
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm");
                String trim = simpleDateFormat.format(parse).trim();
                String trim2 = simpleDateFormat2.format(parse).trim();
                arrayList2.add(trim + " " + trim2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        this.graph_view_monthly.getXAxis().setValueFormatter(new IndexAxisValueFormatter(arrayList2));
        YAxis axisLeft = this.graph_view_monthly.getAxisLeft();
        axisLeft.removeAllLimitLines();
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeft.setTypeface(this.tf_quick_bold);
        axisLeft.setTextSize(this.graph_left_axis_text_size);
        axisLeft.setTextColor(getResources().getColor(R.color.axis_label_color));
        axisLeft.setDrawGridLines(false);
        axisLeft.setAxisMaximum((float) (MaxBloodPressure + this.extra_y_axis_value));
//        axisLeft.setAxisMaximum((float) (this.SQLite_health_tracker.GetMaxBloodPressureValue(i) + this.extra_y_axis_value));
        this.graph_view_monthly.getAxisRight().setEnabled(false);
        SetBloodPressureBarWidth(this.graph_view_monthly, barData, arrayList);
        this.graph_view_monthly.invalidate();
    }

    private void SetBloodPressureYearlyChart(float MaxBloodPressure, List<BloodPressureChartData> bloodPressureChartDataList) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        int i2 = instance.get(1);
        instance.get(2);
        instance.get(5);
        ArrayList<BloodPressureChartData> arrayList = (ArrayList) bloodPressureChartDataList;
//                (ArrayList) this.SQLite_health_tracker.GetBloodPressureChartYearlyData(i, i2);
        if (arrayList.size() == 0) {
            this.lbl_no_data.setVisibility(View.VISIBLE);
            this.lbl_no_data.setText("No Yearly data available!");
        } else {
            this.lbl_no_data.setVisibility(View.GONE);
        }
        List<BarEntry> GetSystolicEntries = GetSystolicEntries(arrayList);
        List<BarEntry> GetDiastolicEntries = GetDiastolicEntries(arrayList);
        this.dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(GetSystolicEntries, this.systolic_legend_text);
        barDataSet.setColor(getResources().getColor(R.color.systolic_bar_legend_color));
        barDataSet.setValueTextColor(getResources().getColor(R.color.systolic_bar_legend_color));
        barDataSet.setValueTextSize(10.0f);
        barDataSet.setGradientColor(this.chart_bottom_color_1, this.chart_top_color_1);
        BarDataSet barDataSet2 = new BarDataSet(GetDiastolicEntries, this.diastolic_legend_text);
        barDataSet2.setColors(getResources().getColor(R.color.diastolic_bar_legend_color));
        barDataSet2.setValueTextColor(getResources().getColor(R.color.diastolic_bar_legend_color));
        barDataSet2.setValueTextSize(10.0f);
        barDataSet2.setGradientColor(this.chart_bottom_color_2, this.chart_top_color_2);
        this.dataSets.add(barDataSet);
        this.dataSets.add(barDataSet2);
        BarData barData = new BarData(this.dataSets);
        this.graph_view_yearly.setData(barData);
        this.graph_view_yearly.getAxisLeft().setAxisMinimum(0.0f);
        this.graph_view_yearly.getDescription().setEnabled(false);
        this.graph_view_yearly.getAxisRight().setAxisMinimum(0.0f);
        this.graph_view_yearly.setDrawBarShadow(false);
        this.graph_view_yearly.setDrawValueAboveBar(true);
        this.graph_view_yearly.setMaxVisibleValueCount(10);
        this.graph_view_yearly.setPinchZoom(false);
        this.graph_view_yearly.setDrawGridBackground(false);
        this.graph_view_yearly.setExtraBottomOffset(5.0f);
        this.graph_view_yearly.setDrawBorders(false);
        MyMarkerView myMarkerView = new MyMarkerView(this, R.layout.custom_marker_view);
        myMarkerView.setChartView(this.graph_view_yearly);
        this.graph_view_yearly.setMarker(myMarkerView);
        this.graph_view_yearly.getLegend().setEnabled(this.is_chart_legend_visible);
        Legend legend = this.graph_view_yearly.getLegend();
        legend.setTypeface(this.tf_quick_bold);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(this.graph_legend_text_size);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setYOffset(0.0f);
        legend.setXOffset(0.0f);
        XAxis xAxis = this.graph_view_yearly.getXAxis();
        xAxis.setTypeface(this.tf_quick_bold);
        xAxis.setGranularity(1.0f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(0.0f);
        xAxis.setTextSize(this.graph_x_axis_text_size);
        xAxis.setTextColor(getResources().getColor(R.color.axis_label_color));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum((float) GetSystolicEntries(arrayList).size());
        ArrayList arrayList2 = new ArrayList();
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            try {
                Date parse = new SimpleDateFormat("yyyy-MM-dd hh:mm a").parse(String.valueOf(arrayList.get(i3).dateTime));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm");
                String trim = simpleDateFormat.format(parse).trim();
                simpleDateFormat2.format(parse).trim();
                arrayList2.add(trim);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        this.graph_view_yearly.getXAxis().setValueFormatter(new IndexAxisValueFormatter(arrayList2));
        YAxis axisLeft = this.graph_view_yearly.getAxisLeft();
        axisLeft.removeAllLimitLines();
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeft.setTypeface(this.tf_quick_bold);
        axisLeft.setTextSize(this.graph_left_axis_text_size);
        axisLeft.setTextColor(getResources().getColor(R.color.axis_label_color));
        axisLeft.setDrawGridLines(false);
        axisLeft.setAxisMaximum((float) (MaxBloodPressure + this.extra_y_axis_value));
        this.graph_view_yearly.getAxisRight().setEnabled(false);
        SetBloodPressureBarWidth(this.graph_view_yearly, barData, arrayList);
        this.graph_view_yearly.invalidate();
    }

    private void SetBloodPressureCustomChart(float MaxBloodPressure, List<BloodPressureChartData> bloodPressureChartDataList) {
        ArrayList<BloodPressureChartData> arrayList = (ArrayList) bloodPressureChartDataList;
//                this.SQLite_health_tracker.GetBloodPressureChartCustomData(i, this.custom_start_date, this.custom_end_date);
        if (arrayList.size() == 0) {
            this.lbl_no_data.setVisibility(View.VISIBLE);
            this.lbl_no_data.setText("No Yearly data available!");
        } else {
            this.lbl_no_data.setVisibility(View.GONE);
        }
        List<BarEntry> GetSystolicEntries = GetSystolicEntries(arrayList);
        List<BarEntry> GetDiastolicEntries = GetDiastolicEntries(arrayList);
        this.dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(GetSystolicEntries, this.systolic_legend_text);
        barDataSet.setColor(getResources().getColor(R.color.systolic_bar_legend_color));
        barDataSet.setValueTextColor(getResources().getColor(R.color.systolic_bar_legend_color));
        barDataSet.setValueTextSize(10.0f);
        barDataSet.setGradientColor(this.chart_bottom_color_1, this.chart_top_color_1);
        BarDataSet barDataSet2 = new BarDataSet(GetDiastolicEntries, this.diastolic_legend_text);
        barDataSet2.setColors(getResources().getColor(R.color.diastolic_bar_legend_color));
        barDataSet2.setValueTextColor(getResources().getColor(R.color.diastolic_bar_legend_color));
        barDataSet2.setValueTextSize(10.0f);
        barDataSet2.setGradientColor(this.chart_bottom_color_2, this.chart_top_color_2);
        this.dataSets.add(barDataSet);
        this.dataSets.add(barDataSet2);
        BarData barData = new BarData(this.dataSets);
        this.graph_view_custom.setData(barData);
        this.graph_view_custom.getAxisLeft().setAxisMinimum(0.0f);
        this.graph_view_custom.getDescription().setEnabled(false);
        this.graph_view_custom.getAxisRight().setAxisMinimum(0.0f);
        this.graph_view_custom.setDrawBarShadow(false);
        this.graph_view_custom.setDrawValueAboveBar(true);
        this.graph_view_custom.setMaxVisibleValueCount(10);
        this.graph_view_custom.setPinchZoom(false);
        this.graph_view_custom.setDrawGridBackground(false);
        this.graph_view_custom.setExtraBottomOffset(5.0f);
        this.graph_view_custom.setDrawBorders(false);
        MyMarkerView myMarkerView = new MyMarkerView(this, R.layout.custom_marker_view);
        myMarkerView.setChartView(this.graph_view_custom);
        this.graph_view_custom.setMarker(myMarkerView);
        this.graph_view_custom.getLegend().setEnabled(this.is_chart_legend_visible);
        Legend legend = this.graph_view_custom.getLegend();
        legend.setTypeface(this.tf_quick_bold);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(this.graph_legend_text_size);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setYOffset(0.0f);
        legend.setXOffset(0.0f);
        XAxis xAxis = this.graph_view_custom.getXAxis();
        xAxis.setTypeface(this.tf_quick_bold);
        xAxis.setGranularity(1.0f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(0.0f);
        xAxis.setTextSize(this.graph_x_axis_text_size);
        xAxis.setTextColor(getResources().getColor(R.color.axis_label_color));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum((float) GetSystolicEntries(arrayList).size());
        ArrayList arrayList2 = new ArrayList();
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            try {
                Date parse = new SimpleDateFormat("yyyy-MM-dd hh:mm a").parse(String.valueOf(arrayList.get(i2).dateTime));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm a");
                String trim = simpleDateFormat.format(parse).trim();
                simpleDateFormat2.format(parse).trim();
                arrayList2.add(trim);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        this.graph_view_custom.getXAxis().setValueFormatter(new IndexAxisValueFormatter(arrayList2));
        YAxis axisLeft = this.graph_view_custom.getAxisLeft();
        axisLeft.removeAllLimitLines();
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeft.setTypeface(this.tf_quick_bold);
        axisLeft.setTextSize(this.graph_left_axis_text_size);
        axisLeft.setTextColor(getResources().getColor(R.color.axis_label_color));
        axisLeft.setDrawGridLines(false);
        axisLeft.setAxisMaximum((float) (MaxBloodPressure + this.extra_y_axis_value));
        this.graph_view_custom.getAxisRight().setEnabled(false);
        SetBloodPressureBarWidth(this.graph_view_custom, barData, arrayList);
        this.graph_view_custom.invalidate();
    }

    private void SetBloodPressureBarWidth(BarChart barChart, BarData barData, ArrayList<BloodPressureChartData> arrayList) {
        if (this.dataSets.size() > 1) {
            float size = (0.7f / ((float) this.dataSets.size())) - 0.02f;
            this.defaultBarWidth = size;
            if (size >= 0.0f) {
                barData.setBarWidth(size);
            }
            int size2 = GetSystolicEntries(arrayList).size();
            if (size2 != -1) {
                barChart.getXAxis().setAxisMinimum(0.0f);
                barChart.getXAxis().setAxisMaximum((barChart.getBarData().getGroupWidth(0.3f, 0.02f) * ((float) size2)) + 0.0f);
                barChart.getXAxis().setCenterAxisLabels(true);
            }
            barChart.groupBars(0.0f, 0.3f, 0.02f);
            barChart.invalidate();
            return;
        }
        barData.setBarWidth(this.bar_width);
        barChart.getXAxis().setCenterAxisLabels(false);
        barChart.getXAxis().resetAxisMaximum();
        barChart.getXAxis().resetAxisMinimum();
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.invalidate();
    }

    private List<BarEntry> GetSystolicEntries(ArrayList<BloodPressureChartData> arrayList) {
        ArrayList arrayList2 = new ArrayList();
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList2.add(new BarEntry((float) i, (float) arrayList.get(i).systolic_value));
        }
        return arrayList2.subList(0, arrayList.size());
    }

    private List<BarEntry> GetDiastolicEntries(ArrayList<BloodPressureChartData> arrayList) {
        ArrayList arrayList2 = new ArrayList();
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList2.add(new BarEntry((float) i, (float) arrayList.get(i).diastolic_value));
        }
        return arrayList2.subList(0, arrayList.size());
    }

    private void SetBloodSugarAllChart(float i, List<BloodSugarChartData> dataArray) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        instance.get(1);
        instance.get(2);
        instance.get(5);
        ArrayList<BloodSugarChartData> arrayList = (ArrayList) dataArray;
//                this.SQLite_health_tracker.GetBloodSugarChartAllData(i);
        if (arrayList.size() == 0) {
            this.lbl_no_data.setVisibility(View.VISIBLE);
            this.lbl_no_data.setText("No data available for All!");
        } else {
            this.lbl_no_data.setVisibility(View.GONE);
        }
        List<BarEntry> GetBloodSugarEntries = GetBloodSugarEntries(arrayList);
        this.dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(GetBloodSugarEntries, this.blood_sugar_legend_text);
        barDataSet.setColor(getResources().getColor(R.color.chart_bar_legend_color));
        barDataSet.setValueTextColor(getResources().getColor(R.color.chart_label_color));
        barDataSet.setValueTextSize(10.0f);
        barDataSet.setGradientColor(this.chart_bottom_color, this.chart_top_color);
        this.dataSets.add(barDataSet);
        BarData barData = new BarData(this.dataSets);
        this.graph_view_all.setData(barData);
        this.graph_view_all.getAxisLeft().setAxisMinimum(0.0f);
        this.graph_view_all.getDescription().setEnabled(false);
        this.graph_view_all.getAxisRight().setAxisMinimum(0.0f);
        this.graph_view_all.setDrawBarShadow(false);
        this.graph_view_all.setDrawValueAboveBar(true);
        this.graph_view_all.setMaxVisibleValueCount(10);
        this.graph_view_all.setPinchZoom(false);
        this.graph_view_all.setDrawGridBackground(false);
        this.graph_view_all.setExtraBottomOffset(5.0f);
        this.graph_view_all.setDrawBorders(false);
        MyMarkerView myMarkerView = new MyMarkerView(this, R.layout.custom_marker_view);
        myMarkerView.setChartView(this.graph_view_all);
        this.graph_view_all.setMarker(myMarkerView);
        this.graph_view_all.getLegend().setEnabled(this.is_chart_legend_visible);
        Legend legend = this.graph_view_all.getLegend();
        legend.setTypeface(this.tf_quick_bold);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(this.graph_legend_text_size);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setYOffset(0.0f);
        legend.setXOffset(0.0f);
        XAxis xAxis = this.graph_view_all.getXAxis();
        xAxis.setTypeface(this.tf_quick_bold);
        xAxis.setGranularity(1.0f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(0.0f);
        xAxis.setTextSize(this.graph_x_axis_text_size);
        xAxis.setTextColor(getResources().getColor(R.color.axis_label_color));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum((float) GetBloodSugarEntries(arrayList).size());
        xAxis.setCenterAxisLabels(true);
        ArrayList arrayList2 = new ArrayList();
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            arrayList2.add(arrayList.get(i2).date.trim());
        }
        this.graph_view_all.getXAxis().setValueFormatter(new IndexAxisValueFormatter(arrayList2));
        YAxis axisLeft = this.graph_view_all.getAxisLeft();
        axisLeft.removeAllLimitLines();
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeft.setTypeface(this.tf_quick_bold);
        axisLeft.setTextSize(this.graph_left_axis_text_size);
        axisLeft.setTextColor(getResources().getColor(R.color.axis_label_color));
        axisLeft.setDrawGridLines(false);
        axisLeft.setAxisMaximum((float) (i + this.extra_y_axis_value));
        this.graph_view_all.getAxisRight().setEnabled(false);
        SetBloodSugarBarWidth(this.graph_view_all, barData, arrayList);
        this.graph_view_all.invalidate();
    }

    private void SetBloodSugarTodayChart(float i, List<BloodSugarChartData> dataArray) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        int i2 = instance.get(1);
        int i3 = instance.get(5);
        ArrayList<BloodSugarChartData> arrayList = (ArrayList) dataArray;
//                this.SQLite_health_tracker.GetBloodSugarChartTodayData(i, i3, instance.get(2) + 1, i2);
        if (arrayList.size() == 0) {
            this.lbl_no_data.setVisibility(View.VISIBLE);
            this.lbl_no_data.setText("No data available for Today!");
        } else {
            this.lbl_no_data.setVisibility(View.GONE);
        }
        List<BarEntry> GetBloodSugarEntries = GetBloodSugarEntries(arrayList);
        this.dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(GetBloodSugarEntries, this.blood_sugar_legend_text);
        barDataSet.setColor(getResources().getColor(R.color.chart_bar_legend_color));
        barDataSet.setValueTextColor(getResources().getColor(R.color.chart_label_color));
        barDataSet.setValueTextSize(10.0f);
        barDataSet.setGradientColor(this.chart_bottom_color, this.chart_top_color);
        this.dataSets.add(barDataSet);
        BarData barData = new BarData(this.dataSets);
        this.graph_view_today.setData(barData);
        this.graph_view_today.getAxisLeft().setAxisMinimum(0.0f);
        this.graph_view_today.getDescription().setEnabled(false);
        this.graph_view_today.getAxisRight().setAxisMinimum(0.0f);
        this.graph_view_today.setDrawBarShadow(false);
        this.graph_view_today.setDrawValueAboveBar(true);
        this.graph_view_today.setMaxVisibleValueCount(10);
        this.graph_view_today.setPinchZoom(false);
        this.graph_view_today.setDrawGridBackground(false);
        this.graph_view_today.setExtraBottomOffset(5.0f);
        this.graph_view_today.setDrawBorders(false);
        MyMarkerView myMarkerView = new MyMarkerView(this, R.layout.custom_marker_view);
        myMarkerView.setChartView(this.graph_view_today);
        this.graph_view_today.setMarker(myMarkerView);
        this.graph_view_today.getLegend().setEnabled(this.is_chart_legend_visible);
        Legend legend = this.graph_view_today.getLegend();
        legend.setTypeface(this.tf_quick_bold);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(this.graph_legend_text_size);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setYOffset(0.0f);
        legend.setXOffset(0.0f);
        XAxis xAxis = this.graph_view_today.getXAxis();
        xAxis.setTypeface(this.tf_quick_bold);
        xAxis.setGranularity(1.0f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(0.0f);
        xAxis.setTextSize(this.graph_x_axis_text_size);
        xAxis.setTextColor(getResources().getColor(R.color.axis_label_color));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum((float) GetBloodSugarEntries(arrayList).size());
        xAxis.setCenterAxisLabels(true);
        ArrayList arrayList2 = new ArrayList();
        for (int i4 = 0; i4 < arrayList.size(); i4++) {
            try {
                arrayList2.add(new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("yyyy-MM-dd hh:mm a").parse(String.valueOf(arrayList.get(i4).dateTime))).trim());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        this.graph_view_today.getXAxis().setValueFormatter(new IndexAxisValueFormatter(arrayList2));
        YAxis axisLeft = this.graph_view_today.getAxisLeft();
        axisLeft.removeAllLimitLines();
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeft.setTypeface(this.tf_quick_bold);
        axisLeft.setTextSize(this.graph_left_axis_text_size);
        axisLeft.setTextColor(getResources().getColor(R.color.axis_label_color));
        axisLeft.setDrawGridLines(false);
        axisLeft.setAxisMaximum((float) (i + this.extra_y_axis_value));
        this.graph_view_today.getAxisRight().setEnabled(false);
        SetBloodSugarBarWidth(this.graph_view_today, barData, arrayList);
        this.graph_view_today.invalidate();
    }

    private void SetBloodSugarMonthlyChart(float i, List<BloodSugarChartData> dataArray) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        int i2 = instance.get(1);
        instance.get(5);
        ArrayList<BloodSugarChartData> arrayList = (ArrayList) dataArray;
//                this.SQLite_health_tracker.GetBloodSugarChartMonthlyData(i, instance.get(2) + 1, i2);
        if (arrayList.size() == 0) {
            this.lbl_no_data.setVisibility(View.VISIBLE);
            this.lbl_no_data.setText("No Monthly data available!");
        } else {
            this.lbl_no_data.setVisibility(View.GONE);
        }
        List<BarEntry> GetBloodSugarEntries = GetBloodSugarEntries(arrayList);
        this.dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(GetBloodSugarEntries, this.blood_sugar_legend_text);
        barDataSet.setColor(getResources().getColor(R.color.chart_bar_legend_color));
        barDataSet.setValueTextColor(getResources().getColor(R.color.chart_label_color));
        barDataSet.setValueTextSize(10.0f);
        barDataSet.setGradientColor(this.chart_bottom_color, this.chart_top_color);
        this.dataSets.add(barDataSet);
        BarData barData = new BarData(this.dataSets);
        this.graph_view_monthly.setData(barData);
        this.graph_view_monthly.getAxisLeft().setAxisMinimum(0.0f);
        this.graph_view_monthly.getDescription().setEnabled(false);
        this.graph_view_monthly.getAxisRight().setAxisMinimum(0.0f);
        this.graph_view_monthly.setDrawBarShadow(false);
        this.graph_view_monthly.setDrawValueAboveBar(true);
        this.graph_view_monthly.setMaxVisibleValueCount(10);
        this.graph_view_monthly.setPinchZoom(false);
        this.graph_view_monthly.setDrawGridBackground(false);
        this.graph_view_monthly.setExtraBottomOffset(20.0f);
        this.graph_view_monthly.setDrawBorders(false);
        MyMarkerView myMarkerView = new MyMarkerView(this, R.layout.custom_marker_view);
        myMarkerView.setChartView(this.graph_view_monthly);
        this.graph_view_monthly.setMarker(myMarkerView);
        this.graph_view_monthly.getLegend().setEnabled(this.is_chart_legend_visible);
        Legend legend = this.graph_view_monthly.getLegend();
        legend.setTypeface(this.tf_quick_bold);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(this.graph_legend_text_size);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setYOffset(0.0f);
        legend.setXOffset(0.0f);
        XAxis xAxis = this.graph_view_monthly.getXAxis();
        xAxis.setTypeface(this.tf_quick_bold);
        xAxis.setGranularity(1.0f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(0.0f);
        xAxis.setTextSize(this.graph_x_axis_text_size);
        xAxis.setTextColor(getResources().getColor(R.color.axis_label_color));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum((float) GetBloodSugarEntries(arrayList).size());
        ArrayList arrayList2 = new ArrayList();
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            try {
                Date parse = new SimpleDateFormat("yyyy-MM-dd hh:mm a").parse(String.valueOf(arrayList.get(i3).dateTime));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm");
                String trim = simpleDateFormat.format(parse).trim();
                String trim2 = simpleDateFormat2.format(parse).trim();
                arrayList2.add(trim + " " + trim2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        this.graph_view_monthly.getXAxis().setValueFormatter(new IndexAxisValueFormatter(arrayList2));
        YAxis axisLeft = this.graph_view_monthly.getAxisLeft();
        axisLeft.removeAllLimitLines();
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeft.setTypeface(this.tf_quick_bold);
        axisLeft.setTextSize(this.graph_left_axis_text_size);
        axisLeft.setTextColor(getResources().getColor(R.color.axis_label_color));
        axisLeft.setDrawGridLines(false);
        axisLeft.setAxisMaximum((float) (i + this.extra_y_axis_value));
        this.graph_view_monthly.getAxisRight().setEnabled(false);
        SetBloodSugarBarWidth(this.graph_view_monthly, barData, arrayList);
        this.graph_view_monthly.invalidate();
    }

    private void SetBloodSugarYearlyChart(float i, List<BloodSugarChartData> dataArray) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        int i2 = instance.get(1);
        instance.get(2);
        instance.get(5);
        ArrayList<BloodSugarChartData> arrayList = (ArrayList) dataArray;
//                this.SQLite_health_tracker.GetBloodSugarChartYearlyData(i, i2);
        if (arrayList.size() == 0) {
            this.lbl_no_data.setVisibility(View.VISIBLE);
            this.lbl_no_data.setText("No Yearly data available!");
        } else {
            this.lbl_no_data.setVisibility(View.GONE);
        }
        List<BarEntry> GetBloodSugarEntries = GetBloodSugarEntries(arrayList);
        this.dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(GetBloodSugarEntries, this.blood_sugar_legend_text);
        barDataSet.setColor(getResources().getColor(R.color.chart_bar_legend_color));
        barDataSet.setValueTextColor(getResources().getColor(R.color.chart_label_color));
        barDataSet.setValueTextSize(10.0f);
        barDataSet.setGradientColor(this.chart_bottom_color, this.chart_top_color);
        this.dataSets.add(barDataSet);
        BarData barData = new BarData(this.dataSets);
        this.graph_view_yearly.setData(barData);
        this.graph_view_yearly.getAxisLeft().setAxisMinimum(0.0f);
        this.graph_view_yearly.getDescription().setEnabled(false);
        this.graph_view_yearly.getAxisRight().setAxisMinimum(0.0f);
        this.graph_view_yearly.setDrawBarShadow(false);
        this.graph_view_yearly.setDrawValueAboveBar(true);
        this.graph_view_yearly.setMaxVisibleValueCount(10);
        this.graph_view_yearly.setPinchZoom(false);
        this.graph_view_yearly.setDrawGridBackground(false);
        this.graph_view_yearly.setExtraBottomOffset(5.0f);
        this.graph_view_yearly.setDrawBorders(false);
        MyMarkerView myMarkerView = new MyMarkerView(this, R.layout.custom_marker_view);
        myMarkerView.setChartView(this.graph_view_yearly);
        this.graph_view_yearly.setMarker(myMarkerView);
        this.graph_view_yearly.getLegend().setEnabled(this.is_chart_legend_visible);
        Legend legend = this.graph_view_yearly.getLegend();
        legend.setTypeface(this.tf_quick_bold);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(this.graph_legend_text_size);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setYOffset(0.0f);
        legend.setXOffset(0.0f);
        XAxis xAxis = this.graph_view_yearly.getXAxis();
        xAxis.setTypeface(this.tf_quick_bold);
        xAxis.setGranularity(1.0f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(0.0f);
        xAxis.setTextSize(this.graph_x_axis_text_size);
        xAxis.setTextColor(getResources().getColor(R.color.axis_label_color));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum((float) GetBloodSugarEntries(arrayList).size());
        ArrayList arrayList2 = new ArrayList();
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            try {
                Date parse = new SimpleDateFormat("yyyy-MM-dd hh:mm a").parse(String.valueOf(arrayList.get(i3).dateTime));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm");
                String trim = simpleDateFormat.format(parse).trim();
                simpleDateFormat2.format(parse).trim();
                arrayList2.add(trim);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        this.graph_view_yearly.getXAxis().setValueFormatter(new IndexAxisValueFormatter(arrayList2));
        YAxis axisLeft = this.graph_view_yearly.getAxisLeft();
        axisLeft.removeAllLimitLines();
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeft.setTypeface(this.tf_quick_bold);
        axisLeft.setTextSize(this.graph_left_axis_text_size);
        axisLeft.setTextColor(getResources().getColor(R.color.axis_label_color));
        axisLeft.setDrawGridLines(false);
        axisLeft.setAxisMaximum((float) (i + this.extra_y_axis_value));
        this.graph_view_yearly.getAxisRight().setEnabled(false);
        SetBloodSugarBarWidth(this.graph_view_yearly, barData, arrayList);
        this.graph_view_yearly.invalidate();
    }

    private void SetBloodSugarCustomChart(float i, List<BloodSugarChartData> dataArray) {
        ArrayList<BloodSugarChartData> arrayList = (ArrayList) dataArray;
//                this.SQLite_health_tracker.GetBloodSugarChartCustomData(i, this.custom_start_date, this.custom_end_date);
        if (arrayList.size() == 0) {
            this.lbl_no_data.setVisibility(View.VISIBLE);
            this.lbl_no_data.setText("No Yearly data available!");
        } else {
            this.lbl_no_data.setVisibility(View.GONE);
        }
        List<BarEntry> GetBloodSugarEntries = GetBloodSugarEntries(arrayList);
        this.dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(GetBloodSugarEntries, this.blood_sugar_legend_text);
        barDataSet.setColor(getResources().getColor(R.color.chart_bar_legend_color));
        barDataSet.setValueTextColor(getResources().getColor(R.color.chart_label_color));
        barDataSet.setValueTextSize(10.0f);
        barDataSet.setGradientColor(this.chart_bottom_color, this.chart_top_color);
        this.dataSets.add(barDataSet);
        BarData barData = new BarData(this.dataSets);
        this.graph_view_custom.setData(barData);
        this.graph_view_custom.getAxisLeft().setAxisMinimum(0.0f);
        this.graph_view_custom.getDescription().setEnabled(false);
        this.graph_view_custom.getAxisRight().setAxisMinimum(0.0f);
        this.graph_view_custom.setDrawBarShadow(false);
        this.graph_view_custom.setDrawValueAboveBar(true);
        this.graph_view_custom.setMaxVisibleValueCount(10);
        this.graph_view_custom.setPinchZoom(false);
        this.graph_view_custom.setDrawGridBackground(false);
        this.graph_view_custom.setExtraBottomOffset(5.0f);
        this.graph_view_custom.setDrawBorders(false);
        MyMarkerView myMarkerView = new MyMarkerView(this, R.layout.custom_marker_view);
        myMarkerView.setChartView(this.graph_view_custom);
        this.graph_view_custom.setMarker(myMarkerView);
        this.graph_view_custom.getLegend().setEnabled(this.is_chart_legend_visible);
        Legend legend = this.graph_view_custom.getLegend();
        legend.setTypeface(this.tf_quick_bold);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(this.graph_legend_text_size);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setYOffset(0.0f);
        legend.setXOffset(0.0f);
        XAxis xAxis = this.graph_view_custom.getXAxis();
        xAxis.setTypeface(this.tf_quick_bold);
        xAxis.setGranularity(1.0f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(0.0f);
        xAxis.setTextSize(this.graph_x_axis_text_size);
        xAxis.setTextColor(getResources().getColor(R.color.axis_label_color));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum((float) GetBloodSugarEntries(arrayList).size());
        ArrayList arrayList2 = new ArrayList();
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            try {
                arrayList2.add(arrayList.get(i2).date.trim());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.graph_view_custom.getXAxis().setValueFormatter(new IndexAxisValueFormatter(arrayList2));
        YAxis axisLeft = this.graph_view_custom.getAxisLeft();
        axisLeft.removeAllLimitLines();
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeft.setTypeface(this.tf_quick_bold);
        axisLeft.setTextSize(this.graph_left_axis_text_size);
        axisLeft.setTextColor(getResources().getColor(R.color.axis_label_color));
        axisLeft.setDrawGridLines(false);
        axisLeft.setAxisMaximum((float) (i + this.extra_y_axis_value));
        this.graph_view_custom.getAxisRight().setEnabled(false);
        SetBloodSugarBarWidth(this.graph_view_custom, barData, arrayList);
        this.graph_view_custom.invalidate();
    }

    private void SetBloodSugarBarWidth(BarChart barChart, BarData barData, ArrayList<BloodSugarChartData> arrayList) {
        if (this.dataSets.size() > 1) {
            float size = (0.7f / ((float) this.dataSets.size())) - 0.02f;
            this.defaultBarWidth = size;
            if (size >= 0.0f) {
                barData.setBarWidth(size);
            }
            int size2 = GetBloodSugarEntries(arrayList).size();
            if (size2 != -1) {
                barChart.getXAxis().setAxisMinimum(0.0f);
                barChart.getXAxis().setAxisMaximum((barChart.getBarData().getGroupWidth(0.3f, 0.02f) * ((float) size2)) + 0.0f);
                barChart.getXAxis().setCenterAxisLabels(true);
            }
            barChart.groupBars(0.0f, 0.3f, 0.02f);
            barChart.invalidate();
            return;
        }
        barData.setBarWidth(this.bar_width);
        barChart.getXAxis().setCenterAxisLabels(false);
        barChart.getXAxis().resetAxisMaximum();
        barChart.getXAxis().resetAxisMinimum();
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.invalidate();
    }

    private List<BarEntry> GetBloodSugarEntries(ArrayList<BloodSugarChartData> arrayList) {
        ArrayList arrayList2 = new ArrayList();
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList2.add(new BarEntry((float) i, arrayList.get(i).sugar_level));
        }
        return arrayList2.subList(0, arrayList.size());
    }

    private void SetBMIAllChart(List<BMIChartData> list) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        instance.get(1);
        instance.get(2);
        instance.get(5);
        ArrayList<BMIChartData> arrayList = (ArrayList) list;
//                this.SQLite_health_tracker.GetBMIChartAllData(i);
        if (arrayList.size() == 0) {
            this.lbl_no_data.setVisibility(View.VISIBLE);
            this.lbl_no_data.setText("No data available for All!");
        } else {
            this.lbl_no_data.setVisibility(View.GONE);
        }
        List<BarEntry> GetBMIEntries = GetBMIEntries(arrayList);
        this.dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(GetBMIEntries, this.bmi_legend_text);
        barDataSet.setColor(getResources().getColor(R.color.chart_bar_legend_color));
        barDataSet.setValueTextColor(getResources().getColor(R.color.chart_label_color));
        barDataSet.setValueTextSize(10.0f);
        barDataSet.setGradientColor(this.chart_bottom_color, this.chart_top_color);
        this.dataSets.add(barDataSet);
        BarData barData = new BarData(this.dataSets);
        this.graph_view_all.setData(barData);
        this.graph_view_all.getAxisLeft().setAxisMinimum(0.0f);
        this.graph_view_all.getDescription().setEnabled(false);
        this.graph_view_all.getAxisRight().setAxisMinimum(0.0f);
        this.graph_view_all.setDrawBarShadow(false);
        this.graph_view_all.setDrawValueAboveBar(true);
        this.graph_view_all.setMaxVisibleValueCount(10);
        this.graph_view_all.setPinchZoom(false);
        this.graph_view_all.setDrawGridBackground(false);
        this.graph_view_all.setExtraBottomOffset(5.0f);
        this.graph_view_all.setDrawBorders(false);
        MyMarkerView myMarkerView = new MyMarkerView(this, R.layout.custom_marker_view);
        myMarkerView.setChartView(this.graph_view_all);
        this.graph_view_all.setMarker(myMarkerView);
        this.graph_view_all.getLegend().setEnabled(this.is_chart_legend_visible);
        Legend legend = this.graph_view_all.getLegend();
        legend.setTypeface(this.tf_quick_bold);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(this.graph_legend_text_size);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setYOffset(0.0f);
        legend.setXOffset(0.0f);
        XAxis xAxis = this.graph_view_all.getXAxis();
        xAxis.setTypeface(this.tf_quick_bold);
        xAxis.setGranularity(1.0f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(0.0f);
        xAxis.setTextSize(this.graph_x_axis_text_size);
        xAxis.setTextColor(getResources().getColor(R.color.axis_label_color));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum((float) GetBMIEntries(arrayList).size());
        ArrayList arrayList2 = new ArrayList();
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            arrayList2.add(arrayList.get(i2).date.trim());
        }
        this.graph_view_all.getXAxis().setValueFormatter(new IndexAxisValueFormatter(arrayList2));
        YAxis axisLeft = this.graph_view_all.getAxisLeft();
        axisLeft.removeAllLimitLines();
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeft.setTypeface(this.tf_quick_bold);
        axisLeft.setTextSize(this.graph_left_axis_text_size);
        axisLeft.setTextColor(getResources().getColor(R.color.axis_label_color));
        axisLeft.setDrawGridLines(false);
        axisLeft.setAxisMaximum((float) (this.SQLite_health_tracker.GetMaxBMIValue(list) + this.extra_y_axis_value));
        this.graph_view_all.getAxisRight().setEnabled(false);
        SetBMIBarWidth(this.graph_view_all, barData, arrayList);
        this.graph_view_all.invalidate();
    }

    private void SetBMITodayChart(List<BMIChartData> list) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        int i2 = instance.get(1);
        int i3 = instance.get(5);
        ArrayList<BMIChartData> arrayList = (ArrayList) list;
//                this.SQLite_health_tracker.GetBMIChartTodayData(i, i3, instance.get(2) + 1, i2);
        if (arrayList.size() == 0) {
            this.lbl_no_data.setVisibility(View.VISIBLE);
            this.lbl_no_data.setText("No data available for Today!");
        } else {
            this.lbl_no_data.setVisibility(View.GONE);
        }
        List<BarEntry> GetBMIEntries = GetBMIEntries(arrayList);
        this.dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(GetBMIEntries, this.bmi_legend_text);
        barDataSet.setColor(getResources().getColor(R.color.chart_bar_legend_color));
        barDataSet.setValueTextColor(getResources().getColor(R.color.chart_label_color));
        barDataSet.setValueTextSize(10.0f);
        barDataSet.setGradientColor(this.chart_bottom_color, this.chart_top_color);
        this.dataSets.add(barDataSet);
        BarData barData = new BarData(this.dataSets);
        this.graph_view_today.setData(barData);
        this.graph_view_today.getAxisLeft().setAxisMinimum(0.0f);
        this.graph_view_today.getDescription().setEnabled(false);
        this.graph_view_today.getAxisRight().setAxisMinimum(0.0f);
        this.graph_view_today.setDrawBarShadow(false);
        this.graph_view_today.setDrawValueAboveBar(true);
        this.graph_view_today.setMaxVisibleValueCount(10);
        this.graph_view_today.setPinchZoom(false);
        this.graph_view_today.setDrawGridBackground(false);
        this.graph_view_today.setExtraBottomOffset(5.0f);
        this.graph_view_today.setDrawBorders(false);
        MyMarkerView myMarkerView = new MyMarkerView(this, R.layout.custom_marker_view);
        myMarkerView.setChartView(this.graph_view_today);
        this.graph_view_today.setMarker(myMarkerView);
        this.graph_view_today.getLegend().setEnabled(this.is_chart_legend_visible);
        Legend legend = this.graph_view_today.getLegend();
        legend.setTypeface(this.tf_quick_bold);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(this.graph_legend_text_size);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setYOffset(0.0f);
        legend.setXOffset(0.0f);
        XAxis xAxis = this.graph_view_today.getXAxis();
        xAxis.setTypeface(this.tf_quick_bold);
        xAxis.setGranularity(1.0f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(0.0f);
        xAxis.setTextSize(this.graph_x_axis_text_size);
        xAxis.setTextColor(getResources().getColor(R.color.axis_label_color));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum((float) GetBMIEntries(arrayList).size());
        ArrayList arrayList2 = new ArrayList();
        for (int i4 = 0; i4 < arrayList.size(); i4++) {
            try {
                arrayList2.add(new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("yyyy-MM-dd hh:mm a").parse(String.valueOf(arrayList.get(i4).dateTime))).trim());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        this.graph_view_today.getXAxis().setValueFormatter(new IndexAxisValueFormatter(arrayList2));
        YAxis axisLeft = this.graph_view_today.getAxisLeft();
        axisLeft.removeAllLimitLines();
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeft.setTypeface(this.tf_quick_bold);
        axisLeft.setTextSize(this.graph_left_axis_text_size);
        axisLeft.setTextColor(getResources().getColor(R.color.axis_label_color));
        axisLeft.setDrawGridLines(false);
        axisLeft.setAxisMaximum((float) (this.SQLite_health_tracker.GetMaxBMIValue(list) + this.extra_y_axis_value));
        this.graph_view_today.getAxisRight().setEnabled(false);
        SetBMIBarWidth(this.graph_view_today, barData, arrayList);
        this.graph_view_today.invalidate();
    }

    private void SetBMIMonthlyChart(List<BMIChartData> list) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        int i2 = instance.get(1);
        instance.get(5);
        ArrayList<BMIChartData> arrayList = (ArrayList) list;
//                this.SQLite_health_tracker.GetBMIChartMonthlyData(i, instance.get(2) + 1, i2);
        if (arrayList.size() == 0) {
            this.lbl_no_data.setVisibility(View.VISIBLE);
            this.lbl_no_data.setText("No Monthly data available!");
        } else {
            this.lbl_no_data.setVisibility(View.GONE);
        }
        List<BarEntry> GetBMIEntries = GetBMIEntries(arrayList);
        this.dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(GetBMIEntries, this.bmi_legend_text);
        barDataSet.setColor(getResources().getColor(R.color.chart_bar_legend_color));
        barDataSet.setValueTextColor(getResources().getColor(R.color.chart_label_color));
        barDataSet.setValueTextSize(10.0f);
        barDataSet.setGradientColor(this.chart_bottom_color, this.chart_top_color);
        this.dataSets.add(barDataSet);
        BarData barData = new BarData(this.dataSets);
        this.graph_view_monthly.setData(barData);
        this.graph_view_monthly.getAxisLeft().setAxisMinimum(0.0f);
        this.graph_view_monthly.getDescription().setEnabled(false);
        this.graph_view_monthly.getAxisRight().setAxisMinimum(0.0f);
        this.graph_view_monthly.setDrawBarShadow(false);
        this.graph_view_monthly.setDrawValueAboveBar(true);
        this.graph_view_monthly.setMaxVisibleValueCount(10);
        this.graph_view_monthly.setPinchZoom(false);
        this.graph_view_monthly.setDrawGridBackground(false);
        this.graph_view_monthly.setExtraBottomOffset(20.0f);
        this.graph_view_monthly.setDrawBorders(false);
        MyMarkerView myMarkerView = new MyMarkerView(this, R.layout.custom_marker_view);
        myMarkerView.setChartView(this.graph_view_monthly);
        this.graph_view_monthly.setMarker(myMarkerView);
        this.graph_view_monthly.getLegend().setEnabled(this.is_chart_legend_visible);
        Legend legend = this.graph_view_monthly.getLegend();
        legend.setTypeface(this.tf_quick_bold);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(this.graph_legend_text_size);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setYOffset(0.0f);
        legend.setXOffset(0.0f);
        XAxis xAxis = this.graph_view_monthly.getXAxis();
        xAxis.setTypeface(this.tf_quick_bold);
        xAxis.setGranularity(1.0f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(0.0f);
        xAxis.setTextSize(this.graph_x_axis_text_size);
        xAxis.setTextColor(getResources().getColor(R.color.axis_label_color));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum((float) GetBMIEntries(arrayList).size());
        ArrayList arrayList2 = new ArrayList();
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            try {
                Date parse = new SimpleDateFormat("yyyy-MM-dd hh:mm a").parse(String.valueOf(arrayList.get(i3).dateTime));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm");
                String trim = simpleDateFormat.format(parse).trim();
                String trim2 = simpleDateFormat2.format(parse).trim();
                arrayList2.add(trim + " " + trim2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        this.graph_view_monthly.getXAxis().setValueFormatter(new IndexAxisValueFormatter(arrayList2));
        YAxis axisLeft = this.graph_view_monthly.getAxisLeft();
        axisLeft.removeAllLimitLines();
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeft.setTypeface(this.tf_quick_bold);
        axisLeft.setTextSize(this.graph_left_axis_text_size);
        axisLeft.setTextColor(getResources().getColor(R.color.axis_label_color));
        axisLeft.setDrawGridLines(false);
        axisLeft.setAxisMaximum((float) (this.SQLite_health_tracker.GetMaxBMIValue(list) + this.extra_y_axis_value));
        this.graph_view_monthly.getAxisRight().setEnabled(false);
        SetBMIBarWidth(this.graph_view_monthly, barData, arrayList);
        this.graph_view_monthly.invalidate();
    }

    private void SetBMIYearlyChart(List<BMIChartData> list) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        int i2 = instance.get(1);
        instance.get(2);
        instance.get(5);
        ArrayList<BMIChartData> arrayList = (ArrayList) list;
//                this.SQLite_health_tracker.GetBMIChartYearlyData(i, i2);
        if (arrayList.size() == 0) {
            this.lbl_no_data.setVisibility(View.VISIBLE);
            this.lbl_no_data.setText("No Yearly data available!");
        } else {
            this.lbl_no_data.setVisibility(View.GONE);
        }
        List<BarEntry> GetBMIEntries = GetBMIEntries(arrayList);
        this.dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(GetBMIEntries, this.bmi_legend_text);
        barDataSet.setColor(getResources().getColor(R.color.chart_bar_legend_color));
        barDataSet.setValueTextColor(getResources().getColor(R.color.chart_label_color));
        barDataSet.setValueTextSize(10.0f);
        barDataSet.setGradientColor(this.chart_bottom_color, this.chart_top_color);
        this.dataSets.add(barDataSet);
        BarData barData = new BarData(this.dataSets);
        this.graph_view_yearly.setData(barData);
        this.graph_view_yearly.getAxisLeft().setAxisMinimum(0.0f);
        this.graph_view_yearly.getDescription().setEnabled(false);
        this.graph_view_yearly.getAxisRight().setAxisMinimum(0.0f);
        this.graph_view_yearly.setDrawBarShadow(false);
        this.graph_view_yearly.setDrawValueAboveBar(true);
        this.graph_view_yearly.setMaxVisibleValueCount(10);
        this.graph_view_yearly.setPinchZoom(false);
        this.graph_view_yearly.setDrawGridBackground(false);
        this.graph_view_yearly.setExtraBottomOffset(5.0f);
        this.graph_view_yearly.setDrawBorders(false);
        MyMarkerView myMarkerView = new MyMarkerView(this, R.layout.custom_marker_view);
        myMarkerView.setChartView(this.graph_view_yearly);
        this.graph_view_yearly.setMarker(myMarkerView);
        this.graph_view_yearly.getLegend().setEnabled(this.is_chart_legend_visible);
        Legend legend = this.graph_view_yearly.getLegend();
        legend.setTypeface(this.tf_quick_bold);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(this.graph_legend_text_size);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setYOffset(0.0f);
        legend.setXOffset(0.0f);
        XAxis xAxis = this.graph_view_yearly.getXAxis();
        xAxis.setTypeface(this.tf_quick_bold);
        xAxis.setGranularity(1.0f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(0.0f);
        xAxis.setTextSize(this.graph_x_axis_text_size);
        xAxis.setTextColor(getResources().getColor(R.color.axis_label_color));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum((float) GetBMIEntries(arrayList).size());
        ArrayList arrayList2 = new ArrayList();
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            try {
                Date parse = new SimpleDateFormat("yyyy-MM-dd hh:mm a").parse(String.valueOf(arrayList.get(i3).dateTime));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm");
                String trim = simpleDateFormat.format(parse).trim();
                simpleDateFormat2.format(parse).trim();
                arrayList2.add(trim);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        this.graph_view_yearly.getXAxis().setValueFormatter(new IndexAxisValueFormatter(arrayList2));
        YAxis axisLeft = this.graph_view_yearly.getAxisLeft();
        axisLeft.removeAllLimitLines();
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeft.setTypeface(this.tf_quick_bold);
        axisLeft.setTextSize(this.graph_left_axis_text_size);
        axisLeft.setTextColor(getResources().getColor(R.color.axis_label_color));
        axisLeft.setDrawGridLines(false);
        axisLeft.setAxisMaximum((float) (this.SQLite_health_tracker.GetMaxBMIValue(list) + this.extra_y_axis_value));
        this.graph_view_yearly.getAxisRight().setEnabled(false);
        SetBMIBarWidth(this.graph_view_yearly, barData, arrayList);
        this.graph_view_yearly.invalidate();
    }

    private void SetBMICustomChart(List<BMIChartData> list) {
        ArrayList<BMIChartData> arrayList = (ArrayList) list;
//                this.SQLite_health_tracker.GetBMIChartCustomData(i, this.custom_start_date, this.custom_end_date);
        if (arrayList.size() == 0) {
            this.lbl_no_data.setVisibility(View.VISIBLE);
            this.lbl_no_data.setText("No Yearly data available!");
        } else {
            this.lbl_no_data.setVisibility(View.GONE);
        }
        List<BarEntry> GetBMIEntries = GetBMIEntries(arrayList);
        this.dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(GetBMIEntries, this.bmi_legend_text);
        barDataSet.setColor(getResources().getColor(R.color.chart_bar_legend_color));
        barDataSet.setValueTextColor(getResources().getColor(R.color.chart_label_color));
        barDataSet.setValueTextSize(10.0f);
        barDataSet.setGradientColor(this.chart_bottom_color, this.chart_top_color);
        this.dataSets.add(barDataSet);
        BarData barData = new BarData(this.dataSets);
        this.graph_view_custom.setData(barData);
        this.graph_view_custom.getAxisLeft().setAxisMinimum(0.0f);
        this.graph_view_custom.getDescription().setEnabled(false);
        this.graph_view_custom.getAxisRight().setAxisMinimum(0.0f);
        this.graph_view_custom.setDrawBarShadow(false);
        this.graph_view_custom.setDrawValueAboveBar(true);
        this.graph_view_custom.setMaxVisibleValueCount(10);
        this.graph_view_custom.setPinchZoom(false);
        this.graph_view_custom.setDrawGridBackground(false);
        this.graph_view_custom.setExtraBottomOffset(5.0f);
        this.graph_view_custom.setDrawBorders(false);
        MyMarkerView myMarkerView = new MyMarkerView(this, R.layout.custom_marker_view);
        myMarkerView.setChartView(this.graph_view_custom);
        this.graph_view_custom.setMarker(myMarkerView);
        this.graph_view_custom.getLegend().setEnabled(this.is_chart_legend_visible);
        Legend legend = this.graph_view_custom.getLegend();
        legend.setTypeface(this.tf_quick_bold);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(this.graph_legend_text_size);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setYOffset(0.0f);
        legend.setXOffset(0.0f);
        XAxis xAxis = this.graph_view_custom.getXAxis();
        xAxis.setTypeface(this.tf_quick_bold);
        xAxis.setGranularity(1.0f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(0.0f);
        xAxis.setTextSize(this.graph_x_axis_text_size);
        xAxis.setTextColor(getResources().getColor(R.color.axis_label_color));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum((float) GetBMIEntries(arrayList).size());
        ArrayList arrayList2 = new ArrayList();
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            try {
                arrayList2.add(arrayList.get(i2).date.trim());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.graph_view_custom.getXAxis().setValueFormatter(new IndexAxisValueFormatter(arrayList2));
        YAxis axisLeft = this.graph_view_custom.getAxisLeft();
        axisLeft.removeAllLimitLines();
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeft.setTypeface(this.tf_quick_bold);
        axisLeft.setTextSize(this.graph_left_axis_text_size);
        axisLeft.setTextColor(getResources().getColor(R.color.axis_label_color));
        axisLeft.setDrawGridLines(false);
        axisLeft.setAxisMaximum((float) (this.SQLite_health_tracker.GetMaxBMIValue(list) + this.extra_y_axis_value));
        this.graph_view_custom.getAxisRight().setEnabled(false);
        SetBMIBarWidth(this.graph_view_custom, barData, arrayList);
        this.graph_view_custom.invalidate();
    }

    private void SetBMIBarWidth(BarChart barChart, BarData barData, ArrayList<BMIChartData> arrayList) {
        if (this.dataSets.size() > 1) {
            float size = (0.7f / ((float) this.dataSets.size())) - 0.02f;
            this.defaultBarWidth = size;
            if (size >= 0.0f) {
                barData.setBarWidth(size);
            }
            int size2 = GetBMIEntries(arrayList).size();
            if (size2 != -1) {
                barChart.getXAxis().setAxisMinimum(0.0f);
                barChart.getXAxis().setAxisMaximum((barChart.getBarData().getGroupWidth(0.3f, 0.02f) * ((float) size2)) + 0.0f);
                barChart.getXAxis().setCenterAxisLabels(true);
            }
            barChart.groupBars(0.0f, 0.3f, 0.02f);
            barChart.invalidate();
            return;
        }
        barData.setBarWidth(this.bar_width);
        barChart.getXAxis().setCenterAxisLabels(false);
        barChart.getXAxis().resetAxisMaximum();
        barChart.getXAxis().resetAxisMinimum();
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.invalidate();
    }

    private List<BarEntry> GetBMIEntries(ArrayList<BMIChartData> arrayList) {
        ArrayList arrayList2 = new ArrayList();
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList2.add(new BarEntry((float) i, Float.parseFloat(arrayList.get(i).bmi)));
        }
        return arrayList2.subList(0, arrayList.size());
    }

    private void SetBodyTempAllChart(List<BodyTempChartAllData> list) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        instance.get(1);
        instance.get(2);
        instance.get(5);
        ArrayList<BodyTempChartAllData> arrayList = (ArrayList) list;
//                this.SQLite_health_tracker.GetBodyTempChartAllData(i);
        if (arrayList.size() == 0) {
            this.lbl_no_data.setVisibility(View.VISIBLE);
            this.lbl_no_data.setText("No data available for All!");
        } else {
            this.lbl_no_data.setVisibility(View.GONE);
        }
        List<BarEntry> GetCelsiusEntries = GetCelsiusEntries(arrayList);
        List<BarEntry> GetFahrenheitEntries = GetFahrenheitEntries(arrayList);
        List<BarEntry> GetPulseEntries = GetPulseEntries(arrayList);
        this.dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(GetCelsiusEntries, this.celsius_legend_text);
        barDataSet.setColor(getResources().getColor(R.color.celsius_color));
        barDataSet.setValueTextColor(getResources().getColor(R.color.celsius_color));
        barDataSet.setValueTextSize(10.0f);
        barDataSet.setGradientColor(this.chart_bottom_color_1, this.chart_top_color_1);
        BarDataSet barDataSet2 = new BarDataSet(GetFahrenheitEntries, this.fahrenheit_legend_text);
        barDataSet2.setColors(getResources().getColor(R.color.fahrenheit_color));
        barDataSet2.setValueTextColor(getResources().getColor(R.color.fahrenheit_color));
        barDataSet2.setValueTextSize(10.0f);
        barDataSet2.setGradientColor(this.chart_bottom_color_2, this.chart_top_color_2);
        BarDataSet barDataSet3 = new BarDataSet(GetPulseEntries, this.pulse_legend_text);
        barDataSet3.setColors(getResources().getColor(R.color.pulse_color));
        barDataSet3.setValueTextColor(getResources().getColor(R.color.pulse_color));
        barDataSet3.setValueTextSize(10.0f);
        barDataSet3.setGradientColor(this.chart_bottom_color_3, this.chart_top_color_3);
        this.dataSets.add(barDataSet);
        this.dataSets.add(barDataSet2);
        this.dataSets.add(barDataSet3);
        BarData barData = new BarData(this.dataSets);
        this.graph_view_all.setData(barData);
        this.graph_view_all.getAxisLeft().setAxisMinimum(0.0f);
        this.graph_view_all.getDescription().setEnabled(false);
        this.graph_view_all.getAxisRight().setAxisMinimum(0.0f);
        this.graph_view_all.setDrawBarShadow(false);
        this.graph_view_all.setDrawValueAboveBar(true);
        this.graph_view_all.setMaxVisibleValueCount(10);
        this.graph_view_all.setPinchZoom(false);
        this.graph_view_all.setDrawGridBackground(false);
        this.graph_view_all.setExtraBottomOffset(5.0f);
        this.graph_view_all.setDrawBorders(false);
        MyMarkerView myMarkerView = new MyMarkerView(this, R.layout.custom_marker_view);
        myMarkerView.setChartView(this.graph_view_all);
        this.graph_view_all.setMarker(myMarkerView);
        this.graph_view_all.getLegend().setEnabled(this.is_chart_legend_visible);
        Legend legend = this.graph_view_all.getLegend();
        legend.setTypeface(this.tf_quick_bold);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(this.graph_legend_text_size);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setYOffset(0.0f);
        legend.setXOffset(0.0f);
        XAxis xAxis = this.graph_view_all.getXAxis();
        xAxis.setTypeface(this.tf_quick_bold);
        xAxis.setGranularity(1.0f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(0.0f);
        xAxis.setTextSize(this.graph_x_axis_text_size);
        xAxis.setTextColor(getResources().getColor(R.color.axis_label_color));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum((float) GetPulseEntries(arrayList).size());
        ArrayList arrayList2 = new ArrayList();
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            arrayList2.add(arrayList.get(i2).fever_type.trim());
        }
        this.graph_view_all.getXAxis().setValueFormatter(new IndexAxisValueFormatter(arrayList2));
        YAxis axisLeft = this.graph_view_all.getAxisLeft();
        axisLeft.removeAllLimitLines();
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeft.setTypeface(this.tf_quick_bold);
        axisLeft.setTextSize(this.graph_left_axis_text_size);
        axisLeft.setTextColor(getResources().getColor(R.color.axis_label_color));
        axisLeft.setDrawGridLines(false);
        axisLeft.setAxisMaximum((float) (this.SQLite_health_tracker.GetMaxBodyTempValue(list) + this.extra_y_axis_value));
        this.graph_view_all.getAxisRight().setEnabled(false);
        SetBodyTempBarWidth(this.graph_view_all, barData, arrayList);
        this.graph_view_all.invalidate();
    }

    private void SetBodyTempTodayChart(List<BodyTempChartAllData> list) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        int i2 = instance.get(1);
        int i3 = instance.get(5);
        ArrayList<BodyTempChartAllData> arrayList = (ArrayList) list;
//                this.SQLite_health_tracker.GetBodyTempChartTodayData(i, i3, instance.get(2) + 1, i2);
        if (arrayList.size() == 0) {
            this.lbl_no_data.setVisibility(View.VISIBLE);
            this.lbl_no_data.setText("No data available for Today!");
        } else {
            this.lbl_no_data.setVisibility(View.GONE);
        }
        List<BarEntry> GetCelsiusEntries = GetCelsiusEntries(arrayList);
        List<BarEntry> GetFahrenheitEntries = GetFahrenheitEntries(arrayList);
        List<BarEntry> GetPulseEntries = GetPulseEntries(arrayList);
        this.dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(GetCelsiusEntries, this.celsius_legend_text);
        barDataSet.setColor(getResources().getColor(R.color.celsius_color));
        barDataSet.setValueTextColor(getResources().getColor(R.color.celsius_color));
        barDataSet.setValueTextSize(10.0f);
        barDataSet.setGradientColor(this.chart_bottom_color_1, this.chart_top_color_1);
        BarDataSet barDataSet2 = new BarDataSet(GetFahrenheitEntries, this.fahrenheit_legend_text);
        barDataSet2.setColors(getResources().getColor(R.color.fahrenheit_color));
        barDataSet2.setValueTextColor(getResources().getColor(R.color.fahrenheit_color));
        barDataSet2.setValueTextSize(10.0f);
        barDataSet2.setGradientColor(this.chart_bottom_color_2, this.chart_top_color_2);
        BarDataSet barDataSet3 = new BarDataSet(GetPulseEntries, this.pulse_legend_text);
        barDataSet3.setColors(getResources().getColor(R.color.pulse_color));
        barDataSet3.setValueTextColor(getResources().getColor(R.color.pulse_color));
        barDataSet3.setValueTextSize(10.0f);
        barDataSet3.setGradientColor(this.chart_bottom_color_3, this.chart_top_color_3);
        this.dataSets.add(barDataSet);
        this.dataSets.add(barDataSet2);
        this.dataSets.add(barDataSet3);
        BarData barData = new BarData(this.dataSets);
        this.graph_view_today.setData(barData);
        this.graph_view_today.getAxisLeft().setAxisMinimum(0.0f);
        this.graph_view_today.getDescription().setEnabled(false);
        this.graph_view_today.getAxisRight().setAxisMinimum(0.0f);
        this.graph_view_today.setDrawBarShadow(false);
        this.graph_view_today.setDrawValueAboveBar(true);
        this.graph_view_today.setMaxVisibleValueCount(10);
        this.graph_view_today.setPinchZoom(false);
        this.graph_view_today.setDrawGridBackground(false);
        this.graph_view_today.setExtraBottomOffset(5.0f);
        this.graph_view_today.setDrawBorders(false);
        MyMarkerView myMarkerView = new MyMarkerView(this, R.layout.custom_marker_view);
        myMarkerView.setChartView(this.graph_view_today);
        this.graph_view_today.setMarker(myMarkerView);
        this.graph_view_today.getLegend().setEnabled(this.is_chart_legend_visible);
        Legend legend = this.graph_view_today.getLegend();
        legend.setTypeface(this.tf_quick_bold);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(this.graph_legend_text_size);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setYOffset(0.0f);
        legend.setXOffset(0.0f);
        XAxis xAxis = this.graph_view_today.getXAxis();
        xAxis.setTypeface(this.tf_quick_bold);
        xAxis.setGranularity(1.0f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(0.0f);
        xAxis.setTextSize(this.graph_x_axis_text_size);
        xAxis.setTextColor(getResources().getColor(R.color.axis_label_color));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum((float) GetPulseEntries(arrayList).size());
        ArrayList arrayList2 = new ArrayList();
        for (int i4 = 0; i4 < arrayList.size(); i4++) {
            try {
                arrayList2.add(new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a").parse(String.valueOf(arrayList.get(i4).dateTime))).trim());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        this.graph_view_today.getXAxis().setValueFormatter(new IndexAxisValueFormatter(arrayList2));
        YAxis axisLeft = this.graph_view_today.getAxisLeft();
        axisLeft.removeAllLimitLines();
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeft.setTypeface(this.tf_quick_bold);
        axisLeft.setTextSize(this.graph_left_axis_text_size);
        axisLeft.setTextColor(getResources().getColor(R.color.axis_label_color));
        axisLeft.setDrawGridLines(false);
        axisLeft.setAxisMaximum((float) (this.SQLite_health_tracker.GetMaxBodyTempValue(list) + this.extra_y_axis_value));
        this.graph_view_today.getAxisRight().setEnabled(false);
        SetBodyTempBarWidth(this.graph_view_today, barData, arrayList);
        this.graph_view_today.invalidate();
    }

    private void SetBodyTempMonthlyChart(List<BodyTempChartAllData> list) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        int i2 = instance.get(1);
        instance.get(5);
        ArrayList<BodyTempChartAllData> arrayList = (ArrayList) list;
//                this.SQLite_health_tracker.GetBodyTempChartMonthlyData(i, instance.get(2) + 1, i2);
        if (arrayList.size() == 0) {
            this.lbl_no_data.setVisibility(View.VISIBLE);
            this.lbl_no_data.setText("No Monthly data available!");
        } else {
            this.lbl_no_data.setVisibility(View.GONE);
        }
        List<BarEntry> GetCelsiusEntries = GetCelsiusEntries(arrayList);
        List<BarEntry> GetFahrenheitEntries = GetFahrenheitEntries(arrayList);
        List<BarEntry> GetPulseEntries = GetPulseEntries(arrayList);
        this.dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(GetCelsiusEntries, this.celsius_legend_text);
        barDataSet.setColor(getResources().getColor(R.color.celsius_color));
        barDataSet.setValueTextColor(getResources().getColor(R.color.celsius_color));
        barDataSet.setValueTextSize(10.0f);
        barDataSet.setGradientColor(this.chart_bottom_color_1, this.chart_top_color_1);
        BarDataSet barDataSet2 = new BarDataSet(GetFahrenheitEntries, this.fahrenheit_legend_text);
        barDataSet2.setColors(getResources().getColor(R.color.fahrenheit_color));
        barDataSet2.setValueTextColor(getResources().getColor(R.color.fahrenheit_color));
        barDataSet2.setValueTextSize(10.0f);
        barDataSet2.setGradientColor(this.chart_bottom_color_2, this.chart_top_color_2);
        BarDataSet barDataSet3 = new BarDataSet(GetPulseEntries, this.pulse_legend_text);
        barDataSet3.setColors(getResources().getColor(R.color.pulse_color));
        barDataSet3.setValueTextColor(getResources().getColor(R.color.pulse_color));
        barDataSet3.setValueTextSize(10.0f);
        barDataSet3.setGradientColor(this.chart_bottom_color_3, this.chart_top_color_3);
        this.dataSets.add(barDataSet);
        this.dataSets.add(barDataSet2);
        this.dataSets.add(barDataSet3);
        BarData barData = new BarData(this.dataSets);
        this.graph_view_monthly.setData(barData);
        this.graph_view_monthly.getAxisLeft().setAxisMinimum(0.0f);
        this.graph_view_monthly.getDescription().setEnabled(false);
        this.graph_view_monthly.getAxisRight().setAxisMinimum(0.0f);
        this.graph_view_monthly.setDrawBarShadow(false);
        this.graph_view_monthly.setDrawValueAboveBar(true);
        this.graph_view_monthly.setMaxVisibleValueCount(10);
        this.graph_view_monthly.setPinchZoom(false);
        this.graph_view_monthly.setDrawGridBackground(false);
        this.graph_view_monthly.setExtraBottomOffset(20.0f);
        this.graph_view_monthly.setDrawBorders(false);
        MyMarkerView myMarkerView = new MyMarkerView(this, R.layout.custom_marker_view);
        myMarkerView.setChartView(this.graph_view_monthly);
        this.graph_view_monthly.setMarker(myMarkerView);
        this.graph_view_monthly.getLegend().setEnabled(this.is_chart_legend_visible);
        Legend legend = this.graph_view_monthly.getLegend();
        legend.setTypeface(this.tf_quick_bold);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(this.graph_legend_text_size);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setYOffset(0.0f);
        legend.setXOffset(0.0f);
        XAxis xAxis = this.graph_view_monthly.getXAxis();
        xAxis.setTypeface(this.tf_quick_bold);
        xAxis.setGranularity(1.0f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(0.0f);
        xAxis.setTextSize(this.graph_x_axis_text_size);
        xAxis.setTextColor(getResources().getColor(R.color.axis_label_color));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum((float) GetPulseEntries(arrayList).size());
        ArrayList arrayList2 = new ArrayList();
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            try {
                arrayList.get(i3).name.trim();
                Date parse = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a").parse(String.valueOf(arrayList.get(i3).dateTime));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy");
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm");
                String trim = simpleDateFormat.format(parse).trim();
                String trim2 = simpleDateFormat2.format(parse).trim();
                arrayList2.add(trim + " " + trim2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        this.graph_view_monthly.getXAxis().setValueFormatter(new IndexAxisValueFormatter(arrayList2));
        YAxis axisLeft = this.graph_view_monthly.getAxisLeft();
        axisLeft.removeAllLimitLines();
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeft.setTypeface(this.tf_quick_bold);
        axisLeft.setTextSize(this.graph_left_axis_text_size);
        axisLeft.setTextColor(getResources().getColor(R.color.axis_label_color));
        axisLeft.setDrawGridLines(false);
        axisLeft.setAxisMaximum((float) (this.SQLite_health_tracker.GetMaxBodyTempValue(list) + this.extra_y_axis_value));
        this.graph_view_monthly.getAxisRight().setEnabled(false);
        SetBodyTempBarWidth(this.graph_view_monthly, barData, arrayList);
        this.graph_view_monthly.invalidate();
    }

    private void SetBodyTempYearlyChart(List<BodyTempChartAllData> list) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        int i2 = instance.get(1);
        instance.get(2);
        instance.get(5);
        ArrayList<BodyTempChartAllData> arrayList = (ArrayList) list;
//                this.SQLite_health_tracker.GetBodyTempChartYearlyData(i, i2);
        if (arrayList.size() == 0) {
            this.lbl_no_data.setVisibility(View.VISIBLE);
            this.lbl_no_data.setText("No Yearly data available!");
        } else {
            this.lbl_no_data.setVisibility(View.GONE);
        }
        List<BarEntry> GetCelsiusEntries = GetCelsiusEntries(arrayList);
        List<BarEntry> GetFahrenheitEntries = GetFahrenheitEntries(arrayList);
        List<BarEntry> GetPulseEntries = GetPulseEntries(arrayList);
        this.dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(GetCelsiusEntries, this.celsius_legend_text);
        barDataSet.setColor(getResources().getColor(R.color.celsius_color));
        barDataSet.setValueTextColor(getResources().getColor(R.color.celsius_color));
        barDataSet.setValueTextSize(10.0f);
        barDataSet.setGradientColor(this.chart_bottom_color_1, this.chart_top_color_1);
        BarDataSet barDataSet2 = new BarDataSet(GetFahrenheitEntries, this.fahrenheit_legend_text);
        barDataSet2.setColors(getResources().getColor(R.color.fahrenheit_color));
        barDataSet2.setValueTextColor(getResources().getColor(R.color.fahrenheit_color));
        barDataSet2.setValueTextSize(10.0f);
        barDataSet2.setGradientColor(this.chart_bottom_color_2, this.chart_top_color_2);
        BarDataSet barDataSet3 = new BarDataSet(GetPulseEntries, this.pulse_legend_text);
        barDataSet3.setColors(getResources().getColor(R.color.pulse_color));
        barDataSet3.setValueTextColor(getResources().getColor(R.color.pulse_color));
        barDataSet3.setValueTextSize(10.0f);
        barDataSet3.setGradientColor(this.chart_bottom_color_3, this.chart_top_color_3);
        this.dataSets.add(barDataSet);
        this.dataSets.add(barDataSet2);
        this.dataSets.add(barDataSet3);
        BarData barData = new BarData(this.dataSets);
        this.graph_view_yearly.setData(barData);
        this.graph_view_yearly.getAxisLeft().setAxisMinimum(0.0f);
        this.graph_view_yearly.getDescription().setEnabled(false);
        this.graph_view_yearly.getAxisRight().setAxisMinimum(0.0f);
        this.graph_view_yearly.setDrawBarShadow(false);
        this.graph_view_yearly.setDrawValueAboveBar(true);
        this.graph_view_yearly.setMaxVisibleValueCount(10);
        this.graph_view_yearly.setPinchZoom(false);
        this.graph_view_yearly.setDrawGridBackground(false);
        this.graph_view_yearly.setExtraBottomOffset(5.0f);
        this.graph_view_yearly.setDrawBorders(false);
        MyMarkerView myMarkerView = new MyMarkerView(this, R.layout.custom_marker_view);
        myMarkerView.setChartView(this.graph_view_yearly);
        this.graph_view_yearly.setMarker(myMarkerView);
        this.graph_view_yearly.getLegend().setEnabled(this.is_chart_legend_visible);
        Legend legend = this.graph_view_yearly.getLegend();
        legend.setTypeface(this.tf_quick_bold);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(this.graph_legend_text_size);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setYOffset(0.0f);
        legend.setXOffset(0.0f);
        XAxis xAxis = this.graph_view_yearly.getXAxis();
        xAxis.setTypeface(this.tf_quick_bold);
        xAxis.setGranularity(1.0f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(0.0f);
        xAxis.setTextSize(this.graph_x_axis_text_size);
        xAxis.setTextColor(getResources().getColor(R.color.axis_label_color));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum((float) GetPulseEntries(arrayList).size());
        ArrayList arrayList2 = new ArrayList();
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            try {
                Date parse = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a").parse(String.valueOf(arrayList.get(i3).dateTime));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy");
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm");
                String trim = simpleDateFormat.format(parse).trim();
                simpleDateFormat2.format(parse).trim();
                arrayList2.add(trim);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        this.graph_view_yearly.getXAxis().setValueFormatter(new IndexAxisValueFormatter(arrayList2));
        YAxis axisLeft = this.graph_view_yearly.getAxisLeft();
        axisLeft.removeAllLimitLines();
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeft.setTypeface(this.tf_quick_bold);
        axisLeft.setTextSize(this.graph_left_axis_text_size);
        axisLeft.setTextColor(getResources().getColor(R.color.axis_label_color));
        axisLeft.setDrawGridLines(false);
        axisLeft.setAxisMaximum((float) (this.SQLite_health_tracker.GetMaxBodyTempValue(list) + this.extra_y_axis_value));
        this.graph_view_yearly.getAxisRight().setEnabled(false);
        SetBodyTempBarWidth(this.graph_view_yearly, barData, arrayList);
        this.graph_view_yearly.invalidate();
    }

    private void SetBodyTempCustomChart(List<BodyTempChartAllData> list) {
        ArrayList<BodyTempChartAllData> arrayList = (ArrayList) list;
//                this.SQLite_health_tracker.GetBodyTempChartCustomData(i, this.custom_start_date, this.custom_end_date);
        if (arrayList.size() == 0) {
            this.lbl_no_data.setVisibility(View.VISIBLE);
            this.lbl_no_data.setText("No Yearly data available!");
        } else {
            this.lbl_no_data.setVisibility(View.GONE);
        }
        List<BarEntry> GetCelsiusEntries = GetCelsiusEntries(arrayList);
        List<BarEntry> GetFahrenheitEntries = GetFahrenheitEntries(arrayList);
        List<BarEntry> GetPulseEntries = GetPulseEntries(arrayList);
        this.dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(GetCelsiusEntries, this.celsius_legend_text);
        barDataSet.setColor(getResources().getColor(R.color.celsius_color));
        barDataSet.setValueTextColor(getResources().getColor(R.color.celsius_color));
        barDataSet.setValueTextSize(10.0f);
        barDataSet.setGradientColor(this.chart_bottom_color_1, this.chart_top_color_1);
        BarDataSet barDataSet2 = new BarDataSet(GetFahrenheitEntries, this.fahrenheit_legend_text);
        barDataSet2.setColors(getResources().getColor(R.color.fahrenheit_color));
        barDataSet2.setValueTextColor(getResources().getColor(R.color.fahrenheit_color));
        barDataSet2.setValueTextSize(10.0f);
        barDataSet2.setGradientColor(this.chart_bottom_color_2, this.chart_top_color_2);
        BarDataSet barDataSet3 = new BarDataSet(GetPulseEntries, this.pulse_legend_text);
        barDataSet3.setColors(getResources().getColor(R.color.pulse_color));
        barDataSet3.setValueTextColor(getResources().getColor(R.color.pulse_color));
        barDataSet3.setValueTextSize(10.0f);
        barDataSet3.setGradientColor(this.chart_bottom_color_3, this.chart_top_color_3);
        this.dataSets.add(barDataSet);
        this.dataSets.add(barDataSet2);
        this.dataSets.add(barDataSet3);
        BarData barData = new BarData(this.dataSets);
        this.graph_view_custom.setData(barData);
        this.graph_view_custom.getAxisLeft().setAxisMinimum(0.0f);
        this.graph_view_custom.getDescription().setEnabled(false);
        this.graph_view_custom.getAxisRight().setAxisMinimum(0.0f);
        this.graph_view_custom.setDrawBarShadow(false);
        this.graph_view_custom.setDrawValueAboveBar(true);
        this.graph_view_custom.setMaxVisibleValueCount(10);
        this.graph_view_custom.setPinchZoom(false);
        this.graph_view_custom.setDrawGridBackground(false);
        this.graph_view_custom.setExtraBottomOffset(5.0f);
        this.graph_view_custom.setDrawBorders(false);
        MyMarkerView myMarkerView = new MyMarkerView(this, R.layout.custom_marker_view);
        myMarkerView.setChartView(this.graph_view_custom);
        this.graph_view_custom.setMarker(myMarkerView);
        this.graph_view_custom.getLegend().setEnabled(this.is_chart_legend_visible);
        Legend legend = this.graph_view_custom.getLegend();
        legend.setTypeface(this.tf_quick_bold);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(this.graph_legend_text_size);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setYOffset(0.0f);
        legend.setXOffset(0.0f);
        XAxis xAxis = this.graph_view_custom.getXAxis();
        xAxis.setTypeface(this.tf_quick_bold);
        xAxis.setGranularity(1.0f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(0.0f);
        xAxis.setTextSize(this.graph_x_axis_text_size);
        xAxis.setTextColor(getResources().getColor(R.color.axis_label_color));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum((float) GetPulseEntries(arrayList).size());
        ArrayList arrayList2 = new ArrayList();
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            try {
                Date parse = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a").parse(String.valueOf(arrayList.get(i2).dateTime));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm a");
                String trim = simpleDateFormat.format(parse).trim();
                simpleDateFormat2.format(parse).trim();
                arrayList2.add(trim);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        this.graph_view_custom.getXAxis().setValueFormatter(new IndexAxisValueFormatter(arrayList2));
        YAxis axisLeft = this.graph_view_custom.getAxisLeft();
        axisLeft.removeAllLimitLines();
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeft.setTypeface(this.tf_quick_bold);
        axisLeft.setTextSize(this.graph_left_axis_text_size);
        axisLeft.setTextColor(getResources().getColor(R.color.axis_label_color));
        axisLeft.setDrawGridLines(false);
        axisLeft.setAxisMaximum((float) (this.SQLite_health_tracker.GetMaxBodyTempValue(list) + this.extra_y_axis_value));
        this.graph_view_custom.getAxisRight().setEnabled(false);
        SetBodyTempBarWidth(this.graph_view_custom, barData, arrayList);
        this.graph_view_custom.invalidate();
    }

    private void SetBodyTempBarWidth(BarChart barChart, BarData barData, ArrayList<BodyTempChartAllData> arrayList) {
        if (this.dataSets.size() > 1) {
            float size = (0.7f / ((float) this.dataSets.size())) - 0.02f;
            this.defaultBarWidth = size;
            if (size >= 0.0f) {
                barData.setBarWidth(size);
            }
            int size2 = GetCelsiusEntries(arrayList).size();
            if (size2 != -1) {
                barChart.getXAxis().setAxisMinimum(0.0f);
                barChart.getXAxis().setAxisMaximum((barChart.getBarData().getGroupWidth(0.3f, 0.02f) * ((float) size2)) + 0.0f);
                barChart.getXAxis().setCenterAxisLabels(true);
            }
            barChart.groupBars(0.0f, 0.3f, 0.02f);
            barChart.invalidate();
            return;
        }
        barData.setBarWidth(this.bar_width);
        barChart.getXAxis().setCenterAxisLabels(false);
        barChart.getXAxis().resetAxisMaximum();
        barChart.getXAxis().resetAxisMinimum();
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.invalidate();
    }

    private List<BarEntry> GetCelsiusEntries(ArrayList<BodyTempChartAllData> arrayList) {
        ArrayList arrayList2 = new ArrayList();
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList2.add(new BarEntry((float) i, Float.parseFloat(arrayList.get(i).celsius)));
        }
        return arrayList2.subList(0, arrayList.size());
    }

    private List<BarEntry> GetFahrenheitEntries(ArrayList<BodyTempChartAllData> arrayList) {
        ArrayList arrayList2 = new ArrayList();
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList2.add(new BarEntry((float) i, Float.parseFloat(arrayList.get(i).fahrenheit)));
        }
        return arrayList2.subList(0, arrayList.size());
    }

    private List<BarEntry> GetPulseEntries(ArrayList<BodyTempChartAllData> arrayList) {
        ArrayList arrayList2 = new ArrayList();
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList2.add(new BarEntry((float) i, Float.parseFloat(arrayList.get(i).pulse)));
        }
        return arrayList2.subList(0, arrayList.size());
    }

    private void SetCholesterolAllChart(float i, List<CholesterolChartData> list) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        instance.get(1);
        instance.get(2);
        instance.get(5);
        ArrayList<CholesterolChartData> arrayList = (ArrayList<CholesterolChartData>) list;
//                (ArrayList) this.SQLite_health_tracker.GetCholesterolChartAllData(i);
        if (arrayList.size() == 0) {
            this.lbl_no_data.setVisibility(View.VISIBLE);
            this.lbl_no_data.setText("No data available for All!");
        } else {
            this.lbl_no_data.setVisibility(View.GONE);
        }
        List<BarEntry> GetCholesterolEntries = GetCholesterolEntries(arrayList);
        List<BarEntry> GetHDLEntries = GetHDLEntries(arrayList);
        List<BarEntry> GetLDLEntries = GetLDLEntries(arrayList);
        List<BarEntry> GetTriglycerideEntries = GetTriglycerideEntries(arrayList);
        this.dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(GetCholesterolEntries, this.cholesterol_legend_text);
        barDataSet.setColor(getResources().getColor(R.color.gradient_color_1));
        barDataSet.setValueTextColor(getResources().getColor(R.color.gradient_color_1));
        barDataSet.setValueTextSize(10.0f);
        barDataSet.setGradientColor(this.chart_bottom_color_1, this.chart_top_color_1);
        BarDataSet barDataSet2 = new BarDataSet(GetHDLEntries, this.hdl_legend_text);
        barDataSet2.setColors(getResources().getColor(R.color.gradient_color_2));
        barDataSet2.setValueTextColor(getResources().getColor(R.color.gradient_color_2));
        barDataSet2.setValueTextSize(10.0f);
        barDataSet2.setGradientColor(this.chart_bottom_color_2, this.chart_top_color_2);
        BarDataSet barDataSet3 = new BarDataSet(GetLDLEntries, this.ldl_legend_text);
        barDataSet3.setColors(getResources().getColor(R.color.gradient_color_3));
        barDataSet3.setValueTextColor(getResources().getColor(R.color.gradient_color_3));
        barDataSet3.setValueTextSize(10.0f);
        barDataSet3.setGradientColor(this.chart_bottom_color_3, this.chart_top_color_3);
        BarDataSet barDataSet4 = new BarDataSet(GetTriglycerideEntries, this.triglyceride_legend_text);
        barDataSet4.setColors(getResources().getColor(R.color.gradient_color_4));
        barDataSet4.setValueTextColor(getResources().getColor(R.color.gradient_color_4));
        barDataSet4.setValueTextSize(10.0f);
        barDataSet4.setGradientColor(this.chart_bottom_color_4, this.chart_top_color_4);
        this.dataSets.add(barDataSet);
        this.dataSets.add(barDataSet2);
        this.dataSets.add(barDataSet3);
        this.dataSets.add(barDataSet4);
        BarData barData = new BarData(this.dataSets);
        this.graph_view_all.setData(barData);
        this.graph_view_all.getAxisLeft().setAxisMinimum(0.0f);
        this.graph_view_all.getDescription().setEnabled(false);
        this.graph_view_all.getAxisRight().setAxisMinimum(0.0f);
        this.graph_view_all.setDrawBarShadow(false);
        this.graph_view_all.setDrawValueAboveBar(true);
        this.graph_view_all.setMaxVisibleValueCount(10);
        this.graph_view_all.setPinchZoom(false);
        this.graph_view_all.setDrawGridBackground(false);
        this.graph_view_all.setExtraBottomOffset(5.0f);
        this.graph_view_all.setDrawBorders(false);
        MyMarkerView myMarkerView = new MyMarkerView(this, R.layout.custom_marker_view);
        myMarkerView.setChartView(this.graph_view_all);
        this.graph_view_all.setMarker(myMarkerView);
        this.graph_view_all.getLegend().setEnabled(this.is_chart_legend_visible);
        Legend legend = this.graph_view_all.getLegend();
        legend.setTypeface(this.tf_quick_bold);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(this.graph_legend_text_size);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setYOffset(0.0f);
        legend.setXOffset(0.0f);
        XAxis xAxis = this.graph_view_all.getXAxis();
        xAxis.setTypeface(this.tf_quick_bold);
        xAxis.setGranularity(1.0f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(0.0f);
        xAxis.setTextSize(this.graph_x_axis_text_size);
        xAxis.setTextColor(getResources().getColor(R.color.axis_label_color));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum((float) GetCholesterolEntries(arrayList).size());
        ArrayList arrayList2 = new ArrayList();
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            arrayList2.add(arrayList.get(i2).date.trim());
        }
        this.graph_view_all.getXAxis().setValueFormatter(new IndexAxisValueFormatter(arrayList2));
        YAxis axisLeft = this.graph_view_all.getAxisLeft();
        axisLeft.removeAllLimitLines();
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeft.setTypeface(this.tf_quick_bold);
        axisLeft.setTextSize(this.graph_left_axis_text_size);
        axisLeft.setTextColor(getResources().getColor(R.color.axis_label_color));
        axisLeft.setDrawGridLines(false);
        axisLeft.setAxisMaximum((float) (i + this.extra_y_axis_value));
        this.graph_view_all.getAxisRight().setEnabled(false);
        SetCholesterolBarWidth(this.graph_view_all, barData, arrayList);
        this.graph_view_all.invalidate();
    }

    private void SetCholesterolTodayChart(float i, List<CholesterolChartData> list) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        int i2 = instance.get(1);
        int i3 = instance.get(5);
        ArrayList<CholesterolChartData> arrayList = (ArrayList<CholesterolChartData>) list;
        ;
//                (ArrayList) this.SQLite_health_tracker.GetCholesterolChartTodayData(i, i3, instance.get(2) + 1, i2);
        if (arrayList.size() == 0) {
            this.lbl_no_data.setVisibility(View.VISIBLE);
            this.lbl_no_data.setText("No data available for Today!");
        } else {
            this.lbl_no_data.setVisibility(View.GONE);
        }
        List<BarEntry> GetCholesterolEntries = GetCholesterolEntries(arrayList);
        List<BarEntry> GetHDLEntries = GetHDLEntries(arrayList);
        List<BarEntry> GetLDLEntries = GetLDLEntries(arrayList);
        List<BarEntry> GetTriglycerideEntries = GetTriglycerideEntries(arrayList);
        this.dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(GetCholesterolEntries, this.cholesterol_legend_text);
        barDataSet.setColor(getResources().getColor(R.color.gradient_color_1));
        barDataSet.setValueTextColor(getResources().getColor(R.color.gradient_color_1));
        barDataSet.setValueTextSize(10.0f);
        barDataSet.setGradientColor(this.chart_bottom_color_1, this.chart_top_color_1);
        BarDataSet barDataSet2 = new BarDataSet(GetHDLEntries, this.hdl_legend_text);
        barDataSet2.setColors(getResources().getColor(R.color.gradient_color_2));
        barDataSet2.setValueTextColor(getResources().getColor(R.color.gradient_color_2));
        barDataSet2.setValueTextSize(10.0f);
        barDataSet2.setGradientColor(this.chart_bottom_color_2, this.chart_top_color_2);
        BarDataSet barDataSet3 = new BarDataSet(GetLDLEntries, this.ldl_legend_text);
        barDataSet3.setColors(getResources().getColor(R.color.gradient_color_3));
        barDataSet3.setValueTextColor(getResources().getColor(R.color.gradient_color_3));
        barDataSet3.setValueTextSize(10.0f);
        barDataSet3.setGradientColor(this.chart_bottom_color_3, this.chart_top_color_3);
        BarDataSet barDataSet4 = new BarDataSet(GetTriglycerideEntries, this.triglyceride_legend_text);
        barDataSet4.setColors(getResources().getColor(R.color.gradient_color_4));
        barDataSet4.setValueTextColor(getResources().getColor(R.color.gradient_color_4));
        barDataSet4.setValueTextSize(10.0f);
        barDataSet4.setGradientColor(this.chart_bottom_color_4, this.chart_top_color_4);
        this.dataSets.add(barDataSet);
        this.dataSets.add(barDataSet2);
        this.dataSets.add(barDataSet3);
        this.dataSets.add(barDataSet4);
        BarData barData = new BarData(this.dataSets);
        this.graph_view_today.setData(barData);
        this.graph_view_today.getAxisLeft().setAxisMinimum(0.0f);
        this.graph_view_today.getDescription().setEnabled(false);
        this.graph_view_today.getAxisRight().setAxisMinimum(0.0f);
        this.graph_view_today.setDrawBarShadow(false);
        this.graph_view_today.setDrawValueAboveBar(true);
        this.graph_view_today.setMaxVisibleValueCount(10);
        this.graph_view_today.setPinchZoom(false);
        this.graph_view_today.setDrawGridBackground(false);
        this.graph_view_today.setExtraBottomOffset(5.0f);
        this.graph_view_today.setDrawBorders(false);
        MyMarkerView myMarkerView = new MyMarkerView(this, R.layout.custom_marker_view);
        myMarkerView.setChartView(this.graph_view_today);
        this.graph_view_today.setMarker(myMarkerView);
        this.graph_view_today.getLegend().setEnabled(this.is_chart_legend_visible);
        Legend legend = this.graph_view_today.getLegend();
        legend.setTypeface(this.tf_quick_bold);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(this.graph_legend_text_size);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setYOffset(0.0f);
        legend.setXOffset(0.0f);
        XAxis xAxis = this.graph_view_today.getXAxis();
        xAxis.setTypeface(this.tf_quick_bold);
        xAxis.setGranularity(1.0f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(0.0f);
        xAxis.setTextSize(this.graph_x_axis_text_size);
        xAxis.setTextColor(getResources().getColor(R.color.axis_label_color));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum((float) GetCholesterolEntries(arrayList).size());
        ArrayList arrayList2 = new ArrayList();
        for (int i4 = 0; i4 < arrayList.size(); i4++) {
            try {
                arrayList2.add(new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("yyyy-MM-dd hh:mm a").parse(String.valueOf(arrayList.get(i4).dateTime))).trim());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        this.graph_view_today.getXAxis().setValueFormatter(new IndexAxisValueFormatter(arrayList2));
        YAxis axisLeft = this.graph_view_today.getAxisLeft();
        axisLeft.removeAllLimitLines();
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeft.setTypeface(this.tf_quick_bold);
        axisLeft.setTextSize(this.graph_left_axis_text_size);
        axisLeft.setTextColor(getResources().getColor(R.color.axis_label_color));
        axisLeft.setDrawGridLines(false);
        axisLeft.setAxisMaximum((float) (i + this.extra_y_axis_value));
        this.graph_view_today.getAxisRight().setEnabled(false);
        SetCholesterolBarWidth(this.graph_view_today, barData, arrayList);
        this.graph_view_today.invalidate();
    }

    private void SetCholesterolMonthlyChart(float i, List<CholesterolChartData> list) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        int i2 = instance.get(1);
        instance.get(5);
        ArrayList<CholesterolChartData> arrayList = (ArrayList<CholesterolChartData>) list;
        ;
        //    (ArrayList) this.SQLite_health_tracker.GetCholesterolChartMonthlyData(i, instance.get(2) + 1, i2);
        if (arrayList.size() == 0) {
            this.lbl_no_data.setVisibility(View.VISIBLE);
            this.lbl_no_data.setText("No Monthly data available!");
        } else {
            this.lbl_no_data.setVisibility(View.GONE);
        }
        List<BarEntry> GetCholesterolEntries = GetCholesterolEntries(arrayList);
        List<BarEntry> GetHDLEntries = GetHDLEntries(arrayList);
        List<BarEntry> GetLDLEntries = GetLDLEntries(arrayList);
        List<BarEntry> GetTriglycerideEntries = GetTriglycerideEntries(arrayList);
        this.dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(GetCholesterolEntries, this.cholesterol_legend_text);
        barDataSet.setColor(getResources().getColor(R.color.gradient_color_1));
        barDataSet.setValueTextColor(getResources().getColor(R.color.gradient_color_1));
        barDataSet.setValueTextSize(10.0f);
        barDataSet.setGradientColor(this.chart_bottom_color_1, this.chart_top_color_1);
        BarDataSet barDataSet2 = new BarDataSet(GetHDLEntries, this.hdl_legend_text);
        barDataSet2.setColors(getResources().getColor(R.color.gradient_color_2));
        barDataSet2.setValueTextColor(getResources().getColor(R.color.gradient_color_2));
        barDataSet2.setValueTextSize(10.0f);
        barDataSet2.setGradientColor(this.chart_bottom_color_2, this.chart_top_color_2);
        BarDataSet barDataSet3 = new BarDataSet(GetLDLEntries, this.ldl_legend_text);
        barDataSet3.setColors(getResources().getColor(R.color.gradient_color_3));
        barDataSet3.setValueTextColor(getResources().getColor(R.color.gradient_color_3));
        barDataSet3.setValueTextSize(10.0f);
        barDataSet3.setGradientColor(this.chart_bottom_color_3, this.chart_top_color_3);
        BarDataSet barDataSet4 = new BarDataSet(GetTriglycerideEntries, this.triglyceride_legend_text);
        barDataSet4.setColors(getResources().getColor(R.color.gradient_color_4));
        barDataSet4.setValueTextColor(getResources().getColor(R.color.gradient_color_4));
        barDataSet4.setValueTextSize(10.0f);
        barDataSet4.setGradientColor(this.chart_bottom_color_4, this.chart_top_color_4);
        this.dataSets.add(barDataSet);
        this.dataSets.add(barDataSet2);
        this.dataSets.add(barDataSet3);
        this.dataSets.add(barDataSet4);
        BarData barData = new BarData(this.dataSets);
        this.graph_view_monthly.setData(barData);
        this.graph_view_monthly.getAxisLeft().setAxisMinimum(0.0f);
        this.graph_view_monthly.getDescription().setEnabled(false);
        this.graph_view_monthly.getAxisRight().setAxisMinimum(0.0f);
        this.graph_view_monthly.setDrawBarShadow(false);
        this.graph_view_monthly.setDrawValueAboveBar(true);
        this.graph_view_monthly.setMaxVisibleValueCount(10);
        this.graph_view_monthly.setPinchZoom(false);
        this.graph_view_monthly.setDrawGridBackground(false);
        this.graph_view_monthly.setExtraBottomOffset(20.0f);
        this.graph_view_monthly.setDrawBorders(false);
        MyMarkerView myMarkerView = new MyMarkerView(this, R.layout.custom_marker_view);
        myMarkerView.setChartView(this.graph_view_monthly);
        this.graph_view_monthly.setMarker(myMarkerView);
        this.graph_view_monthly.getLegend().setEnabled(this.is_chart_legend_visible);
        Legend legend = this.graph_view_monthly.getLegend();
        legend.setTypeface(this.tf_quick_bold);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(this.graph_legend_text_size);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setYOffset(0.0f);
        legend.setXOffset(0.0f);
        XAxis xAxis = this.graph_view_monthly.getXAxis();
        xAxis.setTypeface(this.tf_quick_bold);
        xAxis.setGranularity(1.0f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(0.0f);
        xAxis.setTextSize(this.graph_x_axis_text_size);
        xAxis.setTextColor(getResources().getColor(R.color.axis_label_color));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum((float) GetCholesterolEntries(arrayList).size());
        ArrayList arrayList2 = new ArrayList();
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            try {
                Date parse = new SimpleDateFormat("yyyy-MM-dd hh:mm a").parse(String.valueOf(arrayList.get(i3).dateTime));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm");
                String trim = simpleDateFormat.format(parse).trim();
                String trim2 = simpleDateFormat2.format(parse).trim();
                arrayList2.add(trim + " " + trim2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        this.graph_view_monthly.getXAxis().setValueFormatter(new IndexAxisValueFormatter(arrayList2));
        YAxis axisLeft = this.graph_view_monthly.getAxisLeft();
        axisLeft.removeAllLimitLines();
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeft.setTypeface(this.tf_quick_bold);
        axisLeft.setTextSize(this.graph_left_axis_text_size);
        axisLeft.setTextColor(getResources().getColor(R.color.axis_label_color));
        axisLeft.setDrawGridLines(false);
        axisLeft.setAxisMaximum((float) (i + this.extra_y_axis_value));
        this.graph_view_monthly.getAxisRight().setEnabled(false);
        SetCholesterolBarWidth(this.graph_view_monthly, barData, arrayList);
        this.graph_view_monthly.invalidate();
    }

    private void SetCholesterolYearlyChart(float i, List<CholesterolChartData> list) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        int i2 = instance.get(1);
        instance.get(2);
        instance.get(5);
        ArrayList<CholesterolChartData> arrayList = (ArrayList<CholesterolChartData>) list;
        ;
//                (ArrayList) this.SQLite_health_tracker.GetCholesterolChartYearlyData(i, i2);
        if (arrayList.size() == 0) {
            this.lbl_no_data.setVisibility(View.VISIBLE);
            this.lbl_no_data.setText("No Yearly data available!");
        } else {
            this.lbl_no_data.setVisibility(View.GONE);
        }
        List<BarEntry> GetCholesterolEntries = GetCholesterolEntries(arrayList);
        List<BarEntry> GetHDLEntries = GetHDLEntries(arrayList);
        List<BarEntry> GetLDLEntries = GetLDLEntries(arrayList);
        List<BarEntry> GetTriglycerideEntries = GetTriglycerideEntries(arrayList);
        this.dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(GetCholesterolEntries, this.cholesterol_legend_text);
        barDataSet.setColor(getResources().getColor(R.color.gradient_color_1));
        barDataSet.setValueTextColor(getResources().getColor(R.color.gradient_color_1));
        barDataSet.setValueTextSize(10.0f);
        barDataSet.setGradientColor(this.chart_bottom_color_1, this.chart_top_color_1);
        BarDataSet barDataSet2 = new BarDataSet(GetHDLEntries, this.hdl_legend_text);
        barDataSet2.setColors(getResources().getColor(R.color.gradient_color_2));
        barDataSet2.setValueTextColor(getResources().getColor(R.color.gradient_color_2));
        barDataSet2.setValueTextSize(10.0f);
        barDataSet2.setGradientColor(this.chart_bottom_color_2, this.chart_top_color_2);
        BarDataSet barDataSet3 = new BarDataSet(GetLDLEntries, this.ldl_legend_text);
        barDataSet3.setColors(getResources().getColor(R.color.gradient_color_3));
        barDataSet3.setValueTextColor(getResources().getColor(R.color.gradient_color_3));
        barDataSet3.setValueTextSize(10.0f);
        barDataSet3.setGradientColor(this.chart_bottom_color_3, this.chart_top_color_3);
        BarDataSet barDataSet4 = new BarDataSet(GetTriglycerideEntries, this.triglyceride_legend_text);
        barDataSet4.setColors(getResources().getColor(R.color.gradient_color_4));
        barDataSet4.setValueTextColor(getResources().getColor(R.color.gradient_color_4));
        barDataSet4.setValueTextSize(10.0f);
        barDataSet4.setGradientColor(this.chart_bottom_color_4, this.chart_top_color_4);
        this.dataSets.add(barDataSet);
        this.dataSets.add(barDataSet2);
        this.dataSets.add(barDataSet3);
        this.dataSets.add(barDataSet4);
        BarData barData = new BarData(this.dataSets);
        this.graph_view_yearly.setData(barData);
        this.graph_view_yearly.getAxisLeft().setAxisMinimum(0.0f);
        this.graph_view_yearly.getDescription().setEnabled(false);
        this.graph_view_yearly.getAxisRight().setAxisMinimum(0.0f);
        this.graph_view_yearly.setDrawBarShadow(false);
        this.graph_view_yearly.setDrawValueAboveBar(true);
        this.graph_view_yearly.setMaxVisibleValueCount(10);
        this.graph_view_yearly.setPinchZoom(false);
        this.graph_view_yearly.setDrawGridBackground(false);
        this.graph_view_yearly.setExtraBottomOffset(5.0f);
        this.graph_view_yearly.setDrawBorders(false);
        MyMarkerView myMarkerView = new MyMarkerView(this, R.layout.custom_marker_view);
        myMarkerView.setChartView(this.graph_view_yearly);
        this.graph_view_yearly.setMarker(myMarkerView);
        this.graph_view_yearly.getLegend().setEnabled(this.is_chart_legend_visible);
        Legend legend = this.graph_view_yearly.getLegend();
        legend.setTypeface(this.tf_quick_bold);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(this.graph_legend_text_size);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setYOffset(0.0f);
        legend.setXOffset(0.0f);
        XAxis xAxis = this.graph_view_yearly.getXAxis();
        xAxis.setTypeface(this.tf_quick_bold);
        xAxis.setGranularity(1.0f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(0.0f);
        xAxis.setTextSize(this.graph_x_axis_text_size);
        xAxis.setTextColor(getResources().getColor(R.color.axis_label_color));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum((float) GetCholesterolEntries(arrayList).size());
        ArrayList arrayList2 = new ArrayList();
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            try {
                Date parse = new SimpleDateFormat("yyyy-MM-dd hh:mm a").parse(String.valueOf(arrayList.get(i3).dateTime));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm");
                String trim = simpleDateFormat.format(parse).trim();
                simpleDateFormat2.format(parse).trim();
                arrayList2.add(trim);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        this.graph_view_yearly.getXAxis().setValueFormatter(new IndexAxisValueFormatter(arrayList2));
        YAxis axisLeft = this.graph_view_yearly.getAxisLeft();
        axisLeft.removeAllLimitLines();
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeft.setTypeface(this.tf_quick_bold);
        axisLeft.setTextSize(this.graph_left_axis_text_size);
        axisLeft.setTextColor(getResources().getColor(R.color.axis_label_color));
        axisLeft.setDrawGridLines(false);
        axisLeft.setAxisMaximum((float) (i + this.extra_y_axis_value));
        this.graph_view_yearly.getAxisRight().setEnabled(false);
        SetCholesterolBarWidth(this.graph_view_yearly, barData, arrayList);
        this.graph_view_yearly.invalidate();
    }

    private void SetCholesterolCustomChart(float i, List<CholesterolChartData> list) {
        ArrayList<CholesterolChartData> arrayList = (ArrayList<CholesterolChartData>) list;
        ;
//                (ArrayList) this.SQLite_health_tracker.GetCholesterolChartCustomData(i, this.custom_start_date, this.custom_end_date);
        if (arrayList.size() == 0) {
            this.lbl_no_data.setVisibility(View.VISIBLE);
            this.lbl_no_data.setText("No Yearly data available!");
        } else {
            this.lbl_no_data.setVisibility(View.GONE);
        }
        List<BarEntry> GetCholesterolEntries = GetCholesterolEntries(arrayList);
        List<BarEntry> GetHDLEntries = GetHDLEntries(arrayList);
        List<BarEntry> GetLDLEntries = GetLDLEntries(arrayList);
        List<BarEntry> GetTriglycerideEntries = GetTriglycerideEntries(arrayList);
        this.dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(GetCholesterolEntries, this.cholesterol_legend_text);
        barDataSet.setColor(getResources().getColor(R.color.gradient_color_1));
        barDataSet.setValueTextColor(getResources().getColor(R.color.gradient_color_1));
        barDataSet.setValueTextSize(10.0f);
        barDataSet.setGradientColor(this.chart_bottom_color_1, this.chart_top_color_1);
        BarDataSet barDataSet2 = new BarDataSet(GetHDLEntries, this.hdl_legend_text);
        barDataSet2.setColors(getResources().getColor(R.color.gradient_color_2));
        barDataSet2.setValueTextColor(getResources().getColor(R.color.gradient_color_2));
        barDataSet2.setValueTextSize(10.0f);
        barDataSet2.setGradientColor(this.chart_bottom_color_2, this.chart_top_color_2);
        BarDataSet barDataSet3 = new BarDataSet(GetLDLEntries, this.ldl_legend_text);
        barDataSet3.setColors(getResources().getColor(R.color.gradient_color_3));
        barDataSet3.setValueTextColor(getResources().getColor(R.color.gradient_color_3));
        barDataSet3.setValueTextSize(10.0f);
        barDataSet3.setGradientColor(this.chart_bottom_color_3, this.chart_top_color_3);
        BarDataSet barDataSet4 = new BarDataSet(GetTriglycerideEntries, this.triglyceride_legend_text);
        barDataSet4.setColors(getResources().getColor(R.color.gradient_color_4));
        barDataSet4.setValueTextColor(getResources().getColor(R.color.gradient_color_4));
        barDataSet4.setValueTextSize(10.0f);
        barDataSet4.setGradientColor(this.chart_bottom_color_4, this.chart_top_color_4);
        this.dataSets.add(barDataSet);
        this.dataSets.add(barDataSet2);
        this.dataSets.add(barDataSet3);
        this.dataSets.add(barDataSet4);
        BarData barData = new BarData(this.dataSets);
        this.graph_view_custom.setData(barData);
        this.graph_view_custom.getAxisLeft().setAxisMinimum(0.0f);
        this.graph_view_custom.getDescription().setEnabled(false);
        this.graph_view_custom.getAxisRight().setAxisMinimum(0.0f);
        this.graph_view_custom.setDrawBarShadow(false);
        this.graph_view_custom.setDrawValueAboveBar(true);
        this.graph_view_custom.setMaxVisibleValueCount(10);
        this.graph_view_custom.setPinchZoom(false);
        this.graph_view_custom.setDrawGridBackground(false);
        this.graph_view_custom.setExtraBottomOffset(5.0f);
        this.graph_view_custom.setDrawBorders(false);
        MyMarkerView myMarkerView = new MyMarkerView(this, R.layout.custom_marker_view);
        myMarkerView.setChartView(this.graph_view_custom);
        this.graph_view_custom.setMarker(myMarkerView);
        this.graph_view_custom.getLegend().setEnabled(this.is_chart_legend_visible);
        Legend legend = this.graph_view_custom.getLegend();
        legend.setTypeface(this.tf_quick_bold);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(this.graph_legend_text_size);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setYOffset(0.0f);
        legend.setXOffset(0.0f);
        XAxis xAxis = this.graph_view_custom.getXAxis();
        xAxis.setTypeface(this.tf_quick_bold);
        xAxis.setGranularity(1.0f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(0.0f);
        xAxis.setTextSize(this.graph_x_axis_text_size);
        xAxis.setTextColor(getResources().getColor(R.color.axis_label_color));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum((float) GetTriglycerideEntries(arrayList).size());
        ArrayList arrayList2 = new ArrayList();
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            try {
                Date parse = new SimpleDateFormat("yyyy-MM-dd hh:mm a").parse(String.valueOf(arrayList.get(i2).dateTime));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm a");
                String trim = simpleDateFormat.format(parse).trim();
                simpleDateFormat2.format(parse).trim();
                arrayList2.add(trim);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        this.graph_view_custom.getXAxis().setValueFormatter(new IndexAxisValueFormatter(arrayList2));
        YAxis axisLeft = this.graph_view_custom.getAxisLeft();
        axisLeft.removeAllLimitLines();
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeft.setTypeface(this.tf_quick_bold);
        axisLeft.setTextSize(this.graph_left_axis_text_size);
        axisLeft.setTextColor(getResources().getColor(R.color.axis_label_color));
        axisLeft.setDrawGridLines(false);
        axisLeft.setAxisMaximum((float) (i + this.extra_y_axis_value));
        this.graph_view_custom.getAxisRight().setEnabled(false);
        SetCholesterolBarWidth(this.graph_view_custom, barData, arrayList);
        this.graph_view_custom.invalidate();
    }

    private void SetCholesterolBarWidth(BarChart barChart, BarData barData, ArrayList<CholesterolChartData> arrayList) {
        if (this.dataSets.size() > 1) {
            float size = (0.7f / ((float) this.dataSets.size())) - 0.02f;
            this.defaultBarWidth = size;
            if (size >= 0.0f) {
                barData.setBarWidth(size);
            }
            int size2 = GetCholesterolEntries(arrayList).size();
            if (size2 != -1) {
                barChart.getXAxis().setAxisMinimum(0.0f);
                barChart.getXAxis().setAxisMaximum((barChart.getBarData().getGroupWidth(0.3f, 0.02f) * ((float) size2)) + 0.0f);
                barChart.getXAxis().setCenterAxisLabels(true);
            }
            barChart.groupBars(0.0f, 0.3f, 0.02f);
            barChart.invalidate();
            return;
        }
        barData.setBarWidth(this.bar_width);
        barChart.getXAxis().setCenterAxisLabels(false);
        barChart.getXAxis().resetAxisMaximum();
        barChart.getXAxis().resetAxisMinimum();
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.invalidate();
    }

    private List<BarEntry> GetCholesterolEntries(ArrayList<CholesterolChartData> arrayList) {
        ArrayList arrayList2 = new ArrayList();
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList2.add(new BarEntry((float) i, arrayList.get(i).cholesterol_value));
        }
        return arrayList2.subList(0, arrayList.size());
    }

    private List<BarEntry> GetHDLEntries(ArrayList<CholesterolChartData> arrayList) {
        ArrayList arrayList2 = new ArrayList();
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList2.add(new BarEntry((float) i, arrayList.get(i).hdl_value));
        }
        return arrayList2.subList(0, arrayList.size());
    }

    private List<BarEntry> GetLDLEntries(ArrayList<CholesterolChartData> arrayList) {
        ArrayList arrayList2 = new ArrayList();
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList2.add(new BarEntry((float) i, arrayList.get(i).ldl_value));
        }
        return arrayList2.subList(0, arrayList.size());
    }

    private List<BarEntry> GetTriglycerideEntries(ArrayList<CholesterolChartData> arrayList) {
        ArrayList arrayList2 = new ArrayList();
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList2.add(new BarEntry((float) i, arrayList.get(i).triglyceride_value));
        }
        return arrayList2.subList(0, arrayList.size());
    }

    private void SetHeartRateAllChart(List<HeartRateChartData> data) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        instance.get(1);
        instance.get(2);
        instance.get(5);
        ArrayList<HeartRateChartData> arrayList = (ArrayList) data;
//                this.SQLite_health_tracker.GetHeartRateChartAllData(i);
        if (arrayList.size() == 0) {
            this.lbl_no_data.setVisibility(View.VISIBLE);
            this.lbl_no_data.setText("No data available for All!");
        } else {
            this.lbl_no_data.setVisibility(View.GONE);
        }
        List<BarEntry> GetHeartRateEntries = GetHeartRateEntries(arrayList);
        this.dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(GetHeartRateEntries, this.heart_rate_legend_text);
        barDataSet.setColor(getResources().getColor(R.color.chart_bar_legend_color));
        barDataSet.setValueTextColor(getResources().getColor(R.color.chart_label_color));
        barDataSet.setValueTextSize(10.0f);
        barDataSet.setGradientColor(this.chart_bottom_color, this.chart_top_color);
        this.dataSets.add(barDataSet);
        BarData barData = new BarData(this.dataSets);
        this.graph_view_all.setData(barData);
        this.graph_view_all.getAxisLeft().setAxisMinimum(0.0f);
        this.graph_view_all.getDescription().setEnabled(false);
        this.graph_view_all.getAxisRight().setAxisMinimum(0.0f);
        this.graph_view_all.setDrawBarShadow(false);
        this.graph_view_all.setDrawValueAboveBar(true);
        this.graph_view_all.setMaxVisibleValueCount(10);
        this.graph_view_all.setPinchZoom(false);
        this.graph_view_all.setDrawGridBackground(false);
        this.graph_view_all.setExtraBottomOffset(5.0f);
        this.graph_view_all.setDrawBorders(false);
        MyMarkerView myMarkerView = new MyMarkerView(this, R.layout.custom_marker_view);
        myMarkerView.setChartView(this.graph_view_all);
        this.graph_view_all.setMarker(myMarkerView);
        this.graph_view_all.getLegend().setEnabled(this.is_chart_legend_visible);
        Legend legend = this.graph_view_all.getLegend();
        legend.setTypeface(this.tf_quick_bold);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(this.graph_legend_text_size);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setYOffset(0.0f);
        legend.setXOffset(0.0f);
        XAxis xAxis = this.graph_view_all.getXAxis();
        xAxis.setTypeface(this.tf_quick_bold);
        xAxis.setGranularity(1.0f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(0.0f);
        xAxis.setTextSize(this.graph_x_axis_text_size);
        xAxis.setTextColor(getResources().getColor(R.color.axis_label_color));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum((float) GetHeartRateEntries(arrayList).size());
        ArrayList arrayList2 = new ArrayList();
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            arrayList2.add(arrayList.get(i2).date.trim());
        }
        this.graph_view_all.getXAxis().setValueFormatter(new IndexAxisValueFormatter(arrayList2));
        YAxis axisLeft = this.graph_view_all.getAxisLeft();
        axisLeft.removeAllLimitLines();
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeft.setTypeface(this.tf_quick_bold);
        axisLeft.setTextSize(this.graph_left_axis_text_size);
        axisLeft.setTextColor(getResources().getColor(R.color.axis_label_color));
        axisLeft.setDrawGridLines(false);
        axisLeft.setAxisMaximum((float) (this.SQLite_health_tracker.GetMaxHeartRateValue(data) + this.extra_y_axis_value));
        this.graph_view_all.getAxisRight().setEnabled(false);
        SetHeartRateBarWidth(this.graph_view_all, barData, arrayList);
        this.graph_view_all.invalidate();
    }

    private void SetHeartRateTodayChart(List<HeartRateChartData> data) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        int i2 = instance.get(1);
        int i3 = instance.get(5);
        ArrayList<HeartRateChartData> arrayList = (ArrayList) data;
//                this.SQLite_health_tracker.GetHeartRateChartTodayData(i, i3, instance.get(2) + 1, i2);
        if (arrayList.size() == 0) {
            this.lbl_no_data.setVisibility(View.VISIBLE);
            this.lbl_no_data.setText("No data available for Today!");
        } else {
            this.lbl_no_data.setVisibility(View.GONE);
        }
        List<BarEntry> GetHeartRateEntries = GetHeartRateEntries(arrayList);
        this.dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(GetHeartRateEntries, this.heart_rate_legend_text);
        barDataSet.setColor(getResources().getColor(R.color.chart_bar_legend_color));
        barDataSet.setValueTextColor(getResources().getColor(R.color.chart_label_color));
        barDataSet.setValueTextSize(10.0f);
        barDataSet.setGradientColor(this.chart_bottom_color, this.chart_top_color);
        this.dataSets.add(barDataSet);
        BarData barData = new BarData(this.dataSets);
        this.graph_view_today.setData(barData);
        this.graph_view_today.getAxisLeft().setAxisMinimum(0.0f);
        this.graph_view_today.getDescription().setEnabled(false);
        this.graph_view_today.getAxisRight().setAxisMinimum(0.0f);
        this.graph_view_today.setDrawBarShadow(false);
        this.graph_view_today.setDrawValueAboveBar(true);
        this.graph_view_today.setMaxVisibleValueCount(10);
        this.graph_view_today.setPinchZoom(false);
        this.graph_view_today.setDrawGridBackground(false);
        this.graph_view_today.setExtraBottomOffset(5.0f);
        this.graph_view_today.setDrawBorders(false);
        MyMarkerView myMarkerView = new MyMarkerView(this, R.layout.custom_marker_view);
        myMarkerView.setChartView(this.graph_view_today);
        this.graph_view_today.setMarker(myMarkerView);
        this.graph_view_today.getLegend().setEnabled(this.is_chart_legend_visible);
        Legend legend = this.graph_view_today.getLegend();
        legend.setTypeface(this.tf_quick_bold);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(this.graph_legend_text_size);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setYOffset(0.0f);
        legend.setXOffset(0.0f);
        XAxis xAxis = this.graph_view_today.getXAxis();
        xAxis.setTypeface(this.tf_quick_bold);
        xAxis.setGranularity(1.0f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(0.0f);
        xAxis.setTextSize(this.graph_x_axis_text_size);
        xAxis.setTextColor(getResources().getColor(R.color.axis_label_color));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum((float) GetHeartRateEntries(arrayList).size());
        ArrayList arrayList2 = new ArrayList();
        for (int i4 = 0; i4 < arrayList.size(); i4++) {
            try {
                arrayList2.add(new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("yyyy-MM-dd hh:mm a").parse(String.valueOf(arrayList.get(i4).dateTime))).trim());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        this.graph_view_today.getXAxis().setValueFormatter(new IndexAxisValueFormatter(arrayList2));
        YAxis axisLeft = this.graph_view_today.getAxisLeft();
        axisLeft.removeAllLimitLines();
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeft.setTypeface(this.tf_quick_bold);
        axisLeft.setTextSize(this.graph_left_axis_text_size);
        axisLeft.setTextColor(getResources().getColor(R.color.axis_label_color));
        axisLeft.setDrawGridLines(false);
        axisLeft.setAxisMaximum((float) (this.SQLite_health_tracker.GetMaxHeartRateValue(data) + this.extra_y_axis_value));
        this.graph_view_today.getAxisRight().setEnabled(false);
        SetHeartRateBarWidth(this.graph_view_today, barData, arrayList);
        this.graph_view_today.invalidate();
    }

    private void SetHeartRateMonthlyChart(List<HeartRateChartData> data) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        int i2 = instance.get(1);
        instance.get(5);
        ArrayList<HeartRateChartData> arrayList = (ArrayList) data;
//                this.SQLite_health_tracker.GetHeartRateChartMonthlyData(i, instance.get(2) + 1, i2);
        if (arrayList.size() == 0) {
            this.lbl_no_data.setVisibility(View.VISIBLE);
            this.lbl_no_data.setText("No Monthly data available!");
        } else {
            this.lbl_no_data.setVisibility(View.GONE);
        }
        List<BarEntry> GetHeartRateEntries = GetHeartRateEntries(arrayList);
        this.dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(GetHeartRateEntries, this.heart_rate_legend_text);
        barDataSet.setColor(getResources().getColor(R.color.chart_bar_legend_color));
        barDataSet.setValueTextColor(getResources().getColor(R.color.chart_label_color));
        barDataSet.setValueTextSize(10.0f);
        barDataSet.setGradientColor(this.chart_bottom_color, this.chart_top_color);
        this.dataSets.add(barDataSet);
        BarData barData = new BarData(this.dataSets);
        this.graph_view_monthly.setData(barData);
        this.graph_view_monthly.getAxisLeft().setAxisMinimum(0.0f);
        this.graph_view_monthly.getDescription().setEnabled(false);
        this.graph_view_monthly.getAxisRight().setAxisMinimum(0.0f);
        this.graph_view_monthly.setDrawBarShadow(false);
        this.graph_view_monthly.setDrawValueAboveBar(true);
        this.graph_view_monthly.setMaxVisibleValueCount(10);
        this.graph_view_monthly.setPinchZoom(false);
        this.graph_view_monthly.setDrawGridBackground(false);
        this.graph_view_monthly.setExtraBottomOffset(20.0f);
        this.graph_view_monthly.setDrawBorders(false);
        MyMarkerView myMarkerView = new MyMarkerView(this, R.layout.custom_marker_view);
        myMarkerView.setChartView(this.graph_view_monthly);
        this.graph_view_monthly.setMarker(myMarkerView);
        this.graph_view_monthly.getLegend().setEnabled(this.is_chart_legend_visible);
        Legend legend = this.graph_view_monthly.getLegend();
        legend.setTypeface(this.tf_quick_bold);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(this.graph_legend_text_size);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setYOffset(0.0f);
        legend.setXOffset(0.0f);
        XAxis xAxis = this.graph_view_monthly.getXAxis();
        xAxis.setTypeface(this.tf_quick_bold);
        xAxis.setGranularity(1.0f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(0.0f);
        xAxis.setTextSize(this.graph_x_axis_text_size);
        xAxis.setTextColor(getResources().getColor(R.color.axis_label_color));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum((float) GetHeartRateEntries(arrayList).size());
        ArrayList arrayList2 = new ArrayList();
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            try {
                Date parse = new SimpleDateFormat("yyyy-MM-dd hh:mm a").parse(String.valueOf(arrayList.get(i3).dateTime));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm");
                String trim = simpleDateFormat.format(parse).trim();
                String trim2 = simpleDateFormat2.format(parse).trim();
                arrayList2.add(trim + " " + trim2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        this.graph_view_monthly.getXAxis().setValueFormatter(new IndexAxisValueFormatter(arrayList2));
        YAxis axisLeft = this.graph_view_monthly.getAxisLeft();
        axisLeft.removeAllLimitLines();
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeft.setTypeface(this.tf_quick_bold);
        axisLeft.setTextSize(this.graph_left_axis_text_size);
        axisLeft.setTextColor(getResources().getColor(R.color.axis_label_color));
        axisLeft.setDrawGridLines(false);
        axisLeft.setAxisMaximum((float) (this.SQLite_health_tracker.GetMaxHeartRateValue(data) + this.extra_y_axis_value));
        this.graph_view_monthly.getAxisRight().setEnabled(false);
        SetHeartRateBarWidth(this.graph_view_monthly, barData, arrayList);
        this.graph_view_monthly.invalidate();
    }

    private void SetHeartRateYearlyChart(List<HeartRateChartData> data) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        int i2 = instance.get(1);
        instance.get(2);
        instance.get(5);
        ArrayList<HeartRateChartData> arrayList = (ArrayList) data;
//                this.SQLite_health_tracker.GetHeartRateChartYearlyData(i, i2);
        if (arrayList.size() == 0) {
            this.lbl_no_data.setVisibility(View.VISIBLE);
            this.lbl_no_data.setText("No Yearly data available!");
        } else {
            this.lbl_no_data.setVisibility(View.GONE);
        }
        List<BarEntry> GetHeartRateEntries = GetHeartRateEntries(arrayList);
        this.dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(GetHeartRateEntries, this.heart_rate_legend_text);
        barDataSet.setColor(getResources().getColor(R.color.chart_bar_legend_color));
        barDataSet.setValueTextColor(getResources().getColor(R.color.chart_label_color));
        barDataSet.setValueTextSize(10.0f);
        barDataSet.setGradientColor(this.chart_bottom_color, this.chart_top_color);
        this.dataSets.add(barDataSet);
        BarData barData = new BarData(this.dataSets);
        this.graph_view_yearly.setData(barData);
        this.graph_view_yearly.getAxisLeft().setAxisMinimum(0.0f);
        this.graph_view_yearly.getDescription().setEnabled(false);
        this.graph_view_yearly.getAxisRight().setAxisMinimum(0.0f);
        this.graph_view_yearly.setDrawBarShadow(false);
        this.graph_view_yearly.setDrawValueAboveBar(true);
        this.graph_view_yearly.setMaxVisibleValueCount(10);
        this.graph_view_yearly.setPinchZoom(false);
        this.graph_view_yearly.setDrawGridBackground(false);
        this.graph_view_yearly.setExtraBottomOffset(5.0f);
        this.graph_view_yearly.setDrawBorders(false);
        MyMarkerView myMarkerView = new MyMarkerView(this, R.layout.custom_marker_view);
        myMarkerView.setChartView(this.graph_view_yearly);
        this.graph_view_yearly.setMarker(myMarkerView);
        this.graph_view_yearly.getLegend().setEnabled(this.is_chart_legend_visible);
        Legend legend = this.graph_view_yearly.getLegend();
        legend.setTypeface(this.tf_quick_bold);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(this.graph_legend_text_size);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setYOffset(0.0f);
        legend.setXOffset(0.0f);
        XAxis xAxis = this.graph_view_yearly.getXAxis();
        xAxis.setTypeface(this.tf_quick_bold);
        xAxis.setGranularity(1.0f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(0.0f);
        xAxis.setTextSize(this.graph_x_axis_text_size);
        xAxis.setTextColor(getResources().getColor(R.color.axis_label_color));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum((float) GetHeartRateEntries(arrayList).size());
        ArrayList arrayList2 = new ArrayList();
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            try {
                Date parse = new SimpleDateFormat("yyyy-MM-dd hh:mm a").parse(String.valueOf(arrayList.get(i3).dateTime));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm");
                String trim = simpleDateFormat.format(parse).trim();
                simpleDateFormat2.format(parse).trim();
                arrayList2.add(trim);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        this.graph_view_yearly.getXAxis().setValueFormatter(new IndexAxisValueFormatter(arrayList2));
        YAxis axisLeft = this.graph_view_yearly.getAxisLeft();
        axisLeft.removeAllLimitLines();
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeft.setTypeface(this.tf_quick_bold);
        axisLeft.setTextSize(this.graph_left_axis_text_size);
        axisLeft.setTextColor(getResources().getColor(R.color.axis_label_color));
        axisLeft.setDrawGridLines(false);
        axisLeft.setAxisMaximum((float) (this.SQLite_health_tracker.GetMaxHeartRateValue(data) + this.extra_y_axis_value));
        this.graph_view_yearly.getAxisRight().setEnabled(false);
        SetHeartRateBarWidth(this.graph_view_yearly, barData, arrayList);
        this.graph_view_yearly.invalidate();
    }

    private void SetHeartRateCustomChart(List<HeartRateChartData> data) {
        ArrayList<HeartRateChartData> arrayList = (ArrayList) data;
//                (ArrayList) this.SQLite_health_tracker.GetHeartRateChartCustomData(i, this.custom_start_date, this.custom_end_date);
        if (arrayList.size() == 0) {
            this.lbl_no_data.setVisibility(View.VISIBLE);
            this.lbl_no_data.setText("No Yearly data available!");
        } else {
            this.lbl_no_data.setVisibility(View.GONE);
        }
        List<BarEntry> GetHeartRateEntries = GetHeartRateEntries(arrayList);
        this.dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(GetHeartRateEntries, this.heart_rate_legend_text);
        barDataSet.setColor(getResources().getColor(R.color.chart_bar_legend_color));
        barDataSet.setValueTextColor(getResources().getColor(R.color.chart_label_color));
        barDataSet.setValueTextSize(10.0f);
        barDataSet.setGradientColor(this.chart_bottom_color, this.chart_top_color);
        this.dataSets.add(barDataSet);
        BarData barData = new BarData(this.dataSets);
        this.graph_view_custom.setData(barData);
        this.graph_view_custom.getAxisLeft().setAxisMinimum(0.0f);
        this.graph_view_custom.getDescription().setEnabled(false);
        this.graph_view_custom.getAxisRight().setAxisMinimum(0.0f);
        this.graph_view_custom.setDrawBarShadow(false);
        this.graph_view_custom.setDrawValueAboveBar(true);
        this.graph_view_custom.setMaxVisibleValueCount(10);
        this.graph_view_custom.setPinchZoom(false);
        this.graph_view_custom.setDrawGridBackground(false);
        this.graph_view_custom.setExtraBottomOffset(5.0f);
        this.graph_view_custom.setDrawBorders(false);
        MyMarkerView myMarkerView = new MyMarkerView(this, R.layout.custom_marker_view);
        myMarkerView.setChartView(this.graph_view_custom);
        this.graph_view_custom.setMarker(myMarkerView);
        this.graph_view_custom.getLegend().setEnabled(this.is_chart_legend_visible);
        Legend legend = this.graph_view_custom.getLegend();
        legend.setTypeface(this.tf_quick_bold);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(this.graph_legend_text_size);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setYOffset(0.0f);
        legend.setXOffset(0.0f);
        XAxis xAxis = this.graph_view_custom.getXAxis();
        xAxis.setTypeface(this.tf_quick_bold);
        xAxis.setGranularity(1.0f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(0.0f);
        xAxis.setTextSize(this.graph_x_axis_text_size);
        xAxis.setTextColor(getResources().getColor(R.color.axis_label_color));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum((float) GetHeartRateEntries(arrayList).size());
        ArrayList arrayList2 = new ArrayList();
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            try {
                arrayList2.add(arrayList.get(i2).date.trim());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.graph_view_custom.getXAxis().setValueFormatter(new IndexAxisValueFormatter(arrayList2));
        YAxis axisLeft = this.graph_view_custom.getAxisLeft();
        axisLeft.removeAllLimitLines();
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeft.setTypeface(this.tf_quick_bold);
        axisLeft.setTextSize(this.graph_left_axis_text_size);
        axisLeft.setTextColor(getResources().getColor(R.color.axis_label_color));
        axisLeft.setDrawGridLines(false);
        axisLeft.setAxisMaximum((float) (this.SQLite_health_tracker.GetMaxHeartRateValue(data) + this.extra_y_axis_value));
        this.graph_view_custom.getAxisRight().setEnabled(false);
        SetHeartRateBarWidth(this.graph_view_custom, barData, arrayList);
        this.graph_view_custom.invalidate();
    }

    private void SetHeartRateBarWidth(BarChart barChart, BarData barData, ArrayList<HeartRateChartData> arrayList) {
        if (this.dataSets.size() > 1) {
            float size = (0.7f / ((float) this.dataSets.size())) - 0.02f;
            this.defaultBarWidth = size;
            if (size >= 0.0f) {
                barData.setBarWidth(size);
            }
            int size2 = GetHeartRateEntries(arrayList).size();
            if (size2 != -1) {
                barChart.getXAxis().setAxisMinimum(0.0f);
                barChart.getXAxis().setAxisMaximum((barChart.getBarData().getGroupWidth(0.3f, 0.02f) * ((float) size2)) + 0.0f);
                barChart.getXAxis().setCenterAxisLabels(true);
            }
            barChart.groupBars(0.0f, 0.3f, 0.02f);
            barChart.invalidate();
            return;
        }
        barData.setBarWidth(this.bar_width);
        barChart.getXAxis().setCenterAxisLabels(false);
        barChart.getXAxis().resetAxisMaximum();
        barChart.getXAxis().resetAxisMinimum();
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.invalidate();
    }

    private List<BarEntry> GetHeartRateEntries(ArrayList<HeartRateChartData> arrayList) {
        ArrayList arrayList2 = new ArrayList();
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList2.add(new BarEntry((float) i, (float) arrayList.get(i).heart_rate_value));
        }
        return arrayList2.subList(0, arrayList.size());
    }

    private void SetWeightAllChart(List<WeightChartData> data) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        instance.get(1);
        instance.get(2);
        instance.get(5);
        ArrayList<WeightChartData> arrayList = (ArrayList) data;
//                this.SQLite_health_tracker.GetWeightChartAllData(i);
        if (arrayList.size() == 0) {
            this.lbl_no_data.setVisibility(View.VISIBLE);
            this.lbl_no_data.setText("No data available for All!");
        } else {
            this.lbl_no_data.setVisibility(View.GONE);
        }
        List<BarEntry> GetWeightEntries = GetWeightEntries(arrayList);
        this.dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(GetWeightEntries, this.weight_legend_text);
        barDataSet.setColor(getResources().getColor(R.color.chart_bar_legend_color));
        barDataSet.setValueTextColor(getResources().getColor(R.color.chart_label_color));
        barDataSet.setValueTextSize(10.0f);
        barDataSet.setGradientColor(this.chart_bottom_color, this.chart_top_color);
        this.dataSets.add(barDataSet);
        BarData barData = new BarData(this.dataSets);
        this.graph_view_all.setData(barData);
        this.graph_view_all.getAxisLeft().setAxisMinimum(0.0f);
        this.graph_view_all.getDescription().setEnabled(false);
        this.graph_view_all.getAxisRight().setAxisMinimum(0.0f);
        this.graph_view_all.setDrawBarShadow(false);
        this.graph_view_all.setDrawValueAboveBar(true);
        this.graph_view_all.setMaxVisibleValueCount(10);
        this.graph_view_all.setPinchZoom(false);
        this.graph_view_all.setDrawGridBackground(false);
        this.graph_view_all.setExtraBottomOffset(5.0f);
        this.graph_view_all.setDrawBorders(false);
        MyMarkerView myMarkerView = new MyMarkerView(this, R.layout.custom_marker_view);
        myMarkerView.setChartView(this.graph_view_all);
        this.graph_view_all.setMarker(myMarkerView);
        this.graph_view_all.getLegend().setEnabled(this.is_chart_legend_visible);
        Legend legend = this.graph_view_all.getLegend();
        legend.setTypeface(this.tf_quick_bold);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(this.graph_legend_text_size);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setYOffset(0.0f);
        legend.setXOffset(0.0f);
        XAxis xAxis = this.graph_view_all.getXAxis();
        xAxis.setTypeface(this.tf_quick_bold);
        xAxis.setGranularity(1.0f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(0.0f);
        xAxis.setTextSize(this.graph_x_axis_text_size);
        xAxis.setTextColor(getResources().getColor(R.color.axis_label_color));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum((float) GetWeightEntries(arrayList).size());
        ArrayList arrayList2 = new ArrayList();
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            arrayList2.add(arrayList.get(i2).date.trim());
        }
        this.graph_view_all.getXAxis().setValueFormatter(new IndexAxisValueFormatter(arrayList2));
        YAxis axisLeft = this.graph_view_all.getAxisLeft();
        axisLeft.removeAllLimitLines();
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeft.setTypeface(this.tf_quick_bold);
        axisLeft.setTextSize(this.graph_left_axis_text_size);
        axisLeft.setTextColor(getResources().getColor(R.color.axis_label_color));
        axisLeft.setDrawGridLines(false);
        axisLeft.setAxisMaximum((float) (this.SQLite_health_tracker.GetMaxWeightValue(data) + this.extra_y_axis_value));
        this.graph_view_all.getAxisRight().setEnabled(false);
        SetWeightBarWidth(this.graph_view_all, barData, arrayList);
        this.graph_view_all.invalidate();
    }

    private void SetWeightTodayChart(List<WeightChartData> data) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        int i2 = instance.get(1);
        int i3 = instance.get(5);
        ArrayList<WeightChartData> arrayList = (ArrayList) data;
//                this.SQLite_health_tracker.GetWeightChartTodayData(i, i3, instance.get(2) + 1, i2);
        if (arrayList.size() == 0) {
            this.lbl_no_data.setVisibility(View.VISIBLE);
            this.lbl_no_data.setText("No data available for Today!");
        } else {
            this.lbl_no_data.setVisibility(View.GONE);
        }
        List<BarEntry> GetWeightEntries = GetWeightEntries(arrayList);
        this.dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(GetWeightEntries, this.weight_legend_text);
        barDataSet.setColor(getResources().getColor(R.color.chart_bar_legend_color));
        barDataSet.setValueTextColor(getResources().getColor(R.color.chart_label_color));
        barDataSet.setValueTextSize(10.0f);
        barDataSet.setGradientColor(this.chart_bottom_color, this.chart_top_color);
        this.dataSets.add(barDataSet);
        BarData barData = new BarData(this.dataSets);
        this.graph_view_today.setData(barData);
        this.graph_view_today.getAxisLeft().setAxisMinimum(0.0f);
        this.graph_view_today.getDescription().setEnabled(false);
        this.graph_view_today.getAxisRight().setAxisMinimum(0.0f);
        this.graph_view_today.setDrawBarShadow(false);
        this.graph_view_today.setDrawValueAboveBar(true);
        this.graph_view_today.setMaxVisibleValueCount(10);
        this.graph_view_today.setPinchZoom(false);
        this.graph_view_today.setDrawGridBackground(false);
        this.graph_view_today.setExtraBottomOffset(5.0f);
        this.graph_view_today.setDrawBorders(false);
        MyMarkerView myMarkerView = new MyMarkerView(this, R.layout.custom_marker_view);
        myMarkerView.setChartView(this.graph_view_today);
        this.graph_view_today.setMarker(myMarkerView);
        this.graph_view_today.getLegend().setEnabled(this.is_chart_legend_visible);
        Legend legend = this.graph_view_today.getLegend();
        legend.setTypeface(this.tf_quick_bold);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(this.graph_legend_text_size);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setYOffset(0.0f);
        legend.setXOffset(0.0f);
        XAxis xAxis = this.graph_view_today.getXAxis();
        xAxis.setTypeface(this.tf_quick_bold);
        xAxis.setGranularity(1.0f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(0.0f);
        xAxis.setTextSize(this.graph_x_axis_text_size);
        xAxis.setTextColor(getResources().getColor(R.color.axis_label_color));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum((float) GetWeightEntries(arrayList).size());
        ArrayList arrayList2 = new ArrayList();
        for (int i4 = 0; i4 < arrayList.size(); i4++) {
            try {
                arrayList2.add(new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("yyyy-MM-dd hh:mm a").parse(String.valueOf(arrayList.get(i4).dateTime))).trim());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        this.graph_view_today.getXAxis().setValueFormatter(new IndexAxisValueFormatter(arrayList2));
        YAxis axisLeft = this.graph_view_today.getAxisLeft();
        axisLeft.removeAllLimitLines();
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeft.setTypeface(this.tf_quick_bold);
        axisLeft.setTextSize(this.graph_left_axis_text_size);
        axisLeft.setTextColor(getResources().getColor(R.color.axis_label_color));
        axisLeft.setDrawGridLines(false);
        axisLeft.setAxisMaximum((float) (this.SQLite_health_tracker.GetMaxWeightValue(data) + this.extra_y_axis_value));
        this.graph_view_today.getAxisRight().setEnabled(false);
        SetWeightBarWidth(this.graph_view_today, barData, arrayList);
        this.graph_view_today.invalidate();
    }

    private void SetWeightMonthlyChart(List<WeightChartData> data) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        int i2 = instance.get(1);
        instance.get(5);
        ArrayList<WeightChartData> arrayList = (ArrayList) data;
//                this.SQLite_health_tracker.GetWeightChartMonthlyData(i, instance.get(2) + 1, i2);
        if (arrayList.size() == 0) {
            this.lbl_no_data.setVisibility(View.VISIBLE);
            this.lbl_no_data.setText("No Monthly data available!");
        } else {
            this.lbl_no_data.setVisibility(View.GONE);
        }
        List<BarEntry> GetWeightEntries = GetWeightEntries(arrayList);
        this.dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(GetWeightEntries, this.weight_legend_text);
        barDataSet.setColor(getResources().getColor(R.color.chart_bar_legend_color));
        barDataSet.setValueTextColor(getResources().getColor(R.color.chart_label_color));
        barDataSet.setValueTextSize(10.0f);
        barDataSet.setGradientColor(this.chart_bottom_color, this.chart_top_color);
        this.dataSets.add(barDataSet);
        BarData barData = new BarData(this.dataSets);
        this.graph_view_monthly.setData(barData);
        this.graph_view_monthly.getAxisLeft().setAxisMinimum(0.0f);
        this.graph_view_monthly.getDescription().setEnabled(false);
        this.graph_view_monthly.getAxisRight().setAxisMinimum(0.0f);
        this.graph_view_monthly.setDrawBarShadow(false);
        this.graph_view_monthly.setDrawValueAboveBar(true);
        this.graph_view_monthly.setMaxVisibleValueCount(10);
        this.graph_view_monthly.setPinchZoom(false);
        this.graph_view_monthly.setDrawGridBackground(false);
        this.graph_view_monthly.setExtraBottomOffset(20.0f);
        this.graph_view_monthly.setDrawBorders(false);
        MyMarkerView myMarkerView = new MyMarkerView(this, R.layout.custom_marker_view);
        myMarkerView.setChartView(this.graph_view_monthly);
        this.graph_view_monthly.setMarker(myMarkerView);
        this.graph_view_monthly.getLegend().setEnabled(this.is_chart_legend_visible);
        Legend legend = this.graph_view_monthly.getLegend();
        legend.setTypeface(this.tf_quick_bold);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(this.graph_legend_text_size);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setYOffset(0.0f);
        legend.setXOffset(0.0f);
        XAxis xAxis = this.graph_view_monthly.getXAxis();
        xAxis.setTypeface(this.tf_quick_bold);
        xAxis.setGranularity(1.0f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(0.0f);
        xAxis.setTextSize(this.graph_x_axis_text_size);
        xAxis.setTextColor(getResources().getColor(R.color.axis_label_color));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum((float) GetWeightEntries(arrayList).size());
        ArrayList arrayList2 = new ArrayList();
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            try {
                Date parse = new SimpleDateFormat("yyyy-MM-dd hh:mm a").parse(String.valueOf(arrayList.get(i3).dateTime));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm");
                String trim = simpleDateFormat.format(parse).trim();
                String trim2 = simpleDateFormat2.format(parse).trim();
                arrayList2.add(trim + " " + trim2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        this.graph_view_monthly.getXAxis().setValueFormatter(new IndexAxisValueFormatter(arrayList2));
        YAxis axisLeft = this.graph_view_monthly.getAxisLeft();
        axisLeft.removeAllLimitLines();
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeft.setTypeface(this.tf_quick_bold);
        axisLeft.setTextSize(this.graph_left_axis_text_size);
        axisLeft.setTextColor(getResources().getColor(R.color.axis_label_color));
        axisLeft.setDrawGridLines(false);
        axisLeft.setAxisMaximum((float) (this.SQLite_health_tracker.GetMaxWeightValue(data) + this.extra_y_axis_value));
        this.graph_view_monthly.getAxisRight().setEnabled(false);
        SetWeightBarWidth(this.graph_view_monthly, barData, arrayList);
        this.graph_view_monthly.invalidate();
    }

    private void SetWeightYearlyChart(List<WeightChartData> data) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        int i2 = instance.get(1);
        instance.get(2);
        instance.get(5);
        ArrayList<WeightChartData> arrayList = (ArrayList) data;
//                this.SQLite_health_tracker.GetWeightChartYearlyData(i, i2);
        if (arrayList.size() == 0) {
            this.lbl_no_data.setVisibility(View.VISIBLE);
            this.lbl_no_data.setText("No Yearly data available!");
        } else {
            this.lbl_no_data.setVisibility(View.GONE);
        }
        List<BarEntry> GetWeightEntries = GetWeightEntries(arrayList);
        this.dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(GetWeightEntries, this.weight_legend_text);
        barDataSet.setColor(getResources().getColor(R.color.chart_bar_legend_color));
        barDataSet.setValueTextColor(getResources().getColor(R.color.chart_label_color));
        barDataSet.setValueTextSize(10.0f);
        barDataSet.setGradientColor(this.chart_bottom_color, this.chart_top_color);
        this.dataSets.add(barDataSet);
        BarData barData = new BarData(this.dataSets);
        this.graph_view_yearly.setData(barData);
        this.graph_view_yearly.getAxisLeft().setAxisMinimum(0.0f);
        this.graph_view_yearly.getDescription().setEnabled(false);
        this.graph_view_yearly.getAxisRight().setAxisMinimum(0.0f);
        this.graph_view_yearly.setDrawBarShadow(false);
        this.graph_view_yearly.setDrawValueAboveBar(true);
        this.graph_view_yearly.setMaxVisibleValueCount(10);
        this.graph_view_yearly.setPinchZoom(false);
        this.graph_view_yearly.setDrawGridBackground(false);
        this.graph_view_yearly.setExtraBottomOffset(5.0f);
        this.graph_view_yearly.setDrawBorders(false);
        MyMarkerView myMarkerView = new MyMarkerView(this, R.layout.custom_marker_view);
        myMarkerView.setChartView(this.graph_view_yearly);
        this.graph_view_yearly.setMarker(myMarkerView);
        this.graph_view_yearly.getLegend().setEnabled(this.is_chart_legend_visible);
        Legend legend = this.graph_view_yearly.getLegend();
        legend.setTypeface(this.tf_quick_bold);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(this.graph_legend_text_size);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setYOffset(0.0f);
        legend.setXOffset(0.0f);
        XAxis xAxis = this.graph_view_yearly.getXAxis();
        xAxis.setTypeface(this.tf_quick_bold);
        xAxis.setGranularity(1.0f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(0.0f);
        xAxis.setTextSize(this.graph_x_axis_text_size);
        xAxis.setTextColor(getResources().getColor(R.color.axis_label_color));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum((float) GetWeightEntries(arrayList).size());
        ArrayList arrayList2 = new ArrayList();
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            try {
                Date parse = new SimpleDateFormat("yyyy-MM-dd hh:mm a").parse(String.valueOf(arrayList.get(i3).dateTime));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm");
                String trim = simpleDateFormat.format(parse).trim();
                simpleDateFormat2.format(parse).trim();
                arrayList2.add(trim);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        this.graph_view_yearly.getXAxis().setValueFormatter(new IndexAxisValueFormatter(arrayList2));
        YAxis axisLeft = this.graph_view_yearly.getAxisLeft();
        axisLeft.removeAllLimitLines();
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeft.setTypeface(this.tf_quick_bold);
        axisLeft.setTextSize(this.graph_left_axis_text_size);
        axisLeft.setTextColor(getResources().getColor(R.color.axis_label_color));
        axisLeft.setDrawGridLines(false);
        axisLeft.setAxisMaximum((float) (this.SQLite_health_tracker.GetMaxWeightValue(data) + this.extra_y_axis_value));
        this.graph_view_yearly.getAxisRight().setEnabled(false);
        SetWeightBarWidth(this.graph_view_yearly, barData, arrayList);
        this.graph_view_yearly.invalidate();
    }

    private void SetWeightCustomChart(List<WeightChartData> data) {
        ArrayList<WeightChartData> arrayList = (ArrayList) data;
//                this.SQLite_health_tracker.GetWeightChartCustomData(i, this.custom_start_date, this.custom_end_date);
        if (arrayList.size() == 0) {
            this.lbl_no_data.setVisibility(View.VISIBLE);
            this.lbl_no_data.setText("No Yearly data available!");
        } else {
            this.lbl_no_data.setVisibility(View.GONE);
        }
        List<BarEntry> GetWeightEntries = GetWeightEntries(arrayList);
        this.dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(GetWeightEntries, this.weight_legend_text);
        barDataSet.setColor(getResources().getColor(R.color.chart_bar_legend_color));
        barDataSet.setValueTextColor(getResources().getColor(R.color.chart_label_color));
        barDataSet.setValueTextSize(10.0f);
        barDataSet.setGradientColor(this.chart_bottom_color, this.chart_top_color);
        this.dataSets.add(barDataSet);
        BarData barData = new BarData(this.dataSets);
        this.graph_view_custom.setData(barData);
        this.graph_view_custom.getAxisLeft().setAxisMinimum(0.0f);
        this.graph_view_custom.getDescription().setEnabled(false);
        this.graph_view_custom.getAxisRight().setAxisMinimum(0.0f);
        this.graph_view_custom.setDrawBarShadow(false);
        this.graph_view_custom.setDrawValueAboveBar(true);
        this.graph_view_custom.setMaxVisibleValueCount(10);
        this.graph_view_custom.setPinchZoom(false);
        this.graph_view_custom.setDrawGridBackground(false);
        this.graph_view_custom.setExtraBottomOffset(5.0f);
        this.graph_view_custom.setDrawBorders(false);
        MyMarkerView myMarkerView = new MyMarkerView(this, R.layout.custom_marker_view);
        myMarkerView.setChartView(this.graph_view_custom);
        this.graph_view_custom.setMarker(myMarkerView);
        this.graph_view_custom.getLegend().setEnabled(this.is_chart_legend_visible);
        Legend legend = this.graph_view_custom.getLegend();
        legend.setTypeface(this.tf_quick_bold);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(this.graph_legend_text_size);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setYOffset(0.0f);
        legend.setXOffset(0.0f);
        XAxis xAxis = this.graph_view_custom.getXAxis();
        xAxis.setTypeface(this.tf_quick_bold);
        xAxis.setGranularity(1.0f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(0.0f);
        xAxis.setTextSize(this.graph_x_axis_text_size);
        xAxis.setTextColor(getResources().getColor(R.color.axis_label_color));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum((float) GetWeightEntries(arrayList).size());
        ArrayList arrayList2 = new ArrayList();
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            try {
                arrayList2.add(arrayList.get(i2).date.trim());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.graph_view_custom.getXAxis().setValueFormatter(new IndexAxisValueFormatter(arrayList2));
        YAxis axisLeft = this.graph_view_custom.getAxisLeft();
        axisLeft.removeAllLimitLines();
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeft.setTypeface(this.tf_quick_bold);
        axisLeft.setTextSize(this.graph_left_axis_text_size);
        axisLeft.setTextColor(getResources().getColor(R.color.axis_label_color));
        axisLeft.setDrawGridLines(false);
        axisLeft.setAxisMaximum((float) (this.SQLite_health_tracker.GetMaxWeightValue(data) + this.extra_y_axis_value));
        this.graph_view_custom.getAxisRight().setEnabled(false);
        SetWeightBarWidth(this.graph_view_custom, barData, arrayList);
        this.graph_view_custom.invalidate();
    }

    private void SetWeightBarWidth(BarChart barChart, BarData barData, ArrayList<WeightChartData> arrayList) {
        if (this.dataSets.size() > 1) {
            float size = (0.7f / ((float) this.dataSets.size())) - 0.02f;
            this.defaultBarWidth = size;
            if (size >= 0.0f) {
                barData.setBarWidth(size);
            }
            int size2 = GetWeightEntries(arrayList).size();
            if (size2 != -1) {
                barChart.getXAxis().setAxisMinimum(0.0f);
                barChart.getXAxis().setAxisMaximum((barChart.getBarData().getGroupWidth(0.3f, 0.02f) * ((float) size2)) + 0.0f);
                barChart.getXAxis().setCenterAxisLabels(true);
            }
            barChart.groupBars(0.0f, 0.3f, 0.02f);
            barChart.invalidate();
            return;
        }
        barData.setBarWidth(this.bar_width);
        barChart.getXAxis().setCenterAxisLabels(false);
        barChart.getXAxis().resetAxisMaximum();
        barChart.getXAxis().resetAxisMinimum();
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.invalidate();
    }

    private List<BarEntry> GetWeightEntries(ArrayList<WeightChartData> arrayList) {
        ArrayList arrayList2 = new ArrayList();
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList2.add(new BarEntry((float) i, arrayList.get(i).weight));
        }
        return arrayList2.subList(0, arrayList.size());
    }


    private void GetDefaultSpinnerValues() {
        String trim = this.arrayReportTypes[this.spinner_report_types.getSelectedItemPosition()].trim();
        this.current_report_type = trim;
        SetChartByUserIDReportType(this.sharedPreferencesUtils.getUserId(), trim, this.current_report_duration);
    }


    private void SetChartByUserIDReportType(int i, String str, String str2) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        int day = instance.get(Calendar.DAY_OF_MONTH);
        int month = instance.get(Calendar.MONTH) + 1; // Month starts from 0, so we add 1
        int year = instance.get(Calendar.YEAR);

        BloodCountChartRequest bloodCountChartRequest = new BloodCountChartRequest(
                sharedPreferencesUtils.getUserId(), day, month, year, custom_start_date, custom_end_date);

        if (str.equalsIgnoreCase(getResources().getString(R.string.lbl_chart_blood_count))) {
            String string = getResources().getString(R.string.lbl_m_mm3);
            TextView textView = this.txt_chart_type;
            textView.setText(getResources().getString(R.string.lbl_chart_blood_count) + " (" + string + ")");

            if (str2.equalsIgnoreCase(getResources().getString(R.string.lbl_all))) {
                this.graph_view_today.setVisibility(View.GONE);
                this.graph_view_monthly.setVisibility(View.GONE);
                this.graph_view_yearly.setVisibility(View.GONE);
                this.graph_view_all.setVisibility(View.VISIBLE);
                this.graph_view_custom.setVisibility(View.GONE);
                this.graph_view_all.invalidate();
                this.graph_view_all.clear();
                getBloodCountChartAllData(bloodCountChartRequest);
//                SetBloodCountAllChart(i);
            } else if (str2.equalsIgnoreCase(getResources().getString(R.string.lbl_today))) {
                this.graph_view_today.setVisibility(View.VISIBLE);
                this.graph_view_monthly.setVisibility(View.GONE);
                this.graph_view_yearly.setVisibility(View.GONE);
                this.graph_view_all.setVisibility(View.GONE);
                this.graph_view_custom.setVisibility(View.GONE);
                this.graph_view_today.invalidate();
                this.graph_view_today.clear();
                getBloodCountChartTodayData(bloodCountChartRequest);
//                SetBloodCountTodayChart(i);
            } else if (str2.equalsIgnoreCase(getResources().getString(R.string.lbl_month))) {
                this.graph_view_today.setVisibility(View.GONE);
                this.graph_view_monthly.setVisibility(View.VISIBLE);
                this.graph_view_yearly.setVisibility(View.GONE);
                this.graph_view_all.setVisibility(View.GONE);
                this.graph_view_custom.setVisibility(View.GONE);
                this.graph_view_monthly.invalidate();
                this.graph_view_monthly.clear();

                getBloodCountChartMonthlyData(bloodCountChartRequest);

//                SetBloodCountMonthlyChart(i);

            } else if (str2.equalsIgnoreCase(getResources().getString(R.string.lbl_year))) {
                this.graph_view_today.setVisibility(View.GONE);
                this.graph_view_monthly.setVisibility(View.GONE);
                this.graph_view_yearly.setVisibility(View.VISIBLE);
                this.graph_view_all.setVisibility(View.GONE);
                this.graph_view_custom.setVisibility(View.GONE);
                this.graph_view_yearly.invalidate();
                this.graph_view_yearly.clear();

                getBloodCountChartYearlyData(bloodCountChartRequest);
//                SetBloodCountYearlyChart(i);
            } else if (str2.equalsIgnoreCase(getResources().getString(R.string.lbl_custom))) {
                this.graph_view_today.setVisibility(View.GONE);
                this.graph_view_monthly.setVisibility(View.GONE);
                this.graph_view_yearly.setVisibility(View.GONE);
                this.graph_view_all.setVisibility(View.GONE);
                this.graph_view_custom.setVisibility(View.VISIBLE);
                this.graph_view_custom.invalidate();
                this.graph_view_custom.clear();

                getBloodCountChartCustomData(bloodCountChartRequest);
//                SetBloodCountCustomChart(i);
            }
        } else if (str.equalsIgnoreCase(getResources().getString(R.string.lbl_chart_blood_pressure))) {
            String string2 = getResources().getString(R.string.lbl_mg_dl);
            TextView textView2 = this.txt_chart_type;
            textView2.setText(getResources().getString(R.string.lbl_chart_blood_pressure) + " (" + string2 + ")");
            if (str2.equalsIgnoreCase(getResources().getString(R.string.lbl_all))) {
                this.graph_view_today.setVisibility(View.GONE);
                this.graph_view_monthly.setVisibility(View.GONE);
                this.graph_view_yearly.setVisibility(View.GONE);
                this.graph_view_all.setVisibility(View.VISIBLE);
                this.graph_view_custom.setVisibility(View.GONE);
                this.graph_view_all.invalidate();
                this.graph_view_all.clear();
                getBloodPressureChartAllData(bloodCountChartRequest);
//                 SetBloodPressureAllChart(i);
            } else if (str2.equalsIgnoreCase(getResources().getString(R.string.lbl_today))) {
                this.graph_view_today.setVisibility(View.VISIBLE);
                this.graph_view_monthly.setVisibility(View.GONE);
                this.graph_view_yearly.setVisibility(View.GONE);
                this.graph_view_all.setVisibility(View.GONE);
                this.graph_view_custom.setVisibility(View.GONE);
                this.graph_view_today.invalidate();
                this.graph_view_today.clear();
                getBloodPressureTodayData(bloodCountChartRequest);
//                SetBloodPressureTodayChart(i);
            } else if (str2.equalsIgnoreCase(getResources().getString(R.string.lbl_month))) {
                this.graph_view_today.setVisibility(View.GONE);
                this.graph_view_monthly.setVisibility(View.VISIBLE);
                this.graph_view_yearly.setVisibility(View.GONE);
                this.graph_view_all.setVisibility(View.GONE);
                this.graph_view_custom.setVisibility(View.GONE);
                this.graph_view_monthly.invalidate();
                this.graph_view_monthly.clear();
                getBloodPressureMonthlyData(bloodCountChartRequest);
//                SetBloodPressureMonthlyChart(i);
            } else if (str2.equalsIgnoreCase(getResources().getString(R.string.lbl_year))) {
                this.graph_view_today.setVisibility(View.GONE);
                this.graph_view_monthly.setVisibility(View.GONE);
                this.graph_view_yearly.setVisibility(View.VISIBLE);
                this.graph_view_all.setVisibility(View.GONE);
                this.graph_view_custom.setVisibility(View.GONE);
                this.graph_view_yearly.invalidate();
                this.graph_view_yearly.clear();
                getBloodPressureYearlyData(bloodCountChartRequest);

//                SetBloodPressureYearlyChart(i);
            } else if (str2.equalsIgnoreCase(getResources().getString(R.string.lbl_custom))) {
                this.graph_view_today.setVisibility(View.GONE);
                this.graph_view_monthly.setVisibility(View.GONE);
                this.graph_view_yearly.setVisibility(View.GONE);
                this.graph_view_all.setVisibility(View.GONE);
                this.graph_view_custom.setVisibility(View.VISIBLE);
                this.graph_view_custom.invalidate();
                this.graph_view_custom.clear();
                getBloodPressureCustomData(bloodCountChartRequest);

//                SetBloodPressureCustomChart(i);
            }
        } else if (str.equalsIgnoreCase(getResources().getString(R.string.lbl_chart_blood_sugar))) {
            String string3 = getResources().getString(R.string.lbl_mg_dl);
            TextView textView3 = this.txt_chart_type;
            textView3.setText(getResources().getString(R.string.lbl_chart_blood_sugar) + " (" + string3 + ")");

            if (str2.equalsIgnoreCase(getResources().getString(R.string.lbl_all))) {
                this.graph_view_today.setVisibility(View.GONE);
                this.graph_view_monthly.setVisibility(View.GONE);
                this.graph_view_yearly.setVisibility(View.GONE);
                this.graph_view_all.setVisibility(View.VISIBLE);
                this.graph_view_custom.setVisibility(View.GONE);
                this.graph_view_all.invalidate();
                this.graph_view_all.clear();
                getBloodSugarChartAllData(bloodCountChartRequest);
//                SetBloodSugarAllChart(i);
            } else if (str2.equalsIgnoreCase(getResources().getString(R.string.lbl_today))) {
                this.graph_view_today.setVisibility(View.VISIBLE);
                this.graph_view_monthly.setVisibility(View.GONE);
                this.graph_view_yearly.setVisibility(View.GONE);
                this.graph_view_all.setVisibility(View.GONE);
                this.graph_view_custom.setVisibility(View.GONE);
                this.graph_view_today.invalidate();
                this.graph_view_today.clear();
                getBloodSugarChartTodayData(bloodCountChartRequest);
//                SetBloodSugarTodayChart(i);
            } else if (str2.equalsIgnoreCase(getResources().getString(R.string.lbl_month))) {
                this.graph_view_today.setVisibility(View.GONE);
                this.graph_view_monthly.setVisibility(View.VISIBLE);
                this.graph_view_yearly.setVisibility(View.GONE);
                this.graph_view_all.setVisibility(View.GONE);
                this.graph_view_custom.setVisibility(View.GONE);
                this.graph_view_monthly.invalidate();
                this.graph_view_monthly.clear();
                getBloodSugarChartMonthlyData(bloodCountChartRequest);
//                SetBloodSugarMonthlyChart(i);
            } else if (str2.equalsIgnoreCase(getResources().getString(R.string.lbl_year))) {
                this.graph_view_today.setVisibility(View.GONE);
                this.graph_view_monthly.setVisibility(View.GONE);
                this.graph_view_yearly.setVisibility(View.VISIBLE);
                this.graph_view_all.setVisibility(View.GONE);
                this.graph_view_custom.setVisibility(View.GONE);
                this.graph_view_yearly.invalidate();
                this.graph_view_yearly.clear();
                getBloodSugarChartYearlyData(bloodCountChartRequest);
//                SetBloodSugarYearlyChart(i);
            } else if (str2.equalsIgnoreCase(getResources().getString(R.string.lbl_custom))) {
                this.graph_view_today.setVisibility(View.GONE);
                this.graph_view_monthly.setVisibility(View.GONE);
                this.graph_view_yearly.setVisibility(View.GONE);
                this.graph_view_all.setVisibility(View.GONE);
                this.graph_view_custom.setVisibility(View.VISIBLE);
                this.graph_view_custom.invalidate();
                this.graph_view_custom.clear();
                getBloodSugarChartCustomData(bloodCountChartRequest);

//                SetBloodSugarCustomChart(i);
            }
        } else if (str.equalsIgnoreCase(getResources().getString(R.string.lbl_chart_bmi))) {
            this.txt_chart_type.setText(getResources().getString(R.string.lbl_chart_bmi));
            if (str2.equalsIgnoreCase(getResources().getString(R.string.lbl_all))) {
                this.graph_view_today.setVisibility(View.GONE);
                this.graph_view_monthly.setVisibility(View.GONE);
                this.graph_view_yearly.setVisibility(View.GONE);
                this.graph_view_all.setVisibility(View.VISIBLE);
                this.graph_view_custom.setVisibility(View.GONE);
                this.graph_view_all.invalidate();
                this.graph_view_all.clear();
                getBmiChartAllData(bloodCountChartRequest);
            } else if (str2.equalsIgnoreCase(getResources().getString(R.string.lbl_today))) {
                this.graph_view_today.setVisibility(View.VISIBLE);
                this.graph_view_monthly.setVisibility(View.GONE);
                this.graph_view_yearly.setVisibility(View.GONE);
                this.graph_view_all.setVisibility(View.GONE);
                this.graph_view_custom.setVisibility(View.GONE);
                this.graph_view_today.invalidate();
                this.graph_view_today.clear();
                getBmiChartTodayData(bloodCountChartRequest);
            } else if (str2.equalsIgnoreCase(getResources().getString(R.string.lbl_month))) {
                this.graph_view_today.setVisibility(View.GONE);
                this.graph_view_monthly.setVisibility(View.VISIBLE);
                this.graph_view_yearly.setVisibility(View.GONE);
                this.graph_view_all.setVisibility(View.GONE);
                this.graph_view_custom.setVisibility(View.GONE);
                this.graph_view_monthly.invalidate();
                this.graph_view_monthly.clear();
                getBmiChartMonthlyData(bloodCountChartRequest);
            } else if (str2.equalsIgnoreCase(getResources().getString(R.string.lbl_year))) {
                this.graph_view_today.setVisibility(View.GONE);
                this.graph_view_monthly.setVisibility(View.GONE);
                this.graph_view_yearly.setVisibility(View.VISIBLE);
                this.graph_view_all.setVisibility(View.GONE);
                this.graph_view_custom.setVisibility(View.GONE);
                this.graph_view_yearly.invalidate();
                this.graph_view_yearly.clear();
                getBmiChartYearlyData(bloodCountChartRequest);
            } else if (str2.equalsIgnoreCase(getResources().getString(R.string.lbl_custom))) {
                this.graph_view_today.setVisibility(View.GONE);
                this.graph_view_monthly.setVisibility(View.GONE);
                this.graph_view_yearly.setVisibility(View.GONE);
                this.graph_view_all.setVisibility(View.GONE);
                this.graph_view_custom.setVisibility(View.VISIBLE);
                this.graph_view_custom.invalidate();
                this.graph_view_custom.clear();
                getBmiChartCustomData(bloodCountChartRequest);
            }
        } else if (str.equalsIgnoreCase(getResources().getString(R.string.lbl_chart_body_temp))) {
            this.txt_chart_type.setText(getResources().getString(R.string.lbl_chart_body_temp));
            if (str2.equalsIgnoreCase(getResources().getString(R.string.lbl_all))) {
                this.graph_view_today.setVisibility(View.GONE);
                this.graph_view_monthly.setVisibility(View.GONE);
                this.graph_view_yearly.setVisibility(View.GONE);
                this.graph_view_all.setVisibility(View.VISIBLE);
                this.graph_view_custom.setVisibility(View.GONE);
                this.graph_view_all.invalidate();
                this.graph_view_all.clear();
                getTempChartAllData(bloodCountChartRequest);
            } else if (str2.equalsIgnoreCase(getResources().getString(R.string.lbl_today))) {
                this.graph_view_today.setVisibility(View.VISIBLE);
                this.graph_view_monthly.setVisibility(View.GONE);
                this.graph_view_yearly.setVisibility(View.GONE);
                this.graph_view_all.setVisibility(View.GONE);
                this.graph_view_custom.setVisibility(View.GONE);
                this.graph_view_today.invalidate();
                this.graph_view_today.clear();
                getTempChartTodayData(bloodCountChartRequest);
            } else if (str2.equalsIgnoreCase(getResources().getString(R.string.lbl_month))) {
                this.graph_view_today.setVisibility(View.GONE);
                this.graph_view_monthly.setVisibility(View.VISIBLE);
                this.graph_view_yearly.setVisibility(View.GONE);
                this.graph_view_all.setVisibility(View.GONE);
                this.graph_view_custom.setVisibility(View.GONE);
                this.graph_view_monthly.invalidate();
                this.graph_view_monthly.clear();
                getTempChartMonthlyData(bloodCountChartRequest);
            } else if (str2.equalsIgnoreCase(getResources().getString(R.string.lbl_year))) {
                this.graph_view_today.setVisibility(View.GONE);
                this.graph_view_monthly.setVisibility(View.GONE);
                this.graph_view_yearly.setVisibility(View.VISIBLE);
                this.graph_view_all.setVisibility(View.GONE);
                this.graph_view_custom.setVisibility(View.GONE);
                this.graph_view_yearly.invalidate();
                this.graph_view_yearly.clear();
                getTempChartYearlyData(bloodCountChartRequest);
            } else if (str2.equalsIgnoreCase(getResources().getString(R.string.lbl_custom))) {
                this.graph_view_today.setVisibility(View.GONE);
                this.graph_view_monthly.setVisibility(View.GONE);
                this.graph_view_yearly.setVisibility(View.GONE);
                this.graph_view_all.setVisibility(View.GONE);
                this.graph_view_custom.setVisibility(View.VISIBLE);
                this.graph_view_custom.invalidate();
                this.graph_view_custom.clear();
                getTempChartCustomData(bloodCountChartRequest);
            }
        } else if (str.equalsIgnoreCase(getResources().getString(R.string.lbl_chart_cholesterol))) {
            String string4 = getResources().getString(R.string.lbl_mg_dl);
            TextView textView4 = this.txt_chart_type;
            textView4.setText(getResources().getString(R.string.lbl_chart_cholesterol) + " (" + string4 + ")");
            if (str2.equalsIgnoreCase(getResources().getString(R.string.lbl_all))) {
                this.graph_view_today.setVisibility(View.GONE);
                this.graph_view_monthly.setVisibility(View.GONE);
                this.graph_view_yearly.setVisibility(View.GONE);
                this.graph_view_all.setVisibility(View.VISIBLE);
                this.graph_view_custom.setVisibility(View.GONE);
                this.graph_view_all.invalidate();
                this.graph_view_all.clear();
                getCholesterolChartAllData(bloodCountChartRequest);
            } else if (str2.equalsIgnoreCase(getResources().getString(R.string.lbl_today))) {
                this.graph_view_today.setVisibility(View.VISIBLE);
                this.graph_view_monthly.setVisibility(View.GONE);
                this.graph_view_yearly.setVisibility(View.GONE);
                this.graph_view_all.setVisibility(View.GONE);
                this.graph_view_custom.setVisibility(View.GONE);
                this.graph_view_today.invalidate();
                this.graph_view_today.clear();
                getCholesterolChartTodayData(bloodCountChartRequest);
            } else if (str2.equalsIgnoreCase(getResources().getString(R.string.lbl_month))) {
                this.graph_view_today.setVisibility(View.GONE);
                this.graph_view_monthly.setVisibility(View.VISIBLE);
                this.graph_view_yearly.setVisibility(View.GONE);
                this.graph_view_all.setVisibility(View.GONE);
                this.graph_view_custom.setVisibility(View.GONE);
                this.graph_view_monthly.invalidate();
                this.graph_view_monthly.clear();
                getCholesterolChartMonthlyData(bloodCountChartRequest);
            } else if (str2.equalsIgnoreCase(getResources().getString(R.string.lbl_year))) {
                this.graph_view_today.setVisibility(View.GONE);
                this.graph_view_monthly.setVisibility(View.GONE);
                this.graph_view_yearly.setVisibility(View.VISIBLE);
                this.graph_view_all.setVisibility(View.GONE);
                this.graph_view_custom.setVisibility(View.GONE);
                this.graph_view_yearly.invalidate();
                this.graph_view_yearly.clear();
                getCholesterolChartYearlyData(bloodCountChartRequest);
            } else if (str2.equalsIgnoreCase(getResources().getString(R.string.lbl_custom))) {
                this.graph_view_today.setVisibility(View.GONE);
                this.graph_view_monthly.setVisibility(View.GONE);
                this.graph_view_yearly.setVisibility(View.GONE);
                this.graph_view_all.setVisibility(View.GONE);
                this.graph_view_custom.setVisibility(View.VISIBLE);
                this.graph_view_custom.invalidate();
                this.graph_view_custom.clear();
                getCholesterolChartCustomData(bloodCountChartRequest);
            }
        } else if (str.equalsIgnoreCase(getResources().getString(R.string.lbl_chart_heart_rate))) {
            String string5 = getResources().getString(R.string.lbl_bpm);
            TextView textView5 = this.txt_chart_type;
            textView5.setText(getResources().getString(R.string.lbl_chart_heart_rate) + " (" + string5 + ")");
            if (str2.equalsIgnoreCase(getResources().getString(R.string.lbl_all))) {
                this.graph_view_today.setVisibility(View.GONE);
                this.graph_view_monthly.setVisibility(View.GONE);
                this.graph_view_yearly.setVisibility(View.GONE);
                this.graph_view_all.setVisibility(View.VISIBLE);
                this.graph_view_custom.setVisibility(View.GONE);
                this.graph_view_all.invalidate();
                this.graph_view_all.clear();
                getHrChartAllData(bloodCountChartRequest);
            } else if (str2.equalsIgnoreCase(getResources().getString(R.string.lbl_today))) {
                this.graph_view_today.setVisibility(View.VISIBLE);
                this.graph_view_monthly.setVisibility(View.GONE);
                this.graph_view_yearly.setVisibility(View.GONE);
                this.graph_view_all.setVisibility(View.GONE);
                this.graph_view_custom.setVisibility(View.GONE);
                this.graph_view_today.invalidate();
                this.graph_view_today.clear();
                getHrChartTodayData(bloodCountChartRequest);
//                SetHeartRateTodayChart(i);
            } else if (str2.equalsIgnoreCase(getResources().getString(R.string.lbl_month))) {
                this.graph_view_today.setVisibility(View.GONE);
                this.graph_view_monthly.setVisibility(View.VISIBLE);
                this.graph_view_yearly.setVisibility(View.GONE);
                this.graph_view_all.setVisibility(View.GONE);
                this.graph_view_custom.setVisibility(View.GONE);
                this.graph_view_monthly.invalidate();
                this.graph_view_monthly.clear();
                getHrChartMonthlyData(bloodCountChartRequest);
//                SetHeartRateMonthlyChart(i);
            } else if (str2.equalsIgnoreCase(getResources().getString(R.string.lbl_year))) {
                this.graph_view_today.setVisibility(View.GONE);
                this.graph_view_monthly.setVisibility(View.GONE);
                this.graph_view_yearly.setVisibility(View.VISIBLE);
                this.graph_view_all.setVisibility(View.GONE);
                this.graph_view_custom.setVisibility(View.GONE);
                this.graph_view_yearly.invalidate();
                this.graph_view_yearly.clear();
                getHrChartYearlyData(bloodCountChartRequest);

//                SetHeartRateYearlyChart(i);
            } else if (str2.equalsIgnoreCase(getResources().getString(R.string.lbl_custom))) {
                this.graph_view_today.setVisibility(View.GONE);
                this.graph_view_monthly.setVisibility(View.GONE);
                this.graph_view_yearly.setVisibility(View.GONE);
                this.graph_view_all.setVisibility(View.GONE);
                this.graph_view_custom.setVisibility(View.VISIBLE);
                this.graph_view_custom.invalidate();
                this.graph_view_custom.clear();
                getHrChartCustomData(bloodCountChartRequest);

//                SetHeartRateCustomChart(i);
            }
        } else if (str.equalsIgnoreCase(getResources().getString(R.string.lbl_chart_weight))) {
            this.txt_chart_type.setText(getResources().getString(R.string.lbl_chart_weight));
            if (str2.equalsIgnoreCase(getResources().getString(R.string.lbl_all))) {
                this.graph_view_today.setVisibility(View.GONE);
                this.graph_view_monthly.setVisibility(View.GONE);
                this.graph_view_yearly.setVisibility(View.GONE);
                this.graph_view_all.setVisibility(View.VISIBLE);
                this.graph_view_custom.setVisibility(View.GONE);
                this.graph_view_all.invalidate();
                this.graph_view_all.clear();
                getWeightChartAllData(bloodCountChartRequest);
            } else if (str2.equalsIgnoreCase(getResources().getString(R.string.lbl_today))) {
                this.graph_view_today.setVisibility(View.VISIBLE);
                this.graph_view_monthly.setVisibility(View.GONE);
                this.graph_view_yearly.setVisibility(View.GONE);
                this.graph_view_all.setVisibility(View.GONE);
                this.graph_view_custom.setVisibility(View.GONE);
                this.graph_view_today.invalidate();
                this.graph_view_today.clear();
                getWeightChartTodayData(bloodCountChartRequest);
            } else if (str2.equalsIgnoreCase(getResources().getString(R.string.lbl_month))) {
                this.graph_view_today.setVisibility(View.GONE);
                this.graph_view_monthly.setVisibility(View.VISIBLE);
                this.graph_view_yearly.setVisibility(View.GONE);
                this.graph_view_all.setVisibility(View.GONE);
                this.graph_view_custom.setVisibility(View.GONE);
                this.graph_view_monthly.invalidate();
                this.graph_view_monthly.clear();
                getWeightChartMonthlyData(bloodCountChartRequest);
            } else if (str2.equalsIgnoreCase(getResources().getString(R.string.lbl_year))) {
                this.graph_view_today.setVisibility(View.GONE);
                this.graph_view_monthly.setVisibility(View.GONE);
                this.graph_view_yearly.setVisibility(View.VISIBLE);
                this.graph_view_all.setVisibility(View.GONE);
                this.graph_view_custom.setVisibility(View.GONE);
                this.graph_view_yearly.invalidate();
                this.graph_view_yearly.clear();
                getWeightChartYearlyData(bloodCountChartRequest);
            } else if (str2.equalsIgnoreCase(getResources().getString(R.string.lbl_custom))) {
                this.graph_view_today.setVisibility(View.GONE);
                this.graph_view_monthly.setVisibility(View.GONE);
                this.graph_view_yearly.setVisibility(View.GONE);
                this.graph_view_all.setVisibility(View.GONE);
                this.graph_view_custom.setVisibility(View.VISIBLE);
                this.graph_view_custom.invalidate();
                this.graph_view_custom.clear();
                getWeightChartCustomData(bloodCountChartRequest);
            }
        }
    }

    @SuppressLint("WrongViewCast")
    private void setUpActionBar() {
        ((TextView) findViewById(R.id.toolbar_txt_title_1)).setText(getResources().getString(R.string.lbl_tracker));
        ((TextView) findViewById(R.id.toolbar_txt_title_2)).setText(getResources().getString(R.string.lbl_statistics));
        findViewById(R.id.toolbar_rel_back).setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                TrackerStatisticsActivity.this.onBackPressed();
            }
        });
        setSupportActionBar(findViewById(R.id.toolbar_actionbar));
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayShowTitleEnabled(false);
        supportActionBar.setDisplayHomeAsUpEnabled(false);
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
        GetDefaultSpinnerValues();


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private void getBloodCountChartAllData(BloodCountChartRequest bloodCountChartRequest) {
        String token = sharedPreferencesUtils.read("token", "");
        if (!token.equals("")) {
            ApiClient.INSTANCE.getApiService().getBloodCountChartAllData(token, bloodCountChartRequest)
                    .enqueue(new Callback<BloodCountChartResponse>() {
                        @Override
                        public void onResponse(Call<BloodCountChartResponse> call, Response<BloodCountChartResponse> response) {
                            if (response.isSuccessful()) {
                                getHighestValueAll(response.body().getData());
                            } else {
                                showMessage("Failed");

                            }
                        }

                        @Override
                        public void onFailure(Call<BloodCountChartResponse> call, Throwable t) {
                            showMessage("Failed ...");

                        }
                    });

        }
    }

    private void getBloodCountChartTodayData(BloodCountChartRequest bloodCountChartRequest) {
        String token = sharedPreferencesUtils.read("token", "");
        if (!token.equals("")) {
            ApiClient.INSTANCE.getApiService().getBloodCountChartTodayData(token, bloodCountChartRequest)
                    .enqueue(new Callback<BloodCountChartResponse>() {
                        @Override
                        public void onResponse(Call<BloodCountChartResponse> call, Response<BloodCountChartResponse> response) {
                            if (response.isSuccessful()) {
                                getHighestValueToday(response.body().getData());
                            } else {
                                showMessage("Failed");

                            }
                        }

                        @Override
                        public void onFailure(Call<BloodCountChartResponse> call, Throwable t) {
                            showMessage("Failed ...");

                        }
                    });

        }
    }

    private void getBloodCountChartMonthlyData(BloodCountChartRequest bloodCountChartRequest) {
        String token = sharedPreferencesUtils.read("token", "");
        if (!token.equals("")) {
            ApiClient.INSTANCE.getApiService().getBloodCountChartMonthlyData(token, bloodCountChartRequest)
                    .enqueue(new Callback<BloodCountChartResponse>() {
                        @Override
                        public void onResponse(Call<BloodCountChartResponse> call, Response<BloodCountChartResponse> response) {
                            if (response.isSuccessful()) {
                                getHighestValueMonth(response.body().getData());
                            } else {
                                showMessage("Failed");

                            }
                        }

                        @Override
                        public void onFailure(Call<BloodCountChartResponse> call, Throwable t) {
                            showMessage("Failed ...");

                        }
                    });

        }
    }

    private void getBloodCountChartYearlyData(BloodCountChartRequest bloodCountChartRequest) {
        String token = sharedPreferencesUtils.read("token", "");
        if (!token.equals("")) {
            ApiClient.INSTANCE.getApiService().getBloodCountChartYearlyData(token, bloodCountChartRequest)
                    .enqueue(new Callback<BloodCountChartResponse>() {
                        @Override
                        public void onResponse(Call<BloodCountChartResponse> call, Response<BloodCountChartResponse> response) {
                            if (response.isSuccessful()) {
                                getHighestValueYear(response.body().getData());
                            } else {
                                showMessage("Failed");

                            }
                        }

                        @Override
                        public void onFailure(Call<BloodCountChartResponse> call, Throwable t) {
                            showMessage("Failed ...");

                        }
                    });

        }
    }

    private void getBloodCountChartCustomData(BloodCountChartRequest bloodCountChartRequest) {
        String token = sharedPreferencesUtils.read("token", "");
        if (!token.equals("")) {
            ApiClient.INSTANCE.getApiService().getBloodCountChartCustomData(token, bloodCountChartRequest)
                    .enqueue(new Callback<BloodCountChartResponse>() {
                        @Override
                        public void onResponse(Call<BloodCountChartResponse> call, Response<BloodCountChartResponse> response) {
                            if (response.isSuccessful()) {
                                getHighestValueCustom(response.body().getData());
                            } else {
                                showMessage("Failed");

                            }
                        }

                        @Override
                        public void onFailure(Call<BloodCountChartResponse> call, Throwable t) {
                            showMessage("Failed ...");

                        }
                    });

        }
    }


    private void getHighestValueAll(List<BloodCountChartData> dataArray) {
        float highestWBC = Float.MIN_VALUE;

        for (int i = 0; i < dataArray.size(); i++) {
            float wbc = dataArray.get(i).rbc_value;
            if (wbc > highestWBC) {
                highestWBC = wbc;
            }
        }
        SetBloodCountAllChart(dataArray, highestWBC);
    }

    private void getHighestValueToday(List<BloodCountChartData> dataArray) {
        float highestWBC = Float.MIN_VALUE;

        for (int i = 0; i < dataArray.size(); i++) {
            float wbc = dataArray.get(i).rbc_value;
            if (wbc > highestWBC) {
                highestWBC = wbc;
            }
        }
        SetBloodCountTodayChart(dataArray, highestWBC);
    }

    private void getHighestValueMonth(List<BloodCountChartData> dataArray) {
        float highestWBC = Float.MIN_VALUE;

        for (int i = 0; i < dataArray.size(); i++) {
            float wbc = dataArray.get(i).rbc_value;
            if (wbc > highestWBC) {
                highestWBC = wbc;
            }
        }
        SetBloodCountMonthlyChart(dataArray, highestWBC);
    }


    private void getHighestValueYear(List<BloodCountChartData> dataArray) {
        float highestWBC = Float.MIN_VALUE;

        for (int i = 0; i < dataArray.size(); i++) {
            float wbc = dataArray.get(i).rbc_value;
            if (wbc > highestWBC) {
                highestWBC = wbc;
            }
        }
        SetBloodCountYearlyChart(dataArray, highestWBC);
    }


    private void getHighestValueCustom(List<BloodCountChartData> dataArray) {
        float highestWBC = Float.MIN_VALUE;

        for (int i = 0; i < dataArray.size(); i++) {
            float wbc = dataArray.get(i).rbc_value;
            if (wbc > highestWBC) {
                highestWBC = wbc;
            }
        }
        SetBloodCountCustomChart(dataArray, highestWBC);
    }


    //bp
    private void getBloodPressureChartAllData(BloodCountChartRequest bloodCountChartRequest) {
        String token = sharedPreferencesUtils.read("token", "");
        if (!token.equals("")) {
            ApiClient.INSTANCE.getApiService().getBloodPressureChartAllData(token, bloodCountChartRequest)
                    .enqueue(new Callback<BloodPressureChartResponse>() {
                        @Override
                        public void onResponse(Call<BloodPressureChartResponse> call, Response<BloodPressureChartResponse> response) {
                            if (response.isSuccessful()) {
                                getHighestBloodPressureValueAll(response.body().getData());
                            } else {
                                showMessage("Failed");

                            }
                        }

                        @Override
                        public void onFailure(Call<BloodPressureChartResponse> call, Throwable t) {
                            showMessage("Failed ...");

                        }
                    });

        }
    }

    private void getBloodPressureTodayData(BloodCountChartRequest bloodCountChartRequest) {
        String token = sharedPreferencesUtils.read("token", "");
        if (!token.equals("")) {
            ApiClient.INSTANCE.getApiService().getBloodPressureChartTodayData(token, bloodCountChartRequest)
                    .enqueue(new Callback<BloodPressureChartResponse>() {
                        @Override
                        public void onResponse(Call<BloodPressureChartResponse> call, Response<BloodPressureChartResponse> response) {
                            if (response.isSuccessful()) {
                                getHighestBloodPressureValueToday(response.body().getData());
                            } else {
                                showMessage("Failed");

                            }
                        }

                        @Override
                        public void onFailure(Call<BloodPressureChartResponse> call, Throwable t) {
                            showMessage("Failed ...");

                        }
                    });

        }
    }

    private void getBloodPressureMonthlyData(BloodCountChartRequest bloodCountChartRequest) {
        String token = sharedPreferencesUtils.read("token", "");
        if (!token.equals("")) {
            ApiClient.INSTANCE.getApiService().getBloodPressureChartMonthlyData(token, bloodCountChartRequest)
                    .enqueue(new Callback<BloodPressureChartResponse>() {
                        @Override
                        public void onResponse(Call<BloodPressureChartResponse> call, Response<BloodPressureChartResponse> response) {
                            if (response.isSuccessful()) {
                                getHighestBloodPressureValueMonth(response.body().getData());
                            } else {
                                showMessage("Failed");

                            }
                        }

                        @Override
                        public void onFailure(Call<BloodPressureChartResponse> call, Throwable t) {
                            showMessage("Failed ...");

                        }
                    });

        }
    }

    private void getBloodPressureYearlyData(BloodCountChartRequest bloodCountChartRequest) {
        String token = sharedPreferencesUtils.read("token", "");
        if (!token.equals("")) {
            ApiClient.INSTANCE.getApiService().getBloodPressureChartYearlyData(token, bloodCountChartRequest)
                    .enqueue(new Callback<BloodPressureChartResponse>() {
                        @Override
                        public void onResponse(Call<BloodPressureChartResponse> call, Response<BloodPressureChartResponse> response) {
                            if (response.isSuccessful()) {
                                getHighestBloodPressureValueYear(response.body().getData());
                            } else {
                                showMessage("Failed");

                            }
                        }

                        @Override
                        public void onFailure(Call<BloodPressureChartResponse> call, Throwable t) {
                            showMessage("Failed ...");

                        }
                    });

        }
    }

    private void getBloodPressureCustomData(BloodCountChartRequest bloodCountChartRequest) {
        String token = sharedPreferencesUtils.read("token", "");
        if (!token.equals("")) {
            ApiClient.INSTANCE.getApiService().getBloodPressureChartCustomData(token, bloodCountChartRequest)
                    .enqueue(new Callback<BloodPressureChartResponse>() {
                        @Override
                        public void onResponse(Call<BloodPressureChartResponse> call, Response<BloodPressureChartResponse> response) {
                            if (response.isSuccessful()) {
                                getHighestBloodPressureValueCustom(response.body().getData());
                            } else {
                                showMessage("Failed");

                            }
                        }

                        @Override
                        public void onFailure(Call<BloodPressureChartResponse> call, Throwable t) {
                            showMessage("Failed ...");

                        }
                    });

        }
    }


    private void getHighestBloodPressureValueAll(List<BloodPressureChartData> dataArray) {
        float highestBp = Float.MIN_VALUE;

        for (int i = 0; i < dataArray.size(); i++) {
            float bp = dataArray.get(i).pulse_pressure_value;
            if (bp > highestBp) {
                highestBp = bp;
            }
        }
        SetBloodPressureAllChart(highestBp, dataArray);
    }

    private void getHighestBloodPressureValueToday(List<BloodPressureChartData> dataArray) {
        float highestBp = Float.MIN_VALUE;

        for (int i = 0; i < dataArray.size(); i++) {
            float bp = dataArray.get(i).pulse_pressure_value;
            if (bp > highestBp) {
                highestBp = bp;
            }
        }
        SetBloodPressureTodayChart(highestBp, dataArray);
    }

    private void getHighestBloodPressureValueMonth(List<BloodPressureChartData> dataArray) {
        float highestBp = Float.MIN_VALUE;

        for (int i = 0; i < dataArray.size(); i++) {
            float bp = dataArray.get(i).pulse_pressure_value;
            if (bp > highestBp) {
                highestBp = bp;
            }
        }
        SetBloodPressureMonthlyChart(highestBp, dataArray);
    }


    private void getHighestBloodPressureValueYear(List<BloodPressureChartData> dataArray) {
        float highestBp = Float.MIN_VALUE;

        for (int i = 0; i < dataArray.size(); i++) {
            float bp = dataArray.get(i).pulse_pressure_value;
            if (bp > highestBp) {
                highestBp = bp;
            }
        }
        SetBloodPressureYearlyChart(highestBp, dataArray);
    }


    private void getHighestBloodPressureValueCustom(List<BloodPressureChartData> dataArray) {
        float highestBp = Float.MIN_VALUE;

        for (int i = 0; i < dataArray.size(); i++) {
            float bp = dataArray.get(i).pulse_pressure_value;
            if (bp > highestBp) {
                highestBp = bp;
            }
        }
        SetBloodPressureCustomChart(highestBp, dataArray);
    }


    //bs
    private void getBloodSugarChartAllData(BloodCountChartRequest bloodCountChartRequest) {
        String token = sharedPreferencesUtils.read("token", "");
        if (!token.equals("")) {
            ApiClient.INSTANCE.getApiService().getBloodSugarChartAllData(token, bloodCountChartRequest)
                    .enqueue(new Callback<BloodSugarChartResponse>() {
                        @Override
                        public void onResponse(Call<BloodSugarChartResponse> call, Response<BloodSugarChartResponse> response) {
                            if (response.isSuccessful()) {
                                getBsHighestValueAll(response.body().getData());
                            } else {
                                showMessage("Failed");

                            }
                        }

                        @Override
                        public void onFailure(Call<BloodSugarChartResponse> call, Throwable t) {
                            showMessage("Failed ...");

                        }
                    });

        }
    }

    private void getBloodSugarChartTodayData(BloodCountChartRequest bloodCountChartRequest) {
        String token = sharedPreferencesUtils.read("token", "");
        if (!token.equals("")) {
            ApiClient.INSTANCE.getApiService().getBloodSugarChartTodayData(token, bloodCountChartRequest)
                    .enqueue(new Callback<BloodSugarChartResponse>() {
                        @Override
                        public void onResponse(Call<BloodSugarChartResponse> call, Response<BloodSugarChartResponse> response) {
                            if (response.isSuccessful()) {
                                getBsHighestValueToday(response.body().getData());
                            } else {
                                showMessage("Failed");

                            }
                        }

                        @Override
                        public void onFailure(Call<BloodSugarChartResponse> call, Throwable t) {
                            showMessage("Failed ...");

                        }
                    });

        }
    }

    private void getBloodSugarChartMonthlyData(BloodCountChartRequest bloodCountChartRequest) {
        String token = sharedPreferencesUtils.read("token", "");
        if (!token.equals("")) {
            ApiClient.INSTANCE.getApiService().getBloodSugarChartMonthlyData(token, bloodCountChartRequest)
                    .enqueue(new Callback<BloodSugarChartResponse>() {
                        @Override
                        public void onResponse(Call<BloodSugarChartResponse> call, Response<BloodSugarChartResponse> response) {
                            if (response.isSuccessful()) {
                                getBsHighestValueMonth(response.body().getData());
                            } else {
                                showMessage("Failed");

                            }
                        }

                        @Override
                        public void onFailure(Call<BloodSugarChartResponse> call, Throwable t) {
                            showMessage("Failed ...");

                        }
                    });

        }
    }

    private void getBloodSugarChartYearlyData(BloodCountChartRequest bloodCountChartRequest) {
        String token = sharedPreferencesUtils.read("token", "");
        if (!token.equals("")) {
            ApiClient.INSTANCE.getApiService().getBloodSugarChartYearlyData(token, bloodCountChartRequest)
                    .enqueue(new Callback<BloodSugarChartResponse>() {
                        @Override
                        public void onResponse(Call<BloodSugarChartResponse> call, Response<BloodSugarChartResponse> response) {
                            if (response.isSuccessful()) {
                                getBsHighestValueYear(response.body().getData());
                            } else {
                                showMessage("Failed");

                            }
                        }

                        @Override
                        public void onFailure(Call<BloodSugarChartResponse> call, Throwable t) {
                            showMessage("Failed ...");

                        }
                    });

        }
    }

    private void getBloodSugarChartCustomData(BloodCountChartRequest bloodCountChartRequest) {
        String token = sharedPreferencesUtils.read("token", "");
        if (!token.equals("")) {
            ApiClient.INSTANCE.getApiService().getBloodSugarChartCustomData(token, bloodCountChartRequest)
                    .enqueue(new Callback<BloodSugarChartResponse>() {
                        @Override
                        public void onResponse(Call<BloodSugarChartResponse> call, Response<BloodSugarChartResponse> response) {
                            if (response.isSuccessful()) {
                                getBsHighestValueCustom(response.body().getData());
                            } else {
                                showMessage("Failed");

                            }
                        }

                        @Override
                        public void onFailure(Call<BloodSugarChartResponse> call, Throwable t) {
                            showMessage("Failed ...");

                        }
                    });

        }
    }

    private void getBsHighestValueAll(List<BloodSugarChartData> dataArray) {
        float highestSugar = Float.MIN_VALUE;

        for (int i = 0; i < dataArray.size(); i++) {
            float max = dataArray.get(i).sugar_level;
            if (max > highestSugar) {
                highestSugar = max;
            }
        }
        SetBloodSugarAllChart(highestSugar, dataArray);
    }

    private void getBsHighestValueToday(List<BloodSugarChartData> dataArray) {


        float highestSugar = Float.MIN_VALUE;

        for (int i = 0; i < dataArray.size(); i++) {
            float max = dataArray.get(i).sugar_level;
            if (max > highestSugar) {
                highestSugar = max;
            }
        }

        SetBloodSugarTodayChart(highestSugar, dataArray);
    }

    private void getBsHighestValueMonth(List<BloodSugarChartData> dataArray) {
        float highestSugar = Float.MIN_VALUE;

        for (int i = 0; i < dataArray.size(); i++) {
            float max = dataArray.get(i).sugar_level;
            if (max > highestSugar) {
                highestSugar = max;
            }
        }
        SetBloodSugarMonthlyChart(highestSugar, dataArray);
    }


    private void getBsHighestValueYear(List<BloodSugarChartData> dataArray) {
        float highestSugar = Float.MIN_VALUE;

        for (int i = 0; i < dataArray.size(); i++) {
            float max = dataArray.get(i).sugar_level;
            if (max > highestSugar) {
                highestSugar = max;
            }
        }
        SetBloodSugarYearlyChart(highestSugar, dataArray);
    }


    private void getBsHighestValueCustom(List<BloodSugarChartData> dataArray) {
        float highestSugar = Float.MIN_VALUE;

        for (int i = 0; i < dataArray.size(); i++) {
            float max = dataArray.get(i).sugar_level;
            if (max > highestSugar) {
                highestSugar = max;
            }
        }
        SetBloodSugarCustomChart(highestSugar, dataArray);
    }


    //ch
    private void getCholesterolChartAllData(BloodCountChartRequest bloodCountChartRequest) {
        String token = sharedPreferencesUtils.read("token", "");
        if (!token.equals("")) {
            ApiClient.INSTANCE.getApiService().getCholesterolChartAllData(token, bloodCountChartRequest)
                    .enqueue(new Callback<CholesterolChartResponse>() {
                        @Override
                        public void onResponse(Call<CholesterolChartResponse> call, Response<CholesterolChartResponse> response) {
                            if (response.isSuccessful()) {
                                getChHighestValueAll(response.body().getData());
                            } else {
                                showMessage("Failed");

                            }
                        }

                        @Override
                        public void onFailure(Call<CholesterolChartResponse> call, Throwable t) {
                            showMessage("Failed ...");

                        }
                    });

        }
    }

    private void getCholesterolChartTodayData(BloodCountChartRequest bloodCountChartRequest) {
        String token = sharedPreferencesUtils.read("token", "");
        if (!token.equals("")) {
            ApiClient.INSTANCE.getApiService().getCholesterolChartTodayData(token, bloodCountChartRequest)
                    .enqueue(new Callback<CholesterolChartResponse>() {
                        @Override
                        public void onResponse(Call<CholesterolChartResponse> call, Response<CholesterolChartResponse> response) {
                            if (response.isSuccessful()) {
                                getChHighestValueToday(response.body().getData());
                            } else {
                                showMessage("Failed");

                            }
                        }

                        @Override
                        public void onFailure(Call<CholesterolChartResponse> call, Throwable t) {
                            showMessage("Failed ...");

                        }
                    });

        }
    }

    private void getCholesterolChartMonthlyData(BloodCountChartRequest bloodCountChartRequest) {
        String token = sharedPreferencesUtils.read("token", "");
        if (!token.equals("")) {
            ApiClient.INSTANCE.getApiService().getCholesterolChartMonthlyData(token, bloodCountChartRequest)
                    .enqueue(new Callback<CholesterolChartResponse>() {
                        @Override
                        public void onResponse(Call<CholesterolChartResponse> call, Response<CholesterolChartResponse> response) {
                            if (response.isSuccessful()) {
                                getChHighestValueMonth(response.body().getData());
                            } else {
                                showMessage("Failed");

                            }
                        }

                        @Override
                        public void onFailure(Call<CholesterolChartResponse> call, Throwable t) {
                            showMessage("Failed ...");

                        }
                    });

        }
    }

    private void getCholesterolChartYearlyData(BloodCountChartRequest bloodCountChartRequest) {
        String token = sharedPreferencesUtils.read("token", "");
        if (!token.equals("")) {
            ApiClient.INSTANCE.getApiService().getCholesterolChartYearlyData(token, bloodCountChartRequest)
                    .enqueue(new Callback<CholesterolChartResponse>() {
                        @Override
                        public void onResponse(Call<CholesterolChartResponse> call, Response<CholesterolChartResponse> response) {
                            if (response.isSuccessful()) {
                                getChHighestValueYear(response.body().getData());
                            } else {
                                showMessage("Failed");

                            }
                        }

                        @Override
                        public void onFailure(Call<CholesterolChartResponse> call, Throwable t) {
                            showMessage("Failed ...");

                        }
                    });

        }
    }

    private void getCholesterolChartCustomData(BloodCountChartRequest bloodCountChartRequest) {
        String token = sharedPreferencesUtils.read("token", "");
        if (!token.equals("")) {
            ApiClient.INSTANCE.getApiService().getCholesterolChartCustomData(token, bloodCountChartRequest)
                    .enqueue(new Callback<CholesterolChartResponse>() {
                        @Override
                        public void onResponse(Call<CholesterolChartResponse> call, Response<CholesterolChartResponse> response) {
                            if (response.isSuccessful()) {
                                getChHighestValueCustom(response.body().getData());
                            } else {
                                showMessage("Failed");

                            }
                        }

                        @Override
                        public void onFailure(Call<CholesterolChartResponse> call, Throwable t) {
                            showMessage("Failed ...");

                        }
                    });

        }
    }


    private void getChHighestValueAll(List<CholesterolChartData> dataArray) {
        float highestSugar = Float.MIN_VALUE;

        for (int i = 0; i < dataArray.size(); i++) {
            float max = dataArray.get(i).hdl_value;
            if (max > highestSugar) {
                highestSugar = max;
            }
        }
        SetCholesterolAllChart(highestSugar, dataArray);
    }

    private void getChHighestValueToday(List<CholesterolChartData> dataArray) {
        float highestSugar = Float.MIN_VALUE;

        for (int i = 0; i < dataArray.size(); i++) {
            float max = dataArray.get(i).hdl_value;
            if (max > highestSugar) {
                highestSugar = max;
            }
        }
        SetCholesterolTodayChart(highestSugar, dataArray);
    }

    private void getChHighestValueMonth(List<CholesterolChartData> dataArray) {
        float highestSugar = Float.MIN_VALUE;

        for (int i = 0; i < dataArray.size(); i++) {
            float max = dataArray.get(i).hdl_value;
            if (max > highestSugar) {
                highestSugar = max;
            }
        }
        SetCholesterolMonthlyChart(highestSugar, dataArray);
    }


    private void getChHighestValueYear(List<CholesterolChartData> dataArray) {
        float highestSugar = Float.MIN_VALUE;

        for (int i = 0; i < dataArray.size(); i++) {
            float max = dataArray.get(i).hdl_value;
            if (max > highestSugar) {
                highestSugar = max;
            }
        }
        SetCholesterolYearlyChart(highestSugar, dataArray);
    }


    private void getChHighestValueCustom(List<CholesterolChartData> dataArray) {
        float highestSugar = Float.MIN_VALUE;

        for (int i = 0; i < dataArray.size(); i++) {
            float max = dataArray.get(i).hdl_value;
            if (max > highestSugar) {
                highestSugar = max;
            }
        }
        SetCholesterolCustomChart(highestSugar, dataArray);
    }


    //bs
    private void getHrChartAllData(BloodCountChartRequest bloodCountChartRequest) {
        String token = sharedPreferencesUtils.read("token", "");
        if (!token.equals("")) {
            ApiClient.INSTANCE.getApiService().getHeartRateChartAllData(token, bloodCountChartRequest)
                    .enqueue(new Callback<HeartRateChartResponse>() {
                        @Override
                        public void onResponse(Call<HeartRateChartResponse> call, Response<HeartRateChartResponse> response) {
                            if (response.isSuccessful()) {
                                SetHeartRateAllChart(response.body().getData());
                            } else {
                                showMessage("Failed");

                            }
                        }

                        @Override
                        public void onFailure(Call<HeartRateChartResponse> call, Throwable t) {
                            showMessage("Failed ...");

                        }
                    });

        }
    }

    private void getHrChartTodayData(BloodCountChartRequest bloodCountChartRequest) {
        String token = sharedPreferencesUtils.read("token", "");
        if (!token.equals("")) {
            ApiClient.INSTANCE.getApiService().getHeartRateChartTodayData(token, bloodCountChartRequest)
                    .enqueue(new Callback<HeartRateChartResponse>() {
                        @Override
                        public void onResponse(Call<HeartRateChartResponse> call, Response<HeartRateChartResponse> response) {
                            if (response.isSuccessful()) {
                                SetHeartRateTodayChart(response.body().getData());
                            } else {
                                showMessage("Failed");

                            }
                        }

                        @Override
                        public void onFailure(Call<HeartRateChartResponse> call, Throwable t) {
                            showMessage("Failed ...");

                        }
                    });

        }
    }

    private void getHrChartMonthlyData(BloodCountChartRequest bloodCountChartRequest) {
        String token = sharedPreferencesUtils.read("token", "");
        if (!token.equals("")) {
            ApiClient.INSTANCE.getApiService().getHeartRateChartMonthlyData(token, bloodCountChartRequest)
                    .enqueue(new Callback<HeartRateChartResponse>() {
                        @Override
                        public void onResponse(Call<HeartRateChartResponse> call, Response<HeartRateChartResponse> response) {
                            if (response.isSuccessful()) {
                                SetHeartRateMonthlyChart(response.body().getData());
                            } else {
                                showMessage("Failed");

                            }
                        }

                        @Override
                        public void onFailure(Call<HeartRateChartResponse> call, Throwable t) {
                            showMessage("Failed ...");

                        }
                    });

        }
    }

    private void getHrChartYearlyData(BloodCountChartRequest bloodCountChartRequest) {
        String token = sharedPreferencesUtils.read("token", "");
        if (!token.equals("")) {
            ApiClient.INSTANCE.getApiService().getHeartRateChartYearlyData(token, bloodCountChartRequest)
                    .enqueue(new Callback<HeartRateChartResponse>() {
                        @Override
                        public void onResponse(Call<HeartRateChartResponse> call, Response<HeartRateChartResponse> response) {
                            if (response.isSuccessful()) {
                                SetHeartRateYearlyChart(response.body().getData());
                            } else {
                                showMessage("Failed");

                            }
                        }

                        @Override
                        public void onFailure(Call<HeartRateChartResponse> call, Throwable t) {
                            showMessage("Failed ...");

                        }
                    });

        }
    }

    private void getHrChartCustomData(BloodCountChartRequest bloodCountChartRequest) {
        String token = sharedPreferencesUtils.read("token", "");
        if (!token.equals("")) {
            ApiClient.INSTANCE.getApiService().getHeartRateChartCustomData(token, bloodCountChartRequest)
                    .enqueue(new Callback<HeartRateChartResponse>() {
                        @Override
                        public void onResponse(Call<HeartRateChartResponse> call, Response<HeartRateChartResponse> response) {
                            if (response.isSuccessful()) {
                                SetHeartRateCustomChart(response.body().getData());
                            } else {
                                showMessage("Failed");

                            }
                        }

                        @Override
                        public void onFailure(Call<HeartRateChartResponse> call, Throwable t) {
                            showMessage("Failed ...");

                        }
                    });

        }
    }


    //weight
    private void getWeightChartAllData(BloodCountChartRequest weightChartRequest) {
        String token = sharedPreferencesUtils.read("token", "");
        if (!token.equals("")) {
            ApiClient.INSTANCE.getApiService().getWeightChartAllData(token, weightChartRequest)
                    .enqueue(new Callback<WeightChartResponse>() {
                        @Override
                        public void onResponse(Call<WeightChartResponse> call, Response<WeightChartResponse> response) {
                            if (response.isSuccessful()) {
                                SetWeightAllChart(response.body().getData());
                            } else {
                                showMessage("Failed");

                            }
                        }

                        @Override
                        public void onFailure(Call<WeightChartResponse> call, Throwable t) {
                            showMessage("Failed ...");

                        }
                    });

        }
    }

    private void getWeightChartTodayData(BloodCountChartRequest weightChartRequest) {
        String token = sharedPreferencesUtils.read("token", "");
        if (!token.equals("")) {
            ApiClient.INSTANCE.getApiService().getWeightChartTodayData(token, weightChartRequest)
                    .enqueue(new Callback<WeightChartResponse>() {
                        @Override
                        public void onResponse(Call<WeightChartResponse> call, Response<WeightChartResponse> response) {
                            if (response.isSuccessful()) {
                                SetWeightTodayChart(response.body().getData());
                            } else {
                                showMessage("Failed");

                            }
                        }

                        @Override
                        public void onFailure(Call<WeightChartResponse> call, Throwable t) {
                            showMessage("Failed ...");

                        }
                    });

        }
    }

    private void getWeightChartMonthlyData(BloodCountChartRequest weightChartRequest) {
        String token = sharedPreferencesUtils.read("token", "");
        if (!token.equals("")) {
            ApiClient.INSTANCE.getApiService().getWeightChartMonthlyData(token, weightChartRequest)
                    .enqueue(new Callback<WeightChartResponse>() {
                        @Override
                        public void onResponse(Call<WeightChartResponse> call, Response<WeightChartResponse> response) {
                            if (response.isSuccessful()) {
                                SetWeightMonthlyChart(response.body().getData());
                            } else {
                                showMessage("Failed");

                            }
                        }

                        @Override
                        public void onFailure(Call<WeightChartResponse> call, Throwable t) {
                            showMessage("Failed ...");

                        }
                    });

        }
    }

    private void getWeightChartYearlyData(BloodCountChartRequest weightChartRequest) {
        String token = sharedPreferencesUtils.read("token", "");
        if (!token.equals("")) {
            ApiClient.INSTANCE.getApiService().getWeightChartYearlyData(token, weightChartRequest)
                    .enqueue(new Callback<WeightChartResponse>() {
                        @Override
                        public void onResponse(Call<WeightChartResponse> call, Response<WeightChartResponse> response) {
                            if (response.isSuccessful()) {
                                SetWeightYearlyChart(response.body().getData());
                            } else {
                                showMessage("Failed");

                            }
                        }

                        @Override
                        public void onFailure(Call<WeightChartResponse> call, Throwable t) {
                            showMessage("Failed ...");

                        }
                    });

        }
    }

    private void getWeightChartCustomData(BloodCountChartRequest weightChartRequest) {
        String token = sharedPreferencesUtils.read("token", "");
        if (!token.equals("")) {
            ApiClient.INSTANCE.getApiService().getWeightChartCustomData(token, weightChartRequest)
                    .enqueue(new Callback<WeightChartResponse>() {
                        @Override
                        public void onResponse(Call<WeightChartResponse> call, Response<WeightChartResponse> response) {
                            if (response.isSuccessful()) {
                                SetWeightCustomChart(response.body().getData());
                            } else {
                                showMessage("Failed");

                            }
                        }

                        @Override
                        public void onFailure(Call<WeightChartResponse> call, Throwable t) {
                            showMessage("Failed ...");

                        }
                    });

        }
    }


    //temp 1
    private void getTempChartAllData(BloodCountChartRequest weightChartRequest) {
        String token = sharedPreferencesUtils.read("token", "");
        if (!token.equals("")) {
            ApiClient.INSTANCE.getApiService().getBodyTempChartAllData(token, weightChartRequest)
                    .enqueue(new Callback<BodyTempChartResponse>() {
                        @Override
                        public void onResponse(Call<BodyTempChartResponse> call, Response<BodyTempChartResponse> response) {
                            if (response.isSuccessful()) {
                                SetBodyTempAllChart(response.body().getData());
                            } else {
                                showMessage("Failed");

                            }
                        }

                        @Override
                        public void onFailure(Call<BodyTempChartResponse> call, Throwable t) {
                            showMessage("Failed ...");

                        }
                    });

        }
    }

    private void getTempChartTodayData(BloodCountChartRequest weightChartRequest) {
        String token = sharedPreferencesUtils.read("token", "");
        if (!token.equals("")) {
            ApiClient.INSTANCE.getApiService().getBodyTempChartTodayData(token, weightChartRequest)
                    .enqueue(new Callback<BodyTempChartResponse>() {
                        @Override
                        public void onResponse(Call<BodyTempChartResponse> call, Response<BodyTempChartResponse> response) {
                            if (response.isSuccessful()) {
                                SetBodyTempTodayChart(response.body().getData());
                            } else {
                                showMessage("Failed");

                            }
                        }

                        @Override
                        public void onFailure(Call<BodyTempChartResponse> call, Throwable t) {
                            showMessage("Failed ...");

                        }
                    });

        }
    }

    private void getTempChartMonthlyData(BloodCountChartRequest weightChartRequest) {
        String token = sharedPreferencesUtils.read("token", "");
        if (!token.equals("")) {
            ApiClient.INSTANCE.getApiService().getBodyTempChartMonthlyData(token, weightChartRequest)
                    .enqueue(new Callback<BodyTempChartResponse>() {
                        @Override
                        public void onResponse(Call<BodyTempChartResponse> call, Response<BodyTempChartResponse> response) {
                            if (response.isSuccessful()) {
                                SetBodyTempMonthlyChart(response.body().getData());
                            } else {
                                showMessage("Failed");

                            }
                        }

                        @Override
                        public void onFailure(Call<BodyTempChartResponse> call, Throwable t) {
                            showMessage("Failed ...");

                        }
                    });

        }
    }

    private void getTempChartYearlyData(BloodCountChartRequest weightChartRequest) {
        String token = sharedPreferencesUtils.read("token", "");
        if (!token.equals("")) {
            ApiClient.INSTANCE.getApiService().getBodyTempChartYearlyData(token, weightChartRequest)
                    .enqueue(new Callback<BodyTempChartResponse>() {
                        @Override
                        public void onResponse(Call<BodyTempChartResponse> call, Response<BodyTempChartResponse> response) {
                            if (response.isSuccessful()) {
                                SetBodyTempYearlyChart(response.body().getData());
                            } else {
                                showMessage("Failed");

                            }
                        }

                        @Override
                        public void onFailure(Call<BodyTempChartResponse> call, Throwable t) {
                            showMessage("Failed ...");

                        }
                    });

        }
    }

    private void getTempChartCustomData(BloodCountChartRequest weightChartRequest) {
        String token = sharedPreferencesUtils.read("token", "");
        if (!token.equals("")) {
            ApiClient.INSTANCE.getApiService().getBodyTempChartCustomData(token, weightChartRequest)
                    .enqueue(new Callback<BodyTempChartResponse>() {
                        @Override
                        public void onResponse(Call<BodyTempChartResponse> call, Response<BodyTempChartResponse> response) {
                            if (response.isSuccessful()) {
                                SetBodyTempCustomChart(response.body().getData());
                            } else {
                                showMessage("Failed");

                            }
                        }

                        @Override
                        public void onFailure(Call<BodyTempChartResponse> call, Throwable t) {
                            showMessage("Failed ...");

                        }
                    });

        }
    }


    //mbi
    private void getBmiChartAllData(BloodCountChartRequest weightChartRequest) {
        String token = sharedPreferencesUtils.read("token", "");
        if (!token.equals("")) {
            ApiClient.INSTANCE.getApiService().getBMIChartAllData(token, weightChartRequest)
                    .enqueue(new Callback<BMIChartResponse>() {
                        @Override
                        public void onResponse(Call<BMIChartResponse> call, Response<BMIChartResponse> response) {
                            if (response.isSuccessful()) {
                                SetBMIAllChart(response.body().getData());
                            } else {
                                showMessage("Failed");

                            }
                        }

                        @Override
                        public void onFailure(Call<BMIChartResponse> call, Throwable t) {
                            showMessage("Failed ...");

                        }
                    });

        }
    }

    private void getBmiChartTodayData(BloodCountChartRequest weightChartRequest) {
        String token = sharedPreferencesUtils.read("token", "");
        if (!token.equals("")) {
            ApiClient.INSTANCE.getApiService().getBMIChartTodayData(token, weightChartRequest)
                    .enqueue(new Callback<BMIChartResponse>() {
                        @Override
                        public void onResponse(Call<BMIChartResponse> call, Response<BMIChartResponse> response) {
                            if (response.isSuccessful()) {
                                SetBMITodayChart(response.body().getData());
                            } else {
                                showMessage("Failed");

                            }
                        }

                        @Override
                        public void onFailure(Call<BMIChartResponse> call, Throwable t) {
                            showMessage("Failed ...");

                        }
                    });

        }
    }

    private void getBmiChartMonthlyData(BloodCountChartRequest weightChartRequest) {
        String token = sharedPreferencesUtils.read("token", "");
        if (!token.equals("")) {
            ApiClient.INSTANCE.getApiService().getBMIChartMonthlyData(token, weightChartRequest)
                    .enqueue(new Callback<BMIChartResponse>() {
                        @Override
                        public void onResponse(Call<BMIChartResponse> call, Response<BMIChartResponse> response) {
                            if (response.isSuccessful()) {
                                SetBMIMonthlyChart(response.body().getData());
                            } else {
                                showMessage("Failed");

                            }
                        }

                        @Override
                        public void onFailure(Call<BMIChartResponse> call, Throwable t) {
                            showMessage("Failed ...");

                        }
                    });

        }
    }

    private void getBmiChartYearlyData(BloodCountChartRequest weightChartRequest) {
        String token = sharedPreferencesUtils.read("token", "");
        if (!token.equals("")) {
            ApiClient.INSTANCE.getApiService().getBMIChartYearlyData(token, weightChartRequest)
                    .enqueue(new Callback<BMIChartResponse>() {
                        @Override
                        public void onResponse(Call<BMIChartResponse> call, Response<BMIChartResponse> response) {
                            if (response.isSuccessful()) {
                                SetBMIYearlyChart(response.body().getData());
                            } else {
                                showMessage("Failed");

                            }
                        }

                        @Override
                        public void onFailure(Call<BMIChartResponse> call, Throwable t) {
                            showMessage("Failed ...");

                        }
                    });

        }
    }

    private void getBmiChartCustomData(BloodCountChartRequest weightChartRequest) {
        String token = sharedPreferencesUtils.read("token", "");
        if (!token.equals("")) {
            ApiClient.INSTANCE.getApiService().getBMIChartCustomData(token, weightChartRequest)
                    .enqueue(new Callback<BMIChartResponse>() {
                        @Override
                        public void onResponse(Call<BMIChartResponse> call, Response<BMIChartResponse> response) {
                            if (response.isSuccessful()) {
                                SetBMICustomChart(response.body().getData());
                            } else {
                                showMessage("Failed");

                            }
                        }

                        @Override
                        public void onFailure(Call<BMIChartResponse> call, Throwable t) {
                            showMessage("Failed ...");

                        }
                    });

        }
    }


    //temp

    private void showMessage(String message) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

}
