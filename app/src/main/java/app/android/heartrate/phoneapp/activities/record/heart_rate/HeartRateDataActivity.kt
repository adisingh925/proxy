package app.android.heartrate.phoneapp.activities.record.heart_rate

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
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.android.heartrate.phoneapp.R
import app.android.heartrate.phoneapp.activities.record.heart_rate.AddHeartRateActivity
import app.android.heartrate.phoneapp.adapters.HeartRateDataAdapter
import app.android.heartrate.phoneapp.adapters.SpinnerProfileAdapter
import app.android.heartrate.phoneapp.model.classes.HeartRateData
import app.android.heartrate.phoneapp.sharedpreferences.SharedPreferences
import app.android.heartrate.phoneapp.sqlite.SQLiteHealthTracker
import app.android.heartrate.phoneapp.utils.AppConstants
import app.android.heartrate.phoneapp.utils.EUGeneralClass

class HeartRateDataActivity : AppCompatActivity() {
    var SQLite_health_tracker: SQLiteHealthTracker? = null

    var adapter_hr_data: HeartRateDataAdapter? = null
    var array_heart_rate_data: ArrayList<HeartRateData> = ArrayList()

    var is_user_interact: Boolean = false
    var mContext: Context? = null
    var push_animation: Animation? = null
    private lateinit var recycler_heart_rate: RecyclerView

    var spinner_profile_adapter: SpinnerProfileAdapter? = null
    var spinner_profiles: Spinner? = null
    var txt_no_data: TextView? = null
    var spinner_txt_name: TextView? = null

    var sharedPreferencesUtils: SharedPreferences? = null

    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        SetView()


        AppConstants.overridePendingTransitionEnter(this)
    }

    private fun SetView() {
        setContentView(R.layout.activity_heart_rate_data_list)

        this.mContext = this
        this.push_animation = AnimationUtils.loadAnimation(this, R.anim.view_push)
        setUpActionBar()
        sharedPreferencesUtils = SharedPreferences
        val sQLiteHealthTracker = SQLiteHealthTracker(this)
        this.SQLite_health_tracker = sQLiteHealthTracker
        sQLiteHealthTracker.openToWrite()
        this.spinner_profiles = findViewById(R.id.hr_spinner_profiles)
        this.recycler_heart_rate = findViewById(R.id.hr_rv_data)
        this.spinner_txt_name = findViewById(R.id.spinner_txt_name)
        recycler_heart_rate.setLayoutManager(LinearLayoutManager(this))
        recycler_heart_rate.setItemAnimator(DefaultItemAnimator())
        val textView = findViewById<TextView>(R.id.txt_no_data)
        this.txt_no_data = textView
        textView.visibility = View.GONE
        SetProfileSpinner()
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        this.is_user_interact = true
    }

    private fun SetProfileSpinner() {
        val name = sharedPreferencesUtils!!.getUserName()
        spinner_txt_name!!.text = name
    }


    private fun SetHeartRateDataList() {
        array_heart_rate_data.clear()
        val arrayList =
            SQLite_health_tracker!!.GetHeartRateDataByUserID(sharedPreferencesUtils!!.getUserId()) as ArrayList<HeartRateData>
        this.array_heart_rate_data = arrayList
        if (arrayList.size > 0) {
            txt_no_data!!.visibility = View.GONE
            val r0: HeartRateDataAdapter =
                object : HeartRateDataAdapter(this, this.array_heart_rate_data) {
                    override fun onHeartRateAdapterClickItem(i: Int, view: View) {
                        if (view.id == R.id.row_hr_rel_edit) {
                            AppConstants.selected_heart_rate_data =
                                array_heart_rate_data[i]
                            AppConstants.is_heart_rate_edit_mode = true
                            this@HeartRateDataActivity.AddHeartRateDataScreen()
                        }
                        if (view.id == R.id.row_hr_rel_delete) {
                            this@HeartRateDataActivity.ConformDeleteDialog(
                                array_heart_rate_data[i].row_id,
                                array_heart_rate_data[i].user_id
                            )
                        }
                    }
                }
            this.adapter_hr_data = r0
            recycler_heart_rate!!.adapter = r0
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
                SQLite_health_tracker!!.deleteHeartRateByID(i)
                EUGeneralClass.ShowSuccessToast(
                    this@HeartRateDataActivity,
                    AppConstants.data_deleted_messages
                )
                this@HeartRateDataActivity.SetHeartRateDataList()
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }
            dialog.dismiss()
        }
        button2.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }


    private fun AddHeartRateDataScreen() {
        startActivity(Intent(this, AddHeartRateActivity::class.java))
    }

    private fun setUpActionBar() {
        (findViewById<View>(R.id.toolbar_txt_title_1) as TextView).text =
            resources.getString(R.string.lbl_header_heart)
        (findViewById<View>(R.id.toolbar_txt_title_2) as TextView).text =
            resources.getString(R.string.lbl_header_rate_data)
        findViewById<View>(R.id.tool_bar_rel_add_user).setOnClickListener {
            AppConstants.is_heart_rate_edit_mode = false
            this@HeartRateDataActivity.AddHeartRateDataScreen()
        }
        findViewById<View>(R.id.toolbar_rel_back).setOnClickListener { this@HeartRateDataActivity.onBackPressed() }
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
        SetHeartRateDataList()
    }


    override fun onBackPressed() {
        super.onBackPressed()
    }
}
