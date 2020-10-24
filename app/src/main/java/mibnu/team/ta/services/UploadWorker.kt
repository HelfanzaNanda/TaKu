package mibnu.team.ta.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import mibnu.team.ta.R
import mibnu.team.ta.activity.MainActivity
import mibnu.team.ta.models.Berkas
import mibnu.team.ta.utils.Utilities
import mibnu.team.ta.webservices.ApiClient
import mibnu.team.ta.webservices.WrappedResponse
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class UploadWorker(var context : Context, workerParams : WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val api = ApiClient.instanceBackend()
        val multipartTypedOutput = arrayOfNulls<MultipartBody.Part>(Utilities.listKey.size)
        var i = 0
        for(j in Utilities.listKey){
            val data = inputData.getString(j)!!
            val file = File(data)
            val body: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
            multipartTypedOutput[i] = MultipartBody.Part.createFormData(j, file.name, body)
            i++
        }
        api.uploadBerkas(Utilities.getToken(context)!!, multipartTypedOutput).enqueue(object : Callback<WrappedResponse<Berkas>>{
            override fun onFailure(call: Call<WrappedResponse<Berkas>>, t: Throwable) {
                showNotification("Bangdu", "Kesalahan saat mengupload berkas (${t.message}")
                Result.failure()
            }

            override fun onResponse(call: Call<WrappedResponse<Berkas>>, response: Response<WrappedResponse<Berkas>>) {
                if (response.isSuccessful){
                    val x = response.body()
                    x?.let {
                        if(x.status){
                            println("SUccess notification will send")
                            showNotification("Bangdu", "Berkas berhasil diupload")
                        }else{
                            println("Failed notid will send")
                            showNotification("Bangdu", "Berkas gagal diupload :(")
                            Result.failure()
                        }
                    }
                }else{
                    println(response.code())
                    println(response.message())
                    println(response.errorBody())
                    println("Failed notification will be send")
                    showNotification("Bangdu", "Gagal mengupload berkas")
                }
            }
        })
        return Result.success()
    }

    private fun showNotification(title: String, task: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                "upload_berkas", "upload_berkas", NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
//        val intent = Intent(context, MainActivity::class.java)
//        val pendingIntent = PendingIntent.getActivity(
//            context, id, intent,
//            PendingIntent.FLAG_UPDATE_CURRENT
//        )

        val notification = NotificationCompat.Builder(applicationContext, "upload_berkas")
            .setContentTitle(title)
            .setContentText(task)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setSmallIcon(R.mipmap.ic_launcher)
        notificationManager.notify(1, notification.build())
    }

}