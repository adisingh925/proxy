package app.android.heartrate.phoneapp.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

object CommonUtils {

    fun setupActionBar(toolbar: Toolbar, activity: AppCompatActivity, title: String, displayHomeAsUpEnabled: Boolean = true) {
        activity.setSupportActionBar(toolbar)
        val actionBar: androidx.appcompat.app.ActionBar? = activity.supportActionBar
        actionBar?.apply {
            setTitle(title)
            setDisplayHomeAsUpEnabled(displayHomeAsUpEnabled)
        }
    }
}