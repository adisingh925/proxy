package app.android.heartrate.phoneapp.activities.record.cholestrol

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
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.android.heartrate.phoneapp.R
import app.android.heartrate.phoneapp.adapters.SpinnerProfileAdapter
import app.android.heartrate.phoneapp.databinding.ActivityAddCholesterolBinding
import app.android.heartrate.phoneapp.model.bloodsugar.BloodSugarResponse
import app.android.heartrate.phoneapp.model.cholesterol.CholesterolResponse
import app.android.heartrate.phoneapp.model.classes.BloodSugarData
import app.android.heartrate.phoneapp.model.classes.CholesterolData
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

class AddCholesterolActivity() : AppCompatActivity() {
    var SQLite_health_tracker: SQLiteHealthTracker? = null
    var date_time: String = ""
    var day: Int = 0
    var et_hdl_value: EditText? = null
    var et_ldl_value: EditText? = null
    var et_notes: EditText? = null
    var et_total_cholesterol: EditText? = null
    var et_triglyceride_value: EditText? = null
    var hour: Int = 0
    var mContext: Context? = null
    var month: Int = 0
    var push_animation: Animation? = null
    private lateinit var rel_select_date: RelativeLayout
    private lateinit var rel_select_time: RelativeLayout
    var spinner_profile_adapter: SpinnerProfileAdapter? = null
    var spinner_profiles: Spinner? = null
    private lateinit var txt_date: TextView
    var spinner_txt_name: TextView? = null
    private lateinit var txt_time: TextView
    var year: Int = 0
    private var current_date_time = ""
    private var save_entry_day = 0
    private var save_entry_hour = 0
    private var save_entry_minute = 0
    private var save_entry_month = 0
    private var save_entry_year = 0

    private lateinit var sharedPreferencesUtils: SharedPreferences

