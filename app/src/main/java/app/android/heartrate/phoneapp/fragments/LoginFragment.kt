package app.android.heartrate.phoneapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import app.android.heartrate.phoneapp.R
import app.android.heartrate.phoneapp.databinding.FragmentLoginBinding
import app.android.heartrate.phoneapp.model.LoginRequest
import app.android.heartrate.phoneapp.model.LoginSuccessResponse
import app.android.heartrate.phoneapp.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginFragment : Fragment() {

    private val binding by lazy {
        FragmentLoginBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding.signupText.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment3_to_signupFragment)
        }

        binding.login.setOnClickListener {
            if (binding.emailInputField.text.toString().isEmpty()) {
                binding.emailLayout.error = "Email is required"
                return@setOnClickListener
            }

            if (binding.passwordInputField.text.toString().isEmpty()) {
                binding.passwordLayout.error = "Password is required"
                return@setOnClickListener
            }

            val call = ApiClient.apiService.login(
                LoginRequest(
                    binding.emailInputField.text.toString(),
                    binding.passwordInputField.text.toString()
                )
            )

            call.enqueue(object : Callback<LoginSuccessResponse> {
                override fun onResponse(
                    call: Call<LoginSuccessResponse>,
                    response: Response<LoginSuccessResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.d("LoginFragment", "onResponse: ${response.body()}")
                        val loginResponse = response.body()
                        if (loginResponse != null) {
                            if (loginResponse.code == 1) {
                                findNavController().navigate(R.id.action_loginFragment3_to_dashboardFragment)
                            } else {
                                Toast.makeText(context, "Invalid email or password!", Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            Toast.makeText(context, "Invalid email or password!", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(context, "Invalid email or password!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginSuccessResponse>, t: Throwable) {
                    Log.d("LoginFragment", "onFailure: ${t.message}")
                    Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                }
            })
        }


        return binding.root
    }
}