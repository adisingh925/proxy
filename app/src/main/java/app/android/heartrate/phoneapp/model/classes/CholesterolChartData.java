package app.android.heartrate.phoneapp.model.classes;

import com.google.gson.annotations.SerializedName;

public class CholesterolChartData {
    @SerializedName("cholesterol")
    public float cholesterol_value = 0.0f;
    public String date = "";
    @SerializedName("date_time")
    public String dateTime = "";
    public int day = 0;
    @SerializedName("HDL")
    public float hdl_value = 0.0f;
    public int hour = 0;
    @SerializedName("LDL")
    public float ldl_value = 0.0f;
    public int month = 0;
    public String month_string = "";
    public String notes = "";
    public String result = "";
    public int row_id = 0;
    public String time = "";
    @SerializedName("triglyceride")
    public float triglyceride_value = 0.0f;
    public int user_id = 0;
    public int year = 0;
}
