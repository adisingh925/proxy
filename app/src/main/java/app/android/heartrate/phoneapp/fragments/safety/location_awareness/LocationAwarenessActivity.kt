package app.android.heartrate.phoneapp.fragments.safety.location_awareness

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import app.android.heartrate.phoneapp.R
import app.android.heartrate.phoneapp.databinding.ActivityLocationAwarenessBinding
import app.android.heartrate.phoneapp.fragments.safety.home_invasion.HomeInvasionActivity

class LocationAwarenessActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLocationAwarenessBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationAwarenessBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}