package app.android.heartrate.phoneapp.fragments.support

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.android.heartrate.phoneapp.databinding.FragmentSupportBinding
import app.android.heartrate.phoneapp.databinding.FragmentWellnessBinding
import app.android.heartrate.phoneapp.fragments.base.BaseFragment
import app.android.heartrate.phoneapp.fragments.medication.add.AddMedicationActivity
import app.android.heartrate.phoneapp.fragments.support.about_us.AboutUsActivity
import app.android.heartrate.phoneapp.fragments.support.faq.FqaActivity
import app.android.heartrate.phoneapp.fragments.support.feedback.FeedbackActivity
import app.android.heartrate.phoneapp.fragments.support.help_center_screen.HelpCenterActivity
import app.android.heartrate.phoneapp.fragments.support.privacy_policy.PrivacyPolicyActivity
import app.android.heartrate.phoneapp.fragments.support.share.ShareActivity
import app.android.heartrate.phoneapp.fragments.support.terms_condition.TermsConditionsActivity

class SupportFragment : BaseFragment() {

    private lateinit var binding: FragmentSupportBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSupportBinding.inflate(layoutInflater)
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
        binding.cvFaq.setOnClickListener {
            startActivity(FqaActivity::class.java)
        }

        binding.cvHelpCenter.setOnClickListener {
            startActivity(HelpCenterActivity::class.java)
        }

        binding.cvFeedBack.setOnClickListener {
            startActivity(FeedbackActivity::class.java)
        }

        binding.cvShare.setOnClickListener {
            startActivity(ShareActivity::class.java)
        }

        binding.cvAboutUs.setOnClickListener {
            startActivity(AboutUsActivity::class.java)
        }

        binding.cvPrivacyPolicy.setOnClickListener {
            startActivity(PrivacyPolicyActivity::class.java)
        }

        binding.cvTermsConditions.setOnClickListener {
            startActivity(TermsConditionsActivity::class.java)
        }
    }

    private fun startActivity(cls: Class<*>) {
        startActivity(Intent(requireContext(), cls))
    }
}