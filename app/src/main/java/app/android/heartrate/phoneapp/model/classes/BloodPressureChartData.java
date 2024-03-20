package app.android.heartrate.phoneapp.model.classes;

import com.google.gson.annotations.SerializedName;

public class BloodPressureChartData {
    public String date = "";
    @SerializedName("date_time")
    public String dateTime = "";
    public int day = 0;
    @SerializedName("diastolic")
    public int diastolic_value = 0;
    public int hour = 0;

    @SerializedName("mean_arterial_pressure")
    public int mean_arterial_pressure_value = 0;
    public int month = 0;
    public String month_string = "";
    public String notes = "";
    @SerializedName("pulse_pressure")
    public int pulse_pressure_value = 0;
    @SerializedName("pulse_rate")
    public int pulse_rate_value = 0;
    public String result = "";
    public int row_id = 0;
    @SerializedName("color")
    public String status_color = "";
    @SerializedName("systolic")
    public int systolic_value = 0;
    public String time = "";
    public int user_id = 0;
    public int year = 0;
}
