package app.android.heartrate.phoneapp.activities.record.blood_sugar

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.text.format.DateFormat
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.android.heartrate.phoneapp.R
import app.android.heartrate.phoneapp.adapters.SpinnerProfileAdapter
import app.android.heartrate.phoneapp.databinding.ActivityAddBloodSugarBinding
import app.android.heartrate.phoneapp.model.bloodpressure.BloodPressureResponse
import app.android.heartrate.phoneapp.model.bloodsugar.BloodSugarResponse
import app.android.heartrate.phoneapp.model.classes.BloodPressureData
import app.android.heartrate.phoneapp.model.classes.BloodSugarData
import app.android.heartrate.phoneapp.retrofit.ApiClient
import app.android.heartrate.phoneapp.sharedpreferences.SharedPreferences
import app.android.heartrate.phoneapp.sqlite.SQLiteHealthTracker
import app.android.heartrate.phoneapp.sqlite.rulerpicker.RulerValuePicker
import app.android.heartrate.phoneapp.sqlite.rulerpicker.RulerValuePickerListener
import app.android.heartrate.phoneapp.utils.AppConstants
import app.android.heartrate.phoneapp.utils.EUGeneralClass
import com.github.mikephil.charting.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Arrays
import java.util.Calendar

class AddBloodSugarActivity() : AppCompatActivity() {
    var SQLite_health_tracker: SQLiteHealthTracker? = null
    var adagValue: String = ""
    var arrayListCurrentStatus: ArrayList<String> = ArrayList(
        Arrays.asList(
            AppConstants.pre_meal_testing,
            AppConstants.post_meal_testing,
            AppConstants.fasting_testing,
            AppConstants.general_testing
        )
    )

    var blood_sugar_result: String? = null
    var blood_sugar_value: Float = 0.0f
    var current_status: String? = null
    var current_status_spinner_adapter: ArrayAdapter<String>? = null
    var date: String = ""
    var date_time: String = ""
    var day: Int = 0
    var dcctValue: String = ""
    private lateinit var et_hemoglobin_level: EditText
    var et_ketone_level: EditText? = null
    var et_notes: EditText? = null
    var hemoLevel: Double = Utils.DOUBLE_EPSILON
    var hemoglobinLevel: String = ""
    var hemoglobinLevelLength: Int = 0
    var hour: Int = 0
    var ketoneLevel: String = ""
    var mContext: Context? = null
    var month: Int = 0
    var notes: String = ""
    var push_animation: Animation? = null
    private lateinit var rel_select_date: RelativeLayout
    private lateinit var rel_select_time: RelativeLayout
    var ruler_sugar_level: RulerValuePicker? = null
    var spinner_current_status: Spinner? = null
    var spinner_profile_adapter: SpinnerProfileAdapter? = null
    var spinner_profiles: Spinner? = null
    var status_color: String = "#8CC63F"
    var time: String = ""
    var toastMessage: String = ""
    private lateinit var txt_adag_level: TextView
    private lateinit var txt_bs_level: TextView
    private lateinit var txt_date: TextView
    var spinner_txt_name: TextView? = null
    private lateinit var txt_dcct_level: TextView
    private lateinit var txt_time: TextView
    var year: Int = 0
    private var current_date_time = ""
    private var save_entry_day = 0
    private var save_entry_hour = 0
    private var save_entry_minute = 0
    private var save_entry_month = 0
    private var save_entry_year = 0

    private lateinit var sharedPreferencesUtils: SharedPreferences

    private lateinit var binding: ActivityAddBloodSugarBinding

    fun CalculateADAG(d: Double): Double {
        return (d * 28.7) - 46.7
    }

