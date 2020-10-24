package mibnu.team.ta.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import mibnu.team.ta.models.Berkas
import mibnu.team.ta.services.UploadWorker
import mibnu.team.ta.utils.SingleLiveEvent
import mibnu.team.ta.webservices.ApiClient
import mibnu.team.ta.webservices.WrappedResponse
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class BerkasViewModel : ViewModel(){
    private val api = ApiClient.instanceBackend()
    private var state : SingleLiveEvent<BerkasState> = SingleLiveEvent()
    private var berkas = MutableLiveData<MutableMap<String, String>>()

    init {
        berkas.value = mutableMapOf()
    }

    private fun setLoading() { state.value = BerkasState.IsLoading(true) }
    private fun hideLoading() { state.value = BerkasState.IsLoading(false) }

    fun addBerkas(key : String, v : String){
        berkas.value!!.put(key, v)
    }


    fun upload(token: String){
        setLoading()
        val multipartTypedOutput = arrayOfNulls<MultipartBody.Part>(berkas.value!!.size)
        var i = 0
        berkas.value!!.forEach{ (key, value) ->
            val file = File(value)
            val body: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
            multipartTypedOutput[i] = MultipartBody.Part.createFormData(key, file.name, body)
            i++
        }
        api.uploadBerkas(token, multipartTypedOutput).enqueue(object : Callback<WrappedResponse<Berkas>>{
            override fun onFailure(call: Call<WrappedResponse<Berkas>>, t: Throwable) {
                println(t.message)
                hideLoading()
                state.value = BerkasState.ShowToast(t.message.toString())
            }

            override fun onResponse(call: Call<WrappedResponse<Berkas>>, response: Response<WrappedResponse<Berkas>>) {
                if (response.isSuccessful){
                    val b = response.body()
                    b?.let {
                        if(it.status){
                            state.value = BerkasState.Success
                            state.value = BerkasState.ShowToast("Berkas berhasil diunggah")
                        }else{
                            state.value = BerkasState.ShowAlert("Gagal saat mengupload berkas. Coba lagi nanti")
                        }
                    }
                }else{
                    println(response.body())
                    println(response.code())
                    println(response.message())
                    state.value = BerkasState.ShowAlert("Gagal mengupload berkas")
                }
                hideLoading()
            }
        })
    }

    fun validate() : Boolean{
        if (berkas.value?.size != 9){
            return false
        }
        var b = true
        berkas.value!!.forEach { (_, v) ->
            if(v.isEmpty()){
                b = false
            }
        }
        return b
    }

    fun listenToState() = state
    fun listenToBerkas() = berkas
}

sealed class BerkasState {
    data class IsLoading(var state : Boolean) : BerkasState()
    data class ShowToast(var message : String) : BerkasState()
    object Success : BerkasState()
    data class ShowAlert(var message: String) : BerkasState()
}