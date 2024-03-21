package app.android.heartrate.phoneapp.model.temperature

data class BodyTempChartRequest(
    val user_id: Int,
    val day: Int,
    val month: Int,
    val year: Int,
    val datefrom: String,
    val dateto: String
)