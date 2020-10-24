package mibnu.team.ta.models

import com.google.gson.annotations.SerializedName

data class Grafik(
    @SerializedName("TEGAL SELATAN") var  tegalSelatan:Int=0,
    @SerializedName("TEGAL BARAT") var tegalBarat:Int=0,
    @SerializedName("TEGAL TIMUR") var tegalTimur:Int=0,
    @SerializedName("MARGADANA") var margadana:Int=0
)