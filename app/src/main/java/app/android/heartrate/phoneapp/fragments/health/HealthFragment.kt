package app.android.heartrate.phoneapp.fragments.health

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.android.heartrate.phoneapp.databinding.FragmentHealthBinding
import app.android.heartrate.phoneapp.databinding.FragmentWellnessBinding
import app.android.heartrate.phoneapp.fragments.base.BaseFragment
import app.android.heartrate.phoneapp.fragments.health.bp.BpActivity
import app.android.heartrate.phoneapp.fragments.health.ekg.EkgActivity
import app.android.heartrate.phoneapp.fragments.health.pulse.PulseActivity
import app.android.heartrate.phoneapp.fragments.health.sp02.Sp02Activity
import app.android.heartrate.phoneapp.fragments.health.sugar.SugarActivity
import app.android.heartrate.phoneapp.fragments.health.temp.TemperatureActivity

class HealthFragment : BaseFragment() {

    private lateinit var binding: FragmentHealthBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHealthBinding.inflate(layoutInflater)
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
        binding.cvSp02.setOnClickListener {
            startActivity(Sp02Activity::class.java)
        }
        binding.cvBp.setOnClickListener {
            startActivity(BpActivity::class.java)
        }
        binding.cvPulse.setOnClickListener {
            startActivity(PulseActivity::class.java)
        }
        binding.cvTemp.setOnClickListener {
            startActivity(TemperatureActivity::class.java)
        }
        binding.cvSugar.setOnClickListener {
            startActivity(SugarActivity::class.java)
        }
        binding.cvEkg.setOnClickListener {
            startActivity(EkgActivity::class.java)
        }

    }

    private fun startActivity(cls: Class<*>) {
        startActivity(Intent(requireContext(), cls))
    }
}