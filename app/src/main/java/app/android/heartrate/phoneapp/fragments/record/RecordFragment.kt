package app.android.heartrate.phoneapp.fragments.record

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.android.heartrate.phoneapp.activities.record.TrackerToolsActivity
import app.android.heartrate.phoneapp.databinding.FragmentRecordBinding
import app.android.heartrate.phoneapp.databinding.FragmentWellnessBinding
import app.android.heartrate.phoneapp.fragments.base.BaseFragment
import app.android.heartrate.phoneapp.fragments.medication.add.AddMedicationActivity
import app.android.heartrate.phoneapp.fragments.record.add.AddRecordActivity

class RecordFragment : BaseFragment() {

    private lateinit var binding: FragmentRecordBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecordBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }


    private fun init(){
        handleClicks()
    }

    private fun handleClicks(){
        binding.cvAddRecord.setOnClickListener {
            startActivity(AddRecordActivity::class.java)
        }

        binding.cvHealthTracker.setOnClickListener {
            startActivity(TrackerToolsActivity::class.java)
        }
    }

    private fun startActivity(cls: Class<*>) {
        startActivity(Intent(requireContext(), cls))
    }
}