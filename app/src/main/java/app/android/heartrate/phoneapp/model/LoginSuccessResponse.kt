package app.android.heartrate.phoneapp.model

data class LoginSuccessResponse(
    val code: Int,
    val msg: String,
    val token: String
)