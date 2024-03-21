package app.android.heartrate.phoneapp.activities.record.heart_rate

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.os.Bundle
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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.android.heartrate.phoneapp.R
import app.android.heartrate.phoneapp.adapters.SpinnerProfileAdapter
import app.android.heartrate.phoneapp.databinding.ActivityAddHeartRateBinding
import app.android.heartrate.phoneapp.model.cholesterol.CholesterolResponse
import app.android.heartrate.phoneapp.model.classes.CholesterolData
import app.android.heartrate.phoneapp.model.classes.HeartRateData
import app.android.heartrate.phoneapp.model.heartrate.HeartRateResponse
import app.android.heartrate.phoneapp.retrofit.ApiClient
import app.android.heartrate.phoneapp.sharedpreferences.SharedPreferences
import app.android.heartrate.phoneapp.sqlite.SQLiteHealthTracker
import app.android.heartrate.phoneapp.utils.AppConstants
import app.android.heartrate.phoneapp.utils.EUGeneralClass
import com.shawnlin.numberpicker.NumberPicker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Arrays
import java.util.Calendar

class AddHeartRateActivity() : AppCompatActivity() {
    var ageIndex: Int = 0
    var SQLite_health_tracker: SQLiteHealthTracker? = null
    var adapter_spinner_gender: ArrayAdapter<String>? = null
    var adapter_spinner_status: ArrayAdapter<String>? = null
    var age_default_value: Int = AppConstants.default_hr_age_value
    var age_max_value: Int = 120
    var age_min_value: Int = 1
    var age_string: String = ""
    var age_unit: String = ""
    var age_value: Int = 0
    var arrayListCurrentStatus: ArrayList<String> = ArrayList(
        Arrays.asList(
            AppConstants.hr_status_resting,
            AppConstants.hr_status_general,
            AppConstants.hr_status_after_exercise,
            AppConstants.hr_status_before_exercise,
            AppConstants.hr_status_tired,
            AppConstants.hr_status_unwell,
            AppConstants.hr_status_surprised,
            AppConstants.hr_status_sad,
            AppConstants.hr_status_angry,
            AppConstants.hr_status_fear_full,
            AppConstants.hr_status_in_love
        )
    )
    var arrayListGender: ArrayList<String> = ArrayList(
        Arrays.asList(
            AppConstants.hr_gender_male,
            AppConstants.hr_gender_female,
            AppConstants.hr_gender_child
        )
    )

    var color_string: String = ""
    var current_status: String = ""
    var date: String = ""
    var date_time: String = ""
    var day: Int = 0
    var et_heart_rate: EditText? = null
    var et_notes: EditText? = null
    var gender: String = ""
    var heart_rate_string: String = ""
    var heart_rate_value: Int = 0
    var hour: Int = 0
    var mContext: Context? = null
    var month: Int = 0
    var notes: String = ""
    var np_age: NumberPicker? = null
    var push_animation: Animation? = null
    var range_string: String = ""
    private lateinit var rel_select_date: RelativeLayout
    private lateinit var rel_select_time: RelativeLayout
    var result_string: String = ""
    var spinner_current_status: Spinner? = null
    var spinner_gender: Spinner? = null
    var spinner_profile_adapter: SpinnerProfileAdapter? = null
    var spinner_profiles: Spinner? = null
    var time: String = ""
    var txt_age: TextView? = null
    var txt_age_unit: TextView? = null
    private lateinit var txt_date: TextView
    var spinner_txt_name: TextView? = null
    private lateinit var txt_time: TextView
    var year: Int = 0
    private lateinit var array_age_range: Array<String?>
    private var current_date_time = ""
    private var save_entry_day = 0
    private var save_entry_hour = 0
    private var save_entry_minute = 0
    private var save_entry_month = 0
    private var save_entry_year = 0

