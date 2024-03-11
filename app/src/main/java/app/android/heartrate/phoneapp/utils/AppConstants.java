package app.android.heartrate.phoneapp.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.github.mikephil.charting.utils.Utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import app.android.heartrate.phoneapp.R;
import app.android.heartrate.phoneapp.model.classes.BMIData;
import app.android.heartrate.phoneapp.model.classes.BloodCountData;
import app.android.heartrate.phoneapp.model.classes.BloodPressureData;
import app.android.heartrate.phoneapp.model.classes.BloodSugarData;
import app.android.heartrate.phoneapp.model.classes.BodyTempData;
import app.android.heartrate.phoneapp.model.classes.CholesterolData;
import app.android.heartrate.phoneapp.model.classes.HeartRateData;
import app.android.heartrate.phoneapp.model.classes.MedicineData;
import app.android.heartrate.phoneapp.model.classes.UserProfileData;
import app.android.heartrate.phoneapp.model.classes.WeightData;


public class AppConstants {
    public static final String CROP_FOLDER_NAME = "CropImages";
    public static String FEVER = "Fever";
    public static String HEALTH_TRACKER_DB_NAME = "easy_health_tracker.db";
    public static String HEIGHT_CM = "cm";
    public static String HEIGHT_FEET = "feet";
    public static String HIGH_FEVER = "High Fever";
    public static String HYPOTHERMIA = "Hypothermia";
    public static final String MY_FOLDER_NAME = "HealthTracker";
    public static String NORMAL = "Normal";
    public static final String PROFILE_FOLDER_NAME = "UserProfile";
    public static String WATER_REMINDER_DB_NAME = "water_reminder.db";
    public static String age_unit_year = "year";
    public static String age_unit_years = "years";
    public static String bmi_result_moderately_obese = "Moderately obese";
    public static String bmi_result_normal = "Normal (healthy weight)";
    public static String bmi_result_overweight = "Overweight";
    public static String bmi_result_severely = "Severely underweight";
    public static String bmi_result_severely_obese = "Severely obese";
    public static String bmi_result_underweight = "Underweight";
    public static String bmi_result_very_severely = "Very severely underweight";
    public static String bmi_result_very_severely_obese = "Very severely obese";
    public static String bmi_unit_kg = "kg";
    public static String bmi_unit_lb = "lb";
    public static String cholesterol_result_borderline = "Borderline";
    public static String cholesterol_result_good = "Good";
    public static String cholesterol_result_high = "High";
    public static String cholesterol_result_low = "Low";
    public static String create_profile_messages = "There is no profile available for save data.\nPlease create a profile to record your data!";

    public static String data_deleted_messages = "Data deleted successfully!";

    public static String data_saved_messages = "Data saved successfully!";

    public static String data_updated_messages = "Data updated successfully!";
    public static int default_age_value = 20;
    public static int default_diastolic_value = 80;
    public static int default_hr_age_value = 25;
    public static int default_hr_value = 70;
    public static int default_pulse_rate_value = 60;
    public static int default_sugar_level_value = 150;
    public static int default_systolic_value = 150;
    public static int default_weight_value = 40;
    public static String error_field_require = "This field is required!";
    public static String error_height_range_kg = "Enter height between 0.5 & 2.5!";
    public static String error_height_range_pound = "Enter height between 19.68 & 98.42!";
    public static String fasting_testing = "Fasting";
    public static String general_testing = "General";
    public static String hr_gender_child = "Child";
    public static String hr_gender_female = "Female";
    public static String hr_gender_male = "Male";
    public static String hr_result_above_average = "ABOVE AVERAGE";
    public static String hr_result_athlete = "ATHLETE";
    public static String hr_result_average = "AVERAGE";
    public static String hr_result_below_average = "BELOW AVERAGE";
    public static String hr_result_excellent = "EXCELLENT";
    public static String hr_result_good = "GOOD";
    public static String hr_result_poor = "POOR";
    public static String hr_status_after_exercise = "After Exercise";
    public static String hr_status_angry = "Angry";
    public static String hr_status_before_exercise = "Before Exercise";
    public static String hr_status_fear_full = "Fearful";
    public static String hr_status_general = "General";
    public static String hr_status_in_love = "In Love";
    public static String hr_status_resting = "Resting";
    public static String hr_status_sad = "Sad";
    public static String hr_status_surprised = "Surprised";
    public static String hr_status_tired = "Tired";
    public static String hr_status_unwell = "Unwell";
    public static boolean is_blood_count_edit_mode = false;
    public static boolean is_bmi_edit_mode = false;
    public static boolean is_body_temp_edit_mode = false;
    public static boolean is_bp_edit_mode = false;
    public static boolean is_bs_edit_mode = false;
    public static boolean is_cholesterol_edit_mode = false;
    public static boolean is_come_from_start = true;
    public static boolean is_heart_rate_edit_mode = false;
    public static boolean is_medicine_edit_mode = false;
    public static boolean is_profile_edit_mode = false;
    public static boolean is_tools_interstitial_show = false;
    public static boolean is_weight_edit_mode = false;
    public static String post_meal_testing = "Post-meal";
    public static String pre_meal_testing = "Pre-meal";
    public static String pressure_level_high_crisis = "HIGH BLOOD PRESSURE CRISIS";
    public static String pressure_level_high_stage_1 = "HIGH BLOOD PRESSURE : STAGE 1 HYPERTENSION";
    public static String pressure_level_high_stage_2 = "HIGH BLOOD PRESSURE : STAGE 2 HYPERTENSION";
    public static String pressure_level_low = "LOW BLOOD PRESSURE (HYPOTENSION)";
    public static String pressure_level_normal = "NORMAL";
    public static String pressure_level_pre_hypertension = "PRE HYPERTENSION";
    public static String quick_bold_font_path = "quicksand_bold.ttf";
    public static String roboto_font_path = "Roboto-Regular.ttf";
    public static float ruler_long_height_ration = 0.6f;
    public static float ruler_short_height_ration = 0.2f;
    public static BloodCountData selected_blood_count_data = null;
    public static BMIData selected_bmi_data = null;
    public static BodyTempData selected_body_temp_data = null;
    public static BloodPressureData selected_bp_data = null;
    public static BloodSugarData selected_bs_data = null;
    public static CholesterolData selected_cholesterol_data = null;
    public static HeartRateData selected_heart_rate_data = null;
    public static MedicineData selected_medicine_data = null;
    public static UserProfileData selected_profile_data = null;
    public static WeightData selected_weight_data = null;
    public static String sugar_level_diabetes = "DIABETES";
    public static String sugar_level_low = "LOW";
    public static String sugar_level_normal = "NORMAL";
    public static String sugar_level_pre_diabetes = "PRE DIABETES (IMPAIRED GLUCOSE)";
    public static String values_not_natural = "Values are not natural!";

