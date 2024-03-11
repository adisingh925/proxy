package app.android.heartrate.phoneapp.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import app.android.heartrate.phoneapp.model.classes.WaterReminderData;
import app.android.heartrate.phoneapp.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

public class SQLiteWaterReminder {
    private static final String CREATE_USER_PROFILE_TABLE = "CREATE TABLE water_states (row_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, date_time TEXT UNIQUE,intook INTEGER,total_intake INTEGER);";
    private static final int DATABASE_VERSION = 1;
    private static final String KEY_DATE_TIME = "date_time";
    private static final String KEY_INTOOK = "intook";
    private static final String KEY_ROW_ID = "row_id";
    private static final String KEY_TOTAL_INTAKE = "total_intake";
    private static final String STATES_TABLE = "water_states";
    private static SQLiteDatabase sqLiteDatabase;
    private static SQLiteOpenHelper sqLiteHelper;
    private final Context mContext;

    public SQLiteWaterReminder(Context context) {
        this.mContext = context;
    }

    public SQLiteWaterReminder open() throws SQLException {
        SQLiteHelper sQLiteHelper = new SQLiteHelper(this.mContext, AppConstants.WATER_REMINDER_DB_NAME, null, 1);
        sqLiteHelper = sQLiteHelper;
        sqLiteDatabase = sQLiteHelper.getWritableDatabase();
        return this;
    }

    public SQLiteWaterReminder openToRead() throws SQLException {
        SQLiteHelper sQLiteHelper = new SQLiteHelper(this.mContext, AppConstants.WATER_REMINDER_DB_NAME, null, 1);
        sqLiteHelper = sQLiteHelper;
        sqLiteDatabase = sQLiteHelper.getReadableDatabase();
        return this;
    }

    public SQLiteWaterReminder openToWrite() throws SQLException {
        SQLiteHelper sQLiteHelper = new SQLiteHelper(this.mContext, AppConstants.WATER_REMINDER_DB_NAME, null, 1);
        sqLiteHelper = sQLiteHelper;
        sqLiteDatabase = sQLiteHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        sqLiteHelper.close();
    }

    public class SQLiteHelper extends SQLiteOpenHelper {
        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        }

        public SQLiteHelper(Context context, String str, SQLiteDatabase.CursorFactory cursorFactory, int i) {
            super(context, str, cursorFactory, i);
        }

        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            sQLiteDatabase.execSQL(SQLiteWaterReminder.CREATE_USER_PROFILE_TABLE);
        }

        public void onOpen(SQLiteDatabase sQLiteDatabase) {
            super.onOpen(sQLiteDatabase);
            sQLiteDatabase.disableWriteAheadLogging();
        }
    }

    public long InsertWaterStateData(WaterReminderData waterReminderData) {
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        String trim = waterReminderData.taken_date_time.trim();
        int i = waterReminderData.water_intook;
        int i2 = waterReminderData.water_intake;
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_DATE_TIME, trim);
        contentValues.put(KEY_INTOOK, Integer.valueOf(i));
        contentValues.put(KEY_TOTAL_INTAKE, Integer.valueOf(i2));
        return sqLiteDatabase.insertWithOnConflict(STATES_TABLE, null, contentValues, 4);
    }

    public long InsertWaterIntook(String str, int i) {
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        int GetIntook = GetIntook(str);
        String str2 = "'" + str + "'";
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_INTOOK, Integer.valueOf(GetIntook + i));
        return sqLiteDatabase.update(STATES_TABLE, contentValues, "date_time = " + str2, null);
    }

    public long UpdateTotalIntake(String str, int i) {
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TOTAL_INTAKE, Integer.valueOf(i));
        return sqLiteDatabase.update(STATES_TABLE, contentValues, "date_time= ?", null);
    }

    public int GetIntook(String str) {
        int i;
        sqLiteDatabase = sqLiteHelper.getReadableDatabase();
        Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT intook FROM water_states WHERE date_time = " + ("'" + str + "'"), null);
        if (rawQuery != null) {
            if (rawQuery.getCount() <= 0) {
                Log.e("Cursor ::", "Cursor null...");
            } else if (rawQuery.moveToFirst()) {
                do {
                    i = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_INTOOK));
                } while (rawQuery.moveToNext());
                return i;
            }
        }
        return 0;
    }

    public int CheckIntookExist(String str) {
        int i;
        sqLiteDatabase = sqLiteHelper.getReadableDatabase();
        Cursor rawQuery = sqLiteDatabase.rawQuery("SELECT intook FROM water_states WHERE date_time = " + ("'" + str + "'"), null);
        if (rawQuery != null) {
            if (rawQuery.getCount() <= 0) {
                Log.e("Cursor ::", "Cursor null...");
            } else if (rawQuery.moveToFirst()) {
                do {
                    i = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_INTOOK));
                } while (rawQuery.moveToNext());
                return i;
            }
        }
        return 0;
    }

    public List GetAllStatesData() {
        ArrayList arrayList = new ArrayList();
        try {
            SQLiteDatabase readableDatabase = sqLiteHelper.getReadableDatabase();
            sqLiteDatabase = readableDatabase;
            Cursor rawQuery = readableDatabase.rawQuery("SELECT * FROM water_states ORDER BY row_id DESC", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() <= 0) {
                    Log.e("Cursor ::", "Cursor null...");
                } else if (rawQuery.moveToFirst()) {
                    do {
                        int i = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_ROW_ID));
                        String string = rawQuery.getString(rawQuery.getColumnIndexOrThrow(KEY_DATE_TIME));
                        int i2 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_INTOOK));
                        int i3 = rawQuery.getInt(rawQuery.getColumnIndexOrThrow(KEY_TOTAL_INTAKE));
                        WaterReminderData waterReminderData = new WaterReminderData();
                        waterReminderData.row_id = i;
                        waterReminderData.taken_date_time = string.trim();
                        waterReminderData.water_intook = i2;
                        waterReminderData.water_intake = i3;
                        arrayList.add(waterReminderData);
                    } while (rawQuery.moveToNext());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public boolean CheckSatesDataExist() {
        SQLiteDatabase readableDatabase = sqLiteHelper.getReadableDatabase();
        sqLiteDatabase = readableDatabase;
        Cursor rawQuery = readableDatabase.rawQuery("SELECT * FROM water_states", null);
        return rawQuery != null && rawQuery.getCount() != 0;
    }
}
