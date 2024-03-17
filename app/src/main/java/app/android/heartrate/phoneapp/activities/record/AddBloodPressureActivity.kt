package app.android.heartrate.phoneapp.activities.record

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import app.android.heartrate.phoneapp.R
import app.android.heartrate.phoneapp.model.classes.BloodPressureData
import app.android.heartrate.phoneapp.sharedpreferences.SharedPreferences
import app.android.heartrate.phoneapp.sqlite.SQLiteHealthTracker
import app.android.heartrate.phoneapp.utils.AppConstants
import app.android.heartrate.phoneapp.utils.EUGeneralClass
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar

class AddBloodPressureActivity() : AppCompatActivity() {
    var SQLite_health_tracker: SQLiteHealthTracker? = null

    var blood_pressure_result: String = ""
    var date: String = ""
    var date_time: String = ""
    var day: Int = 0
    var diastolic_string: String = ""
    var diastolic_value: Int = 0
    private lateinit var et_diastolic_level: EditText
    var et_notes: EditText? = null
    private lateinit var et_pulse_rate_level: EditText
    private lateinit var et_systolic_level: EditText
    var hour: Int = 0
    var mContext: Context? = null
    var mean_arterial_string: String = ""
    var mean_arterial_value: Int = 0
    var month: Int = 0
    var notes: String = ""
    var pulse_pressure_string: String = ""
    var pulse_pressure_value: Int = 0
    var pulse_rate_string: String = ""
    var pulse_rate_value: Int = 0
    var push_animation: Animation? = null
    private lateinit var rel_select_date: RelativeLayout
    private lateinit var rel_select_time: RelativeLayout
    var spinner_profiles: Spinner? = null
    var status_color: String = "#8CC63F"
    var systolic_string: String = ""
    var systolic_value: Int = 0
    var time: String = ""
    private lateinit var txt_date: TextView
    private lateinit var txt_mean_arterial_level: TextView
    private lateinit var txt_pulse_pressure_level: TextView
    private lateinit var txt_time: TextView
    var spinner_txt_name: TextView? = null
    var year: Int = 0
    private var current_date_time = ""
    private var save_entry_day = 0
    private var save_entry_hour = 0
    private var save_entry_minute = 0
    private var save_entry_month = 0
    private var save_entry_year = 0
    private var sharedPreferencesUtils: SharedPreferences? = null

    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        SetView()

