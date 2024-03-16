package app.android.heartrate.phoneapp.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AppUtils {
    public static String FIRST_RUN_KEY = "firstrun";
    public static String NOTIFICATION_FREQUENCY_KEY = "notificationfrequency";
    public static String NOTIFICATION_MSG_KEY = "notificationmsg";
    public static String NOTIFICATION_STATUS_KEY = "notificationstatus";
    public static String NOTIFICATION_TONE_URI_KEY = "notificationtone";
    public static int PRIVATE_MODE = 0;
    public static String SLEEPING_TIME_KEY = "sleepingtime";
    public static String TOTAL_INTAKE = "totalintake";
    public static String USERS_SHARED_PREF = "user_pref";
    public static String WAKEUP_TIME = "wakeuptime";
    public static String WEIGHT_KEY = "weight";
    public static String WORK_TIME_KEY = "worktime";

    public static String getCurrentDate() {
        return new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
    }

    public static double calculateIntake(int i, int i2) {
        return (((double) (i * 100)) / 3.0d) + ((double) ((i2 / 6) * 7));
    }
}
