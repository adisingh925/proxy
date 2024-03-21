package app.android.heartrate.phoneapp.activities.record.body_temp

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
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
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Spinner
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import app.android.heartrate.phoneapp.R
import app.android.heartrate.phoneapp.adapters.SpinnerProfileAdapter
import app.android.heartrate.phoneapp.databinding.ActivityAddBodyTempBinding
import app.android.heartrate.phoneapp.model.bloodsugar.BloodSugarResponse
import app.android.heartrate.phoneapp.model.classes.BloodSugarData
import app.android.heartrate.phoneapp.model.classes.BodyTempData
import app.android.heartrate.phoneapp.model.temperature.BodyTempResponse
import app.android.heartrate.phoneapp.retrofit.ApiClient
import app.android.heartrate.phoneapp.sharedpreferences.SharedPreferences
import app.android.heartrate.phoneapp.sqlite.SQLiteHealthTracker
import app.android.heartrate.phoneapp.utils.AppConstants
import app.android.heartrate.phoneapp.utils.EUGeneralClass
import app.android.heartrate.phoneapp.utils.StoredPreferencesValue
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar

class AddBodyTempActivity() : AppCompatActivity() {
    var SQLite_health_tracker: SQLiteHealthTracker? = null

    var cel_max_value: Float = 42.0f
    var cel_min_value: Float = 33.0f
    var cel_value: Float = 37.0f
    var current_minute: String = ""
    var date_time: String = ""
    var day: Int = 0
    var fah_max_value: Float = 108.0f
    var fah_min_value: Float = 91.0f
    var fah_value: Float = 0.0f
    var hour: Int = 0
    private lateinit var img_info: ImageView
    var lbl_seek_celsius_max: TextView? = null
    var lbl_seek_celsius_min: TextView? = null
    var lbl_seek_fahrenheit_max: TextView? = null
    var lbl_seek_fahrenheit_min: TextView? = null
    var lbl_seek_pulse_max: TextView? = null
    var lbl_seek_pulse_min: TextView? = null
    var spinner_txt_name: TextView? = null
    var mContext: Context? = null
    var month: Int = 0
    var pulse_max_value: Float = 250.0f
    var pulse_min_value: Float = 1.0f
    var pulse_value: Int = 72
    var push_animation: Animation? = null
    var rel_fever_status: RelativeLayout? = null
    private lateinit var rel_select_date: RelativeLayout
    private lateinit var rel_select_time: RelativeLayout
    private lateinit var save_txt_date: TextView
    private lateinit var save_txt_time: TextView
    var seek_celsius: SeekBar? = null
    var seek_fahrenheit: SeekBar? = null
    var seek_pulse: SeekBar? = null
    var simple_data_format: SimpleDateFormat? = null
    var spinner_profile_adapter: SpinnerProfileAdapter? = null
    var spinner_profiles: Spinner? = null
    var txt_fever_status: TextView? = null
    var txt_seek_celsius_progress: TextView? = null
    var txt_seek_fahrenheit_progress: TextView? = null
    var txt_seek_pulse_progress: TextView? = null
    var year: Int = 0
    private lateinit var array_celsius_range: Array<String?>
    private lateinit var array_fahrenheit_range: Array<String?>
    private lateinit var array_pulse_range: Array<String?>
    private var celsiusIndex = 0
    private var current_date_time = ""
    private var fahrenheitIndex = 0
    private var pulseIndex = 0
    private var save_entry_day = 0
    private var save_entry_hour = 0
    private var save_entry_minute = 0
    private var save_entry_month = 0
    private var save_entry_year = 0

