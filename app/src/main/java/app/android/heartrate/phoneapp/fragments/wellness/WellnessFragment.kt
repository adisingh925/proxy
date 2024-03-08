package app.android.heartrate.phoneapp.fragments.wellness

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.android.heartrate.phoneapp.databinding.FragmentWellnessBinding
import app.android.heartrate.phoneapp.fragments.base.BaseFragment

class WellnessFragment : BaseFragment() {

    private lateinit var binding: FragmentWellnessBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWellnessBinding.inflate(layoutInflater)
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

    }
}