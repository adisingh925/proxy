package app.android.heartrate.phoneapp;

import android.app.Application;
import android.content.Context;

import app.android.heartrate.phoneapp.sharedpreferences.SharedPreferences;

public class App extends Application {
    private static App sApp;

    /**
     * Access to the singleton
     *
     * @return App
     */
    public static App getInstance() {
        return sApp;
    }

    @Override
    public void onCreate() {
        sApp = this;
        super.onCreate();
        //Initialize the module
        SharedPreferences.INSTANCE.init(this);

    }


    public static Context getAppContext() {
        return getInstance();
    }

}
