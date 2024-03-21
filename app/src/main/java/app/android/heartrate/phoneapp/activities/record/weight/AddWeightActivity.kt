package app.android.heartrate.phoneapp.activities.record.weight

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
import app.android.heartrate.phoneapp.databinding.ActivityAddWeightBinding
import app.android.heartrate.phoneapp.model.classes.BodyTempData
import app.android.heartrate.phoneapp.model.classes.WeightData
import app.android.heartrate.phoneapp.model.temperature.BodyTempResponse
import app.android.heartrate.phoneapp.model.weight.WeightResponse
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
import java.util.Calendar

class AddWeightActivity() : AppCompatActivity() {
    var weightIndex: Int = 0
    var SQLite_health_tracker: SQLiteHealthTracker? = null
    var date_time: String = ""
    var day: Int = 0
    var et_notes: EditText? = null
    var hour: Int = 0
    var mContext: Context? = null
    var month: Int = 0
    var np_weight: NumberPicker? = null
    var push_animation: Animation? = null
    private lateinit var rel_select_date: RelativeLayout
    private lateinit var rel_select_time: RelativeLayout

    var spinner_profile_adapter: SpinnerProfileAdapter? = null
    var spinner_profiles: Spinner? = null
    private lateinit var txt_date: TextView
    private lateinit var txt_time: TextView
    var spinner_txt_name: TextView? = null
    var txt_weight: TextView? = null
    var weight_default_value: Int = 55
    var weight_max_value: Int = 300
    var weight_min_value: Int = 1
    var year: Int = 0
    private lateinit var array_weight_range: Array<String?>
    private var current_date_time = ""
    private var save_entry_day = 0
    private var save_entry_hour = 0
    private var save_entry_minute = 0
    private var save_entry_month = 0
    private var save_entry_year = 0

