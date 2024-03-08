package app.android.heartrate.phoneapp.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import app.android.heartrate.phoneapp.HomeActivity
import app.android.heartrate.phoneapp.R
import app.android.heartrate.phoneapp.databinding.FragmentLoginBinding
import app.android.heartrate.phoneapp.model.GetRoleResponse
import app.android.heartrate.phoneapp.model.LoginRequest
import app.android.heartrate.phoneapp.model.LoginSuccessResponse
import app.android.heartrate.phoneapp.model.SignupResponse
import app.android.heartrate.phoneapp.retrofit.ApiClient
import app.android.heartrate.phoneapp.sharedpreferences.SharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern


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
            binding.emailLayout.error = null
            binding.passwordLayout.error = null

            if (binding.emailInputField.text.toString().isEmpty()) {
                binding.emailLayout.error = "Email is required"
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailInputField.text.toString())
                    .matches()
            ) {
                binding.emailLayout.error = "Invalid email"
                return@setOnClickListener
            }

            if (binding.passwordInputField.text.toString().isEmpty()) {
                binding.passwordLayout.error = "Password is required"
                return@setOnClickListener
            }

            if (binding.passwordInputField.text.toString().length < 6) {
                binding.passwordLayout.error = "Password must be at least 6 characters"
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
                    val body = response.body()
                    if (body != null) {
                        if (response.isSuccessful) {
                            Log.d("LoginFragment", "onResponse: ${response.body()}")
                            if (body.code == 1) {
                                SharedPreferences.write("token", body.token)
                                val getRoleCall = ApiClient.apiService.getRole(
                                    SharedPreferences.read("token", "").toString()
                                )

                                getRoleCall.enqueue(object : Callback<GetRoleResponse> {
                                    override fun onResponse(
                                        call: Call<GetRoleResponse>,
                                        response: Response<GetRoleResponse>
                                    ) {
                                        val body = response.body()
                                        Log.d("LoginFragment", "onResponse: ${response.body()}")
                                        if (body != null) {
                                            if (response.isSuccessful) {
                                                if (body.code == 1) {
                                                    Log.d("LoginFragment", "role success")
                                                    val profileCheck =
                                                        ApiClient.apiService.checkProfile(
                                                            SharedPreferences.read("token", "")
                                                                .toString()
                                                        )
                                                    profileCheck.enqueue(object :
                                                        Callback<SignupResponse> {
                                                        override fun onResponse(
                                                            call: Call<SignupResponse>,
                                                            response: Response<SignupResponse>
                                                        ) {
                                                            if (response.isSuccessful) {
                                                                val body = response.body()
                                                                Log.d(
                                                                    "Profile Check",
                                                                    "onResponse: ${response}"
                                                                )
                                                                if (body != null) {
                                                                    if (body.code == 1) {
                                                                        proceedToHomeActivity()
                                                                    } else {
                                                                        findNavController().navigate(
                                                                            R.id.action_loginFragment3_to_profileFragment
                                                                        )
                                                                    }
                                                                } else {
                                                                    findNavController().navigate(R.id.action_loginFragment3_to_profileFragment)
                                                                }
                                                            } else {
                                                                findNavController().navigate(R.id.action_loginFragment3_to_profileFragment)
                                                            }
                                                        }

                                                        override fun onFailure(
                                                            call: Call<SignupResponse>,
                                                            t: Throwable
                                                        ) {
                                                            findNavController().navigate(R.id.action_loginFragment3_to_profileFragment)
                                                        }
                                                    })
                                                }
                                            } else {
                                                findNavController().navigate(R.id.action_loginFragment3_to_chooseRoleFragment)
                                            }
                                        } else {
                                            findNavController().navigate(R.id.action_loginFragment3_to_chooseRoleFragment)
                                        }
                                    }

                                    override fun onFailure(
                                        call: Call<GetRoleResponse>,
                                        t: Throwable
                                    ) {
                                        findNavController().navigate(R.id.action_loginFragment3_to_chooseRoleFragment)
                                    }
                                })
                            } else {
                                Toast.makeText(
                                    context,
                                    body.msg,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(context, body.msg, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginSuccessResponse>, t: Throwable) {
                    Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                }
            })
        }

        return binding.root
    }

    private fun proceedToHomeActivity() {
//         findNavController().navigate(R.id.action_loginFragment3_to_dashboardFragment)
        val intent = Intent(requireContext(), HomeActivity::class.java)
        startActivity(intent)
    }
}