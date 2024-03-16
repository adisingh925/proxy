package app.android.heartrate.phoneapp.fragments

import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.android.heartrate.phoneapp.R
import app.android.heartrate.phoneapp.activities.main.HomeActivity
import app.android.heartrate.phoneapp.databinding.FragmentWelcomeFragmentBinding
import app.android.heartrate.phoneapp.model.GetProfileResponse
import app.android.heartrate.phoneapp.retrofit.ApiClient
import app.android.heartrate.phoneapp.sharedpreferences.SharedPreferences
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WelcomeFragment : Fragment() {

    private val binding by lazy {
        FragmentWelcomeFragmentBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        binding.agreeButton.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment3)
        }
        binding.description.movementMethod = LinkMovementMethod.getInstance()
        checkTokenExist()
    }

    private fun checkTokenExist() {
        val token = SharedPreferences.read("token", "").toString()
        if (token.isNotEmpty()) {
            proceedToHomeActivity()
        }
    }


    private fun proceedToHomeActivity() {
//         findNavController().navigate(R.id.action_loginFragment3_to_dashboardFragment)
        val intent = Intent(requireContext(), HomeActivity::class.java)
        startActivity(intent)
    }


}