        AppConstants.overridePendingTransitionEnter(this)
    }

    private fun SetView() {
        setContentView(R.layout.activity_add_blood_pressure)

        this.mContext = this
        activity_add_blood_pressure = this
        this.push_animation = AnimationUtils.loadAnimation(this, R.anim.view_push)
        setUpActionBar()
        sharedPreferencesUtils = SharedPreferences
        val sQLiteHealthTracker = SQLiteHealthTracker(this)
        this.SQLite_health_tracker = sQLiteHealthTracker
        sQLiteHealthTracker.openToWrite()
        this.spinner_profiles = findViewById(R.id.add_bp_spinner_profiles)
        this.et_systolic_level = findViewById(R.id.add_bp_et_systolic_value)
        this.spinner_txt_name = findViewById(R.id.spinner_txt_name)
        this.et_diastolic_level = findViewById(R.id.add_bp_et_diastolic_value)
        this.et_pulse_rate_level = findViewById(R.id.add_bp_et_pulse_rate)
        this.rel_select_date = findViewById(R.id.add_bp_rel_select_date)
        this.txt_date = findViewById(R.id.add_bp_txt_date)
        this.rel_select_time = findViewById(R.id.add_bp_rel_select_time)
        this.txt_time = findViewById(R.id.add_bp_txt_time)
        this.txt_pulse_pressure_level = findViewById(R.id.add_bp_txt_pulse_pressure_value)
        this.txt_mean_arterial_level = findViewById(R.id.add_bp_txt_mean_arterial_value)
        this.et_notes = findViewById(R.id.add_bp_et_notes)
        et_systolic_level.setText(AppConstants.default_systolic_value.toString())
        et_diastolic_level.setText(AppConstants.default_diastolic_value.toString())
        et_pulse_rate_level.setText(AppConstants.default_pulse_rate_value.toString())
        txt_pulse_pressure_level.setText(
            CalculatePulsePressure(
                AppConstants.default_systolic_value,
                AppConstants.default_diastolic_value
            )
        )
        txt_mean_arterial_level.setText(
            CalculateMAP(
                AppConstants.default_systolic_value,
                AppConstants.default_diastolic_value
            )
        )
        SetProfileSpinner()
        SetCurrentDateTime()
        if (AppConstants.is_bp_edit_mode) {
            SetBloodPressureData()
        }
        rel_select_date.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                this@AddBloodPressureActivity.hideSoftKeyboard()
                val instance = Calendar.getInstance()
                this@AddBloodPressureActivity.save_entry_year = instance[1]
                this@AddBloodPressureActivity.save_entry_month = instance[2]
                this@AddBloodPressureActivity.save_entry_day = instance[5]
                DatePickerDialog(
                    this@AddBloodPressureActivity,
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
                    this@AddBloodPressureActivity.save_entry_year,
                    this@AddBloodPressureActivity.save_entry_month,
                    this@AddBloodPressureActivity.save_entry_day
                ).show()
            }
        })
        rel_select_time.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val instance = Calendar.getInstance()
                this@AddBloodPressureActivity.save_entry_hour = instance[11]
                this@AddBloodPressureActivity.save_entry_minute = instance[12]
                TimePickerDialog(
                    this@AddBloodPressureActivity,
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
                    this@AddBloodPressureActivity.save_entry_hour,
                    this@AddBloodPressureActivity.save_entry_minute,
                    false
                ).show()
            }
        })
        et_systolic_level.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                val length = et_systolic_level.getText().toString().trim { it <= ' ' }.length
                val length2 = et_diastolic_level.getText().toString().trim { it <= ' ' }.length
                if (length > 0 && length2 > 0) {
                    val parseInt = et_systolic_level.getText().toString().trim { it <= ' ' }
                        .toInt()
                    val parseInt2 = et_diastolic_level.getText().toString().trim { it <= ' ' }
                        .toInt()
                    txt_pulse_pressure_level.setText(
                        this@AddBloodPressureActivity.CalculatePulsePressure(
                            parseInt,
                            parseInt2
                        )
                    )
                    txt_mean_arterial_level.setText(
                        this@AddBloodPressureActivity.CalculateMAP(
                            parseInt,
                            parseInt2
                        )
                    )
                }
            }
        })
        et_diastolic_level.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                val length = et_systolic_level.getText().toString().trim { it <= ' ' }.length
                val length2 = et_diastolic_level.getText().toString().trim { it <= ' ' }.length
                if (length > 0 && length2 > 0) {
                    val parseInt = et_systolic_level.getText().toString().trim { it <= ' ' }
                        .toInt()
                    val parseInt2 = et_diastolic_level.getText().toString().trim { it <= ' ' }
                        .toInt()
                    txt_pulse_pressure_level.setText(
                        this@AddBloodPressureActivity.CalculatePulsePressure(
                            parseInt,
                            parseInt2
                        )
                    )
                    txt_mean_arterial_level.setText(
                        this@AddBloodPressureActivity.CalculateMAP(
                            parseInt,
                            parseInt2
                        )
                    )
                }
            }
        })
    }

    private fun SetProfileSpinner() {
        val name = sharedPreferencesUtils!!.getUserName()
        spinner_txt_name!!.text = name
    }

    fun hideSoftKeyboard() {
        val currentFocus = currentFocus
        if (currentFocus != null) {
            (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                currentFocus.windowToken,
                0
            )
        }
    }

    private fun SetBloodPressureData() {
        if (AppConstants.selected_bp_data != null) {
            this.date = AppConstants.selected_bp_data.date.trim { it <= ' ' }
            this.time = AppConstants.selected_bp_data.time.trim { it <= ' ' }
            this.systolic_value = AppConstants.selected_bp_data.systolic_value
            this.diastolic_value = AppConstants.selected_bp_data.diastolic_value
            this.pulse_rate_value = AppConstants.selected_bp_data.pulse_rate_value
            this.pulse_pressure_value = AppConstants.selected_bp_data.pulse_pressure_value
            this.mean_arterial_value = AppConstants.selected_bp_data.mean_arterial_pressure_value
            this.status_color = AppConstants.selected_bp_data.status_color.trim { it <= ' ' }
            this.blood_pressure_result = AppConstants.selected_bp_data.result.trim { it <= ' ' }
            this.notes = AppConstants.selected_bp_data.notes.trim { it <= ' ' }
            txt_date!!.text = this.date
            txt_time!!.text = this.time
            this.systolic_string = systolic_value.toString()
            this.diastolic_string = diastolic_value.toString()
            this.pulse_rate_string = pulse_rate_value.toString()
            et_systolic_level!!.setText(this.systolic_string)
            et_diastolic_level!!.setText(this.diastolic_string)
            et_pulse_rate_level!!.setText(this.pulse_rate_string)
            txt_pulse_pressure_level!!.text = pulse_pressure_value.toString()
            txt_mean_arterial_level!!.text = mean_arterial_value.toString()
            et_notes!!.setText(this.notes)
            return
        }
        EUGeneralClass.ShowErrorToast(this, "Something went wrong!")
    }


    private fun SaveProcess() {
        try {
            this.date = txt_date!!.text.toString().trim { it <= ' ' }
            this.time = txt_time!!.text.toString().trim { it <= ' ' }
            this.systolic_value =
                et_systolic_level!!.text.toString().trim { it <= ' ' }.toInt()
            this.diastolic_value =
                et_diastolic_level!!.text.toString().trim { it <= ' ' }.toInt()
            this.pulse_rate_value =
                et_pulse_rate_level!!.text.toString().trim { it <= ' ' }.toInt()
            this.systolic_string = systolic_value.toString()
            this.diastolic_string = diastolic_value.toString()
            this.pulse_rate_string = pulse_rate_value.toString()
            val charSequence =
                txt_pulse_pressure_level!!.text.toString()
            this.pulse_pressure_string = charSequence
            this.pulse_pressure_value = charSequence.toInt()
            val charSequence2 =
                txt_mean_arterial_level!!.text.toString()
            this.mean_arterial_string = charSequence2
            this.mean_arterial_value = charSequence2.toInt()
            this.notes = et_notes!!.text.toString()
            GetResult()
            val bloodPressureData = BloodPressureData()
            val selectedItemPosition = spinner_profiles!!.selectedItemPosition
            if (!AppConstants.is_bp_edit_mode) {
                bloodPressureData.user_id = sharedPreferencesUtils!!.getUserId()
                bloodPressureData.date = date.trim { it <= ' ' }
                bloodPressureData.time = time.trim { it <= ' ' }
                bloodPressureData.systolic_value = this.systolic_value
                bloodPressureData.diastolic_value = this.diastolic_value
                bloodPressureData.pulse_pressure_value = this.pulse_pressure_value
                bloodPressureData.pulse_rate_value = this.pulse_rate_value
                bloodPressureData.mean_arterial_pressure_value = this.mean_arterial_value
                bloodPressureData.status_color = status_color.trim { it <= ' ' }
                bloodPressureData.result = blood_pressure_result.trim { it <= ' ' }
                bloodPressureData.notes = notes.trim { it <= ' ' }
                GetDateTime(date.trim { it <= ' ' }, time.trim { it <= ' ' })
                bloodPressureData.dateTime = this.date_time
                bloodPressureData.day = this.day
                bloodPressureData.month = this.month
                bloodPressureData.year = this.year
                bloodPressureData.hour = this.hour
                SQLite_health_tracker!!.InsertBloodPressureData(bloodPressureData)
                EUGeneralClass.ShowSuccessToast(this, AppConstants.data_saved_messages)
                onBackPressed()
                return
            }
            val i = AppConstants.selected_bp_data.row_id
            bloodPressureData.user_id = sharedPreferencesUtils!!.getUserId()
            bloodPressureData.date = date.trim { it <= ' ' }
            bloodPressureData.time = time.trim { it <= ' ' }
            bloodPressureData.systolic_value = this.systolic_value
            bloodPressureData.diastolic_value = this.diastolic_value
            bloodPressureData.pulse_pressure_value = this.pulse_pressure_value
            bloodPressureData.pulse_rate_value = this.pulse_rate_value
            bloodPressureData.mean_arterial_pressure_value = this.mean_arterial_value
            bloodPressureData.status_color = status_color.trim { it <= ' ' }
            bloodPressureData.result = blood_pressure_result.trim { it <= ' ' }
            bloodPressureData.notes = notes.trim { it <= ' ' }
            GetDateTime(date.trim { it <= ' ' }, time.trim { it <= ' ' })
            bloodPressureData.dateTime = this.date_time
            bloodPressureData.day = this.day
            bloodPressureData.month = this.month
            bloodPressureData.year = this.year
            bloodPressureData.hour = this.hour
            SQLite_health_tracker!!.UpdateBloodPressureData(
                i,
                sharedPreferencesUtils!!.getUserId(), bloodPressureData
            )
            EUGeneralClass.ShowSuccessToast(this, AppConstants.data_updated_messages)
            onBackPressed()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun GetResult() {
        var i: Int = 0 //check this out
        val i2 = this.systolic_value
        if (i2 < 70 && this.diastolic_value < 40) {
            this.blood_pressure_result = AppConstants.pressure_level_low
            this.status_color = "#00A99D"
        } else if ((i2 >= 70 && i2 < 90) || ((diastolic_value.also { i = it }) >= 40 && i < 60)) {
            this.blood_pressure_result = AppConstants.pressure_level_low
            this.status_color = "#00A99D"
        } else if ((i2 >= 90) && (i2 < 120) && (i >= 60) && (i < 80)) {
            this.blood_pressure_result = AppConstants.pressure_level_normal
            this.status_color = "#8CC63F"
        } else if ((i2 >= 120 && i2 < 140) || (i >= 80 && i < 90)) {
            this.blood_pressure_result = AppConstants.pressure_level_pre_hypertension
            this.status_color = "#D9E021"
        } else if ((i2 >= 140 && i2 < 160) || (i >= 90 && i < 100)) {
            this.blood_pressure_result = AppConstants.pressure_level_high_stage_1
            this.status_color = "#FBB03B"
        } else if ((i2 >= 160 && i2 < 180) || (i >= 100 && i < 110)) {
            this.blood_pressure_result = AppConstants.pressure_level_high_stage_2
            this.status_color = "#F15A24"
        } else if (i2 >= 180 || i >= 110) {
            this.blood_pressure_result = AppConstants.pressure_level_high_crisis
            this.status_color = "#ED1C24"
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

    fun CalculatePulsePressure(i: Int, i2: Int): String {
        return (i - i2).toString()
    }

    fun CalculateMAP(i: Int, i2: Int): String {
        return ((i + (i2 * 2)) / 3).toString()
    }

    private fun setUpActionBar() {
        (findViewById<View>(R.id.toolbar_txt_title_1) as TextView).text =
            resources.getString(R.string.lbl_header_add)
        (findViewById<View>(R.id.toolbar_txt_title_2) as TextView).text =
            resources.getString(R.string.lbl_header_blood_pressure)
        findViewById<View>(R.id.toolbar_rel_save).setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                this@AddBloodPressureActivity.SaveProcess()
            }
        })
        findViewById<View>(R.id.toolbar_rel_back).setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                this@AddBloodPressureActivity.onBackPressed()
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
        var activity_add_blood_pressure: Activity? = null
    }
}
