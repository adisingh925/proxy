package app.android.heartrate.phoneapp.model.bloodsugar

import app.android.heartrate.phoneapp.model.classes.BloodSugarChartData

data class BloodSugarChartResponse(
    val msg: String,
    val code: Int,
    val data: List<BloodSugarChartData>?
)

