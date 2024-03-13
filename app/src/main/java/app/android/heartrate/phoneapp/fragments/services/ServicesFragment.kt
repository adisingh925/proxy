package app.android.heartrate.phoneapp.fragments.services

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.android.heartrate.phoneapp.databinding.FragmentServicesBinding
import app.android.heartrate.phoneapp.fragments.base.BaseFragment
import app.android.heartrate.phoneapp.fragments.services.accu_pressure.AccuPressureActivity
import app.android.heartrate.phoneapp.fragments.services.care_giver.CareGiverActivity
import app.android.heartrate.phoneapp.fragments.services.chiropractor.ChiropractorActivity
import app.android.heartrate.phoneapp.fragments.services.mental_health.MentalHealthActivity
import app.android.heartrate.phoneapp.fragments.services.online_utility_payment.OnlineUtilityActivity
import app.android.heartrate.phoneapp.fragments.services.psychological_therapy.PsychologicalTherapyActivity
import app.android.heartrate.phoneapp.fragments.services.yoga_instructor.YogaInstructorActivity

class ServicesFragment : BaseFragment() {

    private lateinit var binding: FragmentServicesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentServicesBinding.inflate(layoutInflater)
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
        binding.cvCareGiver.setOnClickListener {
            startActivity(CareGiverActivity::class.java)
        }

        binding.cvAccuPressure.setOnClickListener {
            startActivity(AccuPressureActivity::class.java)
        }

        binding.cvChiroprator.setOnClickListener {
            startActivity(ChiropractorActivity::class.java)
        }

        binding.cvYogaInstructor.setOnClickListener {
            startActivity(YogaInstructorActivity::class.java)
        }

        binding.cvMentalHealth.setOnClickListener {
            startActivity(MentalHealthActivity::class.java)
        }

        binding.cvMobileOnline.setOnClickListener {
            startActivity(OnlineUtilityActivity::class.java)
        }

        binding.cvPsychological.setOnClickListener {
            startActivity(PsychologicalTherapyActivity::class.java)
        }


    }

    private fun startActivity(cls: Class<*>) {
        startActivity(Intent(requireContext(), cls))
    }


}