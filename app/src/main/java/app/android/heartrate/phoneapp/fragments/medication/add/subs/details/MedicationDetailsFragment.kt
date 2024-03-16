package app.android.heartrate.phoneapp.fragments.medication.add.subs.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.android.heartrate.phoneapp.databinding.FragmentMedicationDetailsBinding
import app.android.heartrate.phoneapp.databinding.FragmentNewAppointmentBinding
import app.android.heartrate.phoneapp.fragments.base.BaseFragment

class MedicationDetailsFragment : BaseFragment() {

    private lateinit var binding: FragmentMedicationDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMedicationDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {

    }
}