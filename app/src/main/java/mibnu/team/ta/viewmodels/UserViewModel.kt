package mibnu.team.ta.viewmodels

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.iid.FirebaseInstanceId
import mibnu.team.ta.models.User
import mibnu.team.ta.utils.SingleLiveEvent
import mibnu.team.ta.utils.Utilities
import mibnu.team.ta.webservices.ApiClient
import mibnu.team.ta.webservices.WrappedResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern


class UserViewModel : ViewModel(){
    private var api = ApiClient.instanceBackend()
    private var state : SingleLiveEvent<UserState> = SingleLiveEvent()
    private var currentUser =  MutableLiveData<User>()

    private fun toast(message: String) { state.value = UserState.ShowToast(message) }

    fun getFcmToken(){
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
            if (it.isSuccessful){
                it.result?.let { result->
                    val fcmToken = result.token
                } ?: kotlin.run {
                    toast("Failed to get firebase token")
                }
            }else{
                toast("Cannot get firebase token")
            }
        }
    }

    fun login(nik: String, password: String){
        state.value = UserState.IsLoading(true)
        api.loginUser(nik, password).enqueue(object : Callback<WrappedResponse<User>> {
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                println(t.message)
                state.value = UserState.ShowToast(t.message.toString())
                state.value = UserState.IsLoading(false)
            }

            override fun onResponse(
                call: Call<WrappedResponse<User>>,
                response: Response<WrappedResponse<User>>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    body?.let {
                        if (it.status) {
                            state.value = UserState.Success(it.data!!.token!!)
                        } else {
                            state.value = UserState.ShowToast("Login gagal")
                        }
                    }
                } else {
                    state.value =
                        UserState.ShowAlert("tidak dapat masuk. Periksa password , pastikan anda sudah melakukan aktivasi")
                }
                state.value = UserState.IsLoading(false)
            }

        })
    }

    fun validate(nik: String, password: String):Boolean{
        state.value = UserState.Reset



        if (!Utilities.isValidUsername(nik)){
            state.value = UserState.Validate(nik = "jangan kosong")
            return false
        }

        if (!Utilities.isValidNik(nik)){
            state.value = UserState.Validate(nik = "nik 16 karakter")
            return false
        }
        if (!Utilities.isValidPasswords(password)) {
            state.value = UserState.Validate(password = "jangan Kosong")
            return false
        }
        if (!nik.substring(0, 4).equals("3376")){
            println(nik.substring(0, 4))
            state.value = UserState.Validate(nik = "bukan nik kota tegal")
            return false
        }

        if (!Utilities.isValidPassword(password)){
            state.value =UserState.Validate(password = "password tidak valid")
            return false
        }
        return true
    }

    fun profile(token: String){
        state.value = UserState.IsLoading(true)
        api.profile(token).enqueue(object : Callback<WrappedResponse<User>> {
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                println(t.message)
                state.value = UserState.ShowToast(t.message.toString())
                state.value = UserState.IsLoading(false)
            }

            override fun onResponse(
                call: Call<WrappedResponse<User>>,
                response: Response<WrappedResponse<User>>
            ) {
                if (response.isSuccessful) {
                    val b = response.body()
                    b?.let {
                        if (it.status) {
                            currentUser.postValue(it.data)
                            state.value = UserState.ShowToast(it.data?.nama.toString())
                        } else {
                            state.value = UserState.ShowToast("gagal")
                        }
                    }
                } else {
                    state.value = UserState.ShowToast("gagal mengambil info")
                }
                state.value = UserState.IsLoading(false)
            }
        })
    }
    fun updateProfile(token: String, password: String){
        state.value = UserState.IsLoading(true)
        api.update(token, password).enqueue(object : Callback<WrappedResponse<User>> {
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                println("onFailure :" + t.message)
                println(t.printStackTrace())
                state.value = UserState.ShowToast("onFailure :" + t.message)
            }

            override fun onResponse(
                call: Call<WrappedResponse<User>>,
                response: Response<WrappedResponse<User>>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.status!!) {
                        state.value = UserState.ShowToast("berhasil update Data")
                        state.value = UserState.Sucess
                    } else {
                        println("Update gagal")
                        state.value = UserState.ShowToast("gagal")
                    }
                } else {

                    state.value = UserState.ShowToast("gagal")
                    println("gagal")
                }
                state.value = UserState.IsLoading(false)
            }

        })

    }

    fun listenUIState()=state
    fun listenToCurrentUser() = currentUser
}




sealed class UserState{
    object  Reset : UserState()
    data class ShowAlert(var message: String) : UserState()
    data class IsLoading(var state: Boolean) : UserState()
    data class ShowToast(var message: String) : UserState()
    data class Validate(var nik: String? = null, var password: String? = null) :UserState()
    data class Success(var param: String) : UserState()
    object Sucess: UserState()
}