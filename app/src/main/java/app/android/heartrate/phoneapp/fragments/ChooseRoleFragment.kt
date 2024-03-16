package app.android.heartrate.phoneapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.android.heartrate.phoneapp.R
import app.android.heartrate.phoneapp.databinding.FragmentChooseRoleBinding
import app.android.heartrate.phoneapp.model.SignupResponse
import app.android.heartrate.phoneapp.model.UpdateRoleRequest
import app.android.heartrate.phoneapp.retrofit.ApiClient
import app.android.heartrate.phoneapp.sharedpreferences.SharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChooseRoleFragment : Fragment() {

    private val binding by lazy {
        FragmentChooseRoleBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding.submit.setOnClickListener {
            var id = 0

            if (binding.radioCareGiver.isChecked) {
                id = 4
            } else if (binding.radioCareReceiver.isChecked) {
                id = 2
            }

            if (id != 0) {
                val updateRole = ApiClient.apiService.updateRole(
                    SharedPreferences.read("token", "").toString(),
                    UpdateRoleRequest(
                        id.toString()
                    )
                )

                updateRole.enqueue(object : Callback<SignupResponse> {
                    override fun onResponse(
                        call: Call<SignupResponse>,
                        response: Response<SignupResponse>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                requireContext(),
                                "Role updated successfully",
                                Toast.LENGTH_SHORT
                            ).show()

                            findNavController().navigate(R.id.action_chooseRoleFragment_to_profileFragment)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Failed to update role",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                        Toast.makeText(
                            requireContext(),
                            "Failed to update role",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }
        }
        return binding.root
    }
}