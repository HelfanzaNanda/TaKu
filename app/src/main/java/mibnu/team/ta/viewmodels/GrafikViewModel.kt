package mibnu.team.ta.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mibnu.team.ta.models.Grafik
import mibnu.team.ta.utils.SingleLiveEvent
import mibnu.team.ta.webservices.ApiClient
import mibnu.team.ta.webservices.WrappedListResponse
import mibnu.team.ta.webservices.WrappedResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GrafikViewModel :ViewModel(){
    private var api= ApiClient.instanceBackend()
    private  var state : SingleLiveEvent<GrafitState> = SingleLiveEvent()
    private var kecamatanInfo = MutableLiveData<Grafik>()
    private fun setLoading() { state.value = GrafitState.IsLoading(true) }
    private fun hideLoading() { state.value = GrafitState.IsLoading(false) }
    private fun showToast(mesage : String) { state.value = GrafitState.ShowToast(mesage) }

    fun fetchKecamatanInfo(){
        api.grafik().enqueue(object :Callback<WrappedResponse<Grafik>>{
            override fun onFailure(call: Call<WrappedResponse<Grafik>>, t: Throwable) {
                println(t.printStackTrace())
                println(t.message)
                hideLoading()
                showToast(t.message.toString())
            }

            override fun onResponse(
                call: Call<WrappedResponse<Grafik>>,
                response: Response<WrappedResponse<Grafik>>
            ) {
                if (response.isSuccessful){
                    val b = response.body()
                    kecamatanInfo.postValue(b?.data)
                }else{
                    showToast("Kesalahan saat mengambil data")
                }
                hideLoading()
            }

        })
    }
    fun listenToKecamatanInfo()=kecamatanInfo
}

sealed class GrafitState {
    data class IsLoading(var state : Boolean) : GrafitState()
    data class ShowToast(var message : String) : GrafitState()
}