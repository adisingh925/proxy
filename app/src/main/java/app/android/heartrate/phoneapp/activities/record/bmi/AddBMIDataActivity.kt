package app.android.heartrate.phoneapp.activities.record.bmi

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import app.android.heartrate.phoneapp.R
import app.android.heartrate.phoneapp.adapters.SpinnerProfileAdapter
import app.android.heartrate.phoneapp.model.classes.BMICalcForKg
import app.android.heartrate.phoneapp.model.classes.BMICalcForPound
import app.android.heartrate.phoneapp.model.classes.BMIData
import app.android.heartrate.phoneapp.model.classes.IBMICalc
import app.android.heartrate.phoneapp.sharedpreferences.SharedPreferences
import app.android.heartrate.phoneapp.sqlite.SQLiteHealthTracker
import app.android.heartrate.phoneapp.utils.AppConstants
import app.android.heartrate.phoneapp.utils.EUGeneralClass
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar

class AddBMIDataActivity() : AppCompatActivity() {
    var SQLite_health_tracker: SQLiteHealthTracker? = null
    var age_unit: String = ""
    var age_value: Int = 0
    var current_date: String = ""
    var current_time: String = ""
    var date: String = ""
    var date_time: String = ""
    var day: Int = 0
    var default_age: Int = 20
    var default_weight: Int = 40
    private lateinit var et_age: EditText
    var et_height: EditText? = null
    private lateinit var et_weight: EditText
    var heightUnit: String = ""
    var height_value: Float = 0.0f
    var hour: Int = 0
    var lbl_imperial: TextView? = null
    var lbl_metric: TextView? = null
    private lateinit var lin_imperial: LinearLayout
    private lateinit var lin_metric: LinearLayout
    var mContext: Context? = null
    var month: Int = 0
    var push_animation: Animation? = null
    private lateinit var rel_select_date: RelativeLayout
    private lateinit var rel_select_time: RelativeLayout
    var spinner_profile_adapter: SpinnerProfileAdapter? = null
    var spinner_profiles: Spinner? = null
    var time: String = ""
    private lateinit var txt_age_unit: TextView
    private lateinit var txt_date: TextView
    var spinner_txt_name: TextView? = null
    var txt_height_unit: TextView? = null
    private lateinit var txt_time: TextView
    var txt_weight_unit: TextView? = null
    var view_line_imperial: View? = null
    var view_line_metric: View? = null
    var weightUnit: String = ""
    var weight_value: Float = 0.0f
    var year: Int = 0
    private var current_date_time = ""
    private var indexOfCheckedItemInMenu = 0
    private var save_entry_day = 0
    private var save_entry_hour = 0
    private var save_entry_minute = 0
    private var save_entry_month = 0
    private var save_entry_year = 0

    private var sharedPreferencesUtils: SharedPreferences? = null

    fun CalculateADAG(d: Double): Double {
        return (d * 28.7) - 46.7
    }

    fun CalculateDCCT(d: Double): Double {
        return (d * 35.6) - 77.3
    }


    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        SetView()


