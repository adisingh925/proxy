package app.android.heartrate.phoneapp.activities.record

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.android.heartrate.phoneapp.R
import app.android.heartrate.phoneapp.adapters.BloodPressureDataAdapter
import app.android.heartrate.phoneapp.adapters.SpinnerProfileAdapter
import app.android.heartrate.phoneapp.databinding.ActivityBpDataListBinding
import app.android.heartrate.phoneapp.fragments.base.BaseActivity
import app.android.heartrate.phoneapp.model.classes.BloodPressureData
import app.android.heartrate.phoneapp.sharedpreferences.SharedPreferences
import app.android.heartrate.phoneapp.sqlite.SQLiteHealthTracker
import app.android.heartrate.phoneapp.utils.AppConstants
import app.android.heartrate.phoneapp.utils.EUGeneralClass

class BloodPressureDataActivity : BaseActivity() {
    var SQLite_health_tracker: SQLiteHealthTracker? = null
    var adapter_bp_data: BloodPressureDataAdapter? = null
    var array_bp_data: ArrayList<BloodPressureData> = ArrayList()
    private var sharedPreferencesUtils: SharedPreferences? = null
    private lateinit var img_status_info: ImageView
    var is_user_interact: Boolean = false
    var mContext: Context? = null
    var push_animation: Animation? = null
    private lateinit var recycler_blood_pressure: RecyclerView
    var spinner_profile_adapter: SpinnerProfileAdapter? = null
    var spinner_profiles: Spinner? = null
    var txt_no_data: TextView? = null
    var spinner_txt_name: TextView? = null
    private lateinit var binding: ActivityBpDataListBinding


    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        binding = ActivityBpDataListBinding.inflate(layoutInflater)
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
        this.spinner_profiles = findViewById(R.id.bp_spinner_profiles)
        this.spinner_txt_name = findViewById(R.id.spinner_txt_name)
        this.recycler_blood_pressure = findViewById(R.id.bp_rv_data)
        recycler_blood_pressure.setLayoutManager(LinearLayoutManager(this))
        recycler_blood_pressure.setItemAnimator(DefaultItemAnimator())
        val textView = findViewById<TextView>(R.id.txt_no_data)
        this.txt_no_data = textView
        textView.visibility = View.GONE
        this.img_status_info = findViewById(R.id.bp_img_status_info)
        SetProfileSpinner()
        img_status_info.setOnClickListener { view ->
            view.startAnimation(push_animation)
            StatusInfoDialog()
        }
    }


    private fun StatusInfoDialog() {
        val dialog = Dialog(this, R.style.TransparentBackground)
        dialog.requestWindowFeature(1)
        dialog.setContentView(R.layout.dialog_bp_status_info)
        val button = dialog.findViewById<Button>(R.id.dialog_bp_status_btn_ok)
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


    private fun SetBloodPressureList() {
        array_bp_data.clear()
        val arrayList = SQLite_health_tracker!!.GetBloodPressureDataByUserID(
                sharedPreferencesUtils!!.getUserId()
            )
        this.array_bp_data = arrayList as ArrayList<BloodPressureData>
        if (arrayList.size > 0) {
            txt_no_data!!.visibility = View.GONE
            Log.e(" arrayList ", " ===> " + arrayList.size)
            val r0: BloodPressureDataAdapter =
                object : BloodPressureDataAdapter(this, this.array_bp_data) {
                    override fun onBloodPressureAdapterClickItem(i: Int, view: View) {
                        if (view.id == R.id.row_bp_rel_edit) {
                            AppConstants.selected_bp_data =
                                array_bp_data[i]
                            AppConstants.is_bp_edit_mode = true
                            this@BloodPressureDataActivity.AddBloodPressureScreen()
                        }
                        if (view.id == R.id.row_bp_rel_delete) {
                            this@BloodPressureDataActivity.ConformDeleteDialog(
                                array_bp_data[i].row_id,
                                array_bp_data[i].user_id
                            )
                        }
                    }
                }
            this.adapter_bp_data = r0
            recycler_blood_pressure!!.adapter = r0
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
                SQLite_health_tracker!!.deleteBloodPressureByID(i)
                EUGeneralClass.ShowSuccessToast(
                    this@BloodPressureDataActivity,
                    AppConstants.data_deleted_messages
                )
                this@BloodPressureDataActivity.SetBloodPressureList()
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }
            dialog.dismiss()
        }
        button2.setOnClickListener { view: View? -> dialog.dismiss() }
        dialog.show()
    }


    private fun AddBloodPressureScreen() {
        startActivity(Intent(this, AddBloodPressureActivity::class.java))
    }

    private fun setUpActionBar() {
        (findViewById<View>(R.id.toolbar_txt_title_1) as TextView).text =
            resources.getString(R.string.lbl_header_blood)
        (findViewById<View>(R.id.toolbar_txt_title_2) as TextView).text =
            resources.getString(R.string.lbl_header_pressure_data)
        findViewById<View>(R.id.tool_bar_rel_add_user).setOnClickListener {
            AppConstants.is_bp_edit_mode = false
            this@BloodPressureDataActivity.AddBloodPressureScreen()
        }
        findViewById<View>(R.id.toolbar_rel_back).setOnClickListener { this@BloodPressureDataActivity.onBackPressed() }
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
        SetBloodPressureList()
    }


    override fun onBackPressed() {
        super.onBackPressed()
    }
}
