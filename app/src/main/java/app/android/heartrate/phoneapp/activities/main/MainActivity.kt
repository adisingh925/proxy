package app.android.heartrate.phoneapp.activities.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.android.heartrate.phoneapp.R
import app.android.heartrate.phoneapp.sharedpreferences.SharedPreferences

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SharedPreferences.init(this)
    }
}