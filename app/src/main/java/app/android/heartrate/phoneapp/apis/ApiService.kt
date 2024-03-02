package app.android.heartrate.phoneapp.apis

import app.android.heartrate.phoneapp.model.LoginRequest
import app.android.heartrate.phoneapp.model.LoginSuccessResponse
import app.android.heartrate.phoneapp.model.SignupResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginSuccessResponse>

    @POST("signup")
    fun signup(@Body signupRequest: LoginRequest): Call<SignupResponse>
}