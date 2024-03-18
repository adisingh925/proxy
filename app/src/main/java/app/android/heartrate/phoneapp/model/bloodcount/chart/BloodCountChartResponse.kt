package app.android.heartrate.phoneapp.model.bloodcount.chart

import app.android.heartrate.phoneapp.model.classes.BloodCountChartData

data class BloodCountChartResponse(
    val msg: String,
    val code: Int,
    val data: List<BloodCountChartData>?
)