package app.android.heartrate.phoneapp.model.temperature

import app.android.heartrate.phoneapp.model.classes.BodyTempData

data class BodyTempResponse(
       val msg: String,
       val code: Int,
       val data: List<BodyTempData>?
)