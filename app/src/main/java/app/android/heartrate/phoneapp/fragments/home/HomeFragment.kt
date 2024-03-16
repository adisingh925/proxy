package app.android.heartrate.phoneapp.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import app.android.heartrate.phoneapp.databinding.FragmentHomeBinding
import app.android.heartrate.phoneapp.model.GetProfileResponse
import app.android.heartrate.phoneapp.retrofit.ApiClient
import app.android.heartrate.phoneapp.sharedpreferences.SharedPreferences
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getProfile()
    }

    private fun getProfile() {
        val getUserProfile = ApiClient.apiService.getProfile(
            SharedPreferences.read("token", "")
                .toString()
        )

        getUserProfile.enqueue(object : Callback<GetProfileResponse> {
            override fun onResponse(
                call: Call<GetProfileResponse>,
                response: Response<GetProfileResponse>
            ) {
                if (response.isSuccessful) {
                    val getProfileResponse = response.body()
                    if (getProfileResponse?.data != null) {
                        val profileData = getProfileResponse.data
                        val sharedPreferencesUtils = SharedPreferences
                        sharedPreferencesUtils.setUserProfile(profileData)
                        sharedPreferencesUtils.setUserId(profileData.userId)
                        sharedPreferencesUtils.setUserName(profileData.firstName + " " + profileData.lastName)
                        sharedPreferencesUtils.setUserEmail(profileData.email)
                    }
                }
            }

            override fun onFailure(call: Call<GetProfileResponse>, t: Throwable) {
            }

        })

    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


}