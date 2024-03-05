package app.android.heartrate.phoneapp.apis

import app.android.heartrate.phoneapp.model.GetRoleResponse
import app.android.heartrate.phoneapp.model.LoginRequest
import app.android.heartrate.phoneapp.model.LoginSuccessResponse
import app.android.heartrate.phoneapp.model.SignupResponse
import app.android.heartrate.phoneapp.model.UpdateProfileRequest
import app.android.heartrate.phoneapp.model.UpdateRoleRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginSuccessResponse>

    @POST("signup")
    fun signup(@Body signupRequest: LoginRequest): Call<SignupResponse>

    @POST("updateRole")
    fun updateRole(
        @Header("Authorization") bearerToken: String,
        @Body updateRoleRequest: UpdateRoleRequest
    ): Call<SignupResponse>

    @GET("getRole")
    fun getRole(
        @Header("Authorization") bearerToken: String,
    ): Call<GetRoleResponse>

    @POST("updateProfile")
    fun updateProfile(
        @Header("Authorization") bearerToken: String,
        @Body updateProfileRequest: UpdateProfileRequest
    ): Call<SignupResponse>
}