package mibnu.team.ta.models

import com.google.gson.annotations.SerializedName

class SingleResult (
    @SerializedName("RESPON") var respon : String? = null
)

class MultipleResult (
    @SerializedName("NO_KK") var nomorKK : String? = null
)
