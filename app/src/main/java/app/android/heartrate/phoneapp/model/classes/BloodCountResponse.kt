package app.android.heartrate.phoneapp.model.classes

data class BloodCountResponse(
    var msg: String = "",
    var code: Int,
    val data: List<BloodCountData>?
)
