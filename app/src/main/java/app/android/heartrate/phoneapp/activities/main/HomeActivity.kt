package app.android.heartrate.phoneapp.activities.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import app.android.heartrate.phoneapp.R
import app.android.heartrate.phoneapp.databinding.ActivityHomeBinding
import app.android.heartrate.phoneapp.fragments.profile.ProfileActivity
import app.android.heartrate.phoneapp.model.GetProfileResponse
import app.android.heartrate.phoneapp.model.ProfileData
import app.android.heartrate.phoneapp.retrofit.ApiClient
import app.android.heartrate.phoneapp.sharedpreferences.SharedPreferences
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var header: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)
        drawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_wellness,
                R.id.nav_health,
                R.id.nav_medication,
                R.id.nav_record,
                R.id.nav_appointments,
                R.id.nav_safety,
                R.id.nav_services,
                R.id.nav_settings,
                R.id.nav_support
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        handleProfileClicks()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    private fun handleProfileClicks() {
        header = binding.navView.getHeaderView(0)
        val editProfile = header.findViewById<TextView>(R.id.tvEditProfile)
        editProfile.setOnClickListener {
            drawerLayout.closeDrawers()
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        getProfile()

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
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
                        setProfileData(profileData)
                    }
                }
            }

            override fun onFailure(call: Call<GetProfileResponse>, t: Throwable) {
            }

        })

    }


    private fun setProfileData(profileData: ProfileData) {
        val userName = header.findViewById<TextView>(R.id.tvUserName)
        val userEmail = header.findViewById<TextView>(R.id.tvUserEmail)
        profileData.let {
            userName.text = (it.firstName + " " + it?.lastName)
            userEmail.text = it.email
        }
    }

}