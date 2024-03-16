package app.android.heartrate.phoneapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import app.android.heartrate.phoneapp.databinding.FragmentSignupBinding
import app.android.heartrate.phoneapp.model.LoginRequest
import app.android.heartrate.phoneapp.model.SignupResponse
import app.android.heartrate.phoneapp.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignupFragment : Fragment() {

    private val binding by lazy {
        FragmentSignupBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding.submit.setOnClickListener {
            if (binding.emailInputField.text.toString().isEmpty()) {
                binding.emailLayout.error = "Email is required"
                return@setOnClickListener
            }

            if (binding.passwordInputField.text.toString().isEmpty()) {
                binding.passwordLayout.error = "Password is required"
                return@setOnClickListener
            }

            if (binding.reTypePasswordInputField.text.toString().isEmpty()) {
                binding.reTypePasswordLayout.error = "Confirm Password is required"
                return@setOnClickListener
            }

            if (binding.passwordInputField.text.toString() != binding.reTypePasswordInputField.text.toString()) {
                binding.reTypePasswordLayout.error = "Password does not match"
                return@setOnClickListener
            }

            // Call the API to register the user
            val signup = ApiClient.apiService.signup(
                LoginRequest(
                    binding.emailInputField.text.toString(),
                    binding.passwordInputField.text.toString()
                )
            )

            signup.enqueue(object : Callback<SignupResponse> {
                override fun onResponse(
                    call: Call<SignupResponse>,
                    response: Response<SignupResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.code == 1) {
                            Toast.makeText(
                                requireContext(),
                                response.body()!!.msg,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                response.body()?.msg ?: "Signup Failed",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "A User with this email already exists!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), "Signup Failed", Toast.LENGTH_SHORT).show()
                }
            })
        }

        return binding.root
    }
}