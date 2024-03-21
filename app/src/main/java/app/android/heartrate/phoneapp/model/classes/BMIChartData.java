package app.android.heartrate.phoneapp.model.classes;

import com.google.gson.annotations.SerializedName;

public class BMIChartData {
    public String age = "";
    public String birth_date = "";
    public String bmi = "";
    public String date = "";
    @SerializedName("date_time")
    public String dateTime = "";
    public int day = 0;
    public String height = "";
    public String height_unit = "";
    public int hour = 0;
    public int month = 0;
    public String month_string = "";
    public int row_id = 0;
    public String time = "";
    public int user_id = 0;
    public String weight = "";
    public String weight_unit = "";
    public int year = 0;
}