    private lateinit var sharedPreferencesUtils: SharedPreferences
    private lateinit var binding: ActivityAddHeartRateBinding

    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        binding = ActivityAddHeartRateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        SetView()
        AppConstants.overridePendingTransitionEnter(this)
    }

    private fun SetView() {
        this.mContext = this
        this.push_animation = AnimationUtils.loadAnimation(this, R.anim.view_push)
        setUpActionBar()
        sharedPreferencesUtils = SharedPreferences
        val sQLiteHealthTracker = SQLiteHealthTracker(this)
        this.SQLite_health_tracker = sQLiteHealthTracker
        sQLiteHealthTracker.openToWrite()
        this.spinner_profiles = findViewById(R.id.add_hr_spinner_profiles)
        this.spinner_gender = findViewById(R.id.add_hr_spinner_gender)
        this.spinner_current_status = findViewById(R.id.add_hr_spinner_current_status)
        this.txt_age = findViewById(R.id.add_hr_txt_age)
        this.txt_age_unit = findViewById(R.id.add_hr_txt_age_unit)
        this.np_age = findViewById(R.id.add_hr_np_age)
        val editText = findViewById<EditText>(R.id.add_hr_et_heart_rate)
        this.et_heart_rate = editText
        editText.setText(AppConstants.default_hr_value.toString())
        val editText2 = this.et_heart_rate
        editText2!!.setSelection(editText2.text.toString().trim { it <= ' ' }.length)
        this.rel_select_date = findViewById(R.id.add_hr_rel_select_date)
        this.txt_date = findViewById(R.id.add_hr_txt_date)
        this.rel_select_time = findViewById(R.id.add_hr_rel_select_time)
        this.txt_time = findViewById(R.id.add_hr_txt_time)
        this.et_notes = findViewById(R.id.add_hr_et_notes)
        this.spinner_txt_name = findViewById(R.id.spinner_txt_name)
        SetProfileSpinner()
        SetAgePicker()
        SetGenderSpinner()
        SetCurrentStatusSpinner()
        SetCurrentDateTime()
        if (AppConstants.is_heart_rate_edit_mode) {
            SetHeartRateData()
        }
        rel_select_date.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                this@AddHeartRateActivity.hideSoftKeyboard()
                val instance = Calendar.getInstance()
                this@AddHeartRateActivity.save_entry_year = instance[1]
                this@AddHeartRateActivity.save_entry_month = instance[2]
                this@AddHeartRateActivity.save_entry_day = instance[5]
                DatePickerDialog(
                    this@AddHeartRateActivity,
                    R.style.DialogTheme,
                    OnDateSetListener { datePicker, i, i2, i3 ->
                        try {
                            txt_date.setText(
                                SimpleDateFormat("yyyy-MM-dd").format(
                                    SimpleDateFormat("dd/MM/yyyy").parse(
                                        (i3.toString() + "/" + (i2 + 1) + "/" + i).trim { it <= ' ' })
                                )
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    },
                    this@AddHeartRateActivity.save_entry_year,
                    this@AddHeartRateActivity.save_entry_month,
                    this@AddHeartRateActivity.save_entry_day
                ).show()
            }
        })
        rel_select_time.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val instance = Calendar.getInstance()
                this@AddHeartRateActivity.save_entry_hour = instance[11]
                this@AddHeartRateActivity.save_entry_minute = instance[12]
                TimePickerDialog(
                    this@AddHeartRateActivity,
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
                    this@AddHeartRateActivity.save_entry_hour,
                    this@AddHeartRateActivity.save_entry_minute,
                    false
                ).show()
            }
        })
    }

    private fun SetAgePicker() {
        this.array_age_range = arrayOfNulls(this.age_max_value)
        var i = 0
        while (true) {
            val strArr = this.array_age_range
            if (i >= strArr.size) {
                break
            }
            val i2 = i + 1
            strArr[i] = i2.toString()
            i = i2
        }
        var i3 = 0
        while (true) {
            val strArr2 = this.array_age_range
            if (i3 >= strArr2.size) {
                break
            } else if (strArr2[i3].equals(age_default_value.toString(), ignoreCase = true)) {
                this.ageIndex = i3
                break
            } else {
                i3++
            }
        }
        np_age!!.maxValue = this.age_max_value
        np_age!!.minValue = this.age_min_value
        np_age!!.displayedValues = this.array_age_range
        np_age!!.wrapSelectorWheel = false
        np_age!!.value = this.age_default_value
        np_age!!.setOnValueChangedListener(object : NumberPicker.OnValueChangeListener {
            override fun onValueChange(numberPicker: NumberPicker, i: Int, i2: Int) {
                this@AddHeartRateActivity.ageIndex = i2
                txt_age!!.text = numberPicker.value.toString().trim { it <= ' ' }
                if (numberPicker.value == 1) {
                    this@AddHeartRateActivity.age_unit = AppConstants.age_unit_year
                    txt_age_unit!!.text = this@AddHeartRateActivity.age_unit
                } else if (numberPicker.value > 1) {
                    this@AddHeartRateActivity.age_unit = AppConstants.age_unit_years
                    txt_age_unit!!.text = this@AddHeartRateActivity.age_unit
                }
            }
        })
        txt_age!!.text = AppConstants.default_hr_age_value.toString()
        txt_age_unit!!.text = AppConstants.age_unit_years
    }

    private fun SetGenderSpinner() {
        val arrayAdapter = ArrayAdapter(this, R.layout.spinner_list, this.arrayListGender)
        this.adapter_spinner_gender = arrayAdapter
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list)
        spinner_gender!!.adapter = this.adapter_spinner_gender
    }

    private fun SetCurrentStatusSpinner() {
        val arrayAdapter = ArrayAdapter(this, R.layout.spinner_list, this.arrayListCurrentStatus)
        this.adapter_spinner_status = arrayAdapter
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list)
        spinner_current_status!!.adapter = this.adapter_spinner_status
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

    private fun SetHeartRateData() {
        if (AppConstants.selected_heart_rate_data != null) {
            this.date = AppConstants.selected_heart_rate_data.date.trim { it <= ' ' }
            this.time = AppConstants.selected_heart_rate_data.time.trim { it <= ' ' }
            val i = AppConstants.selected_heart_rate_data.age
            this.age_value = i
            this.age_string = i.toString().trim { it <= ' ' }
            val i2 = AppConstants.selected_heart_rate_data.heart_rate_value
            this.heart_rate_value = i2
            this.heart_rate_string = i2.toString().trim { it <= ' ' }
            this.gender = AppConstants.selected_heart_rate_data.gender.trim { it <= ' ' }
            this.current_status =
                AppConstants.selected_heart_rate_data.current_status.trim { it <= ' ' }
            this.color_string = AppConstants.selected_heart_rate_data.color.trim { it <= ' ' }
            this.result_string = AppConstants.selected_heart_rate_data.result.trim { it <= ' ' }
            this.range_string = AppConstants.selected_heart_rate_data.range.trim { it <= ' ' }
            this.notes = AppConstants.selected_heart_rate_data.notes.trim { it <= ' ' }
            var i3 = 0
            while (true) {
                val strArr = this.array_age_range
                if (i3 >= strArr.size) {
                    break
                } else if (strArr[i3].equals(age_value.toString(), ignoreCase = true)) {
                    this.ageIndex = i3
                    break
                } else {
                    i3++
                }
            }
            np_age!!.maxValue = this.age_max_value
            np_age!!.minValue = this.age_min_value
            np_age!!.displayedValues = this.array_age_range
            np_age!!.wrapSelectorWheel = false
            np_age!!.value = this.ageIndex + 1
            txt_age!!.text = this.age_string
            val i4 = this.age_value
            if (i4 == 1) {
                val str = AppConstants.age_unit_year
                this.age_unit = str
                txt_age_unit!!.text = str
            } else if (i4 > 1) {
                val str2 = AppConstants.age_unit_years
                this.age_unit = str2
                txt_age_unit!!.text = str2
            }
            et_heart_rate!!.setText(this.heart_rate_string)
            val editText = this.et_heart_rate
            editText!!.setSelection(editText.text.toString().trim { it <= ' ' }.length)
            if (gender.equals(AppConstants.hr_gender_male, ignoreCase = true)) {
                spinner_gender!!.setSelection(0)
            } else if (gender.equals(AppConstants.hr_gender_female, ignoreCase = true)) {
                spinner_gender!!.setSelection(1)
            } else if (gender.equals(AppConstants.hr_gender_child, ignoreCase = true)) {
                spinner_gender!!.setSelection(2)
            }
            if (current_status.equals(AppConstants.hr_status_resting, ignoreCase = true)) {
                spinner_current_status!!.setSelection(0)
            } else if (current_status.equals(AppConstants.hr_status_general, ignoreCase = true)) {
                spinner_current_status!!.setSelection(1)
            } else if (current_status.equals(
                    AppConstants.hr_status_after_exercise,
                    ignoreCase = true
                )
            ) {
                spinner_current_status!!.setSelection(2)
            } else if (current_status.equals(
                    AppConstants.hr_status_before_exercise,
                    ignoreCase = true
                )
            ) {
                spinner_current_status!!.setSelection(3)
            } else if (current_status.equals(AppConstants.hr_status_tired, ignoreCase = true)) {
                spinner_current_status!!.setSelection(4)
            } else if (current_status.equals(AppConstants.hr_status_unwell, ignoreCase = true)) {
                spinner_current_status!!.setSelection(5)
            } else if (current_status.equals(AppConstants.hr_status_surprised, ignoreCase = true)) {
                spinner_current_status!!.setSelection(6)
            } else if (current_status.equals(AppConstants.hr_status_sad, ignoreCase = true)) {
                spinner_current_status!!.setSelection(7)
            } else if (current_status.equals(AppConstants.hr_status_angry, ignoreCase = true)) {
                spinner_current_status!!.setSelection(8)
            } else if (current_status.equals(AppConstants.hr_status_fear_full, ignoreCase = true)) {
                spinner_current_status!!.setSelection(9)
            } else if (current_status.equals(AppConstants.hr_status_in_love, ignoreCase = true)) {
                spinner_current_status!!.setSelection(10)
            }
            txt_date!!.text = this.date
            txt_time!!.text = this.time
            et_notes!!.setText(this.notes)
            return
        }
        EUGeneralClass.ShowErrorToast(this, "Something went wrong!")
    }


    private fun SaveProcess() {
        try {
            this.date = txt_date!!.text.toString().trim { it <= ' ' }
            this.time = txt_time!!.text.toString().trim { it <= ' ' }
            this.age_string = txt_age!!.text.toString().trim { it <= ' ' }
            this.age_value = np_age!!.value
            val trim = et_heart_rate!!.text.toString().trim { it <= ' ' }
            this.heart_rate_string = trim
            this.heart_rate_value = trim.toInt()
            this.gender = spinner_gender!!.selectedItem.toString()
            this.current_status =
                spinner_current_status!!.selectedItem.toString()
            this.notes = et_notes!!.text.toString().trim { it <= ' ' }
            val heartRateData = HeartRateData()
            GetResult()
            if (!AppConstants.is_heart_rate_edit_mode) {
                heartRateData.user_id = sharedPreferencesUtils!!.getUserId()
                heartRateData.date = date.trim { it <= ' ' }
                heartRateData.time = time.trim { it <= ' ' }
                heartRateData.age = this.age_value
                heartRateData.heart_rate_value = this.heart_rate_value
                heartRateData.gender = gender.trim { it <= ' ' }
                heartRateData.current_status = current_status.trim { it <= ' ' }
                heartRateData.color = color_string.trim { it <= ' ' }
                heartRateData.result = this.result_string
                heartRateData.range = this.range_string
                heartRateData.notes = notes.trim { it <= ' ' }
                GetDateTime(date.trim { it <= ' ' }, time.trim { it <= ' ' })
                heartRateData.dateTime = this.date_time
                heartRateData.day = this.day
                heartRateData.month = this.month
                heartRateData.year = this.year
                heartRateData.hour = this.hour

                insertData(heartRateData)

//                SQLite_health_tracker!!.InsertHeartRateData(heartRateData)
//                EUGeneralClass.ShowSuccessToast(this, "Heart Rate Data saved successfully!")
//                onBackPressed()
//                return
            }
            val i = AppConstants.selected_heart_rate_data.row_id
            heartRateData.user_id = sharedPreferencesUtils!!.getUserId()
            heartRateData.date = date.trim { it <= ' ' }
            heartRateData.time = time.trim { it <= ' ' }
            heartRateData.heart_rate_value = this.heart_rate_value
            heartRateData.age = this.age_value
            heartRateData.gender = gender.trim { it <= ' ' }
            heartRateData.current_status = current_status.trim { it <= ' ' }
            heartRateData.color = color_string.trim { it <= ' ' }
            heartRateData.result = this.result_string
            heartRateData.range = this.range_string
            heartRateData.notes = notes.trim { it <= ' ' }
            GetDateTime(date.trim { it <= ' ' }, time.trim { it <= ' ' })
            heartRateData.dateTime = this.date_time
            heartRateData.day = this.day
            heartRateData.month = this.month
            heartRateData.year = this.year
            heartRateData.hour = this.hour
            heartRateData.row_id = i

            updateData(heartRateData)
//            SQLite_health_tracker!!.UpdateHeartRateData(
//                i,
//                sharedPreferencesUtils!!.getUserId(), heartRateData
//            )
//            EUGeneralClass.ShowSuccessToast(this, "Heart Rate Data updated successfully!")
//            onBackPressed()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun GetResult() {
        if (gender.equals(AppConstants.hr_gender_child, ignoreCase = true)) {
            val i = this.age_value
            if (i >= 1 && i <= 3) {
                val i2 = this.heart_rate_value
                if (i2 == 70 || i2 <= 110) {
                    this.color_string = "#F7931E"
                    this.result_string = AppConstants.hr_result_average
                    this.range_string = "70-110"
                }
            } else if (i > 3 && i <= 6) {
                val i3 = this.heart_rate_value
                if (i3 == 65 || i3 <= 110) {
                    this.color_string = "#F7931E"
                    this.result_string = AppConstants.hr_result_average
                    this.range_string = "70-110"
                }
            } else if (i > 6 && i <= 12) {
                val i4 = this.heart_rate_value
                if (i4 == 60 || i4 <= 95) {
                    this.color_string = "#F7931E"
                    this.result_string = AppConstants.hr_result_average
                    this.range_string = "70-110"
                }
            } else if (i > 12 && i < 18) {
                val i5 = this.heart_rate_value
                if (i5 == 55 || i5 <= 85) {
                    this.color_string = "#F7931E"
                    this.result_string = AppConstants.hr_result_average
                    this.range_string = "70-110"
                }
            }
        } else if (gender.equals(AppConstants.hr_gender_male, ignoreCase = true)) {
            val i6 = this.age_value
            if (i6 == 18 || i6 <= 25) {
                val i7 = this.heart_rate_value
                if (i7 == 49 || i7 <= 55) {
                    this.color_string = "#00A99D"
                    this.result_string = AppConstants.hr_result_athlete
                    this.range_string = "70-73"
                } else if (i7 >= 56 && i7 <= 61) {
                    this.color_string = "#8CC63F"
                    this.result_string = AppConstants.hr_result_excellent
                    this.range_string = "70-73"
                } else if (i7 >= 62 && i7 <= 65) {
                    this.color_string = "#D9E021"
                    this.result_string = AppConstants.hr_result_good
                    this.range_string = "70-73"
                } else if (i7 >= 66 && i7 <= 69) {
                    this.color_string = "#FBB03B"
                    this.result_string = AppConstants.hr_result_above_average
                    this.range_string = "70-73"
                } else if (i7 >= 70 && i7 <= 73) {
                    this.color_string = "#F7931E"
                    this.result_string = AppConstants.hr_result_average
                    this.range_string = "70-73"
                } else if (i7 >= 74 && i7 <= 81) {
                    this.color_string = "#F15A24"
                    this.result_string = AppConstants.hr_result_below_average
                    this.range_string = "70-73"
                } else if (i7 >= 82) {
                    this.color_string = "#ED1C24"
                    this.result_string = AppConstants.hr_result_poor
                    this.range_string = "70-73"
                }
            } else if (i6 >= 26 && i6 <= 35) {
                val i8 = this.heart_rate_value
                if (i8 == 49 || i8 <= 54) {
                    this.color_string = "#00A99D"
                    this.result_string = AppConstants.hr_result_athlete
                    this.range_string = "71-74"
                } else if (i8 >= 55 && i8 <= 61) {
                    this.color_string = "#8CC63F"
                    this.result_string = AppConstants.hr_result_excellent
                    this.range_string = "71-74"
                } else if (i8 >= 62 && i8 <= 65) {
                    this.color_string = "#D9E021"
                    this.result_string = AppConstants.hr_result_good
                    this.range_string = "71-74"
                } else if (i8 >= 66 && i8 <= 70) {
                    this.color_string = "#FBB03B"
                    this.result_string = AppConstants.hr_result_above_average
                    this.range_string = "71-74"
                } else if (i8 >= 71 && i8 <= 74) {
                    this.color_string = "#F7931E"
                    this.result_string = AppConstants.hr_result_average
                    this.range_string = "71-74"
                } else if (i8 >= 75 && i8 <= 81) {
                    this.color_string = "#F15A24"
                    this.result_string = AppConstants.hr_result_below_average
                    this.range_string = "71-74"
                } else if (i8 >= 82) {
                    this.color_string = "#ED1C24"
                    this.result_string = AppConstants.hr_result_poor
                    this.range_string = "71-74"
                }
            } else if (i6 >= 36 && i6 <= 45) {
                val i9 = this.heart_rate_value
                if (i9 == 50 || i9 <= 56) {
                    this.color_string = "#00A99D"
                    this.result_string = AppConstants.hr_result_athlete
                    this.range_string = "71-75"
                } else if (i9 >= 57 && i9 <= 62) {
                    this.color_string = "#8CC63F"
                    this.result_string = AppConstants.hr_result_excellent
                    this.range_string = "71-75"
                } else if (i9 >= 63 && i9 <= 66) {
                    this.color_string = "#D9E021"
                    this.result_string = AppConstants.hr_result_good
                    this.range_string = "71-75"
                } else if (i9 >= 67 && i9 <= 70) {
                    this.color_string = "#FBB03B"
                    this.result_string = AppConstants.hr_result_above_average
                    this.range_string = "71-75"
                } else if (i9 >= 71 && i9 <= 75) {
                    this.color_string = "#F7931E"
                    this.result_string = AppConstants.hr_result_average
                    this.range_string = "71-75"
                } else if (i9 >= 76 && i9 <= 82) {
                    this.color_string = "#F15A24"
                    this.result_string = AppConstants.hr_result_below_average
                    this.range_string = "71-75"
                } else if (i9 >= 83) {
                    this.color_string = "#ED1C24"
                    this.result_string = AppConstants.hr_result_poor
                    this.range_string = "71-75"
                }
            } else if (i6 >= 46 && i6 <= 55) {
                val i10 = this.heart_rate_value
                if (i10 == 50 || i10 <= 57) {
                    this.color_string = "#00A99D"
                    this.result_string = AppConstants.hr_result_athlete
                    this.range_string = "72-76"
                } else if (i10 >= 58 && i10 <= 63) {
                    this.color_string = "#8CC63F"
                    this.result_string = AppConstants.hr_result_excellent
                    this.range_string = "72-76"
                } else if (i10 >= 64 && i10 <= 67) {
                    this.color_string = "#D9E021"
                    this.result_string = AppConstants.hr_result_good
                    this.range_string = "72-76"
                } else if (i10 >= 68 && i10 <= 71) {
                    this.color_string = "#FBB03B"
                    this.result_string = AppConstants.hr_result_above_average
                    this.range_string = "72-76"
                } else if (i10 >= 72 && i10 <= 76) {
                    this.color_string = "#F7931E"
                    this.result_string = AppConstants.hr_result_average
                    this.range_string = "72-76"
                } else if (i10 >= 77 && i10 <= 83) {
                    this.color_string = "#F15A24"
                    this.result_string = AppConstants.hr_result_below_average
                    this.range_string = "72-76"
                } else if (i10 >= 84) {
                    this.color_string = "#ED1C24"
                    this.result_string = AppConstants.hr_result_poor
                    this.range_string = "72-76"
                }
            } else if (i6 >= 56 && i6 <= 65) {
                val i11 = this.heart_rate_value
                if (i11 == 51 || i11 <= 56) {
                    this.color_string = "#00A99D"
                    this.result_string = AppConstants.hr_result_athlete
                    this.range_string = "72-75"
                } else if (i11 >= 57 && i11 <= 61) {
                    this.color_string = "#8CC63F"
                    this.result_string = AppConstants.hr_result_excellent
                    this.range_string = "72-75"
                } else if (i11 >= 62 && i11 <= 67) {
                    this.color_string = "#D9E021"
                    this.result_string = AppConstants.hr_result_good
                    this.range_string = "72-75"
                } else if (i11 >= 68 && i11 <= 71) {
                    this.color_string = "#FBB03B"
                    this.result_string = AppConstants.hr_result_above_average
                    this.range_string = "72-75"
                } else if (i11 >= 72 && i11 <= 75) {
                    this.color_string = "#F7931E"
                    this.result_string = AppConstants.hr_result_average
                    this.range_string = "72-75"
                } else if (i11 >= 76 && i11 <= 81) {
                    this.color_string = "#F15A24"
                    this.result_string = AppConstants.hr_result_below_average
                    this.range_string = "72-75"
                } else if (i11 >= 82) {
                    this.color_string = "#ED1C24"
                    this.result_string = AppConstants.hr_result_poor
                    this.range_string = "72-75"
                }
            } else if (i6 > 65) {
                val i12 = this.heart_rate_value
                if (i12 == 50 || i12 <= 55) {
                    this.color_string = "#00A99D"
                    this.result_string = AppConstants.hr_result_athlete
                    this.range_string = "70-73"
                } else if (i12 >= 56 && i12 <= 61) {
                    this.color_string = "#8CC63F"
                    this.result_string = AppConstants.hr_result_excellent
                    this.range_string = "70-73"
                } else if (i12 >= 62 && i12 <= 65) {
                    this.color_string = "#D9E021"
                    this.result_string = AppConstants.hr_result_good
                    this.range_string = "70-73"
                } else if (i12 >= 66 && i12 <= 69) {
                    this.color_string = "#FBB03B"
                    this.result_string = AppConstants.hr_result_above_average
                    this.range_string = "70-73"
                } else if (i12 >= 70 && i12 <= 73) {
                    this.color_string = "#F7931E"
                    this.result_string = AppConstants.hr_result_average
                    this.range_string = "70-73"
                } else if (i12 >= 74 && i12 <= 79) {
                    this.color_string = "#F15A24"
                    this.result_string = AppConstants.hr_result_below_average
                    this.range_string = "70-73"
                } else if (i12 >= 80) {
                    this.color_string = "#ED1C24"
                    this.result_string = AppConstants.hr_result_poor
                    this.range_string = "70-73"
                }
            }
        } else if (gender.equals(AppConstants.hr_gender_female, ignoreCase = true)) {
            val i13 = this.age_value
            if (i13 == 18 || i13 <= 25) {
                val i14 = this.heart_rate_value
                if (i14 == 54 || i14 <= 60) {
                    this.color_string = "#00A99D"
                    this.result_string = AppConstants.hr_result_athlete
                    this.range_string = "74-78"
                } else if (i14 >= 61 && i14 <= 65) {
                    this.color_string = "#8CC63F"
                    this.result_string = AppConstants.hr_result_excellent
                    this.range_string = "74-78"
                } else if (i14 >= 66 && i14 <= 69) {
                    this.color_string = "#D9E021"
                    this.result_string = AppConstants.hr_result_good
                    this.range_string = "74-78"
                } else if (i14 >= 70 && i14 <= 73) {
                    this.color_string = "#FBB03B"
                    this.result_string = AppConstants.hr_result_above_average
                    this.range_string = "74-78"
                } else if (i14 >= 74 && i14 <= 78) {
                    this.color_string = "#F7931E"
                    this.result_string = AppConstants.hr_result_average
                    this.range_string = "74-78"
                } else if (i14 >= 79 && i14 <= 84) {
                    this.color_string = "#F15A24"
                    this.result_string = AppConstants.hr_result_below_average
                    this.range_string = "74-78"
                } else if (i14 >= 85) {
                    this.color_string = "#ED1C24"
                    this.result_string = AppConstants.hr_result_poor
                    this.range_string = "74-78"
                }
            } else if (i13 >= 26 && i13 <= 35) {
                val i15 = this.heart_rate_value
                if (i15 == 54 || i15 <= 59) {
                    this.color_string = "#00A99D"
                    this.result_string = AppConstants.hr_result_athlete
                    this.range_string = "73-76"
                } else if (i15 >= 60 && i15 <= 64) {
                    this.color_string = "#8CC63F"
                    this.result_string = AppConstants.hr_result_excellent
                    this.range_string = "73-76"
                } else if (i15 >= 65 && i15 <= 68) {
                    this.color_string = "#D9E021"
                    this.result_string = AppConstants.hr_result_good
                    this.range_string = "73-76"
                } else if (i15 >= 69 && i15 <= 72) {
                    this.color_string = "#FBB03B"
                    this.result_string = AppConstants.hr_result_above_average
                    this.range_string = "73-76"
                } else if (i15 >= 73 && i15 <= 76) {
                    this.color_string = "#F7931E"
                    this.result_string = AppConstants.hr_result_average
                    this.range_string = "73-76"
                } else if (i15 >= 77 && i15 <= 82) {
                    this.color_string = "#F15A24"
                    this.result_string = AppConstants.hr_result_below_average
                    this.range_string = "73-76"
                } else if (i15 >= 83) {
                    this.color_string = "#ED1C24"
                    this.result_string = AppConstants.hr_result_poor
                    this.range_string = "73-76"
                }
            } else if (i13 >= 36 && i13 <= 45) {
                val i16 = this.heart_rate_value
                if (i16 == 54 || i16 <= 59) {
                    this.color_string = "#00A99D"
                    this.result_string = AppConstants.hr_result_athlete
                    this.range_string = "74-78"
                } else if (i16 >= 60 && i16 <= 64) {
                    this.color_string = "#8CC63F"
                    this.result_string = AppConstants.hr_result_excellent
                    this.range_string = "74-78"
                } else if (i16 >= 65 && i16 <= 69) {
                    this.color_string = "#D9E021"
                    this.result_string = AppConstants.hr_result_good
                    this.range_string = "74-78"
                } else if (i16 >= 70 && i16 <= 73) {
                    this.color_string = "#FBB03B"
                    this.result_string = AppConstants.hr_result_above_average
                    this.range_string = "74-78"
                } else if (i16 >= 74 && i16 <= 78) {
                    this.color_string = "#F7931E"
                    this.result_string = AppConstants.hr_result_average
                    this.range_string = "74-78"
                } else if (i16 >= 79 && i16 <= 84) {
                    this.color_string = "#F15A24"
                    this.result_string = AppConstants.hr_result_below_average
                    this.range_string = "74-78"
                } else if (i16 >= 85) {
                    this.color_string = "#ED1C24"
                    this.result_string = AppConstants.hr_result_poor
                    this.range_string = "74-78"
                }
            } else if (i13 >= 46 && i13 <= 55) {
                val i17 = this.heart_rate_value
                if (i17 == 54 || i17 <= 60) {
                    this.color_string = "#00A99D"
                    this.result_string = AppConstants.hr_result_athlete
                    this.range_string = "74-77"
                } else if (i17 >= 61 && i17 <= 65) {
                    this.color_string = "#8CC63F"
                    this.result_string = AppConstants.hr_result_excellent
                    this.range_string = "74-77"
                } else if (i17 >= 66 && i17 <= 69) {
                    this.color_string = "#D9E021"
                    this.result_string = AppConstants.hr_result_good
                    this.range_string = "74-77"
                } else if (i17 >= 70 && i17 <= 73) {
                    this.color_string = "#FBB03B"
                    this.result_string = AppConstants.hr_result_above_average
                    this.range_string = "74-77"
                } else if (i17 >= 74 && i17 <= 77) {
                    this.color_string = "#F7931E"
                    this.result_string = AppConstants.hr_result_average
                    this.range_string = "74-77"
                } else if (i17 >= 78 && i17 <= 83) {
                    this.color_string = "#F15A24"
                    this.result_string = AppConstants.hr_result_below_average
                    this.range_string = "74-77"
                } else if (i17 >= 84) {
                    this.color_string = "#ED1C24"
                    this.result_string = AppConstants.hr_result_poor
                    this.range_string = "74-77"
                }
            } else if (i13 >= 56 && i13 <= 65) {
                val i18 = this.heart_rate_value
                if (i18 == 54 || i18 <= 59) {
                    this.color_string = "#00A99D"
                    this.result_string = AppConstants.hr_result_athlete
                    this.range_string = "74-77"
                } else if (i18 >= 60 && i18 <= 64) {
                    this.color_string = "#8CC63F"
                    this.result_string = AppConstants.hr_result_excellent
                    this.range_string = "74-77"
                } else if (i18 >= 65 && i18 <= 68) {
                    this.color_string = "#D9E021"
                    this.result_string = AppConstants.hr_result_good
                    this.range_string = "74-77"
                } else if (i18 >= 69 && i18 <= 73) {
                    this.color_string = "#FBB03B"
                    this.result_string = AppConstants.hr_result_above_average
                    this.range_string = "74-77"
                } else if (i18 >= 74 && i18 <= 77) {
                    this.color_string = "#F7931E"
                    this.result_string = AppConstants.hr_result_average
                    this.range_string = "74-77"
                } else if (i18 >= 78 && i18 <= 83) {
                    this.color_string = "#F15A24"
                    this.result_string = AppConstants.hr_result_below_average
                    this.range_string = "74-77"
                } else if (i18 >= 84) {
                    this.color_string = "#ED1C24"
                    this.result_string = AppConstants.hr_result_poor
                    this.range_string = "74-77"
                }
            } else if (i13 > 65) {
                val i19 = this.heart_rate_value
                if (i19 == 54 || i19 <= 59) {
                    this.color_string = "#00A99D"
                    this.result_string = AppConstants.hr_result_athlete
                    this.range_string = "73-76"
                } else if (i19 >= 60 && i19 <= 64) {
                    this.color_string = "#8CC63F"
                    this.result_string = AppConstants.hr_result_excellent
                    this.range_string = "73-76"
                } else if (i19 >= 65 && i19 <= 68) {
                    this.color_string = "#D9E021"
                    this.result_string = AppConstants.hr_result_good
                    this.range_string = "73-76"
                } else if (i19 >= 69 && i19 <= 72) {
                    this.color_string = "#FBB03B"
                    this.result_string = AppConstants.hr_result_above_average
                    this.range_string = "73-76"
                } else if (i19 >= 73 && i19 <= 76) {
                    this.color_string = "#F7931E"
                    this.result_string = AppConstants.hr_result_average
                    this.range_string = "73-76"
                } else if (i19 >= 77 && i19 <= 84) {
                    this.color_string = "#F15A24"
                    this.result_string = AppConstants.hr_result_below_average
                    this.range_string = "73-76"
                } else if (i19 >= 85) {
                    this.color_string = "#ED1C24"
                    this.result_string = AppConstants.hr_result_poor
                    this.range_string = "73-76"
                }
            }
        }
    }

    private fun SetCurrentDateTime() {
        try {
            val instance = Calendar.getInstance()
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa")
            val format = simpleDateFormat.format(instance.time)
            this.current_date_time = format
            val parse = simpleDateFormat.parse(format)
            val simpleDateFormat2 = SimpleDateFormat("yyyy-MM-dd")
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
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
            val simpleDateFormat2 = SimpleDateFormat("dd")
            val simpleDateFormat3 = SimpleDateFormat("MM")
            val simpleDateFormat4 = SimpleDateFormat("yyyy")
            val simpleDateFormat5 = SimpleDateFormat("hh:mm aa")
            val simpleDateFormat6 = SimpleDateFormat("hh")
            val parse = SimpleDateFormat("yyyy-MM-dd hh:mm aa").parse(str3)
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
            resources.getString(R.string.lbl_header_heart_rate)
        findViewById<View>(R.id.toolbar_rel_save).setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                this@AddHeartRateActivity.SaveProcess()
            }
        })
        findViewById<View>(R.id.toolbar_rel_back).setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                this@AddHeartRateActivity.onBackPressed()
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


    private fun insertData(data: HeartRateData) {
        val token = sharedPreferencesUtils.read("token", "")
        if (!token.isNullOrEmpty()) {
            val call = ApiClient.apiService.postHeartRate(token, data)
            call.enqueue(object : Callback<HeartRateResponse> {
                override fun onResponse(
                    call: Call<HeartRateResponse>,
                    response: Response<HeartRateResponse>
                ) {
                    if (response.isSuccessful) {
                        showMessage(response.body()?.msg ?: "Saved successfully ")
                        onBackPressed()
                        return
                    } else {
                        showMessage("An error occurred trying to save data")
                    }
                }

                override fun onFailure(call: Call<HeartRateResponse>, t: Throwable) {
                    showMessage("An error occurred trying to save data " + t.localizedMessage)

                }

            })
        }
    }

    private fun updateData(data: HeartRateData) {
        val token = sharedPreferencesUtils.read("token", "")
        if (!token.isNullOrEmpty()) {
            val call = ApiClient.apiService.updateHeartRate(token, data)
            call.enqueue(object : Callback<HeartRateResponse> {
                override fun onResponse(
                    call: Call<HeartRateResponse>,
                    response: Response<HeartRateResponse>
                ) {
                    if (response.isSuccessful) {
                        showMessage("Updated successfully!")
                        onBackPressed()
                    } else {
                        showMessage("Unable to update")
                    }
                }

                override fun onFailure(call: Call<HeartRateResponse>, t: Throwable) {
                    showMessage("An error occurred updating. Try again later")
                }

            })
        }

    }

    private fun showMessage(message: String) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
    }
}
