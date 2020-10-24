package mibnu.team.ta.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserCapil(
    @SerializedName("NIK") var nik : String? = null,
    @SerializedName("NO_KK") var nomorKK : String? = null,
    @SerializedName("NAMA_LGKP") var namaLengkap : String? = null,
    @SerializedName("JENIS_KLMIN") var jenisKelamin : String? = null,
    @SerializedName("TMPT_LHR") var tempatLahir : String? = null,
    @SerializedName("TGL_LHR") var tanggalLahir : String? = null,
    @SerializedName("NAMA_LGKP_IBU") var namaIbu : String? = null,
    @SerializedName("NAMA_LGKP_AYAH") var namaAyah : String? = null,
    @SerializedName("KAB_NAME") var namaKabupaten : String? = null,
    @SerializedName("KEC_NAME") var namaKecamatan : String? = null,
    @SerializedName("KEL_NAME") var namaKelurahan : String? = null,
    @SerializedName("ALAMAT") var alamat : String? = null,
    @SerializedName("NO_RT") var rw : String? = null,
    @SerializedName("NO_RW") var rt : String? = null
) : Parcelable