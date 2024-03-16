package app.android.heartrate.phoneapp.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import app.android.heartrate.phoneapp.R;
import app.android.heartrate.phoneapp.model.classes.BMIChartData;
import app.android.heartrate.phoneapp.model.classes.BMIData;
import app.android.heartrate.phoneapp.model.classes.BloodCountChartData;
import app.android.heartrate.phoneapp.model.classes.BloodCountData;
import app.android.heartrate.phoneapp.model.classes.BloodPressureChartData;
import app.android.heartrate.phoneapp.model.classes.BloodPressureData;
import app.android.heartrate.phoneapp.model.classes.BloodSugarChartData;
import app.android.heartrate.phoneapp.model.classes.BloodSugarData;
import app.android.heartrate.phoneapp.model.classes.BodyTempChartAllData;
import app.android.heartrate.phoneapp.model.classes.BodyTempData;
import app.android.heartrate.phoneapp.model.classes.CholesterolChartData;
import app.android.heartrate.phoneapp.model.classes.CholesterolData;
import app.android.heartrate.phoneapp.model.classes.HeartRateChartData;
import app.android.heartrate.phoneapp.model.classes.HeartRateData;
import app.android.heartrate.phoneapp.model.classes.MedicineData;
import app.android.heartrate.phoneapp.model.classes.MedicineNameData;
import app.android.heartrate.phoneapp.model.classes.UserProfileData;
import app.android.heartrate.phoneapp.model.classes.UserProfileSpinnerData;
import app.android.heartrate.phoneapp.model.classes.WeightChartData;
import app.android.heartrate.phoneapp.model.classes.WeightData;
import app.android.heartrate.phoneapp.utils.AppConstants;

public class SQLiteHealthTracker {
    private static final String ALL_MEDICINE_TABLE = "all_medicines";
    private static final String BLOOD_COUNT_TABLE = "blood_count_data";
    private static final String BLOOD_PRESSURE_TABLE = "blood_pressure_data";
    private static final String BLOOD_SUGAR_TABLE = "blood_sugar_data";
    private static final String BMI_TABLE = "bmi_data";
    private static final String BODY_TEMP_DATA_TABLE = "body_temp_data";
    private static final String CHOLESTEROL_TABLE = "cholesterol_data";
    private static final String CREATE_ALL_MEDICINE_TABLE = "CREATE TABLE all_medicines (row_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, medicine_id VARCHAR,medicine_name VARCHAR);";
    private static final String CREATE_BLOOD_COUNT_TABLE = "CREATE TABLE blood_count_data (row_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, user_id INTEGER,date_time VARCHAR,date VARCHAR,time VARCHAR,day INTEGER,month INTEGER,year INTEGER,hour INTEGER,RBC FLOAT,WBC FLOAT,platelets FLOAT,hemoglobin FLOAT,notes VARCHAR NOT NULL DEFAULT '');";
    private static final String CREATE_BLOOD_PRESSURE_TABLE = "CREATE TABLE blood_pressure_data (row_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, user_id INTEGER,date_time VARCHAR,date VARCHAR,time VARCHAR,day INTEGER,month INTEGER,year INTEGER,hour INTEGER,systolic INTEGER,diastolic INTEGER,pulse_rate INTEGER,pulse_pressure INTEGER,mean_arterial_pressure INTEGER,color VARCHAR,result VARCHAR,notes VARCHAR NOT NULL DEFAULT '');";
    private static final String CREATE_BLOOD_SUGAR_TABLE = "CREATE TABLE blood_sugar_data (row_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, user_id INTEGER,date_time VARCHAR,date VARCHAR,time VARCHAR,day INTEGER,month INTEGER,year INTEGER,hour INTEGER,current_status VARCHAR,sugar_level FLOAT,ketone_level FLOAT,hemoglobin_level FLOAT,blood_adag FLOAT,blood_dcct FLOAT,color VARCHAR,result VARCHAR,notes VARCHAR NOT NULL DEFAULT '');";
    private static final String CREATE_BMI_TABLE = "CREATE TABLE bmi_data (row_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, user_id INTEGER,date_time VARCHAR,date VARCHAR,time VARCHAR,day INTEGER,month INTEGER,year INTEGER,hour INTEGER,weight VARCHAR,weight_unit VARCHAR,height VARCHAR,height_unit VARCHAR,age VARCHAR,bmi VARCHAR,birth_date VARCHAR);";
    private static final String CREATE_BODY_TEMP_DATA_TABLE = "CREATE TABLE body_temp_data (row_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, user_id INTEGER,name VARCHAR,date_time VARCHAR,date VARCHAR,time VARCHAR,day INTEGER,month INTEGER,year INTEGER,hour INTEGER,celsius VARCHAR,fahrenheit VARCHAR,pulse VARCHAR,tags VARCHAR);";
    private static final String CREATE_CHOLESTEROL_TABLE = "CREATE TABLE cholesterol_data (row_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, user_id INTEGER,date_time VARCHAR,date VARCHAR,time VARCHAR,day INTEGER,month INTEGER,year INTEGER,hour INTEGER,cholesterol FLOAT,HDL FLOAT,LDL FLOAT,triglyceride FLOAT,result VARCHAR,notes VARCHAR NOT NULL DEFAULT '');";
    private static final String CREATE_HEART_RATE_TABLE = "CREATE TABLE heart_rate_data (row_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, user_id INTEGER,date_time VARCHAR,date VARCHAR,time VARCHAR,day INTEGER,month INTEGER,year INTEGER,hour INTEGER,heart_rate INTEGER,age INTEGER,gender VARCHAR,current_status VARCHAR,color VARCHAR,result VARCHAR,range VARCHAR,notes VARCHAR NOT NULL DEFAULT '');";
    private static final String CREATE_MEDICINE_TABLE = "CREATE TABLE medicine_data (row_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, user_id INTEGER,date_time VARCHAR,date VARCHAR,time VARCHAR,day INTEGER,month INTEGER,year INTEGER,hour INTEGER,medicine_name VARCHAR,measure_unit VARCHAR,dosage VARCHAR,times_day VARCHAR,notes VARCHAR NOT NULL DEFAULT '');";
    private static final String CREATE_USER_PROFILE_TABLE = "CREATE TABLE user_profiles (row_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, user_id INTEGER,user_name VARCHAR,user_photo_path VARCHAR,gender VARCHAR,birth_date VARCHAR,weight VARCHAR,height_unit VARCHAR,height_feet VARCHAR,height VARCHAR);";
    private static final String CREATE_WEIGHT_DATA_TABLE = "CREATE TABLE weight_data (row_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, user_id INTEGER,date_time VARCHAR,date VARCHAR,time VARCHAR,day INTEGER,month INTEGER,year INTEGER,hour INTEGER,weight FLOAT,notes VARCHAR  NOT NULL DEFAULT '');";
    private static final int DATABASE_VERSION = 1;
    private static final String HEART_RATE_TABLE = "heart_rate_data";
    private static final String KEY_ADAG = "blood_adag";
    private static final String KEY_AGE = "age";
    private static final String KEY_BIRTH_DATE = "birth_date";
    private static final String KEY_BMI = "bmi";
    private static final String KEY_CELSIUS = "celsius";
    private static final String KEY_CHOLESTEROL = "cholesterol";
    private static final String KEY_COLOR = "color";
    private static final String KEY_CURRENT_STATUS = "current_status";
    private static final String KEY_DATE = "date";
    private static final String KEY_DATE_TIME = "date_time";
    private static final String KEY_DAY = "day";
    private static final String KEY_DCCT = "blood_dcct";
    private static final String KEY_DIASTOLIC = "diastolic";
    private static final String KEY_DOSAGE = "dosage";
    private static final String KEY_FAHRENHEIT = "fahrenheit";
    private static final String KEY_FEET = "feet";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_HDL = "HDL";
    private static final String KEY_HEART_RATE = "heart_rate";
    private static final String KEY_HEIGHT = "height";
    private static final String KEY_HEIGHT_FEET = "height_feet";
    private static final String KEY_HEIGHT_UNIT = "height_unit";
    private static final String KEY_HEMOGLOBIN = "hemoglobin";
    private static final String KEY_HEMOGLOBIN_LEVEL = "hemoglobin_level";
    private static final String KEY_HOUR = "hour";
    private static final String KEY_INCHES = "inches";
    private static final String KEY_KETONE_LEVEL = "ketone_level";
    private static final String KEY_LDL = "LDL";
    private static final String KEY_MEAN_ARTERIAL_PRESSURE = "mean_arterial_pressure";
    private static final String KEY_MEASURE_UNIT = "measure_unit";
    private static final String KEY_MEDICINE_ID = "medicine_id";
    private static final String KEY_MEDICINE_NAME = "medicine_name";
    private static final String KEY_MONTH = "month";
    private static final String KEY_NAME = "name";
    private static final String KEY_NOTES = "notes";
    private static final String KEY_PLATELETS = "platelets";
    private static final String KEY_POUNDS = "pounds";
    private static final String KEY_PULSE = "pulse";
    private static final String KEY_PULSE_PRESSURE = "pulse_pressure";
    private static final String KEY_PULSE_RATE = "pulse_rate";
    private static final String KEY_RANGE = "range";
    private static final String KEY_RBC = "RBC";
    private static final String KEY_RESULT = "result";
    private static final String KEY_ROW_ID = "row_id";
    private static final String KEY_SUGAR_LEVEL = "sugar_level";
    private static final String KEY_SYSTOLIC = "systolic";
    private static final String KEY_TAGS = "tags";
    private static final String KEY_TIME = "time";
    private static final String KEY_TIMES_DAY = "times_day";
    private static final String KEY_TRIGLYCERIDE = "triglyceride";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_PHOTO_PATH = "user_photo_path";
    private static final String KEY_WBC = "WBC";
    private static final String KEY_WEIGHT = "weight";
    private static final String KEY_WEIGHT_UNIT = "weight_unit";
    private static final String KEY_YEAR = "year";
    private static final String MEDICINE_TABLE = "medicine_data";
    private static final String USER_PROFILE_TABLE = "user_profiles";
    private static final String WEIGHT_DATA_TABLE = "weight_data";
    private static SQLiteDatabase sqLiteDatabase;
    private static SQLiteOpenHelper sqLiteHelper;
    private final Context mContext;
    String date_format = "dd/MM/yyyy";
    String date_time_format = "dd/MM/yyyy hh:mm a";
    String month_format = "MMMM";
    String time_format = "hh:mm a";

    public SQLiteHealthTracker(Context context) {
        this.mContext = context;
    }

    public static int GetUserProfileLatestRowID() {
        int i;
        SQLiteDatabase readableDatabase = sqLiteHelper.getReadableDatabase();
        sqLiteDatabase = readableDatabase;
        Cursor rawQuery = readableDatabase.rawQuery("SELECT row_id FROM user_profiles ORDER BY row_id DESC;", null);
        try {
            rawQuery.moveToFirst();
            i = rawQuery.getInt(0);
        } catch (Exception unused) {
            i = -1;
        }
        rawQuery.close();
        return i;
    }

    public static int GetLatestRowID() {
        int i;
        SQLiteDatabase readableDatabase = sqLiteHelper.getReadableDatabase();
        sqLiteDatabase = readableDatabase;
        Cursor rawQuery = readableDatabase.rawQuery("SELECT row_id FROM body_temp_data ORDER BY row_id DESC;", null);
        try {
            rawQuery.moveToFirst();
            i = rawQuery.getInt(0);
        } catch (Exception unused) {
            i = -1;
        }
        rawQuery.close();
        return i;
    }

    public static float[] GetTempStatisticsData() {
        SQLiteDatabase readableDatabase = sqLiteHelper.getReadableDatabase();
        sqLiteDatabase = readableDatabase;
        float[] fArr = new float[3];
        Cursor rawQuery = readableDatabase.rawQuery("SELECT SUM(celsius), SUM(fahrenheit), SUM(pulse), COUNT(celsius), COUNT(fahrenheit), COUNT(pulse) FROM body_temp_data", null);
        try {
            rawQuery.moveToFirst();
            fArr[0] = rawQuery.getFloat(0) / rawQuery.getFloat(3);
            fArr[1] = rawQuery.getFloat(1) / rawQuery.getFloat(4);
            fArr[2] = rawQuery.getFloat(2) / rawQuery.getFloat(5);
        } catch (Exception unused) {
        }
        rawQuery.close();
        return fArr;
    }

    public static ArrayList<String> hmSort(HashMap<String, Integer> hashMap) {
        LinkedList linkedList = new LinkedList(hashMap.entrySet());
        ArrayList<String> arrayList = new ArrayList<>();
        Iterator it = linkedList.iterator();
        while (it.hasNext()) {
            arrayList.add((String) ((Map.Entry) it.next()).getKey());
        }
        return arrayList;
    }

