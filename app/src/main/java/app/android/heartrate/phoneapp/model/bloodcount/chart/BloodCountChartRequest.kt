package app.android.heartrate.phoneapp.model.bloodcount.chart

data class BloodCountChartRequest(
    val user_id: Int,
    val day: Int,
    val month: Int,
    val year: Int,
    val datefrom: String,
    val dateto: String
)