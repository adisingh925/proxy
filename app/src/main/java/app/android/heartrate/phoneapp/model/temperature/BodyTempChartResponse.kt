package app.android.heartrate.phoneapp.model.temperature

import app.android.heartrate.phoneapp.model.classes.BodyTempChartAllData

data class BodyTempChartResponse(
    val msg: String,
    val code: Int,
    val data: List<BodyTempChartAllData>?
)
