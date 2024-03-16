package app.android.heartrate.phoneapp.fragments.safety

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.android.heartrate.phoneapp.databinding.FragmentSafetyBinding
import app.android.heartrate.phoneapp.fragments.base.BaseFragment
import app.android.heartrate.phoneapp.fragments.safety.car_crash.CarCrashActivity
import app.android.heartrate.phoneapp.fragments.safety.emergency_call.EmergencyCallActivity
import app.android.heartrate.phoneapp.fragments.safety.fall_detection.FallDetectionActivity
import app.android.heartrate.phoneapp.fragments.safety.geo_fencing.GeoFenchingActivity
import app.android.heartrate.phoneapp.fragments.safety.home_invasion.HomeInvasionActivity
import app.android.heartrate.phoneapp.fragments.safety.location_awareness.LocationAwarenessActivity

class SafetyFragment : BaseFragment() {

    private lateinit var binding: FragmentSafetyBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSafetyBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        handleClicks()
    }

    private fun handleClicks() {
        binding.cvEmergencyCall.setOnClickListener {
            startActivity(EmergencyCallActivity::class.java)
        }

        binding.cvFallDetection.setOnClickListener {
            startActivity(FallDetectionActivity::class.java)
        }

        binding.cvLocationAwareness.setOnClickListener {
            startActivity(LocationAwarenessActivity::class.java)
        }

        binding.cvGeoFencing.setOnClickListener {
            startActivity(GeoFenchingActivity::class.java)
        }
        binding.cvHomeInvasion.setOnClickListener {
            startActivity(HomeInvasionActivity::class.java)
        }
        binding.cvCarCrash.setOnClickListener {
            startActivity(CarCrashActivity::class.java)
        }


    }

    private fun startActivity(cls: Class<*>) {
        startActivity(Intent(requireContext(), cls))
    }
}