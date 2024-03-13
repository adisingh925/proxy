package app.android.heartrate.phoneapp.fragments.health.temp.subs.measure

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import app.android.heartrate.phoneapp.R
import app.android.heartrate.phoneapp.databinding.FragmentMeasureTempBinding
import app.android.heartrate.phoneapp.fragments.base.BaseFragment
import app.android.heartrate.phoneapp.utils.CommonUtils

class MeasureTempFragment : BaseFragment() {

    private lateinit var binding: FragmentMeasureTempBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMeasureTempBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        CommonUtils.updateToolbarTitle("Measure Temperature", requireActivity())
        handleClicks()
    }

    private fun handleClicks() {
        binding.btnAnalytics.setOnClickListener {
            findNavController().navigate(R.id.action_measureTempFragment_to_analyticsTempFragment)
        }
    }


}