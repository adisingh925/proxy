package app.android.heartrate.phoneapp.fragments.wellness.steps.subs.analytics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.android.heartrate.phoneapp.databinding.FragmentAnalyticsStepsBinding
import app.android.heartrate.phoneapp.databinding.FragmentAnalyticsStressBinding
import app.android.heartrate.phoneapp.fragments.base.BaseFragment
import app.android.heartrate.phoneapp.utils.CommonUtils

class AnalyticsStepFragment : BaseFragment() {


    private lateinit var binding: FragmentAnalyticsStepsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnalyticsStepsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        CommonUtils.updateToolbarTitle("Steps Analytics", requireActivity())

    }


}