    private lateinit var sharedPreferencesUtils: SharedPreferences
    private lateinit var binding: ActivityAddBodyTempBinding

    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        binding = ActivityAddBodyTempBinding.inflate(layoutInflater)
        setContentView(binding.root)
        SetView()
        AppConstants.overridePendingTransitionEnter(this)
    }

    private fun SetView() {

        this.mContext = this
        this.push_animation = AnimationUtils.loadAnimation(this, R.anim.view_push)
        setUpActionBar()
        sharedPreferencesUtils = SharedPreferences
        this.simple_data_format = SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa")
        val sQLiteHealthTracker = SQLiteHealthTracker(this)
        this.SQLite_health_tracker = sQLiteHealthTracker
        sQLiteHealthTracker.openToWrite()
        this.array_celsius_range = arrayOfNulls(91)
        this.array_fahrenheit_range = arrayOfNulls(91)
        this.array_pulse_range =
            arrayOfNulls(ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION)
        var i = 0
        var i2 = 0
        while (true) {
            val strArr = this.array_celsius_range
            if (i2 >= strArr.size) {
                break
            }
            val f = (((i2) + 330).toFloat()) / 10.0f
            strArr[i2] = f.toString()
            array_fahrenheit_range[i2] =
                ((Math.round(((f * 1.8f) + 32.0f) * 10.0f).toFloat()) / 10.0f).toString()
            i2++
        }
        while (true) {
            val strArr2 = this.array_pulse_range
            if (i >= strArr2.size) {
                break
            }
            val i3 = i + 1
            strArr2[i] = i3.toString()
            i = i3
        }
        this.img_info = findViewById(R.id.add_temp_img_info)
        this.spinner_profiles = findViewById(R.id.add_temp_spinner_profiles)
        this.rel_fever_status = findViewById(R.id.add_temp_rel_fever_status)
        this.txt_fever_status = findViewById(R.id.add_temp_txt_fever_status)
        this.rel_select_date = findViewById(R.id.add_temp_rel_select_date)
        this.save_txt_date = findViewById(R.id.add_temp_txt_date)
        this.rel_select_time = findViewById(R.id.add_temp_rel_select_time)
        this.save_txt_time = findViewById(R.id.add_temp_txt_time)
        this.seek_pulse = findViewById(R.id.add_temp_seek_pulse)
        this.seek_celsius = findViewById(R.id.add_temp_seek_cel)
        this.seek_fahrenheit = findViewById(R.id.add_temp_seek_fah)
        this.lbl_seek_pulse_min = findViewById(R.id.add_temp_lbl_pulse_min)
        this.lbl_seek_pulse_max = findViewById(R.id.add_temp_lbl_pulse_max)
        this.txt_seek_pulse_progress = findViewById(R.id.add_temp_txt_pulse_progress)
        this.lbl_seek_celsius_min = findViewById(R.id.add_temp_lbl_cel_min)
        this.lbl_seek_celsius_max = findViewById(R.id.add_temp_lbl_cel_max)
        this.txt_seek_celsius_progress = findViewById(R.id.add_temp_txt_cel_progress)
        this.lbl_seek_fahrenheit_min = findViewById(R.id.add_temp_lbl_fah_min)
        this.lbl_seek_fahrenheit_max = findViewById(R.id.add_temp_lbl_fah_max)
        this.spinner_txt_name = findViewById(R.id.spinner_txt_name)
        this.txt_seek_fahrenheit_progress = findViewById(R.id.add_temp_txt_fah_progress)
        SetProfileSpinner()
        SetNumberPickers()
        SetCurrentDateTime()
        if (AppConstants.is_body_temp_edit_mode) {
            SetBodyTempData()
        }
        rel_select_date.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val instance = Calendar.getInstance()
                this@AddBodyTempActivity.save_entry_year = instance[1]
                this@AddBodyTempActivity.save_entry_month = instance[2]
                this@AddBodyTempActivity.save_entry_day = instance[5]
                DatePickerDialog(
                    this@AddBodyTempActivity,
                    R.style.DialogTheme,
                    OnDateSetListener { datePicker, i, i2, i3 ->
                        try {
                            save_txt_date.setText(
                                SimpleDateFormat("yyyy-MM-dd").format(
                                    SimpleDateFormat("dd/MM/yyyy").parse(
                                        (i3.toString() + "/" + (i2 + 1) + "/" + i).trim { it <= ' ' })
                                )
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    },
                    this@AddBodyTempActivity.save_entry_year,
                    this@AddBodyTempActivity.save_entry_month,
                    this@AddBodyTempActivity.save_entry_day
                ).show()
            }
        })
        rel_select_time.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val instance = Calendar.getInstance()
                this@AddBodyTempActivity.save_entry_hour = instance[11]
                this@AddBodyTempActivity.save_entry_minute = instance[12]
                TimePickerDialog(
                    this@AddBodyTempActivity,
                    R.style.DialogTheme,
                    object : OnTimeSetListener {
                        override fun onTimeSet(timePicker: TimePicker, i: Int, i2: Int) {
                            try {
                                save_txt_time.setText(
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
                    this@AddBodyTempActivity.save_entry_hour,
                    this@AddBodyTempActivity.save_entry_minute,
                    false
                ).show()
            }
        })
        img_info.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                view.startAnimation(this@AddBodyTempActivity.push_animation)
                this@AddBodyTempActivity.InfoDialog()
            }
        })
    }

    @SuppressLint("NewApi")
    private fun SetBodyTempData() {
        if (AppConstants.selected_body_temp_data != null) {
            this.celsiusIndex = AppConstants.selected_body_temp_data.celsius_index
            this.fahrenheitIndex = AppConstants.selected_body_temp_data.fahrenheit_index
            this.pulseIndex = AppConstants.selected_body_temp_data.pulse_index
            this.cel_value = AppConstants.selected_body_temp_data.celsius_value
            this.fah_value = AppConstants.selected_body_temp_data.fahrenheit_value
            this.pulse_value = AppConstants.selected_body_temp_data.pulse_value.toInt()
            seek_pulse!!.min = pulse_min_value.toInt()
            seek_pulse!!.max = pulse_max_value.toInt()
            lbl_seek_pulse_min!!.text = pulse_min_value.toString()
            lbl_seek_pulse_max!!.text = pulse_max_value.toString()
            seek_celsius!!.min = cel_min_value.toInt()
            seek_celsius!!.max = cel_max_value.toInt()
            lbl_seek_celsius_min!!.text = cel_min_value.toString()
            lbl_seek_celsius_max!!.text = cel_max_value.toString()
            seek_fahrenheit!!.min = fah_min_value.toInt()
            seek_fahrenheit!!.max = fah_max_value.toInt()
            lbl_seek_fahrenheit_min!!.text = fah_min_value.toString()
            lbl_seek_fahrenheit_max!!.text = fah_max_value.toString()
            this.fah_value = AppConstants.convertCelsiusToFahrenheit(this.cel_value)
            seek_pulse!!.progress = this.pulse_value
            seek_celsius!!.progress = cel_value.toInt()
            seek_fahrenheit!!.progress = fah_value.toInt()
            txt_seek_pulse_progress!!.text = pulse_value.toString()
            txt_seek_celsius_progress!!.text = cel_value.toString()
            txt_seek_fahrenheit_progress!!.text = fah_value.toString()
            SetEditDateTime(
                this.save_txt_date,
                this.save_txt_time,
                AppConstants.selected_body_temp_data.dateTime.trim { it <= ' ' })
            SetFeverStatus(this.cel_value)
            return
        }
        EUGeneralClass.ShowErrorToast(this, "Something went wrong!")
    }

    private fun SetProfileSpinner() {
        val name = sharedPreferencesUtils!!.getUserName()
        spinner_txt_name!!.text = name
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
            save_txt_date!!.text = format2
            save_txt_time!!.text = DateFormat.format("hh:mm aaa", Calendar.getInstance().time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    private fun SetEditDateTime(textView: TextView?, textView2: TextView?, str: String) {
        try {
            val parse = SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa").parse(str)
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
            val simpleDateFormat2 = SimpleDateFormat("hh:mm aa")
            val simpleDateFormat3 = SimpleDateFormat("mm")
            val format = simpleDateFormat.format(parse)
            val format2 = simpleDateFormat2.format(parse)
            this.current_minute = simpleDateFormat3.format(parse)
            textView!!.text = format
            textView2!!.text = format2
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    @SuppressLint("NewApi")
    private fun SetNumberPickers() {
        this.celsiusIndex =
            StoredPreferencesValue.getDefaultCelsiusIndex(StoredPreferencesValue.CELSIUS_KEY, this)
        this.fahrenheitIndex = StoredPreferencesValue.getDefaultFahrenheitIndex(
            StoredPreferencesValue.FAHRENHEIT_KEY,
            this
        )
        this.pulseIndex =
            StoredPreferencesValue.getDefaultPulseIndex(StoredPreferencesValue.PULSE_KEY, this)
        this.cel_value = StoredPreferencesValue.getDefaultCelsiusValue(this)
        this.fah_value = StoredPreferencesValue.getDefaultFahrenheitValue(this)
        this.pulse_value = StoredPreferencesValue.getDefaultPulseValue(this)
        seek_pulse!!.min = pulse_min_value.toInt()
        seek_pulse!!.max = pulse_max_value.toInt()
        lbl_seek_pulse_min!!.text = pulse_min_value.toString()
        lbl_seek_pulse_max!!.text = pulse_max_value.toString()
        seek_celsius!!.min = cel_min_value.toInt()
        seek_celsius!!.max = cel_max_value.toInt()
        lbl_seek_celsius_min!!.text = cel_min_value.toString()
        lbl_seek_celsius_max!!.text = cel_max_value.toString()
        seek_fahrenheit!!.min = fah_min_value.toInt()
        seek_fahrenheit!!.max = fah_max_value.toInt()
        lbl_seek_fahrenheit_min!!.text = fah_min_value.toString()
        lbl_seek_fahrenheit_max!!.text = fah_max_value.toString()
        this.fah_value = AppConstants.convertCelsiusToFahrenheit(this.cel_value)
        seek_pulse!!.progress = this.pulse_value
        seek_celsius!!.progress = cel_value.toInt()
        seek_fahrenheit!!.progress = fah_value.toInt()
        txt_seek_pulse_progress!!.text = pulse_value.toString()
        txt_seek_celsius_progress!!.text = cel_value.toString()
        txt_seek_fahrenheit_progress!!.text = fah_value.toString()
        seek_pulse!!.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, z: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                val progress = seek_pulse!!.progress
                Log.e("Pulse Value :", progress.toString())
                this@AddBodyTempActivity.pulse_value = progress
                txt_seek_pulse_progress!!.text = pulse_value.toString()
            }
        })
        seek_celsius!!.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, z: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                val progress = seek_celsius!!.progress
                Log.e("Celsius Value :", progress.toString())
                this@AddBodyTempActivity.cel_value = progress.toFloat()
                val addBodyTempActivity = this@AddBodyTempActivity
                addBodyTempActivity.fah_value =
                    AppConstants.convertCelsiusToFahrenheit(addBodyTempActivity.cel_value)
                seek_fahrenheit!!.progress = fah_value.toInt()
                val addBodyTempActivity2 = this@AddBodyTempActivity
                addBodyTempActivity2.SetFeverStatus(addBodyTempActivity2.cel_value)
                txt_seek_celsius_progress!!.text = cel_value.toString()
                txt_seek_fahrenheit_progress!!.text = fah_value.toString()
            }
        })
        seek_fahrenheit!!.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, z: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                val progress =
                    seek_fahrenheit!!.progress
                Log.e("Fah Value :", progress.toString())
                this@AddBodyTempActivity.fah_value = progress.toFloat()
                val addBodyTempActivity = this@AddBodyTempActivity
                addBodyTempActivity.cel_value =
                    AppConstants.convertFahrenheitToCelsius(addBodyTempActivity.fah_value)
                seek_celsius!!.progress = cel_value.toInt()
                val addBodyTempActivity2 = this@AddBodyTempActivity
                addBodyTempActivity2.SetFeverStatus(addBodyTempActivity2.cel_value)
                txt_seek_celsius_progress!!.text = cel_value.toString()
                txt_seek_fahrenheit_progress!!.text = fah_value.toString()
            }
        })
        SetFeverStatus(this.cel_value)
    }


    private fun SaveProcess() {
        try {
            val trim = array_celsius_range[celsiusIndex].toString().trim { it <= ' ' }
            val trim2 =
                array_fahrenheit_range[fahrenheitIndex].toString().trim { it <= ' ' }
            val trim3 = array_pulse_range[pulseIndex].toString().trim { it <= ' ' }
            val trim4 = save_txt_date!!.text.toString().trim { it <= ' ' }
            val trim5 = save_txt_time!!.text.toString().trim { it <= ' ' }
            val bodyTempData = BodyTempData()
            if (!AppConstants.is_body_temp_edit_mode) {
                bodyTempData.user_id = sharedPreferencesUtils!!.getUserId()
                bodyTempData.name = sharedPreferencesUtils!!.getUserName()
                bodyTempData.date = date_time.trim { it <= ' ' }
                bodyTempData.celsius = trim
                bodyTempData.fahrenheit = trim2
                bodyTempData.pulse = trim3
                bodyTempData.celsius_value = this.cel_value
                bodyTempData.fahrenheit_value = this.fah_value
                bodyTempData.pulse_value = pulse_value.toFloat()
                if ((trim == "")) {
                    bodyTempData.celsius = "-"
                }
                if ((trim2 == "")) {
                    bodyTempData.fahrenheit = "-"
                }
                if ((trim3 == "")) {
                    bodyTempData.pulse = "-"
                }
                GetDateTime(trim4.trim { it <= ' ' }, trim5.trim { it <= ' ' })
                bodyTempData.date = trim4.trim { it <= ' ' }
                bodyTempData.time = trim5.trim { it <= ' ' }
                bodyTempData.dateTime = date_time.trim { it <= ' ' }
                bodyTempData.day = this.day
                bodyTempData.month = this.month
                bodyTempData.year = this.year
                bodyTempData.hour = this.hour
                bodyTempData.tags = sharedPreferencesUtils!!.getUserName()
                insertData(bodyTempData)
//                SQLite_health_tracker!!.InsertTemperatureData(bodyTempData)
//                EUGeneralClass.ShowSuccessToast(this, AppConstants.data_saved_messages)
            } else {
                val i = AppConstants.selected_body_temp_data.row_id
                bodyTempData.user_id = sharedPreferencesUtils!!.getUserId()
                bodyTempData.name = sharedPreferencesUtils!!.getUserName()
                bodyTempData.celsius = trim
                bodyTempData.fahrenheit = trim2
                bodyTempData.pulse = trim3
                bodyTempData.celsius_value = this.cel_value
                bodyTempData.fahrenheit_value = this.fah_value
                bodyTempData.pulse_value = pulse_value.toFloat()
                if ((trim == "")) {
                    bodyTempData.celsius = "-"
                }
                if ((trim2 == "")) {
                    bodyTempData.fahrenheit = "-"
                }
                if ((trim3 == "")) {
                    bodyTempData.pulse = "-"
                }
                GetDateTime(trim4.trim { it <= ' ' }, trim5.trim { it <= ' ' })
                bodyTempData.date = trim4.trim { it <= ' ' }
                bodyTempData.time = trim5.trim { it <= ' ' }
                bodyTempData.dateTime = date_time.trim { it <= ' ' }
                bodyTempData.day = this.day
                bodyTempData.month = this.month
                bodyTempData.year = this.year
                bodyTempData.hour = this.hour
                bodyTempData.tags = sharedPreferencesUtils!!.getUserName()
                bodyTempData.row_id = i
                updateData(bodyTempData)
//                SQLite_health_tracker!!.UpdateTemperatureData(
//                    i,
//                    sharedPreferencesUtils!!.getUserId(), bodyTempData
//                )
//                EUGeneralClass.ShowSuccessToast(this, AppConstants.data_updated_messages)
            }
            StoredPreferencesValue.setDefaultCelsiusValue(this.cel_value, this)
            StoredPreferencesValue.setDefaultFahrenheitValue(this.fah_value, this)
            StoredPreferencesValue.setDefaultPulseValue(this.pulse_value, this)
            StoredPreferencesValue.setDefaultCelsiusIndex(
                StoredPreferencesValue.CELSIUS_KEY,
                this.celsiusIndex,
                this
            )
            StoredPreferencesValue.setDefaultFahrenheitIndex(
                StoredPreferencesValue.FAHRENHEIT_KEY,
                this.fahrenheitIndex,
                this
            )
            StoredPreferencesValue.setDefaultPulseIndex(
                StoredPreferencesValue.PULSE_KEY,
                this.pulseIndex,
                this
            )
            onBackPressed()
        } catch (e: Exception) {
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


    private fun SetFeverStatus(f: Float) {
        if (f >= 36.5f && (f.toDouble()) <= 37.5) {
            txt_fever_status!!.text = AppConstants.NORMAL
            rel_fever_status!!.setBackgroundResource(R.drawable.rel_normal_bg)
        } else if (f > 37.5f && (f.toDouble()) <= 38.3) {
            txt_fever_status!!.text = AppConstants.FEVER
            rel_fever_status!!.setBackgroundResource(R.drawable.rel_fever_bg)
        } else if (f > 38.3f) {
            txt_fever_status!!.text = AppConstants.HIGH_FEVER
            rel_fever_status!!.setBackgroundResource(R.drawable.rel_high_fever_bg)
        } else if (f < 35.0f) {
            txt_fever_status!!.text = AppConstants.HYPOTHERMIA
            rel_fever_status!!.setBackgroundResource(R.drawable.rel_hypothermia_bg)
        }
    }

    private fun setUpActionBar() {
        (findViewById<View>(R.id.toolbar_txt_title_1) as TextView).text =
            resources.getString(R.string.lbl_header_add)
        (findViewById<View>(R.id.toolbar_txt_title_2) as TextView).text =
            resources.getString(R.string.lbl_header_body_temp_data)
        findViewById<View>(R.id.toolbar_rel_save).setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                this@AddBodyTempActivity.SaveProcess()
            }
        })
        findViewById<View>(R.id.toolbar_rel_back).setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                this@AddBodyTempActivity.onBackPressed()
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


    private fun InfoDialog() {
        val dialog = Dialog((mContext)!!, R.style.TransparentBackground)
        dialog.requestWindowFeature(1)
        dialog.setContentView(R.layout.dialog_rate)
        val button = dialog.findViewById<Button>(R.id.dialog_conform_btn_yes)
        val button2 = dialog.findViewById<Button>(R.id.dialog_conform_btn_no)
        val string = resources.getString(R.string.app_temp_info)
        (dialog.findViewById<View>(R.id.dialog_conform_txt_header) as TextView).text =
            "Body Temperature"
        (dialog.findViewById<View>(R.id.dialog_conform_txt_message) as TextView).text = string
        button.text = "OK"
        button2.text = "Cancel"
        button2.visibility = View.GONE
        button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                dialog.dismiss()
            }
        })
        button2.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                dialog.dismiss()
            }
        })
        dialog.show()
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


    private fun insertData(data: BodyTempData) {
        val token = sharedPreferencesUtils.read("token", "")
        if (!token.isNullOrEmpty()) {
            val call = ApiClient.apiService.postBodyTemp(token, data)
            call.enqueue(object : Callback<BodyTempResponse> {
                override fun onResponse(
                    call: Call<BodyTempResponse>,
                    response: Response<BodyTempResponse>
                ) {
                    if (response.isSuccessful) {
                        showMessage(response.body()?.msg ?: "Saved successfully ")
                        onBackPressed()
                        return
                    } else {
                        showMessage("An error occurred trying to save data")
                    }
                }

                override fun onFailure(call: Call<BodyTempResponse>, t: Throwable) {
                    showMessage("An error occurred trying to save data " + t.localizedMessage)

                }

            })
        }
    }

    private fun updateData(data: BodyTempData) {
        val token = sharedPreferencesUtils.read("token", "")
        if (!token.isNullOrEmpty()) {
            val call = ApiClient.apiService.updateBodyTemp(token, data)
            call.enqueue(object : Callback<BodyTempResponse> {
                override fun onResponse(
                    call: Call<BodyTempResponse>,
                    response: Response<BodyTempResponse>
                ) {
                    if (response.isSuccessful) {
                        showMessage("Updated successfully!")
                        onBackPressed()
                    } else {
                        showMessage("Unable to update")
                    }
                }

                override fun onFailure(call: Call<BodyTempResponse>, t: Throwable) {
                    showMessage("An error occurred updating. Try again later")
                }

            })
        }

    }


    private fun showMessage(message: String) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
    }


}
