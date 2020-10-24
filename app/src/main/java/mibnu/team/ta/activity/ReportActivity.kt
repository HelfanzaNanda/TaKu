package mibnu.team.ta.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_report.*
import mibnu.team.ta.R
import mibnu.team.ta.models.Report
import mibnu.team.ta.utils.Utilities
import mibnu.team.ta.viewmodels.ReportState
import mibnu.team.ta.viewmodels.ReportViewModel

class ReportActivity : AppCompatActivity() {
    private lateinit var reportViewModel: ReportViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        reportViewModel = ViewModelProvider(this).get(ReportViewModel::class.java)
        reportViewModel.listenUIState().observe(this, Observer { handleUI(it) })
        reportYu()
    }

    private fun handleUI(it: ReportState) {
    when(it){
        is ReportState.ShowToast -> toast(it.message)
        is ReportState.Success -> finish()
    }

    }
    private fun reportYu(){
        submit.setOnClickListener {
            val report = isi.text.toString().trim()
            reportViewModel.reportData("${Utilities.getToken(this@ReportActivity)!!}",report)
        }
    }

    private fun toast(message: String) = Toast.makeText(this@ReportActivity, message, Toast.LENGTH_SHORT).show()
}