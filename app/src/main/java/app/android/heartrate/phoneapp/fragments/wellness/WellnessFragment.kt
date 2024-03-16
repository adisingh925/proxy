package app.android.heartrate.phoneapp.fragments.wellness

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.android.heartrate.phoneapp.databinding.FragmentWellnessBinding
import app.android.heartrate.phoneapp.fragments.base.BaseFragment
import app.android.heartrate.phoneapp.fragments.wellness.cal_burns.CalBurnsActivity
import app.android.heartrate.phoneapp.fragments.wellness.fitness.FitnessActivity
import app.android.heartrate.phoneapp.fragments.wellness.sleep.SleepActivity
import app.android.heartrate.phoneapp.fragments.wellness.steps.StepsActivity
import app.android.heartrate.phoneapp.fragments.wellness.stress.StressActivity

class WellnessFragment : BaseFragment() {

    private lateinit var binding: FragmentWellnessBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWellnessBinding.inflate(layoutInflater)
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
        binding.cvFitness.setOnClickListener {
            startActivity(FitnessActivity::class.java)
        }
        binding.cvSleep.setOnClickListener {
            startActivity(SleepActivity::class.java)
        }
        binding.cvStress.setOnClickListener {
            startActivity(StressActivity::class.java)
        }
        binding.cvSteps.setOnClickListener {
            startActivity(StepsActivity::class.java)
        }
        binding.cvCalBurns.setOnClickListener {
            startActivity(CalBurnsActivity::class.java)
        }
    }

    private fun startActivity(cls: Class<*>) {
        startActivity(Intent(requireContext(), cls))
    }
}