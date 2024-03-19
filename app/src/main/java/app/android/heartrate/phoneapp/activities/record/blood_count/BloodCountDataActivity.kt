package app.android.heartrate.phoneapp.activities.record.blood_count

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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.android.heartrate.phoneapp.R
import app.android.heartrate.phoneapp.adapters.BloodCountDataAdapter
import app.android.heartrate.phoneapp.databinding.ActivityBloodCountListBinding
import app.android.heartrate.phoneapp.model.classes.BloodCountData
import app.android.heartrate.phoneapp.model.classes.BloodCountResponse
import app.android.heartrate.phoneapp.retrofit.ApiClient
import app.android.heartrate.phoneapp.sharedpreferences.SharedPreferences
import app.android.heartrate.phoneapp.sqlite.SQLiteHealthTracker
import app.android.heartrate.phoneapp.utils.AppConstants
import app.android.heartrate.phoneapp.utils.EUGeneralClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BloodCountDataActivity : AppCompatActivity() {
    var SQLite_health_tracker: SQLiteHealthTracker? = null

    var adapter_blood_count_data: BloodCountDataAdapter? = null

    var array_blood_count_data: ArrayList<BloodCountData> = ArrayList()

    var is_user_interact: Boolean = false
    var mContext: Context? = null
    var push_animation: Animation? = null
    private lateinit var recycler_blood_count_data: RecyclerView
    var spinner_profiles: Spinner? = null
    var txt_no_data: TextView? = null
    var spinner_txt_name: TextView? = null
    private lateinit var sharedPreferencesUtils: SharedPreferences

    private lateinit var binding: ActivityBloodCountListBinding

    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        binding = ActivityBloodCountListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        SetView()
        AppConstants.overridePendingTransitionEnter(this)
    }

    private fun SetView() {
        sharedPreferencesUtils = SharedPreferences
        this.mContext = this
        this.push_animation = AnimationUtils.loadAnimation(this, R.anim.view_push)
        setUpActionBar()
        val sQLiteHealthTracker = SQLiteHealthTracker(this)
        this.SQLite_health_tracker = sQLiteHealthTracker
        sQLiteHealthTracker.openToWrite()
        this.spinner_profiles = findViewById(R.id.bc_spinner_profiles)
        this.recycler_blood_count_data = findViewById(R.id.bc_rv_data)
        this.spinner_txt_name = findViewById(R.id.spinner_txt_name)
        recycler_blood_count_data.setLayoutManager(LinearLayoutManager(this))
        recycler_blood_count_data.setItemAnimator(DefaultItemAnimator())
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


    private fun SetBloodCountDataList(arrayList: List<BloodCountData>?) {
        array_blood_count_data.clear()
        this.array_blood_count_data = arrayList as ArrayList<BloodCountData>
        if (arrayList.isNotEmpty()) {
            txt_no_data!!.visibility = View.GONE
            val r0: BloodCountDataAdapter =
                object : BloodCountDataAdapter(this, this.array_blood_count_data) {
                    override fun onBloodCountAdapterClickItem(i: Int, view: View) {
                        if (view.id == R.id.row_bc_rel_edit) {
                            AppConstants.selected_blood_count_data =
                                array_blood_count_data[i]
                            AppConstants.is_blood_count_edit_mode = true
                            AddBloodCountScreen()
                        }
                        if (view.id == R.id.row_bc_rel_delete) {
                            this@BloodCountDataActivity.ConformDeleteDialog(
                                array_blood_count_data[i].row_id,
                                array_blood_count_data[i].user_id
                            )
                        }
                    }
                }
            this.adapter_blood_count_data = r0
            recycler_blood_count_data!!.adapter = r0
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
                deleteBloodCount(i)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }
            dialog.dismiss()
        }
        button2.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }


    private fun AddBloodCountScreen() {
        startActivity(Intent(this, AddBloodCountActivity::class.java))
    }

    private fun setUpActionBar() {
        (findViewById<View>(R.id.toolbar_txt_title_1) as TextView).text =
            resources.getString(R.string.lbl_header_blood)
        (findViewById<View>(R.id.toolbar_txt_title_2) as TextView).text =
            resources.getString(R.string.lbl_header_count_data)
        findViewById<View>(R.id.tool_bar_rel_add_user).setOnClickListener {
            AppConstants.is_blood_count_edit_mode = false
            this@BloodCountDataActivity.AddBloodCountScreen()
        }
        findViewById<View>(R.id.toolbar_rel_back).setOnClickListener { view: View? -> this@BloodCountDataActivity.onBackPressed() }
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
        fetchBloodCountData()
    }


    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun fetchBloodCountData() {
        val token = sharedPreferencesUtils.read("token", "")
        if (!token.isNullOrEmpty()) {
            val call = ApiClient.apiService.getBloodCountsByUserId(token)
            call.enqueue(object : Callback<BloodCountResponse> {
                override fun onResponse(
                    call: Call<BloodCountResponse>,
                    response: Response<BloodCountResponse>
                ) {
                    if (response.isSuccessful) {
                        val dResponse = response.body()

                        if (dResponse?.code == 1) {
                            SetBloodCountDataList(dResponse.data)
                        } else {
                            showMessage(dResponse?.msg ?: "An error occurred ")
                        }
                    } else {
                        showMessage("Unable to fetch blood count")
                    }
                }

                override fun onFailure(call: Call<BloodCountResponse>, t: Throwable) {
                    showMessage("Unable to fetch blood count, Please try again later")
                }

            })
        }

    }

    private fun deleteBloodCount(rowId: Int) {
        val token = sharedPreferencesUtils.read("token", "")
        if (!token.isNullOrEmpty()) {
            val call = ApiClient.apiService.deleteBloodCount(token, rowId)
            call.enqueue(object : Callback<BloodCountData> {
                override fun onResponse(
                    call: Call<BloodCountData>,
                    response: Response<BloodCountData>
                ) {
                    if (response.isSuccessful) {
                        EUGeneralClass.ShowSuccessToast(
                            this@BloodCountDataActivity,
                            AppConstants.data_deleted_messages
                        )

                        fetchBloodCountData()
                    } else {
                        showMessage("Unable to delete")
                    }
                }

                override fun onFailure(call: Call<BloodCountData>, t: Throwable) {
                    showMessage("Unable to delete this blood count, Please try again later ..")
                }

            })
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
    }
}
