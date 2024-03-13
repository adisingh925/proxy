package app.android.heartrate.phoneapp.fragments.wellness.cal_burns.subs.measure

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import app.android.heartrate.phoneapp.R
import app.android.heartrate.phoneapp.databinding.FragmentMeasureCalsBinding
import app.android.heartrate.phoneapp.fragments.base.BaseFragment
import app.android.heartrate.phoneapp.utils.CommonUtils

class MeasureCalBurnsFragment : BaseFragment() {

    private lateinit var binding: FragmentMeasureCalsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMeasureCalsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        CommonUtils.updateToolbarTitle("Measure Cal. Burned", requireActivity())
        handleClicks()
    }

    private fun handleClicks() {
        binding.btnAnalytics.setOnClickListener {
            findNavController().navigate(R.id.action_measureCalsFragment_to_analyticsCalsFragment)
        }
    }

}