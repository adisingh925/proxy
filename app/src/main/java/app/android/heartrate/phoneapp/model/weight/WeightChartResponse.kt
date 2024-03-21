package app.android.heartrate.phoneapp.model.weight

import app.android.heartrate.phoneapp.model.classes.WeightChartData

data class WeightChartResponse(
      val msg: String,
      val code: Int,
      val data: List<WeightChartData>?
)