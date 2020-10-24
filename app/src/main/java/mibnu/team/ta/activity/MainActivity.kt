package mibnu.team.ta.activity

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.anychart.APIlib
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.enums.Align
import com.anychart.enums.LegendLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.chart_bar.*
import mibnu.team.ta.R
import mibnu.team.ta.models.Grafik
import mibnu.team.ta.models.User
import mibnu.team.ta.utils.Utilities
import mibnu.team.ta.viewmodels.GrafikViewModel
import mibnu.team.ta.viewmodels.UserViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var userViewModel : UserViewModel
    private  lateinit var grafikViewModel: GrafikViewModel
    companion object {
        const val CHANNEL_ID = "channel_bangdu"
        private const val CHANNEL_NAME= "Bangdu"
        private const val CHANNEL_DESC = "Android FCM"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Thread(Runnable {
            if (Utilities.getToken(this@MainActivity) == null) {
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
        }).start()
//        grafikViewModel = ViewModelProvider(this).get(GrafikViewModel::class.java)
//        grafikViewModel.fetchKecamatanInfo()
//        grafikViewModel.listenToKecamatanInfo().observe(this, Observer {
//            attachGrafik(it)
//        })
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        Toast.makeText(this, Utilities.getToken(this).toString(), Toast.LENGTH_LONG).show()
        Utilities.getToken(this)?.let { token -> userViewModel.profile(token) }
        userViewModel.listenToCurrentUser().observe(this, Observer { attachToUI(it) })
        dashboardMenus()
        checkPermisson()
        setupNotificationManager()
    }


    private fun attachToUI(user : User){
        tvName.text = user.nama
        tvNik.text = user.nik
    }
//    private fun attachGrafik(it:Grafik){
//
//
//        bar_chart.setProgressBar(progress_bar)
//        APIlib.getInstance().setActiveAnyChartView(bar_chart)
//        val pie = AnyChart.pie()
//        val data:MutableList<DataEntry> = mutableListOf()
//        data.add(ValueDataEntry("Margadana",it.margadana))
//        data.add(ValueDataEntry("Tegal Timur",it.tegalTimur))
//        data.add(ValueDataEntry("Tegal Selatan",it.tegalSelatan))
//        data.add(ValueDataEntry("Tegal Barat",it.tegalBarat))
//        pie.data(data)
//
//        pie.title("Hasil Pengajuan dana Kematian perkecamatan")
//        pie.labels().position("outsider")
//        pie.legend().title().enabled(true)
//        pie.legend().position("center-buttom").itemsLayout(LegendLayout.HORIZONTAL).align(Align.CENTER)
//        bar_chart.setChart(pie)
//
//
//    }

    private fun dashboardMenus(){
        kirimBerkas.setOnClickListener {
            startActivity(Intent(this, BerkasActivity::class.java))
        }
        skm.setOnClickListener {
            startActivity(Intent(this,SkmActivity::class.java))
        }
        lacakBerkas.setOnClickListener {
            startActivity(Intent(this,TrackingActivity::class.java))
        }
        card.setOnClickListener {
            startActivity(Intent(this,ProfileActivity::class.java))
        }
        bantuan.setOnClickListener {
            startSupportChat()

        }
    }
    private fun startSupportChat() {
        try {
            val headerReceiver = "hi" // Replace with your message.
            val bodyMessageFormal = " bisa bantu saya?" // Replace with your message.
            val whatsappContain = headerReceiver + bodyMessageFormal
            val trimToNumner = "+6285725261353" //10 digit number
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://wa.me/$trimToNumner/?text=$whatsappContain")
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun checkPermisson(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                Toast.makeText(this, "Aplikasi tidak berjalan tanpa izin ke kamera", Toast.LENGTH_LONG).show()
                finish()
            } else {
                ActivityCompat.requestPermissions(this,  arrayOf(Manifest.permission.CAMERA), 20)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            20 -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i("IntroAct", "Permission has been denied by user")
                } else {
                    Log.i("IntroAct", "Permission has been granted by user")
                }
            }
        }
    }

    private fun setupNotificationManager(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = CHANNEL_DESC
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }


}

