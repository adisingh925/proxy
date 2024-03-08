package app.android.heartrate.phoneapp.fragments.appointments.my_appointment

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import app.android.heartrate.phoneapp.R
import app.android.heartrate.phoneapp.databinding.ActivityMyAppointmentBinding
import app.android.heartrate.phoneapp.databinding.ActivityTemperatureBinding

class MyAppointmentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyAppointmentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyAppointmentBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}