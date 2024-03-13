package app.android.heartrate.phoneapp.fragments

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.android.heartrate.phoneapp.R
import app.android.heartrate.phoneapp.databinding.FragmentWelcomeFragmentBinding

class WelcomeFragment : Fragment() {

    private val binding by lazy {
        FragmentWelcomeFragmentBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding.agreeButton.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment3)
        }

        binding.description.movementMethod = LinkMovementMethod.getInstance()

        return binding.root
    }
}