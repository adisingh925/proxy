package app.android.heartrate.phoneapp.retrofit

import app.android.heartrate.phoneapp.apis.ApiService
import okhttp3.EventListener
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "http://ec2-3-110-171-201.ap-south-1.compute.amazonaws.com/"


    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        val builder = OkHttpClient.Builder()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.connectTimeout(45, TimeUnit.SECONDS)
        builder.callTimeout(45, TimeUnit.SECONDS)
        builder.writeTimeout(45, TimeUnit.SECONDS)
        builder.readTimeout(45, TimeUnit.SECONDS)
        builder.addInterceptor(interceptor)
        return builder.build()
    }
}

object ApiClient {
    val apiService: ApiService by lazy {
        RetrofitClient.retrofit.create(ApiService::class.java)
    }
}