package app.android.heartrate.phoneapp.model.cholesterol

import app.android.heartrate.phoneapp.model.classes.CholesterolData

data class CholesterolResponse(
val msg: String,
val code: Int,
val data: List<CholesterolData>?
)
