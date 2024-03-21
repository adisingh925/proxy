package app.android.heartrate.phoneapp.model.bmi

import app.android.heartrate.phoneapp.model.classes.BMIData

data class BMIResponse(
    val msg: String,
    val code: Int,
    val data: List<BMIData>?
)
