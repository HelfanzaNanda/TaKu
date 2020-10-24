package mibnu.team.ta.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mibnu.team.ta.models.Tracking
import mibnu.team.ta.utils.SingleLiveEvent
import mibnu.team.ta.webservices.ApiClient
import mibnu.team.ta.webservices.WrappedResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrackViewModel : ViewModel(){
    private val api = ApiClient.instanceBackend()
    private var state:SingleLiveEvent<TrackingState> = SingleLiveEvent()
    private var datas = MutableLiveData<Tracking>()

    private fun setLoading(){state.value = TrackingState.IsLoading(true)}
    private fun hideLoading(){state.value = TrackingState.IsLoading(false)}
    private fun showToast(mesage:String){state.value = TrackingState.ShowToast(mesage)}

    fun trackData(token : String){
        setLoading()
        api.tracking(token).enqueue(object:Callback<WrappedResponse<Tracking>>{
            override fun onFailure(call: Call<WrappedResponse<Tracking>>, t: Throwable) {
                println(t.message)
                println(t.printStackTrace())
                hideLoading()
                showToast(t.message.toString())
            }

            override fun onResponse(
                call: Call<WrappedResponse<Tracking>>,
                response: Response<WrappedResponse<Tracking>>
            ) {
                if (response.isSuccessful){
                    val b = response.body()
                    println(b.toString())
                    datas.postValue((b?.data))
                }else{
                    showToast("Kesalahan saat mengambil data")
                }
                hideLoading()
            }

        })
    }
    fun listenToUIState() = state
    fun listenToDatas()= datas
    fun listeToDataValids() = datas
}

sealed class TrackingState{
    data class  IsLoading(var state:Boolean):TrackingState()
    data class ShowToast(var message:String):TrackingState()
    object Success: TrackingState()
}