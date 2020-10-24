package mibnu.team.ta.models

import com.google.gson.annotations.SerializedName

data class Responder(
    @SerializedName("nik") var nik : String? = null,
    @SerializedName("kk") var kk : String? = null,
    @SerializedName("nama") var nama:String?=null
)