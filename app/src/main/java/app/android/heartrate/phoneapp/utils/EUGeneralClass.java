package app.android.heartrate.phoneapp.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Process;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.PrintStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import app.android.heartrate.phoneapp.R;
import kotlin.UByte;

public class EUGeneralClass {
    private static final String TAG = "EUGeneralClass: ";
    public static String current_date;
    public static String current_date_time;
    public static Date current_datetime;
    public static String current_time;

    public static SimpleDateFormat f283df;
    public static boolean is_online;
    public static SimpleDateFormat sdf;
    public static SimpleDateFormat sdt;
    private static Context mContext;

    public EUGeneralClass(Context context) {
        mContext = context;
    }

    public static void RateApp(Context context) {
        try {
            mContext = context;
            ConformRateDialog(mContext, EUGeneralHelper.rate_url + mContext.getPackageName(), "Rate App", "If you enjoy this app, would you mind taking a moment to rate it?\nThanks for your support!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void ShareApp(Context context) {
        try {
            mContext = context;
            String str = mContext.getResources().getString(R.string.app_name) + " :";
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.putExtra("android.intent.extra.SUBJECT", str);
            intent.putExtra("android.intent.extra.TEXT", str + "\n" + (EUGeneralHelper.rate_url + mContext.getPackageName()));
            mContext.startActivity(Intent.createChooser(intent, "Share via"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean z = true;
        if (Build.VERSION.SDK_INT >= 23) {
            Network activeNetwork = connectivityManager.getActiveNetwork();
            if (activeNetwork == null) {
                return false;
            }
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
            if (networkCapabilities == null || (!networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) && !networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) && !networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) && !networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH))) {
                z = false;
            }
            return Boolean.valueOf(z);
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            z = false;
        }
        return Boolean.valueOf(z);
    }

    public static String GetCurrentDateTime() {
        try {
            Calendar instance = Calendar.getInstance();
            PrintStream printStream = System.out;
            printStream.println("Current time => " + instance.getTime());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm aa");
            f283df = simpleDateFormat;
            String format = simpleDateFormat.format(instance.getTime());
            current_date_time = format;
            current_datetime = f283df.parse(format);
            sdf = new SimpleDateFormat("dd-MMM-yyyy");
            sdt = new SimpleDateFormat("HH:mm aa");
            current_date = sdf.format(current_datetime);
            current_time = sdt.format(current_datetime);
            current_time = (String) DateFormat.format("hh:mm aaa", Calendar.getInstance().getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return current_date_time;
    }

    public static void ShowSuccessToast(Context context, String str) {
        MDToast.makeText(context, str, MDToast.LENGTH_SHORT, 1).show();
    }

    public static void ShowInfoToast(Context context, String str) {
        MDToast.makeText(context, str, MDToast.LENGTH_SHORT, 0).show();
    }

    public static void ShowWarningToast(Context context, String str) {
        MDToast.makeText(context, str, MDToast.LENGTH_SHORT, 2).show();
    }

    public static void ShowErrorToast(Context context, String str) {
        MDToast.makeText(context, str, MDToast.LENGTH_SHORT, 3).show();
    }

    public static void ConformRateDialog(final Context context, final String str, String str2, String str3) {
        final Dialog dialog = new Dialog(context, R.style.TransparentBackground);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_rate);
        Button button = dialog.findViewById(R.id.dialog_conform_btn_yes);
        Button button2 = dialog.findViewById(R.id.dialog_conform_btn_no);
        ((TextView) dialog.findViewById(R.id.dialog_conform_txt_header)).setText(str2);
        ((TextView) dialog.findViewById(R.id.dialog_conform_txt_message)).setText(str3);
        button.setText("Rate now");
        button2.setText("Cancel");
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                try {
                    context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public static final String md5(String str) {
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(str.getBytes());
            byte[] digest = instance.digest();
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b : digest) {
                String hexString = Integer.toHexString(b & UByte.MAX_VALUE);
                while (hexString.length() < 2) {
                    hexString = "0" + hexString;
                }
                stringBuffer.append(hexString);
            }
            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void ExitDialog(Context context, final Activity activity) {
        final Dialog dialog = new Dialog(context, R.style.TransparentBackground);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_exit);
        ((TextView) dialog.findViewById(R.id.dialog_exit_txt_header)).setText("Confirm Exit");
        ((TextView) dialog.findViewById(R.id.dialog_exit_txt_message)).setText("Are you sure you want to exit from application?\nThank You for Using Our Application!");
        ((TextView) dialog.findViewById(R.id.dialog_exit_txt_continue)).setText("Exit");
        ((TextView) dialog.findViewById(R.id.dialog_exit_txt_cancel)).setText("Cancel");
        dialog.findViewById(R.id.dialog_exit_rel_continue).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                EUGeneralClass.FinishApp(activity);
            }
        });
        dialog.findViewById(R.id.dialog_exit_rel_cancel).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void ExitApp(Activity activity) {
        activity.finishAndRemoveTask();
        Process.killProcess(Process.myPid());
        System.exit(0);
    }

    public static void FinishApp(Activity activity) {
        activity.finishAffinity();
    }
}
