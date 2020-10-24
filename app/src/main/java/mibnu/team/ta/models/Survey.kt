package mibnu.team.ta.models

import com.google.gson.annotations.SerializedName

data class Survey(
    @SerializedName("nilai") var nilai:String? = null,
    @SerializedName("responder") var waris : Responder? = null
)