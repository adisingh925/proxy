package app.android.heartrate.phoneapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.android.heartrate.phoneapp.R
import app.android.heartrate.phoneapp.databinding.FragmentDashboardBinding


class DashboardFragment : Fragment() {

    private val binding by lazy {
        FragmentDashboardBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding.heartbeatCard.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_pairingFragment)
        }

        return binding.root
    }
}