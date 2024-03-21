package app.android.heartrate.phoneapp.model.weight

data class WeightChartRequest(
    val user_id: Int,
    val day: Int,
    val month: Int,
    val year: Int,
    val datefrom: String,
    val dateto: String
)