    fun CalculateDCCT(d: Double): Double {
        return (d * 35.6) - 77.3
    }


    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        binding = ActivityAddBloodSugarBinding.inflate(layoutInflater)
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
        this.spinner_profiles = findViewById(R.id.add_bs_spinner_profiles)
        this.txt_bs_level = findViewById(R.id.add_bs_txt_bs_level)
        val rulerValuePicker = findViewById<RulerValuePicker>(R.id.add_bs_ruler_sugar_level)
        this.ruler_sugar_level = rulerValuePicker
        rulerValuePicker.selectValue(AppConstants.default_sugar_level_value)
        ruler_sugar_level!!.setIndicatorHeight(
            AppConstants.ruler_long_height_ration,
            AppConstants.ruler_short_height_ration
        )
        txt_bs_level.setText(AppConstants.default_sugar_level_value.toString())
        this.spinner_current_status = findViewById(R.id.add_bs_spinner_current_status)
        this.et_ketone_level = findViewById(R.id.add_bs_et_ketone_level)
        this.et_hemoglobin_level = findViewById(R.id.add_bs_et_hemoglobin_level)
        this.txt_adag_level = findViewById(R.id.add_bs_txt_adag_value)
        this.txt_dcct_level = findViewById(R.id.add_bs_txt_dcct_value)
        this.et_notes = findViewById(R.id.add_bs_et_notes)
        this.rel_select_date = findViewById(R.id.add_bs_rel_select_date)
        this.txt_date = findViewById(R.id.add_bs_txt_date)
        this.spinner_txt_name = findViewById(R.id.spinner_txt_name)
        this.rel_select_time = findViewById(R.id.add_bs_rel_select_time)
        this.txt_time = findViewById(R.id.add_bs_txt_time)
        SetProfileSpinner()
        SetCurrentStatusSpinner()
        SetCurrentDateTime()
        if (AppConstants.is_bs_edit_mode) {
            SetBloodSugarData()
        }
        rel_select_date.setOnClickListener(View.OnClickListener { view: View? ->
            this@AddBloodSugarActivity.hideSoftKeyboard()
            val instance: Calendar = Calendar.getInstance()
            this@AddBloodSugarActivity.save_entry_year = instance.get(1)
            this@AddBloodSugarActivity.save_entry_month = instance.get(2)
            this@AddBloodSugarActivity.save_entry_day = instance.get(5)
            DatePickerDialog(
                this@AddBloodSugarActivity,
                R.style.DialogTheme,
                object : OnDateSetListener {
                    override fun onDateSet(datePicker: DatePicker, i: Int, i2: Int, i3: Int) {
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
                    }
                },
                this@AddBloodSugarActivity.save_entry_year,
                this@AddBloodSugarActivity.save_entry_month,
                this@AddBloodSugarActivity.save_entry_day
            ).show()
        })
        rel_select_time.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val instance = Calendar.getInstance()
                this@AddBloodSugarActivity.save_entry_hour = instance[11]
                this@AddBloodSugarActivity.save_entry_minute = instance[12]
                TimePickerDialog(
                    this@AddBloodSugarActivity,
                    R.style.DialogTheme,
                    OnTimeSetListener { timePicker, i, i2 ->
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
                    },
                    this@AddBloodSugarActivity.save_entry_hour,
                    this@AddBloodSugarActivity.save_entry_minute,
                    false
                ).show()
            }
        })
        et_hemoglobin_level.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable) {
            }

            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
                if (charSequence.toString().length > 0) {
                    val decimalFormat = DecimalFormat("##")
                    txt_adag_level.setText(
                        decimalFormat.format(
                            this@AddBloodSugarActivity.CalculateADAG(
                                charSequence.toString().toDouble()
                            )
                        )
                    )
                    txt_dcct_level.setText(
                        decimalFormat.format(
                            this@AddBloodSugarActivity.CalculateDCCT(
                                charSequence.toString().toDouble()
                            )
                        )
                    )
                } else if (charSequence.toString().length == 0) {
                    txt_adag_level.setText("")
                    txt_dcct_level.setText("")
                }
            }
        })
        ruler_sugar_level!!.setValuePickerListener(object : RulerValuePickerListener {
            override fun onIntermediateValueChange(i: Int) {
            }

            override fun onValueChange(i: Int) {
                txt_bs_level.setText(i.toString())
            }
        })
    }

    private fun SetCurrentStatusSpinner() {
        val arrayAdapter = ArrayAdapter(this, R.layout.spinner_list, this.arrayListCurrentStatus)
        this.current_status_spinner_adapter = arrayAdapter
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list)
        spinner_current_status!!.adapter = this.current_status_spinner_adapter
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

    private fun SetBloodSugarData() {
        if (AppConstants.selected_bs_data != null) {
            this.date = AppConstants.selected_bs_data.date.trim { it <= ' ' }
            this.time = AppConstants.selected_bs_data.time.trim { it <= ' ' }
            this.current_status = AppConstants.selected_bs_data.current_status.trim { it <= ' ' }
            this.blood_sugar_value = AppConstants.selected_bs_data.sugar_level
            this.ketoneLevel = AppConstants.selected_bs_data.keton_level.toString()
            this.hemoglobinLevel = AppConstants.selected_bs_data.hemoglobin_level.toString()
            this.adagValue = AppConstants.selected_bs_data.blood_ADAG.toString()
            this.dcctValue = AppConstants.selected_bs_data.blood_DCCT.toString()
            this.status_color = AppConstants.selected_bs_data.status_color.trim { it <= ' ' }
            this.blood_sugar_result = AppConstants.selected_bs_data.result.trim { it <= ' ' }
            this.notes = AppConstants.selected_bs_data.notes.trim { it <= ' ' }
            ruler_sugar_level!!.selectValue(blood_sugar_value.toInt())
            ruler_sugar_level!!.setIndicatorHeight(
                AppConstants.ruler_long_height_ration,
                AppConstants.ruler_short_height_ration
            )
            txt_bs_level!!.text = blood_sugar_value.toString()
            if ((this.current_status == AppConstants.pre_meal_testing)) {
                spinner_current_status!!.setSelection(0)
            } else if ((this.current_status == AppConstants.post_meal_testing)) {
                spinner_current_status!!.setSelection(1)
            } else if ((this.current_status == AppConstants.fasting_testing)) {
                spinner_current_status!!.setSelection(2)
            } else if ((this.current_status == AppConstants.general_testing)) {
                spinner_current_status!!.setSelection(3)
            }
            txt_date!!.text = this.date
            txt_time!!.text = this.time
            et_ketone_level!!.setText(this.ketoneLevel)
            et_hemoglobin_level!!.setText(this.hemoglobinLevel)
            txt_adag_level!!.text = this.adagValue
            txt_dcct_level!!.text = this.dcctValue
            et_notes!!.setText(this.notes)
            return
        }
        EUGeneralClass.ShowErrorToast(this, "Something went wrong!")
    }


    private fun SaveProcess() {
        var z = false
        var editText: EditText? = null
        try {
            this.date = txt_date!!.text.toString().trim { it <= ' ' }
            this.time = txt_time!!.text.toString().trim { it <= ' ' }
            this.ketoneLevel =
                et_ketone_level!!.text.toString().trim { it <= ' ' }
            this.hemoglobinLevel =
                et_hemoglobin_level!!.text.toString().trim { it <= ' ' }
            this.adagValue = txt_adag_level!!.text.toString().trim { it <= ' ' }
            this.dcctValue = txt_dcct_level!!.text.toString().trim { it <= ' ' }
            this.notes = et_notes!!.text.toString().trim { it <= ' ' }
            this.hemoglobinLevelLength = hemoglobinLevel.length
            if (TextUtils.isEmpty(this.ketoneLevel)) {
                et_ketone_level!!.error = AppConstants.error_field_require
                editText = this.et_ketone_level
                z = true
            }
            if (z) {
                editText!!.requestFocus()
                return
            }
            val bloodSugarData = BloodSugarData()
            this.blood_sugar_value =
                ruler_sugar_level!!.currentValue.toFloat()
            this.current_status =
                spinner_current_status!!.selectedItem.toString()
            if (hemoglobinLevel.length == 0) {
                this.hemoLevel = Utils.DOUBLE_EPSILON
            } else {
                this.hemoLevel = hemoglobinLevel.toDouble()
            }
            GetResult()
            val str = this.blood_sugar_result
            if (str != null) {
                if (str.length != 0) {
                    if (!AppConstants.is_bs_edit_mode) {
                        bloodSugarData.user_id = sharedPreferencesUtils!!.getUserId()
                        bloodSugarData.date = date.trim { it <= ' ' }
                        bloodSugarData.time = time.trim { it <= ' ' }
                        bloodSugarData.current_status = current_status!!.trim { it <= ' ' }
                        bloodSugarData.sugar_level = this.blood_sugar_value
                        bloodSugarData.keton_level = ketoneLevel.trim { it <= ' ' }
                            .toFloat()
                        bloodSugarData.hemoglobin_level = hemoglobinLevel.trim { it <= ' ' }
                            .toFloat()
                        bloodSugarData.blood_ADAG = adagValue.trim { it <= ' ' }.toFloat()
                        bloodSugarData.blood_DCCT = dcctValue.trim { it <= ' ' }.toFloat()
                        bloodSugarData.status_color = status_color.trim { it <= ' ' }
                        bloodSugarData.result = blood_sugar_result!!.trim { it <= ' ' }
                        bloodSugarData.notes = notes.trim { it <= ' ' }
                        GetDateTime(date.trim { it <= ' ' }, time.trim { it <= ' ' })
                        bloodSugarData.dateTime = this.date_time
                        bloodSugarData.day = this.day
                        bloodSugarData.month = this.month
                        bloodSugarData.year = this.year
                        bloodSugarData.hour = this.hour
                        if ("".equals(AppConstants.values_not_natural, ignoreCase = true)) {
                            EUGeneralClass.ShowErrorToast(this, "")
                        } else {
                            insertData(bloodSugarData)
                        }
//                        SQLite_health_tracker!!.InsertBloodSugarData(bloodSugarData)


                    }


                    val i = AppConstants.selected_bs_data.row_id
                    bloodSugarData.user_id = sharedPreferencesUtils!!.getUserId()
                    bloodSugarData.date = date.trim { it <= ' ' }
                    bloodSugarData.time = time.trim { it <= ' ' }
                    bloodSugarData.current_status = current_status!!.trim { it <= ' ' }
                    bloodSugarData.sugar_level = this.blood_sugar_value
                    bloodSugarData.keton_level = ketoneLevel.trim { it <= ' ' }.toFloat()
                    bloodSugarData.hemoglobin_level = hemoglobinLevel.trim { it <= ' ' }
                        .toFloat()
                    bloodSugarData.blood_ADAG = adagValue.trim { it <= ' ' }.toFloat()
                    bloodSugarData.blood_DCCT = dcctValue.trim { it <= ' ' }.toFloat()
                    bloodSugarData.status_color = status_color.trim { it <= ' ' }
                    bloodSugarData.result = blood_sugar_result!!.trim { it <= ' ' }
                    bloodSugarData.notes = notes.trim { it <= ' ' }
                    GetDateTime(date.trim { it <= ' ' }, time.trim { it <= ' ' })
                    bloodSugarData.dateTime = this.date_time
                    bloodSugarData.day = this.day
                    bloodSugarData.month = this.month
                    bloodSugarData.year = this.year
                    bloodSugarData.hour = this.hour
                    bloodSugarData.row_id = i
                    if ("".equals(AppConstants.values_not_natural, ignoreCase = true)) {
                        EUGeneralClass.ShowErrorToast(this, "")
                    } else {
                        updateData(bloodSugarData)
                    }


//                    SQLite_health_tracker!!.UpdateBloodSugarData(
//                        i,
//                        sharedPreferencesUtils!!.getUserId(), bloodSugarData
//                    )
//                    EUGeneralClass.ShowSuccessToast(this, "Blood Sugar Data updated successfully!")
//                    onBackPressed()
//                    return
                }
            }
            EUGeneralClass.ShowErrorToast(
                this,
                "Values are not natural! Please enter natural value as par condition!"
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun GetResult() {
        if (hemoglobinLevel.equals("", ignoreCase = true) || hemoglobinLevel.equals(
                null,
                ignoreCase = true
            ) || hemoglobinLevel.isEmpty()
        ) {
            if ((this.current_status == AppConstants.pre_meal_testing)) {
                val f = this.blood_sugar_value
                if (f < 70.0f) {
                    this.blood_sugar_result = AppConstants.sugar_level_low
                    this.status_color = "#00A99D"
                } else if (f >= 70.0f && f <= 100.0f) {
                    this.blood_sugar_result = AppConstants.sugar_level_normal
                    this.status_color = "#8CC63F"
                } else if (f >= 101.0f && f < 126.0f) {
                    this.blood_sugar_result = AppConstants.sugar_level_pre_diabetes
                    this.status_color = "#FBB03B"
                } else if (f >= 126.0f) {
                    this.blood_sugar_result = AppConstants.sugar_level_diabetes
                    this.status_color = "#F15A24"
                } else {
                    this.toastMessage = AppConstants.values_not_natural
                }
            } else if ((this.current_status == AppConstants.post_meal_testing)) {
                val f2 = this.blood_sugar_value
                if (f2 < 70.0f) {
                    this.blood_sugar_result = AppConstants.sugar_level_low
                    this.status_color = "#00A99D"
                } else if (f2 >= 70.0f && f2 <= 140.0f) {
                    this.blood_sugar_result = AppConstants.sugar_level_normal
                    this.status_color = "#8CC63F"
                } else if (f2 >= 141.0f && f2 < 200.0f) {
                    this.blood_sugar_result = AppConstants.sugar_level_pre_diabetes
                    this.status_color = "#FBB03B"
                } else if (f2 >= 200.0f) {
                    this.blood_sugar_result = AppConstants.sugar_level_diabetes
                    this.status_color = "#F15A24"
                } else {
                    this.toastMessage = AppConstants.values_not_natural
                }
            } else if ((this.current_status == AppConstants.fasting_testing)) {
                val f3 = this.blood_sugar_value
                if (f3 < 70.0f) {
                    this.blood_sugar_result = AppConstants.sugar_level_low
                    this.status_color = "#00A99D"
                } else if (f3 >= 70.0f && f3 <= 99.0f) {
                    this.blood_sugar_result = AppConstants.sugar_level_normal
                    this.status_color = "#8CC63F"
                } else if (f3 >= 100.0f && f3 <= 125.0f) {
                    this.blood_sugar_result = AppConstants.sugar_level_pre_diabetes
                    this.status_color = "#FBB03B"
                } else if (f3 > 125.0f) {
                    this.blood_sugar_result = AppConstants.sugar_level_diabetes
                    this.status_color = "#F15A24"
                } else {
                    this.toastMessage = AppConstants.values_not_natural
                }
            } else if ((this.current_status == AppConstants.general_testing)) {
                val f4 = this.blood_sugar_value
                if (f4 < 70.0f) {
                    this.blood_sugar_result = AppConstants.sugar_level_low
                    this.status_color = "#00A99D"
                } else if (f4 >= 70.0f && f4 <= 140.0f) {
                    this.blood_sugar_result = AppConstants.sugar_level_normal
                    this.status_color = "#8CC63F"
                } else if (f4 >= 141.0f && f4 < 200.0f) {
                    this.blood_sugar_result = AppConstants.sugar_level_pre_diabetes
                    this.status_color = "#FBB03B"
                } else if (f4 >= 200.0f) {
                    this.blood_sugar_result = AppConstants.sugar_level_diabetes
                    this.status_color = "#F15A24"
                } else {
                    this.toastMessage = AppConstants.values_not_natural
                }
            }
        } else if (hemoglobinLevel.equals("", ignoreCase = true) && hemoglobinLevel.equals(
                null,
                ignoreCase = true
            ) && hemoglobinLevel.isEmpty()
        ) {
        } else {
            if ((this.current_status == AppConstants.pre_meal_testing)) {
                val f5 = this.blood_sugar_value
                if (f5 < 70.0f) {
                    val d = this.hemoLevel
                    if (d < 4.0 || (d >= 4.0 && d <= 5.1)) {
                        this.blood_sugar_result = AppConstants.sugar_level_low
                        this.status_color = "#00A99D"
                        return
                    }
                }
                if (f5 >= 70.0f && f5 <= 100.0f) {
                    val d2 = this.hemoLevel
                    if (d2 >= 5.2 && d2 <= 5.6) {
                        this.blood_sugar_result = AppConstants.sugar_level_normal
                        this.status_color = "#8CC63F"
                        return
                    }
                }
                if (f5 >= 101.0f && f5 < 126.0f) {
                    val d3 = this.hemoLevel
                    if (d3 >= 5.7 && d3 <= 6.4) {
                        this.blood_sugar_result = AppConstants.sugar_level_pre_diabetes
                        this.status_color = "#FBB03B"
                        return
                    }
                }
                if (f5 >= 126.0f) {
                    val d4 = this.hemoLevel
                    if ((d4 >= 6.5 && d4 <= 15.0) || d4 > 15.0) {
                        this.blood_sugar_result = AppConstants.sugar_level_diabetes
                        this.status_color = "#F15A24"
                        return
                    }
                }
                this.toastMessage = AppConstants.values_not_natural
            } else if ((this.current_status == AppConstants.post_meal_testing)) {
                val f6 = this.blood_sugar_value
                if (f6 < 70.0f) {
                    val d5 = this.hemoLevel
                    if (d5 < 4.0 || (d5 >= 4.0 && d5 <= 5.1)) {
                        this.blood_sugar_result = AppConstants.sugar_level_low
                        this.status_color = "#00A99D"
                        return
                    }
                }
                if (f6 >= 70.0f && f6 <= 140.0f) {
                    val d6 = this.hemoLevel
                    if (d6 >= 5.2 && d6 <= 5.6) {
                        this.blood_sugar_result = AppConstants.sugar_level_normal
                        this.status_color = "#8CC63F"
                        return
                    }
                }
                if (f6 >= 141.0f && f6 < 200.0f) {
                    val d7 = this.hemoLevel
                    if (d7 >= 5.7 && d7 <= 6.4) {
                        this.blood_sugar_result = AppConstants.sugar_level_pre_diabetes
                        this.status_color = "#FBB03B"
                        return
                    }
                }
                if (f6 >= 200.0f) {
                    val d8 = this.hemoLevel
                    if ((d8 >= 6.5 && d8 <= 15.0) || d8 > 15.0) {
                        this.blood_sugar_result = AppConstants.sugar_level_diabetes
                        this.status_color = "#F15A24"
                        return
                    }
                }
                this.toastMessage = AppConstants.values_not_natural
            } else if ((this.current_status == AppConstants.fasting_testing)) {
                val f7 = this.blood_sugar_value
                if (f7 < 70.0f) {
                    val d9 = this.hemoLevel
                    if (d9 < 4.0 || (d9 >= 4.0 && d9 <= 5.1)) {
                        this.blood_sugar_result = AppConstants.sugar_level_low
                        this.status_color = "#00A99D"
                        return
                    }
                }
                if (f7 >= 70.0f && f7 <= 99.0f) {
                    val d10 = this.hemoLevel
                    if (d10 >= 5.2 && d10 <= 5.6) {
                        this.blood_sugar_result = AppConstants.sugar_level_normal
                        this.status_color = "#8CC63F"
                        return
                    }
                }
                if (f7 >= 100.0f && f7 <= 125.0f) {
                    val d11 = this.hemoLevel
                    if (d11 >= 5.7 && d11 <= 6.4) {
                        this.blood_sugar_result = AppConstants.sugar_level_pre_diabetes
                        this.status_color = "#FBB03B"
                        return
                    }
                }
                if (f7 > 125.0f) {
                    val d12 = this.hemoLevel
                    if ((d12 >= 6.5 && d12 <= 15.0) || d12 > 15.0) {
                        this.blood_sugar_result = AppConstants.sugar_level_diabetes
                        this.status_color = "#F15A24"
                        return
                    }
                }
                this.toastMessage = AppConstants.values_not_natural
            } else if ((this.current_status == AppConstants.general_testing)) {
                val f8 = this.blood_sugar_value
                if (f8 < 70.0f) {
                    val d13 = this.hemoLevel
                    if (d13 < 4.0 || (d13 >= 4.0 && d13 <= 5.1)) {
                        this.blood_sugar_result = AppConstants.sugar_level_low
                        this.status_color = "#00A99D"
                        return
                    }
                }
                if (f8 >= 70.0f && f8 <= 140.0f) {
                    val d14 = this.hemoLevel
                    if (d14 >= 5.2 && d14 <= 5.6) {
                        this.blood_sugar_result = AppConstants.sugar_level_normal
                        this.status_color = "#8CC63F"
                        return
                    }
                }
                if (f8 >= 141.0f && f8 < 200.0f) {
                    val d15 = this.hemoLevel
                    if (d15 >= 5.7 && d15 <= 6.4) {
                        this.blood_sugar_result = AppConstants.sugar_level_pre_diabetes
                        this.status_color = "#FBB03B"
                        return
                    }
                }
                if (f8 >= 200.0f) {
                    val d16 = this.hemoLevel
                    if ((d16 >= 6.5 && d16 <= 15.0) || d16 > 15.0) {
                        this.blood_sugar_result = AppConstants.sugar_level_diabetes
                        this.status_color = "#F15A24"
                        return
                    }
                }
                this.toastMessage = AppConstants.values_not_natural
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
            resources.getString(R.string.lbl_header_blood_sugar)
        findViewById<View>(R.id.toolbar_rel_save).setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                SaveProcess()
            }
        })
        findViewById<View>(R.id.toolbar_rel_back).setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                this@AddBloodSugarActivity.onBackPressed()
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


    private fun insertData(bloodSugarData: BloodSugarData) {
        val token = sharedPreferencesUtils.read("token", "")
        if (!token.isNullOrEmpty()) {
            val call = ApiClient.apiService.postBloodSugar(token, bloodSugarData)
            call.enqueue(object : Callback<BloodSugarResponse> {
                override fun onResponse(
                    call: Call<BloodSugarResponse>,
                    response: Response<BloodSugarResponse>
                ) {
                    if (response.isSuccessful) {
                        showMessage(response.body()?.msg ?: "Saved successfully ")
                        onBackPressed()
                        return
                    } else {
                        showMessage("An error occurred trying to save data")
                    }
                }

                override fun onFailure(call: Call<BloodSugarResponse>, t: Throwable) {
                    showMessage("An error occurred trying to save data " + t.localizedMessage)

                }

            })
        }
    }

    private fun updateData(bloodSugarData: BloodSugarData) {
        val token = sharedPreferencesUtils.read("token", "")
        if (!token.isNullOrEmpty()) {
            val call = ApiClient.apiService.updateBloodSugar(token, bloodSugarData)
            call.enqueue(object : Callback<BloodSugarData> {
                override fun onResponse(
                    call: Call<BloodSugarData>,
                    response: Response<BloodSugarData>
                ) {
                    if (response.isSuccessful) {
                        showMessage("Updated successfully!")
                        onBackPressed()
                    } else {
                        showMessage("Unable to update")
                    }
                }

                override fun onFailure(call: Call<BloodSugarData>, t: Throwable) {
                    showMessage("An error occurred updating. Try again later")
                }

            })
        }

    }

    private fun showMessage(message: String) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
    }


}
