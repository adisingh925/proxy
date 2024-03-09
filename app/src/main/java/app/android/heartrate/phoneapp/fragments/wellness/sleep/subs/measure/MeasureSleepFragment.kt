package app.android.heartrate.phoneapp.fragments.wellness.sleep.subs.measure

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import app.android.heartrate.phoneapp.R
import app.android.heartrate.phoneapp.databinding.FragmentMeasureFitnessBinding
import app.android.heartrate.phoneapp.databinding.FragmentMeasureSleepBinding
import app.android.heartrate.phoneapp.fragments.base.BaseFragment
import app.android.heartrate.phoneapp.utils.CommonUtils

class MeasureSleepFragment : BaseFragment() {

    private lateinit var binding: FragmentMeasureSleepBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMeasureSleepBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        CommonUtils.updateToolbarTitle("Measure Sleep", requireActivity())
        handleClicks()
    }

    private fun handleClicks() {
        binding.btnAnalytics.setOnClickListener {
            findNavController().navigate(R.id.action_measureSleepFragment_to_analyticsSleepFragment)
        }
    }

}