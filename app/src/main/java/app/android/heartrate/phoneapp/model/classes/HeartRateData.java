package app.android.heartrate.phoneapp.model.classes;

import com.google.gson.annotations.SerializedName;

public class HeartRateData {
    public int age = 0;
    public String color = "";
    public String current_status = "";
    public String date = "";
    public String dateTime = "";
    public int day = 0;
    public String gender = "";
    @SerializedName("heart_rate")
    public int heart_rate_value = 0;
    public int hour = 0;
    public int month = 0;
    public String month_string = "";
    public String notes = "";
    @SerializedName("rate_range")
    public String range = "";
    public String result = "";
    public int row_id = 0;
    public String time = "";
    public int user_id = 0;
    public int year = 0;
}
