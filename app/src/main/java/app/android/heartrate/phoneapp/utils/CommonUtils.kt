package app.android.heartrate.phoneapp.utils

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

object CommonUtils {

    fun setupActionBar(
        toolbar: Toolbar,
        activity: Activity,
        title: String,
        displayHomeAsUpEnabled: Boolean = true
    ) {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        val actionBar: androidx.appcompat.app.ActionBar? = activity.supportActionBar
        actionBar?.apply {
            setTitle(title)
            setDisplayHomeAsUpEnabled(displayHomeAsUpEnabled)
        }
    }

    fun updateToolbarTitle(title: String, activity: Activity) {
        val actionBar: androidx.appcompat.app.ActionBar? =
            (activity as AppCompatActivity).supportActionBar
        actionBar?.apply {
            setTitle(title)
        }
    }

}