package app.android.heartrate.phoneapp.activities.record.medicine

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.text.format.DateFormat
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import app.android.heartrate.phoneapp.R
import app.android.heartrate.phoneapp.adapters.SpinnerProfileAdapter
import app.android.heartrate.phoneapp.model.classes.MedicineData
import app.android.heartrate.phoneapp.model.classes.MedicineNameData
import app.android.heartrate.phoneapp.sharedpreferences.SharedPreferences
import app.android.heartrate.phoneapp.sqlite.SQLiteHealthTracker
import app.android.heartrate.phoneapp.utils.AppConstants
import app.android.heartrate.phoneapp.utils.EUGeneralClass
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar

class AddMedicinesActivity() : AppCompatActivity() {
    var SQLite_health_tracker: SQLiteHealthTracker? = null
    var arrayListUnitOfMeasure: ArrayList<String> = ArrayList(
        mutableListOf(
            "mg",
            "tablet",
            "unit",
            "g",
            "mcg",
            "ml",
            "pill",
            "drop",
            "capsule"
        )
    )
    var date_time: String = ""
    var day: Int = 0
    var et_medicine_dosage: EditText? = null
    var et_medicine_name: EditText? = null
    var et_notes: EditText? = null
    var et_times_a_day: EditText? = null
    var hour: Int = 0
    var mContext: Context? = null
    var month: Int = 0
    var push_animation: Animation? = null
    private lateinit var rel_select_date: RelativeLayout
    private lateinit var rel_select_time: RelativeLayout

    private var sharedPreferencesUtils: SharedPreferences? = null
    var spinner_profile_adapter: SpinnerProfileAdapter? = null
    var spinner_profiles: Spinner? = null
    private lateinit var spinner_unit_measure: Spinner
    private lateinit var txt_date: TextView
    var spinner_txt_name: TextView? = null
    var txt_dosage_unit_title: TextView? = null
    private lateinit var txt_time: TextView
    var unit_measure_spinner_adapter: ArrayAdapter<String>? = null
    var year: Int = 0
    private var current_date_time = ""
    private var save_entry_day = 0
    private var save_entry_hour = 0
    private var save_entry_minute = 0
    private var save_entry_month = 0
    private var save_entry_year = 0

    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        SetView()


