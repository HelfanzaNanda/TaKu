package mibnu.team.ta.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import coil.api.load
import com.fxn.pix.Pix
import kotlinx.android.synthetic.main.activity_berkas.*
import mibnu.team.ta.R
import mibnu.team.ta.services.UploadWorker
import mibnu.team.ta.viewmodels.BerkasState
import mibnu.team.ta.viewmodels.BerkasViewModel
import java.io.File


class BerkasActivity : AppCompatActivity() {
    private lateinit var berkasViewModel : BerkasViewModel
    private val KTP_MENINGGAL_CODE = 10
    private val KK_MENINGGAL_CODE = 11
    private val JAMKESMAS_CODE = 12
    private val KTP_WARIS_CODE = 13
    private val KK_WARIS_CODE = 14
    private val AKTA_KEMATIAN_CODE = 15
    private val PERNYATAAN_WARIS_CODE = 16
    private val INTEGRITAS_WARIS_CODE = 17
    private val BUKU_TABUNGAN_CODE = 18
    private val KTP_MENINGGAL_KEY = "ktp_meninggal"
    private val KK_MENINGGAL_KEY = "kk_meninggal"
    private val JAMKESMAS_KEY = "jamkesmas"
    private val KTP_WARIS_KEY = "ktp_waris"
    private val KK_WARIS_KEY = "kk_waris"
    private val AKTA_KEMATIAN_KEY = "akta_kematian"
    private val PERNYATAAN_WARIS_KEY = "pernyataan_ahli_waris"
    private val INTEGRITAS_WARIS_KEY = "pakta_waris"
    private val BUKU_TABUNGAN_KEY = "buku_tabungan"
    private val imageMap = mutableMapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_berkas)
        selectImgBehavior()
        berkasViewModel = ViewModelProvider(this).get(BerkasViewModel::class.java)
        berkasViewModel.listenToState().observer(this, Observer { handleUIState(it) })
        doUpload()
        checkBoxBehavior()
        checkButtonYesAcceptable()
    }

    private fun selectImgBehavior(){
        img_Ktp_Meninggal.setOnClickListener { Pix.start(this, KTP_MENINGGAL_CODE) }
        img_kk_meninggal.setOnClickListener { Pix.start(this, KK_MENINGGAL_CODE) }
        img_jamkesmas.setOnClickListener { Pix.start(this, JAMKESMAS_CODE) }
        img_ktp_waris.setOnClickListener  { Pix.start(this, KTP_WARIS_CODE) }
        img_kk_waris.setOnClickListener  { Pix.start(this, KK_WARIS_CODE) }
        img_Akta_kematian.setOnClickListener  { Pix.start(this, AKTA_KEMATIAN_CODE) }
        img_pernyataan_waris.setOnClickListener  { Pix.start(this, PERNYATAAN_WARIS_CODE) }
        img_integritas_waris.setOnClickListener  { Pix.start(this, INTEGRITAS_WARIS_CODE) }
        img_buku_tabungan.setOnClickListener {Pix.start(this,BUKU_TABUNGAN_CODE) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            KTP_MENINGGAL_CODE -> {
                if(resultCode == Activity.RESULT_OK && data != null){
                    val selectedImageUri = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                    selectedImageUri?.let{
                        berkasViewModel.addBerkas(KTP_MENINGGAL_KEY, it[0])
                        img_Ktp_Meninggal.load(File( it[0]))
                    }
                }
            }
            KK_MENINGGAL_CODE -> {
                if(resultCode == Activity.RESULT_OK && data != null){
                    val selectedImageUri = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                    selectedImageUri?.let{
                        berkasViewModel.addBerkas(KK_MENINGGAL_KEY, it[0])
                        img_kk_meninggal.load(File( it[0]))
                    }
                }
            }
            JAMKESMAS_CODE -> {
                if(resultCode == Activity.RESULT_OK && data != null){
                    val selectedImageUri = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                    selectedImageUri?.let{
                        berkasViewModel.addBerkas(JAMKESMAS_KEY, it[0])
                        img_jamkesmas.load(File( it[0]))
                    }
                }
            }
            KTP_WARIS_CODE -> {
                if(resultCode == Activity.RESULT_OK && data != null){
                    val selectedImageUri = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                    selectedImageUri?.let{
                        berkasViewModel.addBerkas(KTP_WARIS_KEY, it[0])
                        img_ktp_waris.load(File( it[0]))
                    }
                }
            }
            KK_WARIS_CODE -> {
                if(resultCode == Activity.RESULT_OK && data != null){
                    val selectedImageUri = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                    selectedImageUri?.let{
                        berkasViewModel.addBerkas(KK_WARIS_KEY, it[0])
                        img_kk_waris.load(File( it[0]))
                    }
                }
            }
            AKTA_KEMATIAN_CODE-> {
                if(resultCode == Activity.RESULT_OK && data != null){
                    val selectedImageUri = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                    selectedImageUri?.let{
                        berkasViewModel.addBerkas(AKTA_KEMATIAN_KEY, it[0])
                        img_Akta_kematian.load(File( it[0]))
                    }
                }
            }
            PERNYATAAN_WARIS_CODE -> {
                if(resultCode == Activity.RESULT_OK && data != null){
                    val selectedImageUri = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                    selectedImageUri?.let{
                        berkasViewModel.addBerkas(PERNYATAAN_WARIS_KEY, it[0])
                        img_pernyataan_waris.load(File( it[0]))
                    }
                }
            }
            INTEGRITAS_WARIS_CODE -> {
                if(resultCode == Activity.RESULT_OK && data != null){
                    val selectedImageUri = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                    selectedImageUri?.let{
                        berkasViewModel.addBerkas(INTEGRITAS_WARIS_KEY, it[0])
                        img_integritas_waris.load(File( it[0]))
                    }
                }
            }
            BUKU_TABUNGAN_CODE -> {
                if (resultCode == Activity.RESULT_OK && data != null){
                    val selectedImageUri = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                    selectedImageUri?.let{
                        berkasViewModel.addBerkas(BUKU_TABUNGAN_KEY, it[0])
                        img_buku_tabungan.load(File( it[0]))
                    }
                }
            }
//            BUKU_TABUNGAN_CODE -> {
//                if(resultCode == Activity.RESULT_OK && data != null){
//                    val selectedImageUri = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
//                    selectedImageUri?.let{
//                        berkasViewModel.addBerkas(BUKU_TABUNGAN_KEY, it[0])
//                        img_buku_tabungan.load(File( it[0]))
//                    }
//                }
//            }
        }
    }

    private fun handleUIState(it: BerkasState){
        when(it){
            is BerkasState.IsLoading -> btn_kirim.isEnabled = !it.state
            is BerkasState.ShowAlert -> showAler(it.message)
            is BerkasState.ShowToast -> toast(it.message)
            is BerkasState.Success -> finish()
        }
    }



    private fun showAler(messag : String){
        AlertDialog.Builder(this).apply {
            setMessage(messag)
            setPositiveButton(resources.getString(R.string.common_understand)){ d, _ -> d.dismiss()}
        }.show()
    }

    private fun doUpload(){
        btn_kirim.setOnClickListener {
            if(berkasViewModel.validate()){
//                berkasViewModel.upload(Utilities.getToken(this)!!)
                startWork()
                toast("Berkas akan segera diupload. Periksa notifikasi")
                finish()
            }else{
                showAler(resources.getString(R.string.validation_berkas))
            }
        }
    }

    private fun createData(): Data {
        val data = Data.Builder()
        berkasViewModel.listenToBerkas().value!!.forEach { (k, v) ->
            data.putString(k, v)
        }
        return data.build()
     }

    private fun startWork() {
        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(UploadWorker::class.java).apply {
            setInputData(createData())
        }.build()
        WorkManager.getInstance(this).enqueue(oneTimeWorkRequest)
    }


    private fun toast(m : String) = Toast.makeText(this, m, Toast.LENGTH_LONG).show()

    private fun checkButtonYesAcceptable(){
        btn_kirim.isEnabled = !(!cb_sure.isChecked)
    }

    private fun checkBoxBehavior(){
        cb_sure.setOnCheckedChangeListener{v, isChecked -> checkButtonYesAcceptable()}
    }

}
