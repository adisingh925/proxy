package app.android.heartrate.phoneapp.activities.record.bmi

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import app.android.heartrate.phoneapp.AdAdmob
import app.android.heartrate.phoneapp.R
import app.android.heartrate.phoneapp.utils.AppConstants
import java.util.Locale

class BMIResultActivity : AppCompatActivity() {
    var bmiValue: String? = null
    var img_arrow_moderately_obese: ImageView? = null
    var img_arrow_normal: ImageView? = null
    var img_arrow_overweight: ImageView? = null
    var img_arrow_severely: ImageView? = null
    var img_arrow_severely_obese: ImageView? = null
    var img_arrow_underweight: ImageView? = null
    var img_arrow_very_severely: ImageView? = null
    var img_arrow_very_severely_obese: ImageView? = null
    private lateinit var img_share: ImageView
    var push_animation: Animation? = null
    var resultText: String? = null
    private lateinit var txt_bmi_result: TextView
    var spinner_txt_name: TextView? = null
    private lateinit var txt_bmi_result_text: TextView


    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        SetView()

        val adAdmob = AdAdmob(this)
        adAdmob.FullscreenAd(this)
    }

    private fun SetView() {
        setContentView(R.layout.activity_bmi_result)

        this.push_animation = AnimationUtils.loadAnimation(this, R.anim.view_push)
        StrictMode.setVmPolicy(VmPolicy.Builder().build())
        setUpActionBar()
        this.img_arrow_very_severely = findViewById(R.id.bmi_result_img_very_severely)
        this.img_arrow_severely = findViewById(R.id.bmi_result_img_severely)
        this.img_arrow_underweight = findViewById(R.id.bmi_result_img_underweight)
        this.img_arrow_normal = findViewById(R.id.bmi_result_img_normal)
        this.img_arrow_overweight = findViewById(R.id.bmi_result_img_overweight)
        this.img_arrow_moderately_obese = findViewById(R.id.bmi_result_img_moderately_obese)
        this.spinner_txt_name = findViewById(R.id.spinner_txt_name)
        this.img_arrow_severely_obese = findViewById(R.id.bmi_result_img_severely_obese)
        this.img_arrow_very_severely_obese = findViewById(R.id.bmi_result_img_very_severely_obese)
        this.img_share = findViewById(R.id.bmi_result_img_share)
        this.txt_bmi_result_text = findViewById(R.id.bmi_result_txt_result_text)
        this.txt_bmi_result = findViewById(R.id.bmi_result_txt_result)
        val intent = intent
        this.resultText = intent.getStringExtra("ResultText")
        this.bmiValue = intent.getStringExtra("BMI")
        txt_bmi_result_text.setText(this.resultText)
        txt_bmi_result.setText(
            String.format(
                Locale.US,
                "%.2f",
                bmiValue?.toFloat()
            )
        )
        SetResultImage(this.resultText)
        img_share.setOnClickListener({ view ->
            view.startAnimation(this@BMIResultActivity.push_animation)
            val sb = "My BMI is " + String.format(
                Locale.US,
                "%.2f",
                bmiValue?.toFloat()
            ) +
                    " which is " +
                    this@BMIResultActivity.resultText + "."
            val intent = Intent()
            intent.setAction("android.intent.action.SEND")
            intent.putExtra("android.intent.extra.TEXT", sb)
            intent.setType("text/plain")
            this@BMIResultActivity.startActivity(Intent.createChooser(intent, "Share via"))
        })
    }

    private fun SetResultImage(str: String?) {
        if (str.equals(AppConstants.bmi_result_very_severely, ignoreCase = true)) {
            img_arrow_very_severely!!.visibility = View.VISIBLE
            img_arrow_severely!!.visibility = View.INVISIBLE
            img_arrow_underweight!!.visibility = View.INVISIBLE
            img_arrow_normal!!.visibility = View.INVISIBLE
            img_arrow_overweight!!.visibility = View.INVISIBLE
            img_arrow_moderately_obese!!.visibility = View.INVISIBLE
            img_arrow_severely_obese!!.visibility = View.INVISIBLE
            img_arrow_very_severely_obese!!.visibility = View.INVISIBLE
        } else if (str.equals(AppConstants.bmi_result_severely, ignoreCase = true)) {
            img_arrow_very_severely!!.visibility = View.INVISIBLE
            img_arrow_severely!!.visibility = View.VISIBLE
            img_arrow_underweight!!.visibility = View.INVISIBLE
            img_arrow_normal!!.visibility = View.INVISIBLE
            img_arrow_overweight!!.visibility = View.INVISIBLE
            img_arrow_moderately_obese!!.visibility = View.INVISIBLE
            img_arrow_severely_obese!!.visibility = View.INVISIBLE
            img_arrow_very_severely_obese!!.visibility = View.INVISIBLE
        } else if (str.equals(AppConstants.bmi_result_underweight, ignoreCase = true)) {
            img_arrow_very_severely!!.visibility = View.INVISIBLE
            img_arrow_severely!!.visibility = View.INVISIBLE
            img_arrow_underweight!!.visibility = View.VISIBLE
            img_arrow_normal!!.visibility = View.INVISIBLE
            img_arrow_overweight!!.visibility = View.INVISIBLE
            img_arrow_moderately_obese!!.visibility = View.INVISIBLE
            img_arrow_severely_obese!!.visibility = View.INVISIBLE
            img_arrow_very_severely_obese!!.visibility = View.INVISIBLE
        } else if (str.equals(AppConstants.bmi_result_normal, ignoreCase = true)) {
            img_arrow_very_severely!!.visibility = View.INVISIBLE
            img_arrow_severely!!.visibility = View.INVISIBLE
            img_arrow_underweight!!.visibility = View.INVISIBLE
            img_arrow_normal!!.visibility = View.VISIBLE
            img_arrow_overweight!!.visibility = View.INVISIBLE
            img_arrow_moderately_obese!!.visibility = View.INVISIBLE
            img_arrow_severely_obese!!.visibility = View.INVISIBLE
            img_arrow_very_severely_obese!!.visibility = View.INVISIBLE
        } else if (str.equals(AppConstants.bmi_result_overweight, ignoreCase = true)) {
            img_arrow_very_severely!!.visibility = View.INVISIBLE
            img_arrow_severely!!.visibility = View.INVISIBLE
            img_arrow_underweight!!.visibility = View.INVISIBLE
            img_arrow_normal!!.visibility = View.INVISIBLE
            img_arrow_overweight!!.visibility = View.VISIBLE
            img_arrow_moderately_obese!!.visibility = View.INVISIBLE
            img_arrow_severely_obese!!.visibility = View.INVISIBLE
            img_arrow_very_severely_obese!!.visibility = View.INVISIBLE
        } else if (str.equals(AppConstants.bmi_result_moderately_obese, ignoreCase = true)) {
            img_arrow_very_severely!!.visibility = View.INVISIBLE
            img_arrow_severely!!.visibility = View.INVISIBLE
            img_arrow_underweight!!.visibility = View.INVISIBLE
            img_arrow_normal!!.visibility = View.INVISIBLE
            img_arrow_overweight!!.visibility = View.INVISIBLE
            img_arrow_moderately_obese!!.visibility = View.VISIBLE
            img_arrow_severely_obese!!.visibility = View.INVISIBLE
            img_arrow_very_severely_obese!!.visibility = View.INVISIBLE
        } else if (str.equals(AppConstants.bmi_result_severely_obese, ignoreCase = true)) {
            img_arrow_very_severely!!.visibility = View.INVISIBLE
            img_arrow_severely!!.visibility = View.INVISIBLE
            img_arrow_underweight!!.visibility = View.INVISIBLE
            img_arrow_normal!!.visibility = View.INVISIBLE
            img_arrow_overweight!!.visibility = View.INVISIBLE
            img_arrow_moderately_obese!!.visibility = View.INVISIBLE
            img_arrow_severely_obese!!.visibility = View.VISIBLE
            img_arrow_very_severely_obese!!.visibility = View.INVISIBLE
        } else if (str.equals(AppConstants.bmi_result_very_severely_obese, ignoreCase = true)) {
            img_arrow_very_severely!!.visibility = View.INVISIBLE
            img_arrow_severely!!.visibility = View.INVISIBLE
            img_arrow_underweight!!.visibility = View.INVISIBLE
            img_arrow_normal!!.visibility = View.INVISIBLE
            img_arrow_overweight!!.visibility = View.INVISIBLE
            img_arrow_moderately_obese!!.visibility = View.INVISIBLE
            img_arrow_severely_obese!!.visibility = View.INVISIBLE
            img_arrow_very_severely_obese!!.visibility = View.VISIBLE
        }
    }

    private fun setUpActionBar() {
        (findViewById<View>(R.id.toolbar_txt_title_1) as TextView).text =
            resources.getString(R.string.lbl_bmi)
        (findViewById<View>(R.id.toolbar_txt_title_2) as TextView).text =
            resources.getString(R.string.lbl_result)
        findViewById<View>(R.id.toolbar_rel_back).setOnClickListener { this@BMIResultActivity.onBackPressed() }
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