        AppConstants.overridePendingTransitionEnter(this)
    }

    private fun SetView() {
        setContentView(R.layout.activity_add_medicine)

        this.mContext = this
        activity_add_medicine = this
        this.push_animation = AnimationUtils.loadAnimation(this, R.anim.view_push)
        setUpActionBar()
        sharedPreferencesUtils = SharedPreferences
        val sQLiteHealthTracker = SQLiteHealthTracker(this)
        this.SQLite_health_tracker = sQLiteHealthTracker
        sQLiteHealthTracker.openToWrite()
        this.spinner_profiles = findViewById(R.id.add_medicine_spinner_profiles)
        this.rel_select_date = findViewById(R.id.add_medicine_rel_select_date)
        this.txt_date = findViewById(R.id.add_medicine_txt_date)
        this.spinner_txt_name = findViewById(R.id.spinner_txt_name)
        this.rel_select_time = findViewById(R.id.add_medicine_rel_select_time)
        this.txt_time = findViewById(R.id.add_medicine_txt_time)
        this.spinner_unit_measure = findViewById(R.id.add_medicine_spinner_unit_measure)
        this.txt_dosage_unit_title = findViewById(R.id.add_medicine_txt_dosage_unit)
        this.et_medicine_name = findViewById(R.id.add_medicine_et_medicine_name)
        this.et_medicine_dosage = findViewById(R.id.add_medicine_et_dosage)
        this.et_times_a_day = findViewById(R.id.add_medicine_et_times_a_day)
        this.et_notes = findViewById(R.id.add_medicine_et_notes)
        val arrayAdapter = ArrayAdapter(this, R.layout.spinner_list, this.arrayListUnitOfMeasure)
        this.unit_measure_spinner_adapter = arrayAdapter
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list)
        spinner_unit_measure.setAdapter(this.unit_measure_spinner_adapter)
        val textView = this.txt_dosage_unit_title
        textView!!.text = "Dosage (" + spinner_unit_measure.getSelectedItem().toString() + ") :"
        SetProfileSpinner()
        SetCurrentDateTime()
        if (AppConstants.is_medicine_edit_mode) {
            SetMedicineData()
        }
        rel_select_date.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                this@AddMedicinesActivity.hideSoftKeyboard()
                val instance = Calendar.getInstance()
                this@AddMedicinesActivity.save_entry_year = instance[1]
                this@AddMedicinesActivity.save_entry_month = instance[2]
                this@AddMedicinesActivity.save_entry_day = instance[5]
                DatePickerDialog(
                    this@AddMedicinesActivity,
                    R.style.DialogTheme,
                    OnDateSetListener { datePicker, i, i2, i3 ->
                        try {
                            txt_date.setText(
                                SimpleDateFormat("dd/MM/yyyy").format(
                                    SimpleDateFormat("dd/MM/yyyy").parse(
                                        (i3.toString() + "/" + (i2 + 1) + "/" + i).trim { it <= ' ' })
                                )
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    },
                    this@AddMedicinesActivity.save_entry_year,
                    this@AddMedicinesActivity.save_entry_month,
                    this@AddMedicinesActivity.save_entry_day
                ).show()
            }
        })
        rel_select_time.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val instance = Calendar.getInstance()
                this@AddMedicinesActivity.save_entry_hour = instance[11]
                this@AddMedicinesActivity.save_entry_minute = instance[12]
                TimePickerDialog(
                    this@AddMedicinesActivity,
                    R.style.DialogTheme,
                    object : OnTimeSetListener {
                        override fun onTimeSet(timePicker: TimePicker, i: Int, i2: Int) {
                            try {
                                txt_time.setText(
                                    SimpleDateFormat("hh:mm aa").format(
                                        SimpleDateFormat("hh:mm").parse(
                                            "$i:$i2"
                                        )
                                    )
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    },
                    this@AddMedicinesActivity.save_entry_hour,
                    this@AddMedicinesActivity.save_entry_minute,
                    false
                ).show()
            }
        })
    }

    private fun SetProfileSpinner() {
        val name = sharedPreferencesUtils!!.getUserName()
        spinner_txt_name!!.text = name
    }

    @SuppressLint("WrongConstant")
    fun hideSoftKeyboard() {
        val currentFocus = currentFocus
        if (currentFocus != null) {
            (getSystemService("input_method") as InputMethodManager).hideSoftInputFromWindow(
                currentFocus.windowToken,
                0
            )
        }
    }

    private fun SetMedicineData() {
        if (AppConstants.selected_medicine_data != null) {
            val medicineData = AppConstants.selected_medicine_data
            val trim = medicineData.date.trim { it <= ' ' }
            val trim2 = medicineData.time.trim { it <= ' ' }
            val trim3 = medicineData.medicine_name.trim { it <= ' ' }
            val trim4 = medicineData.dosage.trim { it <= ' ' }
            val trim5 = medicineData.measure_unit.trim { it <= ' ' }
            val trim6 = medicineData.times_day.trim { it <= ' ' }
            val trim7 = medicineData.notes.trim { it <= ' ' }
            if ((trim5 == "mg")) {
                spinner_unit_measure!!.setSelection(0)
            } else if ((trim5 == "tablet")) {
                spinner_unit_measure!!.setSelection(1)
            } else if ((trim5 == "unit")) {
                spinner_unit_measure!!.setSelection(2)
            } else if ((trim5 == "g")) {
                spinner_unit_measure!!.setSelection(3)
            } else if ((trim5 == "mcg")) {
                spinner_unit_measure!!.setSelection(4)
            } else if ((trim5 == "ml")) {
                spinner_unit_measure!!.setSelection(5)
            } else if ((trim5 == "pill")) {
                spinner_unit_measure!!.setSelection(6)
            } else if ((trim5 == "drop")) {
                spinner_unit_measure!!.setSelection(7)
            } else if ((trim5 == "capsule")) {
                spinner_unit_measure!!.setSelection(8)
            }
            txt_date!!.text = trim
            txt_time!!.text = trim2
            et_medicine_name!!.setText(trim3)
            et_medicine_dosage!!.setText(trim4)
            et_times_a_day!!.setText(trim6)
            et_notes!!.setText(trim7)
            return
        }
        EUGeneralClass.ShowErrorToast(this, "Something went wrong!")
    }


    private fun SaveProcess() {
        var z = false
        var editText: EditText? = null
        try {
            val trim = txt_date!!.text.toString().trim { it <= ' ' }
            val trim2 = txt_time!!.text.toString().trim { it <= ' ' }
            val trim3 = et_medicine_name!!.text.toString().trim { it <= ' ' }
            val trim4 =
                et_medicine_dosage!!.text.toString().trim { it <= ' ' }
            val obj = spinner_unit_measure!!.selectedItem.toString()
            val trim5 = et_times_a_day!!.text.toString().trim { it <= ' ' }
            val trim6 = et_notes!!.text.toString().trim { it <= ' ' }
            var z2 = true
            if (TextUtils.isEmpty(trim3)) {
                et_medicine_name!!.error = AppConstants.error_field_require
                editText = this.et_medicine_name
                z = true
            }
            if (TextUtils.isEmpty(trim4)) {
                et_medicine_dosage!!.error = AppConstants.error_field_require
                editText = this.et_medicine_dosage
                z = true
            }
            if (TextUtils.isEmpty(trim5)) {
                et_times_a_day!!.error = AppConstants.error_field_require
                editText = this.et_times_a_day
            } else {
                z2 = z
            }
            if (z2) {
                editText!!.requestFocus()
                return
            }
            val medicineData = MedicineData()
            if (!AppConstants.is_medicine_edit_mode) {
                medicineData.user_id = sharedPreferencesUtils!!.getUserId()
                medicineData.date = trim.trim { it <= ' ' }
                medicineData.time = trim2.trim { it <= ' ' }
                medicineData.medicine_name = trim3.trim { it <= ' ' }
                medicineData.measure_unit = obj.trim { it <= ' ' }
                medicineData.dosage = trim4.trim { it <= ' ' }
                medicineData.times_day = trim5.trim { it <= ' ' }
                medicineData.notes = trim6.trim { it <= ' ' }
                GetDateTime(trim.trim { it <= ' ' }, trim2.trim { it <= ' ' })
                medicineData.dateTime = this.date_time
                medicineData.day = this.day
                medicineData.month = this.month
                medicineData.year = this.year
                medicineData.hour = this.hour
                SQLite_health_tracker!!.InsertMedicineData(medicineData)
                if (!SQLite_health_tracker!!.CheckMedicineNameExist(trim3)) {
                    val medicineNameData = MedicineNameData()
                    medicineNameData.medicine_id = SQLite_health_tracker!!.GetLastMedicineId()
                    medicineNameData.medicine_name = trim3.trim { it <= ' ' }
                    SQLite_health_tracker!!.InsertAllMedicine(medicineNameData)
                }
                EUGeneralClass.ShowSuccessToast(this, "Medicine Data saved successfully!")
                onBackPressed()
                return
            }
            val i = AppConstants.selected_medicine_data.row_id
            medicineData.row_id = i
            medicineData.user_id = sharedPreferencesUtils!!.getUserId()
            medicineData.date = trim.trim { it <= ' ' }
            medicineData.time = trim2.trim { it <= ' ' }
            medicineData.medicine_name = trim3.trim { it <= ' ' }
            medicineData.measure_unit = obj.trim { it <= ' ' }
            medicineData.dosage = trim4.trim { it <= ' ' }
            medicineData.times_day = trim5.trim { it <= ' ' }
            medicineData.notes = trim6.trim { it <= ' ' }
            GetDateTime(trim.trim { it <= ' ' }, trim2.trim { it <= ' ' })
            medicineData.dateTime = this.date_time
            medicineData.day = this.day
            medicineData.month = this.month
            medicineData.year = this.year
            medicineData.hour = this.hour
            SQLite_health_tracker!!.UpdateMedicineData(
                i,
                sharedPreferencesUtils!!.getUserId(), medicineData
            )
            if (!SQLite_health_tracker!!.CheckMedicineNameExist(trim3)) {
                val medicineNameData2 = MedicineNameData()
                medicineNameData2.medicine_id = SQLite_health_tracker!!.GetLastMedicineId()
                medicineNameData2.medicine_name = trim3.trim { it <= ' ' }
                SQLite_health_tracker!!.InsertAllMedicine(medicineNameData2)
            }
            EUGeneralClass.ShowSuccessToast(this, "Medicine Data updated successfully!")
            onBackPressed()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun SetCurrentDateTime() {
        try {
            val instance = Calendar.getInstance()
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa")
            val format = simpleDateFormat.format(instance.time)
            this.current_date_time = format
            val parse = simpleDateFormat.parse(format)
            val simpleDateFormat2 = SimpleDateFormat("dd/MM/yyyy")
            val simpleDateFormat3 = SimpleDateFormat("hh:mm aa")
            val format2 = simpleDateFormat2.format(parse)
            simpleDateFormat3.format(parse)
            Log.e("Current Date Time:", this.current_date_time)
            txt_date!!.text = format2
            txt_time!!.text = DateFormat.format("hh:mm aaa", Calendar.getInstance().time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    private fun GetDateTime(str: String, str2: String) {
        try {
            val str3 = "$str $str2"
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
            val simpleDateFormat2 = SimpleDateFormat("dd")
            val simpleDateFormat3 = SimpleDateFormat("MM")
            val simpleDateFormat4 = SimpleDateFormat("yyyy")
            val simpleDateFormat5 = SimpleDateFormat("hh:mm aa")
            val simpleDateFormat6 = SimpleDateFormat("hh")
            val parse = SimpleDateFormat("dd/MM/yyyy hh:mm aa").parse(str3)
            this.date_time = simpleDateFormat.format(parse) + " " + simpleDateFormat5.format(parse)
            this.day = simpleDateFormat2.format(parse).toInt()
            this.month = simpleDateFormat3.format(parse).toInt()
            this.year = simpleDateFormat4.format(parse).toInt()
            this.hour = simpleDateFormat6.format(parse).toInt()
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    private fun setUpActionBar() {
        (findViewById<View>(R.id.toolbar_txt_title_1) as TextView).text =
            resources.getString(R.string.lbl_header_add)
        (findViewById<View>(R.id.toolbar_txt_title_2) as TextView).text =
            resources.getString(R.string.lbl_header_medicine)
        findViewById<View>(R.id.toolbar_rel_save).setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                this@AddMedicinesActivity.SaveProcess()
            }
        })
        findViewById<View>(R.id.toolbar_rel_back).setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                this@AddMedicinesActivity.onBackPressed()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.blank_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == 16908332) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(menuItem)
    }


    public override fun onResume() {
        super.onResume()
    }


    override fun onBackPressed() {
        super.onBackPressed()
        BackScreen()
    }

    private fun BackScreen() {
        finish()
        AppConstants.overridePendingTransitionExit(this)
    }

    companion object {
        var activity_add_medicine: Activity? = null
    }
}
