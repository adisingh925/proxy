package app.android.heartrate.phoneapp.model.bmi

import app.android.heartrate.phoneapp.model.classes.BMIChartData

data class BMIChartResponse(
    val msg: String,
    val code: Int,
    val data: List<BMIChartData>?
)