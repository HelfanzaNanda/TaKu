package mibnu.team.ta.utils

import android.content.Context

class Utilities {
    companion object {
        const val KTP_MENINGGAL_KEY = "ktp_meninggal"
        const val KK_MENINGGAL_KEY = "kk_meninggal"
        const val JAMKESMAS_KEY = "jamkesmas"
        const val KTP_WARIS_KEY = "ktp_waris"
        const val KK_WARIS_KEY = "kk_waris"
        const val AKTA_KEMATIAN_KEY = "akta_kematian"
        const val PERNYATAAN_WARIS_KEY = "pernyataan_ahli_waris"
        const val INTEGRITAS_WARIS_KEY = "pakta_waris"

        val listKey = listOf("ktp_meninggal","kk_meninggal", "jamkesmas", "ktp_waris", "kk_waris", "akta_kematian", "pernyataan_ahli_waris", "pakta_waris","buku_tabungan")


        fun getToken(c : Context) : String? {
            val s = c.getSharedPreferences("USER", Context.MODE_PRIVATE)
            return s?.getString("TOKEN", null)
        }

        fun setToken(context: Context, token : String){
            val t = "Bearer $token"
            val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
            pref.edit().apply {
                putString("TOKEN", t)
                apply()
            }
        }

        fun clearToken(context: Context){
            val pref = context.getSharedPreferences("USER",Context.MODE_PRIVATE)
            pref.edit().clear().apply()
        }

        fun isValidUsername(username : String?) = !username.isNullOrEmpty()
        fun isValidNik(username: String)= username.length >=16
        fun isValidPassword(password : String) = password.length >= 6
        fun isValidPasswords(password : String?) = !password.isNullOrEmpty()
        fun isValidCheckNik(username: String) =username.matches("^[3376]?[0-9]{16,16}\$".toRegex())

    }
}