        AppConstants.overridePendingTransitionEnter(this)
    }

    private fun SetView() {
        setContentView(R.layout.activity_add_bmi)

        this.mContext = this
        activity_add_bmi = this
        this.push_animation = AnimationUtils.loadAnimation(this, R.anim.view_push)
        setUpActionBar()
        sharedPreferencesUtils = SharedPreferences
        this.indexOfCheckedItemInMenu = 1
        val sQLiteHealthTracker = SQLiteHealthTracker(this)
        this.SQLite_health_tracker = sQLiteHealthTracker
        sQLiteHealthTracker.openToWrite()
        this.spinner_profiles = findViewById(R.id.add_bmi_spinner_profiles)
        this.rel_select_date = findViewById(R.id.add_bmi_rel_select_date)
        this.txt_date = findViewById(R.id.add_bmi_txt_date)
        this.rel_select_time = findViewById(R.id.add_bmi_rel_select_time)
        this.txt_time = findViewById(R.id.add_bmi_txt_time)
        this.lin_metric = findViewById(R.id.add_bmi_lin_metric)
        this.lin_imperial = findViewById(R.id.add_bmi_lin_imperial)
        this.lbl_metric = findViewById(R.id.add_bmi_lbl_metric)
        this.lbl_imperial = findViewById(R.id.add_bmi_lbl_imperial)
        this.view_line_metric = findViewById(R.id.add_bmi_hr_metric)
        this.view_line_imperial = findViewById(R.id.add_bmi_hr_imperial)
        this.txt_age_unit = findViewById(R.id.add_bmi_txt_age_unit)
        this.spinner_txt_name = findViewById(R.id.spinner_txt_name)
        this.et_age = findViewById(R.id.add_bmi_et_age)
        this.et_weight = findViewById(R.id.add_bmi_et_weight)
        txt_age_unit.setText(AppConstants.age_unit_years)
        val textView = findViewById<TextView>(R.id.add_bmi_txt_weight_unit)
        this.txt_weight_unit = textView
        textView.text = "Kilogram"
        this.txt_height_unit = findViewById(R.id.add_bmi_txt_height_unit)
        this.et_height = findViewById(R.id.add_bmi_et_height)
        et_age.setText(AppConstants.default_age_value.toString())
        et_weight.setText(AppConstants.default_weight_value.toString())
        SetMetricSystemUI()
        SetProfileSpinner()
        SetCurrentDateTime()
        if (AppConstants.is_bmi_edit_mode) {
            SetBMIData()
        }
        rel_select_date.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                this@AddBMIDataActivity.hideSoftKeyboard()
                val instance = Calendar.getInstance()
                this@AddBMIDataActivity.save_entry_year = instance[1]
                this@AddBMIDataActivity.save_entry_month = instance[2]
                this@AddBMIDataActivity.save_entry_day = instance[5]
                DatePickerDialog(
                    this@AddBMIDataActivity,
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
                    this@AddBMIDataActivity.save_entry_year,
                    this@AddBMIDataActivity.save_entry_month,
                    this@AddBMIDataActivity.save_entry_day
                ).show()
            }
        })
        rel_select_time.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val instance = Calendar.getInstance()
                this@AddBMIDataActivity.save_entry_hour = instance[11]
                this@AddBMIDataActivity.save_entry_minute = instance[12]
                TimePickerDialog(
                    this@AddBMIDataActivity,
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
                    this@AddBMIDataActivity.save_entry_hour,
                    this@AddBMIDataActivity.save_entry_minute,
                    false
                ).show()
            }
        })
        lin_metric.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                this@AddBMIDataActivity.indexOfCheckedItemInMenu = 1
                this@AddBMIDataActivity.SetMetricSystemUI()
            }
        })
        lin_imperial.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                this@AddBMIDataActivity.indexOfCheckedItemInMenu = 2
                this@AddBMIDataActivity.SetImperialSystemUI()
            }
        })
    }


    private fun SetMetricSystemUI() {
        lin_metric!!.setBackgroundResource(R.drawable.radio_unselected_bg)
        lin_imperial!!.setBackgroundResource(R.drawable.radio_unselected_bg)
        lbl_metric!!.setTextColor(resources.getColor(R.color.bmi_tab_selected_text_color))
        lbl_imperial!!.setTextColor(resources.getColor(R.color.bmi_tab_normal_text_color))
        view_line_metric!!.visibility = View.VISIBLE
        view_line_imperial!!.visibility = View.INVISIBLE
        txt_weight_unit!!.text = "Kilogram"
        txt_height_unit!!.text = "Height (Meter) : "
        et_age!!.setText(default_age.toString())
        et_weight!!.setText(default_weight.toString())
        et_height!!.setText("1.1")
        val editText = this.et_height
        editText!!.setSelection(editText.text.toString().length)
    }


    private fun SetImperialSystemUI() {
        lin_metric!!.setBackgroundResource(R.drawable.radio_unselected_bg)
        lin_imperial!!.setBackgroundResource(R.drawable.radio_unselected_bg)
        lbl_metric!!.setTextColor(resources.getColor(R.color.bmi_tab_normal_text_color))
        lbl_imperial!!.setTextColor(resources.getColor(R.color.bmi_tab_selected_text_color))
        view_line_metric!!.visibility = View.INVISIBLE
        view_line_imperial!!.visibility = View.VISIBLE
        txt_weight_unit!!.text = "Pound (Lb)"
        txt_height_unit!!.text = "Height (Inch) : "
        et_age!!.setText(default_age.toString())
        et_weight!!.setText(default_weight.toString())
        et_height!!.setText("43.3")
        val editText = this.et_height
        editText!!.setSelection(editText.text.toString().length)
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

    private fun SetBMIData() {
        if (AppConstants.selected_bmi_data != null) {
            val trim = AppConstants.selected_bmi_data.weight.trim { it <= ' ' }
            val trim2 = AppConstants.selected_bmi_data.weight_unit.trim { it <= ' ' }
            val trim3 = AppConstants.selected_bmi_data.height.trim { it <= ' ' }
            AppConstants.selected_bmi_data.height_unit.trim { it <= ' ' }
            val trim4 = AppConstants.selected_bmi_data.age.trim { it <= ' ' }
            AppConstants.selected_bmi_data.bmi.trim { it <= ' ' }
            AppConstants.selected_bmi_data.birth_date.trim { it <= ' ' }
            if (trim2.equals(AppConstants.bmi_unit_kg, ignoreCase = true)) {
                this.indexOfCheckedItemInMenu = 1
                SetMetricSystemUI()
            } else if (trim2.equals(AppConstants.bmi_unit_lb, ignoreCase = true)) {
                this.indexOfCheckedItemInMenu = 2
                SetImperialSystemUI()
            }
            this.age_value = trim4.toInt()
            et_age!!.setText(trim4)
            et_weight!!.setText(trim)
            val i = this.age_value
            if (i == 1) {
                val str = AppConstants.age_unit_year
                this.age_unit = str
                txt_age_unit!!.text = str
            } else if (i > 1) {
                val str2 = AppConstants.age_unit_years
                this.age_unit = str2
                txt_age_unit!!.text = str2
            }
            et_height!!.setText(trim3)
            val editText = this.et_height
            editText!!.setSelection(editText.text.toString().length)
            return
        }
        EUGeneralClass.ShowErrorToast(this, "Something went wrong!")
    }


    private fun SaveProcess() {
        var z: Boolean
        try {
            toastMessage = ""
            var editText: EditText? = null
            this.date = txt_date!!.text.toString().trim { it <= ' ' }
            this.time = txt_time!!.text.toString().trim { it <= ' ' }
            val trim = et_age!!.text.toString().trim { it <= ' ' }
            val trim2 = et_weight!!.text.toString().trim { it <= ' ' }
            val obj = et_height!!.text.toString()
            var z2 = false
            if (trim.length == 0) {
                et_age!!.error = AppConstants.error_field_require
                editText = this.et_age
                z = true
            } else {
                this.age_value =
                    et_age!!.text.toString().trim { it <= ' ' }.toInt()
                z = false
            }
            if (trim2.length == 0) {
                et_weight!!.error = AppConstants.error_field_require
                editText = this.et_weight
                z = true
            } else {
                this.weight_value =
                    et_weight!!.text.toString().trim { it <= ' ' }.toFloat()
            }
            if (obj.length == 0) {
                et_height!!.error = AppConstants.error_field_require
                editText = this.et_height
            } else {
                val parseFloat = obj.toFloat()
                this.height_value = parseFloat
                val i = this.indexOfCheckedItemInMenu
                if (i != 1) {
                    if (i != 2) {
                        z2 = z
                    } else if ((parseFloat.toDouble()) < 19.68 || (parseFloat.toDouble()) > 98.42) {
                        et_height!!.error = AppConstants.error_height_range_pound
                        editText = this.et_height
                    }
                    if (z2) {
                    }
                } else if ((parseFloat.toDouble()) < 0.5 || (parseFloat.toDouble()) > 2.5) {
                    et_height!!.error = AppConstants.error_height_range_kg
                    editText = this.et_height
                } else if (z2) {
                    editText!!.requestFocus()
                    return
                } else {
                    val bMIData = BMIData()
                    val countBMI = bMICalc.countBMI(this.weight_value, this.height_value)
                    val i2 = (if (countBMI > -1.0f) 1 else (if (countBMI == -1.0f) 0 else -1))
                    if (i2 != 0) {
                        val i3 = this.indexOfCheckedItemInMenu
                        if (i3 == 1) {
                            this.weightUnit = "kg"
                            this.heightUnit = "m"
                        } else if (i3 == 2) {
                            this.weightUnit = "lb"
                            this.heightUnit = "in"
                        }
                        SetCurrentDateTime()
                        val format =
                            SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().time)
                        if (!AppConstants.is_bmi_edit_mode) {
                            bMIData.user_id = sharedPreferencesUtils!!.getUserId()
                            bMIData.date = date.trim { it <= ' ' }
                            bMIData.time = time.trim { it <= ' ' }
                            bMIData.weight = weight_value.toString()
                            bMIData.weight_unit = weightUnit.trim { it <= ' ' }
                            bMIData.height = height_value.toString()
                            bMIData.height_unit = heightUnit.trim { it <= ' ' }
                            bMIData.age = age_value.toString()
                            bMIData.bmi = countBMI.toString()
                            bMIData.birth_date = format.trim { it <= ' ' }
                            GetDateTime(date.trim { it <= ' ' }, time.trim { it <= ' ' })
                            bMIData.dateTime = this.date_time
                            bMIData.day = this.day
                            bMIData.month = this.month
                            bMIData.year = this.year
                            bMIData.hour = this.hour
                            if (toastMessage.equals(
                                    AppConstants.values_not_natural,
                                    ignoreCase = true
                                )
                            ) {
                                EUGeneralClass.ShowErrorToast(this, toastMessage)
                                return
                            } else if (i2 != 0) {
                                SQLite_health_tracker!!.InsertBMIData(bMIData)
                                toastMessage = "BMI Data saved successfully!"
                                EUGeneralClass.ShowSuccessToast(
                                    this,
                                    "BMI Data saved successfully!"
                                )
                                BMIResultScreen(countBMI)
                                return
                            } else {
                                toastMessage = "Wrong values are give so problem in update data!"
                                EUGeneralClass.ShowErrorToast(
                                    this,
                                    "Wrong values are give so problem in update data!"
                                )
                                return
                            }
                        } else {
                            val i4 = AppConstants.selected_bmi_data.row_id
                            bMIData.user_id = sharedPreferencesUtils!!.getUserId()
                            bMIData.date = date.trim { it <= ' ' }
                            bMIData.time = time.trim { it <= ' ' }
                            bMIData.weight = weight_value.toString()
                            bMIData.weight_unit = weightUnit.trim { it <= ' ' }
                            bMIData.height = height_value.toString()
                            bMIData.height_unit = heightUnit.trim { it <= ' ' }
                            bMIData.age = age_value.toString()
                            bMIData.bmi = countBMI.toString()
                            bMIData.birth_date = format.trim { it <= ' ' }
                            GetDateTime(date.trim { it <= ' ' }, time.trim { it <= ' ' })
                            bMIData.dateTime = this.date_time
                            bMIData.day = this.day
                            bMIData.month = this.month
                            bMIData.year = this.year
                            bMIData.hour = this.hour
                            if (toastMessage.equals(
                                    AppConstants.values_not_natural,
                                    ignoreCase = true
                                )
                            ) {
                                EUGeneralClass.ShowErrorToast(this, toastMessage)
                                return
                            } else if (i2 != 0) {
                                SQLite_health_tracker!!.UpdateBMIData(
                                    i4,
                                    sharedPreferencesUtils!!.getUserId(), bMIData
                                )
                                toastMessage = "BMI Data updated successfully!"
                                EUGeneralClass.ShowSuccessToast(
                                    this,
                                    "BMI Data updated successfully!"
                                )
                                onBackPressed()
                                return
                            } else {
                                toastMessage = "Wrong values are give so problem in update data!"
                                EUGeneralClass.ShowErrorToast(
                                    this,
                                    "Wrong values are give so problem in update data!"
                                )
                                return
                            }
                        }
                    } else {
                        et_age!!.setText(default_age.toString())
                        et_weight!!.setText(default_weight.toString())
                        et_height!!.setText("1.1")
                        val editText2 = this.et_height
                        editText2!!.setSelection(editText2.text.toString().length)
                        toastMessage = "Something went wrong for insert data!"
                        EUGeneralClass.ShowSuccessToast(
                            this,
                            "Something went wrong for insert data!"
                        )
                        return
                    }
                }
            }
            z2 = true
            if (z2) {
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun BMIResultScreen(f: Float) {
        val intent = Intent(this, BMIResultActivity::class.java)
        intent.putExtra("ResultText", AppConstants.setResultText(f))
        intent.putExtra("BMI", f.toString())
        startActivity(intent)
        finish()
    }

    private val bMICalc: IBMICalc
        get() {
            val i = this.indexOfCheckedItemInMenu
            if (i == 1) {
                return BMICalcForKg()
            }
            if (i == 2) {
                return BMICalcForPound()
            }
            toastMessage = AppConstants.values_not_natural
            throw IllegalArgumentException("Unknown checked index in menu")
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
            this.current_date = simpleDateFormat2.format(parse)
            this.current_time = simpleDateFormat3.format(parse)
            this.current_time =
                DateFormat.format("hh:mm aaa", Calendar.getInstance().time) as String
            txt_date!!.text = this.current_date
            txt_time!!.text = this.current_time
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
            resources.getString(R.string.lbl_header_bmi)
        findViewById<View>(R.id.toolbar_rel_save).setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                this@AddBMIDataActivity.SaveProcess()
            }
        })
        findViewById<View>(R.id.toolbar_rel_back).setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                this@AddBMIDataActivity.onBackPressed()
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
        val IMPERIAL_SYSTEM_INDEX: Int = 2
        val METRIC_SYSTEM_INDEX: Int = 1
        var activity_add_bmi: Activity? = null
        var toastMessage: String = ""
    }
}
