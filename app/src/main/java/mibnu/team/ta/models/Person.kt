package mibnu.team.ta.models

import com.google.gson.annotations.SerializedName

data class Person(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("nik") var nik : String? = null,
    @SerializedName("kk") var kk : String? = null,
    @SerializedName("nama") var nama : String? = null,
    @SerializedName("jk") var jk : String? = null,
    @SerializedName("tmpt_lhr") var tmpt_lhr : String? = null,
    @SerializedName("tgl_lhr") var tgl_lhr : String? = null,
    @SerializedName("nama_ibu") var nama_ibu : String? = null,
    @SerializedName("nama_ayah") var nama_ayah : String? = null,
    @SerializedName("kota") var kota : String? = null,
    @SerializedName("kec") var kec : String? = null,
    @SerializedName("kel") var kel : String? = null,
    @SerializedName("alamat") var alamat : String? = null,
    @SerializedName("rt") var rt : String? = null,
    @SerializedName("rw") var rw : String? = null,
    @SerializedName("api_token") var token: String? = null,
    @SerializedName("password") var password: String? = null

)