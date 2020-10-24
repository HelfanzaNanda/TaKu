package mibnu.team.ta.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_skm.*
import mibnu.team.ta.R
import mibnu.team.ta.utils.Utilities
import mibnu.team.ta.viewmodels.DataState
import mibnu.team.ta.viewmodels.SurveyViewModel

class SkmActivity : AppCompatActivity() {
    private lateinit var surveyViewModel: SurveyViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skm)
        surveyViewModel = ViewModelProvider(this).get(SurveyViewModel::class.java)
        surveyViewModel.listenToUIState().observer(this, Observer {
            handleui(it)
        })
        btnSurvey()
    }

    private fun handleui(it: DataState) {
        when(it){
            is DataState.ShowToast -> Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
            is DataState.Success  -> finish()
        }
    }

    private fun btnSurvey(){
        baik.setOnClickListener {
            var nilai = 3
            surveyViewModel.surveys("${Utilities.getToken(this@SkmActivity)!!}",nilai)
        }

        cukup.setOnClickListener {
            var nilai = 2
            surveyViewModel.surveys("${Utilities.getToken(this@SkmActivity)!!}",nilai)
        }

        kurang.setOnClickListener {
            var nilai = 1
            surveyViewModel.surveys("${Utilities.getToken(this@SkmActivity)!!}",nilai)
        }

    }

}
