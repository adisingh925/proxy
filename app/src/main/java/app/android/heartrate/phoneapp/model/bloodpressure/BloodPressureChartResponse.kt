package app.android.heartrate.phoneapp.model.bloodpressure

import app.android.heartrate.phoneapp.model.classes.BloodPressureChartData

data class BloodPressureChartResponse(
    val msg: String,
    val code: Int,
    val data: List<BloodPressureChartData>?
)
