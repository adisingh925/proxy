package app.android.heartrate.phoneapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import app.android.heartrate.phoneapp.R
import app.android.heartrate.phoneapp.databinding.FragmentProfileBinding
import app.android.heartrate.phoneapp.model.SignupResponse
import app.android.heartrate.phoneapp.model.UpdateProfileRequest
import app.android.heartrate.phoneapp.retrofit.ApiClient
import app.android.heartrate.phoneapp.sharedpreferences.SharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileFragment : Fragment() {

    private val binding by lazy{
        FragmentProfileBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding.submit.setOnClickListener {

            if(binding.nameEditText.text.toString().isEmpty()){
                binding.nameEditText.error = "Name is required"
                return@setOnClickListener
            }

            if(binding.birthdateEditText.text.toString().isEmpty()){
                binding.birthdateEditText.error = "Birthdate is required"
                return@setOnClickListener
            }

            if(binding.genderEditText.text.toString().isEmpty()) {
                binding.genderEditText.error = "Gender is Required"
                return@setOnClickListener
            }

            if(binding.phoneEditText.text.toString().isEmpty()){
                binding.phoneEditText.error = "Phone Number is required"
                return@setOnClickListener
            }

            val updateProfile = ApiClient.apiService.updateProfile(
                SharedPreferences.read("token", "").toString(),
                UpdateProfileRequest(
                    binding.nameEditText.text.toString(),
                    "",
                    binding.birthdateEditText.text.toString(),
                    binding.genderEditText.text.toString(),
                    binding.phoneEditText.text.toString()
                )
            )

            updateProfile.enqueue(object : Callback<SignupResponse> {
                override fun onResponse(
                    call: Call<SignupResponse>,
                    response: Response<SignupResponse>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            requireContext(),
                            "Profile updated successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Profile update failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                    Toast.makeText(
                        requireContext(),
                        "Profile update failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        }
        return binding.root
    }
}