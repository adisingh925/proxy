package app.android.heartrate.phoneapp.sharedpreferences

import android.content.Context
import android.preference.PreferenceManager
import app.android.heartrate.phoneapp.model.ProfileData
import app.android.heartrate.phoneapp.model.classes.UserProfileData
import com.google.gson.Gson

object SharedPreferences {

    private lateinit var prefs: android.content.SharedPreferences

    fun init(context: Context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun read(key: String, value: String): String? {
        return prefs.getString(key, value)
    }

    fun read(key: String, value: Int): Int {
        return prefs.getInt(key, value)
    }

    fun write(key: String, value: String) {
        val prefsEditor: android.content.SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putString(key, value)
            apply()
        }
    }

    fun write(key: String, value: Int) {
        val prefsEditor: android.content.SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putInt(key, value)
            apply()
        }
    }

    fun deleteAll() {
        val prefsEditor: android.content.SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            clear()
            apply()
        }
    }

    fun getUserProfile(): ProfileData? {
        val strProfile = read("profile", "")
        if (strProfile.isNullOrEmpty()) {
            return null
        }
        return Gson().fromJson(strProfile, ProfileData::class.java)
    }

    fun setUserProfile(profileData: ProfileData) {
        Gson().toJson(profileData).let {
            write("profile", it)
        }
    }
}