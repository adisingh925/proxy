package app.android.heartrate.phoneapp.apis

import app.android.heartrate.phoneapp.model.GetProfileResponse
import app.android.heartrate.phoneapp.model.GetRoleResponse
import app.android.heartrate.phoneapp.model.LoginRequest
import app.android.heartrate.phoneapp.model.LoginSuccessResponse
import app.android.heartrate.phoneapp.model.SignupResponse
import app.android.heartrate.phoneapp.model.UpdateProfileRequest
import app.android.heartrate.phoneapp.model.UpdateRoleRequest

import app.android.heartrate.phoneapp.model.classes.BloodCountResponse
import app.android.heartrate.phoneapp.model.classes.BloodCountData
import app.android.heartrate.phoneapp.model.bloodcount.chart.BloodCountChartRequest
import app.android.heartrate.phoneapp.model.bloodcount.chart.BloodCountChartResponse

import app.android.heartrate.phoneapp.model.bloodpressure.BloodPressureResponse
import app.android.heartrate.phoneapp.model.classes.BloodPressureData
import app.android.heartrate.phoneapp.model.bloodpressure.BloodPressureChartRequest
import app.android.heartrate.phoneapp.model.bloodpressure.BloodPressureChartResponse

import app.android.heartrate.phoneapp.model.bloodsugar.BloodSugarResponse
import app.android.heartrate.phoneapp.model.classes.BloodSugarData
import app.android.heartrate.phoneapp.model.bloodsugar.BloodSugarChartRequest
import app.android.heartrate.phoneapp.model.bloodsugar.BloodSugarChartResponse

