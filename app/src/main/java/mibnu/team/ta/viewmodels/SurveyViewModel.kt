package mibnu.team.ta.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mibnu.team.ta.models.Survey
import mibnu.team.ta.utils.SingleLiveEvent
import mibnu.team.ta.webservices.ApiClient
import mibnu.team.ta.webservices.WrappedResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SurveyViewModel : ViewModel(){
    private val api = ApiClient.instanceBackend()
    private var state : SingleLiveEvent<DataState> = SingleLiveEvent()
    private var datas = MutableLiveData<Survey>()

    private fun setLoading(){state.value = DataState.IsLoading(true)}
    private fun hideLoading(){state.value = DataState.IsLoading(false)}
    private fun showToast(mesage : String){state.value = DataState.ShowToast(mesage)}


    fun surveys(token: String,nilai: Int){
        println(token)
        state.value=DataState.IsLoading(true)
        api.survey(token,nilai.toString() ).enqueue(object  : Callback<WrappedResponse<Survey>>{
            override fun onFailure(call: Call<WrappedResponse<Survey>>, t: Throwable) {
                println("onFailure : "+t.message)
                println(t.printStackTrace())
                state.value = DataState.ShowToast("onFailure :"+t.message)
            }

            override fun onResponse(call: Call<WrappedResponse<Survey>>, response: Response<WrappedResponse<Survey>>) {
                if (response.isSuccessful){
                    val body = response.body() as WrappedResponse
                    if (body.status){
                        state.value = DataState.ShowToast("berhasil menilai")
                        state.value = DataState.Success
                    }else{
                        state.value = DataState.ShowToast("gagal menambah nilai")
                    }
                }else{
                    state.value = DataState.ShowToast("gagal menambahkan")
                }
                state.value = DataState.IsLoading(false)
            }

        })
    }
    fun listenToUIState() = state
    fun listenToDatas() = datas
}




sealed class DataState{
    data class IsLoading(var state : Boolean) : DataState()
    data class ShowToast(var message : String) : DataState()
    object Reset : DataState()
    object Success: DataState()
}

