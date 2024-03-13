package app.android.heartrate.phoneapp.fragments.wellness.steps.subs.measure

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import app.android.heartrate.phoneapp.R
import app.android.heartrate.phoneapp.databinding.FragmentMeasureStepsBinding
import app.android.heartrate.phoneapp.fragments.base.BaseFragment
import app.android.heartrate.phoneapp.utils.CommonUtils

class MeasureStepFragment : BaseFragment() {

    private lateinit var binding: FragmentMeasureStepsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMeasureStepsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        CommonUtils.updateToolbarTitle("Measure Steps", requireActivity())
        handleClicks()
    }

    private fun handleClicks() {
        binding.btnAnalytics.setOnClickListener {
            findNavController().navigate(R.id.action_measureStepsFragment_to_analyticsStepsFragment)
        }
    }

}