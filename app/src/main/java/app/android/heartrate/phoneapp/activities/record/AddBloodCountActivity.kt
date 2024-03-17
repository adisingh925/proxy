package app.android.heartrate.phoneapp.activities.record

import android.annotation.SuppressLint
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
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.TimePicker
import app.android.heartrate.phoneapp.R
import app.android.heartrate.phoneapp.databinding.ActivityAddBloodCountBinding
import app.android.heartrate.phoneapp.fragments.base.BaseActivity
import app.android.heartrate.phoneapp.model.classes.BloodCountData
import app.android.heartrate.phoneapp.retrofit.ApiClient
import app.android.heartrate.phoneapp.sharedpreferences.SharedPreferences
import app.android.heartrate.phoneapp.sqlite.SQLiteHealthTracker
import app.android.heartrate.phoneapp.utils.AppConstants
import app.android.heartrate.phoneapp.utils.EUGeneralClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar

class AddBloodCountActivity() : BaseActivity() {

    var SQLite_health_tracker: SQLiteHealthTracker? = null
    var date_time: String = ""
    var day: Int = 0
    private lateinit var et_hemoglobin_value: EditText
    private lateinit var et_notes: EditText
    private lateinit var et_platelets_value: EditText
    private lateinit var et_rbc_value: EditText
    private lateinit var et_wbc_value: EditText
    var hour: Int = 0
    var minute: Int = 0
    var mContext: Context? = null
    var month: Int = 0
    var push_animation: Animation? = null
    private lateinit var rel_select_date: RelativeLayout
    private lateinit var rel_select_time: RelativeLayout
    var spinner_profiles: Spinner? = null
    private lateinit var txt_date: TextView
    private lateinit var txt_time: TextView
    private lateinit var spinner_txt_name: TextView
    var year: Int = 0
    private var current_date_time = ""
    private var save_entry_day = 0
    private var save_entry_hour = 0
    private var save_entry_minute = 0
    private var save_entry_month = 0
    private var save_entry_year = 0

    private lateinit var sharedPreferencesUtils: SharedPreferences

