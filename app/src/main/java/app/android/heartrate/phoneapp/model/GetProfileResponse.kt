package app.android.heartrate.phoneapp.model

import com.google.gson.annotations.SerializedName

data class GetProfileResponse(@SerializedName("msg")
                              val msg: String = "",
                              @SerializedName("code")
                              val code: Int = 0,
                              @SerializedName("data")
                              val data: ProfileData?)