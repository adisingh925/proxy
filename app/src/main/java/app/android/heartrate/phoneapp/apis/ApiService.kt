package app.android.heartrate.phoneapp.apis

import app.android.heartrate.phoneapp.model.GetProfileResponse
import app.android.heartrate.phoneapp.model.GetRoleResponse
import app.android.heartrate.phoneapp.model.LoginRequest
import app.android.heartrate.phoneapp.model.LoginSuccessResponse
import app.android.heartrate.phoneapp.model.SignupResponse
import app.android.heartrate.phoneapp.model.UpdateProfileRequest
import app.android.heartrate.phoneapp.model.UpdateRoleRequest
import app.android.heartrate.phoneapp.model.classes.BloodCountData
import app.android.heartrate.phoneapp.model.classes.BloodCountResponse
import app.android.heartrate.phoneapp.model.classes.UserId
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginSuccessResponse>

    @GET("getProfile")
    fun getProfile(
        @Header("Authorization") bearerToken: String,
    ): Call<GetProfileResponse>

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

    @GET("checkProfile")
    fun checkProfile(
        @Header("Authorization") bearerToken: String,
    ): Call<SignupResponse>

    @POST("saveBloodCount")
    fun postBloodCount(
        @Header("Authorization") bearerToken: String,
        @Body bloodCountData: BloodCountData
    ): Call<BloodCountResponse>

    @GET("getBloodCountsByUserId")
    fun getBloodCountsByUserId(
        @Header("Authorization") bearerToken: String,
    ): Call<BloodCountResponse>

    @POST("updateBloodCount")
    fun updateBloodCount(
        @Header("Authorization") bearerToken: String,
        @Body bloodCountData: BloodCountData
    ): Call<BloodCountData>

    @DELETE("deleteBloodCountByID/{row_id}")
    fun deleteBloodCount(
        @Header("Authorization") bearerToken: String,
        @Path("row_id") rowId: Int
    ): Call<BloodCountData>
}