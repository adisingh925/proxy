package app.android.heartrate.phoneapp.activities.record.cholestrol

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
import app.android.heartrate.phoneapp.activities.record.cholestrol.AddCholesterolActivity
import app.android.heartrate.phoneapp.adapters.CholesterolDataAdapter
import app.android.heartrate.phoneapp.adapters.SpinnerProfileAdapter
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

class CholesterolDataActivity : AppCompatActivity() {
    var SQLite_health_tracker: SQLiteHealthTracker? = null

    var adapter_cholesterol_data: CholesterolDataAdapter? = null
    var array_cholesterol_data: ArrayList<CholesterolData> = ArrayList()


    var is_user_interact: Boolean = false
    var mContext: Context? = null
    var push_animation: Animation? = null
    private lateinit var recycler_cholesterol: RecyclerView

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
        setContentView(R.layout.activity_cholesterol_data_list)

        this.mContext = this
        this.push_animation = AnimationUtils.loadAnimation(this, R.anim.view_push)
        setUpActionBar()
        sharedPreferencesUtils = SharedPreferences
        val sQLiteHealthTracker = SQLiteHealthTracker(this)
        this.SQLite_health_tracker = sQLiteHealthTracker
        sQLiteHealthTracker.openToWrite()
        this.spinner_profiles = findViewById(R.id.cholesterol_spinner_profiles)
        this.recycler_cholesterol = findViewById(R.id.cholesterol_rv_data)
        this.spinner_txt_name = findViewById(R.id.spinner_txt_name)
        recycler_cholesterol.setLayoutManager(LinearLayoutManager(this))
        recycler_cholesterol.setItemAnimator(DefaultItemAnimator())
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
        val profileData = SQLite_health_tracker!!.GetUserProfileData()
        val name = profileData.firstName + " " + profileData.lastName
        spinner_txt_name!!.text = name
    }


    private fun SetCholesterolDataList(data: List<CholesterolData>?) {
        array_cholesterol_data.clear()
        val arrayList = data
          //  SQLite_health_tracker!!.GetCholesterolDataByUserID(sharedPreferencesUtils!!.getUserId()) as ArrayList<CholesterolData>
        this.array_cholesterol_data = arrayList as ArrayList<CholesterolData>
        if (arrayList.size > 0) {
            txt_no_data!!.visibility = View.GONE
            val r0: CholesterolDataAdapter =
                object : CholesterolDataAdapter(this, this.array_cholesterol_data) {
                    override fun onCholesterolAdapterClickItem(i: Int, view: View) {
                        if (view.id == R.id.row_cholesterol_rel_edit) {
                            AppConstants.selected_cholesterol_data =
                                array_cholesterol_data[i]
                            AppConstants.is_cholesterol_edit_mode = true
                            AddCholesterolScreen()
                        }
                        if (view.id == R.id.row_cholesterol_rel_delete) {
                            ConformDeleteDialog(
                                array_cholesterol_data[i].row_id,
                                array_cholesterol_data[i].user_id
                            )
                        }
                    }
                }
            this.adapter_cholesterol_data = r0
            recycler_cholesterol!!.adapter = r0
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
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }
            dialog.dismiss()
        }
        button2.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }


    private fun AddCholesterolScreen() {
        startActivity(Intent(this, AddCholesterolActivity::class.java))
    }

    private fun setUpActionBar() {
        (findViewById<View>(R.id.toolbar_txt_title_1) as TextView).text =
            resources.getString(R.string.lbl_header_cholesterol)
        (findViewById<View>(R.id.toolbar_txt_title_2) as TextView).text =
            resources.getString(R.string.lbl_header_data)
        findViewById<View>(R.id.tool_bar_rel_add_user).setOnClickListener {
            AppConstants.is_cholesterol_edit_mode = false
            this@CholesterolDataActivity.AddCholesterolScreen()
        }
        findViewById<View>(R.id.toolbar_rel_back).setOnClickListener { this@CholesterolDataActivity.onBackPressed() }
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
            val call = ApiClient.apiService.getCholesterolsByUserId(token)
            call.enqueue(object : Callback<CholesterolResponse> {
                override fun onResponse(
                    call: Call<CholesterolResponse>,
                    response: Response<CholesterolResponse>
                ) {
                    if (response.isSuccessful) {
                        val dResponse = response.body()

                        if (dResponse?.code == 1) {
                            SetCholesterolDataList(dResponse.data)
                        } else {
                            showMessage(dResponse?.msg ?: "An error occurred ")
                        }
                    } else {
                        showMessage("Unable to fetch blood count")
                    }
                }

                override fun onFailure(call: Call<CholesterolResponse>, t: Throwable) {
                    showMessage("Unable to fetch, Please try again later")
                }

            })
        }

    }


    private fun deleteData(rowId: Int) {
        val token = sharedPreferencesUtils?.read("token", "")
        if (!token.isNullOrEmpty()) {
            val call = ApiClient.apiService.deleteCholesterol(token, rowId)
            call.enqueue(object : Callback<CholesterolData> {
                override fun onResponse(
                    call: Call<CholesterolData>,
                    response: Response<CholesterolData>
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

                override fun onFailure(call: Call<CholesterolData>, t: Throwable) {
                    showMessage("Unable to delete, Please try again later ..")
                }

            })
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
    }




}
