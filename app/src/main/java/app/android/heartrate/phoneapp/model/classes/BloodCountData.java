package app.android.heartrate.phoneapp.model.classes;

import com.google.gson.annotations.SerializedName;

public class BloodCountData {
    public String date = "";
    public String dateTime = "";
    public int day = 0;

    public int hour = 0;
    public int minute;
    public int month = 0;
    public String month_string = "";
    public String notes = "";
    @SerializedName("platelets")
    public float platelets_value = 0.0f;
    @SerializedName("RBC")
    public float rbc_value = 0.0f;
    @SerializedName("WBC")
    public float wbc_value = 0.0f;
    @SerializedName("hemoglobin")
    public float hemoglobin_value = 0.0f;
    public int row_id = 0;
    public String time = "";
    public int user_id = 0;

    public int year = 0;
}
