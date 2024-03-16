package app.android.heartrate.phoneapp.model

import com.google.gson.annotations.SerializedName

data class ProfileData(
    @SerializedName("birthday")
    val birthday: String = "",
    @SerializedName("users_id")
    val userId: Int,
    @SerializedName("gender")
    val gender: String = "",
    @SerializedName("phone")
    val phone: String = "",
    @SerializedName("last_name")
    val lastName: String = "",
    @SerializedName("first_name")
    val firstName: String = "",
    @SerializedName("email")
    val email: String = "",
    @SerializedName("username")
    val username: String = ""
)