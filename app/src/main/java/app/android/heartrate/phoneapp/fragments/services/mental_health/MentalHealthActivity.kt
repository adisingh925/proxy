package app.android.heartrate.phoneapp.fragments.services.mental_health

import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import app.android.heartrate.phoneapp.R
import app.android.heartrate.phoneapp.databinding.ActivityMentalHealthBinding
import app.android.heartrate.phoneapp.fragments.base.BaseActivity
import app.android.heartrate.phoneapp.utils.CommonUtils

class MentalHealthActivity : BaseActivity() {
    private lateinit var binding: ActivityMentalHealthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMentalHealthBinding.inflate(layoutInflater)
//        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()
    }


    private fun init() {
        CommonUtils.setupActionBar(binding.toolbar, this, "Mentor Health", true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Handle the back button click, e.g., go back or finish the activity
                onBackPressed()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }
}