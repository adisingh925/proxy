package app.android.heartrate.phoneapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import app.android.heartrate.phoneapp.R
import app.android.heartrate.phoneapp.databinding.FragmentProfileBinding
import app.android.heartrate.phoneapp.model.SignupResponse
import app.android.heartrate.phoneapp.model.UpdateProfileRequest
import app.android.heartrate.phoneapp.retrofit.ApiClient
import app.android.heartrate.phoneapp.sharedpreferences.SharedPreferences
import com.google.android.material.textfield.MaterialAutoCompleteTextView
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

            if(binding.firstNameInputField.text.toString().isEmpty()){
                binding.firstNameInputField.error = "FirstName is required"
                return@setOnClickListener
            }

            if(binding.lastNameInputField.text.toString().isEmpty()){
                binding.lastNameInputField.error = "LastName is required"
                return@setOnClickListener
            }

            if(binding.dobInputField.text.toString().isEmpty()){
                binding.dobInputField.error = "Birthdate is required"
                return@setOnClickListener
            }

            if(binding.genderInputField.text.toString().isEmpty()) {
                binding.genderInputField.error = "Gender is Required"
                return@setOnClickListener
            }

            if(binding.phoneInputField.text.toString().isEmpty()){
                binding.phoneInputField.error = "Phone Number is required"
                return@setOnClickListener
            }

            val updateProfile = ApiClient.apiService.updateProfile(
                SharedPreferences.read("token", "").toString(),
                UpdateProfileRequest(
                    binding.firstNameInputField.text.toString(),
                    binding.lastNameInputField.text.toString(),
                    binding.dobInputField.text.toString(),
                    binding.genderInputField.text.toString(),
                    binding.phoneInputField.text.toString()
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