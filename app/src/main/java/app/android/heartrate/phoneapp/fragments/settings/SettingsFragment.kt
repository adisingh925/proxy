package app.android.heartrate.phoneapp.fragments.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.android.heartrate.phoneapp.databinding.FragmentSettingsBinding
import app.android.heartrate.phoneapp.databinding.FragmentWellnessBinding
import app.android.heartrate.phoneapp.fragments.base.BaseFragment
import app.android.heartrate.phoneapp.fragments.medication.add.AddMedicationActivity
import app.android.heartrate.phoneapp.fragments.settings.link_family.LinkFamilyActivity
import app.android.heartrate.phoneapp.fragments.settings.logout.LogoutActivity
import app.android.heartrate.phoneapp.fragments.settings.preferences.PreferencesActivity
import app.android.heartrate.phoneapp.fragments.settings.security.SecurityActivity

class SettingsFragment : BaseFragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
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
        binding.cvPreferences.setOnClickListener {
            startActivity(PreferencesActivity::class.java)
        }

        binding.cvLinkFamily.setOnClickListener {
            startActivity(LinkFamilyActivity::class.java)
        }

        binding.cvSecurity.setOnClickListener {
            startActivity(SecurityActivity::class.java)
        }

        binding.cvLooout.setOnClickListener {
            startActivity(LogoutActivity::class.java)
        }
    }

    private fun startActivity(cls: Class<*>) {
        startActivity(Intent(requireContext(), cls))
    }
}