    public static ArrayList<String[]> csvData() {
        sqLiteDatabase = sqLiteHelper.getReadableDatabase();
        ArrayList<String[]> arrayList = new ArrayList<>();
        Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM body_temp_data", null);
        arrayList.add(new String[]{"Celsius", "Fahrenheit", "Pulse", "Day", "Month", "Year", "Date"});
        rawQuery.moveToFirst();
        while (!rawQuery.isAfterLast()) {
            arrayList.add(new String[]{rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID)), rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME)), rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_CELSIUS)), rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_FAHRENHEIT)), rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_PULSE)), rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DAY)), rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_MONTH))});
            rawQuery.moveToNext();
        }
        rawQuery.close();
        return arrayList;
    }

    public SQLiteHealthTracker open() throws SQLException {
        SQLiteHelper sQLiteHelper = new SQLiteHelper(this.mContext, AppConstants.HEALTH_TRACKER_DB_NAME, null, 1);
        sqLiteHelper = sQLiteHelper;
        sqLiteDatabase = sQLiteHelper.getWritableDatabase();
        return this;
    }

    public SQLiteHealthTracker openToRead() throws SQLException {
        SQLiteHelper sQLiteHelper = new SQLiteHelper(this.mContext, AppConstants.HEALTH_TRACKER_DB_NAME, null, 1);
        sqLiteHelper = sQLiteHelper;
        sqLiteDatabase = sQLiteHelper.getReadableDatabase();
        return this;
    }

    public SQLiteHealthTracker openToWrite() throws SQLException {
        SQLiteHelper sQLiteHelper = new SQLiteHelper(this.mContext, AppConstants.HEALTH_TRACKER_DB_NAME, null, 1);
        sqLiteHelper = sQLiteHelper;
        sqLiteDatabase = sQLiteHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        sqLiteHelper.close();
    }

    public long InsertUserProfileData(UserProfileData userProfileData) {
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        int i = userProfileData.user_id;
        String trim = userProfileData.user_name.trim();
        String trim2 = userProfileData.user_photo_path.trim();
        String trim3 = userProfileData.user_gender.trim();
        String trim4 = userProfileData.user_birth_date.trim();
        int i2 = userProfileData.user_weight;
        int i3 = userProfileData.user_height;
        String trim5 = userProfileData.user_height_unit.trim();
        String trim6 = userProfileData.user_height_feet.trim();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USER_ID, Integer.valueOf(i));
        contentValues.put(KEY_USER_NAME, trim);
        contentValues.put(KEY_USER_PHOTO_PATH, trim2);
        contentValues.put(KEY_GENDER, trim3);
        contentValues.put(KEY_BIRTH_DATE, trim4);
        contentValues.put(KEY_WEIGHT, Integer.valueOf(i2));
        contentValues.put(KEY_HEIGHT, Integer.valueOf(i3));
        contentValues.put(KEY_HEIGHT_UNIT, trim5);
        contentValues.put(KEY_HEIGHT_FEET, trim6);
        return sqLiteDatabase.insertWithOnConflict(USER_PROFILE_TABLE, null, contentValues, 4);
    }

    public long UpdateUserProfileData(int i, int i2, UserProfileData userProfileData) {
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        String trim = userProfileData.user_name.trim();
        String trim2 = userProfileData.user_photo_path.trim();
        String trim3 = userProfileData.user_gender.trim();
        String trim4 = userProfileData.user_birth_date.trim();
        int i3 = userProfileData.user_weight;
        int i4 = userProfileData.user_height;
        String trim5 = userProfileData.user_height_unit.trim();
        String trim6 = userProfileData.user_height_feet.trim();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USER_ID, Integer.valueOf(i2));
        contentValues.put(KEY_USER_NAME, trim);
        contentValues.put(KEY_USER_PHOTO_PATH, trim2);
        contentValues.put(KEY_GENDER, trim3);
        contentValues.put(KEY_BIRTH_DATE, trim4);
        contentValues.put(KEY_WEIGHT, Integer.valueOf(i3));
        contentValues.put(KEY_HEIGHT, Integer.valueOf(i4));
        contentValues.put(KEY_HEIGHT_UNIT, trim5);
        contentValues.put(KEY_HEIGHT_FEET, trim6);
        return sqLiteDatabase.update(USER_PROFILE_TABLE, contentValues, "row_id=" + i + " AND " + KEY_USER_ID + "=" + i2, null);
    }

    public List GetUserProfileData() {
        Bitmap bitmap;
        ArrayList arrayList = new ArrayList();
        try {
            SQLiteDatabase readableDatabase = sqLiteHelper.getReadableDatabase();
            sqLiteDatabase = readableDatabase;
            Cursor rawQuery = readableDatabase.rawQuery("SELECT * FROM user_profiles ORDER BY row_id DESC", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    do {
                        int i = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i2 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_USER_PHOTO_PATH));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_USER_NAME));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_GENDER));
                        String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_BIRTH_DATE));
                        int i3 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_WEIGHT));
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HEIGHT));
                        String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_HEIGHT_UNIT));
                        String string6 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_HEIGHT_FEET));
                        UserProfileData userProfileData = new UserProfileData();
                        userProfileData.row_id = i;
                        userProfileData.user_id = i2;
                        userProfileData.user_name = string2.trim();
                        userProfileData.user_photo_path = string.trim();
                        userProfileData.user_gender = string3.trim();
                        userProfileData.user_birth_date = string4.trim();
                        userProfileData.user_weight = i3;
                        userProfileData.user_height = i4;
                        userProfileData.user_height_unit = string5.trim();
                        userProfileData.user_height_feet = string6.trim();
                        if (string.contains("file://")) {
                            string = string.replaceAll("file://", "");
                        }
                        Log.e("Image Path:", string);
                        File file = new File(string);
                        if (file.exists()) {
                            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        } else {
                            bitmap = BitmapFactory.decodeResource(this.mContext.getResources(), R.drawable.ic_default_user);
                        }
                        userProfileData.bmp_user_photo = bitmap;
                        arrayList.add(userProfileData);
                    } while (rawQuery.moveToNext());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public List GetUserProfileDataByID(int i) {
        Bitmap bitmap;
        ArrayList arrayList = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor query = sqLiteDatabase.query(USER_PROFILE_TABLE, null, "user_id =?", new String[]{String.valueOf(i)}, null, null, null);
            if (query != null) {
                if (query.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (query.moveToFirst()) {
                    do {
                        int i2 = query.getInt(query.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i3 = query.getInt(query.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = query.getString(query.getColumnIndexOrThrow(KEY_USER_PHOTO_PATH));
                        String string2 = query.getString(query.getColumnIndexOrThrow(KEY_USER_NAME));
                        String string3 = query.getString(query.getColumnIndexOrThrow(KEY_GENDER));
                        String string4 = query.getString(query.getColumnIndexOrThrow(KEY_BIRTH_DATE));
                        int i4 = query.getInt(query.getColumnIndexOrThrow(KEY_WEIGHT));
                        int i5 = query.getInt(query.getColumnIndexOrThrow(KEY_HEIGHT));
                        String string5 = query.getString(query.getColumnIndexOrThrow(KEY_HEIGHT_UNIT));
                        String string6 = query.getString(query.getColumnIndexOrThrow(KEY_HEIGHT_FEET));
                        UserProfileSpinnerData userProfileSpinnerData = new UserProfileSpinnerData();
                        userProfileSpinnerData.row_id = i2;
                        userProfileSpinnerData.user_id = i3;
                        userProfileSpinnerData.user_name = string2.trim();
                        userProfileSpinnerData.user_photo_path = string.trim();
                        userProfileSpinnerData.user_gender = string3.trim();
                        userProfileSpinnerData.user_birth_date = string4.trim();
                        userProfileSpinnerData.user_weight = i4;
                        userProfileSpinnerData.user_height = i5;
                        userProfileSpinnerData.user_height_unit = string5.trim();
                        userProfileSpinnerData.user_height_feet = string6.trim();
                        if (string.contains("file://")) {
                            string = string.replaceAll("file://", "");
                        }
                        Log.e("Image Path:", string);
                        File file = new File(string);
                        if (file.exists()) {
                            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        } else {
                            bitmap = BitmapFactory.decodeResource(this.mContext.getResources(), R.drawable.ic_default_user);
                        }
                        userProfileSpinnerData.bmp_user_photo = bitmap;
                        arrayList.add(userProfileSpinnerData);
                    } while (query.moveToNext());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public String GetProfileNameByID(int i) {
        sqLiteDatabase = sqLiteHelper.getReadableDatabase();
        Cursor query = sqLiteDatabase.query(USER_PROFILE_TABLE, null, "user_id =?", new String[]{String.valueOf(i)}, null, null, null);
        String string = query.moveToLast() ? query.getString(query.getColumnIndexOrThrow(KEY_USER_NAME)) : "";
        query.close();
        sqLiteDatabase.close();
        return string;
    }

    public boolean CheckProfileDataExist() {
        SQLiteDatabase readableDatabase = sqLiteHelper.getReadableDatabase();
        sqLiteDatabase = readableDatabase;
        Cursor rawQuery = readableDatabase.rawQuery("SELECT * FROM user_profiles", null);
        return rawQuery != null && rawQuery.getCount() != 0;
    }

    public boolean CheckProfileNameExist(String str) {
        sqLiteDatabase = sqLiteHelper.getReadableDatabase();
        Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM user_profiles WHERE user_name=" + ("'" + str + "'"), null);
        return rawQuery != null && rawQuery.getCount() != 0;
    }

    public void deleteUserProfileByID(int i) {
        SQLiteDatabase sQLiteDatabase = sqLiteDatabase;
        sQLiteDatabase.delete(USER_PROFILE_TABLE, "row_id=" + i, null);
    }

    public void deleteAllProfileDataByUserID(int i) {
        SQLiteDatabase sQLiteDatabase = sqLiteDatabase;
        sQLiteDatabase.delete(USER_PROFILE_TABLE, "user_id=" + i, null);
        deleteWeightByUserID(i);
        deleteBloodSugarByUserID(i);
        deleteBloodPressureByUserID(i);
        deleteHeartRateByUserID(i);
        deleteBMIByUserID(i);
        deleteCholesterolByUserID(i);
        deleteBloodCountByUserID(i);
        deleteMedicinesByUserID(i);
        deleteBodyTempByUserID(i);
    }

    public void deleteUserProfileByName(String str) {
        SQLiteDatabase sQLiteDatabase = sqLiteDatabase;
        sQLiteDatabase.delete(USER_PROFILE_TABLE, "user_name=" + ("'" + str + "'"), null);
    }

    public void deleteAllUserProfileData() {
        sqLiteDatabase.execSQL("DELETE FROM user_profiles");
    }

    public long InsertBloodCountData(BloodCountData bloodCountData) {
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        int i = bloodCountData.user_id;
        String trim = bloodCountData.dateTime.trim();
        String trim2 = bloodCountData.date.trim();
        String trim3 = bloodCountData.time.trim();
        int i2 = bloodCountData.day;
        int i3 = bloodCountData.month;
        int i4 = bloodCountData.year;
        int i5 = bloodCountData.hour;
        float f = bloodCountData.rbc_value;
        float f2 = bloodCountData.wbc_value;
        float f3 = bloodCountData.platelets_value;
        float f4 = bloodCountData.hemoglobin_value;
        String trim4 = bloodCountData.notes.trim();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USER_ID, Integer.valueOf(i));
        contentValues.put(KEY_DATE_TIME, trim);
        contentValues.put(KEY_DATE, trim2);
        contentValues.put(KEY_TIME, trim3);
        contentValues.put(KEY_DAY, Integer.valueOf(i2));
        contentValues.put(KEY_MONTH, Integer.valueOf(i3));
        contentValues.put(KEY_YEAR, Integer.valueOf(i4));
        contentValues.put(KEY_HOUR, Integer.valueOf(i5));
        contentValues.put(KEY_RBC, Float.valueOf(f));
        contentValues.put(KEY_WBC, Float.valueOf(f2));
        contentValues.put(KEY_PLATELETS, Float.valueOf(f3));
        contentValues.put(KEY_HEMOGLOBIN, Float.valueOf(f4));
        contentValues.put(KEY_NOTES, trim4);
        return sqLiteDatabase.insertWithOnConflict(BLOOD_COUNT_TABLE, null, contentValues, 4);
    }

    public long UpdateBloodCountData(int i, int i2, BloodCountData bloodCountData) {
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        String trim = bloodCountData.dateTime.trim();
        String trim2 = bloodCountData.date.trim();
        String trim3 = bloodCountData.time.trim();
        int i3 = bloodCountData.day;
        int i4 = bloodCountData.month;
        int i5 = bloodCountData.year;
        int i6 = bloodCountData.hour;
        float f = bloodCountData.rbc_value;
        float f2 = bloodCountData.wbc_value;
        float f3 = bloodCountData.platelets_value;
        float f4 = bloodCountData.hemoglobin_value;
        String trim4 = bloodCountData.notes.trim();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USER_ID, Integer.valueOf(i2));
        contentValues.put(KEY_DATE_TIME, trim);
        contentValues.put(KEY_DATE, trim2);
        contentValues.put(KEY_TIME, trim3);
        contentValues.put(KEY_DAY, Integer.valueOf(i3));
        contentValues.put(KEY_MONTH, Integer.valueOf(i4));
        contentValues.put(KEY_YEAR, Integer.valueOf(i5));
        contentValues.put(KEY_HOUR, Integer.valueOf(i6));
        contentValues.put(KEY_RBC, Float.valueOf(f));
        contentValues.put(KEY_WBC, Float.valueOf(f2));
        contentValues.put(KEY_PLATELETS, Float.valueOf(f3));
        contentValues.put(KEY_HEMOGLOBIN, Float.valueOf(f4));
        contentValues.put(KEY_NOTES, trim4);
        return sqLiteDatabase.update(BLOOD_COUNT_TABLE, contentValues, "row_id=" + i, null);
    }

    public List GetBloodCountData() {
        Exception e;
        ArrayList arrayList = new ArrayList();
        try {
            SQLiteDatabase readableDatabase = sqLiteHelper.getReadableDatabase();
            sqLiteDatabase = readableDatabase;
            Cursor rawQuery = readableDatabase.rawQuery("SELECT * FROM blood_count_data ORDER BY row_id DESC", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    while (true) {
                        int i = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i2 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i3 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        float f = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_RBC));
                        float f2 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_WBC));
                        float f3 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_PLATELETS));
                        float f4 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_HEMOGLOBIN));
                        try {
                            String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_NOTES));
                            BloodCountData bloodCountData = new BloodCountData();
                            bloodCountData.row_id = i;
                            bloodCountData.user_id = i2;
                            bloodCountData.dateTime = string.trim();
                            bloodCountData.date = string2.trim();
                            bloodCountData.time = string3.trim();
                            bloodCountData.day = i3;
                            bloodCountData.month = i4;
                            bloodCountData.year = i5;
                            bloodCountData.hour = i6;
                            bloodCountData.rbc_value = f;
                            bloodCountData.wbc_value = f2;
                            bloodCountData.platelets_value = f3;
                            bloodCountData.hemoglobin_value = f4;
                            bloodCountData.notes = string4.trim();
                            try {
                                Date parse = new SimpleDateFormat(this.date_time_format).parse(string);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                                SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                                bloodCountData.date = simpleDateFormat.format(parse).trim();
                                bloodCountData.time = simpleDateFormat3.format(parse).trim();
                                bloodCountData.month_string = simpleDateFormat2.format(parse).trim();
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                            arrayList = arrayList;
                            arrayList.add(bloodCountData);
                            if (!rawQuery.moveToNext()) {
                                break;
                            }
                            rawQuery = rawQuery;
                        } catch (Exception e3) {
                            e = e3;
                            arrayList = arrayList;
                            e.printStackTrace();
                            return arrayList;
                        }
                    }
                }
            }
        } catch (Exception e4) {
            e = e4;
            e.printStackTrace();
            return arrayList;
        }
        return arrayList;
    }

    public List GetBloodCountDataByUserID(int i) {
        ArrayList arrayList;
        Exception e;
        String str = KEY_ROW_ID;
        ArrayList arrayList2 = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM blood_count_data WHERE user_id=" + i + " ORDER BY " + str + " DESC", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                    return arrayList2;
                } else if (rawQuery.moveToFirst()) {
                    while (true) {
                        int i2 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(str));
                        int i3 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        float f = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_RBC));
                        float f2 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_WBC));
                        float f3 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_PLATELETS));
                        float f4 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_HEMOGLOBIN));
                        try {
                            String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_NOTES));
                            BloodCountData bloodCountData = new BloodCountData();
                            bloodCountData.row_id = i2;
                            bloodCountData.user_id = i3;
                            bloodCountData.dateTime = string.trim();
                            bloodCountData.date = string2.trim();
                            bloodCountData.time = string3.trim();
                            bloodCountData.day = i4;
                            bloodCountData.month = i5;
                            bloodCountData.year = i6;
                            bloodCountData.hour = i7;
                            bloodCountData.rbc_value = f;
                            bloodCountData.wbc_value = f2;
                            bloodCountData.platelets_value = f3;
                            bloodCountData.hemoglobin_value = f4;
                            bloodCountData.notes = string4.trim();
                            try {
                                Date parse = new SimpleDateFormat(this.date_time_format).parse(string);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                                SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                                bloodCountData.date = simpleDateFormat.format(parse).trim();
                                bloodCountData.time = simpleDateFormat3.format(parse).trim();
                                bloodCountData.month_string = simpleDateFormat2.format(parse).trim();
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                            arrayList = arrayList2;
                            try {
                                arrayList.add(bloodCountData);
                                if (!rawQuery.moveToNext()) {
                                    return arrayList;
                                }
                                rawQuery = rawQuery;
                                arrayList2 = arrayList;
                                str = str;
                            } catch (Exception e3) {
                                e = e3;
                                e.printStackTrace();
                                return arrayList;
                            }
                        } catch (Exception e4) {
                            e = e4;
                            arrayList = arrayList2;
                            e.printStackTrace();
                            return arrayList;
                        }
                    }
                }
            }
            return arrayList2;
        } catch (Exception e5) {
            e = e5;
            arrayList = arrayList2;
            e.printStackTrace();
            return arrayList;
        }
    }

    public int GetMaxBloodCountValue(int i) {
        Exception e;
        int i2 = 0;
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT MAX(RBC) FROM blood_count_data WHERE user_id=" + i, null);
            if (rawQuery == null || rawQuery.getCount() <= 0 || !rawQuery.moveToFirst()) {
                return 0;
            }
            int i3 = 0;
            do {
                try {
                    i3 = rawQuery.getInt(0);
                } catch (Exception e2) {
                    e = e2;
                    i2 = i3;
                    e.printStackTrace();
                    return i2;
                }
            } while (rawQuery.moveToNext());
            return i3;
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            return i2;
        }
    }

    public void deleteBloodCountByID(int i) {
        SQLiteDatabase sQLiteDatabase = sqLiteDatabase;
        sQLiteDatabase.delete(BLOOD_COUNT_TABLE, "row_id=" + i, null);
    }

    public void deleteBloodCountByUserID(int i) {
        SQLiteDatabase sQLiteDatabase = sqLiteDatabase;
        sQLiteDatabase.delete(BLOOD_COUNT_TABLE, "user_id=" + i, null);
    }

    public void deleteAllBloodCountData() {
        sqLiteDatabase.execSQL("DELETE FROM blood_count_data");
    }

    public List GetBloodCountChartAllData(int i) {
        ArrayList arrayList;
        Exception e;
        String str = KEY_ROW_ID;
        ArrayList arrayList2 = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM blood_count_data WHERE user_id=" + i + " ORDER BY " + str + " DESC", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                    return arrayList2;
                } else if (rawQuery.moveToFirst()) {
                    while (true) {
                        int i2 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(str));
                        int i3 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        float f = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_RBC));
                        float f2 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_WBC));
                        float f3 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_PLATELETS));
                        float f4 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_HEMOGLOBIN));
                        try {
                            String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_NOTES));
                            BloodCountChartData bloodCountChartData = new BloodCountChartData();
                            bloodCountChartData.row_id = i2;
                            bloodCountChartData.user_id = i3;
                            bloodCountChartData.dateTime = string.trim();
                            bloodCountChartData.date = string2.trim();
                            bloodCountChartData.time = string3.trim();
                            bloodCountChartData.day = i4;
                            bloodCountChartData.month = i5;
                            bloodCountChartData.year = i6;
                            bloodCountChartData.hour = i7;
                            bloodCountChartData.rbc_value = f;
                            bloodCountChartData.wbc_value = f2;
                            bloodCountChartData.platelets_value = f3;
                            bloodCountChartData.hemoglobin_value = f4;
                            bloodCountChartData.notes = string4.trim();
                            try {
                                Date parse = new SimpleDateFormat(this.date_time_format).parse(string);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                                SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                                bloodCountChartData.date = simpleDateFormat.format(parse).trim();
                                bloodCountChartData.time = simpleDateFormat3.format(parse).trim();
                                bloodCountChartData.month_string = simpleDateFormat2.format(parse).trim();
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                            arrayList = arrayList2;
                            try {
                                arrayList.add(bloodCountChartData);
                                if (!rawQuery.moveToNext()) {
                                    return arrayList;
                                }
                                rawQuery = rawQuery;
                                arrayList2 = arrayList;
                                str = str;
                            } catch (Exception e3) {
                                e = e3;
                                e.printStackTrace();
                                return arrayList;
                            }
                        } catch (Exception e4) {
                            e = e4;
                            arrayList = arrayList2;
                            e.printStackTrace();
                            return arrayList;
                        }
                    }
                }
            }
            return arrayList2;
        } catch (Exception e5) {
            e = e5;
            arrayList = arrayList2;
            e.printStackTrace();
            return arrayList;
        }
    }

    public List GetBloodCountChartTodayData(int i, int i2, int i3, int i4) {
        Exception e;
        ArrayList arrayList = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM blood_count_data WHERE day = " + i2 + " AND month = " + i3 + " AND year = " + i4 + " AND user_id = " + i + " ORDER BY row_id ASC;", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    while (true) {
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i8 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i9 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i10 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        float f = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_RBC));
                        float f2 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_WBC));
                        float f3 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_PLATELETS));
                        float f4 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_HEMOGLOBIN));
                        try {
                            String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_NOTES));
                            BloodCountChartData bloodCountChartData = new BloodCountChartData();
                            bloodCountChartData.row_id = i5;
                            bloodCountChartData.user_id = i6;
                            bloodCountChartData.dateTime = string.trim();
                            bloodCountChartData.date = string2.trim();
                            bloodCountChartData.time = string3.trim();
                            bloodCountChartData.day = i7;
                            bloodCountChartData.month = i8;
                            bloodCountChartData.year = i9;
                            bloodCountChartData.hour = i10;
                            bloodCountChartData.rbc_value = f;
                            bloodCountChartData.wbc_value = f2;
                            bloodCountChartData.platelets_value = f3;
                            bloodCountChartData.hemoglobin_value = f4;
                            bloodCountChartData.notes = string4.trim();
                            try {
                                Date parse = new SimpleDateFormat(this.date_time_format).parse(string);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                                SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                                bloodCountChartData.date = simpleDateFormat.format(parse).trim();
                                bloodCountChartData.time = simpleDateFormat3.format(parse).trim();
                                bloodCountChartData.month_string = simpleDateFormat2.format(parse).trim();
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                            arrayList = arrayList;
                            arrayList.add(bloodCountChartData);
                            if (!rawQuery.moveToNext()) {
                                break;
                            }
                            rawQuery = rawQuery;
                        } catch (Exception e3) {
                            e = e3;
                            arrayList = arrayList;
                            e.printStackTrace();
                            return arrayList;
                        }
                    }
                }
            }
        } catch (Exception e4) {
            e = e4;
            e.printStackTrace();
            return arrayList;
        }
        return arrayList;
    }

    public List GetBloodCountChartMonthlyData(int i, int i2, int i3) {
        Exception e;
        ArrayList arrayList = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM blood_count_data WHERE month = " + i2 + " AND year = " + i3 + " AND user_id = " + i + " ORDER BY row_id ASC;", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    while (true) {
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i8 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i9 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        float f = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_RBC));
                        float f2 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_WBC));
                        float f3 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_PLATELETS));
                        float f4 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_HEMOGLOBIN));
                        try {
                            String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_NOTES));
                            BloodCountChartData bloodCountChartData = new BloodCountChartData();
                            bloodCountChartData.row_id = i4;
                            bloodCountChartData.user_id = i5;
                            bloodCountChartData.dateTime = string.trim();
                            bloodCountChartData.date = string2.trim();
                            bloodCountChartData.time = string3.trim();
                            bloodCountChartData.day = i6;
                            bloodCountChartData.month = i7;
                            bloodCountChartData.year = i8;
                            bloodCountChartData.hour = i9;
                            bloodCountChartData.rbc_value = f;
                            bloodCountChartData.wbc_value = f2;
                            bloodCountChartData.platelets_value = f3;
                            bloodCountChartData.hemoglobin_value = f4;
                            bloodCountChartData.notes = string4.trim();
                            try {
                                Date parse = new SimpleDateFormat(this.date_time_format).parse(string);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                                SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                                bloodCountChartData.date = simpleDateFormat.format(parse).trim();
                                bloodCountChartData.time = simpleDateFormat3.format(parse).trim();
                                bloodCountChartData.month_string = simpleDateFormat2.format(parse).trim();
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                            arrayList = arrayList;
                            arrayList.add(bloodCountChartData);
                            if (!rawQuery.moveToNext()) {
                                break;
                            }
                            rawQuery = rawQuery;
                        } catch (Exception e3) {
                            e = e3;
                            arrayList = arrayList;
                            e.printStackTrace();
                            return arrayList;
                        }
                    }
                }
            }
        } catch (Exception e4) {
            e = e4;
            e.printStackTrace();
            return arrayList;
        }
        return arrayList;
    }

    public List GetBloodCountChartYearlyData(int i, int i2) {
        Exception e;
        ArrayList arrayList = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM blood_count_data WHERE year = " + i2 + " AND user_id = " + i + " ORDER BY row_id ASC;", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    while (true) {
                        int i3 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i8 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        float f = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_RBC));
                        float f2 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_WBC));
                        float f3 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_PLATELETS));
                        float f4 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_HEMOGLOBIN));
                        try {
                            String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_NOTES));
                            BloodCountChartData bloodCountChartData = new BloodCountChartData();
                            bloodCountChartData.row_id = i3;
                            bloodCountChartData.user_id = i4;
                            bloodCountChartData.dateTime = string.trim();
                            bloodCountChartData.date = string2.trim();
                            bloodCountChartData.time = string3.trim();
                            bloodCountChartData.day = i5;
                            bloodCountChartData.month = i6;
                            bloodCountChartData.year = i7;
                            bloodCountChartData.hour = i8;
                            bloodCountChartData.rbc_value = f;
                            bloodCountChartData.wbc_value = f2;
                            bloodCountChartData.platelets_value = f3;
                            bloodCountChartData.hemoglobin_value = f4;
                            bloodCountChartData.notes = string4.trim();
                            try {
                                Date parse = new SimpleDateFormat(this.date_time_format).parse(string);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                                SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                                bloodCountChartData.date = simpleDateFormat.format(parse).trim();
                                bloodCountChartData.time = simpleDateFormat3.format(parse).trim();
                                bloodCountChartData.month_string = simpleDateFormat2.format(parse).trim();
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                            arrayList = arrayList;
                            arrayList.add(bloodCountChartData);
                            if (!rawQuery.moveToNext()) {
                                break;
                            }
                            rawQuery = rawQuery;
                        } catch (Exception e3) {
                            e = e3;
                            arrayList = arrayList;
                            e.printStackTrace();
                            return arrayList;
                        }
                    }
                }
            }
        } catch (Exception e4) {
            e = e4;
            e.printStackTrace();
            return arrayList;
        }
        return arrayList;
    }

    public List GetBloodCountChartCustomData(int i, String str, String str2) {
        Exception e;
        ArrayList arrayList = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM blood_count_data WHERE user_id = " + i + " AND date >= " + ("'" + str + "'") + " AND date <= " + ("'" + str2 + "'") + " ORDER BY row_id ASC;", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    while (true) {
                        int i2 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i3 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        float f = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_RBC));
                        float f2 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_WBC));
                        float f3 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_PLATELETS));
                        float f4 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_HEMOGLOBIN));
                        try {
                            String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_NOTES));
                            BloodCountChartData bloodCountChartData = new BloodCountChartData();
                            bloodCountChartData.row_id = i2;
                            bloodCountChartData.user_id = i3;
                            bloodCountChartData.dateTime = string.trim();
                            bloodCountChartData.date = string2.trim();
                            bloodCountChartData.time = string3.trim();
                            bloodCountChartData.day = i4;
                            bloodCountChartData.month = i5;
                            bloodCountChartData.year = i6;
                            bloodCountChartData.hour = i7;
                            bloodCountChartData.rbc_value = f;
                            bloodCountChartData.wbc_value = f2;
                            bloodCountChartData.platelets_value = f3;
                            bloodCountChartData.hemoglobin_value = f4;
                            bloodCountChartData.notes = string4.trim();
                            try {
                                Date parse = new SimpleDateFormat(this.date_time_format).parse(string);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                                SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                                bloodCountChartData.date = simpleDateFormat.format(parse).trim();
                                bloodCountChartData.time = simpleDateFormat3.format(parse).trim();
                                bloodCountChartData.month_string = simpleDateFormat2.format(parse).trim();
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                            arrayList = arrayList;
                            arrayList.add(bloodCountChartData);
                            if (!rawQuery.moveToNext()) {
                                break;
                            }
                            rawQuery = rawQuery;
                        } catch (Exception e3) {
                            e = e3;
                            arrayList = arrayList;
                            e.printStackTrace();
                            return arrayList;
                        }
                    }
                }
            }
        } catch (Exception e4) {
            e = e4;
            e.printStackTrace();
            return arrayList;
        }
        return arrayList;
    }

    public long InsertBloodPressureData(BloodPressureData bloodPressureData) {
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        int i = bloodPressureData.user_id;
        String trim = bloodPressureData.dateTime.trim();
        String trim2 = bloodPressureData.date.trim();
        String trim3 = bloodPressureData.time.trim();
        int i2 = bloodPressureData.day;
        int i3 = bloodPressureData.month;
        int i4 = bloodPressureData.year;
        int i5 = bloodPressureData.hour;
        int i6 = bloodPressureData.systolic_value;
        int i7 = bloodPressureData.diastolic_value;
        int i8 = bloodPressureData.pulse_rate_value;
        int i9 = bloodPressureData.pulse_pressure_value;
        int i10 = bloodPressureData.mean_arterial_pressure_value;
        String trim4 = bloodPressureData.status_color.trim();
        String trim5 = bloodPressureData.result.trim();
        String trim6 = bloodPressureData.notes.trim();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USER_ID, Integer.valueOf(i));
        contentValues.put(KEY_DATE_TIME, trim);
        contentValues.put(KEY_DATE, trim2);
        contentValues.put(KEY_TIME, trim3);
        contentValues.put(KEY_DAY, Integer.valueOf(i2));
        contentValues.put(KEY_MONTH, Integer.valueOf(i3));
        contentValues.put(KEY_YEAR, Integer.valueOf(i4));
        contentValues.put(KEY_HOUR, Integer.valueOf(i5));
        contentValues.put(KEY_SYSTOLIC, Integer.valueOf(i6));
        contentValues.put(KEY_DIASTOLIC, Integer.valueOf(i7));
        contentValues.put("pulse_rate", Integer.valueOf(i8));
        contentValues.put(KEY_PULSE_PRESSURE, Integer.valueOf(i9));
        contentValues.put(KEY_MEAN_ARTERIAL_PRESSURE, Integer.valueOf(i10));
        contentValues.put("color", trim4);
        contentValues.put(KEY_RESULT, trim5);
        contentValues.put(KEY_NOTES, trim6);
        return sqLiteDatabase.insertWithOnConflict(BLOOD_PRESSURE_TABLE, null, contentValues, 4);
    }

    public long UpdateBloodPressureData(int i, int i2, BloodPressureData bloodPressureData) {
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        String trim = bloodPressureData.dateTime.trim();
        String trim2 = bloodPressureData.date.trim();
        String trim3 = bloodPressureData.time.trim();
        int i3 = bloodPressureData.day;
        int i4 = bloodPressureData.month;
        int i5 = bloodPressureData.year;
        int i6 = bloodPressureData.hour;
        int i7 = bloodPressureData.systolic_value;
        int i8 = bloodPressureData.diastolic_value;
        int i9 = bloodPressureData.pulse_rate_value;
        int i10 = bloodPressureData.pulse_pressure_value;
        int i11 = bloodPressureData.mean_arterial_pressure_value;
        String trim4 = bloodPressureData.status_color.trim();
        String trim5 = bloodPressureData.result.trim();
        String trim6 = bloodPressureData.notes.trim();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USER_ID, Integer.valueOf(i2));
        contentValues.put(KEY_DATE_TIME, trim);
        contentValues.put(KEY_DATE, trim2);
        contentValues.put(KEY_TIME, trim3);
        contentValues.put(KEY_DAY, Integer.valueOf(i3));
        contentValues.put(KEY_MONTH, Integer.valueOf(i4));
        contentValues.put(KEY_YEAR, Integer.valueOf(i5));
        contentValues.put(KEY_HOUR, Integer.valueOf(i6));
        contentValues.put(KEY_SYSTOLIC, Integer.valueOf(i7));
        contentValues.put(KEY_DIASTOLIC, Integer.valueOf(i8));
        contentValues.put("pulse_rate", Integer.valueOf(i9));
        contentValues.put(KEY_PULSE_PRESSURE, Integer.valueOf(i10));
        contentValues.put(KEY_MEAN_ARTERIAL_PRESSURE, Integer.valueOf(i11));
        contentValues.put("color", trim4);
        contentValues.put(KEY_RESULT, trim5);
        contentValues.put(KEY_NOTES, trim6);
        return sqLiteDatabase.update(BLOOD_PRESSURE_TABLE, contentValues, "row_id=" + i, null);
    }

    public List GetBloodPressureData() {
        Exception e;
        Exception e2;
        ArrayList arrayList = new ArrayList();
        try {
            SQLiteDatabase readableDatabase = sqLiteHelper.getReadableDatabase();
            sqLiteDatabase = readableDatabase;
            Cursor rawQuery = readableDatabase.rawQuery("SELECT * FROM blood_pressure_data ORDER BY row_id DESC", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    while (true) {
                        int i = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i2 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i3 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_SYSTOLIC));
                        int i8 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DIASTOLIC));
                        int i9 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow("pulse_rate"));
                        int i10 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_PULSE_PRESSURE));
                        try {
                            int i11 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MEAN_ARTERIAL_PRESSURE));
                            try {
                                String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow("color"));
                                String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_RESULT));
                                String string6 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_NOTES));
                                BloodPressureData bloodPressureData = new BloodPressureData();
                                bloodPressureData.row_id = i;
                                bloodPressureData.user_id = i2;
                                bloodPressureData.dateTime = string.trim();
                                bloodPressureData.date = string2.trim();
                                bloodPressureData.time = string3.trim();
                                bloodPressureData.day = i3;
                                bloodPressureData.month = i4;
                                bloodPressureData.year = i5;
                                bloodPressureData.hour = i6;
                                bloodPressureData.systolic_value = i7;
                                bloodPressureData.diastolic_value = i8;
                                bloodPressureData.pulse_rate_value = i9;
                                bloodPressureData.pulse_pressure_value = i10;
                                bloodPressureData.mean_arterial_pressure_value = i11;
                                bloodPressureData.status_color = string4.trim();
                                bloodPressureData.result = string5.trim();
                                bloodPressureData.notes = string6.trim();
                                try {
                                    try {
                                        Date parse = new SimpleDateFormat(this.date_time_format).parse(string);
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                                        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                                        bloodPressureData.date = simpleDateFormat.format(parse).trim();
                                        bloodPressureData.time = simpleDateFormat3.format(parse).trim();
                                        bloodPressureData.month_string = simpleDateFormat2.format(parse).trim();
                                    } catch (Exception e3) {
                                        e2 = e3;
                                    }
                                } catch (Exception e4) {
                                    e2 = e4;
                                    e2.printStackTrace();
                                    arrayList = arrayList;
                                    arrayList.add(bloodPressureData);
                                    if (rawQuery.moveToNext()) {
                                    }
                                }
                                arrayList = arrayList;
                                arrayList.add(bloodPressureData);
                                if (rawQuery.moveToNext()) {
                                    break;
                                }
                                rawQuery = rawQuery;
                            } catch (Exception e5) {
                                e = e5;
                                arrayList = arrayList;
                                e.printStackTrace();
                                return arrayList;
                            }
                        } catch (Exception e6) {
                            e = e6;
                            arrayList = arrayList;
                            e.printStackTrace();
                            return arrayList;
                        }
                    }
                }
            }
        } catch (Exception e7) {
            e = e7;
            e.printStackTrace();
            return arrayList;
        }
        return arrayList;
    }

    public int GetMaxBloodPressureValue(int i) {
        int i2;
        int i3;
        int[] iArr = new int[2];
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT MAX(systolic) FROM blood_pressure_data WHERE user_id=" + i, null);
            if (rawQuery == null || rawQuery.getCount() <= 0 || !rawQuery.moveToFirst()) {
                i2 = 0;
            } else {
                do {
                    i2 = rawQuery.getInt(0);
                } while (rawQuery.moveToNext());
            }
            Cursor rawQuery2 = sqLiteDatabase.rawQuery("SELECT MAX(diastolic) FROM blood_pressure_data WHERE user_id=" + i, null);
            if (rawQuery2 == null || rawQuery2.getCount() <= 0 || !rawQuery2.moveToFirst()) {
                i3 = 0;
            } else {
                do {
                    i3 = rawQuery2.getInt(0);
                } while (rawQuery2.moveToNext());
            }
            iArr[0] = i2;
            iArr[1] = i3;
            ArrayList arrayList = new ArrayList();
            for (int i4 = 0; i4 < 2; i4++) {
                arrayList.add(Integer.valueOf(iArr[i4]));
            }
            int intValue = ((Integer) Collections.max(arrayList)).intValue();
            Log.e("Blood Pressure :", "Graph Max Y Axis :- " + intValue);
            return intValue;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List GetBloodPressureDataByUserID(int i) {
        ArrayList arrayList;
        Exception e;
        BloodPressureData bloodPressureData;
        Exception e2;
        String str = KEY_ROW_ID;
        ArrayList arrayList2 = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM blood_pressure_data WHERE user_id=" + i + " ORDER BY " + str + " DESC", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                    return arrayList2;
                } else if (rawQuery.moveToFirst()) {
                    while (true) {
                        int i2 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(str));
                        int i3 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        int i8 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_SYSTOLIC));
                        int i9 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DIASTOLIC));
                        int i10 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow("pulse_rate"));
                        int i11 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_PULSE_PRESSURE));
                        try {
                            int i12 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MEAN_ARTERIAL_PRESSURE));
                            try {
                                String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow("color"));
                                String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_RESULT));
                                String string6 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_NOTES));
                                bloodPressureData = new BloodPressureData();
                                bloodPressureData.row_id = i2;
                                bloodPressureData.user_id = i3;
                                bloodPressureData.dateTime = string.trim();
                                bloodPressureData.date = string2.trim();
                                bloodPressureData.time = string3.trim();
                                bloodPressureData.day = i4;
                                bloodPressureData.month = i5;
                                bloodPressureData.year = i6;
                                bloodPressureData.hour = i7;
                                bloodPressureData.systolic_value = i8;
                                bloodPressureData.diastolic_value = i9;
                                bloodPressureData.pulse_rate_value = i10;
                                bloodPressureData.pulse_pressure_value = i11;
                                bloodPressureData.mean_arterial_pressure_value = i12;
                                bloodPressureData.status_color = string4.trim();
                                bloodPressureData.result = string5.trim();
                                bloodPressureData.notes = string6.trim();
                                try {
                                    try {
                                        Date parse = new SimpleDateFormat(this.date_time_format).parse(string);
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                                        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                                        bloodPressureData.date = simpleDateFormat.format(parse).trim();
                                        bloodPressureData.time = simpleDateFormat3.format(parse).trim();
                                        bloodPressureData.month_string = simpleDateFormat2.format(parse).trim();
                                    } catch (Exception e3) {
                                        e2 = e3;
                                    }
                                } catch (Exception e4) {
                                    e2 = e4;
                                    e2.printStackTrace();
                                    arrayList = arrayList2;
                                    arrayList.add(bloodPressureData);
                                    if (rawQuery.moveToNext()) {
                                    }
                                }
                                arrayList = arrayList2;
                            } catch (Exception e5) {
                                e = e5;
                                arrayList = arrayList2;
                                e.printStackTrace();
                                return arrayList;
                            }
                        } catch (Exception e6) {
                            e = e6;
                            arrayList = arrayList2;
                            e.printStackTrace();
                            return arrayList;
                        }
                        try {
                            arrayList.add(bloodPressureData);
                            if (!rawQuery.moveToNext()) {
                                return arrayList;
                            }
                            arrayList2 = arrayList;
                            str = str;
                            rawQuery = rawQuery;
                        } catch (Exception e7) {
                            e = e7;
                            e.printStackTrace();
                            return arrayList;
                        }
                    }
                }
            }
            return arrayList2;
        } catch (Exception e8) {
            e = e8;
            arrayList = arrayList2;
            e.printStackTrace();
            return arrayList;
        }
    }

    public void deleteBloodPressureByID(int i) {
        SQLiteDatabase sQLiteDatabase = sqLiteDatabase;
        sQLiteDatabase.delete(BLOOD_PRESSURE_TABLE, "row_id=" + i, null);
    }

    public void deleteBloodPressureByUserID(int i) {
        SQLiteDatabase sQLiteDatabase = sqLiteDatabase;
        sQLiteDatabase.delete(BLOOD_PRESSURE_TABLE, "user_id=" + i, null);
    }

    public void deleteAllBloodPressureData() {
        sqLiteDatabase.execSQL("DELETE FROM blood_pressure_data");
    }

    public List GetBloodPressureChartAllData(int i) {
        ArrayList arrayList;
        Exception e;
        BloodPressureChartData bloodPressureChartData;
        Exception e2;
        String str = KEY_ROW_ID;
        ArrayList arrayList2 = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM blood_pressure_data WHERE user_id=" + i + " ORDER BY " + str + " DESC", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                    return arrayList2;
                } else if (rawQuery.moveToFirst()) {
                    while (true) {
                        int i2 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(str));
                        int i3 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        int i8 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_SYSTOLIC));
                        int i9 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DIASTOLIC));
                        int i10 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow("pulse_rate"));
                        int i11 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_PULSE_PRESSURE));
                        try {
                            int i12 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MEAN_ARTERIAL_PRESSURE));
                            try {
                                String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow("color"));
                                String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_RESULT));
                                String string6 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_NOTES));
                                bloodPressureChartData = new BloodPressureChartData();
                                bloodPressureChartData.row_id = i2;
                                bloodPressureChartData.user_id = i3;
                                bloodPressureChartData.dateTime = string.trim();
                                bloodPressureChartData.date = string2.trim();
                                bloodPressureChartData.time = string3.trim();
                                bloodPressureChartData.day = i4;
                                bloodPressureChartData.month = i5;
                                bloodPressureChartData.year = i6;
                                bloodPressureChartData.hour = i7;
                                bloodPressureChartData.systolic_value = i8;
                                bloodPressureChartData.diastolic_value = i9;
                                bloodPressureChartData.pulse_rate_value = i10;
                                bloodPressureChartData.pulse_pressure_value = i11;
                                bloodPressureChartData.mean_arterial_pressure_value = i12;
                                bloodPressureChartData.status_color = string4.trim();
                                bloodPressureChartData.result = string5.trim();
                                bloodPressureChartData.notes = string6.trim();
                                try {
                                    try {
                                        Date parse = new SimpleDateFormat(this.date_time_format).parse(string);
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                                        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                                        bloodPressureChartData.date = simpleDateFormat.format(parse).trim();
                                        bloodPressureChartData.time = simpleDateFormat3.format(parse).trim();
                                        bloodPressureChartData.month_string = simpleDateFormat2.format(parse).trim();
                                    } catch (Exception e3) {
                                        e2 = e3;
                                    }
                                } catch (Exception e4) {
                                    e2 = e4;
                                    e2.printStackTrace();
                                    arrayList = arrayList2;
                                    arrayList.add(bloodPressureChartData);
                                    if (rawQuery.moveToNext()) {
                                    }
                                }
                                arrayList = arrayList2;
                            } catch (Exception e5) {
                                e = e5;
                                arrayList = arrayList2;
                                e.printStackTrace();
                                return arrayList;
                            }
                        } catch (Exception e6) {
                            e = e6;
                            arrayList = arrayList2;
                            e.printStackTrace();
                            return arrayList;
                        }
                        try {
                            arrayList.add(bloodPressureChartData);
                            if (rawQuery.moveToNext()) {
                                return arrayList;
                            }
                            arrayList2 = arrayList;
                            str = str;
                            rawQuery = rawQuery;
                        } catch (Exception e7) {
                            e = e7;
                            e.printStackTrace();
                            return arrayList;
                        }
                    }
                }
            }
            return arrayList2;
        } catch (Exception e8) {
            e = e8;
            arrayList = arrayList2;
            e.printStackTrace();
            return arrayList;
        }
    }

    public List GetBloodPressureChartTodayData(int i, int i2, int i3, int i4) {
        Exception e;
        Exception e2;
        ArrayList arrayList = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM blood_pressure_data WHERE day = " + i2 + " AND month = " + i3 + " AND year = " + i4 + " AND user_id = " + i + " ORDER BY row_id ASC;", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    while (true) {
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i8 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i9 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i10 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        int i11 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_SYSTOLIC));
                        int i12 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DIASTOLIC));
                        int i13 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow("pulse_rate"));
                        int i14 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_PULSE_PRESSURE));
                        try {
                            int i15 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MEAN_ARTERIAL_PRESSURE));
                            try {
                                String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow("color"));
                                String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_RESULT));
                                String string6 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_NOTES));
                                BloodPressureChartData bloodPressureChartData = new BloodPressureChartData();
                                bloodPressureChartData.row_id = i5;
                                bloodPressureChartData.user_id = i6;
                                bloodPressureChartData.dateTime = string.trim();
                                bloodPressureChartData.date = string2.trim();
                                bloodPressureChartData.time = string3.trim();
                                bloodPressureChartData.day = i7;
                                bloodPressureChartData.month = i8;
                                bloodPressureChartData.year = i9;
                                bloodPressureChartData.hour = i10;
                                bloodPressureChartData.systolic_value = i11;
                                bloodPressureChartData.diastolic_value = i12;
                                bloodPressureChartData.pulse_rate_value = i13;
                                bloodPressureChartData.pulse_pressure_value = i14;
                                bloodPressureChartData.mean_arterial_pressure_value = i15;
                                bloodPressureChartData.status_color = string4.trim();
                                bloodPressureChartData.result = string5.trim();
                                bloodPressureChartData.notes = string6.trim();
                                try {
                                    try {
                                        Date parse = new SimpleDateFormat(this.date_time_format).parse(string);
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                                        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                                        bloodPressureChartData.date = simpleDateFormat.format(parse).trim();
                                        bloodPressureChartData.time = simpleDateFormat3.format(parse).trim();
                                        bloodPressureChartData.month_string = simpleDateFormat2.format(parse).trim();
                                    } catch (Exception e3) {
                                        e2 = e3;
                                    }
                                } catch (Exception e4) {
                                    e2 = e4;
                                    e2.printStackTrace();
                                    arrayList = arrayList;
                                    arrayList.add(bloodPressureChartData);
                                    if (rawQuery.moveToNext()) {
                                    }
                                }
                                arrayList = arrayList;
                                arrayList.add(bloodPressureChartData);
                                if (rawQuery.moveToNext()) {
                                    break;
                                }
                                rawQuery = rawQuery;
                            } catch (Exception e5) {
                                e = e5;
                                arrayList = arrayList;
                                e.printStackTrace();
                                return arrayList;
                            }
                        } catch (Exception e6) {
                            e = e6;
                            arrayList = arrayList;
                            e.printStackTrace();
                            return arrayList;
                        }
                    }
                }
            }
        } catch (Exception e7) {
            e = e7;
            e.printStackTrace();
            return arrayList;
        }
        return arrayList;
    }

    public List GetBloodPressureChartMonthlyData(int i, int i2, int i3) {
        Exception e;
        int i4;
        Exception e2;
        ArrayList arrayList = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM blood_pressure_data WHERE month = " + i2 + " AND year = " + i3 + " AND user_id = " + i + " ORDER BY row_id ASC;", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    while (true) {
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i8 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i9 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i10 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        int i11 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_SYSTOLIC));
                        int i12 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DIASTOLIC));
                        int i13 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow("pulse_rate"));
                        int i14 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_PULSE_PRESSURE));
                        try {
                            i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MEAN_ARTERIAL_PRESSURE));
                        } catch (Exception e3) {
                            e = e3;
                            arrayList = arrayList;
                            e.printStackTrace();
                            return arrayList;
                        }
                        try {
                            String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow("color"));
                            String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_RESULT));
                            String string6 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_NOTES));
                            BloodPressureChartData bloodPressureChartData = new BloodPressureChartData();
                            bloodPressureChartData.row_id = i5;
                            bloodPressureChartData.user_id = i6;
                            bloodPressureChartData.dateTime = string.trim();
                            bloodPressureChartData.date = string2.trim();
                            bloodPressureChartData.time = string3.trim();
                            bloodPressureChartData.day = i7;
                            bloodPressureChartData.month = i8;
                            bloodPressureChartData.year = i9;
                            bloodPressureChartData.hour = i10;
                            bloodPressureChartData.systolic_value = i11;
                            bloodPressureChartData.diastolic_value = i12;
                            bloodPressureChartData.pulse_rate_value = i13;
                            bloodPressureChartData.pulse_pressure_value = i14;
                            bloodPressureChartData.mean_arterial_pressure_value = i4;
                            bloodPressureChartData.status_color = string4.trim();
                            bloodPressureChartData.result = string5.trim();
                            bloodPressureChartData.notes = string6.trim();
                            try {
                                try {
                                    Date parse = new SimpleDateFormat(this.date_time_format).parse(string);
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                                    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                                    SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                                    bloodPressureChartData.date = simpleDateFormat.format(parse).trim();
                                    bloodPressureChartData.time = simpleDateFormat3.format(parse).trim();
                                    bloodPressureChartData.month_string = simpleDateFormat2.format(parse).trim();
                                } catch (Exception e4) {
                                    e2 = e4;
                                }
                            } catch (Exception e5) {
                                e2 = e5;
                                e2.printStackTrace();
                                arrayList = arrayList;
                                arrayList.add(bloodPressureChartData);
                                if (rawQuery.moveToNext()) {
                                }
                            }
                            arrayList = arrayList;
                            arrayList.add(bloodPressureChartData);
                            if (rawQuery.moveToNext()) {
                                break;
                            }
                            rawQuery = rawQuery;
                        } catch (Exception e6) {
                            e = e6;
                            arrayList = arrayList;
                            e.printStackTrace();
                            return arrayList;
                        }
                    }
                }
            }
        } catch (Exception e7) {
            e = e7;
            e.printStackTrace();
            return arrayList;
        }
        return arrayList;
    }

    public List GetBloodPressureChartYearlyData(int i, int i2) {
        Exception e;
        Exception e2;
        ArrayList arrayList = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM blood_pressure_data WHERE year = " + i2 + " AND user_id = " + i + " ORDER BY row_id ASC;", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    while (true) {
                        int i3 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i8 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        int i9 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_SYSTOLIC));
                        int i10 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DIASTOLIC));
                        int i11 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow("pulse_rate"));
                        int i12 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_PULSE_PRESSURE));
                        try {
                            int i13 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MEAN_ARTERIAL_PRESSURE));
                            try {
                                String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow("color"));
                                String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_RESULT));
                                String string6 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_NOTES));
                                BloodPressureChartData bloodPressureChartData = new BloodPressureChartData();
                                bloodPressureChartData.row_id = i3;
                                bloodPressureChartData.user_id = i4;
                                bloodPressureChartData.dateTime = string.trim();
                                bloodPressureChartData.date = string2.trim();
                                bloodPressureChartData.time = string3.trim();
                                bloodPressureChartData.day = i5;
                                bloodPressureChartData.month = i6;
                                bloodPressureChartData.year = i7;
                                bloodPressureChartData.hour = i8;
                                bloodPressureChartData.systolic_value = i9;
                                bloodPressureChartData.diastolic_value = i10;
                                bloodPressureChartData.pulse_rate_value = i11;
                                bloodPressureChartData.pulse_pressure_value = i12;
                                bloodPressureChartData.mean_arterial_pressure_value = i13;
                                bloodPressureChartData.status_color = string4.trim();
                                bloodPressureChartData.result = string5.trim();
                                bloodPressureChartData.notes = string6.trim();
                                try {
                                    try {
                                        Date parse = new SimpleDateFormat(this.date_time_format).parse(string);
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                                        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                                        bloodPressureChartData.date = simpleDateFormat.format(parse).trim();
                                        bloodPressureChartData.time = simpleDateFormat3.format(parse).trim();
                                        bloodPressureChartData.month_string = simpleDateFormat2.format(parse).trim();
                                    } catch (Exception e3) {
                                        e2 = e3;
                                    }
                                } catch (Exception e4) {
                                    e2 = e4;
                                    e2.printStackTrace();
                                    arrayList = arrayList;
                                    arrayList.add(bloodPressureChartData);
                                    if (rawQuery.moveToNext()) {
                                    }
                                }
                                arrayList = arrayList;
                                arrayList.add(bloodPressureChartData);
                                if (rawQuery.moveToNext()) {
                                    break;
                                }
                                rawQuery = rawQuery;
                            } catch (Exception e5) {
                                e = e5;
                                arrayList = arrayList;
                                e.printStackTrace();
                                return arrayList;
                            }
                        } catch (Exception e6) {
                            e = e6;
                            arrayList = arrayList;
                            e.printStackTrace();
                            return arrayList;
                        }
                    }
                }
            }
        } catch (Exception e7) {
            e = e7;
            e.printStackTrace();
            return arrayList;
        }
        return arrayList;
    }

    public List GetBloodPressureChartCustomData(int i, String str, String str2) {
        Exception e;
        Exception e2;
        ArrayList arrayList = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM blood_pressure_data WHERE user_id = " + i + " AND date >= " + ("'" + str + "'") + " AND date <= " + ("'" + str2 + "'") + " ORDER BY row_id ASC;", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    while (true) {
                        int i2 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i3 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        int i8 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_SYSTOLIC));
                        int i9 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DIASTOLIC));
                        int i10 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow("pulse_rate"));
                        int i11 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_PULSE_PRESSURE));
                        try {
                            int i12 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MEAN_ARTERIAL_PRESSURE));
                            try {
                                String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow("color"));
                                String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_RESULT));
                                String string6 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_NOTES));
                                BloodPressureChartData bloodPressureChartData = new BloodPressureChartData();
                                bloodPressureChartData.row_id = i2;
                                bloodPressureChartData.user_id = i3;
                                bloodPressureChartData.dateTime = string.trim();
                                bloodPressureChartData.date = string2.trim();
                                bloodPressureChartData.time = string3.trim();
                                bloodPressureChartData.day = i4;
                                bloodPressureChartData.month = i5;
                                bloodPressureChartData.year = i6;
                                bloodPressureChartData.hour = i7;
                                bloodPressureChartData.systolic_value = i8;
                                bloodPressureChartData.diastolic_value = i9;
                                bloodPressureChartData.pulse_rate_value = i10;
                                bloodPressureChartData.pulse_pressure_value = i11;
                                bloodPressureChartData.mean_arterial_pressure_value = i12;
                                bloodPressureChartData.status_color = string4.trim();
                                bloodPressureChartData.result = string5.trim();
                                bloodPressureChartData.notes = string6.trim();
                                try {
                                    try {
                                        Date parse = new SimpleDateFormat(this.date_time_format).parse(string);
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                                        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                                        bloodPressureChartData.date = simpleDateFormat.format(parse).trim();
                                        bloodPressureChartData.time = simpleDateFormat3.format(parse).trim();
                                        bloodPressureChartData.month_string = simpleDateFormat2.format(parse).trim();
                                    } catch (Exception e3) {
                                        e2 = e3;
                                    }
                                } catch (Exception e4) {
                                    e2 = e4;
                                    e2.printStackTrace();
                                    arrayList = arrayList;
                                    arrayList.add(bloodPressureChartData);
                                    if (rawQuery.moveToNext()) {
                                    }
                                }
                                arrayList = arrayList;
                                arrayList.add(bloodPressureChartData);
                                if (rawQuery.moveToNext()) {
                                    break;
                                }
                                rawQuery = rawQuery;
                            } catch (Exception e5) {
                                e = e5;
                                arrayList = arrayList;
                                e.printStackTrace();
                                return arrayList;
                            }
                        } catch (Exception e6) {
                            e = e6;
                            arrayList = arrayList;
                            e.printStackTrace();
                            return arrayList;
                        }
                    }
                }
            }
        } catch (Exception e7) {
            e = e7;
            e.printStackTrace();
            return arrayList;
        }
        return arrayList;
    }

    public long InsertBloodSugarData(BloodSugarData bloodSugarData) {
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        int i = bloodSugarData.user_id;
        String trim = bloodSugarData.dateTime.trim();
        String trim2 = bloodSugarData.date.trim();
        String trim3 = bloodSugarData.time.trim();
        int i2 = bloodSugarData.day;
        int i3 = bloodSugarData.month;
        int i4 = bloodSugarData.year;
        int i5 = bloodSugarData.hour;
        String trim4 = bloodSugarData.current_status.trim();
        float f = bloodSugarData.sugar_level;
        float f2 = bloodSugarData.keton_level;
        float f3 = bloodSugarData.hemoglobin_level;
        float f4 = bloodSugarData.blood_ADAG;
        float f5 = bloodSugarData.blood_DCCT;
        String trim5 = bloodSugarData.status_color.trim();
        String trim6 = bloodSugarData.result.trim();
        String trim7 = bloodSugarData.notes.trim();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USER_ID, Integer.valueOf(i));
        contentValues.put(KEY_DATE_TIME, trim);
        contentValues.put(KEY_DATE, trim2);
        contentValues.put(KEY_TIME, trim3);
        contentValues.put(KEY_DAY, Integer.valueOf(i2));
        contentValues.put(KEY_MONTH, Integer.valueOf(i3));
        contentValues.put(KEY_YEAR, Integer.valueOf(i4));
        contentValues.put(KEY_HOUR, Integer.valueOf(i5));
        contentValues.put(KEY_CURRENT_STATUS, trim4);
        contentValues.put(KEY_SUGAR_LEVEL, Float.valueOf(f));
        contentValues.put(KEY_KETONE_LEVEL, Float.valueOf(f2));
        contentValues.put(KEY_HEMOGLOBIN_LEVEL, Float.valueOf(f3));
        contentValues.put(KEY_ADAG, Float.valueOf(f4));
        contentValues.put(KEY_DCCT, Float.valueOf(f5));
        contentValues.put("color", trim5);
        contentValues.put(KEY_RESULT, trim6);
        contentValues.put(KEY_NOTES, trim7);
        return sqLiteDatabase.insertWithOnConflict(BLOOD_SUGAR_TABLE, null, contentValues, 4);
    }

    public long UpdateBloodSugarData(int i, int i2, BloodSugarData bloodSugarData) {
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        String trim = bloodSugarData.dateTime.trim();
        String trim2 = bloodSugarData.date.trim();
        String trim3 = bloodSugarData.time.trim();
        int i3 = bloodSugarData.day;
        int i4 = bloodSugarData.month;
        int i5 = bloodSugarData.year;
        int i6 = bloodSugarData.hour;
        String trim4 = bloodSugarData.current_status.trim();
        float f = bloodSugarData.sugar_level;
        float f2 = bloodSugarData.keton_level;
        float f3 = bloodSugarData.hemoglobin_level;
        float f4 = bloodSugarData.blood_ADAG;
        float f5 = bloodSugarData.blood_DCCT;
        String trim5 = bloodSugarData.status_color.trim();
        String trim6 = bloodSugarData.result.trim();
        String trim7 = bloodSugarData.notes.trim();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USER_ID, Integer.valueOf(i2));
        contentValues.put(KEY_DATE_TIME, trim);
        contentValues.put(KEY_DATE, trim2);
        contentValues.put(KEY_TIME, trim3);
        contentValues.put(KEY_DAY, Integer.valueOf(i3));
        contentValues.put(KEY_MONTH, Integer.valueOf(i4));
        contentValues.put(KEY_YEAR, Integer.valueOf(i5));
        contentValues.put(KEY_HOUR, Integer.valueOf(i6));
        contentValues.put(KEY_CURRENT_STATUS, trim4);
        contentValues.put(KEY_SUGAR_LEVEL, Float.valueOf(f));
        contentValues.put(KEY_KETONE_LEVEL, Float.valueOf(f2));
        contentValues.put(KEY_HEMOGLOBIN_LEVEL, Float.valueOf(f3));
        contentValues.put(KEY_ADAG, Float.valueOf(f4));
        contentValues.put(KEY_DCCT, Float.valueOf(f5));
        contentValues.put("color", trim5);
        contentValues.put(KEY_RESULT, trim6);
        contentValues.put(KEY_NOTES, trim7);
        return sqLiteDatabase.update(BLOOD_SUGAR_TABLE, contentValues, "row_id=" + i, null);
    }

    public List GetBloodSugarData() {
        Exception e;
        Exception e2;
        ArrayList arrayList = new ArrayList();
        try {
            SQLiteDatabase readableDatabase = sqLiteHelper.getReadableDatabase();
            sqLiteDatabase = readableDatabase;
            Cursor rawQuery = readableDatabase.rawQuery("SELECT * FROM blood_sugar_data ORDER BY row_id DESC", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    while (true) {
                        int i = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i2 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i3 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_CURRENT_STATUS));
                        float f = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_SUGAR_LEVEL));
                        float f2 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_KETONE_LEVEL));
                        float f3 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_HEMOGLOBIN_LEVEL));
                        try {
                            float f4 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_ADAG));
                            try {
                                float f5 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_DCCT));
                                String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_RESULT));
                                String string6 = rawQuery.getString(rawQuery.getColumnIndexOrThrow("color"));
                                String string7 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_NOTES));
                                BloodSugarData bloodSugarData = new BloodSugarData();
                                bloodSugarData.row_id = i;
                                bloodSugarData.user_id = i2;
                                bloodSugarData.dateTime = string.trim();
                                bloodSugarData.date = string2.trim();
                                bloodSugarData.time = string3.trim();
                                bloodSugarData.day = i3;
                                bloodSugarData.month = i4;
                                bloodSugarData.year = i5;
                                bloodSugarData.hour = i6;
                                bloodSugarData.current_status = string4.trim();
                                bloodSugarData.sugar_level = f;
                                bloodSugarData.keton_level = f2;
                                bloodSugarData.hemoglobin_level = f3;
                                bloodSugarData.blood_ADAG = f4;
                                bloodSugarData.blood_DCCT = f5;
                                bloodSugarData.result = string5.trim();
                                bloodSugarData.status_color = string6.trim();
                                bloodSugarData.notes = string7.trim();
                                try {
                                    try {
                                        Date parse = new SimpleDateFormat(this.date_time_format).parse(string);
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                                        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                                        bloodSugarData.date = simpleDateFormat.format(parse).trim();
                                        bloodSugarData.time = simpleDateFormat3.format(parse).trim();
                                        bloodSugarData.month_string = simpleDateFormat2.format(parse).trim();
                                    } catch (Exception e3) {
                                        e2 = e3;
                                    }
                                } catch (Exception e4) {
                                    e2 = e4;
                                    e2.printStackTrace();
                                    arrayList = arrayList;
                                    arrayList.add(bloodSugarData);
                                    if (rawQuery.moveToNext()) {
                                    }
                                }
                                arrayList = arrayList;
                                arrayList.add(bloodSugarData);
                                if (rawQuery.moveToNext()) {
                                    break;
                                }
                                rawQuery = rawQuery;
                            } catch (Exception e5) {
                                e = e5;
                                arrayList = arrayList;
                                e.printStackTrace();
                                return arrayList;
                            }
                        } catch (Exception e6) {
                            e = e6;
                            arrayList = arrayList;
                            e.printStackTrace();
                            return arrayList;
                        }
                    }
                }
            }
        } catch (Exception e7) {
            e = e7;
            e.printStackTrace();
            return arrayList;
        }
        return arrayList;
    }

    public List GetBloodSugarDataByUserID(int i) {
        ArrayList arrayList;
        Exception e;
        BloodSugarData bloodSugarData;
        Exception e2;
        String str = KEY_ROW_ID;
        ArrayList arrayList2 = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM blood_sugar_data WHERE user_id=" + i + " ORDER BY " + str + " DESC", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                    return arrayList2;
                } else if (rawQuery.moveToFirst()) {
                    while (true) {
                        int i2 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(str));
                        int i3 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_CURRENT_STATUS));
                        float f = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_SUGAR_LEVEL));
                        float f2 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_KETONE_LEVEL));
                        float f3 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_HEMOGLOBIN_LEVEL));
                        try {
                            float f4 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_ADAG));
                            try {
                                float f5 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_DCCT));
                                String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_RESULT));
                                String string6 = rawQuery.getString(rawQuery.getColumnIndexOrThrow("color"));
                                String string7 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_NOTES));
                                bloodSugarData = new BloodSugarData();
                                bloodSugarData.row_id = i2;
                                bloodSugarData.user_id = i3;
                                bloodSugarData.dateTime = string.trim();
                                bloodSugarData.date = string2.trim();
                                bloodSugarData.time = string3.trim();
                                bloodSugarData.day = i4;
                                bloodSugarData.month = i5;
                                bloodSugarData.year = i6;
                                bloodSugarData.hour = i7;
                                bloodSugarData.current_status = string4.trim();
                                bloodSugarData.sugar_level = f;
                                bloodSugarData.keton_level = f2;
                                bloodSugarData.hemoglobin_level = f3;
                                bloodSugarData.blood_ADAG = f4;
                                bloodSugarData.blood_DCCT = f5;
                                bloodSugarData.result = string5.trim();
                                bloodSugarData.status_color = string6.trim();
                                bloodSugarData.notes = string7.trim();
                                try {
                                    try {
                                        Date parse = new SimpleDateFormat(this.date_time_format).parse(string);
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                                        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                                        bloodSugarData.date = simpleDateFormat.format(parse).trim();
                                        bloodSugarData.time = simpleDateFormat3.format(parse).trim();
                                        bloodSugarData.month_string = simpleDateFormat2.format(parse).trim();
                                    } catch (Exception e3) {
                                        e2 = e3;
                                    }
                                } catch (Exception e4) {
                                    e2 = e4;
                                    e2.printStackTrace();
                                    arrayList = arrayList2;
                                    arrayList.add(bloodSugarData);
                                    if (rawQuery.moveToNext()) {
                                    }
                                }
                                arrayList = arrayList2;
                            } catch (Exception e5) {
                                e = e5;
                                arrayList = arrayList2;
                                e.printStackTrace();
                                return arrayList;
                            }
                            try {
                                arrayList.add(bloodSugarData);
                                if (!rawQuery.moveToNext()) {
                                    return arrayList;
                                }
                                arrayList2 = arrayList;
                                str = str;
                                rawQuery = rawQuery;
                            } catch (Exception e6) {
                                e = e6;
                                e.printStackTrace();
                                return arrayList;
                            }
                        } catch (Exception e7) {
                            e = e7;
                            arrayList = arrayList2;
                            e.printStackTrace();
                            return arrayList;
                        }
                    }
                }
            }
            return arrayList2;
        } catch (Exception e8) {
            e = e8;
            arrayList = arrayList2;
            e.printStackTrace();
            return arrayList;
        }
    }

    public int GetMaxBloodSugarValue(int i) {
        Exception e;
        int i2 = 0;
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT MAX(sugar_level) FROM blood_sugar_data WHERE user_id=" + i, null);
            if (rawQuery == null || rawQuery.getCount() <= 0 || !rawQuery.moveToFirst()) {
                return 0;
            }
            int i3 = 0;
            do {
                try {
                    i3 = rawQuery.getInt(0);
                } catch (Exception e2) {
                    e = e2;
                    i2 = i3;
                    e.printStackTrace();
                    return i2;
                }
            } while (rawQuery.moveToNext());
            return i3;
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            return i2;
        }
    }

    public void deleteBloodSugarByID(int i) {
        SQLiteDatabase sQLiteDatabase = sqLiteDatabase;
        sQLiteDatabase.delete(BLOOD_SUGAR_TABLE, "row_id=" + i, null);
    }

    public void deleteBloodSugarByUserID(int i) {
        SQLiteDatabase sQLiteDatabase = sqLiteDatabase;
        sQLiteDatabase.delete(BLOOD_SUGAR_TABLE, "user_id=" + i, null);
    }

    public void deleteAllBloodSugarData() {
        sqLiteDatabase.execSQL("DELETE FROM blood_sugar_data");
    }

    public List GetBloodSugarChartAllData(int i) {
        ArrayList arrayList;
        Exception e;
        BloodSugarChartData bloodSugarChartData;
        Exception e2;
        String str = KEY_ROW_ID;
        ArrayList arrayList2 = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM blood_sugar_data WHERE user_id=" + i + " ORDER BY " + str + " DESC", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                    return arrayList2;
                } else if (rawQuery.moveToFirst()) {
                    while (true) {
                        int i2 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(str));
                        int i3 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_CURRENT_STATUS));
                        float f = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_SUGAR_LEVEL));
                        float f2 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_KETONE_LEVEL));
                        float f3 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_HEMOGLOBIN_LEVEL));
                        try {
                            float f4 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_ADAG));
                            try {
                                float f5 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_DCCT));
                                String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_RESULT));
                                String string6 = rawQuery.getString(rawQuery.getColumnIndexOrThrow("color"));
                                String string7 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_NOTES));
                                bloodSugarChartData = new BloodSugarChartData();
                                bloodSugarChartData.row_id = i2;
                                bloodSugarChartData.user_id = i3;
                                bloodSugarChartData.dateTime = string.trim();
                                bloodSugarChartData.date = string2.trim();
                                bloodSugarChartData.time = string3.trim();
                                bloodSugarChartData.day = i4;
                                bloodSugarChartData.month = i5;
                                bloodSugarChartData.year = i6;
                                bloodSugarChartData.hour = i7;
                                bloodSugarChartData.current_status = string4.trim();
                                bloodSugarChartData.sugar_level = f;
                                bloodSugarChartData.keton_level = f2;
                                bloodSugarChartData.hemoglobin_level = f3;
                                bloodSugarChartData.blood_ADAG = f4;
                                bloodSugarChartData.blood_DCCT = f5;
                                bloodSugarChartData.result = string5.trim();
                                bloodSugarChartData.status_color = string6.trim();
                                bloodSugarChartData.notes = string7.trim();
                                try {
                                    try {
                                        Date parse = new SimpleDateFormat(this.date_time_format).parse(string);
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                                        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                                        bloodSugarChartData.date = simpleDateFormat.format(parse).trim();
                                        bloodSugarChartData.time = simpleDateFormat3.format(parse).trim();
                                        bloodSugarChartData.month_string = simpleDateFormat2.format(parse).trim();
                                    } catch (Exception e3) {
                                        e2 = e3;
                                    }
                                } catch (Exception e4) {
                                    e2 = e4;
                                    e2.printStackTrace();
                                    arrayList = arrayList2;
                                    arrayList.add(bloodSugarChartData);
                                    if (rawQuery.moveToNext()) {
                                    }
                                }
                                arrayList = arrayList2;
                            } catch (Exception e5) {
                                e = e5;
                                arrayList = arrayList2;
                                e.printStackTrace();
                                return arrayList;
                            }
                            try {
                                arrayList.add(bloodSugarChartData);
                                if (rawQuery.moveToNext()) {
                                    return arrayList;
                                }
                                arrayList2 = arrayList;
                                str = str;
                                rawQuery = rawQuery;
                            } catch (Exception e6) {
                                e = e6;
                                e.printStackTrace();
                                return arrayList;
                            }
                        } catch (Exception e7) {
                            e = e7;
                            arrayList = arrayList2;
                            e.printStackTrace();
                            return arrayList;
                        }
                    }
                }
            }
            return arrayList2;
        } catch (Exception e8) {
            e = e8;
            arrayList = arrayList2;
            e.printStackTrace();
            return arrayList;
        }
    }

    public List GetBloodSugarChartTodayData(int i, int i2, int i3, int i4) {
        Exception e;
        Exception e2;
        ArrayList arrayList = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM blood_sugar_data WHERE day = " + i2 + " AND month = " + i3 + " AND year = " + i4 + " AND user_id = " + i + " ORDER BY row_id ASC;", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    while (true) {
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i8 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i9 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i10 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_CURRENT_STATUS));
                        float f = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_SUGAR_LEVEL));
                        float f2 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_KETONE_LEVEL));
                        float f3 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_HEMOGLOBIN_LEVEL));
                        try {
                            float f4 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_ADAG));
                            try {
                                float f5 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_DCCT));
                                String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_RESULT));
                                String string6 = rawQuery.getString(rawQuery.getColumnIndexOrThrow("color"));
                                String string7 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_NOTES));
                                BloodSugarChartData bloodSugarChartData = new BloodSugarChartData();
                                bloodSugarChartData.row_id = i5;
                                bloodSugarChartData.user_id = i6;
                                bloodSugarChartData.dateTime = string.trim();
                                bloodSugarChartData.date = string2.trim();
                                bloodSugarChartData.time = string3.trim();
                                bloodSugarChartData.day = i7;
                                bloodSugarChartData.month = i8;
                                bloodSugarChartData.year = i9;
                                bloodSugarChartData.hour = i10;
                                bloodSugarChartData.current_status = string4.trim();
                                bloodSugarChartData.sugar_level = f;
                                bloodSugarChartData.keton_level = f2;
                                bloodSugarChartData.hemoglobin_level = f3;
                                bloodSugarChartData.blood_ADAG = f4;
                                bloodSugarChartData.blood_DCCT = f5;
                                bloodSugarChartData.result = string5.trim();
                                bloodSugarChartData.status_color = string6.trim();
                                bloodSugarChartData.notes = string7.trim();
                                try {
                                    try {
                                        Date parse = new SimpleDateFormat(this.date_time_format).parse(string);
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                                        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                                        bloodSugarChartData.date = simpleDateFormat.format(parse).trim();
                                        bloodSugarChartData.time = simpleDateFormat3.format(parse).trim();
                                        bloodSugarChartData.month_string = simpleDateFormat2.format(parse).trim();
                                    } catch (Exception e3) {
                                        e2 = e3;
                                    }
                                } catch (Exception e4) {
                                    e2 = e4;
                                    e2.printStackTrace();
                                    arrayList = arrayList;
                                    arrayList.add(bloodSugarChartData);
                                    if (rawQuery.moveToNext()) {
                                    }
                                }
                                arrayList = arrayList;
                                arrayList.add(bloodSugarChartData);
                                if (rawQuery.moveToNext()) {
                                    break;
                                }
                                rawQuery = rawQuery;
                            } catch (Exception e5) {
                                e = e5;
                                arrayList = arrayList;
                                e.printStackTrace();
                                return arrayList;
                            }
                        } catch (Exception e6) {
                            e = e6;
                            arrayList = arrayList;
                            e.printStackTrace();
                            return arrayList;
                        }
                    }
                }
            }
        } catch (Exception e7) {
            e = e7;
            e.printStackTrace();
            return arrayList;
        }
        return arrayList;
    }

    public List GetBloodSugarChartMonthlyData(int i, int i2, int i3) {
        Exception e;
        float f;
        Exception e2;
        ArrayList arrayList = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM blood_sugar_data WHERE month = " + i2 + " AND year = " + i3 + " AND user_id = " + i + " ORDER BY row_id ASC;", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    while (true) {
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i8 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i9 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_CURRENT_STATUS));
                        float f2 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_SUGAR_LEVEL));
                        float f3 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_KETONE_LEVEL));
                        float f4 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_HEMOGLOBIN_LEVEL));
                        try {
                            f = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_ADAG));
                        } catch (Exception e3) {
                            e = e3;
                            arrayList = arrayList;
                            e.printStackTrace();
                            return arrayList;
                        }
                        try {
                            float f5 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_DCCT));
                            String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_RESULT));
                            String string6 = rawQuery.getString(rawQuery.getColumnIndexOrThrow("color"));
                            String string7 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_NOTES));
                            BloodSugarChartData bloodSugarChartData = new BloodSugarChartData();
                            bloodSugarChartData.row_id = i4;
                            bloodSugarChartData.user_id = i5;
                            bloodSugarChartData.dateTime = string.trim();
                            bloodSugarChartData.date = string2.trim();
                            bloodSugarChartData.time = string3.trim();
                            bloodSugarChartData.day = i6;
                            bloodSugarChartData.month = i7;
                            bloodSugarChartData.year = i8;
                            bloodSugarChartData.hour = i9;
                            bloodSugarChartData.current_status = string4.trim();
                            bloodSugarChartData.sugar_level = f2;
                            bloodSugarChartData.keton_level = f3;
                            bloodSugarChartData.hemoglobin_level = f4;
                            bloodSugarChartData.blood_ADAG = f;
                            bloodSugarChartData.blood_DCCT = f5;
                            bloodSugarChartData.result = string5.trim();
                            bloodSugarChartData.status_color = string6.trim();
                            bloodSugarChartData.notes = string7.trim();
                            try {
                                try {
                                    Date parse = new SimpleDateFormat(this.date_time_format).parse(string);
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                                    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                                    SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                                    bloodSugarChartData.date = simpleDateFormat.format(parse).trim();
                                    bloodSugarChartData.time = simpleDateFormat3.format(parse).trim();
                                    bloodSugarChartData.month_string = simpleDateFormat2.format(parse).trim();
                                } catch (Exception e4) {
                                    e2 = e4;
                                }
                            } catch (Exception e5) {
                                e2 = e5;
                                e2.printStackTrace();
                                arrayList = arrayList;
                                arrayList.add(bloodSugarChartData);
                                if (rawQuery.moveToNext()) {
                                }
                            }
                            arrayList = arrayList;
                            arrayList.add(bloodSugarChartData);
                            if (rawQuery.moveToNext()) {
                                break;
                            }
                            rawQuery = rawQuery;
                        } catch (Exception e6) {
                            e = e6;
                            arrayList = arrayList;
                            e.printStackTrace();
                            return arrayList;
                        }
                    }
                }
            }
        } catch (Exception e7) {
            e = e7;
            e.printStackTrace();
            return arrayList;
        }
        return arrayList;
    }

    public List GetBloodSugarChartYearlyData(int i, int i2) {
        Exception e;
        Exception e2;
        ArrayList arrayList = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM blood_sugar_data WHERE year = " + i2 + " AND user_id = " + i + " ORDER BY row_id ASC;", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    while (true) {
                        int i3 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i8 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_CURRENT_STATUS));
                        float f = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_SUGAR_LEVEL));
                        float f2 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_KETONE_LEVEL));
                        float f3 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_HEMOGLOBIN_LEVEL));
                        try {
                            float f4 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_ADAG));
                            try {
                                float f5 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_DCCT));
                                String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_RESULT));
                                String string6 = rawQuery.getString(rawQuery.getColumnIndexOrThrow("color"));
                                String string7 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_NOTES));
                                BloodSugarChartData bloodSugarChartData = new BloodSugarChartData();
                                bloodSugarChartData.row_id = i3;
                                bloodSugarChartData.user_id = i4;
                                bloodSugarChartData.dateTime = string.trim();
                                bloodSugarChartData.date = string2.trim();
                                bloodSugarChartData.time = string3.trim();
                                bloodSugarChartData.day = i5;
                                bloodSugarChartData.month = i6;
                                bloodSugarChartData.year = i7;
                                bloodSugarChartData.hour = i8;
                                bloodSugarChartData.current_status = string4.trim();
                                bloodSugarChartData.sugar_level = f;
                                bloodSugarChartData.keton_level = f2;
                                bloodSugarChartData.hemoglobin_level = f3;
                                bloodSugarChartData.blood_ADAG = f4;
                                bloodSugarChartData.blood_DCCT = f5;
                                bloodSugarChartData.result = string5.trim();
                                bloodSugarChartData.status_color = string6.trim();
                                bloodSugarChartData.notes = string7.trim();
                                try {
                                    try {
                                        Date parse = new SimpleDateFormat(this.date_time_format).parse(string);
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                                        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                                        bloodSugarChartData.date = simpleDateFormat.format(parse).trim();
                                        bloodSugarChartData.time = simpleDateFormat3.format(parse).trim();
                                        bloodSugarChartData.month_string = simpleDateFormat2.format(parse).trim();
                                    } catch (Exception e3) {
                                        e2 = e3;
                                    }
                                } catch (Exception e4) {
                                    e2 = e4;
                                    e2.printStackTrace();
                                    arrayList = arrayList;
                                    arrayList.add(bloodSugarChartData);
                                    if (rawQuery.moveToNext()) {
                                    }
                                }
                                arrayList = arrayList;
                                arrayList.add(bloodSugarChartData);
                                if (rawQuery.moveToNext()) {
                                    break;
                                }
                                rawQuery = rawQuery;
                            } catch (Exception e5) {
                                e = e5;
                                arrayList = arrayList;
                                e.printStackTrace();
                                return arrayList;
                            }
                        } catch (Exception e6) {
                            e = e6;
                            arrayList = arrayList;
                            e.printStackTrace();
                            return arrayList;
                        }
                    }
                }
            }
        } catch (Exception e7) {
            e = e7;
            e.printStackTrace();
            return arrayList;
        }
        return arrayList;
    }

    public List GetBloodSugarChartCustomData(int i, String str, String str2) {
        Exception e;
        Exception e2;
        ArrayList arrayList = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM blood_sugar_data WHERE user_id = " + i + " AND date >= " + ("'" + str + "'") + " AND date <= " + ("'" + str2 + "'") + " ORDER BY row_id ASC;", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    while (true) {
                        int i2 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i3 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_CURRENT_STATUS));
                        float f = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_SUGAR_LEVEL));
                        float f2 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_KETONE_LEVEL));
                        float f3 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_HEMOGLOBIN_LEVEL));
                        try {
                            float f4 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_ADAG));
                            try {
                                float f5 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_DCCT));
                                String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_RESULT));
                                String string6 = rawQuery.getString(rawQuery.getColumnIndexOrThrow("color"));
                                String string7 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_NOTES));
                                BloodSugarChartData bloodSugarChartData = new BloodSugarChartData();
                                bloodSugarChartData.row_id = i2;
                                bloodSugarChartData.user_id = i3;
                                bloodSugarChartData.dateTime = string.trim();
                                bloodSugarChartData.date = string2.trim();
                                bloodSugarChartData.time = string3.trim();
                                bloodSugarChartData.day = i4;
                                bloodSugarChartData.month = i5;
                                bloodSugarChartData.year = i6;
                                bloodSugarChartData.hour = i7;
                                bloodSugarChartData.current_status = string4.trim();
                                bloodSugarChartData.sugar_level = f;
                                bloodSugarChartData.keton_level = f2;
                                bloodSugarChartData.hemoglobin_level = f3;
                                bloodSugarChartData.blood_ADAG = f4;
                                bloodSugarChartData.blood_DCCT = f5;
                                bloodSugarChartData.result = string5.trim();
                                bloodSugarChartData.status_color = string6.trim();
                                bloodSugarChartData.notes = string7.trim();
                                try {
                                    try {
                                        Date parse = new SimpleDateFormat(this.date_time_format).parse(string);
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                                        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                                        bloodSugarChartData.date = simpleDateFormat.format(parse).trim();
                                        bloodSugarChartData.time = simpleDateFormat3.format(parse).trim();
                                        bloodSugarChartData.month_string = simpleDateFormat2.format(parse).trim();
                                    } catch (Exception e3) {
                                        e2 = e3;
                                    }
                                } catch (Exception e4) {
                                    e2 = e4;
                                    e2.printStackTrace();
                                    arrayList = arrayList;
                                    arrayList.add(bloodSugarChartData);
                                    if (rawQuery.moveToNext()) {
                                    }
                                }
                                arrayList = arrayList;
                                arrayList.add(bloodSugarChartData);
                                if (rawQuery.moveToNext()) {
                                    break;
                                }
                                rawQuery = rawQuery;
                            } catch (Exception e5) {
                                e = e5;
                                arrayList = arrayList;
                                e.printStackTrace();
                                return arrayList;
                            }
                        } catch (Exception e6) {
                            e = e6;
                            arrayList = arrayList;
                            e.printStackTrace();
                            return arrayList;
                        }
                    }
                }
            }
        } catch (Exception e7) {
            e = e7;
            e.printStackTrace();
            return arrayList;
        }
        return arrayList;
    }

    public long InsertBMIData(BMIData bMIData) {
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        int i = bMIData.user_id;
        String trim = bMIData.dateTime.trim();
        String trim2 = bMIData.date.trim();
        String trim3 = bMIData.time.trim();
        int i2 = bMIData.day;
        int i3 = bMIData.month;
        int i4 = bMIData.year;
        int i5 = bMIData.hour;
        String trim4 = bMIData.weight.trim();
        String trim5 = bMIData.weight_unit.trim();
        String trim6 = bMIData.height.trim();
        String trim7 = bMIData.height_unit.trim();
        String trim8 = bMIData.age.trim();
        String trim9 = bMIData.bmi.trim();
        String trim10 = bMIData.birth_date.trim();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USER_ID, Integer.valueOf(i));
        contentValues.put(KEY_DATE_TIME, trim);
        contentValues.put(KEY_DATE, trim2);
        contentValues.put(KEY_TIME, trim3);
        contentValues.put(KEY_DAY, Integer.valueOf(i2));
        contentValues.put(KEY_MONTH, Integer.valueOf(i3));
        contentValues.put(KEY_YEAR, Integer.valueOf(i4));
        contentValues.put(KEY_HOUR, Integer.valueOf(i5));
        contentValues.put(KEY_WEIGHT, trim4);
        contentValues.put(KEY_WEIGHT_UNIT, trim5);
        contentValues.put(KEY_HEIGHT, trim6);
        contentValues.put(KEY_HEIGHT_UNIT, trim7);
        contentValues.put(KEY_AGE, trim8);
        contentValues.put(KEY_BMI, trim9);
        contentValues.put(KEY_BIRTH_DATE, trim10);
        return sqLiteDatabase.insertWithOnConflict(BMI_TABLE, null, contentValues, 4);
    }

    public long UpdateBMIData(int i, int i2, BMIData bMIData) {
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        String trim = bMIData.dateTime.trim();
        String trim2 = bMIData.date.trim();
        String trim3 = bMIData.time.trim();
        int i3 = bMIData.day;
        int i4 = bMIData.month;
        int i5 = bMIData.year;
        int i6 = bMIData.hour;
        String trim4 = bMIData.weight.trim();
        String trim5 = bMIData.weight_unit.trim();
        String trim6 = bMIData.height.trim();
        String trim7 = bMIData.height_unit.trim();
        String trim8 = bMIData.age.trim();
        String trim9 = bMIData.bmi.trim();
        String trim10 = bMIData.birth_date.trim();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USER_ID, Integer.valueOf(i2));
        contentValues.put(KEY_DATE_TIME, trim);
        contentValues.put(KEY_DATE, trim2);
        contentValues.put(KEY_TIME, trim3);
        contentValues.put(KEY_DAY, Integer.valueOf(i3));
        contentValues.put(KEY_MONTH, Integer.valueOf(i4));
        contentValues.put(KEY_YEAR, Integer.valueOf(i5));
        contentValues.put(KEY_HOUR, Integer.valueOf(i6));
        contentValues.put(KEY_WEIGHT, trim4);
        contentValues.put(KEY_WEIGHT_UNIT, trim5);
        contentValues.put(KEY_HEIGHT, trim6);
        contentValues.put(KEY_HEIGHT_UNIT, trim7);
        contentValues.put(KEY_AGE, trim8);
        contentValues.put(KEY_BMI, trim9);
        contentValues.put(KEY_BIRTH_DATE, trim10);
        return sqLiteDatabase.update(BMI_TABLE, contentValues, "row_id=" + i, null);
    }

    public List GetBMIData() {
        Exception e;
        Exception e2;
        ArrayList arrayList = new ArrayList();
        try {
            SQLiteDatabase readableDatabase = sqLiteHelper.getReadableDatabase();
            sqLiteDatabase = readableDatabase;
            Cursor rawQuery = readableDatabase.rawQuery("SELECT * FROM bmi_data ORDER BY row_id DESC", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    while (true) {
                        int i = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i2 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i3 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_WEIGHT));
                        String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_WEIGHT_UNIT));
                        String string6 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_HEIGHT));
                        String string7 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_HEIGHT_UNIT));
                        try {
                            String string8 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_AGE));
                            try {
                                String string9 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_BMI));
                                String string10 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_BIRTH_DATE));
                                BMIData bMIData = new BMIData();
                                bMIData.row_id = i;
                                bMIData.user_id = i2;
                                bMIData.dateTime = string.trim();
                                bMIData.date = string2.trim();
                                bMIData.time = string3.trim();
                                bMIData.day = i3;
                                bMIData.month = i4;
                                bMIData.year = i5;
                                bMIData.hour = i6;
                                bMIData.weight = string4.trim();
                                bMIData.weight_unit = string5.trim();
                                bMIData.height = string6.trim();
                                bMIData.height_unit = string7.trim();
                                bMIData.age = string8.trim();
                                bMIData.bmi = string9.trim();
                                bMIData.birth_date = string10.trim();
                                try {
                                    try {
                                        Date parse = new SimpleDateFormat(this.date_time_format).parse(string);
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                                        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                                        bMIData.date = simpleDateFormat.format(parse).trim();
                                        bMIData.time = simpleDateFormat3.format(parse).trim();
                                        bMIData.month_string = simpleDateFormat2.format(parse).trim();
                                    } catch (Exception e3) {
                                        e2 = e3;
                                    }
                                } catch (Exception e4) {
                                    e2 = e4;
                                    e2.printStackTrace();
                                    arrayList = arrayList;
                                    arrayList.add(bMIData);
                                    if (rawQuery.moveToNext()) {
                                    }
                                }
                                arrayList = arrayList;
                                arrayList.add(bMIData);
                                if (rawQuery.moveToNext()) {
                                    break;
                                }
                                rawQuery = rawQuery;
                            } catch (Exception e5) {
                                e = e5;
                                arrayList = arrayList;
                                e.printStackTrace();
                                return arrayList;
                            }
                        } catch (Exception e6) {
                            e = e6;
                            arrayList = arrayList;
                            e.printStackTrace();
                            return arrayList;
                        }
                    }
                }
            }
        } catch (Exception e7) {
            e = e7;
            e.printStackTrace();
            return arrayList;
        }
        return arrayList;
    }

    public List GetBMIDataByUserID(int i) {
        ArrayList arrayList;
        Exception e;
        BMIData bMIData;
        Exception e2;
        String str = KEY_ROW_ID;
        ArrayList arrayList2 = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM bmi_data WHERE user_id=" + i + " ORDER BY " + str + " DESC", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                    return arrayList2;
                } else if (rawQuery.moveToFirst()) {
                    while (true) {
                        int i2 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(str));
                        int i3 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_WEIGHT));
                        String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_WEIGHT_UNIT));
                        String string6 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_HEIGHT));
                        String string7 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_HEIGHT_UNIT));
                        try {
                            String string8 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_AGE));
                            try {
                                String string9 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_BMI));
                                String string10 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_BIRTH_DATE));
                                bMIData = new BMIData();
                                bMIData.row_id = i2;
                                bMIData.user_id = i3;
                                bMIData.dateTime = string.trim();
                                bMIData.date = string2.trim();
                                bMIData.time = string3.trim();
                                bMIData.day = i4;
                                bMIData.month = i5;
                                bMIData.year = i6;
                                bMIData.hour = i7;
                                bMIData.weight = string4.trim();
                                bMIData.weight_unit = string5.trim();
                                bMIData.height = string6.trim();
                                bMIData.height_unit = string7.trim();
                                bMIData.age = string8.trim();
                                bMIData.bmi = string9.trim();
                                bMIData.birth_date = string10.trim();
                                try {
                                    try {
                                        Date parse = new SimpleDateFormat(this.date_time_format).parse(string);
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                                        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                                        bMIData.date = simpleDateFormat.format(parse).trim();
                                        bMIData.time = simpleDateFormat3.format(parse).trim();
                                        bMIData.month_string = simpleDateFormat2.format(parse).trim();
                                    } catch (Exception e3) {
                                        e2 = e3;
                                    }
                                } catch (Exception e4) {
                                    e2 = e4;
                                    e2.printStackTrace();
                                    arrayList = arrayList2;
                                    arrayList.add(bMIData);
                                    if (rawQuery.moveToNext()) {
                                    }
                                }
                                arrayList = arrayList2;
                            } catch (Exception e5) {
                                e = e5;
                                arrayList = arrayList2;
                                e.printStackTrace();
                                return arrayList;
                            }
                            try {
                                arrayList.add(bMIData);
                                if (!rawQuery.moveToNext()) {
                                    return arrayList;
                                }
                                arrayList2 = arrayList;
                                str = str;
                                rawQuery = rawQuery;
                            } catch (Exception e6) {
                                e = e6;
                                e.printStackTrace();
                                return arrayList;
                            }
                        } catch (Exception e7) {
                            e = e7;
                            arrayList = arrayList2;
                            e.printStackTrace();
                            return arrayList;
                        }
                    }
                }
            }
            return arrayList2;
        } catch (Exception e8) {
            e = e8;
            arrayList = arrayList2;
            e.printStackTrace();
            return arrayList;
        }
    }

    public int GetMaxBMIValue(int i) {
        Exception e;
        int i2 = 0;
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT MAX(bmi) FROM bmi_data WHERE user_id=" + i, null);
            if (rawQuery == null || rawQuery.getCount() <= 0 || !rawQuery.moveToFirst()) {
                return 0;
            }
            int i3 = 0;
            do {
                try {
                    i3 = rawQuery.getInt(0);
                } catch (Exception e2) {
                    e = e2;
                    i2 = i3;
                    e.printStackTrace();
                    return i2;
                }
            } while (rawQuery.moveToNext());
            return i3;
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            return i2;
        }
    }

    public int GetMaximumBMI() {
        SQLiteDatabase readableDatabase = sqLiteHelper.getReadableDatabase();
        sqLiteDatabase = readableDatabase;
        Cursor rawQuery = readableDatabase.rawQuery("SELECT MAX(bmi) as max_bmi FROM bmi_data", null);
        int i = rawQuery.moveToFirst() ? rawQuery.getInt(rawQuery.getColumnIndexOrThrow("max_bmi")) : 0;
        rawQuery.close();
        return i + 20;
    }

    public void deleteBMIByID(int i) {
        SQLiteDatabase sQLiteDatabase = sqLiteDatabase;
        sQLiteDatabase.delete(BMI_TABLE, "row_id=" + i, null);
    }

    public void deleteBMIByUserID(int i) {
        SQLiteDatabase sQLiteDatabase = sqLiteDatabase;
        sQLiteDatabase.delete(BMI_TABLE, "user_id=" + i, null);
    }

    public void deleteAllBMIData() {
        sqLiteDatabase.execSQL("DELETE FROM bmi_data");
    }

    public List GetBMIChartAllData(int i) {
        ArrayList arrayList;
        Exception e;
        BMIChartData bMIChartData;
        Exception e2;
        String str = KEY_ROW_ID;
        ArrayList arrayList2 = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM bmi_data WHERE user_id=" + i + " ORDER BY " + str + " DESC", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                    return arrayList2;
                } else if (rawQuery.moveToFirst()) {
                    while (true) {
                        int i2 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(str));
                        int i3 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_WEIGHT));
                        String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_WEIGHT_UNIT));
                        String string6 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_HEIGHT));
                        String string7 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_HEIGHT_UNIT));
                        try {
                            String string8 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_AGE));
                            try {
                                String string9 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_BMI));
                                String string10 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_BIRTH_DATE));
                                bMIChartData = new BMIChartData();
                                bMIChartData.row_id = i2;
                                bMIChartData.user_id = i3;
                                bMIChartData.dateTime = string.trim();
                                bMIChartData.date = string2.trim();
                                bMIChartData.time = string3.trim();
                                bMIChartData.day = i4;
                                bMIChartData.month = i5;
                                bMIChartData.year = i6;
                                bMIChartData.hour = i7;
                                bMIChartData.weight = string4.trim();
                                bMIChartData.weight_unit = string5.trim();
                                bMIChartData.height = string6.trim();
                                bMIChartData.height_unit = string7.trim();
                                bMIChartData.age = string8.trim();
                                bMIChartData.bmi = string9.trim();
                                bMIChartData.birth_date = string10.trim();
                                try {
                                    try {
                                        Date parse = new SimpleDateFormat(this.date_time_format).parse(string);
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                                        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                                        bMIChartData.date = simpleDateFormat.format(parse).trim();
                                        bMIChartData.time = simpleDateFormat3.format(parse).trim();
                                        bMIChartData.month_string = simpleDateFormat2.format(parse).trim();
                                    } catch (Exception e3) {
                                        e2 = e3;
                                    }
                                } catch (Exception e4) {
                                    e2 = e4;
                                    e2.printStackTrace();
                                    arrayList = arrayList2;
                                    arrayList.add(bMIChartData);
                                    if (rawQuery.moveToNext()) {
                                    }
                                }
                                arrayList = arrayList2;
                            } catch (Exception e5) {
                                e = e5;
                                arrayList = arrayList2;
                                e.printStackTrace();
                                return arrayList;
                            }
                            try {
                                arrayList.add(bMIChartData);
                                if (rawQuery.moveToNext()) {
                                    return arrayList;
                                }
                                arrayList2 = arrayList;
                                str = str;
                                rawQuery = rawQuery;
                            } catch (Exception e6) {
                                e = e6;
                                e.printStackTrace();
                                return arrayList;
                            }
                        } catch (Exception e7) {
                            e = e7;
                            arrayList = arrayList2;
                            e.printStackTrace();
                            return arrayList;
                        }
                    }
                }
            }
            return arrayList2;
        } catch (Exception e8) {
            e = e8;
            arrayList = arrayList2;
            e.printStackTrace();
            return arrayList;
        }
    }

    public List GetBMIChartTodayData(int i, int i2, int i3, int i4) {
        Exception e;
        Exception e2;
        ArrayList arrayList = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM bmi_data WHERE day = " + i2 + " AND month = " + i3 + " AND year = " + i4 + " AND user_id = " + i + " ORDER BY row_id ASC;", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    while (true) {
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i8 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i9 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i10 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_WEIGHT));
                        String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_WEIGHT_UNIT));
                        String string6 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_HEIGHT));
                        String string7 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_HEIGHT_UNIT));
                        try {
                            String string8 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_AGE));
                            try {
                                String string9 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_BMI));
                                String string10 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_BIRTH_DATE));
                                BMIChartData bMIChartData = new BMIChartData();
                                bMIChartData.row_id = i5;
                                bMIChartData.user_id = i6;
                                bMIChartData.dateTime = string.trim();
                                bMIChartData.date = string2.trim();
                                bMIChartData.time = string3.trim();
                                bMIChartData.day = i7;
                                bMIChartData.month = i8;
                                bMIChartData.year = i9;
                                bMIChartData.hour = i10;
                                bMIChartData.weight = string4.trim();
                                bMIChartData.weight_unit = string5.trim();
                                bMIChartData.height = string6.trim();
                                bMIChartData.height_unit = string7.trim();
                                bMIChartData.age = string8.trim();
                                bMIChartData.bmi = string9.trim();
                                bMIChartData.birth_date = string10.trim();
                                try {
                                    try {
                                        Date parse = new SimpleDateFormat(this.date_time_format).parse(string);
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                                        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                                        bMIChartData.date = simpleDateFormat.format(parse).trim();
                                        bMIChartData.time = simpleDateFormat3.format(parse).trim();
                                        bMIChartData.month_string = simpleDateFormat2.format(parse).trim();
                                    } catch (Exception e3) {
                                        e2 = e3;
                                    }
                                } catch (Exception e4) {
                                    e2 = e4;
                                    e2.printStackTrace();
                                    arrayList = arrayList;
                                    arrayList.add(bMIChartData);
                                    if (rawQuery.moveToNext()) {
                                    }
                                }
                                arrayList = arrayList;
                                arrayList.add(bMIChartData);
                                if (rawQuery.moveToNext()) {
                                    break;
                                }
                                rawQuery = rawQuery;
                            } catch (Exception e5) {
                                e = e5;
                                arrayList = arrayList;
                                e.printStackTrace();
                                return arrayList;
                            }
                        } catch (Exception e6) {
                            e = e6;
                            arrayList = arrayList;
                            e.printStackTrace();
                            return arrayList;
                        }
                    }
                }
            }
        } catch (Exception e7) {
            e = e7;
            e.printStackTrace();
            return arrayList;
        }
        return arrayList;
    }

    public List GetBMIChartMonthlyData(int i, int i2, int i3) {
        Exception e;
        String string;
        Exception e2;
        ArrayList arrayList = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM bmi_data WHERE month = " + i2 + " AND year = " + i3 + " AND user_id = " + i + " ORDER BY row_id ASC;", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    while (true) {
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i8 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i9 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_WEIGHT));
                        String string6 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_WEIGHT_UNIT));
                        String string7 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_HEIGHT));
                        String string8 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_HEIGHT_UNIT));
                        try {
                            string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_AGE));
                        } catch (Exception e3) {
                            e = e3;
                            arrayList = arrayList;
                            e.printStackTrace();
                            return arrayList;
                        }
                        try {
                            String string9 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_BMI));
                            String string10 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_BIRTH_DATE));
                            BMIChartData bMIChartData = new BMIChartData();
                            bMIChartData.row_id = i4;
                            bMIChartData.user_id = i5;
                            bMIChartData.dateTime = string2.trim();
                            bMIChartData.date = string3.trim();
                            bMIChartData.time = string4.trim();
                            bMIChartData.day = i6;
                            bMIChartData.month = i7;
                            bMIChartData.year = i8;
                            bMIChartData.hour = i9;
                            bMIChartData.weight = string5.trim();
                            bMIChartData.weight_unit = string6.trim();
                            bMIChartData.height = string7.trim();
                            bMIChartData.height_unit = string8.trim();
                            bMIChartData.age = string.trim();
                            bMIChartData.bmi = string9.trim();
                            bMIChartData.birth_date = string10.trim();
                            try {
                                try {
                                    Date parse = new SimpleDateFormat(this.date_time_format).parse(string2);
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                                    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                                    SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                                    bMIChartData.date = simpleDateFormat.format(parse).trim();
                                    bMIChartData.time = simpleDateFormat3.format(parse).trim();
                                    bMIChartData.month_string = simpleDateFormat2.format(parse).trim();
                                } catch (Exception e4) {
                                    e2 = e4;
                                }
                            } catch (Exception e5) {
                                e2 = e5;
                                e2.printStackTrace();
                                arrayList = arrayList;
                                arrayList.add(bMIChartData);
                                if (rawQuery.moveToNext()) {
                                }
                            }
                            arrayList = arrayList;
                            arrayList.add(bMIChartData);
                            if (rawQuery.moveToNext()) {
                                break;
                            }
                            rawQuery = rawQuery;
                        } catch (Exception e6) {
                            e = e6;
                            arrayList = arrayList;
                            e.printStackTrace();
                            return arrayList;
                        }
                    }
                }
            }
        } catch (Exception e7) {
            e = e7;
            e.printStackTrace();
            return arrayList;
        }
        return arrayList;
    }

    public List GetBMIChartYearlyData(int i, int i2) {
        Exception e;
        Exception e2;
        ArrayList arrayList = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM bmi_data WHERE year = " + i2 + " AND user_id = " + i + " ORDER BY row_id ASC;", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    while (true) {
                        int i3 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i8 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_WEIGHT));
                        String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_WEIGHT_UNIT));
                        String string6 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_HEIGHT));
                        String string7 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_HEIGHT_UNIT));
                        try {
                            String string8 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_AGE));
                            try {
                                String string9 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_BMI));
                                String string10 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_BIRTH_DATE));
                                BMIChartData bMIChartData = new BMIChartData();
                                bMIChartData.row_id = i3;
                                bMIChartData.user_id = i4;
                                bMIChartData.dateTime = string.trim();
                                bMIChartData.date = string2.trim();
                                bMIChartData.time = string3.trim();
                                bMIChartData.day = i5;
                                bMIChartData.month = i6;
                                bMIChartData.year = i7;
                                bMIChartData.hour = i8;
                                bMIChartData.weight = string4.trim();
                                bMIChartData.weight_unit = string5.trim();
                                bMIChartData.height = string6.trim();
                                bMIChartData.height_unit = string7.trim();
                                bMIChartData.age = string8.trim();
                                bMIChartData.bmi = string9.trim();
                                bMIChartData.birth_date = string10.trim();
                                try {
                                    try {
                                        Date parse = new SimpleDateFormat(this.date_time_format).parse(string);
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                                        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                                        bMIChartData.date = simpleDateFormat.format(parse).trim();
                                        bMIChartData.time = simpleDateFormat3.format(parse).trim();
                                        bMIChartData.month_string = simpleDateFormat2.format(parse).trim();
                                    } catch (Exception e3) {
                                        e2 = e3;
                                    }
                                } catch (Exception e4) {
                                    e2 = e4;
                                    e2.printStackTrace();
                                    arrayList = arrayList;
                                    arrayList.add(bMIChartData);
                                    if (rawQuery.moveToNext()) {
                                    }
                                }
                                arrayList = arrayList;
                                arrayList.add(bMIChartData);
                                if (rawQuery.moveToNext()) {
                                    break;
                                }
                                rawQuery = rawQuery;
                            } catch (Exception e5) {
                                e = e5;
                                arrayList = arrayList;
                                e.printStackTrace();
                                return arrayList;
                            }
                        } catch (Exception e6) {
                            e = e6;
                            arrayList = arrayList;
                            e.printStackTrace();
                            return arrayList;
                        }
                    }
                }
            }
        } catch (Exception e7) {
            e = e7;
            e.printStackTrace();
            return arrayList;
        }
        return arrayList;
    }

    public List GetBMIChartCustomData(int i, String str, String str2) {
        Exception e;
        Exception e2;
        ArrayList arrayList = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM bmi_data WHERE user_id = " + i + " AND date >= " + ("'" + str + "'") + " AND date <= " + ("'" + str2 + "'") + " ORDER BY row_id ASC;", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    while (true) {
                        int i2 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i3 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_WEIGHT));
                        String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_WEIGHT_UNIT));
                        String string6 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_HEIGHT));
                        String string7 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_HEIGHT_UNIT));
                        try {
                            String string8 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_AGE));
                            try {
                                String string9 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_BMI));
                                String string10 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_BIRTH_DATE));
                                BMIChartData bMIChartData = new BMIChartData();
                                bMIChartData.row_id = i2;
                                bMIChartData.user_id = i3;
                                bMIChartData.dateTime = string.trim();
                                bMIChartData.date = string2.trim();
                                bMIChartData.time = string3.trim();
                                bMIChartData.day = i4;
                                bMIChartData.month = i5;
                                bMIChartData.year = i6;
                                bMIChartData.hour = i7;
                                bMIChartData.weight = string4.trim();
                                bMIChartData.weight_unit = string5.trim();
                                bMIChartData.height = string6.trim();
                                bMIChartData.height_unit = string7.trim();
                                bMIChartData.age = string8.trim();
                                bMIChartData.bmi = string9.trim();
                                bMIChartData.birth_date = string10.trim();
                                try {
                                    try {
                                        Date parse = new SimpleDateFormat(this.date_time_format).parse(string);
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                                        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                                        bMIChartData.date = simpleDateFormat.format(parse).trim();
                                        bMIChartData.time = simpleDateFormat3.format(parse).trim();
                                        bMIChartData.month_string = simpleDateFormat2.format(parse).trim();
                                    } catch (Exception e3) {
                                        e2 = e3;
                                    }
                                } catch (Exception e4) {
                                    e2 = e4;
                                    e2.printStackTrace();
                                    arrayList = arrayList;
                                    arrayList.add(bMIChartData);
                                    if (rawQuery.moveToNext()) {
                                    }
                                }
                                arrayList = arrayList;
                                arrayList.add(bMIChartData);
                                if (rawQuery.moveToNext()) {
                                    break;
                                }
                                rawQuery = rawQuery;
                            } catch (Exception e5) {
                                e = e5;
                                arrayList = arrayList;
                                e.printStackTrace();
                                return arrayList;
                            }
                        } catch (Exception e6) {
                            e = e6;
                            arrayList = arrayList;
                            e.printStackTrace();
                            return arrayList;
                        }
                    }
                }
            }
        } catch (Exception e7) {
            e = e7;
            e.printStackTrace();
            return arrayList;
        }
        return arrayList;
    }

    public long InsertTemperatureData(BodyTempData bodyTempData) {
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        int i = bodyTempData.user_id;
        float f = bodyTempData.celsius_value;
        float f2 = bodyTempData.fahrenheit_value;
        String roundToOneDigit = AppConstants.roundToOneDigit(f);
        String roundToOneDigit2 = AppConstants.roundToOneDigit(f2);
        String valueOf = String.valueOf((int) bodyTempData.pulse_value);
        String trim = bodyTempData.dateTime.trim();
        String trim2 = bodyTempData.date.trim();
        String trim3 = bodyTempData.time.trim();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USER_ID, Integer.valueOf(i));
        contentValues.put("name", bodyTempData.name.trim());
        contentValues.put(KEY_DATE_TIME, trim);
        contentValues.put(KEY_DATE, trim2);
        contentValues.put(KEY_TIME, trim3);
        contentValues.put(KEY_CELSIUS, roundToOneDigit);
        contentValues.put(KEY_FAHRENHEIT, roundToOneDigit2);
        contentValues.put(KEY_PULSE, valueOf);
        contentValues.put(KEY_DAY, Integer.valueOf(bodyTempData.day));
        contentValues.put(KEY_MONTH, Integer.valueOf(bodyTempData.month));
        contentValues.put(KEY_YEAR, Integer.valueOf(bodyTempData.year));
        contentValues.put(KEY_HOUR, Integer.valueOf(bodyTempData.hour));
        contentValues.put(KEY_TAGS, bodyTempData.tags.trim());
        return sqLiteDatabase.insertWithOnConflict(BODY_TEMP_DATA_TABLE, null, contentValues, 4);
    }

    public long UpdateTemperatureData(int i, int i2, BodyTempData bodyTempData) {
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        float f = bodyTempData.celsius_value;
        float f2 = bodyTempData.fahrenheit_value;
        String roundToOneDigit = AppConstants.roundToOneDigit(f);
        String roundToOneDigit2 = AppConstants.roundToOneDigit(f2);
        String valueOf = String.valueOf((int) bodyTempData.pulse_value);
        String trim = bodyTempData.dateTime.trim();
        String trim2 = bodyTempData.date.trim();
        String trim3 = bodyTempData.time.trim();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USER_ID, Integer.valueOf(i2));
        contentValues.put("name", bodyTempData.name.trim());
        contentValues.put(KEY_DATE_TIME, trim);
        contentValues.put(KEY_DATE, trim2);
        contentValues.put(KEY_TIME, trim3);
        contentValues.put(KEY_CELSIUS, roundToOneDigit);
        contentValues.put(KEY_FAHRENHEIT, roundToOneDigit2);
        contentValues.put(KEY_PULSE, valueOf);
        contentValues.put(KEY_DAY, Integer.valueOf(bodyTempData.day));
        contentValues.put(KEY_MONTH, Integer.valueOf(bodyTempData.month));
        contentValues.put(KEY_YEAR, Integer.valueOf(bodyTempData.year));
        contentValues.put(KEY_HOUR, Integer.valueOf(bodyTempData.hour));
        contentValues.put(KEY_TAGS, bodyTempData.tags.trim());
        return sqLiteDatabase.update(BODY_TEMP_DATA_TABLE, contentValues, "row_id=" + i, null);
    }

    public List GetTemperatureData() {
        ArrayList arrayList = new ArrayList();
        try {
            SQLiteDatabase readableDatabase = sqLiteHelper.getReadableDatabase();
            sqLiteDatabase = readableDatabase;
            Cursor rawQuery = readableDatabase.rawQuery("SELECT * FROM body_temp_data ORDER BY row_id DESC", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    do {
                        int i = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i2 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow("name"));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TAGS));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_CELSIUS));
                        String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_FAHRENHEIT));
                        String string6 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_PULSE));
                        int i3 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        BodyTempData bodyTempData = new BodyTempData();
                        bodyTempData.row_id = i;
                        bodyTempData.user_id = i2;
                        bodyTempData.name = string.trim();
                        bodyTempData.tags = string2.trim();
                        bodyTempData.dateTime = string3.trim();
                        bodyTempData.celsius = string4.trim();
                        bodyTempData.fahrenheit = string5.trim();
                        bodyTempData.pulse = string6.trim();
                        bodyTempData.celsius_value = Float.parseFloat(string4.trim());
                        bodyTempData.fahrenheit_value = Float.parseFloat(string5.trim());
                        bodyTempData.pulse_value = Float.parseFloat(string6.trim());
                        bodyTempData.day = i3;
                        bodyTempData.month = i4;
                        bodyTempData.year = i5;
                        bodyTempData.hour = i6;
                        bodyTempData.fever_type = GetFeverStatus(Float.parseFloat(string4));
                        try {
                            Date parse = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a").parse(string3);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("MMMM");
                            SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("hh:mm a");
                            bodyTempData.date = simpleDateFormat.format(parse).trim();
                            bodyTempData.time = simpleDateFormat3.format(parse).trim();
                            bodyTempData.month_string = simpleDateFormat2.format(parse).trim();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        arrayList.add(bodyTempData);
                    } while (rawQuery.moveToNext());
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return arrayList;
    }

    public List GetTemperatureDataByUserID(int i) {
        Exception e;
        ArrayList arrayList = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM body_temp_data WHERE user_id=" + i + " ORDER BY " + KEY_ROW_ID + " DESC", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    do {
                        int i2 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i3 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow("name"));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TAGS));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_CELSIUS));
                        String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_FAHRENHEIT));
                        String string6 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_PULSE));
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        BodyTempData bodyTempData = new BodyTempData();
                        bodyTempData.row_id = i2;
                        bodyTempData.user_id = i3;
                        bodyTempData.name = string.trim();
                        bodyTempData.tags = string2.trim();
                        bodyTempData.dateTime = string3.trim();
                        bodyTempData.celsius = string4.trim();
                        bodyTempData.fahrenheit = string5.trim();
                        bodyTempData.pulse = string6.trim();
                        bodyTempData.celsius_value = Float.parseFloat(string4.trim());
                        bodyTempData.fahrenheit_value = Float.parseFloat(string5.trim());
                        bodyTempData.pulse_value = Float.parseFloat(string6.trim());
                        bodyTempData.day = i4;
                        bodyTempData.month = i5;
                        bodyTempData.year = i6;
                        bodyTempData.hour = i7;
                        try {
                            bodyTempData.fever_type = GetFeverStatus(Float.parseFloat(string4));
                            try {
                                Date parse = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a").parse(string3);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("MMMM");
                                SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("hh:mm a");
                                bodyTempData.date = simpleDateFormat.format(parse).trim();
                                bodyTempData.time = simpleDateFormat3.format(parse).trim();
                                bodyTempData.month_string = simpleDateFormat2.format(parse).trim();
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                            arrayList.add(bodyTempData);
                        } catch (Exception e3) {
                            e = e3;
                            e.printStackTrace();
                            return arrayList;
                        }
                    } while (rawQuery.moveToNext());
                }
                return arrayList;
            }
        } catch (Exception e4) {
            e = e4;
            e.printStackTrace();
            return arrayList;
        }
        return arrayList;
    }

    public int GetMaxBodyTempValue(int i) {
        int i2;
        int i3;
        int i4;
        int[] iArr = new int[3];
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT MAX(celsius) FROM body_temp_data WHERE user_id=" + i, null);
            if (rawQuery == null || rawQuery.getCount() <= 0 || !rawQuery.moveToFirst()) {
                i2 = 0;
            } else {
                do {
                    i2 = rawQuery.getInt(0);
                } while (rawQuery.moveToNext());
            }
            Cursor rawQuery2 = sqLiteDatabase.rawQuery("SELECT MAX(fahrenheit) FROM body_temp_data WHERE user_id=" + i, null);
            if (rawQuery2 == null || rawQuery2.getCount() <= 0 || !rawQuery2.moveToFirst()) {
                i3 = 0;
            } else {
                do {
                    i3 = rawQuery2.getInt(0);
                } while (rawQuery2.moveToNext());
            }
            Cursor rawQuery3 = sqLiteDatabase.rawQuery("SELECT MAX(pulse) FROM body_temp_data WHERE user_id=" + i, null);
            if (rawQuery3 == null || rawQuery3.getCount() <= 0 || !rawQuery3.moveToFirst()) {
                i4 = 0;
            } else {
                do {
                    i4 = rawQuery3.getInt(0);
                } while (rawQuery3.moveToNext());
            }
            iArr[0] = i2;
            iArr[1] = i3;
            iArr[2] = i4;
            ArrayList arrayList = new ArrayList();
            for (int i5 = 0; i5 < 3; i5++) {
                arrayList.add(Integer.valueOf(iArr[i5]));
            }
            int intValue = ((Integer) Collections.max(arrayList)).intValue();
            Log.e("Body Temp :", "Graph Max Y Axis :- " + intValue);
            return intValue;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void deleteDataByID(int i) {
        SQLiteDatabase sQLiteDatabase = sqLiteDatabase;
        sQLiteDatabase.delete(BODY_TEMP_DATA_TABLE, "row_id=" + i, null);
    }

    public void deleteBodyTempByUserID(int i) {
        SQLiteDatabase sQLiteDatabase = sqLiteDatabase;
        sQLiteDatabase.delete(BODY_TEMP_DATA_TABLE, "user_id=" + i, null);
    }

    public void deleteDataByName(String str) {
        SQLiteDatabase sQLiteDatabase = sqLiteDatabase;
        sQLiteDatabase.delete(BODY_TEMP_DATA_TABLE, "name=" + ("'" + str + "'"), null);
    }

    public void deleteAllData() {
        sqLiteDatabase.execSQL("DELETE FROM body_temp_data");
    }

    private String GetFeverStatus(float f) {
        if (f >= 36.5f && ((double) f) <= 37.5d) {
            return AppConstants.NORMAL;
        }
        if (f > 37.5f && ((double) f) <= 38.3d) {
            return AppConstants.FEVER;
        }
        if (f > 38.3f) {
            return AppConstants.HIGH_FEVER;
        }
        return f < 35.0f ? AppConstants.HYPOTHERMIA : "";
    }

    public List GetBodyTempChartAllData(int i) {
        Exception e;
        ArrayList arrayList = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM body_temp_data WHERE user_id=" + i + " ORDER BY " + KEY_ROW_ID + " DESC", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    do {
                        int i2 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i3 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow("name"));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TAGS));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_CELSIUS));
                        String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_FAHRENHEIT));
                        String string6 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_PULSE));
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        BodyTempChartAllData bodyTempChartAllData = new BodyTempChartAllData();
                        bodyTempChartAllData.row_id = i2;
                        bodyTempChartAllData.user_id = i3;
                        bodyTempChartAllData.name = string.trim();
                        bodyTempChartAllData.tags = string2.trim();
                        bodyTempChartAllData.dateTime = string3.trim();
                        bodyTempChartAllData.celsius = string4.trim();
                        bodyTempChartAllData.fahrenheit = string5.trim();
                        bodyTempChartAllData.pulse = string6.trim();
                        bodyTempChartAllData.celsius_value = Float.parseFloat(string4.trim());
                        bodyTempChartAllData.fahrenheit_value = Float.parseFloat(string5.trim());
                        bodyTempChartAllData.pulse_value = Float.parseFloat(string6.trim());
                        bodyTempChartAllData.day = i4;
                        bodyTempChartAllData.month = i5;
                        bodyTempChartAllData.year = i6;
                        bodyTempChartAllData.hour = i7;
                        try {
                            bodyTempChartAllData.fever_type = GetFeverStatus(Float.parseFloat(string4));
                            try {
                                Date parse = new SimpleDateFormat("dd/MM/yyyy hh:mm a").parse(string3);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("hh:mm a");
                                bodyTempChartAllData.date = simpleDateFormat.format(parse).trim();
                                bodyTempChartAllData.time = simpleDateFormat2.format(parse).trim();
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                            arrayList.add(bodyTempChartAllData);
                        } catch (Exception e3) {
                            e = e3;
                            e.printStackTrace();
                            return arrayList;
                        }
                    } while (rawQuery.moveToNext());
                }
                return arrayList;
            }
        } catch (Exception e4) {
            e = e4;
            e.printStackTrace();
            return arrayList;
        }
        return arrayList;
    }

    public List GetBodyTempChartTodayData(int i, int i2, int i3, int i4) {
        ArrayList arrayList = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM body_temp_data WHERE day = " + i2 + " AND month = " + i3 + " AND year = " + i4 + " AND user_id = " + i + " ORDER BY row_id ASC;", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    do {
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow("name"));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TAGS));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_CELSIUS));
                        String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_FAHRENHEIT));
                        String string6 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_PULSE));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i8 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i9 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i10 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        BodyTempChartAllData bodyTempChartAllData = new BodyTempChartAllData();
                        bodyTempChartAllData.row_id = i5;
                        bodyTempChartAllData.user_id = i6;
                        bodyTempChartAllData.name = string.trim();
                        bodyTempChartAllData.tags = string2.trim();
                        bodyTempChartAllData.dateTime = string3.trim();
                        bodyTempChartAllData.celsius = string4.trim();
                        bodyTempChartAllData.fahrenheit = string5.trim();
                        bodyTempChartAllData.pulse = string6.trim();
                        bodyTempChartAllData.celsius_value = Float.parseFloat(string4.trim());
                        bodyTempChartAllData.fahrenheit_value = Float.parseFloat(string5.trim());
                        bodyTempChartAllData.pulse_value = Float.parseFloat(string6.trim());
                        bodyTempChartAllData.day = i7;
                        bodyTempChartAllData.month = i8;
                        bodyTempChartAllData.year = i9;
                        bodyTempChartAllData.hour = i10;
                        bodyTempChartAllData.fever_type = GetFeverStatus(Float.parseFloat(string4));
                        try {
                            Date parse = new SimpleDateFormat("dd/MM/yyyy hh:mm a").parse(string3);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("hh:mm a");
                            bodyTempChartAllData.date = simpleDateFormat.format(parse).trim();
                            bodyTempChartAllData.time = simpleDateFormat2.format(parse).trim();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        arrayList.add(bodyTempChartAllData);
                    } while (rawQuery.moveToNext());
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return arrayList;
    }

    public List GetBodyTempChartMonthlyData(int i, int i2, int i3) {
        ArrayList arrayList = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM body_temp_data WHERE month = " + i2 + " AND year = " + i3 + " AND user_id = " + i + " ORDER BY row_id ASC;", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    do {
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow("name"));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TAGS));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_CELSIUS));
                        String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_FAHRENHEIT));
                        String string6 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_PULSE));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i8 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i9 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        BodyTempChartAllData bodyTempChartAllData = new BodyTempChartAllData();
                        bodyTempChartAllData.row_id = i4;
                        bodyTempChartAllData.user_id = i5;
                        bodyTempChartAllData.name = string.trim();
                        bodyTempChartAllData.tags = string2.trim();
                        bodyTempChartAllData.dateTime = string3.trim();
                        bodyTempChartAllData.celsius = string4.trim();
                        bodyTempChartAllData.fahrenheit = string5.trim();
                        bodyTempChartAllData.pulse = string6.trim();
                        bodyTempChartAllData.celsius_value = Float.parseFloat(string4.trim());
                        bodyTempChartAllData.fahrenheit_value = Float.parseFloat(string5.trim());
                        bodyTempChartAllData.pulse_value = Float.parseFloat(string6.trim());
                        bodyTempChartAllData.day = i6;
                        bodyTempChartAllData.month = i7;
                        bodyTempChartAllData.year = i8;
                        bodyTempChartAllData.hour = i9;
                        bodyTempChartAllData.fever_type = GetFeverStatus(Float.parseFloat(string4));
                        try {
                            Date parse = new SimpleDateFormat("dd/MM/yyyy hh:mm a").parse(string3);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("hh:mm a");
                            bodyTempChartAllData.date = simpleDateFormat.format(parse).trim();
                            bodyTempChartAllData.time = simpleDateFormat2.format(parse).trim();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        arrayList.add(bodyTempChartAllData);
                    } while (rawQuery.moveToNext());
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return arrayList;
    }

    public List GetBodyTempChartYearlyData(int i, int i2) {
        ArrayList arrayList = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM body_temp_data WHERE year = " + i2 + " AND user_id = " + i + " ORDER BY row_id ASC;", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    do {
                        int i3 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow("name"));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TAGS));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_CELSIUS));
                        String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_FAHRENHEIT));
                        String string6 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_PULSE));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i8 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        BodyTempChartAllData bodyTempChartAllData = new BodyTempChartAllData();
                        bodyTempChartAllData.row_id = i3;
                        bodyTempChartAllData.user_id = i4;
                        bodyTempChartAllData.name = string.trim();
                        bodyTempChartAllData.tags = string2.trim();
                        bodyTempChartAllData.dateTime = string3.trim();
                        bodyTempChartAllData.celsius = string4.trim();
                        bodyTempChartAllData.fahrenheit = string5.trim();
                        bodyTempChartAllData.pulse = string6.trim();
                        bodyTempChartAllData.celsius_value = Float.parseFloat(string4.trim());
                        bodyTempChartAllData.fahrenheit_value = Float.parseFloat(string5.trim());
                        bodyTempChartAllData.pulse_value = Float.parseFloat(string6.trim());
                        bodyTempChartAllData.day = i5;
                        bodyTempChartAllData.month = i6;
                        bodyTempChartAllData.year = i7;
                        bodyTempChartAllData.hour = i8;
                        bodyTempChartAllData.fever_type = GetFeverStatus(Float.parseFloat(string4));
                        try {
                            Date parse = new SimpleDateFormat("dd/MM/yyyy hh:mm a").parse(string3);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("hh:mm a");
                            bodyTempChartAllData.date = simpleDateFormat.format(parse).trim();
                            bodyTempChartAllData.time = simpleDateFormat2.format(parse).trim();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        arrayList.add(bodyTempChartAllData);
                    } while (rawQuery.moveToNext());
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return arrayList;
    }

    public List GetBodyTempChartCustomData(int i, String str, String str2) {
        ArrayList arrayList = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM body_temp_data WHERE user_id = " + i + " AND date >= " + ("'" + str + "'") + " AND date <= " + ("'" + str2 + "'") + " ORDER BY row_id ASC;", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    do {
                        int i2 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i3 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow("name"));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TAGS));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_CELSIUS));
                        String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_FAHRENHEIT));
                        String string6 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_PULSE));
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        BodyTempChartAllData bodyTempChartAllData = new BodyTempChartAllData();
                        bodyTempChartAllData.row_id = i2;
                        bodyTempChartAllData.user_id = i3;
                        bodyTempChartAllData.name = string.trim();
                        bodyTempChartAllData.tags = string2.trim();
                        bodyTempChartAllData.dateTime = string3.trim();
                        bodyTempChartAllData.celsius = string4.trim();
                        bodyTempChartAllData.fahrenheit = string5.trim();
                        bodyTempChartAllData.pulse = string6.trim();
                        bodyTempChartAllData.celsius_value = Float.parseFloat(string4.trim());
                        bodyTempChartAllData.fahrenheit_value = Float.parseFloat(string5.trim());
                        bodyTempChartAllData.pulse_value = Float.parseFloat(string6.trim());
                        bodyTempChartAllData.day = i4;
                        bodyTempChartAllData.month = i5;
                        bodyTempChartAllData.year = i6;
                        bodyTempChartAllData.hour = i7;
                        bodyTempChartAllData.fever_type = GetFeverStatus(Float.parseFloat(string4));
                        try {
                            Date parse = new SimpleDateFormat("dd/MM/yyyy hh:mm a").parse(string3);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("hh:mm a");
                            bodyTempChartAllData.date = simpleDateFormat.format(parse).trim();
                            bodyTempChartAllData.time = simpleDateFormat2.format(parse).trim();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        arrayList.add(bodyTempChartAllData);
                    } while (rawQuery.moveToNext());
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return arrayList;
    }

    public long InsertCholesterolData(CholesterolData cholesterolData) {
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        int i = cholesterolData.user_id;
        String trim = cholesterolData.dateTime.trim();
        String trim2 = cholesterolData.date.trim();
        String trim3 = cholesterolData.time.trim();
        int i2 = cholesterolData.day;
        int i3 = cholesterolData.month;
        int i4 = cholesterolData.year;
        int i5 = cholesterolData.hour;
        float f = cholesterolData.cholesterol_value;
        float f2 = cholesterolData.hdl_value;
        float f3 = cholesterolData.ldl_value;
        float f4 = cholesterolData.triglyceride_value;
        String trim4 = cholesterolData.result.trim();
        String trim5 = cholesterolData.notes.trim();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USER_ID, Integer.valueOf(i));
        contentValues.put(KEY_DATE_TIME, trim);
        contentValues.put(KEY_DATE, trim2);
        contentValues.put(KEY_TIME, trim3);
        contentValues.put(KEY_DAY, Integer.valueOf(i2));
        contentValues.put(KEY_MONTH, Integer.valueOf(i3));
        contentValues.put(KEY_YEAR, Integer.valueOf(i4));
        contentValues.put(KEY_HOUR, Integer.valueOf(i5));
        contentValues.put(KEY_CHOLESTEROL, Float.valueOf(f));
        contentValues.put(KEY_HDL, Float.valueOf(f2));
        contentValues.put(KEY_LDL, Float.valueOf(f3));
        contentValues.put(KEY_TRIGLYCERIDE, Float.valueOf(f4));
        contentValues.put(KEY_RESULT, trim4);
        contentValues.put(KEY_NOTES, trim5);
        return sqLiteDatabase.insertWithOnConflict(CHOLESTEROL_TABLE, null, contentValues, 4);
    }

    public long UpdateCholesterolData(int i, int i2, CholesterolData cholesterolData) {
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        String trim = cholesterolData.dateTime.trim();
        String trim2 = cholesterolData.date.trim();
        String trim3 = cholesterolData.time.trim();
        int i3 = cholesterolData.day;
        int i4 = cholesterolData.month;
        int i5 = cholesterolData.year;
        int i6 = cholesterolData.hour;
        float f = cholesterolData.cholesterol_value;
        float f2 = cholesterolData.hdl_value;
        float f3 = cholesterolData.ldl_value;
        float f4 = cholesterolData.triglyceride_value;
        String trim4 = cholesterolData.result.trim();
        String trim5 = cholesterolData.notes.trim();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USER_ID, Integer.valueOf(i2));
        contentValues.put(KEY_DATE_TIME, trim);
        contentValues.put(KEY_DATE, trim2);
        contentValues.put(KEY_TIME, trim3);
        contentValues.put(KEY_DAY, Integer.valueOf(i3));
        contentValues.put(KEY_MONTH, Integer.valueOf(i4));
        contentValues.put(KEY_YEAR, Integer.valueOf(i5));
        contentValues.put(KEY_HOUR, Integer.valueOf(i6));
        contentValues.put(KEY_CHOLESTEROL, Float.valueOf(f));
        contentValues.put(KEY_HDL, Float.valueOf(f2));
        contentValues.put(KEY_LDL, Float.valueOf(f3));
        contentValues.put(KEY_TRIGLYCERIDE, Float.valueOf(f4));
        contentValues.put(KEY_RESULT, trim4);
        contentValues.put(KEY_NOTES, trim5);
        return sqLiteDatabase.update(CHOLESTEROL_TABLE, contentValues, "row_id=" + i, null);
    }

    public List GetCholesterolData() {
        Exception e;
        Exception e2;
        ArrayList arrayList = new ArrayList();
        try {
            SQLiteDatabase readableDatabase = sqLiteHelper.getReadableDatabase();
            sqLiteDatabase = readableDatabase;
            Cursor rawQuery = readableDatabase.rawQuery("SELECT * FROM cholesterol_data ORDER BY row_id DESC", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    while (true) {
                        int i = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i2 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i3 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        float f = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_CHOLESTEROL));
                        float f2 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_HDL));
                        float f3 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_LDL));
                        float f4 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_TRIGLYCERIDE));
                        try {
                            String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_RESULT));
                            try {
                                String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_NOTES));
                                CholesterolData cholesterolData = new CholesterolData();
                                cholesterolData.row_id = i;
                                cholesterolData.user_id = i2;
                                cholesterolData.dateTime = string.trim();
                                cholesterolData.date = string2.trim();
                                cholesterolData.time = string3.trim();
                                cholesterolData.day = i3;
                                cholesterolData.month = i4;
                                cholesterolData.year = i5;
                                cholesterolData.hour = i6;
                                cholesterolData.cholesterol_value = f;
                                cholesterolData.hdl_value = f2;
                                cholesterolData.ldl_value = f3;
                                cholesterolData.triglyceride_value = f4;
                                cholesterolData.result = string4.trim();
                                cholesterolData.notes = string5.trim();
                                try {
                                    try {
                                        Date parse = new SimpleDateFormat(this.date_time_format).parse(string);
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                                        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                                        cholesterolData.date = simpleDateFormat.format(parse).trim();
                                        cholesterolData.time = simpleDateFormat3.format(parse).trim();
                                        cholesterolData.month_string = simpleDateFormat2.format(parse).trim();
                                    } catch (Exception e3) {
                                        e2 = e3;
                                    }
                                } catch (Exception e4) {
                                    e2 = e4;
                                    e2.printStackTrace();
                                    arrayList = arrayList;
                                    arrayList.add(cholesterolData);
                                    if (rawQuery.moveToNext()) {
                                    }
                                }
                                arrayList = arrayList;
                                arrayList.add(cholesterolData);
                                if (rawQuery.moveToNext()) {
                                    break;
                                }
                                rawQuery = rawQuery;
                            } catch (Exception e5) {
                                e = e5;
                                arrayList = arrayList;
                                e.printStackTrace();
                                return arrayList;
                            }
                        } catch (Exception e6) {
                            e = e6;
                            arrayList = arrayList;
                            e.printStackTrace();
                            return arrayList;
                        }
                    }
                }
            }
        } catch (Exception e7) {
            e = e7;
            e.printStackTrace();
            return arrayList;
        }
        return arrayList;
    }

    public List GetCholesterolDataByUserID(int i) {
        ArrayList arrayList;
        Exception e;
        CholesterolData cholesterolData;
        Exception e2;
        String str = KEY_ROW_ID;
        ArrayList arrayList2 = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM cholesterol_data WHERE user_id=" + i + " ORDER BY " + str + " DESC", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                    return arrayList2;
                } else if (rawQuery.moveToFirst()) {
                    while (true) {
                        int i2 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(str));
                        int i3 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        float f = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_CHOLESTEROL));
                        float f2 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_HDL));
                        float f3 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_LDL));
                        float f4 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_TRIGLYCERIDE));
                        try {
                            String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_RESULT));
                            try {
                                String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_NOTES));
                                cholesterolData = new CholesterolData();
                                cholesterolData.row_id = i2;
                                cholesterolData.user_id = i3;
                                cholesterolData.dateTime = string.trim();
                                cholesterolData.date = string2.trim();
                                cholesterolData.time = string3.trim();
                                cholesterolData.day = i4;
                                cholesterolData.month = i5;
                                cholesterolData.year = i6;
                                cholesterolData.hour = i7;
                                cholesterolData.cholesterol_value = f;
                                cholesterolData.hdl_value = f2;
                                cholesterolData.ldl_value = f3;
                                cholesterolData.triglyceride_value = f4;
                                cholesterolData.result = string4.trim();
                                cholesterolData.notes = string5.trim();
                                try {
                                    try {
                                        Date parse = new SimpleDateFormat(this.date_time_format).parse(string);
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                                        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                                        cholesterolData.date = simpleDateFormat.format(parse).trim();
                                        cholesterolData.time = simpleDateFormat3.format(parse).trim();
                                        cholesterolData.month_string = simpleDateFormat2.format(parse).trim();
                                    } catch (Exception e3) {
                                        e2 = e3;
                                    }
                                } catch (Exception e4) {
                                    e2 = e4;
                                    e2.printStackTrace();
                                    arrayList = arrayList2;
                                    arrayList.add(cholesterolData);
                                    if (rawQuery.moveToNext()) {
                                    }
                                }
                                arrayList = arrayList2;
                            } catch (Exception e5) {
                                e = e5;
                                arrayList = arrayList2;
                                e.printStackTrace();
                                return arrayList;
                            }
                        } catch (Exception e6) {
                            e = e6;
                            arrayList = arrayList2;
                            e.printStackTrace();
                            return arrayList;
                        }
                        try {
                            arrayList.add(cholesterolData);
                            if (!rawQuery.moveToNext()) {
                                return arrayList;
                            }
                            rawQuery = rawQuery;
                            arrayList2 = arrayList;
                            str = str;
                        } catch (Exception e7) {
                            e = e7;
                            e.printStackTrace();
                            return arrayList;
                        }
                    }
                }
            }
            return arrayList2;
        } catch (Exception e8) {
            e = e8;
            arrayList = arrayList2;
            e.printStackTrace();
            return arrayList;
        }
    }

    public int GetMaxCholesterolValue(int i) {
        int i2;
        int i3;
        int i4;
        int i5;
        int[] iArr = new int[4];
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT MAX(cholesterol) FROM cholesterol_data WHERE user_id=" + i, null);
            if (rawQuery == null || rawQuery.getCount() <= 0 || !rawQuery.moveToFirst()) {
                i2 = 0;
            } else {
                do {
                    i2 = rawQuery.getInt(0);
                } while (rawQuery.moveToNext());
            }
            Cursor rawQuery2 = sqLiteDatabase.rawQuery("SELECT MAX(HDL) FROM cholesterol_data WHERE user_id=" + i, null);
            if (rawQuery2 == null || rawQuery2.getCount() <= 0 || !rawQuery2.moveToFirst()) {
                i3 = 0;
            } else {
                do {
                    i3 = rawQuery2.getInt(0);
                } while (rawQuery2.moveToNext());
            }
            Cursor rawQuery3 = sqLiteDatabase.rawQuery("SELECT MAX(LDL) FROM cholesterol_data WHERE user_id=" + i, null);
            if (rawQuery3 == null || rawQuery3.getCount() <= 0 || !rawQuery3.moveToFirst()) {
                i4 = 0;
            } else {
                do {
                    i4 = rawQuery3.getInt(0);
                } while (rawQuery3.moveToNext());
            }
            Cursor rawQuery4 = sqLiteDatabase.rawQuery("SELECT MAX(triglyceride) FROM cholesterol_data WHERE user_id=" + i, null);
            if (rawQuery4 == null || rawQuery4.getCount() <= 0 || !rawQuery4.moveToFirst()) {
                i5 = 0;
            } else {
                do {
                    i5 = rawQuery4.getInt(0);
                } while (rawQuery4.moveToNext());
            }
            iArr[0] = i2;
            iArr[1] = i3;
            iArr[2] = i4;
            iArr[3] = i5;
            ArrayList arrayList = new ArrayList();
            for (int i6 = 0; i6 < 4; i6++) {
                arrayList.add(Integer.valueOf(iArr[i6]));
            }
            int intValue = ((Integer) Collections.max(arrayList)).intValue();
            Log.e("Cholesterol :", "Graph Max Y Axis :- " + intValue);
            return intValue;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void deleteCholesterolByID(int i) {
        SQLiteDatabase sQLiteDatabase = sqLiteDatabase;
        sQLiteDatabase.delete(CHOLESTEROL_TABLE, "row_id=" + i, null);
    }

    public void deleteCholesterolByUserID(int i) {
        SQLiteDatabase sQLiteDatabase = sqLiteDatabase;
        sQLiteDatabase.delete(CHOLESTEROL_TABLE, "user_id=" + i, null);
    }

    public void deleteAllCholesterolData() {
        sqLiteDatabase.execSQL("DELETE FROM cholesterol_data");
    }

    public List GetCholesterolChartAllData(int i) {
        ArrayList arrayList;
        Exception e;
        CholesterolChartData cholesterolChartData;
        Exception e2;
        String str = KEY_ROW_ID;
        ArrayList arrayList2 = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM cholesterol_data WHERE user_id=" + i + " ORDER BY " + str + " DESC", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                    return arrayList2;
                } else if (rawQuery.moveToFirst()) {
                    while (true) {
                        int i2 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(str));
                        int i3 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        float f = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_CHOLESTEROL));
                        float f2 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_HDL));
                        float f3 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_LDL));
                        float f4 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_TRIGLYCERIDE));
                        try {
                            String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_RESULT));
                            try {
                                String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_NOTES));
                                cholesterolChartData = new CholesterolChartData();
                                cholesterolChartData.row_id = i2;
                                cholesterolChartData.user_id = i3;
                                cholesterolChartData.dateTime = string.trim();
                                cholesterolChartData.date = string2.trim();
                                cholesterolChartData.time = string3.trim();
                                cholesterolChartData.day = i4;
                                cholesterolChartData.month = i5;
                                cholesterolChartData.year = i6;
                                cholesterolChartData.hour = i7;
                                cholesterolChartData.cholesterol_value = f;
                                cholesterolChartData.hdl_value = f2;
                                cholesterolChartData.ldl_value = f3;
                                cholesterolChartData.triglyceride_value = f4;
                                cholesterolChartData.result = string4.trim();
                                cholesterolChartData.notes = string5.trim();
                                try {
                                    try {
                                        Date parse = new SimpleDateFormat(this.date_time_format).parse(string);
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                                        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                                        cholesterolChartData.date = simpleDateFormat.format(parse).trim();
                                        cholesterolChartData.time = simpleDateFormat3.format(parse).trim();
                                        cholesterolChartData.month_string = simpleDateFormat2.format(parse).trim();
                                    } catch (Exception e3) {
                                        e2 = e3;
                                    }
                                } catch (Exception e4) {
                                    e2 = e4;
                                    e2.printStackTrace();
                                    arrayList = arrayList2;
                                    arrayList.add(cholesterolChartData);
                                    if (rawQuery.moveToNext()) {
                                    }
                                }
                                arrayList = arrayList2;
                            } catch (Exception e5) {
                                e = e5;
                                arrayList = arrayList2;
                                e.printStackTrace();
                                return arrayList;
                            }
                        } catch (Exception e6) {
                            e = e6;
                            arrayList = arrayList2;
                            e.printStackTrace();
                            return arrayList;
                        }
                        try {
                            arrayList.add(cholesterolChartData);
                            if (rawQuery.moveToNext()) {
                                return arrayList;
                            }
                            rawQuery = rawQuery;
                            arrayList2 = arrayList;
                            str = str;
                        } catch (Exception e7) {
                            e = e7;
                            e.printStackTrace();
                            return arrayList;
                        }
                    }
                }
            }
            return arrayList2;
        } catch (Exception e8) {
            e = e8;
            arrayList = arrayList2;
            e.printStackTrace();
            return arrayList;
        }
    }

    public List GetCholesterolChartTodayData(int i, int i2, int i3, int i4) {
        Exception e;
        Exception e2;
        ArrayList arrayList = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM cholesterol_data WHERE day = " + i2 + " AND month = " + i3 + " AND year = " + i4 + " AND user_id = " + i + " ORDER BY row_id ASC;", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    while (true) {
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i8 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i9 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i10 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        float f = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_CHOLESTEROL));
                        float f2 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_HDL));
                        float f3 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_LDL));
                        float f4 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_TRIGLYCERIDE));
                        try {
                            String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_RESULT));
                            try {
                                String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_NOTES));
                                CholesterolChartData cholesterolChartData = new CholesterolChartData();
                                cholesterolChartData.row_id = i5;
                                cholesterolChartData.user_id = i6;
                                cholesterolChartData.dateTime = string.trim();
                                cholesterolChartData.date = string2.trim();
                                cholesterolChartData.time = string3.trim();
                                cholesterolChartData.day = i7;
                                cholesterolChartData.month = i8;
                                cholesterolChartData.year = i9;
                                cholesterolChartData.hour = i10;
                                cholesterolChartData.cholesterol_value = f;
                                cholesterolChartData.hdl_value = f2;
                                cholesterolChartData.ldl_value = f3;
                                cholesterolChartData.triglyceride_value = f4;
                                cholesterolChartData.result = string4.trim();
                                cholesterolChartData.notes = string5.trim();
                                try {
                                    try {
                                        Date parse = new SimpleDateFormat(this.date_time_format).parse(string);
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                                        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                                        cholesterolChartData.date = simpleDateFormat.format(parse).trim();
                                        cholesterolChartData.time = simpleDateFormat3.format(parse).trim();
                                        cholesterolChartData.month_string = simpleDateFormat2.format(parse).trim();
                                    } catch (Exception e3) {
                                        e2 = e3;
                                    }
                                } catch (Exception e4) {
                                    e2 = e4;
                                    e2.printStackTrace();
                                    arrayList = arrayList;
                                    arrayList.add(cholesterolChartData);
                                    if (rawQuery.moveToNext()) {
                                    }
                                }
                                arrayList = arrayList;
                                arrayList.add(cholesterolChartData);
                                if (rawQuery.moveToNext()) {
                                    break;
                                }
                                rawQuery = rawQuery;
                            } catch (Exception e5) {
                                e = e5;
                                arrayList = arrayList;
                                e.printStackTrace();
                                return arrayList;
                            }
                        } catch (Exception e6) {
                            e = e6;
                            arrayList = arrayList;
                            e.printStackTrace();
                            return arrayList;
                        }
                    }
                }
            }
        } catch (Exception e7) {
            e = e7;
            e.printStackTrace();
            return arrayList;
        }
        return arrayList;
    }

    public List GetCholesterolChartMonthlyData(int i, int i2, int i3) {
        Exception e;
        String string;
        Exception e2;
        ArrayList arrayList = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM cholesterol_data WHERE month = " + i2 + " AND year = " + i3 + " AND user_id = " + i + " ORDER BY row_id ASC;", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    while (true) {
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i8 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i9 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        float f = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_CHOLESTEROL));
                        float f2 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_HDL));
                        float f3 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_LDL));
                        float f4 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_TRIGLYCERIDE));
                        try {
                            string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_RESULT));
                        } catch (Exception e3) {
                            e = e3;
                            arrayList = arrayList;
                            e.printStackTrace();
                            return arrayList;
                        }
                        try {
                            String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_NOTES));
                            CholesterolChartData cholesterolChartData = new CholesterolChartData();
                            cholesterolChartData.row_id = i4;
                            cholesterolChartData.user_id = i5;
                            cholesterolChartData.dateTime = string2.trim();
                            cholesterolChartData.date = string3.trim();
                            cholesterolChartData.time = string4.trim();
                            cholesterolChartData.day = i6;
                            cholesterolChartData.month = i7;
                            cholesterolChartData.year = i8;
                            cholesterolChartData.hour = i9;
                            cholesterolChartData.cholesterol_value = f;
                            cholesterolChartData.hdl_value = f2;
                            cholesterolChartData.ldl_value = f3;
                            cholesterolChartData.triglyceride_value = f4;
                            cholesterolChartData.result = string.trim();
                            cholesterolChartData.notes = string5.trim();
                            try {
                                try {
                                    Date parse = new SimpleDateFormat(this.date_time_format).parse(string2);
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                                    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                                    SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                                    cholesterolChartData.date = simpleDateFormat.format(parse).trim();
                                    cholesterolChartData.time = simpleDateFormat3.format(parse).trim();
                                    cholesterolChartData.month_string = simpleDateFormat2.format(parse).trim();
                                } catch (Exception e4) {
                                    e2 = e4;
                                }
                            } catch (Exception e5) {
                                e2 = e5;
                                e2.printStackTrace();
                                arrayList = arrayList;
                                arrayList.add(cholesterolChartData);
                                if (rawQuery.moveToNext()) {
                                }
                            }
                            arrayList = arrayList;
                            arrayList.add(cholesterolChartData);
                            if (rawQuery.moveToNext()) {
                                break;
                            }
                            rawQuery = rawQuery;
                        } catch (Exception e6) {
                            e = e6;
                            arrayList = arrayList;
                            e.printStackTrace();
                            return arrayList;
                        }
                    }
                }
            }
        } catch (Exception e7) {
            e = e7;
            e.printStackTrace();
            return arrayList;
        }
        return arrayList;
    }

    public List GetCholesterolChartYearlyData(int i, int i2) {
        Exception e;
        Exception e2;
        ArrayList arrayList = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM cholesterol_data WHERE year = " + i2 + " AND user_id = " + i + " ORDER BY row_id ASC;", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    while (true) {
                        int i3 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i8 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        float f = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_CHOLESTEROL));
                        float f2 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_HDL));
                        float f3 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_LDL));
                        float f4 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_TRIGLYCERIDE));
                        try {
                            String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_RESULT));
                            try {
                                String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_NOTES));
                                CholesterolChartData cholesterolChartData = new CholesterolChartData();
                                cholesterolChartData.row_id = i3;
                                cholesterolChartData.user_id = i4;
                                cholesterolChartData.dateTime = string.trim();
                                cholesterolChartData.date = string2.trim();
                                cholesterolChartData.time = string3.trim();
                                cholesterolChartData.day = i5;
                                cholesterolChartData.month = i6;
                                cholesterolChartData.year = i7;
                                cholesterolChartData.hour = i8;
                                cholesterolChartData.cholesterol_value = f;
                                cholesterolChartData.hdl_value = f2;
                                cholesterolChartData.ldl_value = f3;
                                cholesterolChartData.triglyceride_value = f4;
                                cholesterolChartData.result = string4.trim();
                                cholesterolChartData.notes = string5.trim();
                                try {
                                    try {
                                        Date parse = new SimpleDateFormat(this.date_time_format).parse(string);
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                                        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                                        cholesterolChartData.date = simpleDateFormat.format(parse).trim();
                                        cholesterolChartData.time = simpleDateFormat3.format(parse).trim();
                                        cholesterolChartData.month_string = simpleDateFormat2.format(parse).trim();
                                    } catch (Exception e3) {
                                        e2 = e3;
                                    }
                                } catch (Exception e4) {
                                    e2 = e4;
                                    e2.printStackTrace();
                                    arrayList = arrayList;
                                    arrayList.add(cholesterolChartData);
                                    if (rawQuery.moveToNext()) {
                                    }
                                }
                                arrayList = arrayList;
                                arrayList.add(cholesterolChartData);
                                if (rawQuery.moveToNext()) {
                                    break;
                                }
                                rawQuery = rawQuery;
                            } catch (Exception e5) {
                                e = e5;
                                arrayList = arrayList;
                                e.printStackTrace();
                                return arrayList;
                            }
                        } catch (Exception e6) {
                            e = e6;
                            arrayList = arrayList;
                            e.printStackTrace();
                            return arrayList;
                        }
                    }
                }
            }
        } catch (Exception e7) {
            e = e7;
            e.printStackTrace();
            return arrayList;
        }
        return arrayList;
    }

    public List GetCholesterolChartCustomData(int i, String str, String str2) {
        Exception e;
        Exception e2;
        ArrayList arrayList = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM cholesterol_data WHERE user_id = " + i + " AND date >= " + ("'" + str + "'") + " AND date <= " + ("'" + str2 + "'") + " ORDER BY row_id ASC;", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    while (true) {
                        int i2 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i3 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        float f = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_CHOLESTEROL));
                        float f2 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_HDL));
                        float f3 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_LDL));
                        float f4 = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_TRIGLYCERIDE));
                        try {
                            String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_RESULT));
                            try {
                                String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_NOTES));
                                CholesterolChartData cholesterolChartData = new CholesterolChartData();
                                cholesterolChartData.row_id = i2;
                                cholesterolChartData.user_id = i3;
                                cholesterolChartData.dateTime = string.trim();
                                cholesterolChartData.date = string2.trim();
                                cholesterolChartData.time = string3.trim();
                                cholesterolChartData.day = i4;
                                cholesterolChartData.month = i5;
                                cholesterolChartData.year = i6;
                                cholesterolChartData.hour = i7;
                                cholesterolChartData.cholesterol_value = f;
                                cholesterolChartData.hdl_value = f2;
                                cholesterolChartData.ldl_value = f3;
                                cholesterolChartData.triglyceride_value = f4;
                                cholesterolChartData.result = string4.trim();
                                cholesterolChartData.notes = string5.trim();
                                try {
                                    try {
                                        Date parse = new SimpleDateFormat(this.date_time_format).parse(string);
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                                        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                                        cholesterolChartData.date = simpleDateFormat.format(parse).trim();
                                        cholesterolChartData.time = simpleDateFormat3.format(parse).trim();
                                        cholesterolChartData.month_string = simpleDateFormat2.format(parse).trim();
                                    } catch (Exception e3) {
                                        e2 = e3;
                                    }
                                } catch (Exception e4) {
                                    e2 = e4;
                                    e2.printStackTrace();
                                    arrayList = arrayList;
                                    arrayList.add(cholesterolChartData);
                                    if (rawQuery.moveToNext()) {
                                    }
                                }
                                arrayList = arrayList;
                                arrayList.add(cholesterolChartData);
                                if (rawQuery.moveToNext()) {
                                    break;
                                }
                                rawQuery = rawQuery;
                            } catch (Exception e5) {
                                e = e5;
                                arrayList = arrayList;
                                e.printStackTrace();
                                return arrayList;
                            }
                        } catch (Exception e6) {
                            e = e6;
                            arrayList = arrayList;
                            e.printStackTrace();
                            return arrayList;
                        }
                    }
                }
            }
        } catch (Exception e7) {
            e = e7;
            e.printStackTrace();
            return arrayList;
        }
        return arrayList;
    }

    public long InsertHeartRateData(HeartRateData heartRateData) {
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        int i = heartRateData.user_id;
        String trim = heartRateData.dateTime.trim();
        String trim2 = heartRateData.date.trim();
        String trim3 = heartRateData.time.trim();
        int i2 = heartRateData.day;
        int i3 = heartRateData.month;
        int i4 = heartRateData.year;
        int i5 = heartRateData.hour;
        int i6 = heartRateData.heart_rate_value;
        int i7 = heartRateData.age;
        String trim4 = heartRateData.gender.trim();
        String trim5 = heartRateData.current_status.trim();
        String trim6 = heartRateData.color.trim();
        String trim7 = heartRateData.result.trim();
        String trim8 = heartRateData.range.trim();
        String trim9 = heartRateData.notes.trim();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USER_ID, Integer.valueOf(i));
        contentValues.put(KEY_DATE_TIME, trim);
        contentValues.put(KEY_DATE, trim2);
        contentValues.put(KEY_TIME, trim3);
        contentValues.put(KEY_DAY, Integer.valueOf(i2));
        contentValues.put(KEY_MONTH, Integer.valueOf(i3));
        contentValues.put(KEY_YEAR, Integer.valueOf(i4));
        contentValues.put(KEY_HOUR, Integer.valueOf(i5));
        contentValues.put(KEY_HEART_RATE, Integer.valueOf(i6));
        contentValues.put(KEY_AGE, Integer.valueOf(i7));
        contentValues.put(KEY_GENDER, trim4);
        contentValues.put(KEY_CURRENT_STATUS, trim5);
        contentValues.put("color", trim6);
        contentValues.put(KEY_RESULT, trim7);
        contentValues.put(KEY_RANGE, trim8);
        contentValues.put(KEY_NOTES, trim9);
        return sqLiteDatabase.insertWithOnConflict(HEART_RATE_TABLE, null, contentValues, 4);
    }

    public long UpdateHeartRateData(int i, int i2, HeartRateData heartRateData) {
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        String trim = heartRateData.dateTime.trim();
        String trim2 = heartRateData.date.trim();
        String trim3 = heartRateData.time.trim();
        int i3 = heartRateData.day;
        int i4 = heartRateData.month;
        int i5 = heartRateData.year;
        int i6 = heartRateData.hour;
        int i7 = heartRateData.heart_rate_value;
        int i8 = heartRateData.age;
        String trim4 = heartRateData.gender.trim();
        String trim5 = heartRateData.current_status.trim();
        String trim6 = heartRateData.color.trim();
        String trim7 = heartRateData.result.trim();
        String trim8 = heartRateData.range.trim();
        String trim9 = heartRateData.notes.trim();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USER_ID, Integer.valueOf(i2));
        contentValues.put(KEY_DATE_TIME, trim);
        contentValues.put(KEY_DATE, trim2);
        contentValues.put(KEY_TIME, trim3);
        contentValues.put(KEY_DAY, Integer.valueOf(i3));
        contentValues.put(KEY_MONTH, Integer.valueOf(i4));
        contentValues.put(KEY_YEAR, Integer.valueOf(i5));
        contentValues.put(KEY_HOUR, Integer.valueOf(i6));
        contentValues.put(KEY_HEART_RATE, Integer.valueOf(i7));
        contentValues.put(KEY_AGE, Integer.valueOf(i8));
        contentValues.put(KEY_GENDER, trim4);
        contentValues.put(KEY_CURRENT_STATUS, trim5);
        contentValues.put("color", trim6);
        contentValues.put(KEY_RESULT, trim7);
        contentValues.put(KEY_RANGE, trim8);
        contentValues.put(KEY_NOTES, trim9);
        return sqLiteDatabase.update(HEART_RATE_TABLE, contentValues, "row_id=" + i, null);
    }

    public List GetHeartRateData() {
        Exception e;
        Exception e2;
        ArrayList arrayList = new ArrayList();
        try {
            SQLiteDatabase readableDatabase = sqLiteHelper.getReadableDatabase();
            sqLiteDatabase = readableDatabase;
            Cursor rawQuery = readableDatabase.rawQuery("SELECT * FROM heart_rate_data ORDER BY row_id DESC", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    while (true) {
                        int i = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i2 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i3 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HEART_RATE));
                        int i8 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_AGE));
                        String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_GENDER));
                        String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_CURRENT_STATUS));
                        try {
                            String string6 = rawQuery.getString(rawQuery.getColumnIndexOrThrow("color"));
                            try {
                                String string7 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_RESULT));
                                String string8 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_RANGE));
                                String string9 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_NOTES));
                                HeartRateData heartRateData = new HeartRateData();
                                heartRateData.row_id = i;
                                heartRateData.user_id = i2;
                                heartRateData.dateTime = string.trim();
                                heartRateData.date = string2.trim();
                                heartRateData.time = string3.trim();
                                heartRateData.day = i3;
                                heartRateData.month = i4;
                                heartRateData.year = i5;
                                heartRateData.hour = i6;
                                heartRateData.heart_rate_value = i7;
                                heartRateData.age = i8;
                                heartRateData.gender = string4;
                                heartRateData.current_status = string5;
                                heartRateData.color = string6.trim();
                                heartRateData.result = string7.trim();
                                heartRateData.range = string8.trim();
                                heartRateData.notes = string9.trim();
                                try {
                                    try {
                                        Date parse = new SimpleDateFormat(this.date_time_format).parse(string);
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                                        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                                        heartRateData.date = simpleDateFormat.format(parse).trim();
                                        heartRateData.time = simpleDateFormat3.format(parse).trim();
                                        heartRateData.month_string = simpleDateFormat2.format(parse).trim();
                                    } catch (Exception e3) {
                                        e2 = e3;
                                    }
                                } catch (Exception e4) {
                                    e2 = e4;
                                    e2.printStackTrace();
                                    arrayList = arrayList;
                                    arrayList.add(heartRateData);
                                    if (rawQuery.moveToNext()) {
                                    }
                                }
                                arrayList = arrayList;
                                arrayList.add(heartRateData);
                                if (rawQuery.moveToNext()) {
                                    break;
                                }
                                rawQuery = rawQuery;
                            } catch (Exception e5) {
                                e = e5;
                                arrayList = arrayList;
                                e.printStackTrace();
                                return arrayList;
                            }
                        } catch (Exception e6) {
                            e = e6;
                            arrayList = arrayList;
                            e.printStackTrace();
                            return arrayList;
                        }
                    }
                }
            }
        } catch (Exception e7) {
            e = e7;
            e.printStackTrace();
            return arrayList;
        }
        return arrayList;
    }

    public List GetHeartRateDataByUserID(int i) {
        ArrayList arrayList;
        Exception e;
        HeartRateData heartRateData;
        Exception e2;
        String str = KEY_ROW_ID;
        ArrayList arrayList2 = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM heart_rate_data WHERE user_id=" + i + " ORDER BY " + str + " DESC", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                    return arrayList2;
                } else if (rawQuery.moveToFirst()) {
                    while (true) {
                        int i2 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(str));
                        int i3 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        int i8 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HEART_RATE));
                        int i9 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_AGE));
                        String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_GENDER));
                        String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_CURRENT_STATUS));
                        try {
                            String string6 = rawQuery.getString(rawQuery.getColumnIndexOrThrow("color"));
                            try {
                                String string7 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_RESULT));
                                String string8 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_RANGE));
                                String string9 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_NOTES));
                                heartRateData = new HeartRateData();
                                heartRateData.row_id = i2;
                                heartRateData.user_id = i3;
                                heartRateData.dateTime = string.trim();
                                heartRateData.date = string2.trim();
                                heartRateData.time = string3.trim();
                                heartRateData.day = i4;
                                heartRateData.month = i5;
                                heartRateData.year = i6;
                                heartRateData.hour = i7;
                                heartRateData.heart_rate_value = i8;
                                heartRateData.age = i9;
                                heartRateData.gender = string4;
                                heartRateData.current_status = string5;
                                heartRateData.color = string6.trim();
                                heartRateData.result = string7.trim();
                                heartRateData.range = string8.trim();
                                heartRateData.notes = string9.trim();
                                try {
                                    try {
                                        Date parse = new SimpleDateFormat(this.date_time_format).parse(string);
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                                        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                                        heartRateData.date = simpleDateFormat.format(parse).trim();
                                        heartRateData.time = simpleDateFormat3.format(parse).trim();
                                        heartRateData.month_string = simpleDateFormat2.format(parse).trim();
                                    } catch (Exception e3) {
                                        e2 = e3;
                                    }
                                } catch (Exception e4) {
                                    e2 = e4;
                                    e2.printStackTrace();
                                    arrayList = arrayList2;
                                    arrayList.add(heartRateData);
                                    if (rawQuery.moveToNext()) {
                                    }
                                }
                                arrayList = arrayList2;
                            } catch (Exception e5) {
                                e = e5;
                                arrayList = arrayList2;
                                e.printStackTrace();
                                return arrayList;
                            }
                            try {
                                arrayList.add(heartRateData);
                                if (!rawQuery.moveToNext()) {
                                    return arrayList;
                                }
                                arrayList2 = arrayList;
                                str = str;
                                rawQuery = rawQuery;
                            } catch (Exception e6) {
                                e = e6;
                                e.printStackTrace();
                                return arrayList;
                            }
                        } catch (Exception e7) {
                            e = e7;
                            arrayList = arrayList2;
                            e.printStackTrace();
                            return arrayList;
                        }
                    }
                }
            }
            return arrayList2;
        } catch (Exception e8) {
            e = e8;
            arrayList = arrayList2;
            e.printStackTrace();
            return arrayList;
        }
    }

    public int GetMaxHeartRateValue(int i) {
        Exception e;
        int i2 = 0;
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT MAX(heart_rate) FROM heart_rate_data WHERE user_id=" + i, null);
            if (rawQuery == null || rawQuery.getCount() <= 0 || !rawQuery.moveToFirst()) {
                return 0;
            }
            int i3 = 0;
            do {
                try {
                    i3 = rawQuery.getInt(0);
                } catch (Exception e2) {
                    e = e2;
                    i2 = i3;
                    e.printStackTrace();
                    return i2;
                }
            } while (rawQuery.moveToNext());
            return i3;
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            return i2;
        }
    }

    public void deleteHeartRateByID(int i) {
        SQLiteDatabase sQLiteDatabase = sqLiteDatabase;
        sQLiteDatabase.delete(HEART_RATE_TABLE, "row_id=" + i, null);
    }

    public void deleteHeartRateByUserID(int i) {
        SQLiteDatabase sQLiteDatabase = sqLiteDatabase;
        sQLiteDatabase.delete(HEART_RATE_TABLE, "user_id=" + i, null);
    }

    public void deleteAllHeartRateData() {
        sqLiteDatabase.execSQL("DELETE FROM heart_rate_data");
    }

    public List GetHeartRateChartAllData(int i) {
        ArrayList arrayList;
        Exception e;
        HeartRateChartData heartRateChartData;
        Exception e2;
        String str = KEY_ROW_ID;
        ArrayList arrayList2 = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM heart_rate_data WHERE user_id=" + i + " ORDER BY " + str + " DESC", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                    return arrayList2;
                } else if (rawQuery.moveToFirst()) {
                    while (true) {
                        int i2 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(str));
                        int i3 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        int i8 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HEART_RATE));
                        int i9 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_AGE));
                        String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_GENDER));
                        String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_CURRENT_STATUS));
                        try {
                            String string6 = rawQuery.getString(rawQuery.getColumnIndexOrThrow("color"));
                            try {
                                String string7 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_RESULT));
                                String string8 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_RANGE));
                                String string9 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_NOTES));
                                heartRateChartData = new HeartRateChartData();
                                heartRateChartData.row_id = i2;
                                heartRateChartData.user_id = i3;
                                heartRateChartData.dateTime = string.trim();
                                heartRateChartData.date = string2.trim();
                                heartRateChartData.time = string3.trim();
                                heartRateChartData.day = i4;
                                heartRateChartData.month = i5;
                                heartRateChartData.year = i6;
                                heartRateChartData.hour = i7;
                                heartRateChartData.heart_rate_value = i8;
                                heartRateChartData.age = i9;
                                heartRateChartData.gender = string4;
                                heartRateChartData.current_status = string5;
                                heartRateChartData.color = string6.trim();
                                heartRateChartData.result = string7.trim();
                                heartRateChartData.range = string8.trim();
                                heartRateChartData.notes = string9.trim();
                                try {
                                    try {
                                        Date parse = new SimpleDateFormat(this.date_time_format).parse(string);
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                                        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                                        heartRateChartData.date = simpleDateFormat.format(parse).trim();
                                        heartRateChartData.time = simpleDateFormat3.format(parse).trim();
                                        heartRateChartData.month_string = simpleDateFormat2.format(parse).trim();
                                    } catch (Exception e3) {
                                        e2 = e3;
                                    }
                                } catch (Exception e4) {
                                    e2 = e4;
                                    e2.printStackTrace();
                                    arrayList = arrayList2;
                                    arrayList.add(heartRateChartData);
                                    if (rawQuery.moveToNext()) {
                                    }
                                }
                                arrayList = arrayList2;
                            } catch (Exception e5) {
                                e = e5;
                                arrayList = arrayList2;
                                e.printStackTrace();
                                return arrayList;
                            }
                            try {
                                arrayList.add(heartRateChartData);
                                if (rawQuery.moveToNext()) {
                                    return arrayList;
                                }
                                arrayList2 = arrayList;
                                str = str;
                                rawQuery = rawQuery;
                            } catch (Exception e6) {
                                e = e6;
                                e.printStackTrace();
                                return arrayList;
                            }
                        } catch (Exception e7) {
                            e = e7;
                            arrayList = arrayList2;
                            e.printStackTrace();
                            return arrayList;
                        }
                    }
                }
            }
            return arrayList2;
        } catch (Exception e8) {
            e = e8;
            arrayList = arrayList2;
            e.printStackTrace();
            return arrayList;
        }
    }

    public List GetHeartRateChartTodayData(int i, int i2, int i3, int i4) {
        Exception e;
        Exception e2;
        ArrayList arrayList = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM heart_rate_data WHERE day = " + i2 + " AND month = " + i3 + " AND year = " + i4 + " AND user_id = " + i + " ORDER BY row_id ASC;", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    while (true) {
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i8 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i9 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i10 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        int i11 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HEART_RATE));
                        int i12 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_AGE));
                        String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_GENDER));
                        String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_CURRENT_STATUS));
                        try {
                            String string6 = rawQuery.getString(rawQuery.getColumnIndexOrThrow("color"));
                            try {
                                String string7 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_RESULT));
                                String string8 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_RANGE));
                                String string9 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_NOTES));
                                HeartRateChartData heartRateChartData = new HeartRateChartData();
                                heartRateChartData.row_id = i5;
                                heartRateChartData.user_id = i6;
                                heartRateChartData.dateTime = string.trim();
                                heartRateChartData.date = string2.trim();
                                heartRateChartData.time = string3.trim();
                                heartRateChartData.day = i7;
                                heartRateChartData.month = i8;
                                heartRateChartData.year = i9;
                                heartRateChartData.hour = i10;
                                heartRateChartData.heart_rate_value = i11;
                                heartRateChartData.age = i12;
                                heartRateChartData.gender = string4;
                                heartRateChartData.current_status = string5;
                                heartRateChartData.color = string6.trim();
                                heartRateChartData.result = string7.trim();
                                heartRateChartData.range = string8.trim();
                                heartRateChartData.notes = string9.trim();
                                try {
                                    try {
                                        Date parse = new SimpleDateFormat(this.date_time_format).parse(string);
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                                        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                                        heartRateChartData.date = simpleDateFormat.format(parse).trim();
                                        heartRateChartData.time = simpleDateFormat3.format(parse).trim();
                                        heartRateChartData.month_string = simpleDateFormat2.format(parse).trim();
                                    } catch (Exception e3) {
                                        e2 = e3;
                                    }
                                } catch (Exception e4) {
                                    e2 = e4;
                                    e2.printStackTrace();
                                    arrayList = arrayList;
                                    arrayList.add(heartRateChartData);
                                    if (rawQuery.moveToNext()) {
                                    }
                                }
                                arrayList = arrayList;
                                arrayList.add(heartRateChartData);
                                if (rawQuery.moveToNext()) {
                                    break;
                                }
                                rawQuery = rawQuery;
                            } catch (Exception e5) {
                                e = e5;
                                arrayList = arrayList;
                                e.printStackTrace();
                                return arrayList;
                            }
                        } catch (Exception e6) {
                            e = e6;
                            arrayList = arrayList;
                            e.printStackTrace();
                            return arrayList;
                        }
                    }
                }
            }
        } catch (Exception e7) {
            e = e7;
            e.printStackTrace();
            return arrayList;
        }
        return arrayList;
    }

    public List GetHeartRateChartMonthlyData(int i, int i2, int i3) {
        Exception e;
        String string;
        Exception e2;
        ArrayList arrayList = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM heart_rate_data WHERE month = " + i2 + " AND year = " + i3 + " AND user_id = " + i + " ORDER BY row_id ASC;", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    while (true) {
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i8 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i9 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        int i10 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HEART_RATE));
                        int i11 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_AGE));
                        String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_GENDER));
                        String string6 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_CURRENT_STATUS));
                        try {
                            string = rawQuery.getString(rawQuery.getColumnIndexOrThrow("color"));
                        } catch (Exception e3) {
                            e = e3;
                            arrayList = arrayList;
                            e.printStackTrace();
                            return arrayList;
                        }
                        try {
                            String string7 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_RESULT));
                            String string8 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_RANGE));
                            String string9 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_NOTES));
                            HeartRateChartData heartRateChartData = new HeartRateChartData();
                            heartRateChartData.row_id = i4;
                            heartRateChartData.user_id = i5;
                            heartRateChartData.dateTime = string2.trim();
                            heartRateChartData.date = string3.trim();
                            heartRateChartData.time = string4.trim();
                            heartRateChartData.day = i6;
                            heartRateChartData.month = i7;
                            heartRateChartData.year = i8;
                            heartRateChartData.hour = i9;
                            heartRateChartData.heart_rate_value = i10;
                            heartRateChartData.age = i11;
                            heartRateChartData.gender = string5;
                            heartRateChartData.current_status = string6;
                            heartRateChartData.color = string.trim();
                            heartRateChartData.result = string7.trim();
                            heartRateChartData.range = string8.trim();
                            heartRateChartData.notes = string9.trim();
                            try {
                                try {
                                    Date parse = new SimpleDateFormat(this.date_time_format).parse(string2);
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                                    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                                    SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                                    heartRateChartData.date = simpleDateFormat.format(parse).trim();
                                    heartRateChartData.time = simpleDateFormat3.format(parse).trim();
                                    heartRateChartData.month_string = simpleDateFormat2.format(parse).trim();
                                } catch (Exception e4) {
                                    e2 = e4;
                                }
                            } catch (Exception e5) {
                                e2 = e5;
                                e2.printStackTrace();
                                arrayList = arrayList;
                                arrayList.add(heartRateChartData);
                                if (rawQuery.moveToNext()) {
                                }
                            }
                            arrayList = arrayList;
                            arrayList.add(heartRateChartData);
                            if (rawQuery.moveToNext()) {
                                break;
                            }
                            rawQuery = rawQuery;
                        } catch (Exception e6) {
                            e = e6;
                            arrayList = arrayList;
                            e.printStackTrace();
                            return arrayList;
                        }
                    }
                }
            }
        } catch (Exception e7) {
            e = e7;
            e.printStackTrace();
            return arrayList;
        }
        return arrayList;
    }

    public List GetHeartRateChartYearlyData(int i, int i2) {
        Exception e;
        Exception e2;
        ArrayList arrayList = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM heart_rate_data WHERE year = " + i2 + " AND user_id = " + i + " ORDER BY row_id ASC;", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    while (true) {
                        int i3 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i8 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        int i9 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HEART_RATE));
                        int i10 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_AGE));
                        String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_GENDER));
                        String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_CURRENT_STATUS));
                        try {
                            String string6 = rawQuery.getString(rawQuery.getColumnIndexOrThrow("color"));
                            try {
                                String string7 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_RESULT));
                                String string8 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_RANGE));
                                String string9 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_NOTES));
                                HeartRateChartData heartRateChartData = new HeartRateChartData();
                                heartRateChartData.row_id = i3;
                                heartRateChartData.user_id = i4;
                                heartRateChartData.dateTime = string.trim();
                                heartRateChartData.date = string2.trim();
                                heartRateChartData.time = string3.trim();
                                heartRateChartData.day = i5;
                                heartRateChartData.month = i6;
                                heartRateChartData.year = i7;
                                heartRateChartData.hour = i8;
                                heartRateChartData.heart_rate_value = i9;
                                heartRateChartData.age = i10;
                                heartRateChartData.gender = string4;
                                heartRateChartData.current_status = string5;
                                heartRateChartData.color = string6.trim();
                                heartRateChartData.result = string7.trim();
                                heartRateChartData.range = string8.trim();
                                heartRateChartData.notes = string9.trim();
                                try {
                                    try {
                                        Date parse = new SimpleDateFormat(this.date_time_format).parse(string);
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                                        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                                        heartRateChartData.date = simpleDateFormat.format(parse).trim();
                                        heartRateChartData.time = simpleDateFormat3.format(parse).trim();
                                        heartRateChartData.month_string = simpleDateFormat2.format(parse).trim();
                                    } catch (Exception e3) {
                                        e2 = e3;
                                    }
                                } catch (Exception e4) {
                                    e2 = e4;
                                    e2.printStackTrace();
                                    arrayList = arrayList;
                                    arrayList.add(heartRateChartData);
                                    if (rawQuery.moveToNext()) {
                                    }
                                }
                                arrayList = arrayList;
                                arrayList.add(heartRateChartData);
                                if (rawQuery.moveToNext()) {
                                    break;
                                }
                                rawQuery = rawQuery;
                            } catch (Exception e5) {
                                e = e5;
                                arrayList = arrayList;
                                e.printStackTrace();
                                return arrayList;
                            }
                        } catch (Exception e6) {
                            e = e6;
                            arrayList = arrayList;
                            e.printStackTrace();
                            return arrayList;
                        }
                    }
                }
            }
        } catch (Exception e7) {
            e = e7;
            e.printStackTrace();
            return arrayList;
        }
        return arrayList;
    }

    public List GetHeartRateChartCustomData(int i, String str, String str2) {
        Exception e;
        Exception e2;
        ArrayList arrayList = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM heart_rate_data WHERE user_id = " + i + " AND date >= " + ("'" + str + "'") + " AND date <= " + ("'" + str2 + "'") + " ORDER BY row_id ASC;", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    while (true) {
                        int i2 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i3 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        int i8 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HEART_RATE));
                        int i9 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_AGE));
                        String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_GENDER));
                        String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_CURRENT_STATUS));
                        try {
                            String string6 = rawQuery.getString(rawQuery.getColumnIndexOrThrow("color"));
                            try {
                                String string7 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_RESULT));
                                String string8 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_RANGE));
                                String string9 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_NOTES));
                                HeartRateChartData heartRateChartData = new HeartRateChartData();
                                heartRateChartData.row_id = i2;
                                heartRateChartData.user_id = i3;
                                heartRateChartData.dateTime = string.trim();
                                heartRateChartData.date = string2.trim();
                                heartRateChartData.time = string3.trim();
                                heartRateChartData.day = i4;
                                heartRateChartData.month = i5;
                                heartRateChartData.year = i6;
                                heartRateChartData.hour = i7;
                                heartRateChartData.heart_rate_value = i8;
                                heartRateChartData.age = i9;
                                heartRateChartData.gender = string4;
                                heartRateChartData.current_status = string5;
                                heartRateChartData.color = string6.trim();
                                heartRateChartData.result = string7.trim();
                                heartRateChartData.range = string8.trim();
                                heartRateChartData.notes = string9.trim();
                                try {
                                    try {
                                        Date parse = new SimpleDateFormat(this.date_time_format).parse(string);
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                                        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                                        heartRateChartData.date = simpleDateFormat.format(parse).trim();
                                        heartRateChartData.time = simpleDateFormat3.format(parse).trim();
                                        heartRateChartData.month_string = simpleDateFormat2.format(parse).trim();
                                    } catch (Exception e3) {
                                        e2 = e3;
                                    }
                                } catch (Exception e4) {
                                    e2 = e4;
                                    e2.printStackTrace();
                                    arrayList = arrayList;
                                    arrayList.add(heartRateChartData);
                                    if (rawQuery.moveToNext()) {
                                    }
                                }
                                arrayList = arrayList;
                                arrayList.add(heartRateChartData);
                                if (rawQuery.moveToNext()) {
                                    break;
                                }
                                rawQuery = rawQuery;
                            } catch (Exception e5) {
                                e = e5;
                                arrayList = arrayList;
                                e.printStackTrace();
                                return arrayList;
                            }
                        } catch (Exception e6) {
                            e = e6;
                            arrayList = arrayList;
                            e.printStackTrace();
                            return arrayList;
                        }
                    }
                }
            }
        } catch (Exception e7) {
            e = e7;
            e.printStackTrace();
            return arrayList;
        }
        return arrayList;
    }

    public long InsertWeightData(WeightData weightData) {
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        int i = weightData.user_id;
        float f = weightData.weight;
        String trim = weightData.dateTime.trim();
        String trim2 = weightData.date.trim();
        String trim3 = weightData.time.trim();
        int i2 = weightData.day;
        int i3 = weightData.month;
        int i4 = weightData.year;
        int i5 = weightData.hour;
        String trim4 = weightData.notes.trim();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USER_ID, Integer.valueOf(i));
        contentValues.put(KEY_DATE_TIME, trim);
        contentValues.put(KEY_DATE, trim2);
        contentValues.put(KEY_TIME, trim3);
        contentValues.put(KEY_DAY, Integer.valueOf(i2));
        contentValues.put(KEY_MONTH, Integer.valueOf(i3));
        contentValues.put(KEY_YEAR, Integer.valueOf(i4));
        contentValues.put(KEY_HOUR, Integer.valueOf(i5));
        contentValues.put(KEY_WEIGHT, Float.valueOf(f));
        contentValues.put(KEY_NOTES, trim4);
        return sqLiteDatabase.insertWithOnConflict(WEIGHT_DATA_TABLE, null, contentValues, 4);
    }

    public long UpdateWeightData(int i, int i2, WeightData weightData) {
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        float f = weightData.weight;
        String trim = weightData.dateTime.trim();
        String trim2 = weightData.date.trim();
        String trim3 = weightData.time.trim();
        int i3 = weightData.day;
        int i4 = weightData.month;
        int i5 = weightData.year;
        int i6 = weightData.hour;
        String trim4 = weightData.notes.trim();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USER_ID, Integer.valueOf(i2));
        contentValues.put(KEY_DATE_TIME, trim);
        contentValues.put(KEY_DATE, trim2);
        contentValues.put(KEY_TIME, trim3);
        contentValues.put(KEY_DAY, Integer.valueOf(i3));
        contentValues.put(KEY_MONTH, Integer.valueOf(i4));
        contentValues.put(KEY_YEAR, Integer.valueOf(i5));
        contentValues.put(KEY_HOUR, Integer.valueOf(i6));
        contentValues.put(KEY_WEIGHT, Float.valueOf(f));
        contentValues.put(KEY_NOTES, trim4);
        return sqLiteDatabase.update(WEIGHT_DATA_TABLE, contentValues, "row_id=" + i, null);
    }

    public List GetWeightData() {
        ArrayList arrayList = new ArrayList();
        try {
            SQLiteDatabase readableDatabase = sqLiteHelper.getReadableDatabase();
            sqLiteDatabase = readableDatabase;
            Cursor rawQuery = readableDatabase.rawQuery("SELECT * FROM weight_data ORDER BY row_id DESC", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    do {
                        int i = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i2 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i3 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        float f = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_WEIGHT));
                        String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_NOTES));
                        WeightData weightData = new WeightData();
                        weightData.row_id = i;
                        weightData.user_id = i2;
                        weightData.dateTime = string.trim();
                        weightData.date = string2.trim();
                        weightData.time = string3.trim();
                        weightData.day = i3;
                        weightData.month = i4;
                        weightData.year = i5;
                        weightData.hour = i6;
                        weightData.weight = f;
                        weightData.notes = string4.trim();
                        try {
                            Date parse = new SimpleDateFormat(this.date_time_format).parse(string);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                            SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                            weightData.date = simpleDateFormat.format(parse).trim();
                            weightData.time = simpleDateFormat3.format(parse).trim();
                            weightData.month_string = simpleDateFormat2.format(parse).trim();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        arrayList.add(weightData);
                    } while (rawQuery.moveToNext());
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return arrayList;
    }

    public List GetWeightDataByUserID(int i) {
        ArrayList arrayList = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM weight_data WHERE user_id=" + i + " ORDER BY " + KEY_ROW_ID + " DESC", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    do {
                        int i2 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i3 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        float f = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_WEIGHT));
                        String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_NOTES));
                        WeightData weightData = new WeightData();
                        weightData.row_id = i2;
                        weightData.user_id = i3;
                        weightData.dateTime = string.trim();
                        weightData.date = string2.trim();
                        weightData.time = string3.trim();
                        weightData.day = i4;
                        weightData.month = i5;
                        weightData.year = i6;
                        weightData.hour = i7;
                        weightData.weight = f;
                        weightData.notes = string4.trim();
                        try {
                            Date parse = new SimpleDateFormat(this.date_time_format).parse(string);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                            SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                            weightData.date = simpleDateFormat.format(parse).trim();
                            weightData.time = simpleDateFormat3.format(parse).trim();
                            weightData.month_string = simpleDateFormat2.format(parse).trim();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        arrayList.add(weightData);
                    } while (rawQuery.moveToNext());
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return arrayList;
    }

    public int GetMaximumWeight() {
        SQLiteDatabase readableDatabase = sqLiteHelper.getReadableDatabase();
        sqLiteDatabase = readableDatabase;
        Cursor rawQuery = readableDatabase.rawQuery("SELECT MAX(weight) as max_weight FROM weight_data", null);
        int i = rawQuery.moveToFirst() ? rawQuery.getInt(rawQuery.getColumnIndexOrThrow("max_weight")) : 0;
        rawQuery.close();
        return i + 50;
    }

    public int GetMaxWeightValue(int i) {
        Exception e;
        int i2 = 0;
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT MAX(weight) FROM weight_data WHERE user_id=" + i, null);
            if (rawQuery == null || rawQuery.getCount() <= 0 || !rawQuery.moveToFirst()) {
                return 0;
            }
            int i3 = 0;
            do {
                try {
                    i3 = rawQuery.getInt(0);
                } catch (Exception e2) {
                    e = e2;
                    i2 = i3;
                    e.printStackTrace();
                    return i2;
                }
            } while (rawQuery.moveToNext());
            return i3;
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            return i2;
        }
    }

    public void deleteWeightByID(int i) {
        SQLiteDatabase sQLiteDatabase = sqLiteDatabase;
        sQLiteDatabase.delete(WEIGHT_DATA_TABLE, "row_id=" + i, null);
    }

    public void deleteWeightByUserID(int i) {
        SQLiteDatabase sQLiteDatabase = sqLiteDatabase;
        sQLiteDatabase.delete(WEIGHT_DATA_TABLE, "user_id=" + i, null);
    }

    public void deleteAllWeightData() {
        sqLiteDatabase.execSQL("DELETE FROM weight_data");
    }

    public List GetWeightChartAllData(int i) {
        ArrayList arrayList = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM weight_data WHERE user_id=" + i + " ORDER BY " + KEY_ROW_ID + " DESC", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    do {
                        int i2 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i3 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        float f = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_WEIGHT));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_NOTES));
                        WeightChartData weightChartData = new WeightChartData();
                        weightChartData.row_id = i2;
                        weightChartData.user_id = i3;
                        weightChartData.dateTime = string.trim();
                        weightChartData.day = i4;
                        weightChartData.month = i5;
                        weightChartData.year = i6;
                        weightChartData.hour = i7;
                        weightChartData.weight = f;
                        weightChartData.notes = string2.trim();
                        try {
                            Date parse = new SimpleDateFormat("dd/MM/yyyy hh:mm a").parse(string);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("hh:mm a");
                            weightChartData.date = simpleDateFormat.format(parse).trim();
                            weightChartData.time = simpleDateFormat2.format(parse).trim();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        arrayList.add(weightChartData);
                    } while (rawQuery.moveToNext());
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return arrayList;
    }

    public List GetWeightChartTodayData(int i, int i2, int i3, int i4) {
        ArrayList arrayList = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM weight_data WHERE day = " + i2 + " AND month = " + i3 + " AND year = " + i4 + " AND user_id = " + i + " ORDER BY row_id ASC;", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    do {
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i8 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i9 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i10 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        float f = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_WEIGHT));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_NOTES));
                        WeightChartData weightChartData = new WeightChartData();
                        weightChartData.row_id = i5;
                        weightChartData.user_id = i6;
                        weightChartData.dateTime = string.trim();
                        weightChartData.day = i7;
                        weightChartData.month = i8;
                        weightChartData.year = i9;
                        weightChartData.hour = i10;
                        weightChartData.weight = f;
                        weightChartData.notes = string2.trim();
                        try {
                            Date parse = new SimpleDateFormat("dd/MM/yyyy hh:mm a").parse(string);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("hh:mm a");
                            weightChartData.date = simpleDateFormat.format(parse).trim();
                            weightChartData.time = simpleDateFormat2.format(parse).trim();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        arrayList.add(weightChartData);
                    } while (rawQuery.moveToNext());
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return arrayList;
    }

    public List GetWeightChartMonthlyData(int i, int i2, int i3) {
        ArrayList arrayList = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM weight_data WHERE month = " + i2 + " AND year = " + i3 + " AND user_id = " + i + " ORDER BY row_id ASC;", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    do {
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i8 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i9 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        float f = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_WEIGHT));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_NOTES));
                        WeightChartData weightChartData = new WeightChartData();
                        weightChartData.row_id = i4;
                        weightChartData.user_id = i5;
                        weightChartData.dateTime = string.trim();
                        weightChartData.day = i6;
                        weightChartData.month = i7;
                        weightChartData.year = i8;
                        weightChartData.hour = i9;
                        weightChartData.weight = f;
                        weightChartData.notes = string2.trim();
                        try {
                            Date parse = new SimpleDateFormat("dd/MM/yyyy hh:mm a").parse(string);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("hh:mm a");
                            weightChartData.date = simpleDateFormat.format(parse).trim();
                            weightChartData.time = simpleDateFormat2.format(parse).trim();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        arrayList.add(weightChartData);
                    } while (rawQuery.moveToNext());
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return arrayList;
    }

    public List GetWeightChartYearlyData(int i, int i2) {
        ArrayList arrayList = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM weight_data WHERE year = " + i2 + " AND user_id = " + i + " ORDER BY row_id ASC;", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    do {
                        int i3 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i8 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        float f = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_WEIGHT));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_NOTES));
                        WeightChartData weightChartData = new WeightChartData();
                        weightChartData.row_id = i3;
                        weightChartData.user_id = i4;
                        weightChartData.dateTime = string.trim();
                        weightChartData.day = i5;
                        weightChartData.month = i6;
                        weightChartData.year = i7;
                        weightChartData.hour = i8;
                        weightChartData.weight = f;
                        weightChartData.notes = string2.trim();
                        try {
                            Date parse = new SimpleDateFormat("dd/MM/yyyy hh:mm a").parse(string);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("hh:mm a");
                            weightChartData.date = simpleDateFormat.format(parse).trim();
                            weightChartData.time = simpleDateFormat2.format(parse).trim();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        arrayList.add(weightChartData);
                    } while (rawQuery.moveToNext());
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return arrayList;
    }

    public List GetWeightChartCustomData(int i, String str, String str2) {
        ArrayList arrayList = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM weight_data WHERE user_id = " + i + " AND date >= " + ("'" + str + "'") + " AND date <= " + ("'" + str2 + "'") + " ORDER BY row_id ASC;", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    do {
                        int i2 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i3 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        float f = rawQuery.getFloat(rawQuery.getColumnIndexOrThrow(KEY_WEIGHT));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_NOTES));
                        WeightChartData weightChartData = new WeightChartData();
                        weightChartData.row_id = i2;
                        weightChartData.user_id = i3;
                        weightChartData.dateTime = string.trim();
                        weightChartData.day = i4;
                        weightChartData.month = i5;
                        weightChartData.year = i6;
                        weightChartData.hour = i7;
                        weightChartData.weight = f;
                        weightChartData.notes = string2.trim();
                        try {
                            Date parse = new SimpleDateFormat("dd/MM/yyyy hh:mm a").parse(string);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("hh:mm a");
                            weightChartData.date = simpleDateFormat.format(parse).trim();
                            weightChartData.time = simpleDateFormat2.format(parse).trim();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        arrayList.add(weightChartData);
                    } while (rawQuery.moveToNext());
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return arrayList;
    }

    public long InsertAllMedicine(MedicineNameData medicineNameData) {
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        String trim = medicineNameData.medicine_id.trim();
        String trim2 = medicineNameData.medicine_name.trim();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_MEDICINE_ID, trim);
        contentValues.put(KEY_MEDICINE_NAME, trim2);
        return sqLiteDatabase.insertWithOnConflict(ALL_MEDICINE_TABLE, null, contentValues, 4);
    }

    public long UpdateMedicineName(int i, MedicineNameData medicineNameData) {
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        String trim = medicineNameData.medicine_id.trim();
        String trim2 = medicineNameData.medicine_name.trim();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_MEDICINE_ID, trim);
        contentValues.put(KEY_MEDICINE_NAME, trim2);
        return sqLiteDatabase.update(ALL_MEDICINE_TABLE, contentValues, "row_id=" + i, null);
    }

    public List GetMedicineNameData() {
        ArrayList arrayList = new ArrayList();
        try {
            SQLiteDatabase readableDatabase = sqLiteHelper.getReadableDatabase();
            sqLiteDatabase = readableDatabase;
            Cursor rawQuery = readableDatabase.rawQuery("SELECT * FROM all_medicines ORDER BY medicine_name ASC", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    do {
                        int i = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_MEDICINE_ID));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_MEDICINE_NAME));
                        MedicineNameData medicineNameData = new MedicineNameData();
                        medicineNameData.row_id = i;
                        medicineNameData.medicine_id = string.trim();
                        medicineNameData.medicine_name = string2.trim();
                        arrayList.add(medicineNameData);
                    } while (rawQuery.moveToNext());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public boolean CheckMedicineNameExist(String str) {
        SQLiteDatabase readableDatabase = sqLiteHelper.getReadableDatabase();
        sqLiteDatabase = readableDatabase;
        Cursor query = readableDatabase.query(ALL_MEDICINE_TABLE, null, "medicine_name =?", new String[]{str}, null, null, null);
        if (query == null || query.getCount() == 0) {
            query.close();
            sqLiteDatabase.close();
            return false;
        }
        query.close();
        sqLiteDatabase.close();
        return true;
    }

    public int getCountMedicines() {
        SQLiteDatabase readableDatabase = sqLiteHelper.getReadableDatabase();
        sqLiteDatabase = readableDatabase;
        Cursor rawQuery = readableDatabase.rawQuery("SELECT * FROM all_medicines", null);
        int count = rawQuery.getCount();
        rawQuery.close();
        return count;
    }

    public void deleteMedicines() {
        SQLiteDatabase readableDatabase = sqLiteHelper.getReadableDatabase();
        sqLiteDatabase = readableDatabase;
        readableDatabase.delete(ALL_MEDICINE_TABLE, null, null);
        sqLiteDatabase.close();
    }

    public String getMedicineID(String str) {
        SQLiteDatabase readableDatabase = sqLiteHelper.getReadableDatabase();
        sqLiteDatabase = readableDatabase;
        Cursor query = readableDatabase.query(ALL_MEDICINE_TABLE, null, "medicine_name =?", new String[]{str}, null, null, null);
        String string = query.moveToLast() ? query.getString(query.getColumnIndexOrThrow(KEY_MEDICINE_ID)) : "";
        query.close();
        sqLiteDatabase.close();
        return string;
    }

    public String getMedicineKeyID(String str) {
        SQLiteDatabase readableDatabase = sqLiteHelper.getReadableDatabase();
        sqLiteDatabase = readableDatabase;
        Cursor query = readableDatabase.query(ALL_MEDICINE_TABLE, null, "medicine_name =?", new String[]{str}, null, null, null);
        String string = query.moveToLast() ? query.getString(query.getColumnIndexOrThrow(KEY_ROW_ID)) : "";
        query.close();
        sqLiteDatabase.close();
        return string;
    }

    public void deleteMedicineNameData(int i) {
        SQLiteDatabase writableDatabase = sqLiteHelper.getWritableDatabase();
        sqLiteDatabase = writableDatabase;
        writableDatabase.delete(ALL_MEDICINE_TABLE, "row_id=" + i, null);
        sqLiteDatabase.close();
    }

    public boolean CheckMedicineNameDataExist() {
        SQLiteDatabase readableDatabase = sqLiteHelper.getReadableDatabase();
        sqLiteDatabase = readableDatabase;
        Cursor rawQuery = readableDatabase.rawQuery("SELECT * FROM all_medicines", null);
        return rawQuery != null && rawQuery.getCount() != 0;
    }

    public long InsertMedicineData(MedicineData medicineData) {
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        int i = medicineData.user_id;
        String trim = medicineData.dateTime.trim();
        String trim2 = medicineData.date.trim();
        String trim3 = medicineData.time.trim();
        int i2 = medicineData.day;
        int i3 = medicineData.month;
        int i4 = medicineData.year;
        int i5 = medicineData.hour;
        String trim4 = medicineData.medicine_name.trim();
        String trim5 = medicineData.measure_unit.trim();
        String trim6 = medicineData.dosage.trim();
        String trim7 = medicineData.times_day.trim();
        String trim8 = medicineData.notes.trim();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USER_ID, Integer.valueOf(i));
        contentValues.put(KEY_DATE_TIME, trim);
        contentValues.put(KEY_DATE, trim2);
        contentValues.put(KEY_TIME, trim3);
        contentValues.put(KEY_DAY, Integer.valueOf(i2));
        contentValues.put(KEY_MONTH, Integer.valueOf(i3));
        contentValues.put(KEY_YEAR, Integer.valueOf(i4));
        contentValues.put(KEY_HOUR, Integer.valueOf(i5));
        contentValues.put(KEY_MEDICINE_NAME, trim4);
        contentValues.put(KEY_MEASURE_UNIT, trim5);
        contentValues.put(KEY_DOSAGE, trim6);
        contentValues.put(KEY_TIMES_DAY, trim7);
        contentValues.put(KEY_NOTES, trim8);
        return sqLiteDatabase.insertWithOnConflict(MEDICINE_TABLE, null, contentValues, 4);
    }

    public long UpdateMedicineData(int i, int i2, MedicineData medicineData) {
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        String trim = medicineData.dateTime.trim();
        String trim2 = medicineData.date.trim();
        String trim3 = medicineData.time.trim();
        int i3 = medicineData.day;
        int i4 = medicineData.month;
        int i5 = medicineData.year;
        int i6 = medicineData.hour;
        String trim4 = medicineData.medicine_name.trim();
        String trim5 = medicineData.measure_unit.trim();
        String trim6 = medicineData.dosage.trim();
        String trim7 = medicineData.times_day.trim();
        String trim8 = medicineData.notes.trim();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USER_ID, Integer.valueOf(i2));
        contentValues.put(KEY_DATE_TIME, trim);
        contentValues.put(KEY_DATE, trim2);
        contentValues.put(KEY_TIME, trim3);
        contentValues.put(KEY_DAY, Integer.valueOf(i3));
        contentValues.put(KEY_MONTH, Integer.valueOf(i4));
        contentValues.put(KEY_YEAR, Integer.valueOf(i5));
        contentValues.put(KEY_HOUR, Integer.valueOf(i6));
        contentValues.put(KEY_MEDICINE_NAME, trim4);
        contentValues.put(KEY_MEASURE_UNIT, trim5);
        contentValues.put(KEY_DOSAGE, trim6);
        contentValues.put(KEY_TIMES_DAY, trim7);
        contentValues.put(KEY_NOTES, trim8);
        return sqLiteDatabase.update(MEDICINE_TABLE, contentValues, "row_id=" + i, null);
    }

    public List GetMedicinesData() {
        Exception e;
        ArrayList arrayList = new ArrayList();
        try {
            SQLiteDatabase readableDatabase = sqLiteHelper.getReadableDatabase();
            sqLiteDatabase = readableDatabase;
            Cursor rawQuery = readableDatabase.rawQuery("SELECT * FROM medicine_data ORDER BY row_id DESC", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    while (true) {
                        int i = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        int i2 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i3 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_MEDICINE_NAME));
                        String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_MEASURE_UNIT));
                        String string6 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DOSAGE));
                        String string7 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIMES_DAY));
                        try {
                            String string8 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_NOTES));
                            MedicineData medicineData = new MedicineData();
                            medicineData.row_id = i;
                            medicineData.user_id = i2;
                            medicineData.dateTime = string.trim();
                            medicineData.date = string2.trim();
                            medicineData.time = string3.trim();
                            medicineData.day = i3;
                            medicineData.month = i4;
                            medicineData.year = i5;
                            medicineData.hour = i6;
                            medicineData.medicine_name = string4.trim();
                            medicineData.measure_unit = string5.trim();
                            medicineData.dosage = string6.trim();
                            medicineData.times_day = string7.trim();
                            medicineData.notes = string8.trim();
                            try {
                                Date parse = new SimpleDateFormat(this.date_time_format).parse(string);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                                SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                                medicineData.date = simpleDateFormat.format(parse).trim();
                                medicineData.time = simpleDateFormat3.format(parse).trim();
                                medicineData.month_string = simpleDateFormat2.format(parse).trim();
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                            arrayList = arrayList;
                            arrayList.add(medicineData);
                            if (!rawQuery.moveToNext()) {
                                break;
                            }
                            rawQuery = rawQuery;
                        } catch (Exception e3) {
                            e = e3;
                            arrayList = arrayList;
                            e.printStackTrace();
                            return arrayList;
                        }
                    }
                }
            }
        } catch (Exception e4) {
            e = e4;
            e.printStackTrace();
            return arrayList;
        }
        return arrayList;
    }

    public List GetMedicinesByUserID(int i) {
        ArrayList arrayList;
        Exception e;
        String str = KEY_ROW_ID;
        ArrayList arrayList2 = new ArrayList();
        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT * FROM medicine_data WHERE user_id=" + i + " ORDER BY " + str + " DESC", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                    return arrayList2;
                } else if (rawQuery.moveToFirst()) {
                    while (true) {
                        int i2 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(str));
                        int i3 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_USER_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIME));
                        int i4 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_DAY));
                        int i5 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_MONTH));
                        int i6 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_YEAR));
                        int i7 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_HOUR));
                        String string4 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_MEDICINE_NAME));
                        String string5 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_MEASURE_UNIT));
                        String string6 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DOSAGE));
                        String string7 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_TIMES_DAY));
                        try {
                            String string8 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_NOTES));
                            MedicineData medicineData = new MedicineData();
                            medicineData.row_id = i2;
                            medicineData.user_id = i3;
                            medicineData.date = string2.trim();
                            medicineData.time = string3.trim();
                            medicineData.day = i4;
                            medicineData.month = i5;
                            medicineData.year = i6;
                            medicineData.hour = i7;
                            medicineData.medicine_name = string4.trim();
                            medicineData.measure_unit = string5.trim();
                            medicineData.dosage = string6.trim();
                            medicineData.times_day = string7.trim();
                            medicineData.notes = string8.trim();
                            try {
                                Date parse = new SimpleDateFormat(this.date_time_format).parse(string);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format);
                                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(this.month_format);
                                SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(this.time_format);
                                medicineData.date = simpleDateFormat.format(parse).trim();
                                medicineData.time = simpleDateFormat3.format(parse).trim();
                                medicineData.month_string = simpleDateFormat2.format(parse).trim();
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                            arrayList = arrayList2;
                            try {
                                arrayList.add(medicineData);
                                if (!rawQuery.moveToNext()) {
                                    return arrayList;
                                }
                                rawQuery = rawQuery;
                                arrayList2 = arrayList;
                                str = str;
                            } catch (Exception e3) {
                                e = e3;
                                e.printStackTrace();
                                return arrayList;
                            }
                        } catch (Exception e4) {
                            e = e4;
                            arrayList = arrayList2;
                            e.printStackTrace();
                            return arrayList;
                        }
                    }
                }
            }
            return arrayList2;
        } catch (Exception e5) {
            e = e5;
            arrayList = arrayList2;
            e.printStackTrace();
            return arrayList;
        }
    }

    public String GetLastMedicineId() {
        SQLiteDatabase readableDatabase = sqLiteHelper.getReadableDatabase();
        sqLiteDatabase = readableDatabase;
        Cursor query = readableDatabase.query(MEDICINE_TABLE, null, null, null, null, null, null);
        String string = query.moveToLast() ? query.getString(query.getColumnIndexOrThrow(KEY_ROW_ID)) : "";
        query.close();
        sqLiteDatabase.close();
        return string;
    }

    public void deleteMedicineByID(int i) {
        SQLiteDatabase sQLiteDatabase = sqLiteDatabase;
        sQLiteDatabase.delete(MEDICINE_TABLE, "row_id=" + i, null);
    }

    public void deleteMedicinesByUserID(int i) {
        SQLiteDatabase sQLiteDatabase = sqLiteDatabase;
        sQLiteDatabase.delete(MEDICINE_TABLE, "user_id=" + i, null);
    }

    public void deleteAllMedicineData() {
        sqLiteDatabase.execSQL("DELETE FROM medicine_data");
    }

    public class SQLiteHelper extends SQLiteOpenHelper {
        public SQLiteHelper(Context context, String str, SQLiteDatabase.CursorFactory cursorFactory, int i) {
            super(context, str, cursorFactory, i);
        }

        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        }

        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            sQLiteDatabase.execSQL(SQLiteHealthTracker.CREATE_USER_PROFILE_TABLE);
            sQLiteDatabase.execSQL(SQLiteHealthTracker.CREATE_WEIGHT_DATA_TABLE);
            sQLiteDatabase.execSQL(SQLiteHealthTracker.CREATE_BLOOD_SUGAR_TABLE);
            sQLiteDatabase.execSQL(SQLiteHealthTracker.CREATE_BLOOD_PRESSURE_TABLE);
            sQLiteDatabase.execSQL(SQLiteHealthTracker.CREATE_HEART_RATE_TABLE);
            sQLiteDatabase.execSQL(SQLiteHealthTracker.CREATE_BMI_TABLE);
            sQLiteDatabase.execSQL(SQLiteHealthTracker.CREATE_ALL_MEDICINE_TABLE);
            sQLiteDatabase.execSQL(SQLiteHealthTracker.CREATE_MEDICINE_TABLE);
            sQLiteDatabase.execSQL(SQLiteHealthTracker.CREATE_BODY_TEMP_DATA_TABLE);
            sQLiteDatabase.execSQL(SQLiteHealthTracker.CREATE_CHOLESTEROL_TABLE);
            sQLiteDatabase.execSQL(SQLiteHealthTracker.CREATE_BLOOD_COUNT_TABLE);
        }

        public void onOpen(SQLiteDatabase sQLiteDatabase) {
            super.onOpen(sQLiteDatabase);
            sQLiteDatabase.disableWriteAheadLogging();
        }
    }
}
