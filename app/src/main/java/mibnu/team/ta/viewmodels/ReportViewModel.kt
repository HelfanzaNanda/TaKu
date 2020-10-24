package mibnu.team.ta.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mibnu.team.ta.models.Report
import mibnu.team.ta.utils.SingleLiveEvent
import mibnu.team.ta.webservices.ApiClient
import mibnu.team.ta.webservices.WrappedResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReportViewModel :ViewModel(){
    private val api = ApiClient.instanceBackend()
    private var state : SingleLiveEvent<ReportState> = SingleLiveEvent()
    private var reportdatas = MutableLiveData<List<Report>>()

    private fun setLoading(){state.value = ReportState.IsLoading(true)}
    private fun hideLoading(){state.value = ReportState.IsLoading(false)}
    private fun showToast(mesage : String){state.value = ReportState.ShowToast(mesage)}

    fun reportData(token:String, report:String){
        state.value = ReportState.IsLoading(true)
        api.reportdata(token,report).enqueue(object : Callback<WrappedResponse<Report>>{
            override fun onResponse(
                call: Call<WrappedResponse<Report>>,
                response: Response<WrappedResponse<Report>>
            ) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!) {
                        state.value = ReportState.ShowToast("Data Berhasil dikonfrimasi")
                        state.value = ReportState.Success
                    } else {
                        state.value = ReportState.ShowToast("gagal")
                    }
                }else{
                    state.value = ReportState.ShowToast("gagal")
                    println("gagal")
                }
                state.value = ReportState.IsLoading(false)
            }

            override fun onFailure(call: Call<WrappedResponse<Report>>, t: Throwable) {
                println("onFailure :"+t.message)
                println(t.printStackTrace())
                state.value = ReportState.ShowToast("onFailure :"+t.message)

            }
        })

    }
    fun listenUIState() = state
    fun listenToCurrentUser() = reportdatas
}

sealed class ReportState{
    data class IsLoading(var state : Boolean) : ReportState()
    data class ShowToast(var message : String) : ReportState()
    object Reset : ReportState()
    object Success: ReportState()
}