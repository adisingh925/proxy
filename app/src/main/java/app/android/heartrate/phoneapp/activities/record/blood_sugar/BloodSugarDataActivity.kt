package app.android.heartrate.phoneapp.activities.record.blood_sugar

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.android.heartrate.phoneapp.R
import app.android.heartrate.phoneapp.adapters.BloodSugarDataAdapter
import app.android.heartrate.phoneapp.adapters.SpinnerProfileAdapter
import app.android.heartrate.phoneapp.model.bloodpressure.BloodPressureResponse
import app.android.heartrate.phoneapp.model.bloodsugar.BloodSugarResponse
import app.android.heartrate.phoneapp.model.classes.BloodPressureData
import app.android.heartrate.phoneapp.model.classes.BloodSugarData
import app.android.heartrate.phoneapp.retrofit.ApiClient
import app.android.heartrate.phoneapp.sharedpreferences.SharedPreferences
import app.android.heartrate.phoneapp.sqlite.SQLiteHealthTracker
import app.android.heartrate.phoneapp.utils.AppConstants
import app.android.heartrate.phoneapp.utils.EUGeneralClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BloodSugarDataActivity : AppCompatActivity() {
    var SQLite_health_tracker: SQLiteHealthTracker? = null

    var adapter_bs_data: BloodSugarDataAdapter? = null
    var array_bs_data: ArrayList<BloodSugarData> = ArrayList()
    var img_status_info: ImageView? = null

    var is_user_interact: Boolean = false
    var mContext: Context? = null
    var push_animation: Animation? = null
    private lateinit var recycler_blood_sugar: RecyclerView

    var spinner_profile_adapter: SpinnerProfileAdapter? = null
    var spinner_profiles: Spinner? = null
    var txt_no_data: TextView? = null
    var spinner_txt_name: TextView? = null

    private var sharedPreferencesUtils: SharedPreferences? = null


    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        SetView()


        AppConstants.overridePendingTransitionEnter(this)
    }

    private fun SetView() {
        setContentView(R.layout.activity_bs_data_list)

        this.mContext = this
        this.push_animation = AnimationUtils.loadAnimation(this, R.anim.view_push)
        setUpActionBar()
        sharedPreferencesUtils = SharedPreferences
        val sQLiteHealthTracker = SQLiteHealthTracker(this)
        this.SQLite_health_tracker = sQLiteHealthTracker
        sQLiteHealthTracker.openToWrite()
        this.spinner_profiles = findViewById(R.id.bs_spinner_profiles)
        this.spinner_txt_name = findViewById(R.id.spinner_txt_name)
        this.recycler_blood_sugar = findViewById(R.id.bs_rv_data)
        recycler_blood_sugar.setLayoutManager(LinearLayoutManager(this))
        recycler_blood_sugar.setItemAnimator(DefaultItemAnimator())
        val textView = findViewById<TextView>(R.id.txt_no_data)
        this.txt_no_data = textView
        textView.visibility = View.GONE
        SetProfileSpinner()
        val imageView = findViewById<ImageView>(R.id.bs_img_status_info)
        this.img_status_info = imageView
        imageView.setOnClickListener { view ->
            view.startAnimation(this@BloodSugarDataActivity.push_animation)
            this@BloodSugarDataActivity.StatusInfoDialog()
        }
    }


    private fun StatusInfoDialog() {
        val dialog = Dialog(this, R.style.TransparentBackground)
        dialog.requestWindowFeature(1)
        dialog.setContentView(R.layout.dialog_bs_status_info)
        val button = dialog.findViewById<Button>(R.id.dialog_bs_status_btn_ok)
        button.text = "OK"
        button.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        this.is_user_interact = true
    }

    private fun SetProfileSpinner() {
        val name = sharedPreferencesUtils!!.getUserName()
        spinner_txt_name!!.text = name
    }


    private fun SetBloodSugarList(bloodSugarList: List<BloodSugarData>?) {
        array_bs_data.clear()
        val arrayList = bloodSugarList
//            SQLite_health_tracker!!.GetBloodSugarDataByUserID(sharedPreferencesUtils!!.getUserId()) as ArrayList<*>
        this.array_bs_data = arrayList as ArrayList<BloodSugarData>
        if (arrayList.size > 0) {
            txt_no_data!!.visibility = View.GONE
            val r0: BloodSugarDataAdapter =
                object : BloodSugarDataAdapter(this, this.array_bs_data) {
                    override fun onBloodSugarAdapterClickItem(i: Int, view: View) {
                        if (view.id == R.id.row_bs_rel_edit) {

                            AppConstants.selected_bs_data =
                                array_bs_data[i]
                            AppConstants.is_bs_edit_mode = true
                            this@BloodSugarDataActivity.AddBloodSugarScreen()
                        }
                        if (view.id == R.id.row_bs_rel_delete) {
                            ConformDeleteDialog(
                                array_bs_data[i].row_id,
                                array_bs_data[i].user_id
                            )
                        }
                    }
                }
            this.adapter_bs_data = r0
            recycler_blood_sugar!!.adapter = r0
            return
        }
        txt_no_data!!.visibility = View.VISIBLE
    }


    private fun ConformDeleteDialog(i: Int, i2: Int) {
        val dialog = Dialog(this, R.style.TransparentBackground)
        dialog.requestWindowFeature(1)
        dialog.setContentView(R.layout.dialog_rate)
        val button = dialog.findViewById<Button>(R.id.dialog_conform_btn_yes)
        val button2 = dialog.findViewById<Button>(R.id.dialog_conform_btn_no)
        (dialog.findViewById<View>(R.id.dialog_conform_txt_header) as TextView).text = "Delete"
        (dialog.findViewById<View>(R.id.dialog_conform_txt_message) as TextView).text =
            "Do you want to delete selected data?"
        button.text = "Delete"
        button2.text = "Cancel"
        button.setOnClickListener {
            try {


                deleteData(i)
//                SQLite_health_tracker!!.deleteBloodSugarByID(i)
//                EUGeneralClass.ShowSuccessToast(
//                    this@BloodSugarDataActivity,
//                    AppConstants.data_deleted_messages
//                )
//
//                this@BloodSugarDataActivity.SetBloodSugarList()
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }
            dialog.dismiss()
        }
        button2.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }


    private fun AddBloodSugarScreen() {
        startActivity(Intent(this, AddBloodSugarActivity::class.java))
    }

    private fun setUpActionBar() {
        (findViewById<View>(R.id.toolbar_txt_title_1) as TextView).text =
            resources.getString(R.string.lbl_header_blood)
        (findViewById<View>(R.id.toolbar_txt_title_2) as TextView).text =
            resources.getString(R.string.lbl_header_sugar_data)
        findViewById<View>(R.id.tool_bar_rel_add_user).setOnClickListener {
            AppConstants.is_bs_edit_mode = false
            this@BloodSugarDataActivity.AddBloodSugarScreen()
        }
        findViewById<View>(R.id.toolbar_rel_back).setOnClickListener { this@BloodSugarDataActivity.onBackPressed() }
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

    private fun fetchData() {
        val token = sharedPreferencesUtils?.read("token", "")
        if (!token.isNullOrEmpty()) {
            val call = ApiClient.apiService.getBloodSugarsByUserId(token)
            call.enqueue(object : Callback<BloodSugarResponse> {
                override fun onResponse(
                    call: Call<BloodSugarResponse>,
                    response: Response<BloodSugarResponse>
                ) {
                    if (response.isSuccessful) {
                        val dResponse = response.body()

                        if (dResponse?.code == 1) {
                            SetBloodSugarList(dResponse.data)
                        } else {
                            showMessage(dResponse?.msg ?: "An error occurred ")
                        }
                    } else {
                        showMessage("Unable to fetch blood count")
                    }
                }

                override fun onFailure(call: Call<BloodSugarResponse>, t: Throwable) {
                    showMessage("Unable to fetch blood count, Please try again later")
                }

            })
        }

    }


    private fun deleteData(rowId: Int) {
        val token = sharedPreferencesUtils?.read("token", "")
        if (!token.isNullOrEmpty()) {
            val call = ApiClient.apiService.deleteBloodSugar(token, rowId)
            call.enqueue(object : Callback<BloodSugarData> {
                override fun onResponse(
                    call: Call<BloodSugarData>,
                    response: Response<BloodSugarData>
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

                override fun onFailure(call: Call<BloodSugarData>, t: Throwable) {
                    showMessage("Unable to delete this blood pressure, Please try again later ..")
                }

            })
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
    }


}