    private lateinit var binding: ActivityAddBloodCountBinding
    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        binding = ActivityAddBloodCountBinding.inflate(layoutInflater)
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
        this.spinner_profiles = findViewById(R.id.add_bc_spinner_profiles)
        this.et_rbc_value = findViewById(R.id.add_bc_et_rbc_value)
        this.et_wbc_value = findViewById(R.id.add_bc_et_wbc_value)
        this.et_platelets_value = findViewById(R.id.add_bc_et_platelets_value)
        this.et_hemoglobin_value = findViewById(R.id.add_bc_et_hemoglobin)
        this.et_notes = findViewById(R.id.add_bc_et_notes)
        this.rel_select_date = findViewById(R.id.add_bc_rel_select_date)
        this.txt_date = findViewById(R.id.add_bc_txt_date)
        this.rel_select_time = findViewById(R.id.add_bc_rel_select_time)
        this.txt_time = findViewById(R.id.add_bc_txt_time)
        this.spinner_txt_name = findViewById(R.id.spinner_txt_name)
        SetProfileSpinner()
        SetCurrentDateTime()
        if (AppConstants.is_blood_count_edit_mode) {
            SetBloodCountData()
        }
        rel_select_date.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                this@AddBloodCountActivity.hideSoftKeyboard()
                val instance = Calendar.getInstance()
                this@AddBloodCountActivity.save_entry_year = instance[1]
                this@AddBloodCountActivity.save_entry_month = instance[2]
                this@AddBloodCountActivity.save_entry_day = instance[5]
                DatePickerDialog(
                    this@AddBloodCountActivity,
                    R.style.DialogTheme,
                    OnDateSetListener { datePicker, i, i2, i3 ->
                        try {
                            txt_date.setText(
                                SimpleDateFormat("yyyy-MM-dd").format(
                                    SimpleDateFormat("dd/MM/yyyy").parse(
                                        (i3.toString() + "/" + (i2 + 1) + "/" + i).trim()
                                    )
                                )
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    },
                    this@AddBloodCountActivity.save_entry_year,
                    this@AddBloodCountActivity.save_entry_month,
                    this@AddBloodCountActivity.save_entry_day
                ).show()
            }
        })
        rel_select_time.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val instance = Calendar.getInstance()
                this@AddBloodCountActivity.save_entry_hour = instance[11]
                this@AddBloodCountActivity.save_entry_minute = instance[12]
                TimePickerDialog(
                    this@AddBloodCountActivity,
                    R.style.DialogTheme,
                    object : OnTimeSetListener {
                        override fun onTimeSet(timePicker: TimePicker, i: Int, i2: Int) {
                            try {
                                txt_time.setText(
                                    SimpleDateFormat("hh:mm:ss aa").format(
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
                    this@AddBloodCountActivity.save_entry_hour,
                    this@AddBloodCountActivity.save_entry_minute,
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

    private fun SetBloodCountData() {
        if (AppConstants.selected_blood_count_data != null) {
            val trim = AppConstants.selected_blood_count_data.date.trim { it <= ' ' }
            val trim2 = AppConstants.selected_blood_count_data.time.trim { it <= ' ' }
            val valueOf = AppConstants.selected_blood_count_data.rbc_value.toString()
            val valueOf2 = AppConstants.selected_blood_count_data.wbc_value.toString()
            val valueOf3 = AppConstants.selected_blood_count_data.platelets_value.toString()
            val valueOf4 = AppConstants.selected_blood_count_data.hemoglobin_value.toString()
            val trim3 = AppConstants.selected_blood_count_data.notes.trim { it <= ' ' }
            txt_date!!.text = trim
            txt_time!!.text = trim2
            et_rbc_value!!.setText(valueOf)
            et_wbc_value!!.setText(valueOf2)
            et_platelets_value!!.setText(valueOf3)
            et_hemoglobin_value!!.setText(valueOf4)
            et_notes!!.setText(trim3)
            val editText = this.et_rbc_value
            editText!!.setSelection(editText.text.toString().trim { it <= ' ' }.length)
            val editText2 = this.et_wbc_value
            editText2!!.setSelection(editText2.text.toString().trim { it <= ' ' }.length)
            val editText3 = this.et_platelets_value
            editText3!!.setSelection(editText3.text.toString().trim { it <= ' ' }.length)
            val editText4 = this.et_hemoglobin_value
            editText4!!.setSelection(editText4.text.toString().trim { it <= ' ' }.length)
            val editText5 = this.et_notes
            editText5!!.setSelection(editText5.text.toString().trim { it <= ' ' }.length)
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
            val trim3 = et_rbc_value!!.text.toString().trim { it <= ' ' }
            val trim4 = et_wbc_value!!.text.toString().trim { it <= ' ' }
            val trim5 =
                et_platelets_value!!.text.toString().trim { it <= ' ' }
            val trim6 =
                et_hemoglobin_value!!.text.toString().trim { it <= ' ' }
            val trim7 = et_notes!!.text.toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(trim3)) {
                et_rbc_value!!.error = AppConstants.error_field_require
                editText = this.et_rbc_value
                z = true
            }
            if (z) {
                editText!!.requestFocus()
                return
            }
            val bloodCountData = BloodCountData()
            if (!AppConstants.is_blood_count_edit_mode) {
                bloodCountData.user_id = sharedPreferencesUtils!!.getUserId()
                bloodCountData.date = trim.trim { it <= ' ' }
                bloodCountData.time = trim2.trim { it <= ' ' }
                bloodCountData.rbc_value = trim3.trim { it <= ' ' }.toFloat()
                bloodCountData.wbc_value = trim4.trim { it <= ' ' }.toFloat()
                bloodCountData.platelets_value = trim5.trim { it <= ' ' }.toFloat()
                bloodCountData.hemoglobin_value = trim6.trim { it <= ' ' }.toFloat()
                bloodCountData.notes = trim7.trim { it <= ' ' }
                GetDateTime(trim.trim { it <= ' ' }, trim2.trim { it <= ' ' })
                bloodCountData.dateTime = this.date_time
                bloodCountData.day = this.day
                bloodCountData.month = this.month
                bloodCountData.year = this.year
                bloodCountData.hour = this.hour
                bloodCountData.minute = this.minute
//                SQLite_health_tracker!!.InsertBloodCountData(bloodCountData)
                EUGeneralClass.ShowSuccessToast(this, "Blood Count Data saved successfully!")
                insertData(bloodCountData)

            }
            val i = AppConstants.selected_blood_count_data.row_id
            bloodCountData.user_id = sharedPreferencesUtils!!.getUserId()
            bloodCountData.date = trim.trim { it <= ' ' }
            bloodCountData.time = trim2.trim { it <= ' ' }
            bloodCountData.rbc_value = trim3.trim { it <= ' ' }.toFloat()
            bloodCountData.wbc_value = trim4.trim { it <= ' ' }.toFloat()
            bloodCountData.platelets_value = trim5.trim { it <= ' ' }.toFloat()
            bloodCountData.hemoglobin_value = trim6.trim { it <= ' ' }.toFloat()
            bloodCountData.notes = trim7.trim { it <= ' ' }
            GetDateTime(trim.trim { it <= ' ' }, trim2.trim { it <= ' ' })
            bloodCountData.dateTime = this.date_time
            bloodCountData.day = this.day
            bloodCountData.month = this.month
            bloodCountData.year = this.year
            bloodCountData.hour = this.hour
            bloodCountData.minute = this.minute
            SQLite_health_tracker!!.UpdateBloodCountData(
                i,
                sharedPreferencesUtils!!.getUserId(), bloodCountData
            )
            EUGeneralClass.ShowSuccessToast(this, "Blood Count Data updated successfully!")
            updateBloodCount(bloodCountData)
        } catch (e: Exception) {
            e.printStackTrace()
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
            val simpleDateFormat3 = SimpleDateFormat("hh:mm:ss aa")
            val format2 = simpleDateFormat2.format(parse)
            simpleDateFormat3.format(parse)
            Log.e("Current Date Time:", this.current_date_time)
            txt_date!!.text = format2
            txt_time!!.text = DateFormat.format("hh:mm:ss aaa", Calendar.getInstance().time)
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
            val simpleDateFormat5 = SimpleDateFormat("hh:mm:ss aa")
            val simpleDateFormat6 = SimpleDateFormat("hh")
            val simpleDateFormat7 = SimpleDateFormat("mm")
            val parse = SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa").parse(str3)
            this.date_time = simpleDateFormat.format(parse) + " " + simpleDateFormat5.format(parse)
            this.day = simpleDateFormat2.format(parse).toInt()
            this.month = simpleDateFormat3.format(parse).toInt()
            this.year = simpleDateFormat4.format(parse).toInt()
            this.hour = simpleDateFormat6.format(parse).toInt()
            this.minute = simpleDateFormat7.format(parse).toInt()
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    private fun setUpActionBar() {
        (findViewById<View>(R.id.toolbar_txt_title_1) as TextView).text =
            resources.getString(R.string.lbl_header_add)
        (findViewById<View>(R.id.toolbar_txt_title_2) as TextView).text =
            resources.getString(R.string.lbl_header_blood_count_data)
        findViewById<View>(R.id.toolbar_rel_save).setOnClickListener { this@AddBloodCountActivity.SaveProcess() }
        findViewById<View>(R.id.toolbar_rel_back).setOnClickListener { this@AddBloodCountActivity.onBackPressed() }
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


    private fun insertData(bloodCountData: BloodCountData) {
        val token = sharedPreferencesUtils.read("token", "")
        if (!token.isNullOrEmpty()) {
            val call = ApiClient.apiService.postBloodCount(token, bloodCountData)
            call.enqueue(object : Callback<BloodCountData> {
                override fun onResponse(
                    call: Call<BloodCountData>,
                    response: Response<BloodCountData>
                ) {
                    Log.e(" bloodcount ", " is successful ")
//                    onBackPressed()
//                    return
                }

                override fun onFailure(call: Call<BloodCountData>, t: Throwable) {
                    Log.e(" bloodcount ", " error " + t.localizedMessage)
//                    onBackPressed()
//                    return
                }

            })
        }
    }

    private fun updateBloodCount(bloodCountData: BloodCountData) {
        val token = sharedPreferencesUtils.read("token", "")
        if (!token.isNullOrEmpty()) {
            val call = ApiClient.apiService.updateBloodCount(token, bloodCountData)
            call.enqueue(object : Callback<BloodCountData> {
                override fun onResponse(
                    call: Call<BloodCountData>,
                    response: Response<BloodCountData>
                ) {
                    onBackPressed()
                }

                override fun onFailure(call: Call<BloodCountData>, t: Throwable) {
                    onBackPressed()
                }

            })
        }

    }


}