    private lateinit var binding: ActivityAddCholesterolBinding

    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        binding = ActivityAddCholesterolBinding.inflate(layoutInflater)
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
        this.spinner_profiles = findViewById(R.id.add_cholesterol_spinner_profiles)
        this.et_total_cholesterol = findViewById(R.id.add_cholesterol_et_total_value)
        this.et_hdl_value = findViewById(R.id.add_cholesterol_et_hdl_value)
        this.et_ldl_value = findViewById(R.id.add_cholesterol_et_ldl_value)
        this.et_triglyceride_value = findViewById(R.id.add_cholesterol_et_triglyceride)
        this.et_notes = findViewById(R.id.add_cholesterol_et_notes)
        this.rel_select_date = findViewById(R.id.add_cholesterol_rel_select_date)
        this.txt_date = findViewById(R.id.add_cholesterol_txt_date)
        this.rel_select_time = findViewById(R.id.add_cholesterol_rel_select_time)
        this.txt_time = findViewById(R.id.add_cholesterol_txt_time)
        this.spinner_txt_name = findViewById(R.id.spinner_txt_name)
        SetProfileSpinner()
        SetCurrentDateTime()
        if (AppConstants.is_cholesterol_edit_mode) {
            SetCholesterolData()
        }
        rel_select_date.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                this@AddCholesterolActivity.hideSoftKeyboard()
                val instance = Calendar.getInstance()
                this@AddCholesterolActivity.save_entry_year = instance[1]
                this@AddCholesterolActivity.save_entry_month = instance[2]
                this@AddCholesterolActivity.save_entry_day = instance[5]
                DatePickerDialog(
                    this@AddCholesterolActivity,
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
                    this@AddCholesterolActivity.save_entry_year,
                    this@AddCholesterolActivity.save_entry_month,
                    this@AddCholesterolActivity.save_entry_day
                ).show()
            }
        })
        rel_select_time.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val instance = Calendar.getInstance()
                this@AddCholesterolActivity.save_entry_hour = instance[11]
                this@AddCholesterolActivity.save_entry_minute = instance[12]
                TimePickerDialog(
                    this@AddCholesterolActivity,
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
                    this@AddCholesterolActivity.save_entry_hour,
                    this@AddCholesterolActivity.save_entry_minute,
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

    private fun SetCholesterolData() {
        if (AppConstants.selected_cholesterol_data != null) {
            val trim = AppConstants.selected_cholesterol_data.date.trim { it <= ' ' }
            val trim2 = AppConstants.selected_cholesterol_data.time.trim { it <= ' ' }
            val valueOf = AppConstants.selected_cholesterol_data.cholesterol_value.toString()
            val valueOf2 = AppConstants.selected_cholesterol_data.hdl_value.toString()
            val valueOf3 = AppConstants.selected_cholesterol_data.ldl_value.toString()
            val valueOf4 = AppConstants.selected_cholesterol_data.triglyceride_value.toString()
            val trim3 = AppConstants.selected_cholesterol_data.notes.trim { it <= ' ' }
            txt_date!!.text = trim
            txt_time!!.text = trim2
            et_total_cholesterol!!.setText(valueOf)
            et_hdl_value!!.setText(valueOf2)
            et_ldl_value!!.setText(valueOf3)
            et_triglyceride_value!!.setText(valueOf4)
            et_notes!!.setText(trim3)
            val editText = this.et_total_cholesterol
            editText!!.setSelection(editText.text.toString().trim { it <= ' ' }.length)
            val editText2 = this.et_hdl_value
            editText2!!.setSelection(editText2.text.toString().trim { it <= ' ' }.length)
            val editText3 = this.et_ldl_value
            editText3!!.setSelection(editText3.text.toString().trim { it <= ' ' }.length)
            val editText4 = this.et_triglyceride_value
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
            val trim3 =
                et_total_cholesterol!!.text.toString().trim { it <= ' ' }
            val trim4 = et_hdl_value!!.text.toString().trim { it <= ' ' }
            val trim5 = et_ldl_value!!.text.toString().trim { it <= ' ' }
            val trim6 =
                et_triglyceride_value!!.text.toString().trim { it <= ' ' }
            val GetCholesterolResultText = AppConstants.GetCholesterolResultText(trim3.toFloat())
            val trim7 = et_notes!!.text.toString().trim { it <= ' ' }
            val parseFloat = trim3.trim { it <= ' ' }.toFloat()
            if (TextUtils.isEmpty(trim3)) {
                et_total_cholesterol!!.error = AppConstants.error_field_require
                editText = this.et_total_cholesterol
                z = true
            }
            if (z) {
                editText!!.requestFocus()
                return
            }
            val cholesterolData = CholesterolData()
            if (!AppConstants.is_cholesterol_edit_mode) {
                cholesterolData.user_id = sharedPreferencesUtils!!.getUserId()
                cholesterolData.date = trim.trim { it <= ' ' }
                cholesterolData.time = trim2.trim { it <= ' ' }
                cholesterolData.cholesterol_value = parseFloat
                cholesterolData.hdl_value = trim4.trim { it <= ' ' }.toFloat()
                cholesterolData.ldl_value = trim5.trim { it <= ' ' }.toFloat()
                cholesterolData.triglyceride_value = trim6.trim { it <= ' ' }.toFloat()
                cholesterolData.result = GetCholesterolResultText.trim { it <= ' ' }
                cholesterolData.notes = trim7.trim { it <= ' ' }
                GetDateTime(trim.trim { it <= ' ' }, trim2.trim { it <= ' ' })
                cholesterolData.dateTime = this.date_time
                cholesterolData.day = this.day
                cholesterolData.month = this.month
                cholesterolData.year = this.year
                cholesterolData.hour = this.hour
                insertData(cholesterolData)
//                SQLite_health_tracker!!.InsertCholesterolData(cholesterolData)
//                EUGeneralClass.ShowSuccessToast(this, "Cholesterol Data saved successfully!")
//                onBackPressed()
//                return
            }
            val i2 = AppConstants.selected_cholesterol_data.row_id
            cholesterolData.user_id = AppConstants.selected_cholesterol_data.user_id
            cholesterolData.date = trim.trim { it <= ' ' }
            cholesterolData.time = trim2.trim { it <= ' ' }
            cholesterolData.cholesterol_value = trim3.trim { it <= ' ' }.toFloat()
            cholesterolData.hdl_value = trim4.trim { it <= ' ' }.toFloat()
            cholesterolData.ldl_value = trim5.trim { it <= ' ' }.toFloat()
            cholesterolData.triglyceride_value = trim6.trim { it <= ' ' }.toFloat()
            cholesterolData.result = GetCholesterolResultText.trim { it <= ' ' }
            cholesterolData.notes = trim7.trim { it <= ' ' }
            GetDateTime(trim.trim { it <= ' ' }, trim2.trim { it <= ' ' })
            cholesterolData.dateTime = this.date_time
            cholesterolData.day = this.day
            cholesterolData.month = this.month
            cholesterolData.year = this.year
            cholesterolData.hour = this.hour
            cholesterolData.row_id = i2
            updateData(cholesterolData)

//            SQLite_health_tracker!!.UpdateCholesterolData(
//                i2,
//                sharedPreferencesUtils!!.getUserId(), cholesterolData
//            )
//            EUGeneralClass.ShowSuccessToast(this, "Cholesterol Data updated successfully!")
//            onBackPressed()
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
            resources.getString(R.string.lbl_header_cholesterol_data)
        findViewById<View>(R.id.toolbar_rel_save).setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                this@AddCholesterolActivity.SaveProcess()
            }
        })
        findViewById<View>(R.id.toolbar_rel_back).setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                this@AddCholesterolActivity.onBackPressed()
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

    private fun insertData(data: CholesterolData) {
        val token = sharedPreferencesUtils.read("token", "")
        if (!token.isNullOrEmpty()) {
            val call = ApiClient.apiService.postCholesterol(token, data)
            call.enqueue(object : Callback<CholesterolResponse> {
                override fun onResponse(
                    call: Call<CholesterolResponse>,
                    response: Response<CholesterolResponse>
                ) {
                    if (response.isSuccessful) {
                        showMessage(response.body()?.msg ?: "Saved successfully ")
                        onBackPressed()
                        return
                    } else {
                        showMessage("An error occurred trying to save data")
                    }
                }

                override fun onFailure(call: Call<CholesterolResponse>, t: Throwable) {
                    showMessage("An error occurred trying to save data " + t.localizedMessage)

                }

            })
        }
    }

    private fun updateData(data: CholesterolData) {
        val token = sharedPreferencesUtils.read("token", "")
        if (!token.isNullOrEmpty()) {
            val call = ApiClient.apiService.updateCholesterol(token, data)
            call.enqueue(object : Callback<CholesterolResponse> {
                override fun onResponse(
                    call: Call<CholesterolResponse>,
                    response: Response<CholesterolResponse>
                ) {
                    if (response.isSuccessful) {
                        showMessage("Updated successfully!")
                        onBackPressed()
                    } else {
                        showMessage("Unable to update")
                    }
                }

                override fun onFailure(call: Call<CholesterolResponse>, t: Throwable) {
                    showMessage("An error occurred updating. Try again later")
                }

            })
        }

    }

    private fun showMessage(message: String) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
    }
}
