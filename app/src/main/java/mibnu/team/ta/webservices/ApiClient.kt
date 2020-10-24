package mibnu.team.ta.webservices

import com.google.gson.annotations.SerializedName
import mibnu.team.ta.models.*
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

class ApiClient {
    companion object {
        const val ENDPOINT_CAPIL = "http://103.12.164.52:8185/ws_server/get_json/"
        const val ENDPOINT_BACKEND = "https://bangdu.herokuapp.com/"
        const val DEF_USER_ID = "BP4"
        const val DEF_PASSWORD = "14141414"
        private var retrofit : Retrofit? = null
        private var retrofit2 : Retrofit? = null
        private var opt = OkHttpClient.Builder().apply {
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
        }.build()

        private fun getClient() : Retrofit {
            return if(retrofit == null){
                retrofit = Retrofit.Builder().apply {
                    baseUrl(ENDPOINT_CAPIL)
                    client(opt)
                    addConverterFactory(GsonConverterFactory.create())
                }.build()
                retrofit!!
            }else{
                retrofit!!
            }
        }

        private fun getClient2() : Retrofit {
            return if(retrofit2 == null){
                retrofit2 = Retrofit.Builder().apply {
                    baseUrl(ENDPOINT_BACKEND)
                    client(opt)
                    addConverterFactory(GsonConverterFactory.create())
                }.build()
                retrofit2!!
            }else{
                retrofit2!!
            }
        }



        fun instanceCapil() = getClient().create(ApiService::class.java)
        fun instanceBackend() = getClient2().create(MyApiService::class.java)
    }
}


interface ApiService {

    @POST("bp4/nik")
    fun activate(@Query("USER_ID") userId : String,
                 @Query("PASSWORD") password : String,
                 @Query("NIK") nik : String) :
            Call<ResponseBody>
}

interface MyApiService {
    @Headers("Content-Type: application/json")
    @POST("api/waris/register")
    fun registerUser(@Body body: RequestBody): Call<WrappedResponse<User>>

    @FormUrlEncoded
    @POST("api/waris/login")
    fun loginUser(@Field("nik") nik:String, @Field("password") password:String)
        :Call<WrappedResponse<User>>

    @GET("api/waris/me")
    fun profile(@Header("Authorization") token: String) : Call<WrappedResponse<User>>


    @Multipart
    @POST("api/berkas")
    fun uploadBerkas(@Header("Authorization") token: String, @Part berkas: Array<MultipartBody.Part?>) : Call<WrappedResponse<Berkas>>

    @FormUrlEncoded
    @POST("api/waris/survey")
    fun survey(@Header("Authorization") token: String,@Field("nilai")nilai:String): Call<WrappedResponse<Survey>>

    @FormUrlEncoded
    @POST("api/waris/report")
    fun reportdata(@Header("Authorization") token: String,
    @Field("report") report:String):Call<WrappedResponse<Report>>

    @FormUrlEncoded
    @POST("api/waris/update")
    fun update(
        @Header("Authorization") token: String,
        @Field("password")password: String
    ): Call<WrappedResponse<User>>


    @GET("api/waris/tracking")
    fun tracking(@Header("Authorization") token : String):Call<WrappedResponse<Tracking>>

    @GET("api/waris/grafik")
    fun grafik():Call<WrappedResponse<Grafik>>

}


data class WrappedListResponse<T> (
    @SerializedName("message") var message : String,
    @SerializedName("status") var status : Boolean,
    @SerializedName("data") var data : List<T>
)


data class WrappedResponse<T> (
    @SerializedName("message") var message : String,
    @SerializedName("status") var status : Boolean,
    @SerializedName("data") var data : T
)