package app.android.heartrate.phoneapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class StoredPreferencesValue {
    public static final String CELSIUS_KEY = "celsius_temp";
    public static final String CELSIUS_VALUE_KEY = "celsius_value";
    public static final String FAHRENHEIT_KEY = "fahrenheit_temp";
    public static final String FAHRENHEIT_VALUE_KEY = "fahrenheit_value";
    public static final int MODE = 0;
    public static final String PREF_NAME = "EnglishSpanishTranslator";
    public static final String PULSE_KEY = "pulse_rate";
    public static final String PULSE_VALUE_KEY = "pulse_value";

    public static void setDefaultCelsiusIndex(String str, int i, Context context) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putInt(str, i);
        edit.commit();
    }

    public static int getDefaultCelsiusIndex(String str, Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(str, 40);
    }

    public static void setDefaultFahrenheitIndex(String str, int i, Context context) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putInt(str, i);
        edit.commit();
    }

    public static int getDefaultFahrenheitIndex(String str, Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(str, 40);
    }

    public static void setDefaultPulseIndex(String str, int i, Context context) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putInt(str, i);
        edit.commit();
    }

    public static int getDefaultPulseIndex(String str, Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(str, 71);
    }

    public static void setDefaultPulseValue(int i, Context context) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putInt(PULSE_VALUE_KEY, i);
        edit.commit();
    }

    public static int getDefaultPulseValue(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(PULSE_VALUE_KEY, 72);
    }

    public static void setDefaultCelsiusValue(float f, Context context) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putFloat(CELSIUS_VALUE_KEY, f);
        edit.commit();
    }

    public static float getDefaultCelsiusValue(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getFloat(CELSIUS_VALUE_KEY, 37.0f);
    }

    public static void setDefaultFahrenheitValue(float f, Context context) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putFloat(FAHRENHEIT_VALUE_KEY, f);
        edit.commit();
    }

    public static float getDefaultFahrenheitValue(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getFloat(FAHRENHEIT_VALUE_KEY, 98.6f);
    }
}
