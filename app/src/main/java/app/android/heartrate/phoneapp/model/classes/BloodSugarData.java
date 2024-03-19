package app.android.heartrate.phoneapp.model.classes;

import com.google.gson.annotations.SerializedName;

public class BloodSugarData {
    @SerializedName("blood_adag")
    public float blood_ADAG = 0.0f;
    @SerializedName("blood_dcct")
    public float blood_DCCT = 0.0f;
    public String current_status = "";
    public String date = "";
    public String dateTime = "";
    public int day = 0;
    public float hemoglobin_level = 0.0f;
    public int hour = 0;
    @SerializedName("ketone_level")
    public float keton_level = 0.0f;
    public int month = 0;
    public String month_string = "";
    public String notes = "";
    public String result = "";
    public int row_id = 0;
    @SerializedName("color")
    public String status_color = "";
    public float sugar_level = 0.0f;
    public String time = "";
    public int user_id = 0;
    public int year = 0;
}
