package app.android.heartrate.phoneapp.activities.record.body_temp

import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.android.heartrate.phoneapp.R
import app.android.heartrate.phoneapp.adapters.BodyTempDataAdapter
import app.android.heartrate.phoneapp.adapters.SpinnerProfileAdapter
import app.android.heartrate.phoneapp.databinding.ActivityBodyTempListBinding
import app.android.heartrate.phoneapp.model.bloodsugar.BloodSugarResponse
import app.android.heartrate.phoneapp.model.classes.BloodSugarData
import app.android.heartrate.phoneapp.model.classes.BodyTempData
import app.android.heartrate.phoneapp.model.temperature.BodyTempResponse
import app.android.heartrate.phoneapp.retrofit.ApiClient
import app.android.heartrate.phoneapp.sharedpreferences.SharedPreferences
import app.android.heartrate.phoneapp.sqlite.SQLiteHealthTracker
import app.android.heartrate.phoneapp.utils.AppConstants
import app.android.heartrate.phoneapp.utils.EUGeneralClass
import com.airbnb.lottie.LottieAnimationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BodyTempDataActivity : AppCompatActivity() {
    var SQLite_health_tracker: SQLiteHealthTracker? = null

    var array_temp_data: ArrayList<BodyTempData> = ArrayList()

    var bodyTempAdapter: BodyTempDataAdapter? = null
    var is_user_interact: Boolean = false
    var lbl_no_data: TextView? = null
    var spinner_txt_name: TextView? = null
    var lottie_circular_loading: LottieAnimationView? = null
    private lateinit var recycler_temp_data: RecyclerView
    private lateinit var array_celsius_range: Array<String?>
    private lateinit var array_fahrenheit_range: Array<String?>
    private lateinit var array_pulse_range: Array<String?>

    private lateinit var binding: ActivityBodyTempListBinding
    var mContext: Context? = null


    private var sharedPreferencesUtils: SharedPreferences? = null

    private fun SetTempData(tempList: List<BodyTempData>?){

        array_celsius_range = arrayOfNulls(91)
        array_fahrenheit_range = arrayOfNulls(91)
        array_pulse_range =
            arrayOfNulls(ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION)
        for (i in array_celsius_range.indices) {
            val f = (((i) + 330).toFloat()) / 10.0f
            array_celsius_range[i] = f.toString()
            array_fahrenheit_range[i] = ((Math.round(
                ((f * 1.8f) + 32.0f) * 10.0f
            ).toFloat()) / 10.0f).toString()
        }
        var i2 = 0
        while (i2 < array_pulse_range.size) {
            val i3 = i2 + 1
            array_pulse_range[i2] = i3.toString()
            i2 = i3
        }
        array_temp_data.clear()
        array_temp_data = tempList as ArrayList<BodyTempData>

        recycler_temp_data.layoutManager = LinearLayoutManager(this)
        recycler_temp_data.itemAnimator = DefaultItemAnimator()
        if (array_temp_data.size > 0) {
            lbl_no_data!!.visibility = View.GONE
            var adapter = object : BodyTempDataAdapter(array_temp_data, this) {
                override fun onBodyTempAdapterClickItem(i: Int, view: View) {
                    if (view.id == R.id.bt_row_rel_delete) {
                        ConformDeleteDialog(
                            array_temp_data[i].row_id
                        )
                    }
                    if (view.id == R.id.bt_row_rel_edit) {
                        AppConstants.is_body_temp_edit_mode = true
                        AppConstants.selected_body_temp_data = array_temp_data[i]
                        AddBodyTempScreen()
                    }
                }
            }
            bodyTempAdapter = adapter
            recycler_temp_data.adapter = bodyTempAdapter
        } else {
            lbl_no_data!!.visibility = View.VISIBLE
        }
    }

    var spinner_profile_adapter: SpinnerProfileAdapter? = null
    var spinner_profiles: Spinner? = null


    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        binding = ActivityBodyTempListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        SetView()
        AppConstants.overridePendingTransitionEnter(this)
    }

    private fun SetView() {
        this.mContext = this
        setUpActionBar()
        sharedPreferencesUtils = SharedPreferences
        val sQLiteHealthTracker = SQLiteHealthTracker(this)
        this.SQLite_health_tracker = sQLiteHealthTracker
        sQLiteHealthTracker.openToWrite()
        this.lottie_circular_loading = findViewById(R.id.animation_view)
        this.spinner_txt_name = findViewById(R.id.spinner_txt_name)
        val textView = findViewById<TextView>(R.id.lbl_no_data)
        this.lbl_no_data = textView
        textView.visibility = View.GONE
        this.spinner_profiles = findViewById(R.id.bt_spinner_profiles)
        this.recycler_temp_data = findViewById(R.id.bt_rv_data)
        SetProfileSpinner()
    }


    private fun AddBodyTempScreen() {
        startActivity(Intent(this, AddBodyTempActivity::class.java))
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        this.is_user_interact = true
    }

    private fun SetProfileSpinner() {
        val name = sharedPreferencesUtils!!.getUserName()
        spinner_txt_name!!.text = name
    }



    fun ConformDeleteDialog(i: Int) {
        val dialog = Dialog(this, R.style.TransparentBackground)
        dialog.requestWindowFeature(1)
        dialog.setContentView(R.layout.dialog_rate)
        val button = dialog.findViewById<Button>(R.id.dialog_conform_btn_yes)
        val button2 = dialog.findViewById<Button>(R.id.dialog_conform_btn_no)
        (dialog.findViewById<View>(R.id.dialog_conform_txt_header) as TextView).text = "Delete"
        (dialog.findViewById<View>(R.id.dialog_conform_txt_message) as TextView).text =
            "Do you want to delete this data?"
        button.text = "Delete"
        button2.text = "Cancel"
        button.setOnClickListener {
            try {
                deleteData(i)

            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }
            dialog.dismiss()
        }
        button2.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    fun ShowProgressBar() {
        val lottieAnimationView = this.lottie_circular_loading
        if (lottieAnimationView != null) {
            lottieAnimationView.visibility = View.VISIBLE
        }
    }

    fun DismissProgressBar() {
        val lottieAnimationView = this.lottie_circular_loading
        if (lottieAnimationView != null) {
            lottieAnimationView.visibility = View.GONE
        }
    }

    private fun setUpActionBar() {
        (findViewById<View>(R.id.toolbar_txt_title_1) as TextView).text =
            resources.getString(R.string.lbl_header_body)
        (findViewById<View>(R.id.toolbar_txt_title_2) as TextView).text =
            resources.getString(R.string.lbl_header_temp_data)
        findViewById<View>(R.id.tool_bar_rel_add_user).setOnClickListener {
            AppConstants.is_body_temp_edit_mode = false
            this@BodyTempDataActivity.AddBodyTempScreen()
        }
        findViewById<View>(R.id.toolbar_rel_back).setOnClickListener { this@BodyTempDataActivity.onBackPressed() }
        setSupportActionBar(findViewById(R.id.toolbar_actionbar))
        val supportActionBar = supportActionBar
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar.setDisplayHomeAsUpEnabled(false)
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
        fetchData()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun BackScreen() {
        AppConstants.is_tools_interstitial_show = true

        finish()
        AppConstants.overridePendingTransitionExit(this)
    }

//    inner class BodyTempDataTask(var tempList: List<BodyTempData>?) :
//        AsyncTask<Void?, Void?, Void?>() {
//
//
//        public override fun onPreExecute() {
//            super.onPreExecute()
//            ShowProgressBar()
//        }
//
//        override fun doInBackground(vararg voidArr: Void?): Void? {
//            try {
//                array_celsius_range = arrayOfNulls(91)
//                array_fahrenheit_range = arrayOfNulls(91)
//                array_pulse_range =
//                    arrayOfNulls(ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION)
//                for (i in array_celsius_range.indices) {
//                    val f = (((i) + 330).toFloat()) / 10.0f
//                    array_celsius_range[i] = f.toString()
//                    array_fahrenheit_range[i] = ((Math.round(
//                        ((f * 1.8f) + 32.0f) * 10.0f
//                    ).toFloat()) / 10.0f).toString()
//                }
//                var i2 = 0
//                while (i2 < array_pulse_range.size) {
//                    val i3 = i2 + 1
//                    array_pulse_range[i2] = i3.toString()
//                    i2 = i3
//                }
//                array_temp_data.clear()
//                val bodyTempDataActivity = this@BodyTempDataActivity
//                bodyTempDataActivity.array_temp_data = tempList as ArrayList<BodyTempData>
////                    bodyTempDataActivity.SQLite_health_tracker!!.GetTemperatureDataByUserID(
////                        sharedPreferencesUtils!!.getUserId()
////                    ) as ArrayList<BodyTempData>
//                data_handler.sendMessage(data_handler.obtainMessage(0))
//                return null
//            } catch (e: Exception) {
//                e.printStackTrace()
//                data_handler.sendMessage(data_handler.obtainMessage(99))
//                return null
//            }
//        }
//
//        public override fun onPostExecute(r1: Void?) {
//            super.onPostExecute(null)
//        }
//    }


    private fun fetchData() {
        val token = sharedPreferencesUtils?.read("token", "")
        if (!token.isNullOrEmpty()) {
            val call = ApiClient.apiService.getBodyTempsByUserId(token)
            call.enqueue(object : Callback<BodyTempResponse> {
                override fun onResponse(
                    call: Call<BodyTempResponse>,
                    response: Response<BodyTempResponse>
                ) {
                    if (response.isSuccessful) {
                        val dResponse = response.body()

                        if (dResponse?.code == 1) {
                            SetTempData(dResponse.data)
                        } else {
                            showMessage(dResponse?.msg ?: "An error occurred ")
                        }
                    } else {
                        showMessage("Unable to fetch ")
                    }
                }

                override fun onFailure(call: Call<BodyTempResponse>, t: Throwable) {
                    showMessage("Unable to fetch, Please try again later")
                }

            })
        }

    }


    private fun deleteData(rowId: Int) {
        val token = sharedPreferencesUtils?.read("token", "")
        if (!token.isNullOrEmpty()) {
            val call = ApiClient.apiService.deleteBodyTemp(token, rowId)
            call.enqueue(object : Callback<BodyTempData> {
                override fun onResponse(
                    call: Call<BodyTempData>,
                    response: Response<BodyTempData>
                ) {
                    if (response.isSuccessful) {
                        EUGeneralClass.ShowSuccessToast(
                            mContext,
                            AppConstants.data_deleted_messages
                        )
                        fetchData()
                    } else {
                        showMessage("Unable to delete")
                    }
                }

                override fun onFailure(call: Call<BodyTempData>, t: Throwable) {
                    showMessage("Unable to delete, Please try again later ..")
                }

            })
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
    }


}