    public static float convertCelsiusToFahrenheit(float f) {
        return ((f * 9.0f) / 5.0f) + 32.0f;
    }


    public static float convertFahrenheitToCelsius(float f) {
        return ((f - 32.0f) * 5.0f) / 9.0f;
    }

    public static String setResultText(float f) {
        if (f < 15.0f) {
            return bmi_result_very_severely;
        }
        if (f >= 15.0f && f <= 16.0f) {
            return bmi_result_severely;
        }
        if (f > 16.0f && ((double) f) <= 18.5d) {
            return bmi_result_underweight;
        }
        if (((double) f) > 18.5d && f <= 25.0f) {
            return bmi_result_normal;
        }
        if (f > 25.0f && f <= 30.0f) {
            return bmi_result_overweight;
        }
        if (f > 30.0f && f <= 35.0f) {
            return bmi_result_moderately_obese;
        }
        if (f <= 35.0f || f > 40.0f) {
            return f > 40.0f ? bmi_result_very_severely_obese : "";
        }
        return bmi_result_severely_obese;
    }

    public static String GetCholesterolResultText(float f) {
        if (f > 240.0f) {
            return cholesterol_result_high;
        }
        if (f >= 200.0f && f <= 239.0f) {
            return cholesterol_result_borderline;
        }
        if (f <= 100.0f || f >= 200.0f) {
            return f <= 100.0f ? cholesterol_result_low : "";
        }
        return cholesterol_result_good;
    }

    public static String FeetToCentimeter(String str) {
        boolean isEmpty = TextUtils.isEmpty(str);
        double d = Utils.DOUBLE_EPSILON;
        if (!isEmpty) {
            if (str.contains("'")) {
                String substring = str.substring(0, str.indexOf("'"));
                if (!TextUtils.isEmpty(substring)) {
                    d = Utils.DOUBLE_EPSILON + (Double.valueOf(substring).doubleValue() * 30.48d);
                }
            }
            if (str.contains("\"")) {
                String substring2 = str.substring(str.indexOf("'") + 1, str.indexOf("\""));
                if (!TextUtils.isEmpty(substring2)) {
                    d += Double.valueOf(substring2).doubleValue() * 2.54d;
                }
            }
        }
        return new DecimalFormat("##.##").format(d);
    }

    public static String CentimeterToFeet(String str) {
        int i;
        int i2;
        if (!TextUtils.isEmpty(str)) {
            double doubleValue = Double.valueOf(str).doubleValue() / 2.54d;
            i = (int) Math.floor(doubleValue / 12.0d);
            double d = doubleValue - ((double) (i * 12));
            System.out.println(d);
            i2 = (int) Math.ceil(d);
        } else {
            i = 0;
            i2 = 0;
        }
        return String.format("%d' %d''", Integer.valueOf(i), Integer.valueOf(i2));
    }

    public static String roundToOneDigit(float f) {
        return String.format("%.1f%n", Float.valueOf(f));
    }

    public static float RoundDecimal(float f, int i) {
        return BigDecimal.valueOf(f).setScale(i, RoundingMode.HALF_UP).floatValue();
    }

    public static String getRealPathFromURI(Uri uri, Context context) {
        try {
            Cursor query = context.getContentResolver().query(uri, null, null, null, null);
            if (query == null) {
                return uri.getPath();
            }
            query.moveToFirst();
            @SuppressLint("Range") String string = query.getString(query.getColumnIndex("_data"));
            query.close();
            return string;
        } catch (Exception e) {
            e.printStackTrace();
            return uri.toString();
        }
    }

    public static void overridePendingTransitionEnter(Activity activity) {
        activity.overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
    }

    public static void overridePendingTransitionExit(Activity activity) {
        activity.overridePendingTransition(R.anim.activity_slide_from_left, R.anim.activity_slide_to_right);
    }
}
