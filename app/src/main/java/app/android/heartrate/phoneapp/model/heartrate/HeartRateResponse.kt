package app.android.heartrate.phoneapp.model.heartrate

import app.android.heartrate.phoneapp.model.classes.HeartRateData

data class HeartRateResponse(
    val msg: String,
    val code: Int,
    val data: List<HeartRateData>?
)

