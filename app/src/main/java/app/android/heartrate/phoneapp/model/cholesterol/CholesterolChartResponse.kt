package app.android.heartrate.phoneapp.model.cholesterol

import app.android.heartrate.phoneapp.model.classes.CholesterolChartData

data class CholesterolChartResponse(
    val msg: String,
    val code: Int,
    val data: List<CholesterolChartData>?
)

