package app.android.heartrate.phoneapp.model.heartrate

import app.android.heartrate.phoneapp.model.classes.HeartRateChartData

data class HeartRateChartResponse(
    val msg: String,
    val code: Int,
    val data: List<HeartRateChartData>?
)
