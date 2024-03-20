package app.android.heartrate.phoneapp.model.weight

import app.android.heartrate.phoneapp.model.classes.WeightData

data class WeightResponse
    (
                          val msg: String,
                          val code: Int,
                          val data: List<WeightData>?
)
