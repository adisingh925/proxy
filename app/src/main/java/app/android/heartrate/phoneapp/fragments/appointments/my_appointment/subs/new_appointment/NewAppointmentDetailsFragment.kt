package app.android.heartrate.phoneapp.fragments.appointments.my_appointment.subs.new_appointment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.android.heartrate.phoneapp.databinding.FragmentAnalyticsFitnessBinding
import app.android.heartrate.phoneapp.databinding.FragmentNewAppointmentBinding
import app.android.heartrate.phoneapp.fragments.base.BaseFragment
import app.android.heartrate.phoneapp.utils.CommonUtils

class NewAppointmentDetailsFragment : BaseFragment() {
    private lateinit var binding: FragmentNewAppointmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewAppointmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {

    }

}