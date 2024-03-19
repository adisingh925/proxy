package app.android.heartrate.phoneapp.model.bloodsugar

import app.android.heartrate.phoneapp.model.classes.BloodSugarData

data class BloodSugarResponse(
    val msg: String,
    val code: Int,
    val data: List<BloodSugarData>?
)