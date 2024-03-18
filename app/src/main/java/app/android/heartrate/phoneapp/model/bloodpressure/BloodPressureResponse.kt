package app.android.heartrate.phoneapp.model.bloodpressure

import app.android.heartrate.phoneapp.model.classes.BloodPressureData

data class BloodPressureResponse(
    val msg: String,
    val code: Int,
    val data: List<BloodPressureData>?
)