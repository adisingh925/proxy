package app.android.heartrate.phoneapp.model

data class UpdateProfileRequest(
    val firstName : String,
    val lastName : String,
    val dob : String,
    val gender : String,
    val mobileNumber : String
)
