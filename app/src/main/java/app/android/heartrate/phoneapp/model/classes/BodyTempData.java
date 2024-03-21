package app.android.heartrate.phoneapp.model.classes;

import com.google.gson.annotations.SerializedName;

public class BodyTempData {
    @SerializedName("celsius_name")
    public String celsius = "";
    @SerializedName("celsius_index")
    public int celsius_index = 0;
    @SerializedName("celsius")
    public float celsius_value = 0.0f;
    public String date = "";
    public String dateTime = "";
    public int day = 0;
    @SerializedName("fahrenheit_name")
    public String fahrenheit = "";
    @SerializedName("fahrenheit_index")
    public int fahrenheit_index = 0;
    @SerializedName("fahrenheit")
    public float fahrenheit_value = 0.0f;
    public String fever_type = "";
    public int hour = 0;
    public int month = 0;
    public String month_string = "";
    public String name = "";
    @SerializedName("pulse_name")
    public String pulse = "";
    @SerializedName("pulse_index")
    public int pulse_index = 0;
    @SerializedName("pulse")
    public float pulse_value = 0.0f;
    public int row_id = 0;
    public String tags = "";
    public String time = "";
    public int user_id = 0;
    public int year = 0;
}
