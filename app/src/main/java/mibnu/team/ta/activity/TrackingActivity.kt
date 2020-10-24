package mibnu.team.ta.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_tracking.*

import mibnu.team.ta.R
import mibnu.team.ta.models.Tracking
import mibnu.team.ta.utils.Utilities
import mibnu.team.ta.viewmodels.TrackViewModel


class TrackingActivity : AppCompatActivity() {
    private lateinit var trackViewModel: TrackViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracking)
        trackViewModel = ViewModelProvider(this).get(TrackViewModel::class.java)
        trackViewModel.listenToDatas().observe(this, Observer { attachToUI(it) })
        Utilities.getToken(this)?.let { token -> trackViewModel.trackData(token) }
    buttonLapor()
    }

    private fun buttonLapor() {
        lapor.setOnClickListener {
            startActivity(Intent(this,ReportActivity::class.java))
        }
    }

    private fun attachToUI(track:Tracking){
        if(track.aktaKematianUrl == null){
            tracking_layout.visibility = View.VISIBLE
            empty_view.visibility = View.VISIBLE
        }
        if(track.confirmedI == null){
            tracking_layout.visibility = View.VISIBLE
            no_action.visibility = View.VISIBLE
            lapor.visibility = View.VISIBLE
        }else{
            empty_view.visibility = View.GONE
            tracking_layout.visibility = View.VISIBLE
            lapor.visibility = View.VISIBLE
            if (track.confirmedI == true){
                card1.visibility = View.VISIBLE
                val title : TextView = findViewById(R.id.konfirm1)
                val subtitle : TextView = findViewById(R.id.subkonfirm1)
                val hasil ="DiTerima"
                val subHasil = track.keterangan
                title.text=getString(R.string.PegawaiDisdukcapil)
                status.text=getString(R.string.StatusI)
                subtitle.text=subHasil
                datekon.text = track.dataKonfirmasi
            }else if(track.confirmedI == null){
                card1.visibility = View.GONE
                konfirm1.text = resources.getString(R.string.disdukcapil)
                subkonfirm1.text = resources.getString(R.string.confirmedwaiting)
            }
            else{
                card1.visibility = View.VISIBLE
                val title : TextView = findViewById(R.id.konfirm1)
                val subtitle : TextView = findViewById(R.id.subkonfirm1)
                val hasil ="Ditolak"
                val subHasil = track.keterangan
                title.text=getString(R.string.PegawaiDisdukcapil)
                status.text=getString(R.string.StatusII)
                keterangan.text=getString(R.string.keterangan)
                subtitle.text=subHasil
                datekon.text = track.dataKonfirmasi
                if (track.ket_ktp_meninggal==null){
                    ktp_meninggal.visibility  = View.GONE
                }else{
                    ktp_meninggal.visibility = View.VISIBLE
                    ktp_meninggal.text = resources.getString(R.string.ktp_meninggal)
                }
                if (track.ket_kk_meninggal==null){
                    kk_meninggal.visibility  = View.GONE
                }else{
                    kk_meninggal.visibility = View.VISIBLE
                    kk_meninggal.text = resources.getString(R.string.kk_meninggal)
                }
                if (track.ket_jamkesmas==null){
                    jamkesmas.visibility  = View.GONE
                }else{
                    jamkesmas.visibility = View.VISIBLE
                    jamkesmas.text = resources.getString(R.string.jamkesmas)
                }
                if (track.ket_ktp_waris==null){
                    ktp_waris.visibility  = View.GONE
                }else{
                    ktp_waris.visibility = View.VISIBLE
                    ktp_waris.text = resources.getString(R.string.ktp_ahli_waris)
                }
                if (track.ket_kk_waris==null){
                    kk_waris.visibility  = View.GONE
                }else{
                    kk_waris.visibility = View.VISIBLE
                    kk_waris.text = resources.getString(R.string.kk_ahli_waris)
                }
                if (track.ket_pernyataan_waris==null){
                    pernyataan_waris.visibility  = View.GONE
                }else{
                    pernyataan_waris.visibility = View.VISIBLE
                    pernyataan_waris.text = resources.getString(R.string.surat_pernyataan_waris)
                }
                if (track.ket_pakta_waris==null){
                    integritas_waris.visibility  = View.GONE
                }else{
                    integritas_waris.visibility = View.VISIBLE
                    integritas_waris.text = resources.getString(R.string.pakta_integritas_ahli_waris)
                }
                if (track.ket_buku_tabungan==null){
                    buku_tabungan.visibility  = View.GONE
                }else{
                    buku_tabungan.visibility = View.VISIBLE
                    buku_tabungan.text= resources.getString(R.string.buku_tabungan)
                }
            }

            if(track.confirmedII == true){
                card2.visibility = View.VISIBLE
                konfirm2.text=getString(R.string.PegawaiBakeuda)
                status1.text=getString(R.string.StatusI)
                subkonfirm2.text=track.keteranganII
                datetaf.text = track.dataTransfer
            }else if(track.confirmedII == null){
                card2.visibility = View.GONE
                konfirm2.text = "Dinkes"
                subkonfirm2.text = resources.getString(R.string.confirmedwaiting)
            }else{
                card2.visibility = View.VISIBLE
                val title : TextView = findViewById(R.id.konfirm2)
                val subtitle : TextView = findViewById(R.id.subkonfirm2)
                val hasil ="Ditolak"
                val subHasil = track.keteranganII
                title.text=getString(R.string.PegawaiBakeuda)
                status1.text=getString(R.string.StatusII)
                keterangan1.text=getString(R.string.keterangan)
                subtitle.text=subHasil
                datetaf.text = track.dataTransfer
                if (track.ket_ktp_meninggal==null){
                    ktp_meninggal1.visibility  = View.GONE
                }else{
                    ktp_meninggal1.visibility = View.VISIBLE
                    ktp_meninggal1.text = resources.getString(R.string.ktp_meninggal)
                }
                if (track.ket_kk_meninggal==null){
                    kk_meninggal1.visibility  = View.GONE
                }else{
                    kk_meninggal1.visibility = View.VISIBLE
                    kk_meninggal1.text = resources.getString(R.string.kk_meninggal)
                }
                if (track.ket_jamkesmas==null){
                    jamkesmas1.visibility  = View.GONE
                }else{
                    jamkesmas1.visibility = View.VISIBLE
                    jamkesmas1.text = resources.getString(R.string.jamkesmas)
                }
                if (track.ket_ktp_waris==null){
                    ktp_waris1.visibility  = View.GONE
                }else{
                    ktp_waris1.visibility = View.VISIBLE
                    ktp_waris1.text = resources.getString(R.string.ktp_ahli_waris)
                }
                if (track.ket_kk_waris==null){
                    kk_waris1.visibility  = View.GONE
                }else{
                    kk_waris1.visibility = View.VISIBLE
                    kk_waris1.text = resources.getString(R.string.kk_ahli_waris)
                }
                if (track.ket_pernyataan_waris==null){
                    pernyataan_waris1.visibility  = View.GONE
                }else{
                    pernyataan_waris1.visibility = View.VISIBLE
                    pernyataan_waris1.text = resources.getString(R.string.surat_pernyataan_waris)
                }
                if (track.ket_pakta_waris==null){
                    integritas_waris1.visibility  = View.GONE
                }else{
                    integritas_waris1.visibility = View.VISIBLE
                    integritas_waris1.text = resources.getString(R.string.pakta_integritas_ahli_waris)
                }
                if (track.ket_buku_tabungan==null){
                    buku_tabungan1.visibility  = View.GONE
                }else{
                    buku_tabungan1.visibility = View.VISIBLE
                    buku_tabungan1.text= resources.getString(R.string.buku_tabungan)
                }
            }

        }



    }
//    private fun handluei(it: TrackingState){
//        when(it){
//            is TrackingState.ShowToast -> Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
//            is TrackingState.Success->finish()
//        }
//    }
//    private fun fill(d:Tracking){
//        let {
//            if (d.confirmedI==true){
//
//            }
//        }
//    }

}
