package app.android.heartrate.phoneapp.activities.record.weight

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
import app.android.heartrate.phoneapp.activities.record.weight.AddWeightActivity
import app.android.heartrate.phoneapp.adapters.SpinnerProfileAdapter
import app.android.heartrate.phoneapp.adapters.WeightDataAdapter
import app.android.heartrate.phoneapp.model.classes.WeightData
import app.android.heartrate.phoneapp.sharedpreferences.SharedPreferences
import app.android.heartrate.phoneapp.sqlite.SQLiteHealthTracker
import app.android.heartrate.phoneapp.utils.AppConstants
import app.android.heartrate.phoneapp.utils.EUGeneralClass

class WeightDataActivity : AppCompatActivity() {
    var SQLite_health_tracker: SQLiteHealthTracker? = null

    var adapter_weight_data: WeightDataAdapter? = null
    var array_weight_data: ArrayList<WeightData> = ArrayList()

    var is_user_interact: Boolean = false
    var mContext: Context? = null
    var push_animation: Animation? = null
    private lateinit var recycler_weight: RecyclerView

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
        setContentView(R.layout.activity_weight_data_list)

        this.mContext = this
        this.push_animation = AnimationUtils.loadAnimation(this, R.anim.view_push)
        setUpActionBar()
        sharedPreferencesUtils = SharedPreferences
        val sQLiteHealthTracker = SQLiteHealthTracker(this)
        this.SQLite_health_tracker = sQLiteHealthTracker
        sQLiteHealthTracker.openToWrite()
        this.spinner_profiles = findViewById(R.id.weight_spinner_profiles)
        this.spinner_txt_name = findViewById(R.id.spinner_txt_name)
        this.recycler_weight = findViewById(R.id.weight_rv_data)
        recycler_weight.setLayoutManager(LinearLayoutManager(this))
        recycler_weight.setItemAnimator(DefaultItemAnimator())
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


    private fun SetWeightList() {
        array_weight_data.clear()
        val arrayList: ArrayList<WeightData> =
            SQLite_health_tracker!!.GetWeightDataByUserID(sharedPreferencesUtils!!.getUserId()) as ArrayList<WeightData>
        this.array_weight_data = arrayList
        if (arrayList.size > 0) {
            txt_no_data!!.visibility = View.GONE
            val r0: WeightDataAdapter = object : WeightDataAdapter(this, this.array_weight_data) {
                override fun onWeightAdapterClickItem(i: Int, view: View) {
                    if (view.id == R.id.row_weight_rel_edit) {
                        AppConstants.selected_weight_data =
                            array_weight_data[i]
                        AppConstants.is_weight_edit_mode = true
                        this@WeightDataActivity.AddWeightScreen()
                    }
                    if (view.id == R.id.row_weight_rel_delete) {
                        this@WeightDataActivity.ConformDeleteDialog(
                            array_weight_data[i].row_id,
                            array_weight_data[i].user_id
                        )
                    }
                }
            }
            this.adapter_weight_data = r0
            recycler_weight!!.adapter = r0
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
                SQLite_health_tracker!!.deleteWeightByID(i)
                EUGeneralClass.ShowSuccessToast(
                    this@WeightDataActivity,
                    AppConstants.data_deleted_messages
                )
                this@WeightDataActivity.SetWeightList()
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }
            dialog.dismiss()
        }
        button2.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun AddWeightScreen() {
        startActivity(Intent(this, AddWeightActivity::class.java))
    }

    private fun setUpActionBar() {
        (findViewById<View>(R.id.toolbar_txt_title_1) as TextView).text =
            resources.getString(R.string.lbl_header_weight)
        (findViewById<View>(R.id.toolbar_txt_title_2) as TextView).text =
            resources.getString(R.string.lbl_header_data)
        findViewById<View>(R.id.tool_bar_rel_add_user).setOnClickListener { view: View? ->
            AppConstants.is_weight_edit_mode = false
            this@WeightDataActivity.AddWeightScreen()
        }
        findViewById<View>(R.id.toolbar_rel_back).setOnClickListener { view: View? -> this@WeightDataActivity.onBackPressed() }
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
        SetWeightList()
    }


    override fun onBackPressed() {
        super.onBackPressed()
    }
}