    private lateinit var sharedPreferencesUtils: SharedPreferences
    private lateinit var binding: ActivityAddWeightBinding

    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        binding = ActivityAddWeightBinding.inflate(layoutInflater)
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
        this.spinner_profiles = findViewById(R.id.weight_spinner_profiles)
        this.np_weight = findViewById(R.id.add_weight_np_weight_value)
        this.txt_weight = findViewById(R.id.add_weight_txt_weight)
        this.spinner_txt_name = findViewById(R.id.spinner_txt_name)
        this.et_notes = findViewById(R.id.add_weight_et_notes)
        this.rel_select_date = findViewById(R.id.weight_rel_select_date)
        this.txt_date = findViewById(R.id.weight_txt_date)
        this.rel_select_time = findViewById(R.id.weight_rel_select_time)
        this.txt_time = findViewById(R.id.weight_txt_time)
        SetProfileSpinner()
        SetCurrentDateTime()
        SetWeightPicker()
        if (AppConstants.is_weight_edit_mode) {
            SetWeightData()
        }
        rel_select_date.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                this@AddWeightActivity.hideSoftKeyboard()
                val instance = Calendar.getInstance()
                this@AddWeightActivity.save_entry_year = instance[1]
                this@AddWeightActivity.save_entry_month = instance[2]
                this@AddWeightActivity.save_entry_day = instance[5]
                DatePickerDialog(
                    this@AddWeightActivity,
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
                    this@AddWeightActivity.save_entry_year,
                    this@AddWeightActivity.save_entry_month,
                    this@AddWeightActivity.save_entry_day
                ).show()
            }
        })
        rel_select_time.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val instance = Calendar.getInstance()
                this@AddWeightActivity.save_entry_hour = instance[11]
                this@AddWeightActivity.save_entry_minute = instance[12]
                TimePickerDialog(
                    this@AddWeightActivity,
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
                    this@AddWeightActivity.save_entry_hour,
                    this@AddWeightActivity.save_entry_minute,
                    false
                ).show()
            }
        })
    }

    private fun SetWeightPicker() {
        this.array_weight_range = arrayOfNulls(300)
        var i = 0
        while (true) {
            val strArr = this.array_weight_range
            if (i >= strArr.size) {
                break
            }
            val i2 = i + 1
            strArr[i] = i2.toString()
            i = i2
        }
        var i3 = 0
        while (true) {
            val strArr2 = this.array_weight_range
            if (i3 >= strArr2.size) {
                break
            } else if (strArr2[i3].equals(weight_default_value.toString(), ignoreCase = true)) {
                this.weightIndex = i3
                break
            } else {
                i3++
            }
        }
        np_weight!!.maxValue = this.weight_max_value
        np_weight!!.minValue = this.weight_min_value
        np_weight!!.displayedValues = this.array_weight_range
        np_weight!!.wrapSelectorWheel = false
        np_weight!!.value = this.weight_default_value
        np_weight!!.setOnValueChangedListener(object : NumberPicker.OnValueChangeListener {
            override fun onValueChange(numberPicker: NumberPicker, i: Int, i2: Int) {
                this@AddWeightActivity.weightIndex = i2
                txt_weight!!.text = numberPicker.value.toString().trim { it <= ' ' }
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

    private fun SetWeightData() {
        if (AppConstants.selected_weight_data != null) {
            val trim = AppConstants.selected_weight_data.date.trim { it <= ' ' }
            val trim2 = AppConstants.selected_weight_data.time.trim { it <= ' ' }
            AppConstants.selected_weight_data.weight.toString()
            val trim3 = AppConstants.selected_weight_data.notes.trim { it <= ' ' }
            this.weight_default_value = AppConstants.selected_weight_data.weight.toInt()
            txt_date!!.text = trim
            txt_time!!.text = trim2
            var i = 0
            while (true) {
                val strArr = this.array_weight_range
                if (i >= strArr.size) {
                    break
                } else if (strArr[i].equals(weight_default_value.toString(), ignoreCase = true)) {
                    this.weightIndex = i
                    break
                } else {
                    i++
                }
            }
            np_weight!!.maxValue = this.weight_max_value
            np_weight!!.minValue = this.weight_min_value
            np_weight!!.displayedValues = this.array_weight_range
            np_weight!!.wrapSelectorWheel = false
            np_weight!!.value = this.weightIndex + 1
            txt_weight!!.text = weight_default_value.toString()
            et_notes!!.setText(trim3)
            return
        }
        EUGeneralClass.ShowErrorToast(this, "Something went wrong!")
    }


    private fun SaveProcess() {
        var z = false
        var textView: TextView? = null
        try {
            val trim = txt_date!!.text.toString().trim { it <= ' ' }
            val trim2 = txt_time!!.text.toString().trim { it <= ' ' }
            val trim3 = txt_weight!!.text.toString().trim { it <= ' ' }
            val trim4 = et_notes!!.text.toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(trim3)) {
                txt_weight!!.error = AppConstants.error_field_require
                textView = this.txt_weight
                z = true
            }
            if (z) {
                textView!!.requestFocus()
                return
            }
            val weightData = WeightData()
            if (!AppConstants.is_weight_edit_mode) {
                weightData.user_id = sharedPreferencesUtils!!.getUserId()
                weightData.date = trim.trim { it <= ' ' }
                weightData.time = trim2.trim { it <= ' ' }
                weightData.weight = trim3.trim { it <= ' ' }.toFloat()
                weightData.notes = trim4.trim { it <= ' ' }
                GetDateTime(trim.trim { it <= ' ' }, trim2.trim { it <= ' ' })
                weightData.dateTime = this.date_time
                weightData.day = this.day
                weightData.month = this.month
                weightData.year = this.year
                weightData.hour = this.hour
                insertData(weightData)
//                SQLite_health_tracker!!.InsertWeightData(weightData)
//                EUGeneralClass.ShowSuccessToast(this, "Weight Data saved successfully!")
//                onBackPressed()
//                return
            }
            val i = AppConstants.selected_weight_data.row_id
            weightData.row_id = i
            weightData.user_id = sharedPreferencesUtils!!.getUserId()
            weightData.date = trim.trim { it <= ' ' }
            weightData.time = trim2.trim { it <= ' ' }
            weightData.weight = trim3.trim { it <= ' ' }.toFloat()
            weightData.notes = trim4.trim { it <= ' ' }
            GetDateTime(trim.trim { it <= ' ' }, trim2.trim { it <= ' ' })
            weightData.dateTime = this.date_time
            weightData.day = this.day
            weightData.month = this.month
            weightData.year = this.year
            weightData.hour = this.hour
            weightData.row_id = i
            updateData(weightData)
//            SQLite_health_tracker!!.UpdateWeightData(
//                i,
//                sharedPreferencesUtils!!.getUserId(), weightData
//            )
//            EUGeneralClass.ShowSuccessToast(this, "Weight Data updated successfully!")
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
            resources.getString(R.string.lbl_header_weight)
        findViewById<View>(R.id.toolbar_rel_save).setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                this@AddWeightActivity.SaveProcess()
            }
        })
        findViewById<View>(R.id.toolbar_rel_back).setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                this@AddWeightActivity.onBackPressed()
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


    private fun insertData(data: WeightData) {
        val token = sharedPreferencesUtils.read("token", "")
        if (!token.isNullOrEmpty()) {
            val call = ApiClient.apiService.postWeight(token, data)
            call.enqueue(object : Callback<WeightResponse> {
                override fun onResponse(
                    call: Call<WeightResponse>,
                    response: Response<WeightResponse>
                ) {
                    if (response.isSuccessful) {
                        showMessage(response.body()?.msg ?: "Saved successfully ")
                        onBackPressed()
                        return
                    } else {
                        showMessage("An error occurred trying to save data")
                    }
                }

                override fun onFailure(call: Call<WeightResponse>, t: Throwable) {
                    showMessage("An error occurred trying to save data " + t.localizedMessage)

                }

            })
        }
    }

    private fun updateData(data: WeightData) {
        val token = sharedPreferencesUtils.read("token", "")
        if (!token.isNullOrEmpty()) {
            val call = ApiClient.apiService.updateWeight(token, data)
            call.enqueue(object : Callback<WeightData> {
                override fun onResponse(
                    call: Call<WeightData>,
                    response: Response<WeightData>
                ) {
                    if (response.isSuccessful) {
                        showMessage("Updated successfully!")
                        onBackPressed()
                    } else {
                        showMessage("Unable to update")
                    }
                }

                override fun onFailure(call: Call<WeightData>, t: Throwable) {
                    showMessage("An error occurred updating. Try again later")
                }

            })
        }

    }


    private fun showMessage(message: String) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
    }

}
