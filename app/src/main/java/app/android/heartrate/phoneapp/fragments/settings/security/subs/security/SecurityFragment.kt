package app.android.heartrate.phoneapp.fragments.settings.security.subs.security

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import app.android.heartrate.phoneapp.R
import app.android.heartrate.phoneapp.databinding.FragmentSettingSecurityBinding
import app.android.heartrate.phoneapp.fragments.base.BaseFragment
import app.android.heartrate.phoneapp.utils.CommonUtils

class SecurityFragment : BaseFragment() {

    private lateinit var binding: FragmentSettingSecurityBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingSecurityBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        CommonUtils.updateToolbarTitle("Security Settings", requireActivity())

        handleClicks()
    }

    private fun handleClicks() {
        binding.cvChangePassword.setOnClickListener {
            findNavController().navigate(R.id.action_securityFragment_to_changePasswordFragment)

        }

        binding.cvEmergencyContact.setOnClickListener {
            findNavController().navigate(R.id.action_securityFragment_to_setEmergencyContactFragment)

        }
    }
}