import app.android.heartrate.phoneapp.model.cholesterol.CholesterolResponse
import app.android.heartrate.phoneapp.model.classes.CholesterolData
import app.android.heartrate.phoneapp.model.cholesterol.CholesterolChartRequest
import app.android.heartrate.phoneapp.model.cholesterol.CholesterolChartResponse

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

    @POST("getBloodCountChartAllData")
    fun getBloodCountChartAllData(
        @Header("Authorization") bearerToken: String,
        @Body bloodCountChartRequest: BloodCountChartRequest
    ): Call<BloodCountChartResponse>


    @POST("getBloodCountChartTodayData")
    fun getBloodCountChartTodayData(
        @Header("Authorization") bearerToken: String,
        @Body bloodCountChartRequest: BloodCountChartRequest
    ): Call<BloodCountChartResponse>


    @POST("getBloodCountChartMonthlyData")
    fun getBloodCountChartMonthlyData(
        @Header("Authorization") bearerToken: String,
        @Body bloodCountChartRequest: BloodCountChartRequest
    ): Call<BloodCountChartResponse>


    @POST("getBloodCountChartYearlyData")
    fun getBloodCountChartYearlyData(
        @Header("Authorization") bearerToken: String,
        @Body bloodCountChartRequest: BloodCountChartRequest
    ): Call<BloodCountChartResponse>


    @POST("getBloodCountChartCustomData")
    fun getBloodCountChartCustomData(
        @Header("Authorization") bearerToken: String,
        @Body bloodCountChartRequest: BloodCountChartRequest
    ): Call<BloodCountChartResponse>


    @POST("saveBloodPressure")
    fun saveBloodPressure(
        @Header("Authorization") bearerToken: String,
        @Body bloodPressureData: BloodPressureData
    ): Call<BloodPressureResponse>


    @GET("getBloodPressureDataByUserId")
    fun getBloodPressureDataByUserId(
        @Header("Authorization") bearerToken: String
    ): Call<BloodPressureResponse>

    @POST("updateBloodPressureData")
    fun updateBloodPressure(
        @Header("Authorization") bearerToken: String,
        @Body bloodPressureData: BloodPressureData
    ): Call<BloodPressureData>

    @DELETE("deleteBloodPressureByID/{row_id}")
    fun deleteBloodPressure(
        @Header("Authorization") bearerToken: String,
        @Path("row_id") rowId: Int
    ): Call<BloodPressureData>

    @POST("getBloodPressureChartAllData")
    fun getBloodPressureChartAllData(
        @Header("Authorization") bearerToken: String,
        @Body bloodPressureChartRequest: BloodCountChartRequest
    ): Call<BloodPressureChartResponse>


    @POST("getBloodPressureChartTodayData")
    fun getBloodPressureChartTodayData(
        @Header("Authorization") bearerToken: String,
        @Body bloodPressureChartRequest: BloodCountChartRequest
    ): Call<BloodPressureChartResponse>


    @POST("getBloodPressureChartMonthlyData")
    fun getBloodPressureChartMonthlyData(
        @Header("Authorization") bearerToken: String,
        @Body bloodPressureChartRequest: BloodCountChartRequest
    ): Call<BloodPressureChartResponse>


    @POST("getBloodPressureChartYearlyData")
    fun getBloodPressureChartYearlyData(
        @Header("Authorization") bearerToken: String,
        @Body bloodPressureChartRequest: BloodCountChartRequest
    ): Call<BloodPressureChartResponse>


    @POST("getBloodPressureChartCustomData")
    fun getBloodPressureChartCustomData(
        @Header("Authorization") bearerToken: String,
        @Body bloodPressureChartRequest: BloodCountChartRequest
    ): Call<BloodPressureChartResponse>

    @POST("saveBloodSugar")
    fun postBloodSugar(
        @Header("Authorization") bearerToken: String,
        @Body bloodSugarData: BloodSugarData
    ): Call<BloodSugarResponse>

    @GET("getBloodSugarsByUserId")
    fun getBloodSugarsByUserId(
        @Header("Authorization") bearerToken: String,
    ): Call<BloodSugarResponse>

    @POST("updateBloodSugar")
    fun updateBloodSugar(
        @Header("Authorization") bearerToken: String,
        @Body bloodSugarData: BloodSugarData
    ): Call<BloodSugarData>

    @DELETE("deleteBloodSugarByID/{row_id}")
    fun deleteBloodSugar(
        @Header("Authorization") bearerToken: String,
        @Path("row_id") rowId: Int
    ): Call<BloodSugarData>

    @POST("getBloodSugarChartAllData")
    fun getBloodSugarChartAllData(
        @Header("Authorization") bearerToken: String,
        @Body bloodSugarChartRequest: BloodCountChartRequest
    ): Call<BloodSugarChartResponse>


    @POST("getBloodSugarChartTodayData")
    fun getBloodSugarChartTodayData(
        @Header("Authorization") bearerToken: String,
        @Body bloodSugarChartRequest: BloodCountChartRequest
    ): Call<BloodSugarChartResponse>


    @POST("getBloodSugarChartMonthlyData")
    fun getBloodSugarChartMonthlyData(
        @Header("Authorization") bearerToken: String,
        @Body bloodSugarChartRequest: BloodCountChartRequest
    ): Call<BloodSugarChartResponse>


    @POST("getBloodSugarChartYearlyData")
    fun getBloodSugarChartYearlyData(
        @Header("Authorization") bearerToken: String,
        @Body bloodSugarChartRequest: BloodCountChartRequest
    ): Call<BloodSugarChartResponse>


    @POST("getBloodSugarChartCustomData")
    fun getBloodSugarChartCustomData(
        @Header("Authorization") bearerToken: String,
        @Body bloodSugarChartRequest: BloodCountChartRequest
    ): Call<BloodSugarChartResponse>

    @POST("saveCholesterol")
    fun postCholesterol(
        @Header("Authorization") bearerToken: String,
        @Body bloodSugarData: CholesterolData
    ): Call<CholesterolResponse>

    @GET("getCholesterolsByUserId")
    fun getCholesterolsByUserId(
        @Header("Authorization") bearerToken: String,
    ): Call<CholesterolResponse>

    @POST("updateCholesterol")
    fun updateCholesterol(
        @Header("Authorization") bearerToken: String,
        @Body bloodSugarData: CholesterolData
    ): Call<CholesterolData>

    @DELETE("deleteCholesterolByID/{row_id}")
    fun deleteCholesterol(
        @Header("Authorization") bearerToken: String,
        @Path("row_id") rowId: Int
    ): Call<CholesterolData>

    @POST("getCholesterolChartAllData")
    fun getCholesterolChartAllData(
        @Header("Authorization") bearerToken: String,
        @Body bloodSugarChartRequest: CholesterolChartRequest
    ): Call<CholesterolChartResponse>


    @POST("getCholesterolChartTodayData")
    fun getCholesterolChartTodayData(
        @Header("Authorization") bearerToken: String,
        @Body bloodSugarChartRequest: CholesterolChartRequest
    ): Call<CholesterolChartResponse>


    @POST("getCholesterolChartMonthlyData")
    fun getCholesterolChartMonthlyData(
        @Header("Authorization") bearerToken: String,
        @Body bloodSugarChartRequest: CholesterolChartRequest
    ): Call<CholesterolChartResponse>


    @POST("getCholesterolChartYearlyData")
    fun getCholesterolChartYearlyData(
        @Header("Authorization") bearerToken: String,
        @Body bloodSugarChartRequest: CholesterolChartRequest
    ): Call<CholesterolChartResponse>


    @POST("getCholesterolChartCustomData")
    fun getCholesterolChartCustomData(
        @Header("Authorization") bearerToken: String,
        @Body bloodSugarChartRequest: CholesterolChartRequest
    ): Call<CholesterolChartResponse>

}