package app.android.heartrate.phoneapp.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import app.android.heartrate.phoneapp.databinding.FragmentProfile1Binding
import app.android.heartrate.phoneapp.fragments.base.BaseFragment

class ProfileFragment : BaseFragment() {

    private lateinit var binding: FragmentProfile1Binding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfile1Binding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    private fun initData() {

    }
}