package app.android.heartrate.phoneapp.activities.record.bmi

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
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.android.heartrate.phoneapp.R
import app.android.heartrate.phoneapp.activities.record.bmi.AddBMIDataActivity
import app.android.heartrate.phoneapp.adapters.BMIDataAdapter
import app.android.heartrate.phoneapp.adapters.SpinnerProfileAdapter
import app.android.heartrate.phoneapp.model.classes.BMIData
import app.android.heartrate.phoneapp.sharedpreferences.SharedPreferences
import app.android.heartrate.phoneapp.sqlite.SQLiteHealthTracker
import app.android.heartrate.phoneapp.utils.AppConstants
import app.android.heartrate.phoneapp.utils.EUGeneralClass

class BMIDataActivity : AppCompatActivity() {
    var SQLite_health_tracker: SQLiteHealthTracker? = null
    var adapter_bmi_data: BMIDataAdapter? = null
    var array_bmi_data: ArrayList<BMIData> = ArrayList()
    var img_status_info: ImageView? = null
    var is_user_interact: Boolean = false
    var mContext: Context? = null
    var push_animation: Animation? = null
    private lateinit var recycler_bmi: RecyclerView
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
        setContentView(R.layout.activity_bmi_data_list)

        this.mContext = this
        this.push_animation = AnimationUtils.loadAnimation(this, R.anim.view_push)
        setUpActionBar()
        sharedPreferencesUtils = SharedPreferences
        val sQLiteHealthTracker = SQLiteHealthTracker(this)
        this.SQLite_health_tracker = sQLiteHealthTracker
        sQLiteHealthTracker.openToWrite()
        this.spinner_profiles = findViewById(R.id.bmi_spinner_profiles)
        this.recycler_bmi = findViewById(R.id.bmi_rv_data)
        this.spinner_txt_name = findViewById(R.id.spinner_txt_name)
        recycler_bmi.setLayoutManager(LinearLayoutManager(this))
        recycler_bmi.setItemAnimator(DefaultItemAnimator())
        val textView = findViewById<TextView>(R.id.txt_no_data)
        this.txt_no_data = textView
        textView.visibility = View.GONE
        SetProfileSpinner()
        val imageView = findViewById<ImageView>(R.id.bmi_img_status_info)
        this.img_status_info = imageView
        imageView.setOnClickListener { view ->
            view.startAnimation(this@BMIDataActivity.push_animation)
            this@BMIDataActivity.StatusInfoDialog()
        }
    }


    private fun StatusInfoDialog() {
        val dialog = Dialog(this, R.style.TransparentBackground)
        dialog.requestWindowFeature(1)
        dialog.setContentView(R.layout.dialog_bmi_status_info)
        val button = dialog.findViewById<Button>(R.id.dialog_bmi_status_btn_ok)
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


    private fun SetBMIDataList() {
        array_bmi_data.clear()
        val arrayList: ArrayList<BMIData> =
            SQLite_health_tracker!!.GetBMIDataByUserID(sharedPreferencesUtils!!.getUserId()) as ArrayList<BMIData>
        this.array_bmi_data = arrayList
        if (arrayList.size > 0) {
            txt_no_data!!.visibility = View.GONE
            val r0: BMIDataAdapter = object : BMIDataAdapter(this, this.array_bmi_data) {
                override fun onBMIAdapterClickItem(i: Int, view: View) {
                    if (view.id == R.id.row_bmi_lin_status) {
                        AppConstants.selected_bmi_data =
                            array_bmi_data[i]
                        AppConstants.is_bmi_edit_mode = true
                        val trim = array_bmi_data[i].bmi.trim { it <= ' ' }
                        val intent = Intent(this@BMIDataActivity, BMIResultActivity::class.java)
                        intent.putExtra("ResultText", AppConstants.setResultText(trim.toFloat()))
                        intent.putExtra("BMI", trim)
                        this@BMIDataActivity.startActivity(intent)
                    }
                    if (view.id == R.id.row_bmi_rel_edit) {
                        AppConstants.selected_bmi_data =
                            array_bmi_data[i]
                        AppConstants.is_bmi_edit_mode = true
                        this@BMIDataActivity.AddBMIDataScreen()
                    }
                    if (view.id == R.id.row_bmi_rel_delete) {
                        this@BMIDataActivity.ConformDeleteDialog(
                            array_bmi_data[i].row_id,
                            array_bmi_data[i].user_id
                        )
                    }
                }
            }
            this.adapter_bmi_data = r0
            recycler_bmi!!.adapter = r0
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
                SQLite_health_tracker!!.deleteBMIByID(i)
                EUGeneralClass.ShowSuccessToast(
                    this@BMIDataActivity,
                    AppConstants.data_deleted_messages
                )
                this@BMIDataActivity.SetBMIDataList()
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }
            dialog.dismiss()
        }
        button2.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }


    private fun AddBMIDataScreen() {
        startActivity(Intent(this, AddBMIDataActivity::class.java))
    }

    private fun setUpActionBar() {
        (findViewById<View>(R.id.toolbar_txt_title_1) as TextView).text =
            resources.getString(R.string.lbl_header_body)
        (findViewById<View>(R.id.toolbar_txt_title_2) as TextView).text =
            resources.getString(R.string.lbl_header_mass_index)
        findViewById<View>(R.id.tool_bar_rel_add_user).setOnClickListener {
            AppConstants.is_bmi_edit_mode = false
            this@BMIDataActivity.AddBMIDataScreen()
        }
        findViewById<View>(R.id.toolbar_rel_back).setOnClickListener { this@BMIDataActivity.onBackPressed() }
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
        SetBMIDataList()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        BackScreen()
    }

    private fun BackScreen() {
        finish()
        AppConstants.overridePendingTransitionExit(this)
    }
}
